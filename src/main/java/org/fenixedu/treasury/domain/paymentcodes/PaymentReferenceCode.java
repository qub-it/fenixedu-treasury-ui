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
package org.fenixedu.treasury.domain.paymentcodes;

import java.util.stream.Stream;

import org.fenixedu.bennu.core.domain.Bennu;
import org.fenixedu.treasury.domain.exceptions.TreasuryDomainException;
import org.fenixedu.treasury.domain.paymentcodes.PaymentReferenceCode_Base;

import pt.ist.fenixframework.Atomic;

public class PaymentReferenceCode extends PaymentReferenceCode_Base {
	protected PaymentReferenceCode() {
		super();
		setBennu(Bennu.getInstance());
	}

	protected void init(
			final java.lang.String referenceCode,
			final org.joda.time.DateTime beginDate,
			final org.joda.time.DateTime endDate,
			final org.fenixedu.treasury.domain.paymentcodes.PaymentReferenceCodeStateType state) {
		setReferenceCode(referenceCode);
		setBeginDate(beginDate);
		setEndDate(endDate);
		setState(state);
		checkRules();
	}

	private void checkRules() {
		//
		// CHANGE_ME add more busines validations
		//

		// CHANGE_ME In order to validate UNIQUE restrictions
		// if (findByReferenceCode(getReferenceCode().count()>1)
		// {
		// throw new
		// TreasuryDomainException("error.PaymentReferenceCode.referenceCode.duplicated");
		// }
		// if (findByBeginDate(getBeginDate().count()>1)
		// {
		// throw new
		// TreasuryDomainException("error.PaymentReferenceCode.beginDate.duplicated");
		// }
		// if (findByEndDate(getEndDate().count()>1)
		// {
		// throw new
		// TreasuryDomainException("error.PaymentReferenceCode.endDate.duplicated");
		// }
		// if (findByState(getState().count()>1)
		// {
		// throw new
		// TreasuryDomainException("error.PaymentReferenceCode.state.duplicated");
		// }
	}

	@Atomic
	public void edit(
			final java.lang.String referenceCode,
			final org.joda.time.DateTime beginDate,
			final org.joda.time.DateTime endDate,
			final org.fenixedu.treasury.domain.paymentcodes.PaymentReferenceCodeStateType state) {
		setReferenceCode(referenceCode);
		setBeginDate(beginDate);
		setEndDate(endDate);
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
					"error.PaymentReferenceCode.cannot.delete");
		}

		setBennu(null);

		deleteDomainObject();
	}

	@Atomic
	public static PaymentReferenceCode create(
			final java.lang.String referenceCode,
			final org.joda.time.DateTime beginDate,
			final org.joda.time.DateTime endDate,
			final org.fenixedu.treasury.domain.paymentcodes.PaymentReferenceCodeStateType state) {
		PaymentReferenceCode paymentReferenceCode = new PaymentReferenceCode();
		paymentReferenceCode.init(referenceCode, beginDate, endDate, state);
		return paymentReferenceCode;
	}

	// @formatter: off
	/************
	 * SERVICES *
	 ************/
	// @formatter: on

	public static Stream<PaymentReferenceCode> findAll() {
		return Bennu.getInstance().getPaymentReferenceCodesSet().stream();
	}

	public static Stream<PaymentReferenceCode> findByReferenceCode(
			final java.lang.String referenceCode) {
		return findAll().filter(
				i -> referenceCode.equalsIgnoreCase(i.getReferenceCode()));
	}

	public static Stream<PaymentReferenceCode> findByBeginDate(
			final org.joda.time.DateTime beginDate) {
		return findAll().filter(i -> beginDate.equals(i.getBeginDate()));
	}

	public static Stream<PaymentReferenceCode> findByEndDate(
			final org.joda.time.DateTime endDate) {
		return findAll().filter(i -> endDate.equals(i.getEndDate()));
	}

	public static Stream<PaymentReferenceCode> findByState(
			final org.fenixedu.treasury.domain.paymentcodes.PaymentReferenceCodeStateType state) {
		return findAll().filter(i -> state.equals(i.getState()));
	}

}
