package org.fenixedu.treasury.services.payments.sibs;

import java.math.BigDecimal;
import java.util.List;

import org.fenixedu.treasury.domain.FinantialInstitution;
import org.fenixedu.treasury.services.payments.sibs.incomming.SibsIncommingPaymentFile;
import org.fenixedu.treasury.services.payments.sibs.incomming.SibsIncommingPaymentFileDetailLine;
import org.joda.time.LocalDate;

import com.google.common.base.Function;
import com.google.common.collect.Lists;

public class SIBSImportationFileDTO {

    protected LocalDate whenProcessedBySibs;
    protected String filename;
    protected BigDecimal transactionsTotalAmount;
    protected BigDecimal totalCost;
    protected Integer fileVersion;
    private FinantialInstitution finantialInstitution;

    protected List<SIBSImportationLineDTO> lines;

    public SIBSImportationFileDTO(final SibsIncommingPaymentFile sibsIncomingPaymentFile,
            final FinantialInstitution finantialInstitution) {
        setWhenProcessedBySibs(sibsIncomingPaymentFile.getHeader().getWhenProcessedBySibs().toLocalDate());
        setFilename(sibsIncomingPaymentFile.getFilename());
        setTransactionsTotalAmount(sibsIncomingPaymentFile.getFooter().getTransactionsTotalAmount());
        setTotalCost(sibsIncomingPaymentFile.getFooter().getTotalCost());
        setFileVersion(sibsIncomingPaymentFile.getHeader().getVersion());
        setFinantialInstitution(finantialInstitution);

        setLines(generateLines(sibsIncomingPaymentFile));
    }

    protected List<SIBSImportationLineDTO> generateLines(final SibsIncommingPaymentFile sibsIncomingPaymentFile) {
        return Lists.transform(sibsIncomingPaymentFile.getDetailLines(),
                new Function<SibsIncommingPaymentFileDetailLine, SIBSImportationLineDTO>() {
                    @Override
                    public SIBSImportationLineDTO apply(final SibsIncommingPaymentFileDetailLine line) {
                        return new SIBSImportationLineDTO(SIBSImportationFileDTO.this, line);
                    }
                });
    }

    public List<SIBSImportationLineDTO> getLines() {
        return lines;
    }

    public void setLines(List<SIBSImportationLineDTO> lines) {
        this.lines = lines;
    }

    public LocalDate getWhenProcessedBySibs() {
        return whenProcessedBySibs;
    }

    public void setWhenProcessedBySibs(final LocalDate whenProcessedBySibs) {
        this.whenProcessedBySibs = whenProcessedBySibs;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public BigDecimal getTransactionsTotalAmount() {
        return transactionsTotalAmount;
    }

    public void setTransactionsTotalAmount(BigDecimal transactionsTotalAmount) {
        this.transactionsTotalAmount = transactionsTotalAmount;
    }

    public BigDecimal getTotalCost() {
        return totalCost;
    }

    public void setTotalCost(BigDecimal totalCost) {
        this.totalCost = totalCost;
    }

    public Integer getFileVersion() {
        return fileVersion;
    }

    public void setFileVersion(final Integer fileVersion) {
        this.fileVersion = fileVersion;
    }

    public FinantialInstitution getFinantialInstitution() {
        return finantialInstitution;
    }

    public void setFinantialInstitution(FinantialInstitution finantialInstitution) {
        this.finantialInstitution = finantialInstitution;
    }

}
