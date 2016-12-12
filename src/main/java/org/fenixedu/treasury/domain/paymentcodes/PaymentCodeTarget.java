package org.fenixedu.treasury.domain.paymentcodes;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.TreeSet;

import org.fenixedu.bennu.core.domain.Bennu;
import org.fenixedu.bennu.core.domain.User;
import org.fenixedu.bennu.core.i18n.BundleUtil;
import org.fenixedu.treasury.domain.debt.DebtAccount;
import org.fenixedu.treasury.domain.document.DebitEntry;
import org.fenixedu.treasury.domain.document.DebitNote;
import org.fenixedu.treasury.domain.document.DocumentNumberSeries;
import org.fenixedu.treasury.domain.document.FinantialDocumentType;
import org.fenixedu.treasury.domain.document.InvoiceEntry;
import org.fenixedu.treasury.domain.document.PaymentEntry;
import org.fenixedu.treasury.domain.document.SettlementEntry;
import org.fenixedu.treasury.domain.document.SettlementNote;
import org.fenixedu.treasury.domain.event.TreasuryEvent;
import org.fenixedu.treasury.dto.InterestRateBean;
import org.fenixedu.treasury.util.Constants;
import org.joda.time.DateTime;
import org.joda.time.LocalDate;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;

import pt.ist.fenixframework.Atomic;

public abstract class PaymentCodeTarget extends PaymentCodeTarget_Base {

    public PaymentCodeTarget() {
        super();
    }

    public abstract SettlementNote processPayment(final User person, final BigDecimal amountToPay, DateTime whenRegistered,
            String sibsTransactionId, String comments);

    public abstract String getDescription();

