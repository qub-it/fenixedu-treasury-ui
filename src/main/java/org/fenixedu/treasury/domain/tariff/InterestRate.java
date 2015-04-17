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
