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
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;
import java.util.stream.Stream;

import org.fenixedu.bennu.core.domain.Bennu;
import org.fenixedu.treasury.domain.exceptions.TreasuryDomainException;
import org.fenixedu.treasury.dto.InterestRateBean;
import org.fenixedu.treasury.util.Constants;
import org.joda.time.DateTimeConstants;
import org.joda.time.Days;
import org.joda.time.LocalDate;
import org.joda.time.Months;

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

    public boolean isMaximumDaysToApplyPenaltyApplied() {
        return getMaximumDaysToApplyPenalty() > 0;
    }

    public boolean isMaximumMonthsToApplyPenaltyApplied() {
        return getMaximumMonthsToApplyPenalty() > 0;
    }

    public boolean isApplyInFirstWorkday() {
        return getApplyInFirstWorkday();
    }

    @Atomic
    public void edit(final InterestType interestType, final int numberOfDaysAfterDueDate, final boolean applyInFirstWorkday,
            final int maximumDaysToApplyPenalty, final int maximumMonthsToApplyPenalty, final BigDecimal interestFixedAmount,
            final BigDecimal rate) {

        setInterestType(interestType);
        setNumberOfDaysAfterDueDate(numberOfDaysAfterDueDate);
        setApplyInFirstWorkday(applyInFirstWorkday);
        setMaximumDaysToApplyPenalty(maximumDaysToApplyPenalty);
        setMaximumMonthsToApplyPenalty(maximumMonthsToApplyPenalty);
        setInterestFixedAmount(interestFixedAmount);
        setRate(rate);

        checkRules();
    }

    public InterestRateBean calculateInterest(final Map<LocalDate, BigDecimal> amountInDebtMap,
            final Map<LocalDate, BigDecimal> createdInterestEntries, final LocalDate dueDate, final LocalDate paymentDate) {

        final TreeMap<LocalDate, BigDecimal> sortedMap = new TreeMap<LocalDate, BigDecimal>();
        sortedMap.putAll(amountInDebtMap);
        sortedMap.put(paymentDate, BigDecimal.ZERO);

        if (getInterestType().isFixedAmount()) {
            return calculedForFixedAmount();
        }

        if (getInterestType().isDaily()) {
            return calculateDaily(createdInterestEntries, dueDate, paymentDate, sortedMap);
        }

        if (getInterestType().isMonthly()) {
            return calculateMonthly(createdInterestEntries, dueDate, paymentDate, sortedMap);
        }

        throw new RuntimeException("unknown interest type formula");
    }

    private InterestRateBean calculateMonthly(final Map<LocalDate, BigDecimal> createdInterestEntries, final LocalDate dueDate,
            final LocalDate paymentDate, final TreeMap<LocalDate, BigDecimal> sortedMap) {
        final InterestRateBean result = new InterestRateBean(getInterestType());

        BigDecimal totalInterestAmount = BigDecimal.ZERO;
        int totalOfMonths = 0;

        LocalDate startDate = dueDate.plusMonths(1).withDayOfMonth(1);

        // Iterate over amountInDebtMap and calculate amountToPay
        BigDecimal amountInDebt = null;
        for (final Entry<LocalDate, BigDecimal> entry : sortedMap.entrySet()) {

            if (entry.getKey().isAfter(paymentDate)) {
                break;
            }

            if (entry.getKey().isBefore(startDate)) {
                amountInDebt = entry.getValue();
                continue;
            }

            final LocalDate endDate = entry.getKey();
            final int numberOfMonths = Months.monthsBetween(startDate, endDate).getMonths();
            final BigDecimal amountByRate = Constants.divide(getRate(), Constants.HUNDRED_PERCENT).multiply(amountInDebt);
            final BigDecimal partialInterestAmount = amountByRate.multiply(new BigDecimal(numberOfMonths));

            if (Constants.isPositive(partialInterestAmount)) {
                result.addDetail(partialInterestAmount, startDate, endDate, amountByRate);
            }

            totalInterestAmount = totalInterestAmount.add(partialInterestAmount);
            totalOfMonths += numberOfMonths;

            amountInDebt = entry.getValue();
            startDate = endDate;
        }

        if (createdInterestEntries != null) {
            final TreeMap<LocalDate, BigDecimal> interestSortedMap = new TreeMap<LocalDate, BigDecimal>();
            interestSortedMap.putAll(createdInterestEntries);

            for (final Entry<LocalDate, BigDecimal> entry : createdInterestEntries.entrySet()) {
                result.addCreatedInterestEntry(entry.getKey(), entry.getValue());

                totalInterestAmount = totalInterestAmount.subtract(entry.getValue());
            }
        }

        result.setInterestAmount(getTariff().getFinantialEntity().getFinantialInstitution().getCurrency()
                .getValueWithScale(totalInterestAmount));
        result.setNumberOfMonths(totalOfMonths);

        return result;
    }

    private InterestRateBean calculateDaily(final Map<LocalDate, BigDecimal> createdInterestEntries, final LocalDate dueDate,
            final LocalDate paymentDate, final TreeMap<LocalDate, BigDecimal> sortedMap) {
        final InterestRateBean result = new InterestRateBean(getInterestType());

        BigDecimal totalInterestAmount = BigDecimal.ZERO;
        int totalOfDays = 0;

        LocalDate startDate = applyOnFirstWorkdayIfNecessary(dueDate.plusDays(getNumberOfDaysAfterDueDate()));

        // Iterate over amountInDebtMap and calculate amountToPay
        BigDecimal amountInDebt = null;
        for (final Entry<LocalDate, BigDecimal> entry : sortedMap.entrySet()) {

            if (entry.getKey().isAfter(paymentDate)) {
                break;
            }

            if (entry.getKey().isBefore(startDate)) {
                amountInDebt = entry.getValue();
                continue;
            }

            final LocalDate endDate = entry.getKey();

            int numberOfDays = 0;
            BigDecimal partialInterestAmount;
            if (startDate.getYear() != endDate.getYear()) {
                boolean reachedMaxDays = false;

                int firstYearDays = Days.daysBetween(startDate, Constants.lastDayInYear(startDate.getYear())).getDays();
                int secondYearDays = Days.daysBetween(Constants.firstDayInYear(endDate.getYear()), endDate).getDays();

                {
                    if (isMaximumDaysToApplyPenaltyApplied() && (totalOfDays + firstYearDays) >= getMaximumDaysToApplyPenalty()) {
                        firstYearDays = getMaximumDaysToApplyPenalty() - totalOfDays;
                        reachedMaxDays = true;
                    }

                    final BigDecimal amountPerDay =
                            Constants.divide(amountInDebt, new BigDecimal(Constants.numberOfDaysInYear(startDate.getYear())));

                    partialInterestAmount =
                            Constants.divide(getRate(), Constants.HUNDRED_PERCENT).multiply(amountPerDay)
                                    .multiply(new BigDecimal(firstYearDays));

                    numberOfDays += firstYearDays;

                    if (Constants.isPositive(partialInterestAmount)) {
                        result.addDetail(partialInterestAmount, startDate, Constants.lastDayInYear(startDate.getYear()),
                                amountPerDay);
                    }
                }

                if (!reachedMaxDays) {

                    if (isMaximumDaysToApplyPenaltyApplied()
                            && (totalOfDays + firstYearDays + secondYearDays) >= getMaximumDaysToApplyPenalty()) {
                        firstYearDays = getMaximumDaysToApplyPenalty() - totalOfDays - firstYearDays;
                    }

                    final BigDecimal amountPerDay =
                            Constants.divide(amountInDebt, new BigDecimal(Constants.numberOfDaysInYear(endDate.getYear())));

                    final BigDecimal secondInterestAmount =
                            Constants.divide(getRate(), Constants.HUNDRED_PERCENT).multiply(amountPerDay)
                                    .multiply(new BigDecimal(firstYearDays));

                    if (Constants.isPositive(partialInterestAmount)) {
                        result.addDetail(secondInterestAmount, Constants.firstDayInYear(endDate.getYear()), endDate, amountPerDay);
                    }

                    partialInterestAmount = partialInterestAmount.add(secondInterestAmount);

                    numberOfDays += secondYearDays;
                }

            } else {
                numberOfDays = Days.daysBetween(startDate, endDate).getDays();

                if (endDate.isEqual(paymentDate)) {
                    numberOfDays++;
                }

                if (isMaximumDaysToApplyPenaltyApplied() && (totalOfDays + numberOfDays) >= getMaximumDaysToApplyPenalty()) {
                    numberOfDays = getMaximumDaysToApplyPenalty() - totalOfDays;
                }

                final BigDecimal amountPerDay =
                        Constants.divide(amountInDebt, new BigDecimal(Constants.numberOfDaysInYear(startDate.getYear())));
                partialInterestAmount =
                        Constants.divide(getRate(), Constants.HUNDRED_PERCENT).multiply(amountPerDay)
                                .multiply(new BigDecimal(numberOfDays));

                if (Constants.isPositive(partialInterestAmount)) {
                    result.addDetail(partialInterestAmount, startDate, endDate.isEqual(paymentDate) ? endDate : endDate.minusDays(1), amountPerDay);
                }
            }

            totalInterestAmount = totalInterestAmount.add(partialInterestAmount);
            totalOfDays += numberOfDays;

            amountInDebt = entry.getValue();
            startDate = endDate;

            if (isMaximumDaysToApplyPenaltyApplied() && totalOfDays >= getMaximumDaysToApplyPenalty()) {
                break;
            }
        }

        if (createdInterestEntries != null) {
            final TreeMap<LocalDate, BigDecimal> interestSortedMap = new TreeMap<LocalDate, BigDecimal>();
            interestSortedMap.putAll(createdInterestEntries);

            for (final Entry<LocalDate, BigDecimal> entry : createdInterestEntries.entrySet()) {
                result.addCreatedInterestEntry(entry.getKey(), entry.getValue());

                totalInterestAmount = totalInterestAmount.subtract(entry.getValue());
            }
        }

        result.setInterestAmount(getTariff().getFinantialEntity().getFinantialInstitution().getCurrency()
                .getValueWithScale(totalInterestAmount));
        result.setNumberOfDays(totalOfDays);

        return result;
    }

    private InterestRateBean calculedForFixedAmount() {
        final InterestRateBean result = new InterestRateBean(getInterestType());
        result.setInterestAmount(getTariff().getFinantialEntity().getFinantialInstitution().getCurrency()
                .getValueWithScale(getInterestFixedAmount()));

        return result;
    }

    private LocalDate applyOnFirstWorkdayIfNecessary(final LocalDate date) {
        if (isApplyInFirstWorkday() && isSaturday(date)) {
            return date.plusDays(1);
        } else if (isApplyInFirstWorkday() && isSunday(date)) {
            return date.plusDays(1);
        }

        return date;
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
        setTariff(null);

        deleteDomainObject();
    }

    private boolean isSaturday(final LocalDate date) {
        return date.getDayOfWeek() == DateTimeConstants.SATURDAY;
    }

    private boolean isSunday(final LocalDate date) {
        return date.getDayOfWeek() == DateTimeConstants.SUNDAY;
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
