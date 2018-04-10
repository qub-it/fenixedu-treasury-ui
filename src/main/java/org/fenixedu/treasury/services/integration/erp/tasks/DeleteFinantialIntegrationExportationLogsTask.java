package org.fenixedu.treasury.services.integration.erp.tasks;

import java.io.File;
import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.TreeSet;
import java.util.concurrent.Callable;
import java.util.concurrent.Semaphore;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import pt.ist.fenixframework.DomainRoot;
import org.fenixedu.bennu.io.domain.FileSupport;
import org.fenixedu.bennu.io.domain.LocalFileToDelete;
import org.fenixedu.bennu.scheduler.CronTask;
import org.fenixedu.bennu.scheduler.annotation.Task;
import org.fenixedu.bennu.scheduler.custom.CustomTask;
import org.fenixedu.treasury.domain.document.FinantialDocument;
import org.fenixedu.treasury.domain.integration.ERPExportOperation;
import org.joda.time.DateTime;

import pt.ist.fenixframework.Atomic;
import pt.ist.fenixframework.Atomic.TxMode;
import pt.ist.fenixframework.DomainObject;
import pt.ist.fenixframework.FenixFramework;

@Task(englishTitle = "Delete finantial exportation logs", readOnly = true)
public class DeleteFinantialIntegrationExportationLogsTask extends CronTask {

    private int batchSize = 10;
    private int availableCPUs = Runtime.getRuntime().availableProcessors();
    private int numberOfAvailableThreads = Math.max(1, availableCPUs - 2);
    private static final int numberOfLocalFilesToDeletePerTx = 2500;

    Semaphore semaphore = new Semaphore(numberOfAvailableThreads);

    static boolean shouldRespawn = false;

    @Override
    public void runTask() throws Exception {

        taskLog("NUM FIN DOCS PENDING: %d\n", FenixFramework.getDomainRoot().getFinantialInstitutionsSet().iterator().next()
                .getFinantialDocumentsPendingForExportationSet().size());

        ArrayList<FinantialDocument> domainObjects =
                new ArrayList<FinantialDocument>(FenixFramework.getDomainRoot().getFinantialDocumentsSet());

        List<List<String>> workingBatches = breakIntoBatches(domainObjects, batchSize);

        taskLog("Created " + workingBatches.size() + " batches of " + batchSize + " documents each distributing to "
                + numberOfAvailableThreads + " threads");

        AtomicInteger batchCount = new AtomicInteger(0);
        for (List<String> batch : workingBatches) {
            checkKillSwitch();
            semaphore.acquire();
            // We do each numberOfAvailableThreads + 1 so we ensure that in each thread cycle 
            // only one enters.
            if (batchCount.incrementAndGet() % (numberOfAvailableThreads + 1) == 0) {
                // Tricky bit, should respawn is static and will be changed by fileDeleterThread.
                // First of all we don't need to address synchronization in this case, because only
                // the main thread will spawn FileDeleterThreads and they work in sync, so no need to
                // worry about that.
                //
                // Second part this is done, so we can try to achieve better performance, deleting only
                // 5000 elements at a time, and should spawning several threads, to fully delete elements.
                // Since those threads are always waited due to join(). It won't be a real problem about
                // them getting into conflict as well
                //
                // Notice that each time we enter here we set should respawn to false just to make sure
                // we don't enter a loop due to previous value change by FileDeleterThread 
                shouldRespawn = false;
                boolean initial = true;
                while (initial || shouldRespawn) {
                    initial = false;
                    FileDeleterThread fileDeleterThread = new FileDeleterThread(this);
                    fileDeleterThread.start();
                    fileDeleterThread.join();
                }
            }

            final DeleteFinantialDocumentThread t = new DeleteFinantialDocumentThread(this, batch, batchCount.get());
            taskLog("Starting batch: " + batchCount.get() + "");
            t.start();

        }
    }

    private void checkKillSwitch() {
        if (new File("/tmp/stopTask").exists()) {
            taskLog("W: Exiting...");
            throw new RuntimeException("ERROR");
        }
    }

    public static List<List<String>> breakIntoBatches(List<? extends DomainObject> domainObjects, int batchSize) {
        List<List<String>> workingBatches = new ArrayList<>();
        int start = 0;
        int end = start + batchSize;
        int totalSize = domainObjects.size();

        while (end < totalSize) {
            List<String> subList = domainObjects.subList(start, Math.min(end, totalSize)).stream()
                    .map(DomainObject::getExternalId).collect(Collectors.toList());
            workingBatches.add(subList);
            start = end;
            end = start + batchSize;
        }
        return workingBatches;
    }

    private static class FileDeleterThread extends Thread {

