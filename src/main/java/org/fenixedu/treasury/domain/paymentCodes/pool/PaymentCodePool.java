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
package org.fenixedu.treasury.domain.paymentCodes.pool;

import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream;
import org.apache.commons.lang.StringUtils;
import org.fenixedu.bennu.core.domain.Bennu;
import org.fenixedu.commons.i18n.LocalizedString;
import org.fenixedu.treasury.domain.exceptions.TreasuryDomainException;
import org.fenixedu.treasury.util.LocalizedStringUtil;

import pt.ist.fenixframework.Atomic;

public class PaymentCodePool extends PaymentCodePool_Base {

	protected PaymentCodePool() {
		super();
		setBennu(Bennu.getInstance());
	}

	protected void init(final java.lang.String name,
			final java.lang.Integer minPaymentCodes,
			final java.lang.Integer maxPaymentCodes,
			final java.math.BigDecimal minAmount,
			final java.math.BigDecimal maxAmount, final java.lang.Boolean active) {
		setName(name);
		setMinPaymentCodes(minPaymentCodes);
		setMaxPaymentCodes(maxPaymentCodes);
		setMinAmount(minAmount);
		setMaxAmount(maxAmount);
		setActive(active);
		checkRules();
	}

	private void checkRules() {
		//
		// CHANGE_ME add more busines validations
		//

		// CHANGE_ME In order to validate UNIQUE restrictions
		// if (findByName(getName().count()>1)
		// {
		// throw new
		// TreasuryDomainException("error.PaymentCodePool.name.duplicated");
		// }
		// if (findByMinPaymentCodes(getMinPaymentCodes().count()>1)
		// {
		// throw new
		// TreasuryDomainException("error.PaymentCodePool.minPaymentCodes.duplicated");
		// }
		// if (findByMaxPaymentCodes(getMaxPaymentCodes().count()>1)
		// {
		// throw new
		// TreasuryDomainException("error.PaymentCodePool.maxPaymentCodes.duplicated");
		// }
		// if (findByMinAmount(getMinAmount().count()>1)
		// {
		// throw new
		// TreasuryDomainException("error.PaymentCodePool.minAmount.duplicated");
		// }
		// if (findByMaxAmount(getMaxAmount().count()>1)
		// {
		// throw new
		// TreasuryDomainException("error.PaymentCodePool.maxAmount.duplicated");
		// }
		// if (findByActive(getActive().count()>1)
		// {
		// throw new
		// TreasuryDomainException("error.PaymentCodePool.active.duplicated");
		// }
	}

	@Atomic
	public void edit(final java.lang.String name,
			final java.lang.Integer minPaymentCodes,
			final java.lang.Integer maxPaymentCodes,
			final java.math.BigDecimal minAmount,
			final java.math.BigDecimal maxAmount, final java.lang.Boolean active) {
		setName(name);
		setMinPaymentCodes(minPaymentCodes);
		setMaxPaymentCodes(maxPaymentCodes);
		setMinAmount(minAmount);
		setMaxAmount(maxAmount);
		setActive(active);
		checkRules();
	}

	public boolean isDeletable() {
		return true;
	}

	@Atomic
	public void delete() {
		if (!isDeletable()) {
			throw new TreasuryDomainException(
					"error.PaymentCodePool.cannot.delete");
		}

		setBennu(null);

		deleteDomainObject();
	}

	@Atomic
	public static PaymentCodePool create(final java.lang.String name,
			final java.lang.Integer minPaymentCodes,
			final java.lang.Integer maxPaymentCodes,
			final java.math.BigDecimal minAmount,
			final java.math.BigDecimal maxAmount, final java.lang.Boolean active) {
		PaymentCodePool paymentCodePool = new PaymentCodePool();
		paymentCodePool.init(name, minPaymentCodes, maxPaymentCodes, minAmount,
				maxAmount, active);
		return paymentCodePool;
	}

	// @formatter: off
	/************
	 * SERVICES *
	 ************/
	// @formatter: on

	public static Stream<PaymentCodePool> findAll() {
		return Bennu.getInstance().getPaymentCodePoolsSet().stream();
	}

	public static Stream<PaymentCodePool> findByName(final java.lang.String name) {
		return findAll().filter(i -> name.equalsIgnoreCase(i.getName()));
	}

	public static Stream<PaymentCodePool> findByMinPaymentCodes(
			final java.lang.Integer minPaymentCodes) {
		return findAll().filter(
				i -> minPaymentCodes.equals(i.getMinPaymentCodes()));
	}

	public static Stream<PaymentCodePool> findByMaxPaymentCodes(
			final java.lang.Integer maxPaymentCodes) {
		return findAll().filter(
				i -> maxPaymentCodes.equals(i.getMaxPaymentCodes()));
	}

	public static Stream<PaymentCodePool> findByMinAmount(
			final java.math.BigDecimal minAmount) {
		return findAll().filter(i -> minAmount.equals(i.getMinAmount()));
	}

	public static Stream<PaymentCodePool> findByMaxAmount(
			final java.math.BigDecimal maxAmount) {
		return findAll().filter(i -> maxAmount.equals(i.getMaxAmount()));
	}

	public static Stream<PaymentCodePool> findByActive(
			final java.lang.Boolean active) {
		return findAll().filter(i -> active.equals(i.getActive()));
	}

}
