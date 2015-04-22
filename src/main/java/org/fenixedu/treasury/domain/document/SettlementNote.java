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
import java.util.stream.Stream;

import org.fenixedu.bennu.core.domain.Bennu;
import org.fenixedu.treasury.domain.Currency;
import org.fenixedu.treasury.domain.debt.DebtAccount;
import org.fenixedu.treasury.domain.exceptions.TreasuryDomainException;
import org.joda.time.DateTime;

import pt.ist.fenixframework.Atomic;

import com.google.common.collect.Sets;

public class SettlementNote extends SettlementNote_Base {

	protected SettlementNote() {
		super();
		setBennu(Bennu.getInstance());
	}

	protected void init(
			final FinantialDocumentType finantialDocumentType,
			final DebtAccount debtAccount,
			final DocumentNumberSeries documentNumberSeries,
			final Currency currency,
			final java.lang.String documentNumber,
			final org.joda.time.DateTime documentDate,
			final org.joda.time.DateTime documentDueDate,
			final java.lang.String originDocumentNumber,
			final org.fenixedu.treasury.domain.document.FinantialDocumentStateType state) {
		setFinantialDocumentType(finantialDocumentType);
		setDebtAccount(debtAccount);
		setDocumentNumberSeries(documentNumberSeries);
		setCurrency(currency);
		setDocumentNumber(documentNumber);
		setDocumentDate(documentDate);
		setDocumentDueDate(documentDueDate);
		setOriginDocumentNumber(originDocumentNumber);
		setState(state);
		checkRules();
	}

	protected void checkRules() {
		//
		// CHANGE_ME add more busines validations
		//
		if (getFinantialDocumentType() == null) {
			throw new TreasuryDomainException(
					"error.SettlementNote.finantialDocumentType.required");
		}

		if (getDebtAccount() == null) {
			throw new TreasuryDomainException(
					"error.SettlementNote.debtAccount.required");
		}

		if (getDocumentNumberSeries() == null) {
			throw new TreasuryDomainException(
					"error.SettlementNote.documentNumberSeries.required");
		}

		if (getCurrency() == null) {
			throw new TreasuryDomainException(
					"error.SettlementNote.currency.required");
		}

		// CHANGE_ME In order to validate UNIQUE restrictions
		// if (findByFinantialDocumentType(getFinantialDocumentType().count()>1)
		// {
		// throw new
		// TreasuryDomainException("error.SettlementNote.finantialDocumentType.duplicated");
		// }
		// if (findByDebtAccount(getDebtAccount().count()>1)
		// {
		// throw new
		// TreasuryDomainException("error.SettlementNote.debtAccount.duplicated");
		// }
		// if (findByDocumentNumberSeries(getDocumentNumberSeries().count()>1)
		// {
		// throw new
		// TreasuryDomainException("error.SettlementNote.documentNumberSeries.duplicated");
		// }
		// if (findByCurrency(getCurrency().count()>1)
		// {
		// throw new
		// TreasuryDomainException("error.SettlementNote.currency.duplicated");
		// }
		// if (findByDocumentNumber(getDocumentNumber().count()>1)
		// {
		// throw new
		// TreasuryDomainException("error.SettlementNote.documentNumber.duplicated");
		// }
		// if (findByDocumentDate(getDocumentDate().count()>1)
		// {
		// throw new
		// TreasuryDomainException("error.SettlementNote.documentDate.duplicated");
		// }
		// if (findByDocumentDueDate(getDocumentDueDate().count()>1)
		// {
		// throw new
		// TreasuryDomainException("error.SettlementNote.documentDueDate.duplicated");
		// }
		// if (findByOriginDocumentNumber(getOriginDocumentNumber().count()>1)
		// {
		// throw new
		// TreasuryDomainException("error.SettlementNote.originDocumentNumber.duplicated");
		// }
		// if (findByState(getState().count()>1)
		// {
		// throw new
		// TreasuryDomainException("error.SettlementNote.state.duplicated");
		// }
	}

