package org.fenixedu.treasury.services.integration.erp.tasks;

import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.stream.Collectors;

import org.apache.commons.lang.StringUtils;
import org.fenixedu.bennu.scheduler.CronTask;
import org.fenixedu.bennu.scheduler.annotation.Task;
import org.fenixedu.treasury.domain.Customer;
import org.fenixedu.treasury.domain.FinantialInstitution;
import org.fenixedu.treasury.domain.document.ERPCustomerFieldsBean;
import org.fenixedu.treasury.domain.document.FinantialDocument;
import org.fenixedu.treasury.domain.integration.ERPExportOperation;
import org.fenixedu.treasury.services.integration.erp.ERPExporterManager;
import org.fenixedu.treasury.services.integration.erp.IERPExporter;

import com.google.common.collect.Lists;

import pt.ist.fenixframework.Atomic;
import pt.ist.fenixframework.FenixFramework;

@Deprecated
@Task(englishTitle = "Export All Pending Documents to ERP Integration", readOnly = true)
public class ERPExportAllPendingDocumentsTask extends CronTask {

    private static final int LIMIT = 5000;

    @Override
    public void runTask() throws Exception {
        taskLog("Start");
        try {
            final ExportThread e = new ExportThread(this, false);

            e.start();
            e.join();
        } catch (InterruptedException e) {
        }

        try {
            final ExportThread e = new ExportThread(this, true);

            e.start();
            e.join();
        } catch (InterruptedException e) {
        }

        taskLog("End");
    }

    private static class ExportThread extends Thread {

        ERPExportAllPendingDocumentsTask task;
        boolean exportSettlementNotes = false;

        public ExportThread(ERPExportAllPendingDocumentsTask task, final boolean exportSettlementNotes) {
            this.task = task;
            this.exportSettlementNotes = exportSettlementNotes;
        }

        @Override
        public void run() {
            try {
                FenixFramework.getTransactionManager().withTransaction(new Callable() {

                    @Override
                    public Object call() throws Exception {
                        exportPendingDocumentsForFinantialInstitution(task, exportSettlementNotes);

                        return null;
                    }
                }, new Atomic() {

                    @Override
                    public Class<? extends Annotation> annotationType() {
                        return null;
                    }

                    @Override
                    public boolean flattenNested() {
                        return false;
                    }

                    @Override
                    public TxMode mode() {
                        return TxMode.READ;
                    }
                });
            } catch (final Exception e) {
                e.printStackTrace();
                throw new RuntimeException(e);
            }
        }
    }

    public static List<ERPExportOperation> exportPendingDocumentsForFinantialInstitution(
            final ERPExportAllPendingDocumentsTask task, final boolean exportSettlementNotes) {
        try {

            FinantialInstitution finantialInstitution = FinantialInstitution.findAll().findFirst().orElse(null);

            if (finantialInstitution == null) {
                return Lists.newArrayList();
            }

            List<ERPExportOperation> result = new ArrayList<ERPExportOperation>();

            if (!finantialInstitution.getErpIntegrationConfiguration().getActive()) {
                return result;
            }

            if (finantialInstitution.getErpIntegrationConfiguration() == null) {
                return result;
            }

            if (StringUtils.isEmpty(finantialInstitution.getErpIntegrationConfiguration().getImplementationClassName())) {
                return result;
            }

            IERPExporter erpExporter =
                    finantialInstitution.getErpIntegrationConfiguration().getERPExternalServiceImplementation().getERPExporter();

            if (erpExporter == null) {
                return result;
            }
            
            final List<FinantialDocument> sortedDocuments = erpExporter
                    .filterDocumentsToExport(finantialInstitution.getFinantialDocumentsPendingForExportationSet().stream())
                    .stream()
                    .filter(x -> x.isSettlementNote() == exportSettlementNotes)
                    .collect(Collectors.<FinantialDocument> toList());

            int count = 0;
            if (sortedDocuments.isEmpty() == false) {

                if (finantialInstitution.getErpIntegrationConfiguration().getExportOnlyRelatedDocumentsPerExport()) {
                    while (!sortedDocuments.isEmpty()) {
                        FinantialDocument doc = sortedDocuments.iterator().next();

                        //remove the related documents from the original Set
                        sortedDocuments.remove(doc);

                        count++;

                        if ((count % 100) == 0) {
                            task.taskLog("Read %d\n", count);
                        }

                        // Limit exportation of documents in which customer has invalid addresses
                        final Customer customer = doc.getDebtAccount().getCustomer();
                        final List<String> errorMessages = Lists.newArrayList();
                        if(!ERPCustomerFieldsBean.validateAddress(customer, errorMessages)) {
                            if(!doc.getErpExportOperationsSet().isEmpty()) {
                                continue;
                            }
                        }
                        
                        //Create a ExportOperation
                        ERPExportOperation exportFinantialDocumentToIntegration = ERPExporterManager.exportSingleDocument(doc);

                        if (exportFinantialDocumentToIntegration != null) {
                            result.add(exportFinantialDocumentToIntegration);
                        }
                    }
                }
            }

            return result;
        } catch (final Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }

    }
}
