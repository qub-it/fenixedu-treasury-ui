package org.fenixedu.treasury.domain.paymentcodes;

import static org.fenixedu.treasury.util.TreasuryConstants.treasuryBundle;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.TreeSet;

import org.fenixedu.bennu.core.domain.User;
import org.fenixedu.treasury.domain.Customer;
import org.fenixedu.treasury.domain.Product;
import org.fenixedu.treasury.domain.debt.DebtAccount;
import org.fenixedu.treasury.domain.document.AdvancedPaymentCreditNote;
import org.fenixedu.treasury.domain.document.DebitEntry;
import org.fenixedu.treasury.domain.document.DebitNote;
import org.fenixedu.treasury.domain.document.DocumentNumberSeries;
import org.fenixedu.treasury.domain.document.FinantialDocumentType;
import org.fenixedu.treasury.domain.document.Invoice;
import org.fenixedu.treasury.domain.document.InvoiceEntry;
import org.fenixedu.treasury.domain.document.PaymentEntry;
import org.fenixedu.treasury.domain.document.SettlementEntry;
import org.fenixedu.treasury.domain.document.SettlementNote;
import org.fenixedu.treasury.domain.event.TreasuryEvent;
import org.fenixedu.treasury.domain.settings.TreasurySettings;
import org.fenixedu.treasury.dto.InterestRateBean;
import org.fenixedu.treasury.services.integration.erp.sap.SAPExporter;
import org.fenixedu.treasury.util.TreasuryConstants;
import org.joda.time.DateTime;
import org.joda.time.LocalDate;

import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;

import pt.ist.fenixframework.Atomic;

public abstract class PaymentCodeTarget extends PaymentCodeTarget_Base {

    public PaymentCodeTarget() {
        super();
    }

