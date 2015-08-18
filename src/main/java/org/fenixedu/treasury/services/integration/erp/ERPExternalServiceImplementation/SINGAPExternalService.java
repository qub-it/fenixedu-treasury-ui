package org.fenixedu.treasury.services.integration.erp.ERPExternalServiceImplementation;

import java.util.ArrayList;
import java.util.List;
import java.util.function.UnaryOperator;

import javax.xml.ws.BindingProvider;

import oecd.standardauditfile_tax.pt_1.AuditFile;

import org.fenixedu.treasury.services.integration.erp.IERPExternalService;
import org.fenixedu.treasury.services.integration.erp.dto.DocumentStatusWS;
import org.fenixedu.treasury.services.integration.erp.dto.DocumentStatusWS.StatusType;
import org.fenixedu.treasury.services.integration.erp.dto.DocumentsInformationInput;
import org.fenixedu.treasury.services.integration.erp.dto.DocumentsInformationOutput;
import org.fenixedu.treasury.services.integration.erp.singap.ArrayOfResposta;
import org.fenixedu.treasury.services.integration.erp.singap.Resposta;
import org.fenixedu.treasury.services.integration.erp.singap.Service;
import org.fenixedu.treasury.services.integration.erp.singap.ServiceSoap;

import pt.ist.fenixframework.Atomic;

import com.qubit.solution.fenixedu.bennu.webservices.services.client.BennuWebServiceClient;

public class SINGAPExternalService extends BennuWebServiceClient<ServiceSoap> implements IERPExternalService {

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
        DocumentsInformationOutput output = new DocumentsInformationOutput();
        output.setDocumentStatus(new ArrayList<DocumentStatusWS>());
        ArrayOfResposta carregarSAFTON = getClient().carregarSAFTON(documentsInformation.getData());
        for (Resposta resposta : carregarSAFTON.getResposta()) {
            output.setRequestId(resposta.getChavePrimaria());
            DocumentStatusWS status = new DocumentStatusWS();
            status.setDocumentNumber(resposta.getChavePrimaria());
            status.setErrorDescription(resposta.getMensagem());
            status.setIntegrationStatus(covertToStatusType(resposta.getStatus()));
            output.getDocumentStatus().add(status);
        }
        return output;
    }

    @Override
    public String sendInfoOffline(DocumentsInformationInput documentsInformation) {
        Resposta carregarSAFT = getClient().carregarSAFTOFF(documentsInformation.getDataURI());
        return carregarSAFT.getChavePrimaria();
    }

    @Override
    public List<DocumentStatusWS> getIntegrationStatusFor(String finantialInstitution, List<String> documentsInformation) {
        List<DocumentStatusWS> statusList = new ArrayList<DocumentStatusWS>();

        for (String docId : documentsInformation) {
            Resposta verificaEstado = getClient().verificaEstado(docId);
            DocumentStatusWS docStatus = new DocumentStatusWS();
            docStatus.setDocumentNumber(docId);
            docStatus.setErrorDescription(verificaEstado.getMensagem());
            docStatus.setIntegrationStatus(covertToStatusType(verificaEstado.getStatus()));
            statusList.add(docStatus);
        }

        return statusList;
    }

    private StatusType covertToStatusType(String status) {
        if ("OK".equals(status)) {
            return StatusType.SUCCESS;
        } else {
            return StatusType.ERROR;
        }
    }

    @Override
    protected BindingProvider getService() {
        BindingProvider prov = (BindingProvider) new Service().getServiceSoap();
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