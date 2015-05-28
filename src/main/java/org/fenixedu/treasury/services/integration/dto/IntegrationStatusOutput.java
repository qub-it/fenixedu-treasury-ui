package org.fenixedu.treasury.services.integration.dto;

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
