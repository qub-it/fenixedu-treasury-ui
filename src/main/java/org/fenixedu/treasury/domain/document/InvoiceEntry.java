package org.fenixedu.treasury.domain.document;

import java.math.BigDecimal;
import java.util.stream.Stream;

import org.fenixedu.treasury.domain.Product;
import org.fenixedu.treasury.domain.exceptions.TreasuryDomainException;

public abstract class InvoiceEntry extends InvoiceEntry_Base {
    
    @Override
    protected void init(FinantialDocument finantialDocument, FinantialEntryType finantialEntryType, BigDecimal amount) {
        throw new RuntimeException("error.InvoiceEntry.use.init.with.product");
    }
    
    protected void init(final FinantialDocument finantialDocument, final Product product, final FinantialEntryType finantialEntryType, final BigDecimal amount) {
        super.init(finantialDocument, finantialEntryType, amount);
        
        this.setProduct(product);
    }

    @Override
    protected void checkRules() {
        super.checkRules();
        
        if(!(getFinantialDocument() instanceof Invoice)) {
            throw new TreasuryDomainException("error.InvoiceEntry.finantialDocument.not.invoice.type");
        }
        
        if(getProduct() == null) {
            throw new TreasuryDomainException("error.InvoiceEntry.product.required");
        }
    }

    public static Stream<? extends InvoiceEntry> findAll() {
        return (Stream<InvoiceEntry>) FinantialDocumentEntry.findAll().filter(f -> f instanceof InvoiceEntry);
    }
    
}
