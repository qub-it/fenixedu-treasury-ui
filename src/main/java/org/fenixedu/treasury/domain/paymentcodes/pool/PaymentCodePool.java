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
package org.fenixedu.treasury.domain.paymentcodes.pool;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.fenixedu.bennu.core.domain.Bennu;
import org.fenixedu.treasury.domain.FinantialInstitution;
import org.fenixedu.treasury.domain.PaymentMethod;
import org.fenixedu.treasury.domain.document.DocumentNumberSeries;
import org.fenixedu.treasury.domain.exceptions.TreasuryDomainException;
import org.fenixedu.treasury.services.payments.paymentscodegenerator.PaymentCodeGenerator;
import org.fenixedu.treasury.services.payments.paymentscodegenerator.SequentialPaymentWithCheckDigitCodeGenerator;
import org.fenixedu.treasury.util.LocalizedStringUtil;
import org.joda.time.LocalDate;

import pt.ist.fenixframework.Atomic;

import com.google.common.base.Strings;

public class PaymentCodePool extends PaymentCodePool_Base {

    protected PaymentCodePool() {
        super();
//		setBennu(Bennu.getInstance());
    }

    protected PaymentCodePool(final String name, final String entityReferenceCode, final Long minReferenceCode,
            final Long maxReferenceCode, final BigDecimal minAmount, final BigDecimal maxAmount, final LocalDate validFrom,
            final LocalDate validTo, final Boolean active, final Boolean useCheckDigit,
            final Boolean useAmountToValidateCheckDigit, final FinantialInstitution finantialInstitution,
            DocumentNumberSeries seriesToUseInPayments, PaymentMethod paymentMethod) {
        this();
        init(name, entityReferenceCode, minReferenceCode, maxReferenceCode, minAmount, maxAmount, validFrom, validTo, active,
                useCheckDigit, useAmountToValidateCheckDigit, finantialInstitution, seriesToUseInPayments, paymentMethod);
    }

    protected void init(final String name, final String entityReferenceCode, final Long minReferenceCode,
            final Long maxReferenceCode, final BigDecimal minAmount, final BigDecimal maxAmount, final LocalDate validFrom,
            final LocalDate validTo, final Boolean active, final Boolean useCheckDigit,
            final Boolean useAmountToValidateCheckDigit, final FinantialInstitution finantialInstitution,
            DocumentNumberSeries seriesToUseInPayments, PaymentMethod paymentMethod) {
        setName(name);
        setEntityReferenceCode(entityReferenceCode);
        setMinReferenceCode(minReferenceCode);
        setMaxReferenceCode(maxReferenceCode);
        setMinAmount(minAmount);
        setMaxAmount(maxAmount);
        setValidFrom(validFrom);
        setValidTo(validTo);
        setActive(active);
        setUseCheckDigit(useCheckDigit);
        setUseAmountToValidateCheckDigit(useAmountToValidateCheckDigit);
        setFinantialInstitution(finantialInstitution);
        setPaymentMethod(paymentMethod);
        setDocumentSeriesForPayments(seriesToUseInPayments);
        checkRules();
    }

