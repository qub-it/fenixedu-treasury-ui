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
    
    @Override
    protected void init(final DebtAccount debtAccount, final DocumentNumberSeries documentNumberSeries, final DateTime documentDate) {
        super.init(debtAccount, documentNumberSeries, documentDate);
    }
    
    protected void init(final DebtAccount debtAccount, final DebtAccount payorDebtAccount, final DocumentNumberSeries documentNumberSeries, final DateTime documentDate) {
        super.init(debtAccount, documentNumberSeries, documentDate);
        
        if(payorDebtAccount == null) {
            throw new TreasuryDomainException("error.Invoice.payorDebtAccount.null");
        }
        
        setPayorDebtAccount(payorDebtAccount);
    }

    @Override
    public boolean isInvoice() {
        return true;
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
