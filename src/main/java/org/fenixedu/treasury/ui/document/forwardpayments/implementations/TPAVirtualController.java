package org.fenixedu.treasury.ui.document.forwardpayments.implementations;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.fenixedu.bennu.spring.portal.SpringFunctionality;
import org.fenixedu.treasury.domain.forwardpayments.ForwardPayment;
import org.fenixedu.treasury.domain.forwardpayments.ForwardPaymentRequest;
import org.fenixedu.treasury.domain.forwardpayments.implementations.IForwardPaymentController;
import org.fenixedu.treasury.domain.forwardpayments.implementations.IForwardPaymentImplementation;
import org.fenixedu.treasury.domain.forwardpayments.implementations.IForwardPaymentPlatformService;
import org.fenixedu.treasury.domain.forwardpayments.implementations.TPAVirtualImplementation;
import org.fenixedu.treasury.ui.TreasuryBaseController;
import org.fenixedu.treasury.ui.TreasuryController;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@SpringFunctionality(app = TreasuryController.class, title = "label.title.tpaReturnForwardPayment", accessGroup = "logged")
@RequestMapping(TPAVirtualController.CONTROLLER_URL)
public class TPAVirtualController extends TreasuryBaseController implements IForwardPaymentController {

    public static final String CONTROLLER_URL = "/treasury/document/forwardpayments/tpavirtual";
    private static final String JSP_PATH = "/treasury/document/forwardpayments/forwardpayment/implementations/tpavirtual";

    public String processforwardpayment(ForwardPaymentRequest forwardPayment, Model model, HttpServletResponse response,
            HttpSession session) {

        model.addAttribute("forwardPaymentConfiguration", forwardPayment.getDigitalPaymentPlatform());
        model.addAttribute("forwardPayment", forwardPayment);
        return jspPage("hostedPay");
    }

    private static final String RETURN_FORWARD_PAYMENT_URI = "/returnforwardpayment";
    public static final String RETURN_FORWARD_PAYMENT_URL = CONTROLLER_URL + RETURN_FORWARD_PAYMENT_URI;

    @RequestMapping(value = RETURN_FORWARD_PAYMENT_URI + "/{forwardPaymentId}", method = RequestMethod.POST,
            produces = "text/html")
    public String returnforwardpayment(@PathVariable("forwardPaymentId") ForwardPaymentRequest forwardPayment,
            @RequestParam Map<String, String> responseData, Model model, HttpServletResponse response) {
        TPAVirtualImplementation implementation = (TPAVirtualImplementation) forwardPayment.getDigitalPaymentPlatform();

        model.addAttribute("forwardPaymentConfiguration", implementation);
        try {
            boolean success = implementation.processPayment(forwardPayment, responseData);

            if (success) {
                return String.format("redirect:%s", forwardPayment.getForwardPaymentSuccessUrl());
            }

            return String.format("redirect:%s", forwardPayment.getForwardPaymentInsuccessUrl());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private static final String CURRENT_FORWARD_PAYMENT_STATE_URI = "/currentforwardpaymentstate";
    public static final String CURRENT_FORWARD_PAYMENT_STATE_URL = CONTROLLER_URL + CURRENT_FORWARD_PAYMENT_STATE_URI;

    @RequestMapping(value = CURRENT_FORWARD_PAYMENT_STATE_URI + "/{forwardPaymentId}", method = RequestMethod.POST)
    @ResponseBody
    public String currentforwardpaymentstate(@PathVariable("forwardPaymentId") ForwardPaymentRequest forwardPayment) {
        return forwardPayment.getCurrentState().toString();
    }

    private String jspPage(final String page) {
        return JSP_PATH + "/" + page;
    }

    public static Map<Class<IForwardPaymentPlatformService>, Class<IForwardPaymentController>> CONTROLLER_MAP = new HashMap<>();

    public static void registerForwardPaymentController(Class<IForwardPaymentPlatformService> implementationClass,
            Class<IForwardPaymentController> controllerClass) {
        CONTROLLER_MAP.put(implementationClass, controllerClass);
    }

    public static IForwardPaymentController getForwardPaymentController(final ForwardPaymentRequest forwardPayment) {
        return new TPAVirtualController();
    }

}