    private void checkRules() {
        //
        // CHANGE_ME add more busines validations
        //

        Set<PaymentCodePool> allPools =
                PaymentCodePool.findByActive(true, this.getFinantialInstitution()).collect(Collectors.toSet());

        for (PaymentCodePool pool : allPools) {
            if (!pool.equals(this)) {
                if (this.getMinReferenceCode() >= pool.getMinReferenceCode()
                        && this.getMinReferenceCode() <= pool.getMaxReferenceCode()) {
                    throw new TreasuryDomainException("error.PaymentCodePool.invalid.reference.range.cross.other.pools");
                }

                if (this.getMaxReferenceCode() >= pool.getMinReferenceCode()
                        && this.getMaxReferenceCode() <= pool.getMinReferenceCode()) {
                    throw new TreasuryDomainException("error.PaymentCodePool.invalid.reference.range.cross.other.pools");
                }
            }
        }

        if (LocalizedStringUtil.isTrimmedEmpty(getName())) {
            throw new TreasuryDomainException("error.PaymentCodePool.name.required");
        }

        if (Strings.isNullOrEmpty(this.getEntityReferenceCode())) {
            throw new TreasuryDomainException("error.PaymentCodePool.entityReferenceCode.required");
        }

        if (this.getMinReferenceCode() <= 0 || this.getMinReferenceCode() >= this.getMaxReferenceCode()) {
            throw new TreasuryDomainException("error.PaymentCodePool.MinReferenceCode.invalid");
        }

        if (this.getValidFrom() == null) {
            throw new TreasuryDomainException("error.PaymentCodePool.validFrom.required");
        }

        if (this.getValidTo() == null) {
            throw new TreasuryDomainException("error.PaymentCodePool.validTo.required");
        }

        if (LocalizedStringUtil.isTrimmedEmpty(getName())) {
            throw new TreasuryDomainException("error.PaymentCodePool.name.required");
        }

        if (this.getMaxAmount().compareTo(this.getMinAmount()) < 0) {
            throw new TreasuryDomainException("error.PaymentCodePool.MinMaxAmount.invalid");
        }

        if (this.getValidTo().isBefore(this.getValidFrom())) {
            throw new TreasuryDomainException("error.PaymentCodePool.ValiddFrom.ValidTo.invalid");
        }

        if (this.getFinantialInstitution() == null) {
            throw new TreasuryDomainException("error.PaymentCodePool.finantialInstitution.required");
        }

        if (this.getDocumentSeriesForPayments() == null) {
            throw new TreasuryDomainException("error.PaymentCodePool.documentSeriesForPayments.required");
        }

        if (this.getPaymentMethod() == null) {
            throw new TreasuryDomainException("error.PaymentCodePool.paymentMethod.required");
        }

        if (this.getFinantialInstitution() != this.getDocumentSeriesForPayments().getSeries().getFinantialInstitution()) {
            throw new TreasuryDomainException(
                    "error.PaymentCodePool.documentNumberSeriesForPayments.invalid.finantialInstitution");
        }

    }

    @Atomic
    public void edit(final java.lang.String name, final java.lang.Long minPaymentCodes, final java.lang.Long maxPaymentCodes,
            final java.math.BigDecimal minAmount, final java.math.BigDecimal maxAmount, final java.lang.Boolean active,
            DocumentNumberSeries seriesToUseInPayments, PaymentMethod paymentMethod) {
        setName(name);
        setMinReferenceCode(minPaymentCodes);
        setMaxReferenceCode(maxPaymentCodes);
        setMinAmount(minAmount);
        setMaxAmount(maxAmount);
        setActive(active);
        setDocumentSeriesForPayments(seriesToUseInPayments);
        setPaymentMethod(paymentMethod);
        checkRules();
    }

    public boolean isDeletable() {
        return true;
    }

    @Atomic
    public void delete() {
        if (!isDeletable()) {
            throw new TreasuryDomainException("error.PaymentCodePool.cannot.delete");
        }

//		setBennu(null);

        deleteDomainObject();
    }

    @Atomic
    public static PaymentCodePool create(final String name, final String entityReferenceCode, final Long minReferenceCode,
            final Long maxReferenceCode, final BigDecimal minAmount, final BigDecimal maxAmount, final LocalDate validFrom,
            final LocalDate validTo, final Boolean active, final Boolean useCheckDigit,
            final Boolean useAmountToValidateCheckDigit, final FinantialInstitution finantialInstitution,
            DocumentNumberSeries seriesToUseInPayments, PaymentMethod paymentMethod) {
        return new PaymentCodePool(name, entityReferenceCode, minReferenceCode, maxReferenceCode, minAmount, maxAmount,
                validFrom, validTo, active, useCheckDigit, useAmountToValidateCheckDigit, finantialInstitution,
                seriesToUseInPayments, paymentMethod);

    }

    // @formatter: off
    /************
     * SERVICES *
     ************/
    // @formatter: on

