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
import org.fenixedu.treasury.domain.debt.DebtAccount;
import org.fenixedu.treasury.domain.document.SettlementNote;
import org.fenixedu.treasury.domain.exceptions.TreasuryDomainException;
import org.fenixedu.treasury.domain.paymentcodes.PaymentReferenceCode;
import org.fenixedu.treasury.domain.paymentcodes.SibsTransactionDetail;
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
    public String notification(final HttpServletRequest request, final HttpServletResponse response) {

        final SibsOnlinePaymentsGatewayLog log = createLog();

        try {

            String notificationInitializationVector = SIBSOnlinePaymentsGatewayService.notificationInitializationVector(request);
            String notificationAuthenticationTag = SIBSOnlinePaymentsGatewayService.notificationAuthenticationTag(request);
            String notificationEncryptedPayload = SIBSOnlinePaymentsGatewayService.notificationEncryptedPayload(request);

//            final String notificationInitializationVector = "7C6899AD9068313EB66AB7E4";
//            final String notificationAuthenticationTag = "FC43634E2923BE55D2B9E3D29E75F4D5";
//            final String notificationEncryptedPayload = FileUtils
//                    .readFileToString(new java.io.File("/home/anilmamede/Downloads/SIBS_WEBHOOK_NOTIFICATIONS/file1.txt"));

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
                return null;
            }

            if (!bean.isPaid()) {
                throw new TreasuryDomainException(
                        "error.OnlinePaymentsGatewayWebhooksController.notificationBean.not.paid.check");
            }

            final String sibsMerchantTransactionId = bean.getMerchantTransactionId();
            final String sibsReferenceId = bean.getReferencedId();

            FenixFramework.atomic(() -> {
                log.saveMerchantTransactionId(sibsMerchantTransactionId);
                log.saveReferenceId(sibsReferenceId);
            });
            
            // Find payment code
            final Optional<PaymentReferenceCode> referenceCodeOptional =
                    PaymentReferenceCode.findUniqueBySibsReferenceId(sibsReferenceId);
            if (!referenceCodeOptional.isPresent()) {
                throw new TreasuryDomainException(
                        "error.OnlinePaymentsGatewayWebhooksController.notificationBean.paymentReferenceCode.not.found.by.referenceId");
            }

            final PaymentReferenceCode paymentReferenceCode = referenceCodeOptional.get();
            if (!sibsMerchantTransactionId.equals(paymentReferenceCode.getSibsMerchantTransactionId())) {
                throw new TreasuryDomainException(
                        "error.OnlinePaymentsGatewayWebhooksController.merchantTransactionId.not.equal");
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

                // Payment already registered, skip
                response.setStatus(HttpServletResponse.SC_OK);
                return null;
            }

            final Set<SettlementNote> settlementNotes = paymentReferenceCode.processPayment("unknown", amount, paymentDate,
                    bean.getTransactionId(), sibsMerchantTransactionId, new DateTime(), null);
            FenixFramework.atomic(() -> {
                log.markSettlementNotesCreated(settlementNotes);
            });

            response.setStatus(HttpServletResponse.SC_OK);
            return null;
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
            return null;
        }

    }

    @Atomic(mode = TxMode.WRITE)
    private SibsOnlinePaymentsGatewayLog createLog() {
        return SibsOnlinePaymentsGatewayLog.createLogForWebhookNotification();
    }

}
