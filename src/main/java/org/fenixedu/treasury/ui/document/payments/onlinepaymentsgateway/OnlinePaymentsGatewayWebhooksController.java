package org.fenixedu.treasury.ui.document.payments.onlinepaymentsgateway;

import java.util.Map;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.fenixedu.bennu.spring.portal.SpringFunctionality;
import org.fenixedu.onlinepaymentsgateway.api.PaymentStateBean;
import org.fenixedu.onlinepaymentsgateway.api.SIBSOnlinePaymentsGatewayService;
import org.fenixedu.onlinepaymentsgateway.exceptions.OnlinePaymentsGatewayCommunicationException;
import org.fenixedu.onlinepaymentsgateway.sibs.sdk.PaymentType;
import org.fenixedu.onlinepaymentsgateway.util.Decryption;
import org.fenixedu.treasury.domain.exceptions.TreasuryDomainException;
import org.fenixedu.treasury.domain.forwardpayments.ForwardPayment;
import org.fenixedu.treasury.domain.paymentcodes.PaymentReferenceCode;
import org.fenixedu.treasury.domain.sibsonlinepaymentsgateway.MbwayPaymentRequest;
import org.fenixedu.treasury.domain.sibsonlinepaymentsgateway.SibsOnlinePaymentsGateway;
import org.fenixedu.treasury.domain.sibsonlinepaymentsgateway.SibsOnlinePaymentsGatewayLog;
import org.fenixedu.treasury.ui.TreasuryBaseController;
import org.fenixedu.treasury.ui.TreasuryController;
import org.fenixedu.treasury.ui.document.forwardpayments.ForwardPaymentController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import pt.ist.fenixframework.Atomic;
import pt.ist.fenixframework.Atomic.TxMode;
import pt.ist.fenixframework.FenixFramework;

@SpringFunctionality(title = "label.OnlinePaymentsGatewayWebhooksController.title", app = TreasuryController.class)
@RequestMapping(OnlinePaymentsGatewayWebhooksController.CONTROLLER_URL)
public class OnlinePaymentsGatewayWebhooksController extends TreasuryBaseController {

    private static final Logger logger = LoggerFactory.getLogger(OnlinePaymentsGatewayWebhooksController.class);

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
            
            
            if(isTestPayloadForWebhookActivation(notificationInitializationVector, notificationAuthenticationTag,
                    notificationEncryptedPayload)) {
                response.setStatus(HttpServletResponse.SC_OK);
                return;
            }

            PaymentStateBean bean = SibsOnlinePaymentsGateway.findAll().iterator().next().handleWebhookNotificationRequest(
                    notificationInitializationVector, notificationAuthenticationTag, notificationEncryptedPayload);

            FenixFramework.atomic(() -> {
                log.logRequestReceiveDateAndData(bean.getTransactionId(), bean.isOperationSuccess(), bean.isPaid(),
                        bean.getPaymentGatewayResultCode(), bean.getPaymentGatewayResultDescription());
                log.saveRequestAndResponsePayload(bean.getRequestLog(), null);
            });

            if (!bean.isOperationSuccess()) {
                // Operation with insucess, return ok just to not retry the transaction
                response.setStatus(HttpServletResponse.SC_OK);
                return;
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

            if (PaymentType.PA.name().equals(bean.getPaymentType())) {
                // Sibs reference code request
                final Optional<PaymentReferenceCode> referenceCodeOptional = bean.getTransactionId() != null ? 
                        PaymentReferenceCode.findUniqueBySibsReferenceId(bean.getTransactionId()) : Optional.empty();
                if (referenceCodeOptional.isPresent()) {
                    // Payment reference code pre authorization (creation of reference code)
                    response.setStatus(HttpServletResponse.SC_OK);
                    return;
                }
            }
            
            // Find payment code
            final Optional<PaymentReferenceCode> referenceCodeOptional = bean.getReferencedId() != null ? 
                    PaymentReferenceCode.findUniqueBySibsReferenceId(bean.getReferencedId()) : Optional.empty();

            final Optional<MbwayPaymentRequest> mbwayPaymentRequestOptional =
                    MbwayPaymentRequest.findUniqueBySibsMerchantTransactionId(bean.getMerchantTransactionId());

            if (referenceCodeOptional.isPresent()) {
                if(!PaymentType.RC.name().equals(bean.getPaymentType())) {
                    throw new TreasuryDomainException("error.OnlinePaymentsGatewayWebhooksController.unrecognized.payment.type.for.payment.reference.code");
                }

                if (!bean.isPaid()) {
                    throw new TreasuryDomainException(
                            "error.OnlinePaymentsGatewayWebhooksController.notificationBean.not.paid.check");
                }
                
                final PaymentReferenceCode paymentReferenceCode = referenceCodeOptional.get();

                paymentReferenceCode.processPaymentReferenceCodeTransaction(log, bean);
            } else if (mbwayPaymentRequestOptional.isPresent()) {

                if(!PaymentType.DB.name().equals(bean.getPaymentType())) {
                    throw new TreasuryDomainException("error.OnlinePaymentsGatewayWebhooksController.unrecognized.payment.type.for.mbway.payment.request");
                }
                
                if (bean.isPaid()) {
                    final MbwayPaymentRequest mbwayPaymentRequest = mbwayPaymentRequestOptional.get();
                    
                    mbwayPaymentRequest.processMbwayTransaction(log, bean);
                }

                response.setStatus(HttpServletResponse.SC_OK);
                return;
            } else {
                final boolean isForwardPayment = ForwardPayment.findAll()
                    .filter(p -> p.getSibsMerchantTransactionId() != null)
                    .filter(p -> p.getSibsMerchantTransactionId().equals(bean.getMerchantTransactionId()))
                    .findFirst().isPresent();
                
                if(!isForwardPayment) {
                    throw new TreasuryDomainException(
                            "error.OnlinePaymentsGatewayWebhooksController.notificationBean.paymentReferenceCode.not.found.by.referenceId");
                }
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

    private boolean isTestPayloadForWebhookActivation(final String notificationInitializationVector, final String notificationAuthenticationTag, final String notificationEncryptedPayload) {
        try {
            
            String aesKey = SibsOnlinePaymentsGateway.findAll().iterator().next().getAesKey();
            
            Decryption notification =
                    new Decryption(aesKey, notificationInitializationVector, notificationAuthenticationTag, notificationEncryptedPayload);

            String decryptedPayload = notification.decryptPayload();

            ObjectMapper mapper = new ObjectMapper();
            
            Map<String, String> map = mapper.readValue(decryptedPayload, new TypeReference<Map<String, Object>>(){});
            
            if(map.containsKey("type") && map.containsKey("action")) {
                String typeValue = map.get("type");
                String actionValue = map.get("action");
                
                return "test".equals(typeValue) && "webhook activation".equals(actionValue);
            }
            
            return false;
        } catch (Exception e) {
            return false;
        }
    }

    @Atomic(mode = TxMode.WRITE)
    private SibsOnlinePaymentsGatewayLog createLog() {
        return SibsOnlinePaymentsGatewayLog.createLogForWebhookNotification();
    }

}
