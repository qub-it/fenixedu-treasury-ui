package org.fenixedu.treasury.domain.paymentCodes;

import java.util.stream.Stream;

import org.fenixedu.bennu.core.domain.Bennu;
import org.fenixedu.treasury.domain.exceptions.TreasuryDomainException;

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
			final org.fenixedu.treasury.domain.paymentCodes.PaymentReferenceCodeStateType state) {
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
			final org.fenixedu.treasury.domain.paymentCodes.PaymentReferenceCodeStateType state) {
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
			final org.fenixedu.treasury.domain.paymentCodes.PaymentReferenceCodeStateType state) {
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
			final org.fenixedu.treasury.domain.paymentCodes.PaymentReferenceCodeStateType state) {
		return findAll().filter(i -> state.equals(i.getState()));
	}

}
