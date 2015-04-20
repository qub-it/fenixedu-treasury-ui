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
package org.fenixedu.treasury.domain.tariff;

import java.math.BigDecimal;
import java.util.stream.Stream;

import org.fenixedu.bennu.core.domain.Bennu;
import org.fenixedu.treasury.domain.Product;
import org.fenixedu.treasury.domain.VatType;
import org.fenixedu.treasury.domain.exceptions.TreasuryDomainException;
import org.joda.time.DateTime;
import org.joda.time.Interval;
import org.joda.time.LocalDate;

import pt.ist.fenixframework.Atomic;

public abstract class Tariff extends Tariff_Base {

    protected Tariff() {
        super();
        setBennu(Bennu.getInstance());
    }

    protected void init(final Product product, final VatType vatType, final DateTime beginDate, final DateTime endDate,
            final DueDateCalculationType dueDateCalculationType, final LocalDate fixedDueDate,
            final int numberOfDaysAfterCreationForDueDate, final boolean applyInterests, final InterestType interestType,
            final int numberOfDaysAfterDueDate, final boolean applyInFirstWorkday, final int maximumDaysToApplyPenalty,
            final int maximumMonthsToApplyPenalty, final BigDecimal interestFixedAmount, final BigDecimal rate) {
        setProduct(product);
        setVatType(vatType);
        setBeginDate(beginDate);
        setEndDate(endDate);
        setDueDateCalculationType(dueDateCalculationType);
        setFixedDueDate(fixedDueDate);
        setNumberOfDaysAfterCreationForDueDate(numberOfDaysAfterCreationForDueDate);
        setApplyInterests(applyInterests);
        
        if(getApplyInterests()) {
            InterestRate.create(this, interestType, numberOfDaysAfterCreationForDueDate, applyInFirstWorkday, maximumDaysToApplyPenalty, maximumMonthsToApplyPenalty, interestFixedAmount, rate);
        }
        
        checkRules();
    }

    protected void checkRules() {
        if(getProduct() == null) {
            throw new TreasuryDomainException("error.Tariff.product.required");
        }
        
        if(getVatType() == null) {
            throw new TreasuryDomainException("error.Tariff.vatType.required");
        }
        
        if(getBeginDate() == null) {
            throw new TreasuryDomainException("error.Tariff.beginDate.required");
        }
        
        if(getEndDate() != null && !getEndDate().isAfter(getBeginDate())) {
            throw new TreasuryDomainException("error.Tariff.endDate.must.be.after.beginDate");
        }
        
        if(getDueDateCalculationType() == null) {
            throw new TreasuryDomainException("error.Tariff.dueDateCalculationType.required");
        }
        
        if(getDueDateCalculationType().isFixedDate() && getFixedDueDate() == null) {
            throw new TreasuryDomainException("error.Tariff.fixedDueDate.required");
        }
        
        if(getFixedDueDate().toDateTimeAtStartOfDay().plusDays(1).minusSeconds(1).isBefore(getBeginDate())) {
            throw new TreasuryDomainException("error.Tariff.fixedDueDate.must.be.after.or.equal.beginDate");
        }
        
        if(getDueDateCalculationType().isDaysAfterCreation() && getNumberOfDaysAfterCreationForDueDate() < 0) {
            throw new TreasuryDomainException("error.Tariff.numberOfDaysAfterCreationForDueDate.must.be.positive");
        }
        
        if(getApplyInterests() && getInterestRate() == null) {
            throw new TreasuryDomainException("error.Tariff.interestRate.required");
        }
    }
    
    public abstract BigDecimal getAmount();
    
    public boolean isActive(final DateTime when) {
        return new Interval(getBeginDate(), getEndDate()).contains(when);
    }
    
    public boolean isActive(final Interval dateInterval) {
        return new Interval(getBeginDate(), getEndDate()).overlaps(dateInterval);
    }

    @Atomic
    public void edit(final DateTime beginDate, final DateTime endDate) {
        checkRules();
    }

    public boolean isDeletable() {
        return true;
    }

    @Atomic
    public void delete() {
        if (!isDeletable()) {
            throw new TreasuryDomainException("error.Tariff.cannot.delete");
        }

        setBennu(null);
        deleteDomainObject();
    }

    // @formatter: off
    /************
     * SERVICES *
     ************/
    // @formatter: on

    public static Stream<Tariff> findAll() {
        return Bennu.getInstance().getTariffsSet().stream();
    }
    
    public static Stream<Tariff> find(final Product product) {
        return findAll().filter(t -> t.getProduct() == product);
    }
    
    public static Stream<Tariff> find(final Product product, final DateTime when) {
        return find(product).filter(t -> t.isActive(when));
    }
    
    public static Stream<Tariff> findInInterval(final Product product, final DateTime start, final DateTime end) {
        final Interval interval = new Interval(start, end);
        return find(product).filter(t -> t.isActive(interval));
    }
    
}
