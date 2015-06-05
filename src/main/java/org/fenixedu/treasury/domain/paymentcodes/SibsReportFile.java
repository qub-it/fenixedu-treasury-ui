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
package org.fenixedu.treasury.domain.paymentcodes;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.stream.Stream;

import org.fenixedu.bennu.core.domain.Bennu;
import org.fenixedu.bennu.core.domain.User;
import org.fenixedu.bennu.core.i18n.BundleUtil;
import org.fenixedu.commons.spreadsheet.SheetData;
import org.fenixedu.commons.spreadsheet.SpreadsheetBuilder;
import org.fenixedu.commons.spreadsheet.WorkbookExportFormat;
import org.fenixedu.treasury.domain.exceptions.TreasuryDomainException;
import org.fenixedu.treasury.services.payments.sibs.SIBSImportationFileDTO;
import org.fenixedu.treasury.services.payments.sibs.SIBSImportationLineDTO;
import org.fenixedu.treasury.util.Constants;
//import pt.utl.ist.fenix.tools.spreadsheet.SheetData;
//import pt.utl.ist.fenix.tools.spreadsheet.SpreadsheetBuilder;
//import pt.utl.ist.fenix.tools.spreadsheet.WorkbookExportFormat
import org.joda.time.DateTime;

import pt.ist.fenixframework.Atomic;

public class SibsReportFile extends SibsReportFile_Base {

    public static final String CONTENT_TYPE = "application/vnd.oasis.opendocument.text";

    protected SibsReportFile() {
        super();
        setBennu(Bennu.getInstance());
    }

    protected SibsReportFile(final org.joda.time.DateTime whenProcessedBySibs,
            final java.math.BigDecimal transactionsTotalAmount, final java.math.BigDecimal totalCost, final String displayName,
            final String fileName, final byte[] content) {
        this();
        this.init(whenProcessedBySibs, transactionsTotalAmount, totalCost, displayName, fileName, content);

        checkRules();
    }

    protected void init(final org.joda.time.DateTime whenProcessedBySibs, final java.math.BigDecimal transactionsTotalAmount,
            final java.math.BigDecimal totalCost, final String displayName, final String fileName, final byte[] content) {

        super.init(displayName, fileName, content);
        setWhenProcessedBySibs(whenProcessedBySibs);
        setTransactionsTotalAmount(transactionsTotalAmount);
        setTotalCost(totalCost);
        checkRules();
    }

    private void checkRules() {
        //
        //CHANGE_ME add more busines validations
        //

        //CHANGE_ME In order to validate UNIQUE restrictions
        //if (findByWhenProcessedBySibs(getWhenProcessedBySibs().count()>1)
        //{
        //	throw new TreasuryDomainException("error.SibsReportFile.whenProcessedBySibs.duplicated");
        //}	
        //if (findByTransactionsTotalAmount(getTransactionsTotalAmount().count()>1)
        //{
        //	throw new TreasuryDomainException("error.SibsReportFile.transactionsTotalAmount.duplicated");
        //}	
        //if (findByTotalCost(getTotalCost().count()>1)
        //{
        //	throw new TreasuryDomainException("error.SibsReportFile.totalCost.duplicated");
        //}	
    }

    @Atomic
    public void edit(final org.joda.time.DateTime whenProcessedBySibs, final java.math.BigDecimal transactionsTotalAmount,
            final java.math.BigDecimal totalCost) {
        setWhenProcessedBySibs(whenProcessedBySibs);
        setTransactionsTotalAmount(transactionsTotalAmount);
        setTotalCost(totalCost);
        checkRules();
    }

    public boolean isDeletable() {
        return true;
    }

    @Override
    @Atomic
    public void delete() {
        if (!isDeletable()) {
            throw new TreasuryDomainException("error.SibsReportFile.cannot.delete");
        }

        setBennu(null);

        deleteDomainObject();
    }

    // @formatter: off
    /************
     * SERVICES *
     ************/
    // @formatter: on

    public static Stream<SibsReportFile> findAll() {
        return Bennu.getInstance().getSibsReportFilesSet().stream();
    }

    public static Stream<SibsReportFile> findByBennu(final Bennu bennu) {
        return findAll().filter(i -> bennu.equals(i.getBennu()));
    }

    public static Stream<SibsReportFile> findByWhenProcessedBySibs(final org.joda.time.LocalDate whenProcessedBySibs) {
        return findAll().filter(i -> whenProcessedBySibs.equals(i.getWhenProcessedBySibs()));
    }

    public static Stream<SibsReportFile> findByTransactionsTotalAmount(final java.math.BigDecimal transactionsTotalAmount) {
        return findAll().filter(i -> transactionsTotalAmount.equals(i.getTransactionsTotalAmount()));
    }

    public static Stream<SibsReportFile> findByTotalCost(final java.math.BigDecimal totalCost) {
        return findAll().filter(i -> totalCost.equals(i.getTotalCost()));
    }

    @Override
    public boolean isAccessible(User arg0) {
        // TODO Auto-generated method stub
        return false;
    }

    @Atomic
    public static SibsReportFile create(final org.joda.time.DateTime whenProcessedBySibs,
            final java.math.BigDecimal transactionsTotalAmount, final java.math.BigDecimal totalCost, final String displayName,
            final String fileName, final byte[] content) {
        return new SibsReportFile(whenProcessedBySibs, transactionsTotalAmount, totalCost, displayName, fileName, content);

    }

