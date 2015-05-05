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

import java.math.BigDecimal;
import java.util.stream.Stream;

import org.fenixedu.treasury.domain.Product;
import org.fenixedu.treasury.domain.debt.DebtAccount;
import org.fenixedu.treasury.domain.exceptions.TreasuryDomainException;

public class CreditEntry extends CreditEntry_Base {
    
    protected CreditEntry(final FinantialDocument finantialDocument, final Product product, final BigDecimal amount) {
        init(finantialDocument, product, amount);
    }

    @Override
    protected void init(final FinantialDocument finantialDocument, final DebtAccount debtAccount, final Product product, final FinantialEntryType finantialEntryType,
            final BigDecimal amount) {
        throw new RuntimeException("error.CreditEntry.use.init.without.finantialEntryType");
    }
    
    protected void init(final FinantialDocument finantialDocument, final Product product, final BigDecimal amount) {
        super.init(finantialDocument, finantialDocument.getDebtAccount(), product, FinantialEntryType.DEBIT_ENTRY, amount);
        
        checkRules();
    }
    
    @Override
    protected void checkRules() {
        super.checkRules();
        
        if(getFinantialDocument() != null && !(getFinantialDocument() instanceof CreditNote)) {
            throw new TreasuryDomainException("error.DebitEntry.finantialDocument.not.debit.entry.type");
        }
    }
    
    // @formatter: off
    /************
     * SERVICES *
     ************/
    // @formatter: on

    public static Stream<CreditEntry> findAll() {
        return FinantialDocumentEntry.findAll().filter(f -> f instanceof CreditEntry).map(CreditEntry.class::cast);
    }

    public BigDecimal getOpenAmount() {
		BigDecimal amount = this.getAmount();
		for (SettlementEntry entry : this.getSettlementEntriesSet()) {
			amount = amount.subtract(entry.getAmount());
		}
		return amount;
	}
    
}
