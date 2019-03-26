package org.fenixedu.treasury.services.payments.sibs.incomming;

import static java.lang.String.format;
import static org.fenixedu.treasury.util.TreasuryConstants.DATE_FORMAT_YYYY_MM_DD;

import java.io.IOException;
import java.util.stream.Collectors;

import org.fenixedu.bennu.scheduler.CronTask;
import org.fenixedu.bennu.scheduler.annotation.Task;
import org.fenixedu.treasury.domain.FinantialInstitution;
import org.fenixedu.treasury.domain.exceptions.TreasuryDomainException;
import org.fenixedu.treasury.domain.paymentcodes.SibsReportFile;
import org.fenixedu.treasury.domain.paymentcodes.pool.PaymentCodePool;
import org.fenixedu.treasury.services.payments.sibs.SIBSPaymentsImporter;
import org.fenixedu.treasury.services.payments.sibs.SIBSPaymentsImporter.ProcessResult;
import org.fenixedu.treasury.util.TreasuryConstants;
import org.fenixedu.treasury.services.payments.sibs.SibsPaymentsBrokerService;
import org.joda.time.LocalDate;

@Task(englishTitle = "Process SIBS payments from broker", readOnly = false)
public class ProcessSibsPaymentsFromBroker extends CronTask {

    @Override
    public void runTask() throws Exception {
        final LocalDate now = new LocalDate();
        
        for(int i = 3; i > 0; i--) {
            importFromBroker(now.minusDays(i * 3), now.minusDays((i * 3) - 2));
        }
        
        importFromBroker(now, now);
    }

    private void importFromBroker(final LocalDate fromDate, final LocalDate toDate) throws IOException {
        taskLog(format("[%s - %s] Processing...\n", fromDate.toString(DATE_FORMAT_YYYY_MM_DD), toDate.toString(DATE_FORMAT_YYYY_MM_DD)));
        
        for(final FinantialInstitution finantialInstitution : FinantialInstitution.findAll().collect(Collectors.toSet())) {
            
            if(!finantialInstitution.getSibsConfiguration().isPaymentsBrokerActive()) {
                continue;
            }
            
            for(final PaymentCodePool paymentCodePool : finantialInstitution.getPaymentCodePoolsSet()) {
                try {
                    if(paymentCodePool.getActive() == null || !paymentCodePool.getActive()) {
                        continue;
                    }
                    
                    final SibsIncommingPaymentFile sibsFile =
                            SibsPaymentsBrokerService.readPaymentsFromBroker(paymentCodePool.getFinantialInstitution(), fromDate, toDate,
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
                        taskLog(format("[%s - %s] No payments to register\n", fromDate.toString(DATE_FORMAT_YYYY_MM_DD), toDate.toString(DATE_FORMAT_YYYY_MM_DD)));
                        continue;
                    }
                    
                    throw new RuntimeException(e);
                }
            }
        }
        
        taskLog(format("[%s - %s] Finished.\n", fromDate.toString(DATE_FORMAT_YYYY_MM_DD), toDate.toString(DATE_FORMAT_YYYY_MM_DD)));
    }
}
