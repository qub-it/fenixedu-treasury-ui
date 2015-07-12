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

import org.fenixedu.treasury.domain.exceptions.TreasuryDomainException;
import org.joda.time.DateTime;

import pt.ist.fenixframework.Atomic;

public abstract class IntegrationOperation extends IntegrationOperation_Base {

    protected IntegrationOperation() {
        super();
    }

    protected void init(final org.joda.time.DateTime executionDate, final boolean processed, final boolean success,
            final java.lang.String errorLog) {
        setExecutionDate(executionDate);
        setProcessed(processed);
        setSuccess(success);
        setErrorLog(errorLog);
        checkRules();
    }

    private void checkRules() {
        //
        // CHANGE_ME add more busines validations
        //

        // CHANGE_ME In order to validate UNIQUE restrictions
        // if (findByExecutionDate(getExecutionDate().count()>1)
        // {
        // throw new
        // TreasuryDomainException("error.IntegrationOperation.executionDate.duplicated");
        // }
        // if (findByProcessed(getProcessed().count()>1)
        // {
        // throw new
        // TreasuryDomainException("error.IntegrationOperation.processed.duplicated");
        // }
        // if (findBySuccess(getSuccess().count()>1)
        // {
        // throw new
        // TreasuryDomainException("error.IntegrationOperation.success.duplicated");
        // }
        // if (findByErrorLog(getErrorLog().count()>1)
        // {
        // throw new
        // TreasuryDomainException("error.IntegrationOperation.errorLog.duplicated");
        // }
    }

    @Atomic
    public void edit(final org.joda.time.DateTime executionDate, final boolean processed, final boolean success,
            final java.lang.String errorLog) {
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
        if (!isDeletable()) {
            throw new TreasuryDomainException("error.IntegrationOperation.cannot.delete");
        }
        this.setFinantialInstitution(null);
        if (this.getFile() != null) {
            this.getFile().delete();
        }
        this.setFile(null);
        deleteDomainObject();
    }

    public void appendInfoLog(String message) {
        String infoLog = this.getIntegrationLog();
        if (infoLog == null) {
            this.setIntegrationLog("");
        }
        StringBuilder builder = new StringBuilder();
        builder.append(this.getIntegrationLog()).append("\n");
        builder.append(new DateTime().toString()).append(message);
        this.setIntegrationLog(builder.toString());
    }

}
