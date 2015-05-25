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
package org.fenixedu.treasury.domain.document;

import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.fenixedu.treasury.domain.Currency;
import org.fenixedu.treasury.domain.Product;
import org.fenixedu.treasury.domain.Vat;
import org.fenixedu.treasury.domain.debt.DebtAccount;
import org.fenixedu.treasury.domain.event.TreasuryEvent;
import org.fenixedu.treasury.domain.exceptions.TreasuryDomainException;
import org.fenixedu.treasury.domain.tariff.InterestRate;
import org.fenixedu.treasury.domain.tariff.InterestType;
import org.fenixedu.treasury.domain.tariff.Tariff;
import org.fenixedu.treasury.dto.InterestRateBean;
import org.joda.time.DateTime;
import org.joda.time.Days;
import org.joda.time.LocalDate;

import pt.ist.fenixframework.Atomic;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

public class DebitEntry extends DebitEntry_Base {

    protected DebitEntry(final DebitNote debitNote, final DebtAccount debtAccount, final TreasuryEvent treasuryEvent,
            final Vat vat, final BigDecimal amount, final LocalDate dueDate, final Map<String, String> propertiesMap,
            final Product product, final String description, final BigDecimal quantity, final Tariff tariff,
            final DateTime entryDateTime) {
        init(debitNote, debtAccount, null, product, vat, amount, dueDate, propertiesMap, description, quantity, tariff,
                entryDateTime);
    }

    @Override
    public boolean isDebitNoteEntry() {
        return true;
    }

    @Override
    protected void checkForDeletionBlockers(Collection<String> blockers) {
        super.checkForDeletionBlockers(blockers);
    }

    @Override
    public void delete() {
        TreasuryDomainException.throwWhenDeleteBlocked(getDeletionBlockers());
        this.setTariff(null);
        this.setTreasuryEvent(null);
        super.delete();
    }

    @Override
    protected void init(final FinantialDocument finantialDocument, final DebtAccount debtAccount, final Product product,
            final FinantialEntryType finantialEntryType, final Vat vat, final BigDecimal amount, String description,
            BigDecimal quantity, DateTime entryDateTime) {
        throw new RuntimeException("error.CreditEntry.use.init.without.finantialEntryType");
    }

    protected void init(final DebitNote debitNote, final DebtAccount debtAccount, final TreasuryEvent treasuryEvent,
            final Product product, final Vat vat, final BigDecimal amount, final LocalDate dueDate,
            final Map<String, String> propertiesMap, final String description, final BigDecimal quantity, final Tariff tariff,
            final DateTime entryDateTime) {
        super.init(debitNote, debtAccount, product, FinantialEntryType.DEBIT_ENTRY, vat, amount, description, quantity,
                entryDateTime);

        setTreasuryEvent(treasuryEvent);
        setDueDate(dueDate);
        setPropertiesJsonMap(propertiesMapToJson(propertiesMap));
        setTariff(tariff);
        checkRules();
    }

