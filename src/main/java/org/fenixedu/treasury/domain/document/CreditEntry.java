package org.fenixedu.treasury.domain.document;

import java.math.BigDecimal;
import java.util.stream.Stream;

import org.fenixedu.treasury.domain.Product;
import org.fenixedu.treasury.domain.exceptions.TreasuryDomainException;

public class CreditEntry extends CreditEntry_Base {
    
    
    protected CreditEntry(final FinantialDocument finantialDocument, final Product product, final BigDecimal amount) {
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
        
        if(!(getFinantialDocument() instanceof CreditNote)) {
            throw new TreasuryDomainException("error.DebitEntry.finantialDocument.not.debit.entry.type");
        }
    }
    
    // @formatter: off
    /************
     * SERVICES *
     ************/
    // @formatter: on

    public static Stream<CreditEntry> findAll() {
        return (Stream<CreditEntry>) FinantialDocumentEntry.findAll().filter(f -> f instanceof CreditEntry);
    }
    
}
