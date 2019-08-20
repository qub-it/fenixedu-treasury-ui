package org.fenixedu.treasury.ui.document.payments.onlinepaymentsgateway;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.FileUtils;
import org.fenixedu.bennu.spring.portal.SpringFunctionality;
import org.fenixedu.onlinepaymentsgateway.api.PaymentStateBean;
import org.fenixedu.onlinepaymentsgateway.api.SIBSOnlinePaymentsGatewayService;
import org.fenixedu.onlinepaymentsgateway.exceptions.OnlinePaymentsGatewayCommunicationException;
import org.fenixedu.onlinepaymentsgateway.sibs.sdk.PaymentType;
import org.fenixedu.treasury.domain.debt.DebtAccount;
import org.fenixedu.treasury.domain.document.SettlementNote;
import org.fenixedu.treasury.domain.exceptions.TreasuryDomainException;
import org.fenixedu.treasury.domain.paymentcodes.PaymentReferenceCode;
import org.fenixedu.treasury.domain.paymentcodes.SibsTransactionDetail;
import org.fenixedu.treasury.domain.sibsonlinepaymentsgateway.MbwayPaymentRequest;
import org.fenixedu.treasury.domain.sibsonlinepaymentsgateway.MbwayTransaction;
import org.fenixedu.treasury.domain.sibsonlinepaymentsgateway.SibsOnlinePaymentsGateway;
import org.fenixedu.treasury.domain.sibsonlinepaymentsgateway.SibsOnlinePaymentsGatewayLog;
import org.fenixedu.treasury.ui.TreasuryBaseController;
import org.fenixedu.treasury.ui.TreasuryController;
import org.fenixedu.treasury.ui.document.forwardpayments.ForwardPaymentController;
import org.fenixedu.treasury.util.TreasuryConstants;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import pt.ist.fenixframework.Atomic;
import pt.ist.fenixframework.Atomic.TxMode;
import pt.ist.fenixframework.FenixFramework;

@SpringFunctionality(title = "label.OnlinePaymentsGatewayWebhooksController.title", app = TreasuryController.class)
@RequestMapping(OnlinePaymentsGatewayWebhooksController.CONTROLLER_URL)
public class OnlinePaymentsGatewayWebhooksController extends TreasuryBaseController {

    private static final Logger logger = LoggerFactory.getLogger(ForwardPaymentController.class);

    public static final String CONTROLLER_URL = "/treasury/document/payments/onlinepaymentsgateway";

    private static final String NOTIFICATION_URI = "/notification";
    public static final String NOTIFICATION_URL = CONTROLLER_URL + NOTIFICATION_URI;