	@Atomic
	public void edit(
			final FinantialDocumentType finantialDocumentType,
			final DebtAccount debtAccount,
			final DocumentNumberSeries documentNumberSeries,
			final Currency currency,
			final java.lang.String documentNumber,
			final org.joda.time.DateTime documentDate,
			final org.joda.time.DateTime documentDueDate,
			final java.lang.String originDocumentNumber,
			final org.fenixedu.treasury.domain.document.FinantialDocumentStateType state) {
		setFinantialDocumentType(finantialDocumentType);
		setDebtAccount(debtAccount);
		setDocumentNumberSeries(documentNumberSeries);
		setCurrency(currency);
		setDocumentNumber(documentNumber);
		setDocumentDate(documentDate);
		setDocumentDueDate(documentDueDate);
		setOriginDocumentNumber(originDocumentNumber);
		setState(state);
		checkRules();
	}

	public boolean isDeletable() {
		return true;
	}

	@Atomic
	public void delete() {
		if (!isDeletable()) {
			throw new TreasuryDomainException(
					"error.SettlementNote.cannot.delete");
		}

		setBennu(null);

		deleteDomainObject();
	}

	@Atomic
	public static SettlementNote create(
			final FinantialDocumentType finantialDocumentType,
			final DebtAccount debtAccount,
			final DocumentNumberSeries documentNumberSeries,
			final Currency currency,
			final java.lang.String documentNumber,
			final org.joda.time.DateTime documentDate,
			final org.joda.time.DateTime documentDueDate,
			final java.lang.String originDocumentNumber,
			final org.fenixedu.treasury.domain.document.FinantialDocumentStateType state) {
		SettlementNote settlementNote = new SettlementNote();
		settlementNote.init(finantialDocumentType, debtAccount,
				documentNumberSeries, currency, documentNumber, documentDate,
				documentDueDate, originDocumentNumber, state);
		return settlementNote;
	}

	// @formatter: off
	/************
	 * SERVICES *
	 ************/
	// @formatter: on

	public static Stream<SettlementNote> findAll() {
		return Bennu.getInstance().getFinantialDocumentsSet().stream()
				.filter(x -> x instanceof SettlementNote)
				.map(SettlementNote.class::cast);
	}

	public static Stream<SettlementNote> findByFinantialDocumentType(
			final FinantialDocumentType finantialDocumentType) {
		return findAll()
				.filter(i -> finantialDocumentType.equals(i
						.getFinantialDocumentType()));
	}

	public static Stream<SettlementNote> findByDebtAccount(
			final DebtAccount debtAccount) {
		return findAll().filter(i -> debtAccount.equals(i.getDebtAccount()));
	}

	public static Stream<SettlementNote> findByDocumentNumberSeries(
			final DocumentNumberSeries documentNumberSeries) {
		return findAll().filter(
				i -> documentNumberSeries.equals(i.getDocumentNumberSeries()));
	}

	public static Stream<SettlementNote> findByCurrency(final Currency currency) {
		return findAll().filter(i -> currency.equals(i.getCurrency()));
	}

	public static Stream<SettlementNote> findByDocumentNumber(
			final java.lang.String documentNumber) {
		return findAll().filter(
				i -> documentNumber.equalsIgnoreCase(i.getDocumentNumber()));
	}

	public static Stream<SettlementNote> findByDocumentDate(
			final org.joda.time.DateTime documentDate) {
		return findAll().filter(i -> documentDate.equals(i.getDocumentDate()));
	}

	public static Stream<SettlementNote> findByDocumentDueDate(
			final org.joda.time.DateTime documentDueDate) {
		return findAll().filter(
				i -> documentDueDate.equals(i.getDocumentDueDate()));
	}

	public static Stream<SettlementNote> findByOriginDocumentNumber(
			final java.lang.String originDocumentNumber) {
		return findAll().filter(
				i -> originDocumentNumber.equalsIgnoreCase(i
						.getOriginDocumentNumber()));
	}

	public static Stream<SettlementNote> findByState(
			final org.fenixedu.treasury.domain.document.FinantialDocumentStateType state) {
		return findAll().filter(i -> state.equals(i.getState()));
	}

}
