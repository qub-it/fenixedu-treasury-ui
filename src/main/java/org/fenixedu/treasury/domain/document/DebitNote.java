package org.fenixedu.treasury.domain.document;

import java.util.Set;

import org.fenixedu.treasury.domain.debt.DebtAccount;
import org.fenixedu.treasury.domain.exceptions.TreasuryDomainException;
import org.joda.time.DateTime;

import pt.ist.fenixframework.Atomic;

import com.google.common.collect.Sets;

public class DebitNote extends DebitNote_Base {

    protected DebitNote(final DebtAccount debtAccount, final DocumentNumberSeries documentNumberSeries, final DateTime documentDate) {
        super();
        
        this.init(debtAccount, documentNumberSeries, documentDate);
    }
    
    protected DebitNote(final DebtAccount debtAccount, final DebtAccount payorDebtAccount, final DocumentNumberSeries documentNumberSeries, final DateTime documentDate) {
        super();
        
        this.init(debtAccount, payorDebtAccount, documentNumberSeries, documentDate);
    }
    
    @Override
    public boolean isDebitNote() {
        return true;
    }
    
    public boolean isDeletable() {
        return true;
    }

    @Atomic
    public void delete() {
        if (!isDeletable()) {
            throw new TreasuryDomainException("error.DebitNote.cannot.delete");
        }

        setBennu(null);
        deleteDomainObject();
    }

    // @formatter: off
    /************
     * SERVICES *
     ************/
    // @formatter: on

    public static Set<DebitNote> readAll() {
        final Set<DebitNote> result = Sets.newHashSet();
        
        for (final Invoice invoice : readAll()) {
            if(invoice instanceof DebitNote) {
                result.add((DebitNote) invoice);
            }
        }
        
        return result;
    }

    @Atomic
    public static DebitNote create(final DebtAccount debtAccount, final DocumentNumberSeries documentNumberSeries, final DateTime documentDate) {
        return new DebitNote(debtAccount, documentNumberSeries, documentDate);
    }
    
    @Atomic
    public static DebitNote create(final DebtAccount debtAccount, final DebtAccount payorDebtAccount, final DocumentNumberSeries documentNumberSeries, final DateTime documentDate) {
        return new DebitNote(debtAccount, payorDebtAccount, documentNumberSeries, documentDate);
    }

}
