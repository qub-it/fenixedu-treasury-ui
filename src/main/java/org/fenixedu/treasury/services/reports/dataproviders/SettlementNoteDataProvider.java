package org.fenixedu.treasury.services.reports.dataproviders;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

import org.fenixedu.treasury.domain.document.SettlementNote;

import com.qubit.terra.docs.util.IDocumentFieldsData;
import com.qubit.terra.docs.util.IFieldsExporter;
import com.qubit.terra.docs.util.IReportDataProvider;

public class SettlementNoteDataProvider extends AbstractDataProvider implements IReportDataProvider {

    protected static final String DOCUMENT_TYPE_KEY = "settlementNoteDocumentType";
    protected static final String DOCUMENT_KEY = "settlementNote";
    protected static final String LINES_KEY = "settlementNoteLines";
    protected static final String PAYMENT_LINES_KEY = "paymentLines";
    protected final List<String> allKeys = new ArrayList<String>();
    protected Map<String, Function<IReportDataProvider, Object>> keysDictionary =
            new HashMap<String, Function<IReportDataProvider, Object>>();

    private SettlementNote note;

    public SettlementNoteDataProvider(final SettlementNote note) {
        this.note = note;
        registerKey(DOCUMENT_TYPE_KEY, SettlementNoteDataProvider::handleDocumentTypeKey);
        registerKey(DOCUMENT_KEY, SettlementNoteDataProvider::handleDocument);
        registerKey(LINES_KEY, SettlementNoteDataProvider::handleLines);
        registerKey(PAYMENT_LINES_KEY, SettlementNoteDataProvider::handlePaymentLines);
    }

    private static Object handleDocumentTypeKey(IReportDataProvider provider) {
        SettlementNoteDataProvider invoiceProvider = (SettlementNoteDataProvider) provider;
        return invoiceProvider.note.getFinantialDocumentType().getType().toString();
    }

    private static Object handleLines(IReportDataProvider provider) {
        SettlementNoteDataProvider invoiceProvider = (SettlementNoteDataProvider) provider;
        return invoiceProvider.note.getSettlemetEntriesSet();
    }

    private static Object handlePaymentLines(IReportDataProvider provider) {
        SettlementNoteDataProvider invoiceProvider = (SettlementNoteDataProvider) provider;
        return invoiceProvider.note.getPaymentEntriesSet();
    }

    private static Object handleDocument(IReportDataProvider provider) {
        SettlementNoteDataProvider invoiceProvider = (SettlementNoteDataProvider) provider;
        return invoiceProvider.note;
    }

    @Override
    public void registerFieldsAndImages(IDocumentFieldsData arg0) {
        // TODO Auto-generated method stub
        arg0.registerCollectionAsField(LINES_KEY);
    }

    @Override
    public void registerFieldsMetadata(IFieldsExporter arg0) {
        // TODO Auto-generated method stub

    }

}
