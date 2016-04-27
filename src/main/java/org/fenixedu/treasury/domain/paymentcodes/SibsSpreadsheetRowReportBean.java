package org.fenixedu.treasury.domain.paymentcodes;

import org.apache.poi.ss.usermodel.Row;
import org.fenixedu.treasury.services.payments.sibs.SIBSImportationLineDTO;
import org.fenixedu.treasury.util.Constants;
import org.fenixedu.treasury.util.streaming.spreadsheet.IErrorsLog;
import org.fenixedu.treasury.util.streaming.spreadsheet.SpreadsheetRow;

public class SibsSpreadsheetRowReportBean implements SpreadsheetRow {

    // @formatter:off
    public static String[] SPREADSHEET_HEADERS = { 
            Constants.bundle("label.SibsReportFile.whenProcessedBySibs"),
            Constants.bundle("label.SibsReportFile.filename"),
            Constants.bundle("label.SibsReportFile.transactionsTotalAmount"),
            Constants.bundle("label.SibsReportFile.totalCost"),
            Constants.bundle("label.SibsReportFile.fileVersion"),
            Constants.bundle("label.SibsReportFile.sibsTransactionId"),
            Constants.bundle("label.SibsReportFile.transactionTotalAmount"),
            Constants.bundle("label.SibsReportFile.paymentCode"),
            Constants.bundle("label.SibsReportFile.transactionWhenRegistered"),
            Constants.bundle("label.SibsReportFile.studentNumber"),
            Constants.bundle("label.SibsReportFile.personName"),
            Constants.bundle("label.SibsReportFile.description")
            /* TODO: Appears to be empty. Check if it is needed
            ,
            Constants.bundle("label.SibsReportFile.transactionDescription"),
            Constants.bundle("label.SibsReportFile.transactionAmount") 
            */ };
    // @formatter:off
    
    private SIBSImportationLineDTO line;

    public SibsSpreadsheetRowReportBean(final SIBSImportationLineDTO line) {
        this.line = line;
    }
    
    @Override
    public void writeCellValues(final Row row, final IErrorsLog errorsLog) {
        int i = 0;
        
        try {
            row.createCell(i++).setCellValue(line.getWhenProcessedBySibs().toString("yyyy-MM-dd HH:mm:ss"));
            row.createCell(i++).setCellValue(line.getFilename());
            row.createCell(i++).setCellValue(line.getTransactionsTotalAmount().toPlainString());
            row.createCell(i++).setCellValue(line.getTotalCost().toPlainString());
            row.createCell(i++).setCellValue(line.getFileVersion());
            row.createCell(i++).setCellValue(line.getSibsTransactionId());
            row.createCell(i++).setCellValue(line.getTransactionTotalAmount().toPlainString());
            row.createCell(i++).setCellValue(line.getCode());
            row.createCell(i++).setCellValue(line.getTransactionWhenRegistered().toString("yyyy-MM-dd HH:mm:ss"));
            row.createCell(i++).setCellValue(line.getStudentNumber());
            row.createCell(i++).setCellValue(line.getPersonName());
            row.createCell(i++).setCellValue(line.getDescription());
            
            /* TODO: Appears to be empty. Check if it is needed
            for (int j = 0; j < line.getNumberOfTransactions(); j++) {
                row.createCell(i++).setCellValue(line.getTransactionDescription(j));
                row.createCell(i++).setCellValue(line.getTransactionAmount(j).toPlainString());
            }
            */

            return;
        } catch (final Exception e) {
            e.printStackTrace();
            row.createCell(i++).setCellValue(Constants.bundle("error.SibsSpreadsheetRowReportBean.report.generation.verify.line"));
        }
        
    }

}
