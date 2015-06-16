//package org.fenixedu.treasury.services.payments.exporters;
//
//import java.util.Collections;
//import java.util.Set;
//
//import org.fenixedu.treasury.domain.paymentcodes.MultipleEntriesPaymentCode;
//import org.fenixedu.treasury.domain.paymentcodes.PaymentReferenceCode;
//import org.fenixedu.treasury.domain.paymentcodes.PaymentReferenceCodeStateType;
//import org.joda.time.DateTime;
//
//import pt.ist.fenixframework.Atomic;
//import pt.ist.fenixframework.Atomic.TxMode;
//
//public class ExpiredPaymentCodesGenerator implements IPaymentCodeGenerator {
//
//    @Override
//    public void generate(PaymentCodeGeneratorsErrors errors) {
//        final Thread thread = new Thread() {
//            @Override
//            public void run() {
//                executeAsRead();
//            }
//
//        };
//
//        thread.start();
//        try {
//            thread.join();
//        } catch (InterruptedException e) {
//            //nothing to be done
//        }
//    }
//
//    @Atomic(mode = TxMode.READ)
//    protected void executeAsRead() {
//
//        final DateTime now = new DateTime();
//        for (final MultipleEntriesPaymentCode target : MultipleEntriesPaymentCode.findAll()) {
//
//            if (target.isNew() && !target.isValid(now)) {
//                invalidatePaymentCode(target);
//            }
//        }
//
//    }
//
//    @Atomic(mode = TxMode.WRITE)
//    private void invalidatePaymentCode(MultipleEntriesPaymentCode target) {
//        target.getPaymentReferenceCode().setState(PaymentReferenceCodeStateType.ANNULLED);
//    }
//
//    @Override
//    public Set<PaymentReferenceCode> findActivePaymentCodes(final DateTime when) {
//
//        //no payment codes to return
//
//        return Collections.emptySet();
//
//    }
//
//}
