/**
 * This file was created by Quorum Born IT <http://www.qub-it.com/> and its 
 * copyright terms are bind to the legal agreement regulating the FenixEdu@ULisboa 
 * software development project between Quorum Born IT and Serviços Partilhados da
 * Universidade de Lisboa:
 *  - Copyright © 2015 Quorum Born IT (until any Go-Live phase)
 *  - Copyright © 2015 Universidade de Lisboa (after any Go-Live phase)
 *
 * Contributors: ricardo.pedro@qub-it.com, anil.mamede@qub-it.com
 * 
 *
 * 
 * This file is part of FenixEdu Treasury.
 *
 * FenixEdu Treasury is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * FenixEdu Treasury is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with FenixEdu Treasury.  If not, see <http://www.gnu.org/licenses/>.
 */
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
		
		public static Stream<ExportOperation> findAllExportOperations() {
		    return Bennu.getInstance().getIntegrationOperationsSet().stream().filter(x->x instanceof ExportOperation).map(ExportOperation.class::cast);
		}
		
		public static Stream<ExportOperation> findExportOperationsByExecutionDate(final org.joda.time.DateTime executionDate) {
			return findAllExportOperations().filter(i->executionDate.equals(i.getExecutionDate()));
		  }
		public static Stream<ExportOperation> findExportOperationsByProcessed(final boolean processed) {
			return findAllExportOperations().filter(i->processed == i.getProcessed());
		  }
		public static Stream<ExportOperation> findExportOperationBySuccess(final boolean success) {
			return findAllExportOperations().filter(i->success == i.getSuccess());
		  }
		public static Stream<ExportOperation> findExportOperationByErrorLog(final java.lang.String errorLog) {
			return findAllExportOperations().filter(i->errorLog.equalsIgnoreCase(i.getErrorLog()));
		  }
	   
}
