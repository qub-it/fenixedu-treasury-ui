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

import java.math.BigDecimal;
import java.util.stream.Stream;

import org.apache.commons.lang.StringUtils;
import org.fenixedu.bennu.core.domain.Bennu;
import org.fenixedu.bennu.core.domain.User;
import org.fenixedu.treasury.domain.FinantialInstitution;
import org.fenixedu.treasury.domain.exceptions.TreasuryDomainException;
import org.fenixedu.treasury.domain.paymentcodes.pool.PaymentCodePool;
import org.joda.time.DateTime;
import org.joda.time.LocalDate;

import pt.ist.fenixframework.Atomic;

public class PaymentReferenceCode extends PaymentReferenceCode_Base {
    private static final BigDecimal SIBS_IGNORE_MAX_AMOUNT = BigDecimal.ZERO;

    protected PaymentReferenceCode() {
        super();
        setBennu(Bennu.getInstance());
    }

    protected void init(final java.lang.String referenceCode, final org.joda.time.LocalDate beginDate,
            final org.joda.time.LocalDate endDate,
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
    public void edit(final java.lang.String referenceCode, final org.joda.time.LocalDate beginDate,
            final org.joda.time.LocalDate endDate,
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
            throw new TreasuryDomainException("error.PaymentReferenceCode.cannot.delete");
        }

        setBennu(null);

        deleteDomainObject();
    }

