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
package org.fenixedu.treasury.domain.debt;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.Optional;
import java.util.stream.Stream;

import org.fenixedu.bennu.core.domain.Bennu;
import org.fenixedu.treasury.domain.Customer;
import org.fenixedu.treasury.domain.FinantialInstitution;
import org.fenixedu.treasury.domain.document.FinantialDocument;
import org.fenixedu.treasury.domain.document.InvoiceEntry;
import org.fenixedu.treasury.domain.exceptions.TreasuryDomainException;

import pt.ist.fenixframework.Atomic;

public class DebtAccount extends DebtAccount_Base {

    public DebtAccount() {
        super();
        setBennu(Bennu.getInstance());
    }

    protected DebtAccount(final FinantialInstitution finantialInstitution, final Customer customer) {
        this();
        setCustomer(customer);
        setFinantialInstitution(finantialInstitution);

        checkRules();
    }

    private void checkRules() {
        if (getCustomer() == null) {
            throw new TreasuryDomainException("error.DebtAccount.customer.required");
        }

        if (getFinantialInstitution() == null) {
            throw new TreasuryDomainException("error.DebtAccount.finantialInstitution.required");
        }
    }

    public BigDecimal getTotalInDebt() {
        BigDecimal amount = BigDecimal.ZERO;
        for (FinantialDocument document : this.getFinantialDocumentsSet()) {
            if (document.isClosed() == false) {
                if (document.isDebitNote()) {
                    amount = amount.add(document.getOpenAmount());
                } else if (document.isCreditNote()) {
                    amount = amount.subtract(document.getOpenAmount());
                }
            }
        }

        return amount;
    }

//	public boolean isDeletable() {
//		return true;
//	}
//
//	@Atomic
//	public void delete() {
//		if (!isDeletable()) {
//			throw new TreasuryDomainException("error.DebtAccount.cannot.delete");
//		}
//
//		setBennu(null);
//
//		deleteDomainObject();
//	}
//

    // @formatter: off
    /************
     * SERVICES *
     ************/
    // @formatter: on

    public static Stream<DebtAccount> findAll() {
        return Bennu.getInstance().getDebtAccountsSet().stream();
    }

    public static Stream<DebtAccount> find(final FinantialInstitution finantialInstitution) {
        return findAll().filter(d -> d.getFinantialInstitution() == finantialInstitution);
    }

    public static Stream<DebtAccount> find(final Customer customer) {
        return findAll().filter(d -> d.getCustomer() == customer);
    }

    public static Optional<DebtAccount> findUnique(final FinantialInstitution finantialInstitution, final Customer customer) {
        return find(finantialInstitution).filter(d -> d.getCustomer() == customer).findFirst();
    }

    @Atomic
    public static DebtAccount create(final FinantialInstitution finantialInstitution, final Customer customer) {
        return new DebtAccount(finantialInstitution, customer);
    }

    public Stream<? extends InvoiceEntry> getPendingInvoiceEntries() {
        // TODO Auto-generated method stub
        return null;
    }

}
