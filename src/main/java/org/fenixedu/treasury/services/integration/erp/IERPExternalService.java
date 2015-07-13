package org.fenixedu.treasury.services.integration.erp;

import java.util.function.UnaryOperator;

import oecd.standardauditfile_tax.pt_1.AuditFile;

import org.fenixedu.treasury.services.integration.erp.dto.DocumentsInformationInput;
import org.fenixedu.treasury.services.integration.erp.dto.IntegrationStatusOutput;

public interface IERPExternalService {

    public String sendInfoOnline(DocumentsInformationInput documentsInformation);

    public String sendInfoOffline(DocumentsInformationInput documentsInformation);

    public IntegrationStatusOutput getIntegrationStatusFor(String finantialInstiution, String documentInformaton);

    public UnaryOperator<AuditFile> getAuditFilePreProcessOperator();

}