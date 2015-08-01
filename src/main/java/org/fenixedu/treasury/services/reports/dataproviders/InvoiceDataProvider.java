package org.fenixedu.treasury.services.reports.dataproviders;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

import org.fenixedu.treasury.domain.document.Invoice;

import com.qubit.terra.docs.util.IDocumentFieldsData;
import com.qubit.terra.docs.util.IFieldsExporter;
import com.qubit.terra.docs.util.IReportDataProvider;

public class InvoiceDataProvider extends AbstractDataProvider implements IReportDataProvider {

    protected static final String DOCUMENT_TYPE_KEY = "invoiceDocumentType";
    protected static final String INVOICE_KEY = "invoice";
    protected static final String LINES_KEY = "invoiceLines";
    protected final List<String> allKeys = new ArrayList<String>();
    protected Map<String, Function<IReportDataProvider, Object>> keysDictionary =
            new HashMap<String, Function<IReportDataProvider, Object>>();

    private Invoice invoice;

    public InvoiceDataProvider(final Invoice invoice) {
        this.invoice = invoice;
        registerKey(DOCUMENT_TYPE_KEY, InvoiceDataProvider::handleDocumentTypeKey);
        registerKey(INVOICE_KEY, InvoiceDataProvider::handleInvoice);
        registerKey(LINES_KEY, InvoiceDataProvider::handleInvoiceLines);
    }

    private static Object handleDocumentTypeKey(IReportDataProvider provider) {
        InvoiceDataProvider invoiceProvider = (InvoiceDataProvider) provider;
        return invoiceProvider.invoice.getFinantialDocumentType().getType().toString();
    }

    private static Object handleInvoiceLines(IReportDataProvider provider) {
        InvoiceDataProvider invoiceProvider = (InvoiceDataProvider) provider;
        return invoiceProvider.invoice.getFinantialDocumentEntriesSet();
    }

    private static Object handleInvoice(IReportDataProvider provider) {
        InvoiceDataProvider invoiceProvider = (InvoiceDataProvider) provider;
        Object x = invoiceProvider.invoice;
        return x;
    }

    @Override
    public void registerFieldsAndImages(IDocumentFieldsData arg0) {
        // TODO Auto-generated method stub
        arg0.registerCollectionAsField(LINES_KEY);
    }

    @Override
    public void registerFieldsMetadata(IFieldsExporter arg0) {
        arg0.registerSimpleField("invoice", "Esta é a factura.");
        arg0.registerSimpleField("invoice.debtAccount", "Conta Corrente");
        arg0.registerCollectionField("invoice.invoiceLines", "Conjunto de linhas da factura");

    }

}
