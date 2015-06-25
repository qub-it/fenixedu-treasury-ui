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
import java.util.Collection;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.fenixedu.bennu.core.i18n.BundleUtil;
import org.fenixedu.treasury.domain.FinantialInstitution;
import org.fenixedu.treasury.domain.Product;
import org.fenixedu.treasury.domain.Vat;
import org.fenixedu.treasury.domain.debt.DebtAccount;
import org.fenixedu.treasury.domain.event.TreasuryEvent;
import org.fenixedu.treasury.domain.exceptions.TreasuryDomainException;
import org.fenixedu.treasury.domain.exemption.TreasuryExemption;
import org.fenixedu.treasury.domain.settings.TreasurySettings;
import org.fenixedu.treasury.domain.tariff.InterestRate;
import org.fenixedu.treasury.dto.InterestRateBean;
import org.fenixedu.treasury.util.Constants;
import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import org.springframework.util.StringUtils;

import pt.ist.fenixframework.Atomic;

import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

public class DebitEntry extends DebitEntry_Base {

    public static final Comparator<DebitEntry> COMPARE_BY_OPEN_AMOUNT_WITH_VAT = new Comparator<DebitEntry>() {

        @Override
        public int compare(final DebitEntry o1, final DebitEntry o2) {
            final int c = o1.getAmountWithVat().compareTo(o2.getAmountWithVat());

            return c != 0 ? c : o1.getExternalId().compareTo(o2.getExternalId());
        }
    };

    public static final Comparator<DebitEntry> COMPARE_BY_DUE_DATE = new Comparator<DebitEntry>() {

        @Override
        public int compare(DebitEntry o1, DebitEntry o2) {
            int c = o1.getDueDate().compareTo(o2.getDueDate());

            return c != 0 ? c : o1.getExternalId().compareTo(o2.getExternalId());
        }
    };

    public static final Comparator<DebitEntry> COMPARE_BY_EVENT_ANNULED_AND_BY_DATE = new Comparator<DebitEntry>() {

        @Override
        public int compare(DebitEntry o1, DebitEntry o2) {

            if (!o1.isEventAnnuled() && o2.isEventAnnuled()) {
                return -1;
            } else if (o1.isEventAnnuled() && !o2.isEventAnnuled()) {
                return 1;
            }

            int c = o1.getEntryDateTime().compareTo(o2.getEntryDateTime());

            return c != 0 ? c : o1.getExternalId().compareTo(o2.getExternalId());
        }

    };

    protected DebitEntry(final DebitNote debitNote, final DebtAccount debtAccount, final TreasuryEvent treasuryEvent,
            final Vat vat, final BigDecimal amount, final LocalDate dueDate, final Map<String, String> propertiesMap,
            final Product product, final String description, final BigDecimal quantity, final InterestRate interestRate,
            final DateTime entryDateTime) {
        init(debitNote, debtAccount, treasuryEvent, product, vat, amount, dueDate, propertiesMap, description, quantity,
                interestRate, entryDateTime);
    }

    @Override
    public boolean isDebitNoteEntry() {
        return true;
    }

    @Override
    protected void checkForDeletionBlockers(Collection<String> blockers) {
        super.checkForDeletionBlockers(blockers);

        getInterestDebitEntriesSet().stream().forEach(ide -> ide.checkForDeletionBlockers(blockers));
        if (!getCreditEntriesSet().isEmpty()) {
            blockers.add(BundleUtil.getString(Constants.BUNDLE, "error.DebitEntry.cannot.delete.has.creditentries"));
        }
    }

    @Override
    public void delete() {
        TreasuryDomainException.throwWhenDeleteBlocked(getDeletionBlockers());
        if (this.getInterestRate() != null) {
            InterestRate oldRate = this.getInterestRate();
            this.setInterestRate(null);
            oldRate.delete();
        }
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
            final Map<String, String> propertiesMap, final String description, final BigDecimal quantity,
            final InterestRate interestRate, final DateTime entryDateTime) {
        super.init(debitNote, debtAccount, product, FinantialEntryType.DEBIT_ENTRY, vat, amount, description, quantity,
                entryDateTime);

        setTreasuryEvent(treasuryEvent);
        setDueDate(dueDate);
        setPropertiesJsonMap(propertiesMapToJson(propertiesMap));
        setExemptedAmount(BigDecimal.ZERO);
        setInterestRate(interestRate);

        /* This property has academic significance but is meaningless in treasury scope
         * It is false by default but can be set with markAcademicalActBlockingSuspension
         * service method
         */
        setAcademicalActBlockingSuspension(false);

        checkRules();
    }

