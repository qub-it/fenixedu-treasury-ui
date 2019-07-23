package org.fenixedu.treasury.ui.document.payments.onlinepaymentsgateway;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.fenixedu.bennu.spring.portal.SpringFunctionality;
import org.fenixedu.onlinepaymentsgateway.api.SIBSOnlinePaymentsGatewayService;
import org.fenixedu.onlinepaymentsgateway.sibs.sdk.NotificationBean;
import org.fenixedu.treasury.domain.debt.DebtAccount;
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

import pt.ist.fenixframework.Atomic;
import pt.ist.fenixframework.FenixFramework;
import pt.ist.fenixframework.Atomic.TxMode;

@SpringFunctionality(title="label.OnlinePaymentsGatewayWebhooksController.title", app=TreasuryController.class)
@RequestMapping(OnlinePaymentsGatewayWebhooksController.CONTROLLER_URL)
public class OnlinePaymentsGatewayWebhooksController extends TreasuryBaseController {

    private static final Logger logger = LoggerFactory.getLogger(ForwardPaymentController.class);
    
    public static final String CONTROLLER_URL = "/treasury/document/payments/onlinepaymentsgateway";

    private static final String NOTIFICATION_URI = "/notification";
    public static final String NOTIFICATION_URL = CONTROLLER_URL + NOTIFICATION_URI;
    
    @RequestMapping(path=NOTIFICATION_URI, method=RequestMethod.POST)
    @ResponseBody
    public String notify(final HttpServletRequest request, final HttpServletResponse response) {
        
        final SibsOnlinePaymentsGatewayLog log = createLog();
        
        try {

            String notificationInitializationVector = SIBSOnlinePaymentsGatewayService.notificationInitializationVector(request);
            String notificationAuthenticationTag = SIBSOnlinePaymentsGatewayService.notificationAuthenticationTag(request);
            String notificationEncryptedPayload = SIBSOnlinePaymentsGatewayService.notificationEncryptedPayload(request);
            
            FenixFramework.atomic(() -> {
                log.saveWebhookNotificationData(notificationInitializationVector, notificationAuthenticationTag, notificationEncryptedPayload);
            });
            
            NotificationBean notificationBean = null; // Invoke online payments gateway library
            
            response.setStatus(HttpServletResponse.SC_OK);
        } catch(Exception e) {
            if(log != null) {
                FenixFramework.atomic(() -> {
                    log.markExceptionOccuredAndSaveLog(e);
                });
            }
            
            logger.error(e.getLocalizedMessage(), e);
            
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
        
        return null;
    }
    
    @Atomic(mode = TxMode.WRITE)
    private SibsOnlinePaymentsGatewayLog createLog() {
        return SibsOnlinePaymentsGatewayLog.createLogForWebhookNotification();
    }
    
}
