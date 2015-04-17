package org.fenixedu.treasury.domain.document;

import java.util.Set;

import org.fenixedu.treasury.domain.debt.DebtAccount;
import org.joda.time.DateTime;

import pt.ist.fenixframework.Atomic;

import com.google.common.collect.Sets;

public class SettlementNote extends SettlementNote_Base {
    
    protected SettlementNote(final DebtAccount debtAccount, final DocumentNumberSeries documentNumberSeries, final DateTime documentDate) {
        init(debtAccount, documentNumberSeries, documentDate);
    }
    
    protected void init(final DebtAccount debtAccount, final DocumentNumberSeries documentNumberSeries, final DateTime documentDate) {
        super.init(debtAccount, documentNumberSeries, documentDate);
    }
    
    @Override
    public boolean isSettlementNote() {
        return true;
    }
    
    // @formatter: off
    /************
     * SERVICES *
     ************/
    // @formatter: on

    public static Set<SettlementNote> readAll() {
        final Set<SettlementNote> result = Sets.newHashSet();
        
        for (final FinantialDocument finantialDocument : FinantialDocument.readAll()) {
            if(finantialDocument.isSettlementNote()) {
                result.add((SettlementNote) finantialDocument);
            }
        }
        
        return result;
    }
    
    
    @Atomic
    public SettlementNote create(final DebtAccount debtAccount, final DocumentNumberSeries documentNumberSeries, final DateTime documentDate) {
        return new SettlementNote(debtAccount, documentNumberSeries, documentDate);
    }
    
}
