package org.fenixedu.treasury.services.integration.erp.tasks;

import java.util.Collections;

import org.fenixedu.bennu.scheduler.CronTask;
import org.fenixedu.bennu.scheduler.annotation.Task;
import org.fenixedu.treasury.domain.document.FinantialDocument;
import org.fenixedu.treasury.domain.integration.ERPExportOperation;
import org.fenixedu.treasury.services.integration.erp.ERPExporter;

import pt.ist.fenixframework.FenixFramework;

@Task(englishTitle = "Export Single Doument to ERP Integration", readOnly = true)
public class ERPExportSingleDocumentsTask extends CronTask {

    String externalId = "";

    public ERPExportSingleDocumentsTask() {
        super();
    }

    public ERPExportSingleDocumentsTask(String documentExternalId) {
        externalId = documentExternalId;
    }

    @Override
    public void runTask() throws Exception {

        if (externalId.equals("")) {
            taskLog("External ID empty, not exporting any document");
        } else {
            try {
                FinantialDocument document = FenixFramework.getDomainObject(externalId);
                ERPExportOperation exportOperation =
                        ERPExporter.exportFinantialDocumentToIntegration(document.getDocumentNumberSeries().getSeries()
                                .getFinantialInstitution(), Collections.singletonList(document));
                taskLog("Exported document: " + document.getUiDocumentNumber() + "=>"
                        + (exportOperation.getSuccess() ? "OK" : "NOK"));
            } catch (Exception ex) {
                taskLog("Error exporting document: " + ex.getMessage());
                for (StackTraceElement el : ex.getStackTrace()) {
                    taskLog(el.toString());
                }
            }
        }
    }
}
