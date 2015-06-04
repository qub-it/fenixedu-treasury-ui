/**
 * Copyright © 2002 Instituto Superior Técnico
 *
 * This file is part of FenixEdu Academic.
 *
 * FenixEdu Academic is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * FenixEdu Academic is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with FenixEdu Academic.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.fenixedu.treasury.services.payments.sibs;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Arrays;

import org.apache.commons.lang.StringUtils;
import org.fenixedu.bennu.core.domain.User;
import org.fenixedu.treasury.domain.FinantialInstitution;
import org.fenixedu.treasury.domain.exceptions.TreasuryDomainException;
import org.fenixedu.treasury.domain.paymentcodes.PaymentReferenceCode;
import org.fenixedu.treasury.domain.paymentcodes.PaymentReferenceCodeStateType;
import org.fenixedu.treasury.domain.paymentcodes.SibsInputFile;
import org.fenixedu.treasury.domain.paymentcodes.SibsReportFile;
import org.fenixedu.treasury.services.payments.sibs.incomming.SibsIncommingPaymentFile;
import org.fenixedu.treasury.services.payments.sibs.incomming.SibsIncommingPaymentFileDetailLine;
import org.fenixedu.treasury.ui.TreasuryBaseController;
import org.fenixedu.treasury.util.FileUtils;

import com.google.common.io.ByteStreams;
import com.google.common.io.Files;

public class SIBSPaymentsImporter {

    static private final String PAYMENT_FILE_EXTENSION = "INP";
    static private final String ZIP_FILE_EXTENSION = "ZIP";

//    static public class UploadBean implements Serializable {
//        private static final long serialVersionUID = 3625314688141697558L;
//
//        private transient InputStream inputStream;
//
//        private String filename;
//
//        public InputStream getInputStream() {
//            return inputStream;
//        }
//
//        public void setInputStream(InputStream inputStream) {
//            this.inputStream = inputStream;
//        }
//
//        public String getFilename() {
//            return filename;
//        }
//
//        public void setFilename(String filename) {
//            this.filename = StringNormalizer.normalize(filename);
//        }
//    }

    private class ProcessResult {

//        private final TreasuryBaseController baseController;
        private boolean processFailed = false;

        public ProcessResult(TreasuryBaseController baseController) {
//            this.baseController = baseController;
        }

        public void addMessage(String message, String... args) {
//            baseController.addActionMessage("message", request, message, args);
        }

        public void addError(String message, String... args) {
//            addActionMessage("message", request, message, args);
            reportFailure();
        }

        protected void reportFailure() {
            processFailed = true;
        }

        public boolean hasFailed() {
            return processFailed;
        }
    }

    public void processSIBSPaymentFiles(SibsInputFile inputFile) throws IOException {

        if (StringUtils.endsWithIgnoreCase(inputFile.getFilename(), ZIP_FILE_EXTENSION)) {
            File zipFile = FileUtils.copyToTemporaryFile(inputFile.getStream());
            File unzipDir = null;
            try {
                unzipDir = FileUtils.unzipFile(zipFile);
                if (!unzipDir.isDirectory()) {
                    throw new TreasuryDomainException("error.manager.SIBS.zipException", inputFile.getFilename());
                }
            } catch (Exception e) {
                throw new TreasuryDomainException("error.manager.SIBS.zipException", getMessage(e));
            } finally {
                zipFile.delete();
            }

            recursiveZipProcess(unzipDir);

        } else if (StringUtils.endsWithIgnoreCase(inputFile.getFilename(), PAYMENT_FILE_EXTENSION)) {
            InputStream inputStream = bean.getInputStream();
            File dir = Files.createTempDir();
            File tmp = new File(dir, inputFile.getFilename());
            tmp.deleteOnExit();

            try (OutputStream out = new FileOutputStream(tmp)) {
                ByteStreams.copy(inputStream, out);
            } finally {
                inputStream.close();
            }
            File file = tmp;
            ProcessResult result = new ProcessResult(null);
            result.addMessage("label.manager.SIBS.processingFile", file.getName());
            try {
                processFile(file);
            } catch (FileNotFoundException e) {
                throw new TreasuryDomainException("error.manager.SIBS.zipException", getMessage(e));
            } catch (IOException e) {
                throw new TreasuryDomainException("error.manager.SIBS.IOException", getMessage(e));
            } catch (Exception e) {
                throw new TreasuryDomainException("error.manager.SIBS.fileException", getMessage(e));
            } finally {
                file.delete();
            }
        } else {
            throw new TreasuryDomainException("error.manager.SIBS.notSupportedExtension", inputFile.getFilename());
        }
        return prepareUploadSIBSPaymentFiles(mapping, form, request, response);
    }

//    private static String getMessage(Exception ex) {
//        String message = ex.getMessage() == null ? ex.getClass().getSimpleName() : ex.getMessage();
//        return BundleUtil.getString(Constants.BUNDLE, message);
//    }

    private void recursiveZipProcess(File unzipDir) {
        File[] filesInZip = unzipDir.listFiles();
        Arrays.sort(filesInZip);

        for (File file : filesInZip) {

            if (file.isDirectory()) {
                recursiveZipProcess(file);

            } else {

                if (!StringUtils.endsWithIgnoreCase(file.getName(), PAYMENT_FILE_EXTENSION)) {
                    file.delete();
                    continue;
                }

                try {

                    processFile(file);

                } catch (FileNotFoundException e) {
                    throw new TreasuryDomainException("error.manager.SIBS.zipException", getMessage(e));
                } catch (IOException e) {
                    throw new TreasuryDomainException("error.manager.SIBS.IOException", getMessage(e));
                } catch (Exception e) {
                    throw new TreasuryDomainException("error.manager.SIBS.fileException", getMessage(e));
                } finally {
                    file.delete();
                }
            }
        }

        unzipDir.delete();
    }

//    private void processFile(File file) throws IOException {
//        final ProcessResult result = new ProcessResult(null);
//        result.addMessage("label.manager.SIBS.processingFile", file.getName());
//
//        FileInputStream fileInputStream = null;
//        try {
//            fileInputStream = new FileInputStream(file);
//            final User person = null;//HACK: get the User logged in
//            final SibsIncommingPaymentFile sibsFile = SibsIncommingPaymentFile.parse(file.getName(), fileInputStream);
//
//            result.addMessage("label.manager.SIBS.linesFound", String.valueOf(sibsFile.getDetailLines().size()));
//            result.addMessage("label.manager.SIBS.startingProcess");
//
//            for (final SibsIncommingPaymentFileDetailLine detailLine : sibsFile.getDetailLines()) {
//                try {
//                    processCode(detailLine, person, result);
//                } catch (Exception e) {
//                    result.addError("error.manager.SIBS.processException", detailLine.getCode(), getMessage(e));
//                }
//            }
//
//            result.addMessage("label.manager.SIBS.creatingReport");
//
//            if (!result.hasFailed()) {
//                if (SibsPaymentFileProcessReport.hasAny(sibsFile.getWhenProcessedBySibs(), sibsFile.getVersion())) {
//                    result.addMessage("warning.manager.SIBS.reportAlreadyProcessed");
//                } else {
//                    try {
//                        createSibsFileReport(sibsFile, result);
//                    } catch (Exception ex) {
//                        result.addError("error.manager.SIBS.reportException", getMessage(ex));
//                    }
//                }
//            } else {
//                result.addError("error.manager.SIBS.nonProcessedCodes");
//            }
//
//            result.addMessage("label.manager.SIBS.done");
//
//        } finally {
//            if (fileInputStream != null) {
//                fileInputStream.close();
//            }
//        }
//    }

//    private void processCode(FinantialInstitution finantialInstitution, SibsIncommingPaymentFileDetailLine detailLine,
//            User person, ProcessResult result) throws Exception {
//
//        final PaymentReferenceCode paymentCode = getPaymentReferenceCode(finantialInstitution, detailLine, result);
//
//        if (paymentCode == null) {
//            result.addMessage("error.manager.SIBS.codeNotFound", detailLine.getCode());
//            throw new Exception();
//        }
//
//        final PaymentReferenceCode codeToProcess =
//                getPaymentCodeToProcess(paymentCode, ExecutionYear.readByDateTime(detailLine.getWhenOccuredTransaction()), result);
//
//        if (codeToProcess.getState() == PaymentReferenceCodeStateType.ANNULLED) {
//            result.addMessage("warning.manager.SIBS.anulledCode", codeToProcess.getReferenceCode());
//        }
//
//        if (codeToProcess.isUsed() && codeToProcess.getWhenUpdated().isBefore(detailLine.getWhenOccuredTransaction())) {
//            result.addMessage("warning.manager.SIBS.codeAlreadyProcessed", codeToProcess.getReferenceCode());
//        }
//
//        codeToProcess.process(person, detailLine.getAmount(), detailLine.getWhenOccuredTransaction(),
//                detailLine.getSibsTransactionId(), StringUtils.EMPTY);
//
//    }

//    private PaymentReferenceCode getPaymentCodeToProcess(final PaymentReferenceCode paymentCode, ExecutionYear executionYear,
//            ProcessResult result) {
//
//        final PaymentCodeMapping mapping = paymentCode.getOldPaymentCodeMapping(executionYear);
//
//        final PaymentReferenceCode codeToProcess;
//        if (mapping != null) {
//
//            result.addMessage("warning.manager.SIBS.foundMapping", paymentCode.getReferenceCode(), mapping.getNewPaymentCode()
//                    .getCode());
//            result.addMessage("warning.manager.SIBS.invalidating", paymentCode.getReferenceCode());
//
//            codeToProcess = mapping.getNewPaymentCode();
//            paymentCode.setState(PaymentReferenceCodeStateType.ANNULLED);
//
//        } else {
//            codeToProcess = paymentCode;
//        }
//
//        return codeToProcess;
//    }

    private PaymentReferenceCode getPaymentReferenceCode(final FinantialInstitution finantialInstitution, final String code,
            ProcessResult result) {
        /*
         * TODO:
         * 
         * 09/07/2009 - Payments are not related only to students. readAll() may
         * be heavy to get the PaymentCode.
         * 
         * 
         * Ask Nadir and Joao what is best way to deal with PaymentCode
         * retrieval.
         */

        return PaymentReferenceCode.findByReferenceCode(code, finantialInstitution).findFirst().orElse(null);
    }

    protected String getMessage(Exception ex) {
        String message = ex.getMessage() == null ? ex.getClass().getSimpleName() : ex.getMessage();

        return message;
    }

    protected void processFile(File file) throws IOException {
        byte[] sibsInputFile = FileUtils.readByteArray(file);
        User x = null;
        SibsInputFile.create(file.getName(), file.getName(), sibsInputFile, x);

        final ProcessResult result = new ProcessResult(null);
        result.addMessage("label.manager.SIBS.processingFile", file.getName());

        FileInputStream fileInputStream = null;
        try {
            fileInputStream = new FileInputStream(file);
            final User person = null; //HACK;
            final SibsIncommingPaymentFile sibsFile = SibsIncommingPaymentFile.parse(file.getName(), fileInputStream);

            result.addMessage("label.manager.SIBS.linesFound", String.valueOf(sibsFile.getDetailLines().size()));
            result.addMessage("label.manager.SIBS.startingProcess");

            for (final SibsIncommingPaymentFileDetailLine detailLine : sibsFile.getDetailLines()) {
                try {
                    processCode(detailLine, person, result, file.getName().replace("\\.inp", ""));
                } catch (Exception e) {
                    result.addError("error.manager.SIBS.processException", detailLine.getCode(), getMessage(e));
                }
            }

            result.addMessage("label.manager.SIBS.creatingReport");

            if (result.hasFailed()) {
                result.addError("error.manager.SIBS.nonProcessedCodes");
            }

            try {
                createSibsFileReport(sibsFile, result);
            } catch (Exception ex) {
                ex.printStackTrace();
                result.addError("error.manager.SIBS.reportException", getMessage(ex));
            }

            result.addMessage("label.manager.SIBS.done");

        } finally {
            if (fileInputStream != null) {
                fileInputStream.close();
            }
        }
    }

    protected void processCode(SibsIncommingPaymentFileDetailLine detailLine, User person, ProcessResult result,
            final String sibsImportationFile) throws Exception {

        final PaymentReferenceCode paymentCode = getPaymentCode(detailLine, result);

        if (paymentCode == null) {
            result.addMessage("error.manager.SIBS.codeNotFound", detailLine.getCode());
            throw new Exception();
        }

        final PaymentReferenceCode codeToProcess =
                getPaymentCodeToProcess(paymentCode, ExecutionYear.readByDateTime(detailLine.getWhenOccuredTransaction()), result);

        if (codeToProcess.getState() == PaymentReferenceCodeStateType.ANNULLED) {
            result.addMessage("warning.manager.SIBS.anulledCode", codeToProcess.getReferenceCode());
        }

        if (codeToProcess.isProcessed() && codeToProcess.getWhenUpdated().isBefore(detailLine.getWhenOccuredTransaction())) {
            result.addMessage("warning.manager.SIBS.codeAlreadyProcessed", codeToProcess.getReferenceCode());
        }

        codeToProcess.process(person, detailLine.getAmount(), detailLine.getWhenOccuredTransaction(),
                detailLine.getSibsTransactionId(), sibsImportationFile);

    }

    protected void createSibsFileReport(final SibsIncommingPaymentFile sibsIncomingPaymentFile, final ProcessResult result)
            throws Exception {
        final SIBSImportationFileDTO reportDTO = new SIBSImportationFileDTO(sibsIncomingPaymentFile);
        SibsReportFile.create(reportDTO);
        result.addMessage("label.manager.SIBS.reportCreated");
    }

    /**
     * Copied from head
     */
    private PaymentReferenceCode getPaymentCodeToProcess(final PaymentReferenceCode paymentCode, ProcessResult result) {

        final PaymentReferenceCode codeToProcess;
//        if (mapping != null) {
//
//            result.addMessage("warning.manager.SIBS.foundMapping", paymentCode.getReferenceCode(), mapping.getNewPaymentCode()
//                    .getCode());
//            result.addMessage("warning.manager.SIBS.invalidating", paymentCode.getReferenceCode());
//
//            codeToProcess = mapping.getNewPaymentCode();
//            paymentCode.setState(PaymentReferenceCodeStateType.ANNULLED);
//
//        } else {
        codeToProcess = paymentCode;
//        }

        return codeToProcess;
    }

//    /**
//     * Copied from head
//     */
//    private PaymentCode getPaymentCode(final SibsIncommingPaymentFileDetailLine detailLine, ProcessResult result) {
//        return getPaymentCode(detailLine.getCode(), result);
//    }

//    /**
//     * Copied from head
//     */
//    private PaymentCode getPaymentCode(final String code, ProcessResult result) {
//        /*
//         * TODO:
//         * 
//         * 09/07/2009 - Payments are not related only to students. readAll() may
//         * be heavy to get the PaymentCode.
//         * 
//         * 
//         * Ask Nadir and Joao what is best way to deal with PaymentCode
//         * retrieval.
//         */
//
//        return PaymentCode.readByCode(code);
//    }

}