    public abstract Set<SettlementNote> processPayment(final String username, final BigDecimal amountToPay, DateTime whenRegistered,
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

    
    protected Set<SettlementNote> internalProcessPaymentInNormalPaymentMixingLegacyInvoices(final String username, final BigDecimal amount, final DateTime whenRegistered,
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
        DebtAccount referenceDebtAccount = this.getDebtAccount();
        DocumentNumberSeries docNumberSeries = this.getDocumentSeriesForPayments();
        SettlementNote settlementNote =
                SettlementNote.create(referenceDebtAccount, docNumberSeries, new DateTime(), whenRegistered, comments, null);

        //######################################
        //1. Find the InvoiceEntries
        //2. Create the SEttlementEntries and the SEttlementNote
        //######################################
        
        if(getReferencedCustomers().size() == 1) {
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
                            if (TreasuryConstants.isPositive(calculateUndebitedInterestValue.getInterestAmount())) {
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
        }

        //######################################
        //4. If there is money for more, create a "pending" payment (CreditNote) for being used later
        //######################################

        //if "availableAmount" still exists, then we must create a "pending Payment" or "CreditNote"
        if (availableAmount.compareTo(BigDecimal.ZERO) > 0) {
            settlementNote.createAdvancedPaymentCreditNote(availableAmount,
                    treasuryBundle("label.PaymentCodeTarget.advancedpayment") + comments + "-"
                            + sibsTransactionId,
                    sibsTransactionId);
        }

        //######################################
        //5. Close the SettlementNote
        //######################################

        final Map<String, String> paymentEntryPropertiesMap = fillPaymentEntryPropertiesMap(sibsTransactionId);
        
        PaymentEntry paymentEntry = PaymentEntry.create(getPaymentReferenceCode().getPaymentCodePool().getPaymentMethod(),
                settlementNote, amount, fillPaymentEntryMethodId(), paymentEntryPropertiesMap);
        settlementNote.closeDocument();

        //######################################
        //6. Create a SibsTransactionDetail
        //######################################
        this.getPaymentReferenceCode().setState(PaymentReferenceCodeStateType.PROCESSED);

        return Sets.newHashSet(settlementNote);
    }

    private Set<SettlementNote> internalProcessPaymentInRestrictedPaymentMixingLegacyInvoices(final String username, final BigDecimal amount,
            final DateTime whenRegistered, final String sibsTransactionId, final String comments, final Set<InvoiceEntry> invoiceEntriesToPay) {
        if(!TreasurySettings.getInstance().isRestrictPaymentMixingLegacyInvoices()) {
            throw new RuntimeException("invalid call");
        }
        
        // Check if invoiceEntriesToPay have mixed invoice entries of certified in legacy erp and not
        SettlementNote.checkMixingOfInvoiceEntriesExportedInLegacyERP(invoiceEntriesToPay);
        
        // If all invoice entries are not exported in legacy ERP then invoke the internalProcessPaymentInNormalPaymentMixingLegacyInvoices
        if(invoiceEntriesToPay.stream().allMatch(e -> e.getFinantialDocument() == null || !e.getFinantialDocument().isExportedInLegacyERP())) {
            return internalProcessPaymentInNormalPaymentMixingLegacyInvoices(username, amount, whenRegistered, sibsTransactionId, comments, invoiceEntriesToPay);
        }
        
        final TreeSet<InvoiceEntry> sortedInvoiceEntriesToPay = Sets.newTreeSet(InvoiceEntry.COMPARE_BY_AMOUNT_AND_DUE_DATE);
        sortedInvoiceEntriesToPay.addAll(invoiceEntriesToPay);

        //Process the payment of pending invoiceEntries
        //1. Find the InvoiceEntries
        //2. Create the SEttlementEntries and the SEttlementNote
        //2.1 create the "InterestRate entries"
        //3. Close the SettlementNote
        //4. If there is money for more, create a "pending" payment (CreditNote) in different settlement note for being used later
        //6. Create a SibsTransactionDetail
        BigDecimal availableAmount = amount;

        final DebtAccount referenceDebtAccount = this.getDebtAccount();
        final DocumentNumberSeries docNumberSeries = this.getDocumentSeriesForPayments();
        final SettlementNote settlementNote =
                SettlementNote.create(referenceDebtAccount, docNumberSeries, new DateTime(), whenRegistered, comments, null);

        //######################################
        //1. Find the InvoiceEntries
        //2. Create the SEttlementEntries and the SEttlementNote
        //######################################
        
        if(getReferencedCustomers().size() == 1) {
            for (final InvoiceEntry entry : sortedInvoiceEntriesToPay) {
                if (availableAmount.compareTo(BigDecimal.ZERO) <= 0) {
                    break;
                }
    
                BigDecimal amountToPay = entry.getOpenAmount();
                if (amountToPay.compareTo(BigDecimal.ZERO) > 0) {
                    if (entry.isDebitNoteEntry()) {
                        DebitEntry debitEntry = (DebitEntry) entry;
    
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
                            if (TreasuryConstants.isPositive(calculateUndebitedInterestValue.getInterestAmount())) {
                                debitEntry.createInterestRateDebitEntry(
                                        calculateUndebitedInterestValue, whenRegistered, Optional.<DebitNote> empty());
                            }
                        }
    
                        SettlementEntry.create(entry, settlementNote, amountToPay, entry.getDescription(), whenRegistered, true);
    
                        //Update the amount to Pay
                        availableAmount = availableAmount.subtract(amountToPay);
    
                    } else if (entry.isCreditNoteEntry()) {
                        SettlementEntry.create(entry, settlementNote, entry.getOpenAmount(), entry.getDescription(), whenRegistered, true);
                        //update the amount to Pay
                        availableAmount = availableAmount.add(amountToPay);
                    }
                } else {
                    //Ignore since the "open amount" is ZERO
                }
            }
        }

        if (availableAmount.compareTo(BigDecimal.ZERO) <= 0) {
            availableAmount = BigDecimal.ZERO;
        }
        
        //######################################
        //3. Close the SettlementNote
        //######################################

        final Map<String, String> paymentEntryPropertiesMap = fillPaymentEntryPropertiesMap(sibsTransactionId);
        
        PaymentEntry.create(getPaymentReferenceCode().getPaymentCodePool().getPaymentMethod(),
                settlementNote, amount.subtract(availableAmount), fillPaymentEntryMethodId(), paymentEntryPropertiesMap);
        settlementNote.closeDocument();
        
        final Set<SettlementNote> result = Sets.newHashSet(settlementNote);
        
        //###########################################################################################
        //4. If there is money for more, create a "pending" payment (CreditNote) for being used later
        // which must be settled in different SettlementNote so the advancepayment can be integrated and
        // use with new certified invoices
        //###########################################################################################

        //if "availableAmount" still exists, then we must create a "pending Payment" or "CreditNote"
        if (availableAmount.compareTo(BigDecimal.ZERO) > 0) {
            final SettlementNote advancedPaymentSettlementNote =
                    SettlementNote.create(referenceDebtAccount, docNumberSeries, new DateTime(), whenRegistered, comments, null);

            advancedPaymentSettlementNote.createAdvancedPaymentCreditNote(availableAmount,
                    treasuryBundle("label.PaymentCodeTarget.advancedpayment") + comments + "-" + sibsTransactionId, 
                    sibsTransactionId);

            PaymentEntry.create(getPaymentReferenceCode().getPaymentCodePool().getPaymentMethod(),
                    advancedPaymentSettlementNote, availableAmount, fillPaymentEntryMethodId(), paymentEntryPropertiesMap);
            advancedPaymentSettlementNote.closeDocument();
            
            // Mark both documents as alread exported in legacy ERP
            advancedPaymentSettlementNote.setExportedInLegacyERP(true);
            advancedPaymentSettlementNote.setCloseDate(SAPExporter.ERP_INTEGRATION_START_DATE.minusSeconds(1));
            advancedPaymentSettlementNote.getAdvancedPaymentCreditNote().setExportedInLegacyERP(true);
            advancedPaymentSettlementNote.getAdvancedPaymentCreditNote().setCloseDate(SAPExporter.ERP_INTEGRATION_START_DATE.minusSeconds(1));
            
            result.add(advancedPaymentSettlementNote);
        }

        //######################################
        //5. Create a SibsTransactionDetail
        //######################################
        this.getPaymentReferenceCode().setState(PaymentReferenceCodeStateType.PROCESSED);

        return result;

    }
    
    @Atomic
    protected Set<SettlementNote> internalProcessPayment(final String username, final BigDecimal amount, final DateTime whenRegistered,
            final String sibsTransactionId, final String comments, Set<InvoiceEntry> invoiceEntriesToPay) {

        if(!TreasurySettings.getInstance().isRestrictPaymentMixingLegacyInvoices()) {
            return internalProcessPaymentInNormalPaymentMixingLegacyInvoices(username, amount, whenRegistered, sibsTransactionId, comments, invoiceEntriesToPay);
        } else {
            return internalProcessPaymentInRestrictedPaymentMixingLegacyInvoices(username, amount, whenRegistered, sibsTransactionId, comments, invoiceEntriesToPay);
        }
    }

    private String fillPaymentEntryMethodId() {
        // ANIL (2017-09-13) Required by used ERP at this date
        return String.format("COB PAG SERV %s",getPaymentReferenceCode().getPaymentCodePool().getEntityReferenceCode());
    }

    private Map<String, String> fillPaymentEntryPropertiesMap(final String sibsTransactionId) {
        final Map<String, String> paymentEntryPropertiesMap = Maps.newHashMap();
        paymentEntryPropertiesMap.put("ReferenceCode", getPaymentReferenceCode().getReferenceCode());
        paymentEntryPropertiesMap.put("EntityReferenceCode", getPaymentReferenceCode().getPaymentCodePool().getEntityReferenceCode());
        
        if(!Strings.isNullOrEmpty(sibsTransactionId)) {
            paymentEntryPropertiesMap.put("SibsTransactionId", sibsTransactionId);
        }
        return paymentEntryPropertiesMap;
    }

    protected abstract DocumentNumberSeries getDocumentSeriesInterestDebits();

    protected abstract DocumentNumberSeries getDocumentSeriesForPayments();
    
    protected abstract Set<InvoiceEntry> getInvoiceEntries();

    public abstract LocalDate getDueDate();
    
    public abstract Set<Product> getReferencedProducts();

    public Set<Customer> getReferencedCustomers() {
        final Set<Customer> result = Sets.newHashSet();
        for (final InvoiceEntry entry : getInvoiceEntries()) {
            if (entry.getFinantialDocument() != null && ((Invoice) entry.getFinantialDocument()).isForPayorDebtAccount()) {
                result.add(((Invoice) entry.getFinantialDocument()).getPayorDebtAccount().getCustomer());
                continue;
            }

            result.add(entry.getDebtAccount().getCustomer());
        }

        return result;
    }

}