    public static Stream<PaymentCodePool> findAll() {
        Set<PaymentCodePool> codes = new HashSet<PaymentCodePool>();

        return Bennu.getInstance().getFinantialInstitutionsSet().stream().map(x -> x.getPaymentCodePoolsSet())
                .reduce(codes, (a, b) -> {
                    a.addAll(b);
                    return a;
                }).stream();
    }

    public static Stream<PaymentCodePool> findByName(final java.lang.String name, final FinantialInstitution finantialInstitution) {
        return findByFinantialInstitution(finantialInstitution).filter(i -> name.equalsIgnoreCase(i.getName()));
    }

    public static Stream<PaymentCodePool> findByMinPaymentCodes(final java.lang.Integer minPaymentCodes,
            final FinantialInstitution finantialInstitution) {
        return findByFinantialInstitution(finantialInstitution).filter(i -> minPaymentCodes.equals(i.getMinReferenceCode()));
    }

    public static Stream<PaymentCodePool> findByMaxPaymentCodes(final java.lang.Integer maxPaymentCodes,
            final FinantialInstitution finantialInstitution) {
        return findByFinantialInstitution(finantialInstitution).filter(i -> maxPaymentCodes.equals(i.getMaxReferenceCode()));
    }

    public static Stream<PaymentCodePool> findByMinAmount(final java.math.BigDecimal minAmount,
            final FinantialInstitution finantialInstitution) {
        return findByFinantialInstitution(finantialInstitution).filter(i -> minAmount.equals(i.getMinAmount()));
    }

    public static Stream<PaymentCodePool> findByMaxAmount(final java.math.BigDecimal maxAmount,
            final FinantialInstitution finantialInstitution) {
        return findAll().filter(i -> maxAmount.equals(i.getMaxAmount()));
    }

    public static Stream<PaymentCodePool> findByActive(final java.lang.Boolean active,
            final FinantialInstitution finantialInstitution) {
        return findAll().filter(i -> active.equals(i.getActive()));
    }

    public static Stream<PaymentCodePool> findByFinantialInstitution(final FinantialInstitution finantialInstitution) {
        return findAll().filter(i -> i.getFinantialInstitution().equals(finantialInstitution));
    }

    private static PaymentCodeGenerator _referenceCodeGenerator;

    public PaymentCodeGenerator getReferenceCodeGenerator() {

        if (_referenceCodeGenerator == null) {
            if (Boolean.TRUE.equals(this.getUseCheckDigit())) {
                _referenceCodeGenerator = new SequentialPaymentWithCheckDigitCodeGenerator(this);
            } else {
                //Create a Sequencial CustomerFileCodeGenerator
            }
        }
        return _referenceCodeGenerator;
    }

    @Override
    public void setFinantialInstitution(FinantialInstitution finantialInstitution) {
        if (this.getPaymentReferenceCodesSet().size() > 0) {
            throw new TreasuryDomainException("error.PaymentCodePool.invalid.change.state.with.generated.references");
        }
        super.setFinantialInstitution(finantialInstitution);
    }

    @Atomic
    public void setNewValidPeriod(LocalDate validFrom, LocalDate validTo) {
        if (this.getPaymentReferenceCodesSet().size() > 0) {
            throw new TreasuryDomainException("error.PaymentCodePool.invalid.change.state.with.generated.references");
        }
        this.setValidFrom(validFrom);
        this.setValidTo(validTo);
        checkRules();
    }

    @Atomic
    public void changePooltype(Boolean useCheckDigit, Boolean useAmountToValidateCheckDigit) {
        if (this.getPaymentReferenceCodesSet().size() > 0) {
            throw new TreasuryDomainException("error.PaymentCodePool.invalid.change.state.with.generated.references");
        }

        this.setUseCheckDigit(useCheckDigit);
        this.setUseAmountToValidateCheckDigit(useAmountToValidateCheckDigit);
        checkRules();
    }

    @Atomic
    public void changeFinantialInstitution(FinantialInstitution finantialInstitution) {
        if (this.getPaymentReferenceCodesSet().size() > 0) {
            throw new TreasuryDomainException("error.PaymentCodePool.invalid.change.state.with.generated.references");
        }
        this.setFinantialInstitution(finantialInstitution);
        checkRules();

    }

}
