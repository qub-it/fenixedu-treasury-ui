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
