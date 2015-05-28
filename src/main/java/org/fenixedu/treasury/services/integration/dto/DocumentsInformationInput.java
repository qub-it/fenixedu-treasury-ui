package org.fenixedu.treasury.services.integration.dto;

public class DocumentsInformationInput {
    private String finantialInstitution;
    private Byte[] data;
    private String dataURI;

    public DocumentsInformationInput() {

    }

    public String getDataURI() {
        return dataURI;
    }

    public void setDataURI(String dataURI) {
        this.dataURI = dataURI;
    }

    public Byte[] getData() {
        return data;
    }

    public void setData(Byte[] data) {
        this.data = data;
    }

    public String getFinantialInstitution() {
        return finantialInstitution;
    }

    public void setFinantialInstitution(String finantialInstitution) {
        this.finantialInstitution = finantialInstitution;
    }
}
