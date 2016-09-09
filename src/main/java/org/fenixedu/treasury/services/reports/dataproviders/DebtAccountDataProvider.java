package org.fenixedu.treasury.services.reports.dataproviders;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.fenixedu.treasury.domain.debt.DebtAccount;
import org.fenixedu.treasury.domain.document.DebitEntry;
import org.fenixedu.treasury.domain.document.DebitNote;
import org.fenixedu.treasury.domain.document.InvoiceEntry;
import org.fenixedu.treasury.domain.paymentcodes.MultipleEntriesPaymentCode;
import org.fenixedu.treasury.domain.paymentcodes.PaymentReferenceCode;

import com.qubit.terra.docs.util.IDocumentFieldsData;
import com.qubit.terra.docs.util.IReportDataProvider;

public class DebtAccountDataProvider extends AbstractDataProvider implements IReportDataProvider {

    protected static final String DEBT_ACCOUNT_KEY = "debtAccount";
    protected static final String PAYMENT_LINES_KEY = "paymentLines";

    private DebtAccount debtAccount;
    private List<DebitNote> debitNotesForPaymentLines;

    public DebtAccountDataProvider(final DebtAccount debtAccount) {
        this(debtAccount, null);
    }

    public DebtAccountDataProvider(final DebtAccount debtAccount, final List<DebitNote> debitNotesForPaymentLines) {
        this.debtAccount = debtAccount;
        registerKey(DEBT_ACCOUNT_KEY, DebtAccountDataProvider::handleDebtAccountKey);
        registerKey(PAYMENT_LINES_KEY, DebtAccountDataProvider::handlePaymentsLinesKey);
    }

    private static Object handlePaymentsLinesKey(IReportDataProvider provider) {

        DebtAccountDataProvider debtProvider = (DebtAccountDataProvider) provider;

        Set<PaymentReferenceCode> referencesCodes = new HashSet<PaymentReferenceCode>();

        if (debtProvider.debitNotesForPaymentLines == null) {
            List<? extends InvoiceEntry> pendingDebitEntriesSet = debtProvider.debtAccount.getPendingInvoiceEntriesSet().stream()
                    .filter(x -> x.isDebitNoteEntry()).collect(Collectors.<InvoiceEntry> toList());

            for (InvoiceEntry entry : pendingDebitEntriesSet) {
                final DebitEntry debitEntry = (DebitEntry) entry;

                referencesCodes
                        .addAll(MultipleEntriesPaymentCode.find(debitEntry).filter(m -> m.getPaymentReferenceCode().isUsed())
                                .map(m -> m.getPaymentReferenceCode()).collect(Collectors.toSet()));
            }
        }

        List<PaymentReferenceCodeDataProvider> codesProviders = new ArrayList<PaymentReferenceCodeDataProvider>();
        for (PaymentReferenceCode code : referencesCodes) {
            codesProviders.add(new PaymentReferenceCodeDataProvider(code));
        }
        
        return codesProviders.stream().sorted((x, y) -> x.getDueDate().compareTo(y.getDueDate())).collect(Collectors.toList());
    }

    private static Object handleDebtAccountKey(IReportDataProvider provider) {
        DebtAccountDataProvider invoiceProvider = (DebtAccountDataProvider) provider;
        return invoiceProvider.debtAccount;
    }

    @Override
    public void registerFieldsAndImages(IDocumentFieldsData arg0) {
        // TODO Auto-generated method stub
        arg0.registerCollectionAsField(PAYMENT_LINES_KEY);

    }

}
