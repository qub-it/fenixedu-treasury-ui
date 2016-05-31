package org.fenixedu.treasury.services.payments.sibs.incomming;

import java.util.stream.Collectors;

import org.fenixedu.bennu.scheduler.CronTask;
import org.fenixedu.bennu.scheduler.annotation.Task;
import org.fenixedu.treasury.domain.FinantialInstitution;
import org.fenixedu.treasury.domain.exceptions.TreasuryDomainException;
import org.fenixedu.treasury.domain.paymentcodes.SibsReportFile;
import org.fenixedu.treasury.domain.paymentcodes.pool.PaymentCodePool;
import org.fenixedu.treasury.services.payments.sibs.SIBSPaymentsImporter;
import org.fenixedu.treasury.services.payments.sibs.SIBSPaymentsImporter.ProcessResult;
import org.fenixedu.treasury.services.payments.sibs.SibsPaymentsBrokerService;
import org.joda.time.LocalDate;

@Task(englishTitle = "Process SIBS payments from broker", readOnly = false)
public class ProcessSibsPaymentsFromBroker extends CronTask {

    @Override
    public void runTask() throws Exception {
        
        for(final FinantialInstitution finantialInstitution : FinantialInstitution.findAll().collect(Collectors.toSet())) {
            
            if(!finantialInstitution.getSibsConfiguration().isPaymentsBrokerActive()) {
                continue;
            }
            
            for(final PaymentCodePool paymentCodePool : finantialInstitution.getPaymentCodePoolsSet()) {
                try {
                    if(paymentCodePool.getActive() == null || !paymentCodePool.getActive()) {
                        continue;
                    }
                    
                    LocalDate now = new LocalDate();
                    
                    final SibsIncommingPaymentFile sibsFile =
                            SibsPaymentsBrokerService.readPaymentsFromBroker(paymentCodePool.getFinantialInstitution(), now, now,
                                    true, true);
                    
                    if(sibsFile.getDetailLines().isEmpty()) {
                        continue;
                    }
                    
                    SIBSPaymentsImporter importer = new SIBSPaymentsImporter();
                    SibsReportFile reportFile = null;
                    
                    final ProcessResult result = importer.processSIBSPaymentFiles(sibsFile, paymentCodePool.getFinantialInstitution());
                    reportFile = result.getReportFile();
                    if (result.getReportFile() != null) {
                        reportFile.updateLogMessages(result);
                    }
                } catch(final TreasuryDomainException e) {
                    if(SibsPaymentsBrokerService.ERROR_SIBS_PAYMENTS_BROKER_SERVICE_NO_PAYMENTS_TO_IMPORT.equals(e.getMessage())) {
                        taskLog("No payments to register");
                        continue;
                    }
                    
                    throw new RuntimeException(e);
                }
            }
        }
    }
}
