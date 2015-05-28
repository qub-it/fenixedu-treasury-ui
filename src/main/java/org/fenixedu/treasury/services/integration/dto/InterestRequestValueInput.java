package org.fenixedu.treasury.services.integration.dto;

import java.math.BigDecimal;

public class InterestRequestValueInput {
    private String finantialInstitutionFiscalNumber;
    private String customerCode;
    private String debitNoteNumber;
    private Integer lineNumber;
    private BigDecimal amount;
    private Boolean generateInterestDebitNote;
    public Boolean getGenerateInterestDebitNote() {
        return generateInterestDebitNote;
    }
    public void setGenerateInterestDebitNote(Boolean generateInterestDebitNote) {
        this.generateInterestDebitNote = generateInterestDebitNote;
    }
    public BigDecimal getAmount() {
        return amount;
    }
    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }
    public Integer getLineNumber() {
        return lineNumber;
    }
    public void setLineNumber(Integer lineNumber) {
        this.lineNumber = lineNumber;
    }
    public String getDebitNoteNumber() {
        return debitNoteNumber;
    }
    public void setDebitNoteNumber(String debitNoteNumber) {
        this.debitNoteNumber = debitNoteNumber;
    }
    public String getFinantialInstitutionFiscalNumber() {
        return finantialInstitutionFiscalNumber;
    }
    public void setFinantialInstitutionFiscalNumber(String finantialInstitutionFiscalNumber) {
        this.finantialInstitutionFiscalNumber = finantialInstitutionFiscalNumber;
    }
    public String getCustomerCode() {
        return customerCode;
    }
    public void setCustomerCode(String customerCode) {
        this.customerCode = customerCode;
    }
}
