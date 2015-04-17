package org.fenixedu.treasury.domain.document;

import java.math.BigDecimal;
import java.util.stream.Stream;

import org.fenixedu.bennu.core.domain.Bennu;
import org.fenixedu.treasury.domain.exceptions.TreasuryDomainException;

import pt.ist.fenixframework.Atomic;

public abstract class FinantialDocumentEntry extends FinantialDocumentEntry_Base {

    protected FinantialDocumentEntry() {
        super();
        setBennu(Bennu.getInstance());
    }
    
    protected void init(final FinantialDocument finantialDocument, final FinantialEntryType finantialEntryType, final BigDecimal amount) {
        setFinantialDocument(finantialDocument);
        setFinantialEntryType(finantialEntryType);
        setAmount(amount);
        setQuantity(BigDecimal.ONE);
        
        checkRules();
    }

    protected void checkRules() {
        if(getFinantialDocument() == null) {
            throw new TreasuryDomainException("error.FinantialDocumentEntry.finantialDocument.required");
        }

        if(getFinantialEntryType() == null) {
            throw new TreasuryDomainException("error.FinantialDocumentEntry.finantialEntryType.required");
        }
        
        if(getAmount() == null) {
            throw new TreasuryDomainException("error.FinantialDocumentEntry.amount.required");
        }
        
        if(getAmount().compareTo(BigDecimal.ZERO) < 0) {
            throw new TreasuryDomainException("error.FinantialDocumentEntry.amount.less.than.zero");
        }
    }

    public boolean isDeletable() {
        return true;
    }

    @Atomic
    public void delete() {
        if (!isDeletable()) {
            throw new TreasuryDomainException("error.FinantialDocumentEntry.cannot.delete");
        }

        setBennu(null);

        deleteDomainObject();
    }

    // @formatter: off
    /************
     * SERVICES *
     ************/
    // @formatter: on

    public static Stream<? extends FinantialDocumentEntry> findAll() {
        return Bennu.getInstance().getFinantialDocumentEntriesSet().stream();
    }

}
