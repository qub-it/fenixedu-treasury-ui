package org.fenixedu.treasury.services.payments.exporters;

import java.util.Set;
import java.util.stream.Collectors;

import org.fenixedu.treasury.domain.paymentcodes.PaymentReferenceCode;
import org.fenixedu.treasury.domain.paymentcodes.pool.PaymentCodePool;
import org.joda.time.DateTime;

import pt.ist.fenixframework.Atomic;
import pt.ist.fenixframework.Atomic.TxMode;

import com.google.common.collect.Sets;

public class PooledPaymentCodesGenerator implements IPaymentCodeGenerator {

    @Override
    public void generate(PaymentCodeGeneratorsErrors errors) {
        final Thread thread = new Thread() {
            @Override
            public void run() {
                executeAsRead();
            }

        };

        thread.start();
        try {
            thread.join();
        } catch (InterruptedException e) {
            //nothing to be done
        }
    }

    @Atomic(mode = TxMode.READ)
    protected void executeAsRead() {
        for (final PaymentCodePool paymentCodePool : PaymentCodePool.findAll().filter(x -> x.getUseCheckDigit() == false)
                .collect(Collectors.toList())) {
            updatePool(paymentCodePool);
        }
    }

    @Atomic(mode = TxMode.WRITE)
    private void updatePool(PaymentCodePool paymentCodePool) {
        paymentCodePool.updatePoolReferences();
    }

    @Override
    public Set<PaymentReferenceCode> findActivePaymentCodes(final DateTime when) {
        final Set<PaymentReferenceCode> result = Sets.newHashSet();

        for (final PaymentCodePool paymentCodePool : PaymentCodePool.findAll().filter(x -> x.getUseCheckDigit() == false)
                .collect(Collectors.toSet())) {
            result.addAll(paymentCodePool.getPaymentCodesToExport(when.toLocalDate()));
        }

        return result;
    }

}
