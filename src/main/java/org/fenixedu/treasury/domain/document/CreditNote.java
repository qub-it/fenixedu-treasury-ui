package org.fenixedu.treasury.domain.document;

import java.util.Set;

import org.fenixedu.treasury.domain.debt.DebtAccount;
import org.fenixedu.treasury.domain.exceptions.TreasuryDomainException;
import org.joda.time.DateTime;

import pt.ist.fenixframework.Atomic;

import com.google.common.collect.Sets;

public class CreditNote extends CreditNote_Base {
    
    protected CreditNote(final DebtAccount debtAccount, final DocumentNumberSeries documentNumberSeries, final DateTime documentDate) {
        super();
        
        init(debtAccount, documentNumberSeries, documentDate);
        checkRules();
    }
    
    protected CreditNote(final DebtAccount debtAccount, final DebtAccount payorDebtAccount, final DocumentNumberSeries documentNumberSeries, final DateTime documentDate) {
        super();
        
        init(debtAccount, payorDebtAccount, documentNumberSeries, documentDate);
        checkRules();
    }
    
    @Override
    public boolean isCreditNote() {
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

    public static Set<CreditNote> readAll() {
        final Set<CreditNote> result = Sets.newHashSet();
        
        for (final Invoice invoice : readAll()) {
            if(invoice instanceof CreditNote) {
                result.add((CreditNote) invoice);
            }
        }
        
        return result;
    }

    @Atomic
    public static CreditNote create(final DebtAccount debtAccount, final DocumentNumberSeries documentNumberSeries, final DateTime documentDate) {
        return new CreditNote(debtAccount, documentNumberSeries, documentDate);
    }

    @Atomic
    public static CreditNote create(final DebtAccount debtAccount, final DebtAccount payorDebtAccount, final DocumentNumberSeries documentNumberSeries, final DateTime documentDate) {
        return new CreditNote(debtAccount, payorDebtAccount, documentNumberSeries, documentDate);
    }
    
}
