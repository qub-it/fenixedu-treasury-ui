package org.fenixedu.treasury.ui.document.forwardpayments.implementations;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.ClassUtils;
import org.apache.commons.lang.reflect.MethodUtils;
import org.fenixedu.bennu.spring.portal.SpringFunctionality;
import org.fenixedu.treasury.domain.forwardpayments.ForwardPayment;
import org.fenixedu.treasury.domain.forwardpayments.exceptions.ForwardPaymentAlreadyPayedException;
import org.fenixedu.treasury.domain.forwardpayments.implementations.IForwardPaymentController;
import org.fenixedu.treasury.domain.forwardpayments.implementations.IForwardPaymentImplementation;
import org.fenixedu.treasury.domain.forwardpayments.implementations.PaylineImplementation;
import org.fenixedu.treasury.ui.TreasuryBaseController;
import org.fenixedu.treasury.ui.TreasuryController;
import org.fenixedu.treasury.util.TreasuryConstants;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.google.common.base.Strings;

import pt.ist.fenixframework.Atomic;
import pt.ist.fenixframework.FenixFramework;

@SpringFunctionality(app = TreasuryController.class, title = "label.title.paylineReturnForwardPayment", accessGroup = "logged")
@RequestMapping(PaylineController.CONTROLLER_URL)
public class PaylineController extends TreasuryBaseController implements IForwardPaymentController {

    public static final String CONTROLLER_URL = "/treasury/document/forwardpayments/payline";
    private static final String JSP_PATH = "/treasury/document/forwardpayments/forwardpayment/implementations/payline";

    @Override
    public String processforwardpayment(final ForwardPayment forwardPayment, final Model model,
            final HttpServletResponse response, final HttpSession session) {
        model.addAttribute("forwardPaymentConfiguration", forwardPayment.getForwardPaymentConfiguration());
        
        try {
            final PaylineImplementation paylineImplementation =
                    (PaylineImplementation) forwardPayment.getForwardPaymentConfiguration().implementation();
            final boolean paylineSucess =
                    paylineImplementation.doWebPayment(forwardPayment, readReturnForwardPaymentUrl(), session);

            if (!paylineSucess) {
                return String.format("redirect:%s", forwardPayment.getForwardPaymentInsuccessUrl());
            }

            return "redirect:" + forwardPayment.getPaylineRedirectUrl();
        } catch (Exception e) {
            e.printStackTrace();
            
            rejectPayment(forwardPayment, e.getMessage(), e.getLocalizedMessage());
            return String.format("redirect:%s", forwardPayment.getForwardPaymentInsuccessUrl());
        }
    }

    private static final String RETURN_FORWARD_PAYMENT_URI = "/returnforwardpayment";
    public static final String RETURN_FORWARD_PAYMENT_URL = CONTROLLER_URL + RETURN_FORWARD_PAYMENT_URI;

    protected String readReturnForwardPaymentUrl() {
        return RETURN_FORWARD_PAYMENT_URL;
    }

    @RequestMapping(value = RETURN_FORWARD_PAYMENT_URI + "/{forwardPaymentId}/{action}/{urlChecksum}", method = RequestMethod.GET)
    public String returnforwardpayment(@PathVariable("forwardPaymentId") final ForwardPayment forwardPayment,
            @PathVariable("action") final String action, @PathVariable("urlChecksum") final String urlChecksum,
            @RequestParam final Map<String, String> responseData, final Model model, final HttpServletResponse response) {
        model.addAttribute("forwardPaymentConfiguration", forwardPayment.getForwardPaymentConfiguration());

        try {

            // verify url checksum
            if (Strings.isNullOrEmpty(urlChecksum) || !forwardPayment.getReturnForwardPaymentUrlChecksum().equals(urlChecksum)) {
                rejectPayment(forwardPayment, "INVALID_CHECKSUM", "Invalid checksum");
                return String.format("redirect:%s", forwardPayment.getForwardPaymentInsuccessUrl());
            }

            final PaylineImplementation paylineImplementation =
                    (PaylineImplementation) forwardPayment.getForwardPaymentConfiguration().implementation();
            boolean success = paylineImplementation.processPayment(forwardPayment, action);

            if (success) {
                return String.format("redirect:%s", forwardPayment.getForwardPaymentSuccessUrl());
            }

            return String.format("redirect:%s", forwardPayment.getForwardPaymentInsuccessUrl());

        } catch(final ForwardPaymentAlreadyPayedException e) {
            addErrorMessage(e.getLocalizedMessage(), model);

            return String.format("redirect:%s", forwardPayment.getForwardPaymentInsuccessUrl());
        } catch (Exception e) {
            e.printStackTrace();
            addErrorMessage(TreasuryConstants.treasuryBundle("error.PaylineController.returnforwardpayment.message", e.getMessage()), model);

            FenixFramework.atomic(() -> {
        	Map<String, String> requestBodyMap = new HashMap<>();
        	requestBodyMap.put("action", action);
        	requestBodyMap.put("urlChecksum", urlChecksum);
        	
        	forwardPayment.logException(e, TreasuryConstants.propertiesMapToJson(requestBodyMap), TreasuryConstants.propertiesMapToJson(responseData));
            });
            
            return String.format("redirect:%s", forwardPayment.getForwardPaymentInsuccessUrl());
        }
    }

    @Atomic
    private void rejectPayment(final ForwardPayment forwardPayment, String code, String message) {
        forwardPayment.reject(code, message, "", "");
    }

    private String jspPage(final String page) {
        return JSP_PATH + "/" + page;
    }


    public static Map<Class<IForwardPaymentImplementation>, Class<IForwardPaymentController>> CONTROLLER_MAP =
            new HashMap<>();
    
    public static void registerForwardPaymentController(Class<IForwardPaymentImplementation> implementationClass, Class<IForwardPaymentController> controllerClass) {
        CONTROLLER_MAP.put(implementationClass, controllerClass);
    }
    
    public static IForwardPaymentController getForwardPaymentController(final ForwardPayment forwardPayment) {
        return new PaylineController();
    }
    
}
