package org.fenixedu.treasury.services.integration.erp.ERPExternalServiceImplementation;

import java.util.ArrayList;
import java.util.List;
import java.util.function.UnaryOperator;

import javax.xml.ws.BindingProvider;

import oecd.standardauditfile_tax.pt_1.AuditFile;

import org.fenixedu.treasury.services.integration.erp.IERPExternalService;
import org.fenixedu.treasury.services.integration.erp.dto.DocumentStatusWS;
import org.fenixedu.treasury.services.integration.erp.dto.DocumentsInformationInput;
import org.fenixedu.treasury.services.integration.erp.dto.DocumentsInformationOutput;
import org.fenixedu.treasury.services.integration.erp.dto.IntegrationStatusOutput;
import org.fenixedu.treasury.services.integration.erp.dto.IntegrationStatusOutput.StatusType;
import org.fenixedu.treasury.services.integration.erp.singap.GestaoAcademicaService;
import org.fenixedu.treasury.services.integration.erp.singap.GestaoAcademicaServiceService;
import org.springframework.util.CollectionUtils;

import pt.ist.fenixframework.Atomic;

import com.qubit.solution.fenixedu.bennu.webservices.services.client.BennuWebServiceClient;

public class SINGAPExternalService extends BennuWebServiceClient<GestaoAcademicaService> implements IERPExternalService {

    static {
        //HACK:only for "creation of webserviceclient-configuration"
        createStaticInitializer();
    }

    public SINGAPExternalService() {
    }

    @Atomic
    private static void createStaticInitializer() {
        new SINGAPExternalService();

    }

    @Override
    public DocumentsInformationOutput sendInfoOnline(DocumentsInformationInput documentsInformation) {
        org.fenixedu.treasury.services.integration.erp.singap.DocumentsInformationInput singapInput =
                new org.fenixedu.treasury.services.integration.erp.singap.DocumentsInformationInput();
        singapInput.setDataURI(documentsInformation.getDataURI());
        singapInput.setFinantialInstitution(documentsInformation.getFinantialInstitution());
        singapInput.getData().addAll(CollectionUtils.arrayToList(documentsInformation.getData()));
        DocumentsInformationOutput output = new DocumentsInformationOutput();
        output.setRequestId(getClient().sendInfoOnline(singapInput));
        return output;
    }

    @Override
    public String sendInfoOffline(DocumentsInformationInput documentsInformation) {
        org.fenixedu.treasury.services.integration.erp.singap.DocumentsInformationInput singaInput =
                new org.fenixedu.treasury.services.integration.erp.singap.DocumentsInformationInput();
        singaInput.setDataURI(documentsInformation.getDataURI());
        singaInput.setFinantialInstitution(documentsInformation.getFinantialInstitution());
        return getClient().sendInfoOffline(singaInput);
    }

    @Override
    public List<DocumentStatusWS> getIntegrationStatusFor(String finantialInstitution, List<String> documentsInformation) {
        List<org.fenixedu.treasury.services.integration.erp.singap.IntegrationStatusOutput> integrationStatusFor =
                getClient().getIntegrationStatusFor(documentsInformation.get(0));
        IntegrationStatusOutput item = new IntegrationStatusOutput();
        List<DocumentStatusWS> statusList = new ArrayList<DocumentStatusWS>();
        for (org.fenixedu.treasury.services.integration.erp.singap.DocumentStatusWS singaDocStatus : integrationStatusFor.get(0)
                .getDocumentStatus()) {
            DocumentStatusWS docStatus = new DocumentStatusWS();
            docStatus.setDocumentNumber(singaDocStatus.getDocumentNumber());
            docStatus.setErrorDescription(singaDocStatus.getErrorDescription());
            docStatus.setIntegrationStatus(StatusType.valueOf(singaDocStatus.getIntegrationStatus().toString()));
            statusList.add(docStatus);
        }

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