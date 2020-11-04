package org.fenixedu.treasury.ui.document.forwardpayments.implementations;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.fenixedu.bennu.spring.portal.SpringFunctionality;
import org.fenixedu.treasury.domain.forwardpayments.ForwardPaymentRequest;
import org.fenixedu.treasury.domain.forwardpayments.exceptions.ForwardPaymentAlreadyPayedException;
import org.fenixedu.treasury.domain.forwardpayments.implementations.IForwardPaymentController;
import org.fenixedu.treasury.domain.forwardpayments.implementations.IForwardPaymentPlatformService;
import org.fenixedu.treasury.domain.forwardpayments.payline.PaylineConfiguration;
import org.fenixedu.treasury.ui.TreasuryBaseController;
import org.fenixedu.treasury.ui.TreasuryController;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.google.common.base.Strings;

import pt.ist.fenixframework.Atomic;

@SpringFunctionality(app = TreasuryController.class, title = "label.title.paylineReturnForwardPayment", accessGroup = "logged")
@RequestMapping(PaylineController.CONTROLLER_URL)
public class PaylineController extends TreasuryBaseController implements IForwardPaymentController {

    public static final String CONTROLLER_URL = "/treasury/document/forwardpayments/payline";

    @Override
    public String processforwardpayment(ForwardPaymentRequest forwardPayment, Object model, HttpServletResponse response,
            HttpSession session) {
        ((Model) model).addAttribute("forwardPaymentConfiguration", forwardPayment.getDigitalPaymentPlatform());

        try {
            final PaylineConfiguration paylineImplementation = (PaylineConfiguration) forwardPayment.getDigitalPaymentPlatform();
            final boolean paylineSucess =
                    paylineImplementation.doWebPayment(forwardPayment, readReturnForwardPaymentUrl(), session);

            if (!paylineSucess) {
                return String.format("redirect:%s", forwardPayment.getForwardPaymentInsuccessUrl());
            }

            return "redirect:" + forwardPayment.getPaylineRedirectUrl();
        } catch (Exception e) {
            e.printStackTrace();

            rejectPaymentAndLogException(forwardPayment, "processforwardpayment", e.getMessage(), e.getLocalizedMessage(), e);
            return String.format("redirect:%s", forwardPayment.getForwardPaymentInsuccessUrl());
        }
    }

    private static final String RETURN_FORWARD_PAYMENT_URI = "/returnforwardpayment";
    public static final String RETURN_FORWARD_PAYMENT_URL = CONTROLLER_URL + RETURN_FORWARD_PAYMENT_URI;

    protected String readReturnForwardPaymentUrl() {
        return RETURN_FORWARD_PAYMENT_URL;
    }

    @RequestMapping(value = RETURN_FORWARD_PAYMENT_URI + "/{forwardPaymentId}/{action}/{urlChecksum}", method = RequestMethod.GET)
    public String returnforwardpayment(@PathVariable("forwardPaymentId") ForwardPaymentRequest forwardPayment,
            @PathVariable("action") String action, @PathVariable("urlChecksum") String urlChecksum,
            @RequestParam Map<String, String> responseData, Model model, HttpServletResponse response) {
        model.addAttribute("forwardPaymentConfiguration", forwardPayment.getDigitalPaymentPlatform());

        String operationCode = String.format("%s[%s]", "returnforwardpayment", action);
        try {

            // verify url checksum
            if (Strings.isNullOrEmpty(urlChecksum) || !forwardPayment.getReturnForwardPaymentUrlChecksum().equals(urlChecksum)) {
                rejectPayment(forwardPayment, operationCode, "INVALID_CHECKSUM", "Invalid checksum");
                return String.format("redirect:%s", forwardPayment.getForwardPaymentInsuccessUrl());
            }

            final PaylineConfiguration paylineImplementation = (PaylineConfiguration) forwardPayment.getDigitalPaymentPlatform();
            boolean success = paylineImplementation.processPayment(forwardPayment, action);

            if (success) {
                return String.format("redirect:%s", forwardPayment.getForwardPaymentSuccessUrl());
            }

            return String.format("redirect:%s", forwardPayment.getForwardPaymentInsuccessUrl());
        } catch (final ForwardPaymentAlreadyPayedException e) {
            addErrorMessage(e.getLocalizedMessage(), model);

            return String.format("redirect:%s", forwardPayment.getForwardPaymentInsuccessUrl());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Atomic
    private void rejectPayment(ForwardPaymentRequest forwardPayment, String operationCode, String statusCode,
            String statusMessage) {
        forwardPayment.reject(operationCode, statusCode, statusMessage, "", "");
    }

    @Atomic
    private void rejectPaymentAndLogException(ForwardPaymentRequest forwardPayment, String operationCode, String errorCode,
            String errorMessage, Exception e) {
        forwardPayment.reject(operationCode, errorCode, errorMessage, null, null).logException(e);
    }

    public static Map<Class<IForwardPaymentPlatformService>, Class<IForwardPaymentController>> CONTROLLER_MAP = new HashMap<>();

    public static void registerForwardPaymentController(Class<IForwardPaymentPlatformService> implementationClass,
            Class<IForwardPaymentController> controllerClass) {
        CONTROLLER_MAP.put(implementationClass, controllerClass);
    }

    public static IForwardPaymentController getForwardPaymentController(final ForwardPaymentRequest forwardPayment) {
        return new PaylineController();
    }

}
