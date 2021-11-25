package org.fenixedu.treasury.services.integration.erp.tasks;

import org.fenixedu.bennu.scheduler.CronTask;
import org.fenixedu.bennu.scheduler.annotation.Task;
import org.fenixedu.treasury.domain.FinantialInstitution;
import org.fenixedu.treasury.services.integration.erp.ERPExporterManager;

@Deprecated
@Task(englishTitle = "Update pending reimbursement notes", readOnly = true)
public class ERPUpdatePendingReimbursementNotesTask extends CronTask {

    @Override
    public void runTask() throws Exception {

        FinantialInstitution.findAll().forEach(x -> {

            taskLog("Start Exporting Pending Documents for : " + x.getName());
            if (!x.getErpIntegrationConfiguration().getActive()) {
                return;
            }

            ERPExporterManager.updatePendingReimbursementNotes(x);

            taskLog("Finished update pending reimbursements for : %s", x.getName());

        });
    }
}
