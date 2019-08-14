package org.fenixedu.treasury.domain.sibsonlinepaymentsgateway;

import java.math.BigDecimal;
import java.util.stream.Stream;

import org.fenixedu.treasury.domain.document.SettlementNote;
import org.joda.time.DateTime;

import pt.ist.fenixframework.FenixFramework;

public class MbwayTransaction extends MbwayTransaction_Base {

    public MbwayTransaction() {
        super();
        setCreationDate(new DateTime());
        setDomainRoot(FenixFramework.getDomainRoot());
    }

    public MbwayTransaction(MbwayPaymentRequest mbwayPaymentRequest, String sibsTransactionId, BigDecimal amount,
            DateTime paymentDate, final SettlementNote settlementNote) {
        this();

        setAmount(amount);
        setSibsTransactionId(sibsTransactionId);
        setPaymentDate(paymentDate);
        getSettlementNotesSet().add(settlementNote);
        
        checkRules();
    }

    private void checkRules() {
        
    }
    
    /* ************ */
    /* * SERVICES * */
    /* ************ */
    
    public static MbwayTransaction create(final MbwayPaymentRequest mbwayPaymentRequest, final String sibsTransactionId,
            final BigDecimal amount, final DateTime paymentDate, final SettlementNote settlementNote) {
        final MbwayTransaction mbwayTransaction =
                new MbwayTransaction(mbwayPaymentRequest, sibsTransactionId, amount, paymentDate, settlementNote);

        return mbwayTransaction;

    }
    
    public static Stream<MbwayTransaction> findAll() {
        return FenixFramework.getDomainRoot().getMbwayPaymentTransactionsSet().stream();
    }
    
    public static Stream<MbwayTransaction> find(final MbwayPaymentRequest mbwayPaymentRequest) {
        return mbwayPaymentRequest.getMbwayTransactionsSet().stream();
    }
    
    public static boolean isTransactionProcessingDuplicate(final String sibsTransactionId) {
        return findAll().anyMatch(t -> t.getSibsTransactionId().equals(sibsTransactionId));
    }

}
