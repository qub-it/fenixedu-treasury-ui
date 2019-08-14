package org.fenixedu.treasury.domain.paymentcodes;

import java.math.BigDecimal;
import java.util.Map;
import java.util.Set;

import org.fenixedu.treasury.domain.Customer;
import org.fenixedu.treasury.domain.IPaymentProcessorForInvoiceEntries;
import org.fenixedu.treasury.domain.PaymentMethod;
import org.fenixedu.treasury.domain.Product;
import org.fenixedu.treasury.domain.document.DocumentNumberSeries;
import org.fenixedu.treasury.domain.document.Invoice;
import org.fenixedu.treasury.domain.document.InvoiceEntry;
import org.fenixedu.treasury.domain.document.SettlementNote;
import org.fenixedu.treasury.domain.event.TreasuryEvent;
import org.fenixedu.treasury.domain.settings.TreasurySettings;
import org.joda.time.DateTime;
import org.joda.time.LocalDate;

import com.google.common.base.Strings;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;

import pt.ist.fenixframework.Atomic;

public abstract class PaymentCodeTarget extends PaymentCodeTarget_Base implements IPaymentProcessorForInvoiceEntries {

    public PaymentCodeTarget() {
        super();
    }

    public abstract Set<SettlementNote> processPayment(final String username, final BigDecimal amountToPay,
            DateTime whenRegistered, String sibsTransactionId, String comments);

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

    @Override
    public Set<SettlementNote> internalProcessPaymentInNormalPaymentMixingLegacyInvoices(final String username,
            final BigDecimal amount, final DateTime paymentDate, final String sibsTransactionId, final String comments,
            Set<InvoiceEntry> invoiceEntriesToPay) {

        Set<SettlementNote> result =
                IPaymentProcessorForInvoiceEntries.super.internalProcessPaymentInNormalPaymentMixingLegacyInvoices(username,
                        amount, paymentDate, sibsTransactionId, comments, invoiceEntriesToPay);

        //######################################
        //6. Create a SibsTransactionDetail
        //######################################
        this.getPaymentReferenceCode().setState(PaymentReferenceCodeStateType.PROCESSED);

        return result;
    }

    @Override
    public Set<SettlementNote> internalProcessPaymentInRestrictedPaymentMixingLegacyInvoices(final String username,
            final BigDecimal amount, final DateTime paymentDate, final String sibsTransactionId, final String comments,
            final Set<InvoiceEntry> invoiceEntriesToPay) {

        Set<SettlementNote> result =
                IPaymentProcessorForInvoiceEntries.super.internalProcessPaymentInRestrictedPaymentMixingLegacyInvoices(username,
                        amount, paymentDate, sibsTransactionId, comments, invoiceEntriesToPay);

        //######################################
        //5. Create a SibsTransactionDetail
        //######################################
        this.getPaymentReferenceCode().setState(PaymentReferenceCodeStateType.PROCESSED);

        return result;

    }

    @Atomic
    protected Set<SettlementNote> internalProcessPayment(final String username, final BigDecimal amount,
            final DateTime whenRegistered, final String sibsTransactionId, final String comments,
            Set<InvoiceEntry> invoiceEntriesToPay) {

        if (!TreasurySettings.getInstance().isRestrictPaymentMixingLegacyInvoices()) {
            return internalProcessPaymentInNormalPaymentMixingLegacyInvoices(username, amount, whenRegistered, sibsTransactionId,
                    comments, invoiceEntriesToPay);
        } else {
            return internalProcessPaymentInRestrictedPaymentMixingLegacyInvoices(username, amount, whenRegistered,
                    sibsTransactionId, comments, invoiceEntriesToPay);
        }
    }

    @Override
    public String fillPaymentEntryMethodId() {
        // ANIL (2017-09-13) Required by used ERP at this date
        return String.format("COB PAG SERV %s", getPaymentReferenceCode().getPaymentCodePool().getEntityReferenceCode());
    }

    @Override
    public Map<String, String> fillPaymentEntryPropertiesMap(final String sibsTransactionId) {
        final Map<String, String> paymentEntryPropertiesMap = Maps.newHashMap();
        paymentEntryPropertiesMap.put("ReferenceCode", getPaymentReferenceCode().getReferenceCode());
        paymentEntryPropertiesMap.put("EntityReferenceCode",
                getPaymentReferenceCode().getPaymentCodePool().getEntityReferenceCode());

        if (!Strings.isNullOrEmpty(sibsTransactionId)) {
            paymentEntryPropertiesMap.put("SibsTransactionId", sibsTransactionId);
        }
        return paymentEntryPropertiesMap;
    }

    public abstract DocumentNumberSeries getDocumentSeriesInterestDebits();

    public abstract DocumentNumberSeries getDocumentSeriesForPayments();

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

    @Override
    public PaymentMethod getPaymentMethod() {
        return getPaymentReferenceCode().getPaymentCodePool().getPaymentMethod();
    }

}
