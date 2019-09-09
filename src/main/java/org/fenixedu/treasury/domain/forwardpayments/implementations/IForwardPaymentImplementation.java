package org.fenixedu.treasury.domain.forwardpayments.implementations;

import java.util.List;
import java.util.Optional;

import org.fenixedu.treasury.domain.forwardpayments.ForwardPayment;
import org.fenixedu.treasury.dto.forwardpayments.ForwardPaymentStatusBean;
import org.fenixedu.treasury.ui.document.forwardpayments.IForwardPaymentController;

public interface IForwardPaymentImplementation {

    public IForwardPaymentController getForwardPaymentController(final ForwardPayment forwardPayment);

    public String getPaymentURL(final ForwardPayment forwardPayment);

    public String getFormattedAmount(final ForwardPayment forwardPayment);

    public String getLogosJspPage();

    public String getWarningBeforeRedirectionJspPage();

    public ForwardPaymentStatusBean paymentStatus(final ForwardPayment forwardPayment);

    public PostProcessPaymentStatusBean postProcessPayment(final ForwardPayment forwardPayment, final String justification,
            final Optional<String> specificTransactionId);

    public String getImplementationCode();

    public List<ForwardPaymentStatusBean> verifyPaymentStatus(final ForwardPayment forwardPayment);

}
