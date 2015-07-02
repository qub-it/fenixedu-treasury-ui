package org.fenixedu.treasury.services.integration.erp;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.fenixedu.treasury.domain.FinantialInstitution;
import org.fenixedu.treasury.domain.document.FinantialDocument;
import org.fenixedu.treasury.domain.integration.ERPExportOperation;

import pt.ist.fenixframework.Atomic;

public class ERPExporterManager {

    @Atomic
    public static List<ERPExportOperation> exportPendingDocumentsForFinantialInstitution(FinantialInstitution finantialInstitution) {

        Set<FinantialDocument> pendingDocuments = finantialInstitution.getFinantialDocumentsPendingForExportationSet();

        Comparator<FinantialDocument> sortingComparator = new Comparator<FinantialDocument>() {

            @Override
            public int compare(FinantialDocument o1, FinantialDocument o2) {
                if (o1.getFinantialDocumentType().equals(o2.getFinantialDocumentType())) {
                    return o1.getUiDocumentNumber().compareTo(o2.getUiDocumentNumber());
                } else {
                    if (o1.isDebitNote()) {
                        return -2;
                    } else if (o1.isCreditNote()) {
                        return -1;
                    } else if (o1.isSettlementNote()) {
                        return 1;
                    }
                }
                return 0;
            }
        };
        List<FinantialDocument> sortedDocuments =
                pendingDocuments.stream().sorted(sortingComparator).collect(Collectors.toList());

        List<ERPExportOperation> result = new ArrayList<ERPExportOperation>();
        if (pendingDocuments.isEmpty() == false) {

            if (finantialInstitution.getErpIntegrationConfiguration().getExportOnlyRelatedDocumentsPerExport()) {
                while (sortedDocuments.isEmpty() == false) {
                    FinantialDocument doc = sortedDocuments.iterator().next();
                    Set<FinantialDocument> findRelatedDocuments =
                            doc.findRelatedDocuments(new HashSet<FinantialDocument>(), true).stream()
                                    .filter(x -> x.isDocumentToExport() == true).collect(Collectors.toSet());
                    for (FinantialDocument peingDoc : findRelatedDocuments) {
                        if (doc.isDocumentToExport()) {
                            //remove the related documents from the original Set
                            sortedDocuments.remove(doc);
                        }
                    }

                    //Create a ExportOperation
                    ERPExportOperation exportFinantialDocumentToIntegration =
                            ERPExporter.exportFinantialDocumentToIntegration(finantialInstitution, sortedDocuments);
                    result.add(exportFinantialDocumentToIntegration);
                }

            } else {

                ERPExportOperation exportFinantialDocumentToIntegration =
                        ERPExporter.exportFinantialDocumentToIntegration(finantialInstitution, sortedDocuments);

                result.add(exportFinantialDocumentToIntegration);
            }
        }

        return result;

    }
}
