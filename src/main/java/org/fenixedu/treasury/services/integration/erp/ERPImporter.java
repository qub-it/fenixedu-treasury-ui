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
package org.fenixedu.treasury.services.integration.erp;

import java.io.InputStream;
import java.io.StringWriter;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;

import oecd.standardauditfile_tax.pt_1.AuditFile;
import oecd.standardauditfile_tax.pt_1.PaymentMethod;
import oecd.standardauditfile_tax.pt_1.SAFTPTSettlementType;
import oecd.standardauditfile_tax.pt_1.SourceDocuments.Payments.Payment;
import oecd.standardauditfile_tax.pt_1.SourceDocuments.Payments.Payment.Line;
import oecd.standardauditfile_tax.pt_1.SourceDocuments.WorkingDocuments.WorkDocument;

import org.fenixedu.bennu.core.i18n.BundleUtil;
import org.fenixedu.treasury.domain.Customer;
import org.fenixedu.treasury.domain.debt.DebtAccount;
import org.fenixedu.treasury.domain.document.DocumentNumberSeries;
import org.fenixedu.treasury.domain.document.FinantialDocumentType;
import org.fenixedu.treasury.domain.document.Invoice;
import org.fenixedu.treasury.domain.document.InvoiceEntry;
import org.fenixedu.treasury.domain.document.PaymentEntry;
import org.fenixedu.treasury.domain.document.ReimbursementEntry;
import org.fenixedu.treasury.domain.document.SettlementEntry;
import org.fenixedu.treasury.domain.document.SettlementNote;
import org.fenixedu.treasury.domain.exceptions.TreasuryDomainException;
import org.fenixedu.treasury.domain.integration.ERPConfiguration;
import org.fenixedu.treasury.domain.integration.ERPImportOperation;
import org.fenixedu.treasury.services.integration.erp.dto.DocumentStatusWS;
import org.fenixedu.treasury.services.integration.erp.dto.DocumentsInformationOutput;
import org.fenixedu.treasury.services.integration.erp.dto.IntegrationStatusOutput.StatusType;
import org.fenixedu.treasury.util.Constants;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import pt.ist.fenixframework.Atomic;

// ******************************************************************************************************************************
// http://info.portaldasfinancas.gov.pt/NR/rdonlyres/3B4FECDB-2380-45D7-9019-ABCA80A7E99E/0/Comunicacao_Dados_Doc_Transporte.pdf
// http://info.portaldasfinancas.gov.pt/NR/rdonlyres/15D18787-8AA9-4060-90D5-79F168A927A4/0/Portaria_11922009.pdf
// (Documento Original)
// http://dre.pt/pdf1sdip/2012/11/22700/0672406740.pdf (Adenda para os
// Documentos de Transporte)
// Versão 1.0.3
// https://info.portaldasfinancas.gov.pt/NR/rdonlyres/BA9FB096-D482-445D-A5DB-C05B1980F7D7/0/Portaria_274_2013_21_09.pdf
// ******************************************************************************************************************************
public class ERPImporter {

    private static JAXBContext jaxbContext = null;
    private static Logger logger = LoggerFactory.getLogger(ERPImporter.class);
    private InputStream fileStream;

    public ERPImporter(InputStream fileStream) {
        this.fileStream = fileStream;
    }

    public AuditFile readAuditFileFromXML() {
        try {

            if (jaxbContext == null) {
                jaxbContext = JAXBContext.newInstance(AuditFile.class);
            }
            javax.xml.bind.Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();

            StringWriter writer = new StringWriter();

            AuditFile auditFile = (AuditFile) jaxbUnmarshaller.unmarshal(fileStream);
            return auditFile;
        } catch (JAXBException e) {
            return null;
        }
    }

