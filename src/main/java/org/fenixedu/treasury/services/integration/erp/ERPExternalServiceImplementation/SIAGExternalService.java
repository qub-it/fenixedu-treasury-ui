package org.fenixedu.treasury.services.integration.erp.ERPExternalServiceImplementation;

import java.util.List;

import org.fenixedu.treasury.domain.integration.ERPConfiguration;
import org.fenixedu.treasury.services.integration.erp.IERPExternalService;
import org.fenixedu.treasury.services.integration.erp.dto.DocumentsInformationInput;
import org.fenixedu.treasury.services.integration.erp.dto.IntegrationStatusOutput;

public class SIAGExternalService implements IERPExternalService {

//    ERPExternalService _internalService;

    public SIAGExternalService(ERPConfiguration erpIntegrationConfiguration) {
//        try {
//            _internalService =
//                    new ERPExternalServiceService(new URL(erpIntegrationConfiguration.getExternalURL()))
//                            .getERPExternalServicePort();
//        } catch (MalformedURLException e) {
//            throw new TreasuryDomainException("error.SIAGExternalService.error.creating.stub");
//        }
    }

    @Override
    public String sendInfoOnline(DocumentsInformationInput documentsInformation) {
//        return _internalService.sendInfoOnline(documentsInformation);
        return null;
    }

    @Override
    public String sendInfoOffline(DocumentsInformationInput documentsInformation) {
//        return _internalService.sendInfoOffline(documentsInformation);
        return null;
    }

    @Override
    public List<IntegrationStatusOutput> getIntegrationStatusFor(String requestIdentification) {
//        return _internalService.getIntegrationStatusFor(requestIdentification);
        return null;
    }

}