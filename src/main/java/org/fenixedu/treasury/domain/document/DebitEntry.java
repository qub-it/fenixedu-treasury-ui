package org.fenixedu.treasury.domain.document;

import java.math.BigDecimal;
import java.util.stream.Stream;

import org.fenixedu.treasury.domain.Product;
import org.fenixedu.treasury.domain.exceptions.TreasuryDomainException;

public class DebitEntry extends DebitEntry_Base {
    
    protected DebitEntry(final FinantialDocument finantialDocument, final Product product, BigDecimal amount) {
        init(finantialDocument, product, amount);
    }

    @Override
    protected void init(FinantialDocument finantialDocument, Product product, FinantialEntryType finantialEntryType,
            BigDecimal amount) {
        throw new RuntimeException("error.CreditEntry.use.init.without.finantialEntryType");
    }
    
    protected void init(final FinantialDocument finantialDocument, final Product product, BigDecimal amount) {
        super.init(finantialDocument, product, FinantialEntryType.DEBIT_ENTRY, amount);
        
        checkRules();
    }
    
    @Override
    protected void checkRules() {
        super.checkRules();
        
        if(!(getFinantialDocument() instanceof DebitNote)) {
            throw new TreasuryDomainException("error.DebitEntry.finantialDocument.not.debit.entry.type");
        }
    }
    
    // @formatter: off
    /************
     * SERVICES *
     ************/
    // @formatter: on

    public static Stream<DebitEntry> findAll() {
        return (Stream<DebitEntry>) FinantialDocumentEntry.findAll().filter(f -> f instanceof DebitEntry);
    }
    
}
