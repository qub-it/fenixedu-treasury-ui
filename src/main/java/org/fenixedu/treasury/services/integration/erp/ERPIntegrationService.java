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
package org.fenixedu.treasury.services.integration.erp;

import java.io.File;
import java.util.Optional;
import java.util.Set;

import javax.jws.WebMethod;
import javax.jws.WebService;

import org.apache.commons.lang.ArrayUtils;
import org.fenixedu.treasury.domain.FinantialInstitution;
import org.fenixedu.treasury.domain.document.FinantialDocument;
import org.fenixedu.treasury.domain.exceptions.TreasuryDomainException;
import org.fenixedu.treasury.domain.integration.ERPConfiguration;
import org.fenixedu.treasury.domain.integration.ERPImportOperation;
import org.fenixedu.treasury.domain.integration.OperationFile;
import org.fenixedu.treasury.services.integration.erp.dto.DocumentsInformationInput;
import org.fenixedu.treasury.services.integration.erp.dto.IntegrationStatusOutput;
import org.fenixedu.treasury.services.integration.erp.dto.IntegrationStatusOutput.DocumentStatusWS;
import org.fenixedu.treasury.services.integration.erp.dto.IntegrationStatusOutput.StatusType;
import org.fenixedu.treasury.services.integration.erp.dto.InterestRequestValueInput;
import org.fenixedu.treasury.services.integration.erp.dto.InterestRequestValueOuptut;
import org.joda.time.DateTime;

import pt.ist.fenixframework.FenixFramework;

import com.google.common.io.Files;
import com.qubit.solution.fenixedu.bennu.webservices.services.server.BennuWebService;

@WebService
public class ERPIntegrationService extends BennuWebService {

    public static boolean validate(String username, String password) {
        Optional<FinantialInstitution> findUniqueByFiscalCode = FinantialInstitution.findUniqueByFiscalCode(username);
        if (findUniqueByFiscalCode.isPresent()) {

            ERPConfiguration configuration = findUniqueByFiscalCode.get().getErpIntegrationConfiguration();
            if (configuration != null) {
                return password.compareToIgnoreCase(configuration.getPassword()) == 0;
            }
        }
        return false;
    }

    @WebMethod
    public String sendInfoOnline(DocumentsInformationInput documentsInformation) {
        validateRequestHeader(documentsInformation.getFinantialInstitution());

        FinantialInstitution finantialInstitution = validateFinantialInstitution(documentsInformation);
        //Integrate the information from XML SAFT
        DateTime now = new DateTime();
        String filename = finantialInstitution.getFiscalNumber() + "_" + now.toString() + ".xml";
        OperationFile file = OperationFile.create(filename, ArrayUtils.toPrimitive(documentsInformation.getData()));
        ERPImportOperation operation = ERPImportOperation.create(file, finantialInstitution, now, false, false, false, null);

        ERPImporter importer = new ERPImporter(file.getStream());
        importer.processAuditFile(operation);
        return operation.getExternalId();
    }

    private FinantialInstitution validateFinantialInstitution(DocumentsInformationInput documentsInformation) {
        FinantialInstitution finantialInstitution =
                FinantialInstitution.findUniqueByFiscalCode(documentsInformation.getFinantialInstitution()).orElse(null);

        if (finantialInstitution == null) {
            throw new TreasuryDomainException("error.integration.erp.invalid.fiscalinstitution");
        }
        return finantialInstitution;
    }

    @WebMethod
    public String sendInfoOffline(DocumentsInformationInput documentsInformation) {
        validateRequestHeader(documentsInformation.getFinantialInstitution());

        FinantialInstitution finantialInstitution = validateFinantialInstitution(documentsInformation);
        //Integrate the information from XML SAFT
        DateTime now = new DateTime();
        String filename = finantialInstitution.getFiscalNumber() + "_" + now.toString() + ".xml";
        ERPImportOperation operation = ERPImportOperation.create(null, finantialInstitution, now, false, false, false, null);
        try {
            File externalFile = new File(documentsInformation.getDataURI());
            byte[] bytes = Files.toByteArray(externalFile);
            OperationFile operationFile = OperationFile.create(filename, bytes);
            operation.setFile(operationFile);
            ERPImporter importer = new ERPImporter(operationFile.getStream());
            importer.processAuditFile(operation);
        } catch (Exception e) {
            operation.setErrorLog(e.getLocalizedMessage());
        }
        return operation.getExternalId();
    }

    @WebMethod
    public IntegrationStatusOutput getIntegrationStatusFor(String requestIdentification) {

        ERPImportOperation importOperation = FenixFramework.getDomainObject(requestIdentification);
        validateRequestHeader(importOperation.getFinantialInstitution().getFiscalNumber());
        IntegrationStatusOutput status = new IntegrationStatusOutput(requestIdentification);
        ERPImporter importer = new ERPImporter(importOperation.getFile().getStream());
        Set<String> documentNumbers = importer.getRelatedDocumentsNumber();
        for (String docNumber : documentNumbers) {
            DocumentStatusWS docStatus = new DocumentStatusWS();
            FinantialDocument document =
                    FinantialDocument.findByUiDocumentNumber(importOperation.getFinantialInstitution(), docNumber);
            if (document == null) {
                docStatus.setIntegrationStatus(StatusType.ERROR);
            } else {
                docStatus.setIntegrationStatus(StatusType.SUCCESS);
            }
            status.getDocumentStatus().add(docStatus);
        }
        return status;
    }

    @WebMethod
    public InterestRequestValueOuptut getInterestValueFor(InterestRequestValueInput interestRequest) {
        validateRequestHeader(interestRequest.getFinantialInstitutionFiscalNumber());
        //1. Check if the the lineNumber+DebitNoteNumber is for the Customer of the FinantialInstitution

        //2. Check if the lineNumber+DebitNoteNumber Amount is correct

        //3 . calculate the amount of interest

        InterestRequestValueOuptut bean = new InterestRequestValueOuptut();

        if (interestRequest.getGenerateInterestDebitNote()) {
            //Create DebitNote for the InterestRate
        }
        return bean;
    }

    private void validateRequestHeader(String finantialInstitution) {
        if (finantialInstitution == null || getSecurityHeader() == null
                || !finantialInstitution.equalsIgnoreCase(getSecurityHeader().getUsername())) {
            throw new SecurityException("invalid request permission");
        }
    }
}
