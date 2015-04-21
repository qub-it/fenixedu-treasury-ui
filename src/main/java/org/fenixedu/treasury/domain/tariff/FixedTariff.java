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

import org.fenixedu.treasury.domain.Product;
import org.fenixedu.treasury.domain.VatType;
import org.fenixedu.treasury.domain.exceptions.TreasuryDomainException;
import org.joda.time.DateTime;
import org.joda.time.LocalDate;

public class FixedTariff extends FixedTariff_Base {

    protected FixedTariff(final Product product, final VatType vatType, final DateTime beginDate, final DateTime endDate,
            final BigDecimal amount, final DueDateCalculationType dueDateCalculationType, final LocalDate fixedDueDate,
            final int numberOfDaysAfterCreationForDueDate, final boolean applyInterests, final InterestType interestType,
            final int numberOfDaysAfterDueDate, final boolean applyInFirstWorkday, final int maximumDaysToApplyPenalty,
            final int maximumMonthsToApplyPenalty, final BigDecimal interestFixedAmount, final BigDecimal rate) {
        super();

        init(product, vatType, beginDate, endDate, amount, dueDateCalculationType, fixedDueDate,
                numberOfDaysAfterCreationForDueDate, applyInterests, interestType, numberOfDaysAfterDueDate, applyInFirstWorkday,
                maximumDaysToApplyPenalty, maximumMonthsToApplyPenalty, interestFixedAmount, rate);
    }

    @Override
    protected void init(final Product product, final VatType vatType, final DateTime beginDate, final DateTime endDate,
            final DueDateCalculationType dueDateCalculationType, final LocalDate fixedDueDate,
            final int numberOfDaysAfterCreationForDueDate, final boolean applyInterests, final InterestType interestType,
            final int numberOfDaysAfterDueDate, final boolean applyInFirstWorkday, final int maximumDaysToApplyPenalty,
            final int maximumMonthsToApplyPenalty, final BigDecimal interestFixedAmount, final BigDecimal rate) {
        throw new RuntimeException("error.FixedTariff.use.init.with.amount");
    }

    protected void init(Product product, VatType vatType, DateTime beginDate, DateTime endDate, final BigDecimal amount,
            final DueDateCalculationType dueDateCalculationType, LocalDate fixedDueDate, int numberOfDaysAfterCreationForDueDate,
            boolean applyInterests, InterestType interestType, int numberOfDaysAfterDueDate, boolean applyInFirstWorkday,
            int maximumDaysToApplyPenalty, int maximumMonthsToApplyPenalty, BigDecimal interestFixedAmount, BigDecimal rate) {
        super.init(product, vatType, beginDate, endDate, dueDateCalculationType, fixedDueDate,
                numberOfDaysAfterCreationForDueDate, applyInterests, interestType, numberOfDaysAfterDueDate, applyInFirstWorkday,
                maximumDaysToApplyPenalty, maximumMonthsToApplyPenalty, interestFixedAmount, rate);
        
        checkRules();
    }

    @Override
    protected void checkRules() {
        super.checkRules();
        
        if(getAmount() == null) {
            throw new TreasuryDomainException("error.FixedTariff.amount.required");
        }
    }
    
}