    @Atomic
    public static PaymentReferenceCode create(final java.lang.String referenceCode, final org.joda.time.LocalDate beginDate,
            final org.joda.time.LocalDate endDate,
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

    public static Stream<PaymentReferenceCode> findByReferenceCode(final java.lang.String referenceCode) {
        return findAll().filter(i -> referenceCode.equalsIgnoreCase(i.getReferenceCode()));
    }

    public static Stream<PaymentReferenceCode> findByBeginDate(final org.joda.time.LocalDate beginDate) {
        return findAll().filter(i -> beginDate.equals(i.getBeginDate()));
    }

    public static Stream<PaymentReferenceCode> findByEndDate(final org.joda.time.LocalDate endDate) {
        return findAll().filter(i -> endDate.equals(i.getEndDate()));
    }

    public static Stream<PaymentReferenceCode> findByState(
            final org.fenixedu.treasury.domain.paymentcodes.PaymentReferenceCodeStateType state) {
        return findAll().filter(i -> state.equals(i.getState()));
    }

//    protected PaymentCode() {
//        super();
//        super.setRootDomainObject(Bennu.getInstance());
//        super.setWhenCreated(new LocalDate());
//        super.setWhenUpdated(new LocalDate());
//        super.setState(PaymentCodeState.NEW);
//        super.setEntityCode(ENTITY_CODE);
//    }
//
//    protected void init(final PaymentCodeType paymentCodeType, final YearMonthDay startDate, final YearMonthDay endDate,
//            final Money minAmount, final Money maxAmount, final Person person) {
//
//        checkParameters(paymentCodeType, startDate, endDate, minAmount, maxAmount, person);
//
//        super.setCode(getPaymentCodeGenerator(paymentCodeType).generateNewCodeFor(paymentCodeType, person));
//
//        super.setType(paymentCodeType);
//        super.setStartDate(startDate);
//        super.setEndDate(endDate);
//        super.setMinAmount(minAmount);
//        super.setMaxAmount(maxAmount != null ? maxAmount : new Money(SIBS_IGNORE_MAX_AMOUNT));
//        super.setPerson(person);
//    }

//    private void checkParameters(PaymentCodeType paymentCodeType, YearMonthDay startDate, YearMonthDay endDate, Money minAmount,
//            Money maxAmount, final Person person) {
//
//        if (paymentCodeType == null) {
//            throw new DomainException("error.accounting.PaymentCode.paymentCodeType.cannot.be.null");
//        }
//
//        checkParameters(startDate, endDate, minAmount, maxAmount);
//    }
//
//    private void checkParameters(YearMonthDay startDate, YearMonthDay endDate, Money minAmount, Money maxAmount) {
//        if (startDate == null) {
//            throw new DomainException("error.accounting.PaymentCode.startDate.cannot.be.null");
//        }
//
//        if (endDate == null) {
//            throw new DomainException("error.accounting.PaymentCode.endDate.cannot.be.null");
//        }
//
//        if (minAmount == null) {
//            throw new DomainException("error.accounting.PaymentCode.minAmount.cannot.be.null");
//        }
//    }

    public String getFormattedCode() {
        final StringBuilder result = new StringBuilder();
        int i = 1;
        for (char character : getReferenceCode().toCharArray()) {
            result.append(character);
            if (i % 3 == 0) {
                result.append(" ");
            }
            i++;
        }

        return result.charAt(result.length() - 1) == ' ' ? result.deleteCharAt(result.length() - 1).toString() : result
                .toString();
    }

    @Override
    public void setReferenceCode(String code) {
        throw new TreasuryDomainException("error.accounting.PaymentCode.cannot.modify.code");
    }

//    @Override
//    public void setBeginDate(LocalDate startDate) {
//        throw new TreasuryDomainException("error.org.fenixedu.academic.domain.accounting.PaymentCode.cannot.modify.startDate");
//    }
//
//    @Override
//    public void setEndDate(LocalDate endDate) {
//        throw new DomainException("error.org.fenixedu.academic.domain.accounting.PaymentCode.cannot.modify.endDate");
//    }

    @Override
    public void setMinAmount(BigDecimal minAmount) {
        throw new TreasuryDomainException("error.org.fenixedu.academic.domain.accounting.PaymentCode.cannot.modify.minAmount");
    }

    @Override
    public void setMaxAmount(BigDecimal maxAmount) {
        throw new TreasuryDomainException("error.org.fenixedu.academic.domain.accounting.PaymentCode.cannot.modify.maxAmount");
    }

    @Override
    @Atomic
    public void setState(PaymentReferenceCodeStateType state) {
//        super.setWhenUpdated(new LocalDate());
        super.setState(state);
    }

//    @Override
//    public void setEntityCode(String entityCode) {
//        throw new DomainException("error.accounting.PaymentCode.cannot.modify.entityCode");
//    }

    public boolean isNew() {
        return getState() == PaymentReferenceCodeStateType.UNUSED;
    }

    protected void reuseCode() {
        setState(PaymentReferenceCodeStateType.UNUSED);
    }

    public boolean isUsed() {
        return getState() == PaymentReferenceCodeStateType.USED;
    }

    public boolean isAnnulled() {
        return getState() == PaymentReferenceCodeStateType.ANNULLED;
    }

//    public boolean isInvalid() {
//        return getState() == PaymentCodeState.INVALID;
//    }

    public void anull() {
        setState(PaymentReferenceCodeStateType.ANNULLED);
    }

    public boolean isAvailableForReuse() {
        return !isNew();
    }

    public void update(final LocalDate startDate, final LocalDate endDate, final BigDecimal minAmount, final BigDecimal maxAmount) {
        super.setBeginDate(startDate);
        super.setEndDate(endDate);
        super.setMinAmount(minAmount);
        super.setMaxAmount(maxAmount != null ? maxAmount : SIBS_IGNORE_MAX_AMOUNT);
        checkRules();
    }

    @Atomic
    public void process(User responsibleUser, BigDecimal amount, DateTime whenRegistered, String sibsTransactionId,
            String comments) {

        if (isUsed()) {
            return;
        }

        if (isAnnulled()) {
            throw new TreasuryDomainException("error.accounting.PaymentCode.cannot.process.invalid.codes");
        }

        internalProcess(responsibleUser, amount, whenRegistered, sibsTransactionId, comments);
//        if (!getPaymentCodePool().getType().isReusable()) {
//            setState(PaymentReferenceCodeStateType.USED);
//        }
    }

//    public void delete() {
//        super.setPerson(null);
//        for (PaymentCodeMapping mapping : getOldPaymentCodeMappingsSet()) {
//            mapping.delete();
//        }
//        for (PaymentCodeMapping mapping : getNewPaymentCodeMappingsSet()) {
//            removeNewPaymentCodeMappings(mapping);
//        }
//        setStudentCandidacy(null);
//
//        setRootDomainObject(null);
//        deleteDomainObject();
//    }

    public String getDescription() {
        return this.getPaymentCodePool().getEntityReferenceCode() + " " + this.getReferenceCode();
    }

    protected void internalProcess(final User user, final BigDecimal amount, final DateTime whenRegistered,
            final String sibsTransactionId, final String comments) {

    }

//    public PaymentCodeMapping getOldPaymentCodeMapping(final ExecutionYear executionYear) {
//        for (final PaymentCodeMapping mapping : getOldPaymentCodeMappingsSet()) {
//            if (mapping.has(executionYear)) {
//                return mapping;
//            }
//        }
//        return null;
//    }

    static public PaymentReferenceCode readByCode(final String code, FinantialInstitution finantialInstitution) {
        if (StringUtils.isEmpty(code)) {
            return null;
        }
        PaymentReferenceCode paymentReferenceCode = null;
        for (PaymentCodePool pool : finantialInstitution.getPaymentCodePoolsSet()) {
            paymentReferenceCode =
                    pool.getPaymentReferenceCodesSet().stream().filter(y -> y.getReferenceCode().equals(code)).findFirst()
                            .orElse(null);
            if (paymentReferenceCode != null) {
                break;
            }
        }
        return paymentReferenceCode;
    }

}
