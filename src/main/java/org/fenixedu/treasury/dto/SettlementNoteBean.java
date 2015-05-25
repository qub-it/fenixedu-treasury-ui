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
import org.fenixedu.treasury.domain.Currency;
import org.fenixedu.treasury.domain.PaymentMethod;
import org.fenixedu.treasury.domain.VatType;
import org.fenixedu.treasury.domain.debt.DebtAccount;
import org.fenixedu.treasury.domain.document.CreditEntry;
import org.fenixedu.treasury.domain.document.DebitEntry;
import org.fenixedu.treasury.domain.document.InvoiceEntry;
import org.joda.time.DateTime;
import org.joda.time.LocalDate;

public class SettlementNoteBean implements IBean, Serializable {

    private static final long serialVersionUID = 1L;

    private DebtAccount debtAccount;

    private LocalDate date;

    private List<CreditEntryBean> creditEntries;

    private List<DebitEntryBean> debitEntries;

    private List<InterestEntryBean> interestEntries;

    private List<PaymentEntryBean> paymentEntries;

    private List<TupleDataSourceBean> paymentMethods;

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

    public DateTime getDebitNoteDate() {
        DateTime lowerDate = new DateTime();
        for (DebitEntryBean debitEntryBean : getDebitEntries()) {
            if (debitEntryBean.isIncluded() && debitEntryBean.getDocumentDate().isBefore(lowerDate)) {
                lowerDate = debitEntryBean.getDocumentDate();
            }
        }
        for (InterestEntryBean interestEntryBean : getInterestEntries()) {
            if (interestEntryBean.isIncluded() && interestEntryBean.getDocumentDate().isBefore(lowerDate)) {
                lowerDate = interestEntryBean.getDocumentDate();
            }
        }
        return lowerDate;
    }

    public BigDecimal getPaymentAmount() {
        BigDecimal sum = BigDecimal.ZERO;
        for (DebitEntryBean debitEntryBean : getDebitEntries()) {
            if (debitEntryBean.isIncluded()) {
                sum = sum.add(debitEntryBean.getPaymentAmount());
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

    public BigDecimal getPaymentAmountWithVat() {
        BigDecimal sum = BigDecimal.ZERO;
        for (DebitEntryBean debitEntryBean : getDebitEntries()) {
            if (debitEntryBean.isIncluded()) {
                sum = sum.add(debitEntryBean.getPaymentAmountWithVat());
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
                sum = sum.subtract(creditEntryBean.getCreditEntry().getOpenAmountWithVat());
            }
        }
        return sum;
    }

    public BigDecimal getVatAmount() {
        return getPaymentAmountWithVat().subtract(getPaymentAmount());
    }

    public Map<String, VatAmountBean> getValuesByVat() {
        Map<String, VatAmountBean> sumByVat = new HashMap<String, VatAmountBean>();
        for (VatType vatType : VatType.findAll().collect(Collectors.toList())) {
            sumByVat.put(vatType.getName().getContent(), new VatAmountBean(BigDecimal.ZERO, BigDecimal.ZERO));
        }
        for (DebitEntryBean debitEntryBean : getDebitEntries()) {
            if (debitEntryBean.isIncluded()) {
                String vatType = debitEntryBean.getDebitEntry().getVat().getVatType().getName().getContent();
                sumByVat.get(vatType).addAmount(debitEntryBean.getPaymentAmount());
                sumByVat.get(vatType).addAmountWithVat(debitEntryBean.getPaymentAmountWithVat());
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
                sumByVat.get(vatType).subtractAmountWithVat(creditEntryBean.getCreditEntry().getOpenAmountWithVat());
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

    ///////////////////
    // Inner classes //
    ///////////////////

    public class DebitEntryBean implements IBean, Serializable {

        private static final long serialVersionUID = 1L;

        private DebitEntry debitEntry;

        private boolean isIncluded;

        private boolean isNotValid;

        private BigDecimal paymentAmount;

        public DebitEntryBean() {

        }

        public DebitEntryBean(DebitEntry debitEntry) {
            this.debitEntry = debitEntry;
            this.isIncluded = false;
            this.isNotValid = false;
            this.paymentAmount = BigDecimal.ZERO;
        }

        public DebitEntry getDebitEntry() {
            return debitEntry;
        }

        public void setDebitEntry(DebitEntry debitEntry) {
            this.debitEntry = debitEntry;
        }

        public String getDocumentNumber() {
            return (debitEntry.getFinantialDocument() != null) ? debitEntry.getFinantialDocument().getDocumentNumber() : null;
        }

        public DateTime getDocumentDate() {
            return (debitEntry.getFinantialDocument() != null) ? debitEntry.getFinantialDocument().getDocumentDate() : debitEntry
                    .getEntryDateTime();
        }

        public boolean isIncluded() {
            return isIncluded;
        }

        public void setIncluded(boolean isIncluded) {
            this.isIncluded = isIncluded;
        }

        public BigDecimal getPaymentAmount() {
            BigDecimal amount =
                    paymentAmount.multiply(BigDecimal.ONE.subtract(debitEntry.getVat().getTaxRate()
                            .divide(BigDecimal.valueOf(100))));
            return Currency.getValueWithScale(amount);
        }

        public BigDecimal getPaymentAmountWithVat() {
            return Currency.getValueWithScale(paymentAmount);
        }

        public void setPaymentAmount(BigDecimal paymentAmount) {
            this.paymentAmount = paymentAmount;
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
            return (creditEntry.getFinantialDocument() != null) ? creditEntry.getFinantialDocument().getDocumentNumber() : null;
        }

        public DateTime getDocumentDate() {
            return (creditEntry.getFinantialDocument() != null) ? creditEntry.getFinantialDocument().getDocumentDate() : creditEntry
                    .getEntryDateTime();
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

        public DateTime getDocumentDate() {
            return (debitEntry.getFinantialDocument() != null) ? debitEntry.getFinantialDocument().getDocumentDate() : debitEntry
                    .getEntryDateTime();
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

        private BigDecimal payedAmount;

        private PaymentMethod paymentMethod;

        public PaymentEntryBean() {
            setPayedAmount(BigDecimal.ZERO);
        }

        public PaymentEntryBean(BigDecimal payedAmount, PaymentMethod paymentMethod) {
            this.setPayedAmount(payedAmount);
            this.setPaymentMethod(paymentMethod);
        }

        public BigDecimal getPayedAmount() {
            return payedAmount;
        }

        public void setPayedAmount(BigDecimal payedAmount) {
            this.payedAmount = payedAmount;
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
