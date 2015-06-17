package org.fenixedu.treasury.domain.integration;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Stream;

import org.fenixedu.treasury.domain.FinantialInstitution;
import org.fenixedu.treasury.domain.document.FinantialDocument;
import org.fenixedu.treasury.domain.exceptions.TreasuryDomainException;

import pt.ist.fenixframework.Atomic;

public class ERPExportOperation extends ERPExportOperation_Base {

    protected ERPExportOperation() {
        super();
    }

    protected void init(final OperationFile file, final FinantialInstitution finantialInstitution,
            final org.joda.time.DateTime executionDate, final boolean processed, final boolean success, final boolean corrected,
            final java.lang.String errorLog) {
        setFile(file);
        setFinantialInstitution(finantialInstitution);
        setExecutionDate(executionDate);
        setProcessed(processed);
        setSuccess(success);
        setCorrected(corrected);
        setErrorLog(errorLog);
        checkRules();
    }

    private void checkRules() {
        //
        //CHANGE_ME add more busines validations
        //
        if (getFile() == null) {
            throw new TreasuryDomainException("error.ERPExportOperation.file.required");
        }

        if (getFinantialInstitution() == null) {
            throw new TreasuryDomainException("error.ERPExportOperation.finantialInstitution.required");
        }

        //CHANGE_ME In order to validate UNIQUE restrictions
        //if (findByFile(getFile().count()>1)
        //{
        //  throw new TreasuryDomainException("error.ERPExportOperation.file.duplicated");
        //} 
        //if (findByFinantialInstitution(getFinantialInstitution().count()>1)
        //{
        //  throw new TreasuryDomainException("error.ERPExportOperation.finantialInstitution.duplicated");
        //} 
        //if (findByExecutionDate(getExecutionDate().count()>1)
        //{
        //  throw new TreasuryDomainException("error.ERPExportOperation.executionDate.duplicated");
        //} 
        //if (findByProcessed(getProcessed().count()>1)
        //{
        //  throw new TreasuryDomainException("error.ERPExportOperation.processed.duplicated");
        //} 
        //if (findBySuccess(getSuccess().count()>1)
        //{
        //  throw new TreasuryDomainException("error.ERPExportOperation.success.duplicated");
        //} 
        //if (findByCorrected(getCorrected().count()>1)
        //{
        //  throw new TreasuryDomainException("error.ERPExportOperation.corrected.duplicated");
        //} 
        //if (findByErrorLog(getErrorLog().count()>1)
        //{
        //  throw new TreasuryDomainException("error.ERPExportOperation.errorLog.duplicated");
        //} 
    }

    @Atomic
    public void edit(final OperationFile file, final FinantialInstitution finantialInstitution,
            final org.joda.time.DateTime executionDate, final boolean processed, final boolean success, final boolean corrected,
            final java.lang.String errorLog) {
        setFile(file);
        setFinantialInstitution(finantialInstitution);
        setExecutionDate(executionDate);
        setProcessed(processed);
        setSuccess(success);
        setCorrected(corrected);
        setErrorLog(errorLog);
        checkRules();
    }

    @Override
    protected void checkForDeletionBlockers(Collection<String> blockers) {
        super.checkForDeletionBlockers(blockers);

        //add more logical tests for checking deletion rules
        //if (getXPTORelation() != null)
        //{
        //    blockers.add(BundleUtil.getString(Bundle.APPLICATION, "error.ERPExportOperation.cannot.be.deleted"));
        //}
    }

    @Override
    @Atomic
    public void delete() {
        TreasuryDomainException.throwWhenDeleteBlocked(getDeletionBlockers());

        if (!isDeletable()) {
            throw new TreasuryDomainException("error.ERPExportOperation.cannot.delete");
        }
        this.setFinantialInstitution(null);

        for (FinantialDocument document : this.getFinantialDocumentsSet()) {
            this.removeFinantialDocuments(document);
        }

        super.delete();
    }

    @Atomic
    public static ERPExportOperation create(final byte[] data, final String filename,
            final FinantialInstitution finantialInstitution, final org.joda.time.DateTime executionDate, final boolean processed,
            final boolean success, final boolean corrected, final java.lang.String errorLog) {
        ERPExportOperation eRPExportOperation = new ERPExportOperation();
        OperationFile file;
        file = OperationFile.create(filename, data);

        eRPExportOperation.init(file, finantialInstitution, executionDate, processed, success, corrected, errorLog);
        return eRPExportOperation;
    }

    // @formatter: off
    /************
     * SERVICES *
     ************/
    // @formatter: on

    public static Stream<ERPExportOperation> findAll() {
        Set<ERPExportOperation> results = new HashSet<ERPExportOperation>();
//        Bennu.getInstance().getFinantialInstitutionsSet().forEach(x->results.addAll(x.gete));
        return results.stream();
    }

    public static Stream<ERPExportOperation> findByFile(final OperationFile file) {
        return findAll().filter(i -> file.equals(i.getFile()));
    }

    public static Stream<ERPExportOperation> findByFinantialInstitution(final FinantialInstitution finantialInstitution) {
        return findAll().filter(i -> finantialInstitution.equals(i.getFinantialInstitution()));
    }

    public static Stream<ERPExportOperation> findByExecutionDate(final org.joda.time.DateTime executionDate) {
        return findAll().filter(i -> executionDate.equals(i.getExecutionDate()));
    }

    public static Stream<ERPExportOperation> findByProcessed(final boolean processed) {
        return findAll().filter(i -> processed == i.getProcessed());
    }

    public static Stream<ERPExportOperation> findBySuccess(final boolean success) {
        return findAll().filter(i -> success == i.getSuccess());
    }

    public static Stream<ERPExportOperation> findByCorrected(final boolean corrected) {
        return findAll().filter(i -> corrected == i.getCorrected());
    }

    public static Stream<ERPExportOperation> findByErrorLog(final java.lang.String errorLog) {
        return findAll().filter(i -> errorLog.equalsIgnoreCase(i.getErrorLog()));
    }

}
