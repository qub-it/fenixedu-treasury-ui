package org.fenixedu.treasury.services.integration.erp.ERPExternalServiceImplementation;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javax.xml.ws.BindingProvider;

import org.fenixedu.treasury.domain.exceptions.TreasuryDomainException;
import org.fenixedu.treasury.domain.integration.ERPConfiguration;
import org.fenixedu.treasury.services.integration.erp.IERPExternalService;
import org.fenixedu.treasury.services.integration.erp.dto.DocumentStatusWS;
import org.fenixedu.treasury.services.integration.erp.dto.DocumentsInformationInput;
import org.fenixedu.treasury.services.integration.erp.dto.IntegrationStatusOutput;
import org.fenixedu.treasury.services.integration.erp.dto.IntegrationStatusOutput.StatusType;
import org.fenixedu.treasury.services.integration.erp.siag.ArrayOfIntegrationStatusOutput;
import org.fenixedu.treasury.services.integration.erp.siag.GestaoAcademicaService;
import org.fenixedu.treasury.services.integration.erp.siag.GestaoAcademicaServiceService;
import org.springframework.util.CollectionUtils;

import com.qubit.solution.fenixedu.bennu.webservices.services.client.BennuWebServiceClient;

public class SIAGExternalService extends BennuWebServiceClient<IERPExternalService> implements IERPExternalService {

    GestaoAcademicaService _internalService;
    ERPConfiguration _erpConfiguration;

    public SIAGExternalService(ERPConfiguration erpIntegrationConfiguration) {

        _erpConfiguration = erpIntegrationConfiguration;
    }

    @Override
    public String sendInfoOnline(DocumentsInformationInput documentsInformation) {
        org.fenixedu.treasury.services.integration.erp.siag.DocumentsInformationInput siagInput =
                new org.fenixedu.treasury.services.integration.erp.siag.DocumentsInformationInput();
        siagInput.setDataURI(documentsInformation.getDataURI());
        siagInput.setFinantialInstitution(documentsInformation.getFinantialInstitution());
        siagInput.getData().addAll(CollectionUtils.arrayToList(documentsInformation.getData()));
        return _internalService.sendInfoOnline(siagInput);
    }

    @Override
    public String sendInfoOffline(DocumentsInformationInput documentsInformation) {
        org.fenixedu.treasury.services.integration.erp.siag.DocumentsInformationInput siagInput =
                new org.fenixedu.treasury.services.integration.erp.siag.DocumentsInformationInput();
        siagInput.setDataURI(documentsInformation.getDataURI());
        siagInput.setFinantialInstitution(documentsInformation.getFinantialInstitution());
        return _internalService.sendInfoOffline(siagInput);
    }

    @Override
    public List<IntegrationStatusOutput> getIntegrationStatusFor(String requestIdentification) {
        ArrayOfIntegrationStatusOutput integrationStatusFor = _internalService.getIntegrationStatusFor(requestIdentification);

        List<IntegrationStatusOutput> result = new ArrayList<IntegrationStatusOutput>();
        for (org.fenixedu.treasury.services.integration.erp.siag.IntegrationStatusOutput siagStatus : integrationStatusFor
                .getItem()) {
            IntegrationStatusOutput item = new IntegrationStatusOutput(siagStatus.getRequestId());
            List<DocumentStatusWS> statusList = new ArrayList<DocumentStatusWS>();
            for (org.fenixedu.treasury.services.integration.erp.siag.DocumentStatusWS siagDocStatus : siagStatus
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
        try {
            _internalService =
                    new GestaoAcademicaServiceService(new URL(_erpConfiguration.getExternalURL()))
                            .getSIAGGestaoAcademicaService();
            return (BindingProvider) _internalService;
        } catch (MalformedURLException e) {
            throw new TreasuryDomainException("error.SIAGExternalService.error.creating.stub");
        }
    }
}