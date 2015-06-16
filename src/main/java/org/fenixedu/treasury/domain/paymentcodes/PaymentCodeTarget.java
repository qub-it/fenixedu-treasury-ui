package org.fenixedu.treasury.domain.paymentcodes;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.fenixedu.bennu.core.domain.User;
import org.fenixedu.treasury.domain.debt.DebtAccount;
import org.fenixedu.treasury.domain.document.DebitEntry;
import org.fenixedu.treasury.domain.document.DebitNote;
import org.fenixedu.treasury.domain.document.DocumentNumberSeries;
import org.fenixedu.treasury.domain.document.InvoiceEntry;
import org.fenixedu.treasury.domain.document.PaymentEntry;
import org.fenixedu.treasury.domain.document.SettlementEntry;
import org.fenixedu.treasury.domain.document.SettlementNote;
import org.fenixedu.treasury.domain.event.TreasuryEvent;
import org.joda.time.DateTime;

import pt.ist.fenixframework.Atomic;

public abstract class PaymentCodeTarget extends PaymentCodeTarget_Base {

    public PaymentCodeTarget() {
        super();
    }

    public abstract SettlementNote processPayment(final User person, final BigDecimal amountToPay, DateTime whenRegistered,
            String sibsTransactionId, String comments);

    public abstract String getDescription();

    public String getTargetPayorDescription() {
        if (getReferenceDebtAccount() != null) {
            return getReferenceDebtAccount().getCustomer().getCode() + "-" + getReferenceDebtAccount().getCustomer().getName();
        }
        return "----";
    }

    public abstract boolean isPaymentCodeFor(final TreasuryEvent event);

    public boolean isMultipleEntriesPaymentCode() {
        return false;
    }

    public boolean isFinantialDocumentPaymentCode() {
        return false;
    }

    @Atomic
    protected SettlementNote internalProcessPayment(final User user, final BigDecimal amount, final DateTime whenRegistered,
            final String sibsTransactionId, final String comments, Set<InvoiceEntry> invoiceEntriesToPay) {

        //Process the payment of pending invoiceEntries
        //1. Find the InvoiceEntries
        //2. Create the SEttlementEntries and the SEttlementNote
        //2.1 create the "InterestRate entries"
        //3. Close the SettlementNote
        //4. If there is money for more, create a "pending" payment (CreditNote) for being used later
        //5. Create a SibsTransactionDetail
        BigDecimal availableAmount = amount;

        List<DebitEntry> interestRateEntries = new ArrayList<DebitEntry>();
        DebitNote debitNoteForInterests = null;
        DebtAccount referenceDebtAccount = this.getReferenceDebtAccount();
        DocumentNumberSeries docNumberSeries = this.getDocumentSeriesForPayments();
        SettlementNote settlementNote = SettlementNote.create(referenceDebtAccount, docNumberSeries, new DateTime(), comments);

        for (InvoiceEntry entry : invoiceEntriesToPay) {
            if (availableAmount.compareTo(BigDecimal.ZERO) <= 0) {
                break;
            }

            BigDecimal amountToPay = entry.getOpenAmount();
            if (amountToPay.compareTo(BigDecimal.ZERO) > 0) {
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
            } else {
                //Ignore since the "open amount" is ZERO
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
            settlementNote.createAdvancedPaymentCreditNote(availableAmount, "Adiantamento em pagamento SIBS" + comments + "-"
                    + sibsTransactionId);
        }

        PaymentEntry paymentEntry =
                PaymentEntry.create(this.getPaymentReferenceCode().getPaymentCodePool().getPaymentMethod(), settlementNote,
                        amount);
        //process the SettlementEntries in a Settlement Note

        settlementNote.closeDocument();
        this.getPaymentReferenceCode().setState(PaymentReferenceCodeStateType.PROCESSED);
        return settlementNote;
    }

    protected abstract DocumentNumberSeries getDocumentSeriesForPayments();

    protected abstract DebtAccount getReferenceDebtAccount();

}
