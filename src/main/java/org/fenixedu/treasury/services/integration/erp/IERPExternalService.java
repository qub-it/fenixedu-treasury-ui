package org.fenixedu.treasury.services.integration.erp;

import java.util.List;
import java.util.function.UnaryOperator;

import oecd.standardauditfile_tax.pt_1.AuditFile;

import org.fenixedu.treasury.services.integration.erp.dto.DocumentsInformationInput;
import org.fenixedu.treasury.services.integration.erp.dto.IntegrationStatusOutput;

public interface IERPExternalService {

    public String sendInfoOnline(DocumentsInformationInput documentsInformation);

    public String sendInfoOffline(DocumentsInformationInput documentsInformation);

    public List<IntegrationStatusOutput> getIntegrationStatusFor(String requestIdentification);

    public UnaryOperator<AuditFile> getAuditFilePreProcessOperator();

}