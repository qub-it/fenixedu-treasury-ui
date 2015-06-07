package org.fenixedu.treasury.domain.integration;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.fenixedu.bennu.core.domain.Bennu;
import org.fenixedu.treasury.domain.FinantialInstitution;
import org.fenixedu.treasury.domain.document.FinantialDocument;
import org.fenixedu.treasury.domain.exceptions.TreasuryDomainException;

import pt.ist.fenixframework.Atomic;

public class ERPImportOperation extends ERPImportOperation_Base {

    protected ERPImportOperation() {
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
            throw new TreasuryDomainException("error.ERPImportOperation.file.required");
        }

        if (getFinantialInstitution() == null) {
            throw new TreasuryDomainException("error.ERPImportOperation.finantialInstitution.required");
        }

        //CHANGE_ME In order to validate UNIQUE restrictions
        //if (findByFile(getFile().count()>1)
        //{
        //  throw new TreasuryDomainException("error.ERPImportOperation.file.duplicated");
        //} 
        //if (findByFinantialInstitution(getFinantialInstitution().count()>1)
        //{
        //  throw new TreasuryDomainException("error.ERPImportOperation.finantialInstitution.duplicated");
        //} 
        //if (findByExecutionDate(getExecutionDate().count()>1)
        //{
        //  throw new TreasuryDomainException("error.ERPImportOperation.executionDate.duplicated");
        //} 
        //if (findByProcessed(getProcessed().count()>1)
        //{
        //  throw new TreasuryDomainException("error.ERPImportOperation.processed.duplicated");
        //} 
        //if (findBySuccess(getSuccess().count()>1)
        //{
        //  throw new TreasuryDomainException("error.ERPImportOperation.success.duplicated");
        //} 
        //if (findByCorrected(getCorrected().count()>1)
        //{
        //  throw new TreasuryDomainException("error.ERPImportOperation.corrected.duplicated");
        //} 
        //if (findByErrorLog(getErrorLog().count()>1)
        //{
        //  throw new TreasuryDomainException("error.ERPImportOperation.errorLog.duplicated");
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
        //    blockers.add(BundleUtil.getString(Bundle.APPLICATION, "error.ERPImportOperation.cannot.be.deleted"));
        //}
    }

    @Override
    @Atomic
    public void delete() {
        TreasuryDomainException.throwWhenDeleteBlocked(getDeletionBlockers());

        if (!isDeletable()) {
            throw new TreasuryDomainException("error.ERPImportOperation.cannot.delete");
        }

        this.setFinantialInstitution(null);
        for (FinantialDocument document : this.getFinantialDocumentsSet()) {
            this.removeFinantialDocuments(document);
        }
        deleteDomainObject();
    }

    @Atomic
    public static ERPImportOperation create(final OperationFile file, final FinantialInstitution finantialInstitution,
            final org.joda.time.DateTime executionDate, final boolean processed, final boolean success, final boolean corrected,
            final java.lang.String errorLog) {
        ERPImportOperation eRPImportOperation = new ERPImportOperation();
        eRPImportOperation.init(file, finantialInstitution, executionDate, processed, success, corrected, errorLog);
        return eRPImportOperation;
    }

    // @formatter: off
    /************
     * SERVICES *
     ************/
    // @formatter: on

    public static Stream<ERPImportOperation> findAll() {
        Set<ERPImportOperation> results = new HashSet<ERPImportOperation>();
        for (FinantialInstitution fi : Bennu.getInstance().getFinantialInstitutionsSet()) {
            results.addAll(fi.getIntegrationOperationsSet().stream().filter(x -> x instanceof ERPImportOperation)
                    .map(ERPImportOperation.class::cast).collect(Collectors.toList()));
        }
        return results.stream();
    }

    public static Stream<ERPImportOperation> findByFile(final OperationFile file) {
        return findAll().filter(i -> file.equals(i.getFile()));
    }

    public static Stream<ERPImportOperation> findByFinantialInstitution(final FinantialInstitution finantialInstitution) {
        return findAll().filter(i -> finantialInstitution.equals(i.getFinantialInstitution()));
    }

    public static Stream<ERPImportOperation> findByExecutionDate(final org.joda.time.DateTime executionDate) {
        return findAll().filter(i -> executionDate.equals(i.getExecutionDate()));
    }

    public static Stream<ERPImportOperation> findByProcessed(final boolean processed) {
        return findAll().filter(i -> processed == i.getProcessed());
    }

    public static Stream<ERPImportOperation> findBySuccess(final boolean success) {
        return findAll().filter(i -> success == i.getSuccess());
    }

    public static Stream<ERPImportOperation> findByCorrected(final boolean corrected) {
        return findAll().filter(i -> corrected == i.getCorrected());
    }

    public static Stream<ERPImportOperation> findByErrorLog(final java.lang.String errorLog) {
        return findAll().filter(i -> errorLog.equalsIgnoreCase(i.getErrorLog()));
    }

}
