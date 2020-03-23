package org.fenixedu.treasury.domain.forwardpayments.implementations;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.ClassUtils;
import org.apache.commons.lang.reflect.MethodUtils;
import org.fenixedu.treasury.domain.forwardpayments.ForwardPayment;
import org.springframework.ui.Model;

public interface IForwardPaymentController {

    public static Map<Class<? extends IForwardPaymentImplementation>, Class<? extends IForwardPaymentController>> CONTROLLER_MAP =
            new HashMap<>();
    
    public static void registerForwardPaymentController(
            Class<? extends IForwardPaymentImplementation> implementationClass, 
            Class<? extends IForwardPaymentController> controllerClass) {
        CONTROLLER_MAP.put(implementationClass, controllerClass);
    }
    
    public static IForwardPaymentController getForwardPaymentController(final ForwardPayment forwardPayment) {
        
        try {
            final Class<IForwardPaymentImplementation> implementationClass = ClassUtils.getClass(forwardPayment.getForwardPaymentConfiguration().getImplementation());
            Object result = MethodUtils.invokeStaticMethod(CONTROLLER_MAP.get(implementationClass), "getForwardPaymentController", forwardPayment);
            return (IForwardPaymentController) result;
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
    
    public String processforwardpayment(final ForwardPayment forwardPayment, final Model model,
            final HttpServletResponse response, final HttpSession session);
}
