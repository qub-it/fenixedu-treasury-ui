package org.fenixedu.treasury.domain.document;

import java.util.Set;

import org.fenixedu.treasury.domain.debt.DebtAccount;
import org.fenixedu.treasury.domain.exceptions.TreasuryDomainException;
import org.joda.time.DateTime;

import pt.ist.fenixframework.Atomic;

import com.google.common.collect.Sets;

public abstract class Invoice extends Invoice_Base {

    protected Invoice() {
        super();
    }

    protected void init(final DocumentNumberSeries documentNumberSeries, final DebtAccount debtAccount, final DateTime documentDate) {
        super.init(documentNumberSeries, documentDate);
        setDebtAccount(debtAccount);

        checkRules();
    }

    protected void checkRules() {
        super.checkRules();

        if (getDebtAccount() == null) {
            throw new TreasuryDomainException("error.Invoice.debtAccount.required");
        }
    }

    public boolean isDeletable() {
        return true;
    }

    @Atomic
    public void delete() {
        if (!isDeletable()) {
            throw new TreasuryDomainException("error.Invoice.cannot.delete");
        }

        super.delete();
    }

    // @formatter: off
    /************
     * SERVICES *
     ************/
    // @formatter: on

    public static Set<? extends Invoice> readAll() {
        final Set<Invoice> result = Sets.newHashSet();
        
        for (final Invoice invoice : readAll()) {
            if(invoice instanceof Invoice) {
                result.add(invoice);
            }
        }
        
        return result;
    }

    public static Set<Invoice> find(final DebtAccount debtAccount) {
        Set<Invoice> result = Sets.newHashSet();

        for (final Invoice it : readAll()) {
            if (it.getDebtAccount() == debtAccount) {
                result.add(it);
            }
        }

        return result;
    }
    
    
    
}
