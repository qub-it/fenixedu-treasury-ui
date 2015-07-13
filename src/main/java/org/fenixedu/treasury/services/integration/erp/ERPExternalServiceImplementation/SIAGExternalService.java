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
import org.fenixedu.treasury.services.integration.erp.dto.IntegrationStatusOutput;
import org.fenixedu.treasury.services.integration.erp.dto.IntegrationStatusOutput.StatusType;
import org.fenixedu.treasury.services.integration.erp.siag.GestaoAcademicaService;
import org.fenixedu.treasury.services.integration.erp.siag.GestaoAcademicaServiceService;
import org.springframework.util.CollectionUtils;

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
    public String sendInfoOnline(DocumentsInformationInput documentsInformation) {
        org.fenixedu.treasury.services.integration.erp.siag.DocumentsInformationInput siagInput =
                new org.fenixedu.treasury.services.integration.erp.siag.DocumentsInformationInput();
        siagInput.setDataURI(documentsInformation.getDataURI());
        siagInput.setFinantialInstitution(documentsInformation.getFinantialInstitution());
        siagInput.getData().addAll(CollectionUtils.arrayToList(documentsInformation.getData()));
        String result = getClient().sendInfoOnline(siagInput);
        if (result.contains(SIAG_ERROR_PREFIX)) {
            throw new TreasuryDomainException(result);
        }
        return result;
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
    public IntegrationStatusOutput getIntegrationStatusFor(String finantialInstitution, String documentInformation) {
        List<org.fenixedu.treasury.services.integration.erp.siag.IntegrationStatusOutput> integrationStatusFor =
                getClient().getIntegrationStatusFor(documentInformation);
        IntegrationStatusOutput result = new IntegrationStatusOutput();
//        for (org.fenixedu.treasury.services.integration.erp.siag.IntegrationStatusOutput siagStatus : integrationStatusFor) {
        IntegrationStatusOutput item = new IntegrationStatusOutput();
        List<DocumentStatusWS> statusList = new ArrayList<DocumentStatusWS>();
        for (org.fenixedu.treasury.services.integration.erp.siag.DocumentStatusWS siagDocStatus : integrationStatusFor.get(0)
                .getDocumentStatus()) {
            DocumentStatusWS docStatus = new DocumentStatusWS();
            docStatus.setDocumentNumber(siagDocStatus.getDocumentNumber());
            docStatus.setErrorDescription(siagDocStatus.getErrorDescription());
            docStatus.setIntegrationStatus(StatusType.valueOf(siagDocStatus.getIntegrationStatus().toString()));
            statusList.add(docStatus);
        }
//            item.setDocumentStatus(statusList);
//            result.add(item);
//        }

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