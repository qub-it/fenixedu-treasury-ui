package org.fenixedu.treasury.domain.forwardpayments.implementations;

import org.fenixedu.treasury.domain.forwardpayments.ForwardPaymentStateType;
import org.fenixedu.treasury.dto.forwardpayments.ForwardPaymentStatusBean;

public class PostProcessPaymentStatusBean {

    private ForwardPaymentStatusBean forwardPaymentStatusBean;
    private boolean success;
    
    private ForwardPaymentStateType previousState;
    
    public PostProcessPaymentStatusBean(final ForwardPaymentStatusBean forwardPaymentStatusBean, final ForwardPaymentStateType previousState, final boolean success) {
        this.forwardPaymentStatusBean = forwardPaymentStatusBean;
        this.previousState = previousState;
        this.success = success;
    }
    
    public boolean isSuccess() {
        return success;
    }
    
    public ForwardPaymentStatusBean getForwardPaymentStatusBean() {
        return forwardPaymentStatusBean;
    }
    
    public ForwardPaymentStateType getPreviousState() {
        return previousState;
    }
    
}
