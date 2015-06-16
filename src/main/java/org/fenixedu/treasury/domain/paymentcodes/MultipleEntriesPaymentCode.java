package org.fenixedu.treasury.domain.paymentcodes;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.fenixedu.bennu.core.domain.User;
import org.fenixedu.treasury.domain.FinantialInstitution;
import org.fenixedu.treasury.domain.debt.DebtAccount;
import org.fenixedu.treasury.domain.document.DocumentNumberSeries;
import org.fenixedu.treasury.domain.document.InvoiceEntry;
import org.fenixedu.treasury.domain.document.SettlementNote;
import org.fenixedu.treasury.domain.event.TreasuryEvent;
import org.fenixedu.treasury.domain.exceptions.TreasuryDomainException;
import org.fenixedu.treasury.domain.paymentcodes.pool.PaymentCodePool;
import org.joda.time.DateTime;

import pt.ist.fenixframework.Atomic;

public class MultipleEntriesPaymentCode extends MultipleEntriesPaymentCode_Base {

    public MultipleEntriesPaymentCode() {
        super();
    }

    @Override
    public boolean isMultipleEntriesPaymentCode() {
        return true;
    }

    @Override
    protected DocumentNumberSeries getDocumentSeriesForPayments() {
        return this.getPaymentReferenceCode().getPaymentCodePool().getDocumentSeriesForPayments();
    }

    @Override
    protected DebtAccount getReferenceDebtAccount() {
        //check the DebtAccount for the first "FinantialDocument" or from the "InvoiceEntry"

        if (this.getInvoiceEntriesSet().size() > 0) {
            return this.getInvoiceEntriesSet().iterator().next().getDebtAccount();
        }
        return null;
    }

    @Override
    public SettlementNote processPayment(User person, BigDecimal amountToPay, DateTime whenRegistered, String sibsTransactionId,
            String comments) {

        Set<InvoiceEntry> invoiceEntriesToPay =
                this.getInvoiceEntriesSet().stream().sorted((x, y) -> y.getOpenAmount().compareTo(x.getOpenAmount()))
                        .collect(Collectors.toSet());

        return internalProcessPayment(person, amountToPay, whenRegistered, sibsTransactionId, comments, invoiceEntriesToPay);
    }

    @Override
    public String getDescription() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public boolean isPaymentCodeFor(final TreasuryEvent event) {
        // TODO Auto-generated method stub
        return false;
    }

    protected void init(final PaymentReferenceCode paymentReferenceCode, final java.lang.Boolean valid) {
        setPaymentReferenceCode(paymentReferenceCode);
        setValid(valid);
        checkRules();
    }

    private void checkRules() {
        //
        //CHANGE_ME add more busines validations
        //
        if (getPaymentReferenceCode() == null) {
            throw new TreasuryDomainException("error.MultipleEntriesPaymentCode.paymentReferenceCode.required");
        }

    }

    @Atomic
    public void edit(final PaymentReferenceCode paymentReferenceCode, final java.lang.Boolean valid) {
        setPaymentReferenceCode(paymentReferenceCode);
        setValid(valid);
        checkRules();
    }

    @Override
    protected void checkForDeletionBlockers(Collection<String> blockers) {
        super.checkForDeletionBlockers(blockers);

        //add more logical tests for checking deletion rules
        //if (getXPTORelation() != null)
        //{
        //    blockers.add(BundleUtil.getString(Bundle.APPLICATION, "error.MultipleEntriesPaymentCode.cannot.be.deleted"));
        //}
    }

    @Atomic
    public void delete() {
        TreasuryDomainException.throwWhenDeleteBlocked(getDeletionBlockers());

        if (!isDeletable()) {
            throw new TreasuryDomainException("error.MultipleEntriesPaymentCode.cannot.delete");
        }

        deleteDomainObject();
    }

    private boolean isDeletable() {
        return false;
    }

    @Atomic
    public static MultipleEntriesPaymentCode create(final PaymentReferenceCode paymentReferenceCode, final java.lang.Boolean valid) {
        MultipleEntriesPaymentCode multipleEntriesPaymentCode = new MultipleEntriesPaymentCode();
        multipleEntriesPaymentCode.init(paymentReferenceCode, valid);
        return multipleEntriesPaymentCode;
    }

    // @formatter: off
    /************
     * SERVICES *
     ************/
    // @formatter: on

    public static Stream<MultipleEntriesPaymentCode> findAll(FinantialInstitution finantialInstitution) {
        Set<MultipleEntriesPaymentCode> entries = new HashSet<MultipleEntriesPaymentCode>();
        for (PaymentCodePool pool : finantialInstitution.getPaymentCodePoolsSet()) {
            for (PaymentReferenceCode code : pool.getPaymentReferenceCodesSet()) {
                if (code.getTargetPayment() != null && code.getTargetPayment() instanceof MultipleEntriesPaymentCode) {
                    entries.add((MultipleEntriesPaymentCode) code.getTargetPayment());
                }
            }
        }
        return entries.stream();
    }

    public static Stream<MultipleEntriesPaymentCode> findByValid(FinantialInstitution finantialInstitution,
            final java.lang.Boolean valid) {
        return findAll(finantialInstitution).filter(i -> valid.equals(i.getValid()));
    }

}