    public String getTargetPayorDescription() {
        if (getDebtAccount() != null) {
            return getDebtAccount().getCustomer().getBusinessIdentification() + "-" + getDebtAccount().getCustomer().getName();
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

        final TreeSet<InvoiceEntry> sortedInvoiceEntriesToPay = Sets.newTreeSet(InvoiceEntry.COMPARE_BY_AMOUNT_AND_DUE_DATE);
        sortedInvoiceEntriesToPay.addAll(invoiceEntriesToPay);

        //Process the payment of pending invoiceEntries
        //1. Find the InvoiceEntries
        //2. Create the SEttlementEntries and the SEttlementNote
        //2.1 create the "InterestRate entries"
        //2.2 if there is pending amount, pay the interest rate
        //3. If there is pending amount, try to pay a Pending DebitEntries
        //3.1 create the interestRate entries for pending debit entries
        //3.2 if there is pending amount, try to pay interesrtRate for pending debit entries
        //4. If there is money for more, create a "pending" payment (CreditNote) for being used later
        //5. Close the SettlementNote
        //6. Create a SibsTransactionDetail
        BigDecimal availableAmount = amount;

        List<DebitEntry> interestRateEntries = new ArrayList<DebitEntry>();
        DebitNote debitNoteForInterests = null;
        DebtAccount referenceDebtAccount = this.getDebtAccount();
        DocumentNumberSeries docNumberSeries = this.getDocumentSeriesForPayments();
        SettlementNote settlementNote =
                SettlementNote.create(referenceDebtAccount, docNumberSeries, new DateTime(), whenRegistered, comments, null);

        //######################################
        //1. Find the InvoiceEntries
        //2. Create the SEttlementEntries and the SEttlementNote
        //######################################
        for (InvoiceEntry entry : sortedInvoiceEntriesToPay) {
            if (availableAmount.compareTo(BigDecimal.ZERO) <= 0) {
                break;
            }

            BigDecimal amountToPay = entry.getOpenAmount();
            if (amountToPay.compareTo(BigDecimal.ZERO) > 0) {
                if (entry.isDebitNoteEntry()) {
                    DebitEntry debitEntry = (DebitEntry) entry;

                    if (debitEntry.getFinantialDocument() == null) {
                        final DocumentNumberSeries documentNumberSeries =
                                DocumentNumberSeries.findUniqueDefault(FinantialDocumentType.findForDebitNote(),
                                        getPaymentReferenceCode().getPaymentCodePool().getFinantialInstitution()).get();
                        final DebitNote debitNote = DebitNote.create(debitEntry.getDebtAccount(), documentNumberSeries, new DateTime());
                        debitNote.addDebitNoteEntries(Lists.newArrayList(debitEntry));
                    }

                    if (debitEntry.getFinantialDocument().isPreparing()) {
                        debitEntry.getFinantialDocument().closeDocument();
                    }

                    //check if the amount to pay in the Debit Entry 
                    if (amountToPay.compareTo(availableAmount) > 0) {
                        amountToPay = availableAmount;
                    }

                    if (debitEntry.getOpenAmount().equals(amountToPay)) {
                        //######################################
                        //2.1 create the "InterestRate entries"
                        //######################################
                        InterestRateBean calculateUndebitedInterestValue =
                                debitEntry.calculateUndebitedInterestValue(whenRegistered.toLocalDate());
                        if (Constants.isPositive(calculateUndebitedInterestValue.getInterestAmount())) {
                            DebitEntry interestDebitEntry = debitEntry.createInterestRateDebitEntry(
                                    calculateUndebitedInterestValue, whenRegistered, Optional.<DebitNote> empty());
                            interestRateEntries.add(interestDebitEntry);
                        }
                    }

                    SettlementEntry newSettlementEntry = SettlementEntry.create(entry, settlementNote, amountToPay,
                            entry.getDescription(), whenRegistered, true);

                    //Update the amount to Pay
                    availableAmount = availableAmount.subtract(amountToPay);

                } else if (entry.isCreditNoteEntry()) {
                    SettlementEntry newSettlementEntry = SettlementEntry.create(entry, settlementNote, entry.getOpenAmount(),
                            entry.getDescription(), whenRegistered, true);
                    //update the amount to Pay
                    availableAmount = availableAmount.add(amountToPay);
                }
            } else {
                //Ignore since the "open amount" is ZERO
            }
        }

        //######################################
        //2.2 if there is pending amount, pay the interest rate
        //######################################
        //if we created interestRateEntries then we must close them in a document and try to pay with availableAmount
        if (interestRateEntries.size() > 0) {
            //Create a DebitNote for the Interests DebitEntries
            DebitNote interestNote =
                    DebitNote.create(referenceDebtAccount, this.getDocumentSeriesInterestDebits(), whenRegistered);
            for (DebitEntry interestEntry : interestRateEntries) {
                interestEntry.setFinantialDocument(interestNote);
            }
            interestNote.closeDocument();

            //if "availableAmount" still exists, then we must check if there is any InterestRate to pay
            if (availableAmount.compareTo(BigDecimal.ZERO) > 0) {
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

                    SettlementEntry newSettlementEntry = SettlementEntry.create(interestEntry, settlementNote, amountToPay,
                            interestEntry.getDescription(), whenRegistered, true);
                    //Update the amount to Pay
                    availableAmount = availableAmount.subtract(amountToPay);
                }
            }
            interestRateEntries.clear();
        }


        //######################################
        //4. If there is money for more, create a "pending" payment (CreditNote) for being used later
        //######################################

        //if "availableAmount" still exists, then we must create a "pending Payment" or "CreditNote"
        if (availableAmount.compareTo(BigDecimal.ZERO) > 0) {
            settlementNote.createAdvancedPaymentCreditNote(availableAmount,
                    Constants.bundle("label.PaymentCodeTarget.advancedpayment") + comments + "-"
                            + sibsTransactionId,
                    sibsTransactionId);
        }

        //######################################
        //5. Close the SettlementNote
        //######################################

        PaymentEntry paymentEntry = PaymentEntry.create(this.getPaymentReferenceCode().getPaymentCodePool().getPaymentMethod(),
                settlementNote, amount, null);
        settlementNote.closeDocument();

        //######################################
        //6. Create a SibsTransactionDetail
        //######################################
        this.getPaymentReferenceCode().setState(PaymentReferenceCodeStateType.PROCESSED);

        return settlementNote;
    }

    protected abstract DocumentNumberSeries getDocumentSeriesInterestDebits();

    protected abstract DocumentNumberSeries getDocumentSeriesForPayments();

    public abstract LocalDate getDueDate();

}
