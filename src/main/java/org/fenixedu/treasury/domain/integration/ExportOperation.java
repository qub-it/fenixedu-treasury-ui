package org.fenixedu.treasury.domain.integration;

import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.fenixedu.bennu.core.domain.Bennu;
import org.fenixedu.treasury.domain.exceptions.TreasuryDomainException;

import pt.ist.fenixframework.Atomic;

public class ExportOperation extends ExportOperation_Base {
        
	   protected ExportOperation() {
	        super();
	        setBennu(Bennu.getInstance());
	    }
	    
	    protected void init(final org.joda.time.DateTime executionDate,final boolean processed,final boolean success,final java.lang.String errorLog) {
	setExecutionDate(executionDate);
	setProcessed(processed);
	setSuccess(success);
	setErrorLog(errorLog);
	    	checkRules();
	    }

		private void checkRules() {
			//
			//CHANGE_ME add more busines validations
			//
			
			//CHANGE_ME In order to validate UNIQUE restrictions
			//if (findByExecutionDate(getExecutionDate().count()>1)
			//{
			//	throw new TreasuryDomainException("error.ExportOperation.executionDate.duplicated");
			//}	
			//if (findByProcessed(getProcessed().count()>1)
			//{
			//	throw new TreasuryDomainException("error.ExportOperation.processed.duplicated");
			//}	
			//if (findBySuccess(getSuccess().count()>1)
			//{
			//	throw new TreasuryDomainException("error.ExportOperation.success.duplicated");
			//}	
			//if (findByErrorLog(getErrorLog().count()>1)
			//{
			//	throw new TreasuryDomainException("error.ExportOperation.errorLog.duplicated");
			//}	
		}
		
		@Atomic
		public void edit(final org.joda.time.DateTime executionDate,final boolean processed,final boolean success,final java.lang.String errorLog) {
		    setExecutionDate(executionDate);
		    setProcessed(processed);
		    setSuccess(success);
		    setErrorLog(errorLog);
		    checkRules();
		}
		
		public boolean isDeletable() {
		    return true;
		}
		
		@Atomic
		public void delete() {
		    if(!isDeletable()) {
		        throw new TreasuryDomainException("error.ExportOperation.cannot.delete");
		    }
		    
		    setBennu(null);
		    
		    deleteDomainObject();
		}
		
		 
	    @Atomic
	    public static ExportOperation create(final org.joda.time.DateTime executionDate,final boolean processed,final boolean success,final java.lang.String errorLog) {
	    	ExportOperation exportOperation = new ExportOperation();
	        exportOperation.init( executionDate, processed, success, errorLog);
	        return exportOperation;
	    }

		// @formatter: off
		/************
		 * SERVICES *
		 ************/
	    // @formatter: on
		
		public static Stream<ExportOperation> findAll() {
		    return Bennu.getInstance().getIntegrationOperationsSet().stream().filter(x->x instanceof ExportOperation).map(ExportOperation.class::cast);
		}
		
		public static Stream<ExportOperation> findByExecutionDate(final org.joda.time.DateTime executionDate) {
			return findAll().filter(i->executionDate.equals(i.getExecutionDate()));
		  }
		public static Stream<ExportOperation> findByProcessed(final boolean processed) {
			return findAll().filter(i->processed == i.getProcessed());
		  }
		public static Stream<ExportOperation> findBySuccess(final boolean success) {
			return findAll().filter(i->success == i.getSuccess());
		  }
		public static Stream<ExportOperation> findByErrorLog(final java.lang.String errorLog) {
			return findAll().filter(i->errorLog.equalsIgnoreCase(i.getErrorLog()));
		  }
	   
}