    public InterestRateBean calculateAllInterestValue(final LocalDate whenToCalculate) {
        if (this.getInterestRate() == null) {
            return new InterestRateBean();
        }

        if (!toCalculateInterests(whenToCalculate)) {
            return new InterestRateBean();
        }

        return this.getInterestRate().calculateInterest(amountInDebtMap(whenToCalculate),
                Maps.<LocalDate, BigDecimal> newHashMap(), getDueDate(), whenToCalculate);
    }

    public InterestRateBean calculateUndebitedInterestValue(final LocalDate whenToCalculate) {
        if (!this.isApplyInterests()) {
            return new InterestRateBean();
        }

        if (!toCalculateInterests(whenToCalculate)) {
            return new InterestRateBean();
        }

        return getInterestRate().calculateInterest(amountInDebtMap(whenToCalculate), createdInterestEntriesMap(), getDueDate(),
                whenToCalculate);
    }

    public boolean isApplyInterests() {
        return this.getInterestRate() != null;
    }

    private boolean toCalculateInterests(final LocalDate whenToCalculate) {
        return !whenToCalculate.isBefore(getDueDate().plusDays(getInterestRate().getNumberOfDaysAfterDueDate()));
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

        // If it exempted then it must be on itself or with credit entry but not both
        if (isPositive(getExemptedAmount()) && CreditEntry.findActive(getTreasuryEvent(), getProduct()).count() > 0) {
            throw new TreasuryDomainException(
                    "error.DebitEntry.exemption.cannot.be.on.debit.entry.and.with.credit.entry.at.same.time");
        }
    }

    @Override
    public boolean isFinantialDocumentRequired() {
        return false;
    }

    public boolean isEventAnnuled() {
        return getEventAnnuled();
    }

    @Override
    public BigDecimal getOpenAmount() {
        final BigDecimal openAmount = this.getAmountWithVat().subtract(getPayedAmount());

        return getCurrency().getValueWithScale(isPositive(openAmount) ? openAmount : BigDecimal.ZERO);
    }

    public BigDecimal getPayedAmount() {
        BigDecimal amount = BigDecimal.ZERO;
        for (SettlementEntry entry : this.getSettlementEntriesSet()) {
            if (entry.getFinantialDocument() != null && entry.getFinantialDocument().isClosed()) {
                amount = amount.add(entry.getTotalAmount());
            }
        }
        return amount;
    }

    public BigDecimal getRemainingAmount() {
        return getOpenAmount().subtract(getPayedAmount());
    }

    public boolean isInDebt() {
        return Constants.isPositive(getRemainingAmount());
    }

    public boolean isDueDateExpired(final LocalDate when) {
        return getDueDate().isBefore(when);
    }

    public Map<String, String> getPropertiesMap() {
        if (StringUtils.isEmpty(getPropertiesJsonMap())) {
            return null;
        }

        final GsonBuilder builder = new GsonBuilder();

        final Gson gson = builder.create();
        final Type stringStringMapType = new TypeToken<Map<String, String>>() {
        }.getType();

        final Map<String, String> propertiesMap = gson.fromJson(getPropertiesJsonMap(), stringStringMapType);

        return propertiesMap;
    }

    @Atomic
    public DebitEntry generateInterestRateDebitEntry(final InterestRateBean interest, final DateTime when,
            final DebitNote debitNote) {
        Product product = TreasurySettings.getInstance().getInterestProduct();

        if (product == null) {
            throw new TreasuryDomainException("error.SettlementNote.need.interest.product");
        }

        FinantialInstitution finantialInstitution = this.getDebtAccount().getFinantialInstitution();
        Vat vat = Vat.findActiveUnique(product.getVatType(), finantialInstitution, when).orElse(null);

        DebitEntry interestEntry =
                create(debitNote, getDebtAccount(), getTreasuryEvent(), vat, interest.getInterestAmount(), when.toLocalDate(),
                        propertiesJsonToMap(getPropertiesJsonMap()), product, interest.getDescription(), BigDecimal.ONE, null,
                        when);

        addInterestDebitEntries(interestEntry);

        return interestEntry;
    }

