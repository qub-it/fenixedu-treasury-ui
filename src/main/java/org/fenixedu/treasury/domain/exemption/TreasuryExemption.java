package org.fenixedu.treasury.domain.exemption;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
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
            final String reason, final BigDecimal valueToExempt, final Product product, final boolean creditIfNecessary) {
        this();

        setTreasuryExemptionType(treasuryExemptionType);
        setTreasuryEvent(treasuryEvent);
        setReason(reason);

        /*
         * For now percentages are not supported because they are complex to deal with
         */
        setExemptByPercentage(false);
        setValueToExempt(valueToExempt);

        setProduct(product);

        checkRules();

        exemptEventIfWithDebtEntries(creditIfNecessary);
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

        if (getProduct() == null) {
            throw new TreasuryDomainException("error.TreasuryExemption.product.required");
        }

        if (Strings.isNullOrEmpty(getReason())) {
            throw new TreasuryDomainException("error.TreasuryExemption.reason.empty");
        }
        
        if(TreasuryExemption.find(getTreasuryEvent(), getProduct()).count() > 1) {
            throw new TreasuryDomainException("error.TreasuryExemption.for.product.already.created");
        }
    }

    public boolean isExemptByPercentage() {
        return super.getExemptByPercentage();
    }

    public BigDecimal getExemptedAmount(final TreasuryEvent treasuryEvent) {
        if (isExemptByPercentage()) {
            throw new TreasuryDomainException("error.TreasuryExemption.exempted.by.percentage.not.supported");
        }

        return getValueToExempt();
    }

    private void exemptEventIfWithDebtEntries(boolean creditIfNecessary) {
        if (!creditIfNecessary) {
            return;
        }

        // TreasuryEvent is not charged. Do nothing...
        if (!getTreasuryEvent().isChargedWithDebitEntry(getProduct())) {
            return;
        }

        // We're in conditions to create credit entries. But first
        // calculate the amount to exempt
        BigDecimal amountToExempt = getValueToExempt();

        final List<DebitEntry> activeDebitEntries =
                DebitEntry.findActive(getTreasuryEvent(), getProduct())
                        .sorted(Collections.reverseOrder(DebitEntry.COMPARE_BY_OPEN_AMOUNT_WITH_VAT))
                        .collect(Collectors.<DebitEntry> toList());

        for (final DebitEntry debitEntry : activeDebitEntries) {

            final BigDecimal amountToUse =
                    Constants.isGreaterThan(debitEntry.getAmountWithVat(), amountToExempt) ? amountToExempt : debitEntry
                            .getAmountWithVat();

            debitEntry.exempt(this, amountToUse);

            amountToExempt = amountToExempt.min(amountToUse);

            if (!Constants.isPositive(amountToExempt)) {
                break;
            }
        }
    }

    private void revertExemptionIfPossible() {
        // TreasuryEvent is not charged. Do nothing...
        if (!getTreasuryEvent().isChargedWithDebitEntry()) {
            return;
        }

        final List<DebitEntry> activeDebitEntries =
                DebitEntry.findActive(getTreasuryEvent(), getProduct())
                        .sorted(Collections.reverseOrder(DebitEntry.COMPARE_BY_OPEN_AMOUNT_WITH_VAT))
                        .collect(Collectors.<DebitEntry> toList());

        for (final DebitEntry debitEntry : activeDebitEntries) {

            if(!debitEntry.revertExemptionIfPossible(this)) {
                throw new TreasuryDomainException("error.TreasuryExemption.delete.impossible.due.to.processed.debit.or.credit.entry");
            }
        }        
    }

    private boolean isDeletable() {
        return true;
    }

    @pt.ist.fenixframework.Atomic
    public void delete() {
        if (!isDeletable()) {
            throw new TreasuryDomainException("error.TreasuryExemption.delete.impossible");
        }
        
        revertExemptionIfPossible();

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
    
    public static Stream<TreasuryExemption> find(final TreasuryEvent treasuryEvent, final Product product) {
        return find(treasuryEvent).filter(t -> t.getProduct() == product);
    }

    public static Stream<TreasuryExemption> findByDebtAccount(final DebtAccount debtAccount) {
        return Bennu.getInstance().getTreasuryExemptionsSet().stream()
                .filter(t -> t.getTreasuryEvent().getDebtAccount() == debtAccount);
    }

    @pt.ist.fenixframework.Atomic
    public static TreasuryExemption create(final TreasuryExemptionType treasuryExemptionType, final TreasuryEvent treasuryEvent,
            final String reason, final BigDecimal valueToExempt, final Product product, final boolean creditIfNecessary) {
        return new TreasuryExemption(treasuryExemptionType, treasuryEvent, reason, valueToExempt, product, creditIfNecessary);
    }

}