    //TODOJN --
    @Atomic
    public InterestRateBean calculateInterestValue(LocalDate whenToCalculate) {
        //TODOJN - martelada..
        if (getTariff() != null && getTariff().getInterestRate() != null) {
            getTariff().getInterestRate();
        } else {
            Tariff tariff = Tariff.findAll().findAny().get();
            if (tariff.getInterestRate() == null) {
                InterestRate.create(tariff, InterestType.DAILY, 2, true, 50, 3, BigDecimal.valueOf(50.4), BigDecimal.valueOf(2));
            }
            setTariff(tariff);

        }
        //TODOJN - fim de martelada..
        InterestRate rate = getTariff().getInterestRate();

        Map<DateTime, BigDecimal> payments = new HashMap<DateTime, BigDecimal>();
        for (SettlementEntry settlementEntry : getSettlementEntriesSet()) {
            payments.put(settlementEntry.getFinantialDocument().getDocumentDate(), settlementEntry.getAmount());
        }

        BigDecimal openAmount = getAmountWithVat();
        LocalDate startDate = getDueDate();
        List<BigDecimal> interests = new ArrayList<BigDecimal>();
        for (DateTime date : payments.keySet().stream().sorted().collect(Collectors.toList())) {
            BigDecimal interestAmount = BigDecimal.ZERO;
            switch (rate.getInterestType()) {
            case DAILY:
                if (startDate.getYear() != date.getYear() /*&& rate.isGlobalRate()*/) {
                    int daysInPreviousYear = Days.daysBetween(startDate, new LocalDate(startDate.getYear(), 12, 31)).getDays();
                    //TODOJN - get global rate
                    interestAmount = interestAmount.add(getRateValueUsingDiaryRate(openAmount, daysInPreviousYear, rate));
                    int daysInNextYear = Days.daysBetween(new LocalDate(date.getYear(), 1, 1), date.toLocalDate()).getDays();
                    interestAmount = interestAmount.add(getRateValueUsingDiaryRate(openAmount, daysInNextYear, rate));
                } else {
                    int days = Days.daysBetween(startDate, date.toLocalDate()).getDays();
                    interestAmount = getRateValueUsingDiaryRate(openAmount, days, rate);
                }
                break;
            case MONTHLY:
                if (startDate.getYear() != date.getYear() /*&& rate.isGlobalRate()*/) {
                    int monthsInPreviousYear = 12 - startDate.getMonthOfYear();
                    //TODOJN - get global rate
                    interestAmount = interestAmount.add(getRateValueUsingMonthlyRate(openAmount, monthsInPreviousYear, rate));
                    int monthsInNextYear = date.getMonthOfYear() - 1;
                    interestAmount = interestAmount.add(getRateValueUsingMonthlyRate(openAmount, monthsInNextYear, rate));
                } else {
                    int months = date.getMonthOfYear() - startDate.getMonthOfYear();
                    interestAmount = getRateValueUsingMonthlyRate(openAmount, months, rate);
                }
                break;
            case FIXED_AMOUNT:
                interestAmount = rate.getInterestFixedAmount();
                break;
            }
            interests.add(interestAmount);
            openAmount.subtract(payments.get(date));
            startDate = date.toLocalDate();
        }

        BigDecimal totalInterestAmount = BigDecimal.ZERO;
        for (BigDecimal interestAmount : interests) {
            totalInterestAmount.add(interestAmount);
        }

        for (DebitEntry interestDebitEntry : getInterestDebitEntriesSet()) {
            totalInterestAmount.subtract(interestDebitEntry.getAmountWithVat());
        }

        return new InterestRateBean(totalInterestAmount, getInterestValueDescription());
    }

    private BigDecimal getRateValueUsingDiaryRate(BigDecimal amount, int days, InterestRate rate) {
        int numberOfDays =
                rate.getMaximumDaysToApplyPenalty() < days - rate.getNumberOfDaysAfterDueDate() ? rate
                        .getMaximumDaysToApplyPenalty() : days - rate.getNumberOfDaysAfterDueDate();
        return amount.multiply(BigDecimal.valueOf(numberOfDays / 365)).multiply(rate.getRate().divide(BigDecimal.valueOf(100)));
    }

    private BigDecimal getRateValueUsingMonthlyRate(BigDecimal amount, int months, InterestRate rate) {
        int numberOfMonths = rate.getMaximumMonthsToApplyPenalty() < months ? rate.getMaximumMonthsToApplyPenalty() : months;
        return amount.multiply(BigDecimal.valueOf(numberOfMonths)).multiply(rate.getRate().divide(BigDecimal.valueOf(100)));
    }

    public String getInterestValueDescription() {
        // TODOJN 
        return new String();
    }

    @Override
    protected void checkRules() {
        super.checkRules();

        if (getFinantialDocument() != null && !(getFinantialDocument() instanceof DebitNote)) {
            throw new TreasuryDomainException("error.DebitEntry.finantialDocument.not.debit.entry.type");
        }

        if (getDebtAccount() == null) {
            throw new TreasuryDomainException("error.DebitEntry.debtAccount.required");
        }

        if (getDueDate() == null) {
            throw new TreasuryDomainException("error.DebitEntry.dueDate.required");
        }

        if (this.getFinantialDocument() != null && this.getDueDate().isBefore(this.getFinantialDocument().getDocumentDueDate())) {
            throw new TreasuryDomainException("error.DebitEntry.dueDate.invalid");
        }

        if (this.getTariff() == null) {
            //HACK: Correct invalid, missing tariff
            Tariff t =
                    getProduct().getActiveTariffs(getDebtAccount().getFinantialInstitution(), this.getEntryDateTime())
                            .findFirst().orElse(null);

            if (t != null) {
                this.setTariff(t);
            } else {
                throw new TreasuryDomainException("error.DebitEntry.tariff.invalid");
            }
        }
    }

