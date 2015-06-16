package org.fenixedu.treasury.services.payments.exporters;

import java.util.Set;

import org.fenixedu.treasury.domain.paymentcodes.PaymentReferenceCode;
import org.joda.time.DateTime;

public interface IPaymentCodeGenerator {
    public void generate(final PaymentCodeGeneratorsErrors errors);

    public Set<PaymentReferenceCode> findActivePaymentCodes(final DateTime when);
}