    @Atomic
    public DocumentsInformationOutput processAuditFile(ERPImportOperation eRPImportOperation) {
        DocumentsInformationOutput result = new DocumentsInformationOutput();
        result.setDocumentStatus(new ArrayList<DocumentStatusWS>());
        result.setRequestId(eRPImportOperation.getExternalId());
        try {
            AuditFile auditFile = readAuditFileFromXML();
            BigDecimal totalCredit = BigDecimal.ZERO;
            BigDecimal totalDebit = BigDecimal.ZERO;
            BigInteger totalPayments = BigInteger.ZERO;
            for (Payment payment : auditFile.getSourceDocuments().getPayments().getPayment()) {
                DocumentStatusWS docStatus = new DocumentStatusWS();
                eRPImportOperation.appendInfoLog(BundleUtil.getString(Constants.BUNDLE, "info.ERPImporter.processing.payment",
                        payment.getPaymentRefNo()));
                try {
                    SettlementNote note = processErpPayment(payment, eRPImportOperation);
                    if (note != null) {
                        docStatus.setDocumentNumber(note.getOriginDocumentNumber());
                        docStatus.setIntegrationStatus(StatusType.SUCCESS);
                        totalPayments = totalPayments.add(BigInteger.ONE);
                        totalCredit = totalCredit.add(note.getTotalAmount());
                    } else {
                        throw new TreasuryDomainException("error.ERPImporter.processing.payment", payment.getPaymentRefNo());
                    }
                } catch (Exception ex) {
                    eRPImportOperation.appendInfoLog(BundleUtil.getString(Constants.BUNDLE,
                            "error.ERPImporter.processing.payment", payment.getPaymentRefNo()));
                    eRPImportOperation.appendInfoLog(ex.getLocalizedMessage());
                    docStatus.setDocumentNumber(payment.getPaymentRefNo());
                    docStatus.setErrorDescription(ex.getLocalizedMessage());
                    docStatus.setIntegrationStatus(StatusType.ERROR);
                }
                result.getDocumentStatus().add(docStatus);
            }
            if (totalPayments.compareTo(auditFile.getSourceDocuments().getPayments().getNumberOfEntries()) != 0) {
                throw new TreasuryDomainException("label.error.integration.erpimporter.invalid.number.of.payments");
            }
            if (totalDebit.compareTo(auditFile.getSourceDocuments().getPayments().getTotalDebit()) != 0) {
                throw new TreasuryDomainException("label.error.integration.erpimporter.invalid.total.debit");
            }
            if (totalCredit.compareTo(auditFile.getSourceDocuments().getPayments().getTotalCredit()) != 0) {
                throw new TreasuryDomainException("label.error.integration.erpimporter.invalid.total.credit");
            }

            if (eRPImportOperation.getProcessed() == true) {
                //this is a re-process. set as "corrected"
                eRPImportOperation.setCorrected(true);
            }
            eRPImportOperation.setProcessed(true);
            eRPImportOperation.setExecutionDate(new DateTime());
            eRPImportOperation.setSuccess(true);

        } catch (Exception ex) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append(eRPImportOperation.getErrorLog());
            stringBuilder.append("\n\n" + ex.getLocalizedMessage() + "\n\n");
            for (StackTraceElement el : ex.getStackTrace()) {
                stringBuilder.append(el.toString() + "\n");
            }

            eRPImportOperation.appendErrorLog(stringBuilder.toString());
            eRPImportOperation.setProcessed(true);
            eRPImportOperation.setCorrected(false);
            eRPImportOperation.setExecutionDate(new DateTime());
            eRPImportOperation.setSuccess(false);
        }
        return result;
    }

    @Atomic
    private SettlementNote processErpPayment(Payment payment, ERPImportOperation eRPImportOperation) {
        ERPConfiguration integrationConfig = eRPImportOperation.getFinantialInstitution().getErpIntegrationConfiguration();
        DocumentNumberSeries seriesToIntegratePayments =
                DocumentNumberSeries.find(FinantialDocumentType.findForSettlementNote(),
                        integrationConfig.getPaymentsIntegrationSeries());

        //if is a reimbursement, then we must find the correct document series
        if (payment.getSettlementType() != null && payment.getSettlementType().equals(SAFTPTSettlementType.NR)) {
            seriesToIntegratePayments =
                    DocumentNumberSeries.find(FinantialDocumentType.findForReimbursementNote(),
                            integrationConfig.getPaymentsIntegrationSeries());
        }

        if (seriesToIntegratePayments == null || seriesToIntegratePayments.getSeries().getExternSeries() == false) {
            throw new TreasuryDomainException("label.error.integration.erpimporter.invalid.series.to.integrate.payments");
        }

        //Get the ExternalNumber 
        String externalNumber = payment.getPaymentRefNo();
        Customer customer = Customer.findByCode(payment.getCustomerID()).findFirst().orElse(null);
        SettlementNote settlementNote = null;
        DebtAccount customerDebtAccount = null;
        if (customer != null) {
            customerDebtAccount = DebtAccount.findUnique(eRPImportOperation.getFinantialInstitution(), customer).orElse(null);
            if (customerDebtAccount != null) {
                SettlementNote existingSettlementNote =
                        SettlementNote
                                .findByDocumentNumberSeries(seriesToIntegratePayments)
                                .filter(x -> x.getOriginDocumentNumber() != null
                                        && x.getOriginDocumentNumber().equals(externalNumber)).findFirst().orElse(null);

                if (existingSettlementNote != null && existingSettlementNote.isAnnulled() == false) {
                    //Already exists... //Update ?!??!!?
                    settlementNote = existingSettlementNote;
                    if (!settlementNote.getDebtAccount().equals(customerDebtAccount)) {
                        throw new TreasuryDomainException(
                                "label.error.integration.erpimporter.invalid.debtaccount.existing.payment");
                    }

                    //HACK: DONT Accept repeting Documents
                    throw new TreasuryDomainException("label.error.integration.erpimporter.invalid.already.existing.payment");

                } else {
                    DateTime documentDate = new org.joda.time.DateTime(payment.getTransactionDate().toGregorianCalendar());
                    //Create a new SettlementNote
                    settlementNote =
                            SettlementNote.create(customerDebtAccount, seriesToIntegratePayments, documentDate, externalNumber);
                }
            } else {
                throw new TreasuryDomainException("label.error.integration.erpimporter.invalid.debtaccount.to.integrate.payments");
            }
        } else {
            throw new TreasuryDomainException("label.error.integration.erpimporter.invalid.customer.to.integrate.payments");
        }

        //Continue processing the Payment Document entries (New or Updating??!?!)
        for (Line paymentLine : payment.getLine()) {
            if (paymentLine.getSourceDocumentID().size() != 1) {
                throw new TreasuryDomainException("label.error.integration.erpimporter.invalid.line.source.in.payment");
            }
            String invoiceReferenceNumber = paymentLine.getSourceDocumentID().get(0).getOriginatingON();
            Invoice referenceInvoice =
                    Invoice.findByUIDocumentNumber(eRPImportOperation.getFinantialInstitution(), invoiceReferenceNumber).orElse(
                            null);
            if (referenceInvoice == null) {
                throw new TreasuryDomainException("label.error.integration.erpimporter.invalid.line.source.in.payment");
            }
            if (!referenceInvoice.getDebtAccount().equals(customerDebtAccount)) {
                throw new TreasuryDomainException("label.error.integration.erpimporter.invalid.line.debtaccount.in.payment");
            }
            InvoiceEntry invoiceEntry = referenceInvoice.getEntryInOrder(paymentLine.getLineNumber().intValue());
            if (invoiceEntry == null) {
                throw new TreasuryDomainException("label.error.integration.erpimporter.invalid.line.source.in.payment");
            }

            BigDecimal paymentAmount = paymentLine.getCreditAmount();

            //if it is a SettlementEntry for a CreditEntry, then we must get the "debit amount" of the SAFT paymentLine
            if (invoiceEntry.isCreditNoteEntry()) {
                paymentAmount = paymentLine.getDebitAmount();
            }

            if (invoiceEntry.getOpenAmount().compareTo(paymentAmount) < 0) {
                throw new TreasuryDomainException("label.error.integration.erpimporter.invalid.line.amount.in.payment");
            }

            //Create a new settlement entry for this payment
            SettlementEntry settlementEntry =
                    SettlementEntry.create(invoiceEntry, settlementNote, paymentAmount, invoiceEntry.getDescription(),
                            new DateTime(payment.getDocumentStatus().getPaymentStatusDate()), false);
        }

        if (payment.getSettlementType().equals(SAFTPTSettlementType.NR)) {
            //Continue processing the Reimbursment Methods (New or Updating??!?!)
            for (PaymentMethod paymentMethod : payment.getPaymentMethod()) {
                ReimbursementEntry reimbursmentEntry =
                        ReimbursementEntry.create(settlementNote,
                                convertFromSAFTPaymentMethod(paymentMethod.getPaymentMechanism()),
                                paymentMethod.getPaymentAmount());
            }
        } else {
            //Continue processing the Payment Methods (New or Updating??!?!)
            for (PaymentMethod paymentMethod : payment.getPaymentMethod()) {
                PaymentEntry paymentEntry =
                        PaymentEntry.create(convertFromSAFTPaymentMethod(paymentMethod.getPaymentMechanism()), settlementNote,
                                paymentMethod.getPaymentAmount());
            }
        }

        return settlementNote;
    }

    //convert the saft payment method to fenixEdu payment entry
    private org.fenixedu.treasury.domain.PaymentMethod convertFromSAFTPaymentMethod(String paymentMechanism) {
        org.fenixedu.treasury.domain.PaymentMethod paymentMethod =
                org.fenixedu.treasury.domain.PaymentMethod.findByCode(paymentMechanism);
        if (paymentMethod == null) {
            return org.fenixedu.treasury.domain.PaymentMethod.findAll().findFirst().orElse(null);
        }
        return paymentMethod;
    }

    public Set<String> getRelatedDocumentsNumber() {
        Set<String> result = new HashSet<String>();

        AuditFile file = readAuditFileFromXML();

        for (WorkDocument w : file.getSourceDocuments().getWorkingDocuments().getWorkDocument()) {
            result.add(w.getDocumentNumber());
        }
        for (oecd.standardauditfile_tax.pt_1.SourceDocuments.SalesInvoices.Invoice i : file.getSourceDocuments()
                .getSalesInvoices().getInvoice()) {
            result.add(i.getInvoiceNo());
        }
        for (Payment p : file.getSourceDocuments().getPayments().getPayment()) {
            result.add(p.getPaymentRefNo());
        }
        return result;
    }
}