    protected String propertiesMapToJson(final Map<String, String> propertiesMap) {
        final GsonBuilder builder = new GsonBuilder();

        final Gson gson = builder.create();
        final Type stringStringMapType = new TypeToken<Map<String, String>>() {
        }.getType();

        return gson.toJson(propertiesMap, stringStringMapType);
    }

    @Override
    public boolean isFinantialDocumentRequired() {
        return false;
    }

//    @Override
//    public BigDecimal getDebitAmount() {
//        return this.getTotalAmount();
//    }
//
//    @Override
//    public BigDecimal getCreditAmount() {
//        return Currency.getValueWithScale(BigDecimal.ZERO);
//    }

    public boolean isEventAnnuled() {
        return getEventAnnuled();
    }

    @Override
    public BigDecimal getOpenAmount() {
        final BigDecimal openAmount = this.getAmount().subtract(getPayedAmount());

        return Currency.getValueWithScale(isPositive(openAmount) ? openAmount : BigDecimal.ZERO);
    }

    @Override
    public BigDecimal getAmountWithVat() {
        BigDecimal amount = getAmount().multiply(BigDecimal.ONE.add(getVat().getTaxRate().divide(BigDecimal.valueOf(100))));
        return Currency.getValueWithScale(amount);
    }

    public BigDecimal getOpenAmountWithVat() {
        BigDecimal amount = getOpenAmount().multiply(BigDecimal.ONE.add(getVat().getTaxRate().divide(BigDecimal.valueOf(100))));
        return Currency.getValueWithScale(amount);
    }

    public BigDecimal getPayedAmount() {
        return getSettlementEntriesSet().stream().map((x) -> x.getAmount()).reduce((x, y) -> x.add(y)).orElse(BigDecimal.ZERO);
    }

    public BigDecimal getRemainingAmount() {
        return getOpenAmount().subtract(getPayedAmount());
    }

    // @formatter: off
    /************
     * SERVICES *
     ************/
    // @formatter: on

    /* --- Find methods --- */

    public static Stream<? extends DebitEntry> findAll() {
        return FinantialDocumentEntry.findAll().filter(f -> f instanceof DebitEntry).map(DebitEntry.class::cast);
    }

    public static Stream<? extends DebitEntry> find(final DebitNote debitNote) {
        return findAll().filter(d -> d.getFinantialDocument() == debitNote);
    }

    public static Stream<? extends DebitEntry> find(final TreasuryEvent treasuryEvent) {
        return findAll().filter(d -> d.getTreasuryEvent() == treasuryEvent);
    }

    public static Stream<? extends DebitEntry> findActive(final TreasuryEvent treasuryEvent) {
        return find(treasuryEvent).filter(d -> d.isEventAnnuled());
    }

    /* --- Math methods --- */

    public static BigDecimal amountToPay(final TreasuryEvent treasuryEvent) {
        return findActive(treasuryEvent).map(d -> d.getAmount()).reduce((x, y) -> x.add(y)).get();
    }

    public static BigDecimal payedAmount(final TreasuryEvent treasuryEvent) {
        return findActive(treasuryEvent).map(d -> d.getPayedAmount()).reduce((x, y) -> x.add(y)).get();
    }

    public static BigDecimal remainingAmountToPay(final TreasuryEvent treasuryEvent) {
        return findActive(treasuryEvent).map(d -> d.getRemainingAmount()).reduce((x, y) -> x.add(y)).get();
    }

    /* --- Creation methods --- */

    public static DebitEntry create(final DebitNote debitNote, final DebtAccount debtAccount, final TreasuryEvent treasuryEvent,
            final Vat vat, final BigDecimal amount, final LocalDate dueDate, final Map<String, String> propertiesMap,
            final Product product, final String description, final BigDecimal quantity, final Tariff tariff,
            final DateTime entryDateTime) {
        DebitEntry entry =
                new DebitEntry(debitNote, debtAccount, treasuryEvent, vat, amount, dueDate, propertiesMap, product, description,
                        quantity, tariff, entryDateTime);
        entry.realculateAmountValues();
        return entry;
    }

    public void edit(String description, BigDecimal amount, BigDecimal quantity) {

        this.setDescription(description);
        this.setAmount(amount);
        this.setQuantity(quantity);
        realculateAmountValues();
        checkRules();

    }

}