    public void edit(String description, BigDecimal amount, BigDecimal quantity, final TreasuryEvent treasuryEvent) {

        this.setDescription(description);
        this.setAmount(amount);
        this.setQuantity(quantity);
        this.setTreasuryEvent(treasuryEvent);

        recalculateAmountValues();

        checkRules();
    }

    public boolean isAcademicalActBlockingSuspension() {
        return getAcademicalActBlockingSuspension();
    }

    public boolean exempt(final TreasuryExemption treasuryExemption, final BigDecimal amountWithVat) {
        if (treasuryExemption.getTreasuryEvent() != getTreasuryEvent()) {
            throw new RuntimeException("wrong call");
        }

        if (treasuryExemption.getProduct() != getProduct()) {
            throw new RuntimeException("wrong call");
        }

        if (isEventAnnuled()) {
            throw new RuntimeException("error.DebitEntry.is.event.annuled.cannot.be.exempted");
        }

        final BigDecimal amountWithoutVat = Constants.divide(amountWithVat, BigDecimal.ONE.add(getVatRate()));

        if (isProcessedInClosedDebitNote()) {
            // If there is at least one credit entry from exemption then skip...
            if (CreditEntry.findActiveFromExemption(getTreasuryEvent(), getProduct()).count() > 0) {
                return false;
            }

            final String description =
                    BundleUtil.getString(Constants.BUNDLE, "label.TreasuryExemption.credit.entry.exemption.description",
                            treasuryExemption.getTreasuryExemptionType().getName().getContent());

            final DocumentNumberSeries defaultNumberSeries =
                    DocumentNumberSeries.findUniqueDefault(
                            FinantialDocumentType.findByFinantialDocumentType(FinantialDocumentTypeEnum.CREDIT_NOTE),
                            getDebtAccount().getFinantialInstitution()).get();

            final CreditNote creditNote =
                    CreditNote.create(getDebtAccount(), defaultNumberSeries, new DateTime(), (DebitNote) getFinantialDocument(),
                            null);
            CreditEntry.createFromExemption(treasuryExemption, creditNote, description, amountWithoutVat, new DateTime(), this);

        } else {
            BigDecimal originalAmount = getAmount();
            if (Constants.isPositive(getExemptedAmount())) {
                originalAmount = originalAmount.add(getExemptedAmount());
                setExemptedAmount(BigDecimal.ZERO);
            }

            setAmount(originalAmount.subtract(amountWithoutVat));
            setExemptedAmount(amountWithoutVat);

            recalculateAmountValues();
        }

        checkRules();

        return true;
    }

    public BigDecimal amountInDebt(final LocalDate paymentDate) {
        final Set<SettlementEntry> entries = new TreeSet<SettlementEntry>(SettlementEntry.COMPARATOR_BY_ENTRY_DATE_TIME);

        BigDecimal amountToPay = getAmountWithVat();
        for (final SettlementEntry settlementEntry : entries) {
            if (!settlementEntry.getEntryDateTime().toLocalDate().isBefore(paymentDate)) {
                break;
            }

            amountToPay = amountToPay.subtract(settlementEntry.getAmount());
        }

        return amountToPay;
    }

    public boolean revertExemptionIfPossible(final TreasuryExemption treasuryExemption) {
        // For all credit entries found that are not processed nor closed, delete
        for (final CreditEntry creditEntry : CreditEntry.findActiveFromExemption(getTreasuryEvent(), getProduct()).collect(
                Collectors.<CreditEntry> toSet())) {

            if (creditEntry.isProcessedInClosedDebitNote()) {
                return false;
            }

            creditEntry.delete();
            return true;
        }

        if (isProcessedInClosedDebitNote()) {
            return false;
        }

        setAmount(getAmount().add(getExemptedAmount()));
        setExemptedAmount(BigDecimal.ZERO);

        recalculateAmountValues();

        checkRules();

        return true;
    }

    @Atomic
    public void markAcademicalActBlockingSuspension() {
        setAcademicalActBlockingSuspension(true);
    }

    @Atomic
    public void annulOnEvent() {
        setEventAnnuled(true);
    }

    @Atomic
    public void revertEventAnnuled() {
        setEventAnnuled(false);
    }

