package org.fenixedu.treasury.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.fenixedu.bennu.IBean;
import org.fenixedu.bennu.TupleDataSourceBean;
import org.fenixedu.treasury.domain.PaymentMethod;
import org.fenixedu.treasury.domain.VatType;
import org.fenixedu.treasury.domain.debt.DebtAccount;
import org.fenixedu.treasury.domain.document.CreditEntry;
import org.fenixedu.treasury.domain.document.DebitEntry;
import org.fenixedu.treasury.domain.document.DocumentNumberSeries;
import org.fenixedu.treasury.domain.document.FinantialDocumentType;
import org.fenixedu.treasury.domain.document.InvoiceEntry;
import org.joda.time.LocalDate;

public class SettlementNoteBean implements IBean, Serializable {

    private static final long serialVersionUID = 1L;

    private DebtAccount debtAccount;

    private LocalDate date;

    private DocumentNumberSeries docNumSeries;

    private String originDocumentNumber;

    private List<CreditEntryBean> creditEntries;

    private List<DebitEntryBean> debitEntries;

    private List<InterestEntryBean> interestEntries;

    private List<PaymentEntryBean> paymentEntries;

    private List<TupleDataSourceBean> paymentMethods;

    private List<TupleDataSourceBean> documentNumberSeries;

    public SettlementNoteBean() {
        creditEntries = new ArrayList<CreditEntryBean>();
        debitEntries = new ArrayList<DebitEntryBean>();
        interestEntries = new ArrayList<InterestEntryBean>();
        paymentEntries = new ArrayList<PaymentEntryBean>();
        date = new LocalDate();
        this.setPaymentMethods(PaymentMethod.findAll().collect(Collectors.toList()));
    }

    public SettlementNoteBean(DebtAccount debtAccount) {
        this();
        this.debtAccount = debtAccount;
        for (InvoiceEntry invoiceEntry : debtAccount.getPendingInvoiceEntriesSet()) {
            if (invoiceEntry instanceof DebitEntry) {
                debitEntries.add(new DebitEntryBean((DebitEntry) invoiceEntry));
            } else {
                creditEntries.add(new CreditEntryBean((CreditEntry) invoiceEntry));
            }
        }
        this.setDocumentNumberSeries(DocumentNumberSeries.find(FinantialDocumentType.findForSettlementNote(),
                debtAccount.getFinantialInstitution()).collect(Collectors.toList()));
    }

    public DebtAccount getDebtAccount() {
        return debtAccount;
    }

