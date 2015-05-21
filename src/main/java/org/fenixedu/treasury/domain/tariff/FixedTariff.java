/**
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
package org.fenixedu.treasury.domain.tariff;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Stream;

import org.fenixedu.bennu.core.domain.Bennu;
import org.fenixedu.treasury.domain.FinantialEntity;
import org.fenixedu.treasury.domain.FinantialInstitution;
import org.fenixedu.treasury.domain.Product;
import org.fenixedu.treasury.domain.VatType;
import org.fenixedu.treasury.domain.document.DebitNote;
import org.fenixedu.treasury.domain.exceptions.TreasuryDomainException;
import org.joda.time.DateTime;
import org.joda.time.LocalDate;

import pt.ist.fenixframework.Atomic;

public class FixedTariff extends FixedTariff_Base {

    protected FixedTariff(final FinantialEntity finantialEntity, final Product product, final DateTime beginDate,
            final DateTime endDate, final BigDecimal amount, final DueDateCalculationType dueDateCalculationType,
            final LocalDate fixedDueDate, final int numberOfDaysAfterCreationForDueDate, final boolean applyInterests,
            final InterestType interestType, final int numberOfDaysAfterDueDate, final boolean applyInFirstWorkday,
            final int maximumDaysToApplyPenalty, final int maximumMonthsToApplyPenalty, final BigDecimal interestFixedAmount,
            final BigDecimal rate) {
        super();

        init(finantialEntity, product, beginDate, endDate, amount, dueDateCalculationType, fixedDueDate,
                numberOfDaysAfterCreationForDueDate, applyInterests, interestType, numberOfDaysAfterDueDate, applyInFirstWorkday,
                maximumDaysToApplyPenalty, maximumMonthsToApplyPenalty, interestFixedAmount, rate);
    }

    @Override
    protected void init(final FinantialEntity finantialEntity, final Product product, final DateTime beginDate,
            final DateTime endDate, final DueDateCalculationType dueDateCalculationType, final LocalDate fixedDueDate,
            final int numberOfDaysAfterCreationForDueDate, final boolean applyInterests, final InterestType interestType,
            final int numberOfDaysAfterDueDate, final boolean applyInFirstWorkday, final int maximumDaysToApplyPenalty,
            final int maximumMonthsToApplyPenalty, final BigDecimal interestFixedAmount, final BigDecimal rate) {
        throw new RuntimeException("error.FixedTariff.use.init.with.amount");
    }

    protected void init(final FinantialEntity finantialEntity, final Product product, final DateTime beginDate,
            final DateTime endDate, final BigDecimal amount, final DueDateCalculationType dueDateCalculationType,
            LocalDate fixedDueDate, int numberOfDaysAfterCreationForDueDate, boolean applyInterests, InterestType interestType,
            int numberOfDaysAfterDueDate, boolean applyInFirstWorkday, int maximumDaysToApplyPenalty,
            int maximumMonthsToApplyPenalty, BigDecimal interestFixedAmount, BigDecimal rate) {
        super.init(finantialEntity, product, beginDate, endDate, dueDateCalculationType, fixedDueDate,
                numberOfDaysAfterCreationForDueDate, applyInterests, interestType, numberOfDaysAfterDueDate, applyInFirstWorkday,
                maximumDaysToApplyPenalty, maximumMonthsToApplyPenalty, interestFixedAmount, rate);

        setAmount(amount);
        checkRules();
    }

    protected FixedTariff() {
        super();
        setBennu(Bennu.getInstance());
    }

    protected void init(final Product product, final InterestRate interestRate, final FinantialEntity finantialEntity,
            final java.math.BigDecimal amount, final org.joda.time.DateTime beginDate, final org.joda.time.DateTime endDate,
            final org.fenixedu.treasury.domain.tariff.DueDateCalculationType dueDateCalculationType,
            final org.joda.time.LocalDate fixedDueDate, final int numberOfDaysAfterCreationForDueDate,
            final boolean applyInterests) {
        setProduct(product);
        setInterestRate(interestRate);
        setFinantialEntity(finantialEntity);
        setAmount(amount);
        setBeginDate(beginDate);
        setEndDate(endDate);
        setDueDateCalculationType(dueDateCalculationType);
        setFixedDueDate(fixedDueDate);
        setNumberOfDaysAfterCreationForDueDate(numberOfDaysAfterCreationForDueDate);
        setApplyInterests(applyInterests);
        checkRules();
    }

    public void checkRules() {
        super.checkRules();
        if (getProduct() == null) {
            throw new TreasuryDomainException("error.FixedTariff.product.required");
        }

        if (getFinantialEntity() == null) {
            throw new TreasuryDomainException("error.FixedTariff.finantialEntity.required");
        }

        if (this.getAmount() == null || this.getAmount().compareTo(BigDecimal.ZERO) <= 0) {
            throw new TreasuryDomainException("error.FixedTariff.amount.invalid");
        }

        //CHANGE_ME In order to validate UNIQUE restrictions
        //if (findByProduct(getProduct().count()>1)
        //{
        //  throw new TreasuryDomainException("error.FixedTariff.product.duplicated");
        //} 
        //if (findByInterestRate(getInterestRate().count()>1)
        //{
        //  throw new TreasuryDomainException("error.FixedTariff.interestRate.duplicated");
        //} 
        //if (findByFinantialEntity(getFinantialEntity().count()>1)
        //{
        //  throw new TreasuryDomainException("error.FixedTariff.finantialEntity.duplicated");
        //} 
        //if (findByAmount(getAmount().count()>1)
        //{
        //  throw new TreasuryDomainException("error.FixedTariff.amount.duplicated");
        //} 
        //if (findByBeginDate(getBeginDate().count()>1)
        //{
        //  throw new TreasuryDomainException("error.FixedTariff.beginDate.duplicated");
        //} 
        //if (findByEndDate(getEndDate().count()>1)
        //{
        //  throw new TreasuryDomainException("error.FixedTariff.endDate.duplicated");
        //} 
        //if (findByDueDateCalculationType(getDueDateCalculationType().count()>1)
        //{
        //  throw new TreasuryDomainException("error.FixedTariff.dueDateCalculationType.duplicated");
        //} 
        //if (findByFixedDueDate(getFixedDueDate().count()>1)
        //{
        //  throw new TreasuryDomainException("error.FixedTariff.fixedDueDate.duplicated");
        //} 
        //if (findByNumberOfDaysAfterCreationForDueDate(getNumberOfDaysAfterCreationForDueDate().count()>1)
        //{
        //  throw new TreasuryDomainException("error.FixedTariff.numberOfDaysAfterCreationForDueDate.duplicated");
        //} 
        //if (findByApplyInterests(getApplyInterests().count()>1)
        //{
        //  throw new TreasuryDomainException("error.FixedTariff.applyInterests.duplicated");
        //} 
    }

    @Atomic
    public void edit(final Product product, final InterestRate InterestRate, final FinantialEntity finantialEntity,
            final java.math.BigDecimal amount, final org.joda.time.DateTime beginDate, final org.joda.time.DateTime endDate,
            final org.fenixedu.treasury.domain.tariff.DueDateCalculationType dueDateCalculationType,
            final org.joda.time.LocalDate fixedDueDate, final int numberOfDaysAfterCreationForDueDate,
            final boolean applyInterests) {
        setProduct(product);
        setInterestRate(InterestRate);
        setFinantialEntity(finantialEntity);
        setAmount(amount);
        setBeginDate(beginDate);
        setEndDate(endDate);
        setDueDateCalculationType(dueDateCalculationType);
        setFixedDueDate(fixedDueDate);
        setNumberOfDaysAfterCreationForDueDate(numberOfDaysAfterCreationForDueDate);
        setApplyInterests(applyInterests);
        checkRules();
    }

    public boolean isDeletable() {
        return true;
    }

    @Atomic
    public void delete() {
        if (!isDeletable()) {
            throw new TreasuryDomainException("error.FixedTariff.cannot.delete");
        }

        super.delete();
    }

    @Atomic
    public static FixedTariff create(final Product product, final InterestRate interestRate,
            final FinantialEntity finantialEntity, final java.math.BigDecimal amount, final org.joda.time.DateTime beginDate,
            final org.joda.time.DateTime endDate,
            final org.fenixedu.treasury.domain.tariff.DueDateCalculationType dueDateCalculationType,
            final org.joda.time.LocalDate fixedDueDate, final int numberOfDaysAfterCreationForDueDate,
            final boolean applyInterests) {
        FixedTariff fixedTariff = new FixedTariff();
        fixedTariff.init(product, interestRate, finantialEntity, amount, beginDate, endDate, dueDateCalculationType,
                fixedDueDate, numberOfDaysAfterCreationForDueDate, applyInterests);
        return fixedTariff;
    }

    // @formatter: off
    /************
     * SERVICES *
     ************/
    // @formatter: on

    public static Stream<FixedTariff> findAll(FinantialInstitution institution) {
        Set<FixedTariff> result = new HashSet<FixedTariff>();
        Bennu.getInstance().getFinantialInstitutionsSet()
                .forEach(x -> x.getFinantialEntitiesSet().stream().forEach(y -> result.addAll(y.getFixedTariffSet())));
        return result.stream();
    }

    public static Stream<FixedTariff> findByProduct(final FinantialInstitution institution, final Product product) {
        return findAll(institution).filter(i -> product.equals(i.getProduct()));
    }

    public static Stream<FixedTariff> findByInterestRate(final FinantialInstitution institution, final InterestRate InterestRate) {
        return findAll(institution).filter(i -> InterestRate.equals(i.getInterestRate()));
    }

    public static Stream<FixedTariff> findByFinantialEntity(final FinantialInstitution institution,
            final FinantialEntity finantialEntity) {
        return findAll(institution).filter(i -> finantialEntity.equals(i.getFinantialEntity()));
    }

    public static Stream<FixedTariff> findByAmount(final FinantialInstitution institution, final java.math.BigDecimal amount) {
        return findAll(institution).filter(i -> amount.equals(i.getAmount()));
    }

    public static Stream<FixedTariff> findByBeginDate(final FinantialInstitution institution,
            final org.joda.time.DateTime beginDate) {
        return findAll(institution).filter(i -> beginDate.equals(i.getBeginDate()));
    }

    public static Stream<FixedTariff> findByEndDate(final FinantialInstitution institution, final org.joda.time.DateTime endDate) {
        return findAll(institution).filter(i -> endDate.equals(i.getEndDate()));
    }

    public static Stream<FixedTariff> findByDueDateCalculationType(final FinantialInstitution institution,
            final org.fenixedu.treasury.domain.tariff.DueDateCalculationType dueDateCalculationType) {
        return findAll(institution).filter(i -> dueDateCalculationType.equals(i.getDueDateCalculationType()));
    }

    public static Stream<FixedTariff> findByFixedDueDate(final FinantialInstitution institution,
            final org.joda.time.LocalDate fixedDueDate) {
        return findAll(institution).filter(i -> fixedDueDate.equals(i.getFixedDueDate()));
    }

    public static Stream<FixedTariff> findByNumberOfDaysAfterCreationForDueDate(final FinantialInstitution institution,
            final int numberOfDaysAfterCreationForDueDate) {
        return findAll(institution)
                .filter(i -> numberOfDaysAfterCreationForDueDate == i.getNumberOfDaysAfterCreationForDueDate());
    }

    public static Stream<FixedTariff> findByApplyInterests(final FinantialInstitution institution, final boolean applyInterests) {
        return findAll(institution).filter(i -> applyInterests == i.getApplyInterests());
    }

    @Override
    public String getUiAmount() {
        return this.getFinantialEntity().getFinantialInstitution().getCurrency().getValueFor(this.getAmount().setScale(3));
    }

    public LocalDate calculateDueDate(DebitNote finantialDocument) {
        if (this.getDueDateCalculationType().equals(DueDateCalculationType.DAYS_AFTER_CREATION)) {
            if (finantialDocument != null) {
                return finantialDocument.getDocumentDueDate().plusDays(this.getNumberOfDaysAfterCreationForDueDate())
                        .toLocalDate();
            } else {
                return new DateTime().plusDays(this.getNumberOfDaysAfterCreationForDueDate()).toLocalDate();
            }
        } else if (this.getDueDateCalculationType().equals(DueDateCalculationType.FIXED_DATE)) {
            return this.getFixedDueDate();
        } else {
            return null;
        }
    }

}
