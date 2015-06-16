package org.fenixedu.treasury.services.payments.exporters;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.fenixedu.treasury.domain.paymentcodes.PaymentReferenceCode;
import org.joda.time.DateTime;

import com.google.common.collect.Sets;

public class PaymentCodeGeneratorsRegistry {

    protected static List<IPaymentCodeGenerator> generators = new ArrayList<IPaymentCodeGenerator>();

    final PaymentCodeGeneratorsErrors errors = new PaymentCodeGeneratorsErrors();

    public static PaymentCodeGeneratorsRegistry createInstance() {
        return new PaymentCodeGeneratorsRegistry();
    }

    public static void registerExporterAsFirst(final IPaymentCodeGenerator generator) {
        generators.add(0, generator);
    }

    public static void registerExporter(final IPaymentCodeGenerator generator) {
        generators.add(generator);
    }

    public void generate() {
        for (final IPaymentCodeGenerator generator : generators) {
            generator.generate(errors);
        }
    }

    public Set<PaymentReferenceCode> findActivePaymentCodes(final DateTime when) {
        final Set<PaymentReferenceCode> result = Sets.newHashSet();
        for (final IPaymentCodeGenerator generator : generators) {
            result.addAll(generator.findActivePaymentCodes(when));
        }

        return result;
    }

    public String getErrors() {
        return errors.toString();
    }
}
