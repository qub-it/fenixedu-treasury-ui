package org.fenixedu.treasury.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.fenixedu.bennu.IBean;
import org.fenixedu.bennu.TupleDataSourceBean;
import org.fenixedu.treasury.domain.PaymentMethod;
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

    /*TODOJN - date*/
    public String getDate() {
        return date.toString("yyyy-MM-dd");
    }

    /*TODOJN - date*/
    public void setDate(String date) {
        this.date = LocalDate.parse(date);
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
                //TODOJN -- interest in value
                //sum = sum.add(interestEntryBean);
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
                //TODOJN -- interest in value
                //sum = sum.add(interestEntryBean);
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

//    public Map<String, <BigDecimal, BigDecimal>> getValuesByVat() {
//        Map<String, Pair<BigDecimal, BigDecimal>> sumByVat = new HashMap<String, Pair<BigDecimal, BigDecimal>>();
//        for (VatType vatType : VatType.findAll().collect(Collectors.toList())) {
//            sumByVat.put(vatType.getName().getContent(), BigDecimal.ZERO);
//        }
//        for (DebitEntryBean debitEntryBean : getDebitEntries()) {
//            if (debitEntryBean.isIncluded()) {
//                String vatType = debitEntryBean.getDebitEntry().getVat().getVatType().getName().getContent();
//                sumByVat.put(vatType, sumByVat.get(vatType).add(debitEntryBean.getPaymentAmountWithVat()));
//            }
//        }
//        for (InterestEntryBean interestEntryBean : getInterestEntries()) {
//            if (interestEntryBean.isIncluded()) {
//                //TODOJN -- interest in value
//                //sum = sum.add(interestEntryBean);
//            }
//        }
//        for (CreditEntryBean creditEntryBean : getCreditEntries()) {
//            if (creditEntryBean.isIncluded()) {
//                String vatType = creditEntryBean.getCreditEntry().getVat().getVatType().getName().getContent();
//                sumByVat.put(vatType, sumByVat.get(vatType).subtract(creditEntryBean.getCreditEntry().getOpenAmountWithVat()));
//            }
//        }
//        return sumByVat;
//    }

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
            return paymentAmount;
        }

        public BigDecimal getPaymentAmountWithVat() {
            return paymentAmount.multiply(BigDecimal.ONE.add(debitEntry.getVat().getTaxRate().divide(BigDecimal.valueOf(100))))
                    .setScale(2, RoundingMode.HALF_EVEN);
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

        /*TODOJN passar de string para a classe*/
        private String interest;

        public InterestEntryBean() {
            this.isIncluded = false;
        }

        public InterestEntryBean(DebitEntry debitEntry, String interest) {
            this();
            this.debitEntry = debitEntry;
            this.interest = interest;
        }

        public String getInterest() {
            return interest;
        }

        public void setInterest(String interest) {
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
}
