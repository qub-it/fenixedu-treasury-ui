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

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Stream;

import org.fenixedu.bennu.core.domain.Bennu;
import org.fenixedu.treasury.domain.Customer;
import org.fenixedu.treasury.domain.FinantialInstitution;
import org.fenixedu.treasury.domain.exceptions.TreasuryDomainException;
import org.fenixedu.treasury.util.LocalizedStringUtil;

import pt.ist.fenixframework.Atomic;

public class DebtAccount extends DebtAccount_Base {

	protected DebtAccount() {
		super();
		setBennu(Bennu.getInstance());
	}

	protected void init(final FinantialInstitution finantialInstitution,
			final Customer customer) {
		setFinantialInstitution(finantialInstitution);
		setCustomer(customer);
		checkRules();
	}

	private void checkRules() {
		//
		// CHANGE_ME add more busines validations
		//
		if (getFinantialInstitution() == null) {
			throw new TreasuryDomainException(
					"error.DebtAccount.finantialInstitution.required");
		}

		if (getCustomer() == null) {
			throw new TreasuryDomainException(
					"error.DebtAccount.customer.required");
		}

		// CHANGE_ME In order to validate UNIQUE restrictions
		// if (findByFinantialInstitution(getFinantialInstitution().count()>1)
		// {
		// throw new
		// TreasuryDomainException("error.DebtAccount.finantialInstitution.duplicated");
		// }
		// if (findByCustomer(getCustomer().count()>1)
		// {
		// throw new
		// TreasuryDomainException("error.DebtAccount.customer.duplicated");
		// }
	}

	@Atomic
	public void edit(final FinantialInstitution finantialInstitution,
			final Customer customer) {
		setFinantialInstitution(finantialInstitution);
		setCustomer(customer);
		checkRules();
	}

	public boolean isDeletable() {
		return true;
	}

	@Atomic
	public void delete() {
		if (!isDeletable()) {
			throw new TreasuryDomainException("error.DebtAccount.cannot.delete");
		}

		setBennu(null);

		deleteDomainObject();
	}

	@Atomic
	public static DebtAccount create(
			final FinantialInstitution finantialInstitution,
			final Customer customer) {
		DebtAccount debtAccount = new DebtAccount();
		debtAccount.init(finantialInstitution, customer);
		return debtAccount;
	}

	// @formatter: off
	/************
	 * SERVICES *
	 ************/
	// @formatter: on

	public static Stream<DebtAccount> findAll() {
		Set<DebtAccount> allDebtAccounts = new HashSet<DebtAccount>();
		Bennu.getInstance().getCustomersSet().stream()
				.map(x -> allDebtAccounts.addAll(x.getDebtAccountsSet()));
		return allDebtAccounts.stream();
	}

	public static Stream<DebtAccount> findByFinantialInstitution(
			final FinantialInstitution finantialInstitution) {
		return findAll().filter(
				i -> finantialInstitution.equals(i.getFinantialInstitution()));
	}

	public static Stream<DebtAccount> findByCustomer(final Customer customer) {
		return findAll().filter(i -> customer.equals(i.getCustomer()));
	}

}
