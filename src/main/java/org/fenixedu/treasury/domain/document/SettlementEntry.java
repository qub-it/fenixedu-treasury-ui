package org.fenixedu.treasury.domain.document;

import java.math.BigDecimal;
import java.util.stream.Stream;

import org.fenixedu.treasury.domain.exceptions.TreasuryDomainException;

import pt.ist.fenixframework.Atomic;

public class SettlementEntry extends SettlementEntry_Base {
    
    protected SettlementEntry(final FinantialDocument finantialDocument, final BigDecimal amount) {
        init(finantialDocument, amount);
    }
    
    @Override
    protected void init(final FinantialDocument finantialDocument, final FinantialEntryType finantialEntryType, final BigDecimal amount) {
        throw new RuntimeException("error.SettlementEntry.use.init.without.finantialEntryType");
    }
    
    protected void init(final FinantialDocument finantialDocument, final BigDecimal amount) {
        super.init(finantialDocument, FinantialEntryType.SETTLEMENT_ENTRY, amount);
        
        checkRules();
    }
    
    @Override
    protected void checkRules() {
        super.checkRules();
        
        if(!(getFinantialDocument() instanceof SettlementNote)) {
            throw new TreasuryDomainException("error.SettlementEntry.finantialDocument.not.settlement.note.type");
        }
    }
    
    // @formatter: off
    /************
     * SERVICES *
     ************/
    // @formatter: on

    public static Stream<SettlementEntry> findAll() {
        return (Stream<SettlementEntry>) FinantialDocumentEntry.findAll().filter(f -> f instanceof SettlementEntry);
    }
    
    @Atomic
    public SettlementEntry create(final FinantialDocument finantialDocument, final BigDecimal amount) {
        return new SettlementEntry(finantialDocument, amount);
    }
    
}
