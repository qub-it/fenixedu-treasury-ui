package org.fenixedu.treasury.domain.paymentcodes;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.fenixedu.bennu.core.domain.User;
import org.fenixedu.treasury.domain.FinantialInstitution;
import org.fenixedu.treasury.domain.debt.DebtAccount;
import org.fenixedu.treasury.domain.document.DebitEntry;
import org.fenixedu.treasury.domain.document.DebitNote;
import org.fenixedu.treasury.domain.document.DocumentNumberSeries;
import org.fenixedu.treasury.domain.document.InvoiceEntry;
import org.fenixedu.treasury.domain.document.PaymentEntry;
import org.fenixedu.treasury.domain.document.SettlementEntry;
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

    @Atomic
    protected SettlementNote internalProcessPayment(final User user, final BigDecimal amount, final DateTime whenRegistered,
            final String sibsTransactionId, final String comments) {

        //Process the payment of pending invoiceEntries
        //1. Find the InvoiceEntries
        //2. Create the SEttlementEntries and the SEttlementNote
        //2.1 create the "InterestRate entries"
        //3. Close the SettlementNote
        //4. If there is money for more, create a "pending" payment for being used later
        //5. Create a SibsTransactionDetail
        BigDecimal availableAmount = amount;

        List<DebitEntry> interestRateEntries = new ArrayList<DebitEntry>();
        DebitNote debitNoteForInterests = null;
        DebtAccount referenceDebtAccount = this.getReferenceDebtAccount();
        DocumentNumberSeries docNumberSeries = this.getDocumentSeriesForPayments();
        SettlementNote settlementNote = SettlementNote.create(referenceDebtAccount, docNumberSeries, new DateTime(), "");

        for (InvoiceEntry entry : this.getInvoiceEntriesSet().stream()
                .sorted((x, y) -> y.getOpenAmount().compareTo(x.getOpenAmount())).collect(Collectors.toList())) {
            if (availableAmount.compareTo(BigDecimal.ZERO) <= 0) {
                break;
            }

            BigDecimal amountToPay = entry.getOpenAmount();
            if (entry.isDebitNoteEntry()) {
                DebitEntry debitEntry = (DebitEntry) entry;
                //check if the amount to pay in the Debit Entry 
                if (amountToPay.compareTo(availableAmount) > 0) {
                    amountToPay = availableAmount;
                }

                if (debitEntry.getOpenAmount().equals(amountToPay)) {
                    //TODO
                    //Generate the InterestRate Entry if exists
//                    debitEntry.generateInterestRateDebitEntry(interest, when, debitNote);
                }

                SettlementEntry newSettlementEntry =
                        SettlementEntry.create(entry, settlementNote, amountToPay, entry.getDescription(), whenRegistered);

                //Update the amount to Pay
                availableAmount = availableAmount.subtract(amountToPay);

            } else if (entry.isCreditNoteEntry()) {
                SettlementEntry newSettlementEntry =
                        SettlementEntry.create(entry, settlementNote, entry.getOpenAmount(), entry.getDescription(),
                                whenRegistered);
                //update the amount to Pay
                availableAmount = availableAmount.add(amountToPay);
            }
        }

        //if "availableAmount" still exists, then we must check if there is any InterestRate to pay
        if (interestRateEntries.size() > 0) {
            for (DebitEntry interestEntry : interestRateEntries) {
                //Check if there is enough amount to Pay
                if (availableAmount.compareTo(BigDecimal.ZERO) <= 0) {
                    break;
                }

                BigDecimal amountToPay = interestEntry.getOpenAmount();
                //check if the amount to pay in the Debit Entry 
                if (amountToPay.compareTo(availableAmount) > 0) {
                    amountToPay = availableAmount;
                }

                SettlementEntry newSettlementEntry =
                        SettlementEntry.create(interestEntry, settlementNote, amountToPay, interestEntry.getDescription(),
                                whenRegistered);
                //Update the amount to Pay
                availableAmount = availableAmount.subtract(amountToPay);
            }
        }

        //if "availableAmount" still exists, then we must create a "pending Payment" or "CreditNote"
        if (availableAmount.compareTo(BigDecimal.ZERO) > 0) {
            //Create the CreditNote for this amount and
        }

        PaymentEntry paymentEntry =
                PaymentEntry.create(this.getPaymentReferenceCode().getPaymentCodePool().getPaymentMethod(), settlementNote,
                        amount);
        //process the SettlementEntries in a Settlement Note

        settlementNote.closeDocument();
        this.getPaymentReferenceCode().setState(PaymentReferenceCodeStateType.PROCESSED);
        return settlementNote;
    }

    private DocumentNumberSeries getDocumentSeriesForPayments() {
        return this.getPaymentReferenceCode().getPaymentCodePool().getDocumentSeriesForPayments();
    }

    private DebtAccount getReferenceDebtAccount() {
        //check the DebtAccount for the first "FinantialDocument" or from the "InvoiceEntry"

        if (this.getInvoiceEntriesSet().size() > 0) {
            return this.getInvoiceEntriesSet().iterator().next().getDebtAccount();
        }
        return null;
    }

    @Override
    public SettlementNote processPayment(User person, BigDecimal amountToPay, DateTime whenRegistered, String sibsTransactionId,
            String comments) {
        return internalProcessPayment(person, amountToPay, whenRegistered, sibsTransactionId, comments);
    }

    @Override
    public String getDescription(PaymentCodeTarget targetPaymentCode) {
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

        //CHANGE_ME In order to validate UNIQUE restrictions
        //if (findByPaymentReferenceCode(getPaymentReferenceCode().count()>1)
        //{
        //  throw new TreasuryDomainException("error.MultipleEntriesPaymentCode.paymentReferenceCode.duplicated");
        //} 
        //if (findByValid(getValid().count()>1)
        //{
        //  throw new TreasuryDomainException("error.MultipleEntriesPaymentCode.valid.duplicated");
        //} 
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