    public void setDebtAccount(DebtAccount debtAccount) {
        this.debtAccount = debtAccount;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public List<CreditEntryBean> getCreditEntries() {
        return creditEntries;
    }

    public void setCreditEntries(List<CreditEntryBean> creditEntries) {
        this.creditEntries = creditEntries;
    }

    public List<DebitEntryBean> getDebitEntries() {
        return debitEntries;
    }

    public void setDebitEntries(List<DebitEntryBean> debitEntries) {
        this.debitEntries = debitEntries;
    }

    public List<InterestEntryBean> getInterestEntries() {
        return interestEntries;
    }

    public void setInterestEntries(List<InterestEntryBean> interestEntries) {
        this.interestEntries = interestEntries;
    }

    public LocalDate getDebitNoteDate() {
        LocalDate lowerDate = new LocalDate();
        for (DebitEntryBean debitEntryBean : getDebitEntries()) {
            if (debitEntryBean.isIncluded() && debitEntryBean.getDocumentDueDate().isBefore(lowerDate)) {
                lowerDate = debitEntryBean.getDocumentDueDate();
            }
        }
        for (InterestEntryBean interestEntryBean : getInterestEntries()) {
            if (interestEntryBean.isIncluded() && interestEntryBean.getDocumentDueDate().isBefore(lowerDate)) {
                lowerDate = interestEntryBean.getDocumentDueDate();
            }
        }
        return lowerDate;
    }

    public BigDecimal getDebtAmount() {
        BigDecimal sum = BigDecimal.ZERO;
        for (DebitEntryBean debitEntryBean : getDebitEntries()) {
            if (debitEntryBean.isIncluded()) {
                sum = sum.add(debitEntryBean.getDebtAmount());
            }
        }
        for (InterestEntryBean interestEntryBean : getInterestEntries()) {
            if (interestEntryBean.isIncluded()) {
                sum = sum.add(interestEntryBean.getInterest().getInterestAmount());
            }
        }
        for (CreditEntryBean creditEntryBean : getCreditEntries()) {
            if (creditEntryBean.isIncluded()) {
                sum = sum.subtract(creditEntryBean.getCreditEntry().getOpenAmount());
            }
        }
        return sum;
    }

    public BigDecimal getDebtAmountWithVat() {
        BigDecimal sum = BigDecimal.ZERO;
        for (DebitEntryBean debitEntryBean : getDebitEntries()) {
            if (debitEntryBean.isIncluded()) {
                sum = sum.add(debitEntryBean.getDebtAmountWithVat());
            }
        }
        for (InterestEntryBean interestEntryBean : getInterestEntries()) {
            if (interestEntryBean.isIncluded()) {
                //Interest doesn't have vat
                sum = sum.add(interestEntryBean.getInterest().getInterestAmount());
            }
        }
        for (CreditEntryBean creditEntryBean : getCreditEntries()) {
            if (creditEntryBean.isIncluded()) {
                sum = sum.subtract(creditEntryBean.getCreditEntry().getOpenAmount());
            }
        }
        return sum;
    }

    public BigDecimal getVatAmount() {
        return getDebtAmountWithVat().subtract(getDebtAmount());
    }

    public Map<String, VatAmountBean> getValuesByVat() {
        Map<String, VatAmountBean> sumByVat = new HashMap<String, VatAmountBean>();
        for (VatType vatType : VatType.findAll().collect(Collectors.toList())) {
            sumByVat.put(vatType.getName().getContent(), new VatAmountBean(BigDecimal.ZERO, BigDecimal.ZERO));
        }
        for (DebitEntryBean debitEntryBean : getDebitEntries()) {
            if (debitEntryBean.isIncluded()) {
                String vatType = debitEntryBean.getDebitEntry().getVat().getVatType().getName().getContent();
                sumByVat.get(vatType).addAmount(debitEntryBean.getDebtAmount());
                sumByVat.get(vatType).addAmountWithVat(debitEntryBean.getDebtAmountWithVat());
            }
        }
        for (InterestEntryBean interestEntryBean : getInterestEntries()) {
            if (interestEntryBean.isIncluded()) {
                String vatType = interestEntryBean.getDebitEntry().getVat().getVatType().getName().getContent();
                sumByVat.get(vatType).addAmount(interestEntryBean.getInterest().getInterestAmount());
                sumByVat.get(vatType).addAmountWithVat(interestEntryBean.getInterest().getInterestAmount());
            }
        }
        for (CreditEntryBean creditEntryBean : getCreditEntries()) {
            if (creditEntryBean.isIncluded()) {
                String vatType = creditEntryBean.getCreditEntry().getVat().getVatType().getName().getContent();
                sumByVat.get(vatType).subtractAmount(creditEntryBean.getCreditEntry().getOpenAmount());
                sumByVat.get(vatType).subtractAmountWithVat(creditEntryBean.getCreditEntry().getOpenAmount());
            }
        }
        return sumByVat;
    }

    public List<PaymentEntryBean> getPaymentEntries() {
        return paymentEntries;
    }

    public void setPaymentEntries(List<PaymentEntryBean> paymentEntries) {
        this.paymentEntries = paymentEntries;
    }

    public List<TupleDataSourceBean> getPaymentMethods() {
        return paymentMethods;
    }

    public void setPaymentMethods(List<PaymentMethod> paymentMethods) {
        this.paymentMethods = paymentMethods.stream().map(paymentMethod -> {
            TupleDataSourceBean tuple = new TupleDataSourceBean();
            tuple.setText(paymentMethod.getName().getContent());
            tuple.setId(paymentMethod.getExternalId());
            return tuple;
        }).collect(Collectors.toList());
    }

    public BigDecimal getPaymentAmount() {
        BigDecimal paymentAmount = BigDecimal.ZERO;
        for (PaymentEntryBean paymentEntryBean : getPaymentEntries()) {
            paymentAmount = paymentAmount.add(paymentEntryBean.getPaymentAmount());
        }
        return paymentAmount;
    }

    public DocumentNumberSeries getDocNumSeries() {
        return docNumSeries;
    }

    public void setDocNumSeries(DocumentNumberSeries docNumSeries) {
        this.docNumSeries = docNumSeries;
    }

    public List<TupleDataSourceBean> getDocumentNumberSeries() {
        return documentNumberSeries;
    }

    public void setDocumentNumberSeries(List<DocumentNumberSeries> documentNumberSeries) {
        this.documentNumberSeries = documentNumberSeries.stream().map(docNumSeries -> {
            TupleDataSourceBean tuple = new TupleDataSourceBean();
            tuple.setText(docNumSeries.getSeries().getName().getContent());
            tuple.setId(docNumSeries.getExternalId());
            return tuple;
        }).collect(Collectors.toList());
    }

    public String getOriginDocumentNumber() {
        return originDocumentNumber;
    }

    public void setOriginDocumentNumber(String originDocumentNumber) {
        this.originDocumentNumber = originDocumentNumber;
    }

    ///////////////////
    // Inner classes //
    ///////////////////

    public class DebitEntryBean implements IBean, Serializable {

        private static final long serialVersionUID = 1L;

        private DebitEntry debitEntry;

        private boolean isIncluded;

        private boolean isNotValid;

        private BigDecimal debtAmount;

        public DebitEntryBean() {

        }

        public DebitEntryBean(DebitEntry debitEntry) {
            this.debitEntry = debitEntry;
            this.isIncluded = false;
            this.isNotValid = false;
            this.debtAmount = debitEntry.getOpenAmount();
        }

        public DebitEntry getDebitEntry() {
            return debitEntry;
        }

        public void setDebitEntry(DebitEntry debitEntry) {
            this.debitEntry = debitEntry;
        }

        public String getDocumentNumber() {
            return debitEntry.getFinantialDocument() != null ? debitEntry.getFinantialDocument().getDocumentNumber() : null;
        }

        public LocalDate getDocumentDueDate() {
            return debitEntry.getFinantialDocument() != null ? debitEntry.getFinantialDocument().getDocumentDueDate() : debitEntry
                    .getDueDate();
        }

        public boolean isIncluded() {
            return isIncluded;
        }

        public void setIncluded(boolean isIncluded) {
            this.isIncluded = isIncluded;
        }

        public BigDecimal getDebtAmount() {

            return debitEntry.getDebtAccount().getFinantialInstitution().getCurrency().getValueWithScale(debtAmount);
        }

        public BigDecimal getDebtAmountWithVat() {
            return debitEntry.getDebtAccount().getFinantialInstitution().getCurrency().getValueWithScale(debtAmount);
        }

        public void setDebtAmount(BigDecimal debtAmount) {
            this.debtAmount = debtAmount;
        }

        public boolean isNotValid() {
            return isNotValid;
        }

        public void setNotValid(boolean notValid) {
            this.isNotValid = notValid;
        }
    }

    public class CreditEntryBean implements IBean, Serializable {

        private static final long serialVersionUID = 1L;

        private CreditEntry creditEntry;

        private boolean isIncluded;

        public CreditEntryBean() {
        }

        public CreditEntryBean(CreditEntry creditEntry) {
            this.creditEntry = creditEntry;
        }

        public CreditEntry getCreditEntry() {
            return creditEntry;
        }

        public void setCreditEntry(CreditEntry creditEntry) {
            this.creditEntry = creditEntry;
        }

        public String getDocumentNumber() {
            return creditEntry.getFinantialDocument() != null ? creditEntry.getFinantialDocument().getDocumentNumber() : null;
        }

        public LocalDate getDocumentDueDate() {
            return creditEntry.getFinantialDocument() != null ? creditEntry.getFinantialDocument().getDocumentDueDate() : creditEntry
                    .getEntryDateTime().toLocalDate();
        }

        public boolean isIncluded() {
            return isIncluded;
        }

        public void setIncluded(boolean isIncluded) {
            this.isIncluded = isIncluded;
        }
    }

    public class InterestEntryBean implements IBean, Serializable {

        private static final long serialVersionUID = 1L;

        private DebitEntry debitEntry;

        private boolean isIncluded;

        private InterestRateBean interest;

        public InterestEntryBean() {
            this.isIncluded = false;
        }

        public InterestEntryBean(DebitEntry debitEntry, InterestRateBean interest) {
            this();
            this.debitEntry = debitEntry;
            this.interest = interest;
        }

        public InterestRateBean getInterest() {
            return interest;
        }

        public void setInterest(InterestRateBean interest) {
            this.interest = interest;
        }

        public DebitEntry getDebitEntry() {
            return debitEntry;
        }

        public void setDebitEntry(DebitEntry debitEntry) {
            this.debitEntry = debitEntry;
        }

        public LocalDate getDocumentDueDate() {
            return debitEntry.getFinantialDocument() != null ? debitEntry.getFinantialDocument().getDocumentDueDate() : debitEntry
                    .getDueDate();
        }

        public boolean isIncluded() {
            return isIncluded;
        }

        public void setIncluded(boolean isIncluded) {
            this.isIncluded = isIncluded;
        }

    }

    public class PaymentEntryBean implements IBean, Serializable {

        private static final long serialVersionUID = 1L;

        private BigDecimal paymentAmount;

        private PaymentMethod paymentMethod;

        public PaymentEntryBean() {
            this.paymentAmount = BigDecimal.ZERO;
        }

        public PaymentEntryBean(BigDecimal paymentAmount, PaymentMethod paymentMethod) {
            this.paymentAmount = paymentAmount;
            this.paymentMethod = paymentMethod;
        }

        public BigDecimal getPaymentAmount() {
            return paymentAmount;
        }

        public void setPaymentAmount(BigDecimal paymentAmount) {
            this.paymentAmount = paymentAmount;
        }

        public PaymentMethod getPaymentMethod() {
            return paymentMethod;
        }

        public void setPaymentMethod(PaymentMethod paymentMethod) {
            this.paymentMethod = paymentMethod;
        }
    }

    public class VatAmountBean implements IBean, Serializable {

        private static final long serialVersionUID = 1L;

        private BigDecimal amount;

        private BigDecimal amountWithVat;

        public VatAmountBean(BigDecimal amount, BigDecimal amountWithVat) {
            this.amount = amount;
            this.amountWithVat = amountWithVat;
        }

        public VatAmountBean() {
            this.amount = BigDecimal.ZERO;
            this.amountWithVat = BigDecimal.ZERO;
        }

        public BigDecimal getAmount() {
            return amount;
        }

        public void setAmount(BigDecimal amount) {
            this.amount = amount;
        }

        public void addAmount(BigDecimal partialAmount) {
            this.amount = amount.add(partialAmount);
        }

        public void subtractAmount(BigDecimal partialAmount) {
            this.amount = amount.subtract(partialAmount);
        }

        public BigDecimal getAmountWithVat() {
            return amountWithVat;
        }

        public void setAmountWithVat(BigDecimal amountWithVat) {
            this.amountWithVat = amountWithVat;
        }

        public void addAmountWithVat(BigDecimal partialAmountWithVat) {
            this.amountWithVat = amountWithVat.add(partialAmountWithVat);
        }

        public void subtractAmountWithVat(BigDecimal partialAmountWithVat) {
            this.amountWithVat = amountWithVat.subtract(partialAmountWithVat);
        }
    }

}
