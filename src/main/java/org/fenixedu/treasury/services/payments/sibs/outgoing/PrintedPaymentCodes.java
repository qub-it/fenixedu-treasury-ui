package org.fenixedu.treasury.services.payments.sibs.outgoing;

import java.util.HashSet;
import java.util.Set;

import org.fenixedu.treasury.domain.paymentcodes.PaymentReferenceCode;

public class PrintedPaymentCodes {

    private Set<String> paymentCodes;

    public PrintedPaymentCodes() {
        this.paymentCodes = new HashSet<String>();
    }

    public String exportAsString() {
        StringBuilder result = new StringBuilder();

        for (String code : this.paymentCodes) {
            result.append(code).append(",");
        }

        result.delete(result.length() - 1, result.length());

        return result.toString();
    }

    public Set<String> getPaymentCodes() {
        return paymentCodes;
    }

    public void addPaymentCode(final PaymentReferenceCode paymentCode) {
        this.paymentCodes.add(paymentCode.getReferenceCode());
    }

    public static PrintedPaymentCodes importFromString(final String value) {
        String[] codes = value.split(",");

        PrintedPaymentCodes printPaymentCodes = new PrintedPaymentCodes();

        for (String c : codes) {
            printPaymentCodes.paymentCodes.add(c);
        }

        return printPaymentCodes;
    }
}