        DeleteFinantialIntegrationExportationLogsTask task;

        public FileDeleterThread(DeleteFinantialIntegrationExportationLogsTask task) {
            this.task = task;
        }

        @Override
        public void run() {
            try {
                FenixFramework.getTransactionManager().withTransaction(new Callable<Object>() {

                    @Override
                    public Object call() throws Exception {
                        List<LocalFileToDelete> arrayList = new ArrayList<>(org.fenixedu.bennu.io.domain.FileSupportUtils.retrieveDeleteSet(FileSupport.getInstance()));
                        int totalSize = arrayList.size();
                        shouldRespawn = totalSize > numberOfLocalFilesToDeletePerTx;
                        if (shouldRespawn) {
                            arrayList = arrayList.subList(0, numberOfLocalFilesToDeletePerTx);
                        }
                        String threadName = Thread.currentThread().getName();
                        String date = new DateTime().toString();
                        task.taskLog("[" + threadName + "-Intermission-" + date + "] Deleting: " + arrayList.size()
                                + " files out of " + totalSize + ". Will respawn: " + shouldRespawn + "\n");
                        for (final LocalFileToDelete localFileToDelete : arrayList) {
                            try {
                                localFileToDelete.delete();
                            } catch (Exception e) {

                            }
                        }
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
                        return TxMode.WRITE;
                    }
                });
            } catch (final Exception e) {
                e.printStackTrace();
            }
        }
    }

    private static class DeleteFinantialDocumentThread extends Thread {

        DeleteFinantialIntegrationExportationLogsTask task;
        final List<String> finantialDocumentIds;
        private int batchID;

        public DeleteFinantialDocumentThread(final DeleteFinantialIntegrationExportationLogsTask task, final List<String> batch,
                int batchID) {
            this.task = task;
            this.finantialDocumentIds = batch;
            this.batchID = batchID;
        }

        @Override
        // @Atomic(mode = TxMode.READ)
        public void run() {
            try {
                FenixFramework.getTransactionManager().withTransaction(new Callable() {

                    @Override
                    public Object call() throws Exception {
                        String threadName = Thread.currentThread().getName();
                        int count = 0;
                        int total = finantialDocumentIds.size();
                        for (String finantialDocumentId : finantialDocumentIds) {
                            if (++count % 50 == 0) {
                                task.taskLog("[%s-batchID %s] : %s out of %s", threadName, batchID, count, total);
                            }
                            deleteLogsFromFinantialDocumentExportedDocument(task, finantialDocumentId);
                        }
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
                        return TxMode.WRITE;
                    }
                });
            } catch (final Exception e) {
                e.printStackTrace();
                throw new RuntimeException(e);
            } finally {
                task.taskLog(Thread.currentThread().getName() + " releasing lock");
                task.semaphore.release();
            }
        }

    }

    protected static boolean deleteLogsFromFinantialDocumentExportedDocument(
            final DeleteFinantialIntegrationExportationLogsTask task, final String finantialDocumentId) {
        final FinantialDocument doc = FenixFramework.getDomainObject(finantialDocumentId);

        if (doc.isPreparing()) {
            return false;
        }

        List<ERPExportOperation> logs = new ArrayList<ERPExportOperation>(doc.getErpExportOperationsSet().stream()
                .sorted(Comparator.comparing(ERPExportOperation::getVersioningCreationDate).reversed())
                .collect(Collectors.toList()));

        if (logs.size() > 1) {
            TreeSet<ERPExportOperation> exportationByDateSet = new TreeSet<ERPExportOperation>(localDateComparator());

            int removedLogs = 0;
            for (ERPExportOperation log : logs) {
                try {

                    if (log.getSuccess()) {
                        continue;
                    }

                    if (!exportationByDateSet.add(log)) {
                        removedLogs++;
                        log.getLogFile().delete();
                        log.delete();
                    }

                } catch (Throwable e) {
                    task.taskLog("E\t%s\t%s\t%s\t%s\n", log.getExternalId(), e.getClass().getSimpleName(), e.getMessage());
                }
            }

            task.taskLog("LOGS: %s\t%s\t%s\n", doc.getUiDocumentNumber(), logs.size(), removedLogs);
        }
        return true;
    }

    private static Comparator<? super ERPExportOperation> localDateComparator() {
        return new Comparator<ERPExportOperation>() {

            @Override
            public int compare(final ERPExportOperation o1, final ERPExportOperation o2) {
                return o1.getVersioningCreationDate().toLocalDate().compareTo(o2.getVersioningCreationDate().toLocalDate());
            }

        };

    }

    @Override
    public TxMode getTxMode() {
        return TxMode.READ;
    }
}