package org.fenixedu.treasury.domain.paymentcodes;

import java.math.BigDecimal;

import org.fenixedu.bennu.core.domain.User;
import org.fenixedu.treasury.domain.document.SettlementNote;
import org.fenixedu.treasury.domain.event.TreasuryEvent;
import org.joda.time.DateTime;

public abstract class PaymentCodeTarget extends PaymentCodeTarget_Base {

    public PaymentCodeTarget() {
        super();
    }

    public abstract SettlementNote processPayment(final User person, final BigDecimal amountToPay, DateTime whenRegistered,
            String sibsTransactionId, String comments);

    public abstract String getDescription(final PaymentCodeTarget targetPaymentCode);

//    public IPaymentCodeTransactionReport getTransactionReportOnDate(final DateTime when);

    public abstract boolean isPaymentCodeFor(final TreasuryEvent event);
}
