/**
 * This file was created by Quorum Born IT <http://www.qub-it.com/> and its 
 * copyright terms are bind to the legal agreement regulating the FenixEdu@ULisboa 
 * software development project between Quorum Born IT and Serviços Partilhados da
 * Universidade de Lisboa:
 *  - Copyright © 2015 Quorum Born IT (until any Go-Live phase)
 *  - Copyright © 2015 Universidade de Lisboa (after any Go-Live phase)
 *
 * Contributors: ricardo.pedro@qub-it.com, anil.mamede@qub-it.com
 * 
 *
 * 
 * This file is part of FenixEdu Treasury.
 *
 * FenixEdu Treasury is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * FenixEdu Treasury is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with FenixEdu Treasury.  If not, see <http://www.gnu.org/licenses/>.
 */
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
