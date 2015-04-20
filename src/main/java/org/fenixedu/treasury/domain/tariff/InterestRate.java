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
import org.fenixedu.treasury.domain.exceptions.TreasuryDomainException;

import pt.ist.fenixframework.Atomic;

public class InterestRate extends InterestRate_Base {

    protected InterestRate() {
        super();
        setBennu(Bennu.getInstance());
    }

    protected InterestRate(final Tariff tariff, final InterestType interestType, final int numberOfDaysAfterDueDate,
            final boolean applyInFirstWorkday, final int maximumDaysToApplyPenalty, final int maximumMonthsToApplyPenalty,
            final BigDecimal interestFixedAmount, final BigDecimal rate) {
        this();

        setTariff(tariff);
        setInterestType(interestType);
        setNumberOfDaysAfterDueDate(numberOfDaysAfterDueDate);
        setApplyInFirstWorkday(applyInFirstWorkday);
        setMaximumDaysToApplyPenalty(maximumDaysToApplyPenalty);
        setMaximumMonthsToApplyPenalty(maximumMonthsToApplyPenalty);
        setInterestFixedAmount(interestFixedAmount);
        setRate(rate);

        checkRules();
    }

    private void checkRules() {
        if (getTariff() == null) {
            throw new TreasuryDomainException("error.InterestRate.product.required");
        }

        if (getInterestType() == null) {
            throw new TreasuryDomainException("error.InterestRate.interestType.required");
        }

        if ((getInterestType().isDaily() || getInterestType().isMonthly()) && getRate() == null) {
            throw new TreasuryDomainException("error.InterestRate.rate.required");
        }

        if (getInterestType().isFixedAmount() && getInterestFixedAmount() == null) {
            throw new TreasuryDomainException("error.InterestRate.interestFixedAmount.required");
        }
    }
    
    public boolean isDeletable() {
        return true;
    }

    @Atomic
    public void delete() {
        if (!isDeletable()) {
            throw new TreasuryDomainException("error.InterestRate.cannot.delete");
        }

        setBennu(null);

        deleteDomainObject();
    }

    // @formatter: off
    /************
     * SERVICES *
     ************/
    // @formatter: on

    public static Stream<InterestRate> findAll() {
        return Bennu.getInstance().getInterestRatesSet().stream();
    }

    @Atomic
    public static InterestRate create(final Tariff tariff, final InterestType interestType, final int numberOfDaysAfterDueDate,
            final boolean applyInFirstWorkday, final int maximumDaysToApplyPenalty, final int maximumMonthsToApplyPenalty,
            final BigDecimal interestFixedAmount, final BigDecimal rate) {
        return new InterestRate(tariff, interestType, numberOfDaysAfterDueDate, applyInFirstWorkday, maximumDaysToApplyPenalty,
                maximumMonthsToApplyPenalty, interestFixedAmount, rate);
    }

}
