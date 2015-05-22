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
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.fenixedu.treasury.domain.Currency;
import org.fenixedu.treasury.domain.Product;
import org.fenixedu.treasury.domain.Vat;
import org.fenixedu.treasury.domain.VatType;
import org.fenixedu.treasury.domain.debt.DebtAccount;
import org.fenixedu.treasury.domain.event.TreasuryEvent;
import org.fenixedu.treasury.domain.exceptions.TreasuryDomainException;
import org.fenixedu.treasury.domain.tariff.InterestRate;
import org.fenixedu.treasury.domain.tariff.Tariff;
import org.fenixedu.treasury.dto.InterestRateBean;
import org.joda.time.DateTime;
import org.joda.time.Days;
import org.joda.time.LocalDate;
import org.joda.time.Period;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

public class DebitEntry extends DebitEntry_Base {

    protected DebitEntry(final DebitNote debitNote, final DebtAccount debtAccount, final TreasuryEvent treasuryEvent,
            final Vat vat, final BigDecimal amount, final LocalDate dueDate, final Map<String, String> propertiesMap,
            final Product product, final String description, final BigDecimal quantity, final Tariff tariff) {
        init(debitNote, debtAccount, null, product, vat, amount, dueDate, propertiesMap, description, quantity, tariff);
    }

    @Override
    protected void init(final FinantialDocument finantialDocument, final DebtAccount debtAccount, final Product product,
            final FinantialEntryType finantialEntryType, final Vat vat, final BigDecimal amount, String description,
            BigDecimal quantity) {
        throw new RuntimeException("error.CreditEntry.use.init.without.finantialEntryType");
    }

    protected void init(final DebitNote debitNote, final DebtAccount debtAccount, final TreasuryEvent treasuryEvent,
            final Product product, final Vat vat, final BigDecimal amount, final LocalDate dueDate,
            final Map<String, String> propertiesMap, final String description, final BigDecimal quantity, final Tariff tariff) {
        super.init(debitNote, debtAccount, product, FinantialEntryType.DEBIT_ENTRY, vat, amount, description, quantity);

        setTreasuryEvent(treasuryEvent);
        setDueDate(dueDate);
        setPropertiesJsonMap(propertiesMapToJson(propertiesMap));
        setTariff(tariff);
        checkRules();
    }

    public String calculateInterestValue(LocalDate whenToCalculate) {
        StringBuilder interestDescription = new StringBuilder();
        InterestRate rate = getTariff().getInterestRate();

        Map<DateTime, BigDecimal> payments = new HashMap<DateTime, BigDecimal>();
        for (SettlementEntry settlementEntry : getSettlementEntriesSet()) {
            payments.put(settlementEntry.getFinantialDocument().getDocumentDate(), settlementEntry.getAmount());
        }

        BigDecimal amount = getAmountWithVat();
        LocalDate startDate = getDueDate();
        List<BigDecimal> interests = new ArrayList<BigDecimal>();
        for (DateTime date : payments.keySet().stream().sorted().collect(Collectors.toList())) {
            if (startDate.getYear() != date.getYear()) {
                int daysInPreviousYear = Days.daysBetween(startDate, new LocalDate(startDate.getYear(), 12, 31)).getDays();
                int daysInNextYear = Days.daysBetween(new LocalDate(date.getYear(), 1, 1), date.toLocalDate()).getDays();

            }
        }

        // TODOJN Auto-generated method stub
        return "<info sobre juros>";
    }

//
//    private BigDecimal getRateValueUsingDiaryRate(BigDecimal amount, LocalDate startDate, DateTime date, InterestRate rate) {
//        if( startDate.getYear() != date.getYear() ) {
//            int daysInPreviousYear = Days.daysBetween(startDate, new LocalDate(startDate.getYear(), 12, 31)).getDays();
//            int daysInNextYear = Days.daysBetween(new LocalDate(date.getYear(), 1, 1), date.toLocalDate()).getDays();
//            return amount.multiply(multiplicand)
//        }
//    }

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

    public BigDecimal getDebitAmount() {
        return this.getTotalAmount();
    }

    public BigDecimal getCreditAmount() {
        return Currency.getValueWithScale(BigDecimal.ZERO);
    }

    public boolean isEventAnnuled() {
        return getEventAnnuled();
    }

    public BigDecimal getOpenAmount() {
        final BigDecimal openAmount = this.getAmount().subtract(getPayedAmount());

        return Currency.getValueWithScale(isPositive(openAmount) ? openAmount : BigDecimal.ZERO);
    }

    public BigDecimal getAmountWithVat() {
        return getAmount().multiply(BigDecimal.ONE.add(getVat().getTaxRate().divide(BigDecimal.valueOf(100)))).setScale(2,
                RoundingMode.HALF_EVEN);
    }

    public BigDecimal getOpenAmountWithVat() {
        return getOpenAmount().multiply(BigDecimal.ONE.add(getVat().getTaxRate().divide(BigDecimal.valueOf(100)))).setScale(2,
                RoundingMode.HALF_EVEN);
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
            final Product product, final String description, final BigDecimal quantity, final Tariff tariff) {
        DebitEntry entry =
                new DebitEntry(debitNote, debtAccount, treasuryEvent, vat, amount, dueDate, propertiesMap, product, description,
                        quantity, tariff);
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
