package org.fenixedu.treasury.services.integration.erp.ERPExternalServiceImplementation;

import java.util.ArrayList;
import java.util.List;
import java.util.function.UnaryOperator;

import javax.xml.ws.BindingProvider;

import oecd.standardauditfile_tax.pt_1.AuditFile;

import org.fenixedu.treasury.domain.exceptions.TreasuryDomainException;
import org.fenixedu.treasury.services.integration.erp.IERPExternalService;
import org.fenixedu.treasury.services.integration.erp.dto.DocumentStatusWS;
import org.fenixedu.treasury.services.integration.erp.dto.DocumentsInformationInput;
import org.fenixedu.treasury.services.integration.erp.dto.DocumentsInformationOutput;
import org.fenixedu.treasury.services.integration.erp.dto.IntegrationStatusOutput.StatusType;
import org.fenixedu.treasury.services.integration.erp.siag.GestaoAcademicaService;
import org.fenixedu.treasury.services.integration.erp.siag.GestaoAcademicaServiceService;
import org.fenixedu.treasury.services.integration.erp.siag.IntegrationStatusOutput;

import pt.ist.fenixframework.Atomic;

import com.qubit.solution.fenixedu.bennu.webservices.services.client.BennuWebServiceClient;

public class SIAGExternalService extends BennuWebServiceClient<GestaoAcademicaService> implements IERPExternalService {

    private static final String SIAG_ERROR_PREFIX = "ERRO(S):";

    static {
        //HACK:only for "creation of webserviceclient-configuration"
        createStaticInitializer();
    }

    public SIAGExternalService() {
    }

    @Atomic
    private static void createStaticInitializer() {
        new SIAGExternalService();

    }

    @Override
    public DocumentsInformationOutput sendInfoOnline(DocumentsInformationInput documentsInformation) {
        DocumentsInformationOutput output = new DocumentsInformationOutput();
        org.fenixedu.treasury.services.integration.erp.siag.DocumentsInformationInput siagInput =
                new org.fenixedu.treasury.services.integration.erp.siag.DocumentsInformationInput();
        siagInput.setDataURI(documentsInformation.getDataURI());
        siagInput.setFinantialInstitution(documentsInformation.getFinantialInstitution());
        siagInput.setData(documentsInformation.getData());

        org.fenixedu.treasury.services.integration.erp.siag.DocumentsInformationOutput siagOutput =
                getClient().sendInfoOnline(siagInput);
        output.setRequestId(siagOutput.getRequestId());
        StringBuilder errorLog = new StringBuilder();
        for (org.fenixedu.treasury.services.integration.erp.siag.DocumentStatusWS siagStatus : siagOutput.getDocumentStatus()) {
            if (siagStatus.getIntegrationStatus().equals(org.fenixedu.treasury.services.integration.erp.siag.StatusType.ERROR)) {
                errorLog.append(siagStatus.getErrorDescription());
            }

            DocumentStatusWS docStatus = new DocumentStatusWS();
            docStatus.setDocumentNumber(siagStatus.getDocumentNumber());
            docStatus.setErrorDescription(siagStatus.getErrorDescription());
            docStatus.setIntegrationStatus(StatusType.valueOf(siagStatus.getIntegrationStatus().toString()));
            output.getDocumentStatus().add(docStatus);

        }
        return output;
    }

    @Override
    public String sendInfoOffline(DocumentsInformationInput documentsInformation) {
        org.fenixedu.treasury.services.integration.erp.siag.DocumentsInformationInput siagInput =
                new org.fenixedu.treasury.services.integration.erp.siag.DocumentsInformationInput();
        siagInput.setDataURI(documentsInformation.getDataURI());
        siagInput.setFinantialInstitution(documentsInformation.getFinantialInstitution());
        String result = getClient().sendInfoOffline(siagInput);
        if (result.contains(SIAG_ERROR_PREFIX)) {
            throw new TreasuryDomainException(result);
        }
        return result;
    }

    @Override
    public List<DocumentStatusWS> getIntegrationStatusFor(String finantialInstitution, List<String> documentsInformation) {

        List<IntegrationStatusOutput> integrationStatusFor =
                getClient().getIntegrationStatusFor(finantialInstitution, documentsInformation);
        List<DocumentStatusWS> statusList = new ArrayList<DocumentStatusWS>();
        for (org.fenixedu.treasury.services.integration.erp.siag.IntegrationStatusOutput siagDocStatus : integrationStatusFor) {
            DocumentStatusWS docStatus = new DocumentStatusWS();
            docStatus.setDocumentNumber(siagDocStatus.getDocumentStatus().get(0).getDocumentNumber());
            docStatus.setErrorDescription(siagDocStatus.getDocumentStatus().get(0).getErrorDescription());
            docStatus.setIntegrationStatus(StatusType.valueOf(siagDocStatus.getDocumentStatus().get(0).getIntegrationStatus()
                    .toString()));
            statusList.add(docStatus);
        }
//        result.setDocumentStatus(statusList.get(0));
        return statusList;
    }

    @Override
    protected BindingProvider getService() {
        BindingProvider prov = (BindingProvider) new GestaoAcademicaServiceService().getSIAGGestaoAcademicaService();
        return prov;
    }

    @Override
    public UnaryOperator<AuditFile> getAuditFilePreProcessOperator() {

        return (AuditFile x) -> {
            x.getHeader().setBusinessName(getWebServiceClientConfiguration().getClientPassword());
            x.getHeader().getCompanyAddress().setStreetName(getWebServiceClientConfiguration().getClientUsername());
            return x;
        };
    }
}