    @RequestMapping(path = NOTIFICATION_URI, method = RequestMethod.POST)
    @ResponseBody
    public void notification(final HttpServletRequest request, final HttpServletResponse response) {

        final SibsOnlinePaymentsGatewayLog log = createLog();

        try {

            String notificationInitializationVector = SIBSOnlinePaymentsGatewayService.notificationInitializationVector(request);
            String notificationAuthenticationTag = SIBSOnlinePaymentsGatewayService.notificationAuthenticationTag(request);
            String notificationEncryptedPayload = SIBSOnlinePaymentsGatewayService.notificationEncryptedPayload(request);

            FenixFramework.atomic(() -> {
                log.saveWebhookNotificationData(notificationInitializationVector, notificationAuthenticationTag,
                        notificationEncryptedPayload);
            });

            PaymentStateBean bean = SibsOnlinePaymentsGateway.findAll().iterator().next().handleWebhookNotificationRequest(
                    notificationInitializationVector, notificationAuthenticationTag, notificationEncryptedPayload);

            FenixFramework.atomic(() -> {
                log.logRequestReceiveDateAndData(bean.getTransactionId(), bean.isOperationSuccess(), bean.isPaid(),
                        bean.getPaymentGatewayResultCode(), bean.getPaymentGatewayResultDescription());
                log.saveRequestAndResponsePayload(bean.getRequestLog(), null);
            });

            if (!bean.isOperationSuccess()) {
                throw new TreasuryDomainException(
                        "error.OnlinePaymentsGatewayWebhooksController.notificationBean.operation.insuccess");
            }

            if (!"PAYMENT".equals(bean.getNotificationType())) {
                // Not payment, ignore
                response.setStatus(HttpServletResponse.SC_OK);
                return;
            }

            FenixFramework.atomic(() -> {
                log.saveMerchantTransactionId(bean.getMerchantTransactionId());
                log.saveReferenceId(bean.getReferencedId());
            });

            // Find payment code
            final Optional<PaymentReferenceCode> referenceCodeOptional =
                    PaymentReferenceCode.findUniqueBySibsReferenceId(bean.getReferencedId());

            final Optional<MbwayPaymentRequest> mbwayPaymentRequestOptional =
                    MbwayPaymentRequest.findUniqueBySibsMerchantTransactionId(bean.getMerchantTransactionId());

            if (referenceCodeOptional.isPresent()) {
                if (PaymentType.PA.name().equals(bean.getPaymentType())) {
                    // Payment reference code pre authorization (creation of reference code)
                    response.setStatus(HttpServletResponse.SC_OK);
                    return;
                } else if(!PaymentType.RC.name().equals(bean.getPaymentType())) {
                    throw new TreasuryDomainException("error.OnlinePaymentsGatewayWebhooksController.unrecognized.payment.type.for.payment.reference.code");
                }

                if (!bean.isPaid()) {
                    throw new TreasuryDomainException(
                            "error.OnlinePaymentsGatewayWebhooksController.notificationBean.not.paid.check");
                }
                
                final PaymentReferenceCode paymentReferenceCode = referenceCodeOptional.get();

                processPaymentReferenceCodeTransaction(log, bean, paymentReferenceCode);
            } else if (mbwayPaymentRequestOptional.isPresent()) {

                if(!PaymentType.DB.name().equals(bean.getPaymentType())) {
                    throw new TreasuryDomainException("error.OnlinePaymentsGatewayWebhooksController.unrecognized.payment.type.for.mbway.payment.request");
                }
                
                if (!bean.isPaid()) {
                    throw new TreasuryDomainException(
                            "error.OnlinePaymentsGatewayWebhooksController.notificationBean.not.paid.check");
                }

                final MbwayPaymentRequest mbwayPaymentRequest = mbwayPaymentRequestOptional.get();
                
                processMbwayTransaction(log, bean, mbwayPaymentRequest);
            } else {
                throw new TreasuryDomainException(
                        "error.OnlinePaymentsGatewayWebhooksController.notificationBean.paymentReferenceCode.not.found.by.referenceId");
            }
            
            response.setStatus(HttpServletResponse.SC_OK);
        } catch (Exception e) {
            if (log != null) {
                FenixFramework.atomic(() -> {
                    log.markExceptionOccuredAndSaveLog(e);
                });

                if (e instanceof OnlinePaymentsGatewayCommunicationException) {
                    final OnlinePaymentsGatewayCommunicationException oe = (OnlinePaymentsGatewayCommunicationException) e;
                    FenixFramework.atomic(() -> {
                        log.saveRequestAndResponsePayload(oe.getRequestLog(), oe.getResponseLog());
                    });
                }
            }

            logger.error(e.getLocalizedMessage(), e);

            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }

    private void processMbwayTransaction(final SibsOnlinePaymentsGatewayLog log, PaymentStateBean bean,
            final MbwayPaymentRequest mbwayPaymentRequest) {
        if (!bean.getMerchantTransactionId().equals(mbwayPaymentRequest.getSibsMerchantTransactionId())) {
            throw new TreasuryDomainException("error.OnlinePaymentsGatewayWebhooksController.merchantTransactionId.not.equal");
        }

        FenixFramework.atomic(() -> {
            final SibsOnlinePaymentsGateway sibsOnlinePaymentsGateway = mbwayPaymentRequest.getSibsOnlinePaymentsGateway();
            final DebtAccount debtAccount = mbwayPaymentRequest.getDebtAccount();

            log.associateSibsOnlinePaymentGatewayAndDebtAccount(sibsOnlinePaymentsGateway, debtAccount);
        });

        final BigDecimal amount = bean.getAmount();
        final DateTime paymentDate = bean.getPaymentDate();

        FenixFramework.atomic(() -> {
            log.savePaymentInfo(amount, paymentDate);
        });

        if (amount == null || !TreasuryConstants.isPositive(amount)) {
            throw new TreasuryDomainException("error.OnlinePaymentsGatewayWebhooksController.invalid.amount");
        }

        if (paymentDate == null) {
            throw new TreasuryDomainException("error.OnlinePaymentsGatewayWebhooksController.invalid.payment.date");
        }

        if (MbwayTransaction.isTransactionProcessingDuplicate(bean.getTransactionId())) {
            FenixFramework.atomic(() -> {
                log.markAsDuplicatedTransaction();
            });
        } else {
            final Set<SettlementNote> settlementNotes = mbwayPaymentRequest.processPayment("unknown", amount, paymentDate,
                    bean.getTransactionId(), bean.getMerchantTransactionId());

            FenixFramework.atomic(() -> {
                log.markSettlementNotesCreated(settlementNotes);
            });
        }
    }

    private void processPaymentReferenceCodeTransaction(final SibsOnlinePaymentsGatewayLog log, PaymentStateBean bean,
            final PaymentReferenceCode paymentReferenceCode) {        
        if (!bean.getMerchantTransactionId().equals(paymentReferenceCode.getSibsMerchantTransactionId())) {
            throw new TreasuryDomainException("error.OnlinePaymentsGatewayWebhooksController.merchantTransactionId.not.equal");
        }

        FenixFramework.atomic(() -> {
            final SibsOnlinePaymentsGateway sibsOnlinePaymentsGateway =
                    paymentReferenceCode.getPaymentCodePool().getSibsOnlinePaymentsGateway();
            final DebtAccount debtAccount = paymentReferenceCode.getTargetPayment().getDebtAccount();

            log.associateSibsOnlinePaymentGatewayAndDebtAccount(sibsOnlinePaymentsGateway, debtAccount);
            log.setPaymentCode(paymentReferenceCode.getReferenceCode());
        });

        final BigDecimal amount = bean.getAmount();
        final DateTime paymentDate = bean.getPaymentDate();

        FenixFramework.atomic(() -> {
            log.savePaymentInfo(amount, paymentDate);
        });

        if (amount == null || !TreasuryConstants.isPositive(amount)) {
            throw new TreasuryDomainException("error.OnlinePaymentsGatewayWebhooksController.invalid.amount");
        }

        if (paymentDate == null) {
            throw new TreasuryDomainException("error.OnlinePaymentsGatewayWebhooksController.invalid.payment.date");
        }

        if (SibsTransactionDetail.isReferenceProcessingDuplicate(paymentReferenceCode.getReferenceCode(),
                paymentReferenceCode.getPaymentCodePool().getEntityReferenceCode(), paymentDate)) {
            FenixFramework.atomic(() -> {
                log.markAsDuplicatedTransaction();
            });

        } else {
            final Set<SettlementNote> settlementNotes = paymentReferenceCode.processPayment("unknown", amount, paymentDate,
                    bean.getTransactionId(), bean.getMerchantTransactionId(), new DateTime(), null);
            FenixFramework.atomic(() -> {
                log.markSettlementNotesCreated(settlementNotes);
            });
        }
    }

    @Atomic(mode = TxMode.WRITE)
    private SibsOnlinePaymentsGatewayLog createLog() {
        return SibsOnlinePaymentsGatewayLog.createLogForWebhookNotification();
    }

}
