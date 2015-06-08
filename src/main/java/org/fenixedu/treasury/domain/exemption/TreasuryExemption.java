package org.fenixedu.treasury.domain.exemption;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.fenixedu.bennu.core.domain.Bennu;
import org.fenixedu.bennu.core.i18n.BundleUtil;
import org.fenixedu.treasury.domain.Product;
import org.fenixedu.treasury.domain.debt.DebtAccount;
import org.fenixedu.treasury.domain.document.CreditEntry;
import org.fenixedu.treasury.domain.document.DebitEntry;
import org.fenixedu.treasury.domain.event.TreasuryEvent;
import org.fenixedu.treasury.domain.exceptions.TreasuryDomainException;
import org.fenixedu.treasury.util.Constants;
import org.joda.time.DateTime;

import com.google.common.base.Strings;

public class TreasuryExemption extends TreasuryExemption_Base {

    protected TreasuryExemption() {
        super();
        setBennu(Bennu.getInstance());
    }

    protected TreasuryExemption(final TreasuryExemptionType treasuryExemptionType, final TreasuryEvent treasuryEvent,
            final String reason, final boolean exemptByPercentage, final BigDecimal valueToExempt, final Product product) {
        this();

        if (treasuryEvent.getTreasuryExemption() != null) {
            throw new TreasuryDomainException("error.TreasuryExemption.treasuryEvent.with.exemption.already");
        }

        setTreasuryExemptionType(treasuryExemptionType);
        setTreasuryEvent(treasuryEvent);
        setReason(reason);
        setExemptByPercentage(exemptByPercentage);
        setValueToExempt(valueToExempt);

        setProduct(product);

        checkRules();

        exemptEventIfWithDebtEntries();
    }

    private void checkRules() {
        if (getTreasuryExemptionType() == null) {
            throw new TreasuryDomainException("error.TreasuryExemption.treasuryExemptionType.required");
        }

        if (getTreasuryEvent() == null) {
            throw new TreasuryDomainException("error.TreasuryExemption.treasuryEvent.required");
        }

        if (getValueToExempt() == null) {
            throw new TreasuryDomainException("error.TreasuryExemption.valueToExempt.required");
        }

        if (!Constants.isPositive(getValueToExempt())) {
            throw new TreasuryDomainException("error.TreasuryExemption.valueToExempt.positive.required");
        }

        if (Strings.isNullOrEmpty(getReason())) {
            throw new TreasuryDomainException("error.TreasuryExemption.reason.empty");
        }
    }

    public boolean isExemptByPercentage() {
        return super.getExemptByPercentage();
    }

    private void exemptEventIfWithDebtEntries() {

        // TreasuryEvent is not charged. Do nothing...
        if (!getTreasuryEvent().isChargedWithDebitEntry(getProduct())) {
            return;
        }

        // Some DebitEntry is already with CreditEntry. Ignore and do nothing...
        if (getTreasuryEvent().isAnyDebitEntryWithCreditApplied(getProduct())) {
            return;
        }

        // We're in conditions to create credit entries. But first
        // calculate the amount to exempt
        BigDecimal amountToExempt =
                isExemptByPercentage() ? Constants.divide(getTreasuryEvent().getAmountToPay(getProduct()), getValueToExempt()) : getValueToExempt();

        for (final DebitEntry debitEntry : (getProduct() != null ? DebitEntry.findActive(getTreasuryEvent()) : DebitEntry
                .findActive(getTreasuryEvent(), getProduct())).sorted(
                Collections.reverseOrder(DebitEntry.COMPARE_BY_OPEN_AMOUNT_WITH_VAT)).collect(Collectors.<DebitEntry> toList())) {

            final BigDecimal amountToUse =
                    Constants.isGreaterThan(debitEntry.getAmountWithVat(), amountToExempt) ? amountToExempt : debitEntry
                            .getAmountWithVat();

            BigDecimal amountWithoutVat = Constants.divide(amountToUse, BigDecimal.ONE.add(debitEntry.getVatRate()));

            final String description =
                    BundleUtil.getString(Constants.BUNDLE, "label.TreasuryExemption.credit.entry.exemption.description",
                            getTreasuryExemptionType().getName().getContent());

            CreditEntry.create(null, description, debitEntry.getProduct(), debitEntry.getVat(), amountWithoutVat, new DateTime(),
                    debitEntry, BigDecimal.ONE);

            amountToExempt = amountToExempt.min(amountToUse);

            if (!Constants.isPositive(amountToExempt)) {
                break;
            }
        }
    }

    private boolean isDeletable() {
        return true;
    }
    
    @pt.ist.fenixframework.Atomic
    public void delete() {
        if(!isDeletable()) {
            throw new TreasuryDomainException("error.TreasuryExemption.delete.impossible");
        }
        
        super.setBennu(null);
        
        super.setTreasuryExemptionType(null);
        super.setTreasuryEvent(null);
        super.setProduct(null);
        
        super.deleteDomainObject();
    }

    // @formatter: off
    /************
     * SERVICES *
     ************/
    // @formatter: on

    public static Stream<TreasuryExemption> find(final TreasuryExemptionType treasuryExemptionType) {
        return Bennu.getInstance().getTreasuryExemptionsSet().stream()
                .filter(t -> t.getTreasuryExemptionType() == treasuryExemptionType);
    }

    public static Stream<TreasuryExemption> find(final TreasuryEvent treasuryEvent) {
        return Bennu.getInstance().getTreasuryExemptionsSet().stream().filter(t -> t.getTreasuryEvent() == treasuryEvent);
    }

    public static Stream<TreasuryExemption> findByDebtAccount(final DebtAccount debtAccount) {
        return Bennu.getInstance().getTreasuryExemptionsSet().stream()
                .filter(t -> t.getTreasuryEvent().getDebtAccount() == debtAccount);
    }

    @pt.ist.fenixframework.Atomic
    public static TreasuryExemption create(final TreasuryExemptionType treasuryExemptionType, final TreasuryEvent treasuryEvent,
            final String reason, final boolean exemptByPercentage, final BigDecimal valueToExempt, final Product product) {
        return new TreasuryExemption(treasuryExemptionType, treasuryEvent, reason, exemptByPercentage, valueToExempt, product);
    }
}