    protected byte[] buildContentFor(final SIBSImportationFileDTO reportFileDTO) {
        final String whenProcessedBySibsLabel =
                BundleUtil.getString(Constants.BUNDLE, "label.SibsReportFile.whenProcessedBySibs");
        final String filenameLabel = BundleUtil.getString(Constants.BUNDLE, "label.SibsReportFile.filename");
        final String transactionsTotalAmountLabel =
                BundleUtil.getString(Constants.BUNDLE, "label.SibsReportFile.transactionsTotalAmount");
        final String totalCostLabel = BundleUtil.getString(Constants.BUNDLE, "label.SibsReportFile.totalCost");
        final String fileVersionLabel = BundleUtil.getString(Constants.BUNDLE, "label.SibsReportFile.fileVersion");
        final String sibsTransactionIdLabel = BundleUtil.getString(Constants.BUNDLE, "label.SibsReportFile.sibsTransactionId");
        final String sibsTransactionTotalAmountLabel =
                BundleUtil.getString(Constants.BUNDLE, "label.SibsReportFile.transactionTotalAmount");
        final String transactionWhenRegisteredLabel =
                BundleUtil.getString(Constants.BUNDLE, "label.SibsReportFile.transactionWhenRegistered");
        final String transactionDescriptionLabel =
                BundleUtil.getString(Constants.BUNDLE, "label.SibsReportFile.transactionDescription");
        final String transactionAmountLabel = BundleUtil.getString(Constants.BUNDLE, "label.SibsReportFile.transactionAmount");
        final String paymentCodeLabel = BundleUtil.getString(Constants.BUNDLE, "label.SibsReportFile.paymentCode");
        final String studentNumberLabel = BundleUtil.getString(Constants.BUNDLE, "label.SibsReportFile.studentNumber");
        final String personNameLabel = BundleUtil.getString(Constants.BUNDLE, "label.SibsReportFile.personName");

        final String descriptionLabel = BundleUtil.getString(Constants.BUNDLE, "label.SibsReportFile.description");

        final SheetData<SIBSImportationLineDTO> sheetData = new SheetData<SIBSImportationLineDTO>(reportFileDTO.getLines()) {

            @Override
            protected void makeLine(final SIBSImportationLineDTO line) {
                addCell(whenProcessedBySibsLabel, line.getWhenProcessedBySibs());
                addCell(filenameLabel, line.getFilename());
                addCell(transactionsTotalAmountLabel, line.getTransactionsTotalAmount().toPlainString());
                addCell(totalCostLabel, line.getTotalCost().toPlainString());
                addCell(fileVersionLabel, line.getFileVersion());
                addCell(sibsTransactionIdLabel, line.getSibsTransactionId());
                addCell(sibsTransactionTotalAmountLabel, line.getTransactionTotalAmount().toPlainString());
                addCell(paymentCodeLabel, line.getCode());
                addCell(transactionWhenRegisteredLabel, line.getTransactionWhenRegistered().toString("yyyy-MM-dd HH:mm"));
                addCell(studentNumberLabel, line.getStudentNumber());
                addCell(personNameLabel, line.getPersonName());
                addCell(descriptionLabel, line.getDescription());

//                for (int i = 0; i < line.getNumberOfTransactions(); i++) {
//                    addCell(transactionDescriptionLabel, line.getTransactionDescription(i));
//                    addCell(transactionAmountLabel, line.getTransactionAmount(i));
//                }
            }
        };

        final String sheetName = "label.SibsReportFile.sheetName";
        BundleUtil.getString(Constants.BUNDLE, sheetName);

        ByteArrayOutputStream outputStream = null;
        try {
            outputStream = new ByteArrayOutputStream();
            new SpreadsheetBuilder().addSheet(sheetName, sheetData).build(WorkbookExportFormat.EXCEL, outputStream);
            return outputStream.toByteArray();
        } catch (IOException e) {
            throw new TreasuryDomainException("error.SibsReportFile.spreadsheet.generation.failed");
        } finally {
            try {
                outputStream.close();
            } catch (IOException e) {
                throw new TreasuryDomainException("error.SibsReportFile.spreadsheet.generation.failed");
            }
        }
    }

    protected static String filenameFor(final SIBSImportationFileDTO reportFileDTO) {
        final String date = new DateTime().toString("yyyyMMddHHmm");
        return "Relatorio-SIBS-" + date + ".xlsx";
    }

    protected static String displayNameFor(final SIBSImportationFileDTO reportFileDTO) {
        final String date = new DateTime().toString("yyyyMMddHHmm");
        return "Relatorio-SIBS-" + date;
    }

    public static SibsReportFile create(SIBSImportationFileDTO reportDTO) {
        return create(reportDTO.getWhenProcessedBySibs(), reportDTO.getTransactionsTotalAmount(), reportDTO.getTotalCost(),
                displayNameFor(reportDTO), filenameFor(reportDTO), null);

    }
//    public static Set<SibsReportFile> findAll() {
//        return RootDomainObject.getInstance().getSibsReportFilesSet();
//    }
//
//    @Atomic
//    public static SibsReportFile create(final SIBSImportationFileDTO reportFileDTO) {
//        return new SibsReportFile(reportFileDTO);
//    }

}
