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
package org.fenixedu.treasury.services.integration.erp.dto;

import java.util.ArrayList;
import java.util.List;

public class IntegrationStatusOutput {

    public enum StatusType {
        PENDING, ERROR, SUCCESS;
    }

    public static class DocumentStatusWS {
        private String documentNumber;
        private StatusType integrationStatus;
        private String errorDescription;

        public String getDocumentNumber() {
            return documentNumber;
        }

        public void setDocumentNumber(String documentNumber) {
            this.documentNumber = documentNumber;
        }

        public String getErrorDescription() {
            return errorDescription;
        }

        public void setErrorDescription(String errorDescription) {
            this.errorDescription = errorDescription;
        }

        public StatusType getIntegrationStatus() {
            return integrationStatus;
        }

        public void setIntegrationStatus(StatusType integrationStatus) {
            this.integrationStatus = integrationStatus;
        }
    }

    private List<DocumentStatusWS> _documentStatus;
    private String requestId;

    public IntegrationStatusOutput(String requestId) {
        this.setRequestId(requestId);
        set_documentStatus(new ArrayList<DocumentStatusWS>());
    }

    public String getRequestId() {
        return requestId;
    }

    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }

    public List<DocumentStatusWS> get_documentStatus() {
        return _documentStatus;
    }

    public void set_documentStatus(List<DocumentStatusWS> _documentStatus) {
        this._documentStatus = _documentStatus;
    }
}
