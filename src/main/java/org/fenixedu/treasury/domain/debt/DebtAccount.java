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

import java.util.Set;
import java.util.stream.Stream;

import org.fenixedu.bennu.core.domain.Bennu;
import org.fenixedu.treasury.domain.Customer;
import org.fenixedu.treasury.domain.FinantialInstitution;
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

//		checkRules();
	}
//
//	private void checkRules() {
//		if (LocalizedStringUtil.isEmpty(getCode())) {
//			throw new TreasuryDomainException("error.DebtAccount.code.required");
//		}
//
//		if (LocalizedStringUtil.isTrimmedEmpty(getName())) {
//			throw new TreasuryDomainException("error.DebtAccount.name.required");
//		}
//
//		findByCode(getCode());
//		getName().getLocales().stream()
//				.forEach(l -> findByName(getName().getContent(l)));
//	}
//
//	@Atomic
//	public void edit(final String code, final LocalizedString name) {
//		setCode(code);
//		setName(name);
//
//		checkRules();
//	}
//
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
//	// @formatter: off
//	/************
//	 * SERVICES *
//	 ************/
//	// @formatter: on
//
//	public static Set<DebtAccount> readAll() {
//		return Bennu.getInstance().getDebtAccountsSet();
//	}
//
//	public static DebtAccount findByCode(final String code) {
//		DebtAccount result = null;
//
//		for (final DebtAccount it : readAll()) {
//			if (!it.getCode().equalsIgnoreCase(code)) {
//				continue;
//			}
//
//			if (result != null) {
//				throw new TreasuryDomainException(
//						"error.DebtAccount.duplicated.code");
//			}
//
//			result = it;
//		}
//
//		return result;
//	}
//
//	public static DebtAccount findByName(final String name) {
//		DebtAccount result = null;
//
//		for (final DebtAccount it : readAll()) {
//
//			if (!LocalizedStringUtil.isEqualToAnyLocaleIgnoreCase(it.getName(),
//					name)) {
//				continue;
//			}
//
//			if (result != null) {
//				throw new TreasuryDomainException(
//						"error.DebtAccount.duplicated.name");
//			}
//
//			result = it;
//		}
//
//		return result;
//	}

    @Atomic
    public static DebtAccount create(final FinantialInstitution finantialInstitution, final Customer customer) {
        return new DebtAccount(finantialInstitution, customer);
    }

}
