package org.fenixedu.treasury.services.integration.erp.dto;

import org.fenixedu.treasury.services.integration.erp.dto.IntegrationStatusOutput.StatusType;

public class DocumentStatusWS {
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

    public boolean isIntegratedWithSuccess() {
        return this.getIntegrationStatus() != null && this.getIntegrationStatus().equals(StatusType.SUCCESS);
    }
}