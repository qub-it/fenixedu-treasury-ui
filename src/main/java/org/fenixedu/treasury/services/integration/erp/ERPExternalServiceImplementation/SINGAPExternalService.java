package org.fenixedu.treasury.services.integration.erp.ERPExternalServiceImplementation;

import java.util.ArrayList;
import java.util.List;
import java.util.function.UnaryOperator;

import javax.xml.ws.BindingProvider;

import oecd.standardauditfile_tax.pt_1.AuditFile;

import org.fenixedu.treasury.services.integration.erp.IERPExternalService;
import org.fenixedu.treasury.services.integration.erp.dto.DocumentStatusWS;
import org.fenixedu.treasury.services.integration.erp.dto.DocumentsInformationInput;
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
        new SIAGExternalService();

    }

    @Override
    public String sendInfoOnline(DocumentsInformationInput documentsInformation) {
        org.fenixedu.treasury.services.integration.erp.singap.DocumentsInformationInput siagInput =
                new org.fenixedu.treasury.services.integration.erp.singap.DocumentsInformationInput();
        siagInput.setDataURI(documentsInformation.getDataURI());
        siagInput.setFinantialInstitution(documentsInformation.getFinantialInstitution());
        siagInput.getData().addAll(CollectionUtils.arrayToList(documentsInformation.getData()));
        return getClient().sendInfoOnline(siagInput);
    }

    @Override
    public String sendInfoOffline(DocumentsInformationInput documentsInformation) {
        org.fenixedu.treasury.services.integration.erp.singap.DocumentsInformationInput siagInput =
                new org.fenixedu.treasury.services.integration.erp.singap.DocumentsInformationInput();
        siagInput.setDataURI(documentsInformation.getDataURI());
        siagInput.setFinantialInstitution(documentsInformation.getFinantialInstitution());
        return getClient().sendInfoOffline(siagInput);
    }

    @Override
    public List<IntegrationStatusOutput> getIntegrationStatusFor(String requestIdentification) {
        List<org.fenixedu.treasury.services.integration.erp.singap.IntegrationStatusOutput> integrationStatusFor =
                getClient().getIntegrationStatusFor(requestIdentification);
        List<IntegrationStatusOutput> result = new ArrayList<IntegrationStatusOutput>();
        for (org.fenixedu.treasury.services.integration.erp.singap.IntegrationStatusOutput siagStatus : integrationStatusFor) {
            IntegrationStatusOutput item = new IntegrationStatusOutput(siagStatus.getRequestId());
            List<DocumentStatusWS> statusList = new ArrayList<DocumentStatusWS>();
            for (org.fenixedu.treasury.services.integration.erp.singap.DocumentStatusWS siagDocStatus : siagStatus
                    .getDocumentStatus()) {
                DocumentStatusWS docStatus = new DocumentStatusWS();
                docStatus.setDocumentNumber(siagDocStatus.getDocumentNumber());
                docStatus.setErrorDescription(siagDocStatus.getErrorDescription());
                docStatus.setIntegrationStatus(StatusType.valueOf(siagDocStatus.getIntegrationStatus().toString()));
                statusList.add(docStatus);
            }
            item.setDocumentStatus(statusList);
            result.add(item);
        }

        return result;
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