    private Map<LocalDate, BigDecimal> amountInDebtMap(final LocalDate paymentDate) {
        final Map<LocalDate, BigDecimal> result = new HashMap<LocalDate, BigDecimal>();

        final Set<LocalDate> eventDates = Sets.newHashSet();

        eventDates.add(getDueDate());

        for (final SettlementEntry settlementEntry : getSettlementEntriesSet()) {
            eventDates.add(settlementEntry.getEntryDateTime().toLocalDate());
        }

        for (LocalDate date : eventDates) {
            result.put(date, amountInDebt(date));
        }

        return result;
    }

    private Map<LocalDate, BigDecimal> createdInterestEntriesMap() {
        final Map<LocalDate, BigDecimal> result = Maps.newHashMap();

        for (final DebitEntry interestDebitEntry : getInterestDebitEntriesSet()) {
            result.put(interestDebitEntry.getEntryDateTime().toLocalDate(), interestDebitEntry.getAmountWithVat());
        }

        return result;
    }

    protected String propertiesMapToJson(final Map<String, String> propertiesMap) {
        final GsonBuilder builder = new GsonBuilder();

        final Gson gson = builder.create();
        final Type stringStringMapType = new TypeToken<Map<String, String>>() {
        }.getType();

        return gson.toJson(propertiesMap, stringStringMapType);
    }

    protected Map<String, String> propertiesJsonToMap(final String propertiesMapJson) {
        final GsonBuilder builder = new GsonBuilder();

        final Gson gson = builder.create();
        final Type stringStringMapType = new TypeToken<Map<String, String>>() {
        }.getType();

        return gson.fromJson(propertiesMapJson, stringStringMapType);
    }

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
        return find(treasuryEvent).filter(d -> !d.isEventAnnuled());
    }

    public static Stream<? extends DebitEntry> findActive(final TreasuryEvent treasuryEvent, final Product product) {
        return findActive(treasuryEvent).filter(d -> d.getProduct() == product);
    }

    public static Stream<? extends DebitEntry> findEventAnnuled(final TreasuryEvent treasuryEvent) {
        return find(treasuryEvent).filter(d -> d.isEventAnnuled());
    }

    public static Stream<? extends DebitEntry> findEventAnnuled(final TreasuryEvent treasuryEvent, final Product product) {
        return findEventAnnuled(treasuryEvent).filter(d -> d.getProduct() == product);
    }

    public static BigDecimal payedAmount(final TreasuryEvent treasuryEvent) {
        return findActive(treasuryEvent).map(d -> d.getPayedAmount()).reduce((x, y) -> x.add(y)).orElse(BigDecimal.ZERO);
    }

    public static BigDecimal remainingAmountToPay(final TreasuryEvent treasuryEvent) {
        return findActive(treasuryEvent).map(d -> d.getRemainingAmount()).reduce((x, y) -> x.add(y)).orElse(BigDecimal.ZERO);
    }

    public static DebitEntry create(final DebitNote debitNote, final DebtAccount debtAccount, final TreasuryEvent treasuryEvent,
            final Vat vat, final BigDecimal amount, final LocalDate dueDate, final Map<String, String> propertiesMap,
            final Product product, final String description, final BigDecimal quantity, final InterestRate interestRate,
            final DateTime entryDateTime) {

        if (product.getActive() == false) {
            throw new TreasuryDomainException(Constants.BUNDLE, "error.DebitEntry.invalid.product.not.active");
        }
        DebitEntry entry =
                new DebitEntry(debitNote, debtAccount, treasuryEvent, vat, amount, dueDate, propertiesMap, product, description,
                        quantity, null, entryDateTime);
        if (interestRate != null) {
            //Clone InterestRate if needed
            if (interestRate.getTariff() != null || interestRate.getDebitEntry() != entry) {
                InterestRate copyInterest = InterestRate.createForDebitEntry(entry, interestRate);
                entry.changeInterestRate(copyInterest);
            }
        }
        entry.recalculateAmountValues();
        return entry;
    }

    @Atomic
    public void changeInterestRate(InterestRate newInterestRate) {
        if (this.getInterestRate() != null) {
            InterestRate oldRate = this.getInterestRate();
            this.setInterestRate(null);
            oldRate.delete();
        }
        this.setInterestRate(newInterestRate);
        checkRules();
    }
}
