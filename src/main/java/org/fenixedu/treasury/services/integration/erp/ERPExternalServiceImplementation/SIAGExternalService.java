package org.fenixedu.treasury.services.integration.erp.ERPExternalServiceImplementation;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

import org.fenixedu.treasury.domain.exceptions.TreasuryDomainException;
import org.fenixedu.treasury.domain.integration.ERPConfiguration;
import org.fenixedu.treasury.services.integration.DocumentsInformationInput;
import org.fenixedu.treasury.services.integration.ERPExternalService;
import org.fenixedu.treasury.services.integration.ERPExternalServiceService;
import org.fenixedu.treasury.services.integration.IntegrationStatusOutput;
import org.fenixedu.treasury.services.integration.erp.IERPExternalService;

public class SIAGExternalService implements IERPExternalService {

    ERPExternalService _internalService;

    public SIAGExternalService(ERPConfiguration erpIntegrationConfiguration) {
        try {
            _internalService =
                    new ERPExternalServiceService(new URL(erpIntegrationConfiguration.getExternalURL()))
                            .getERPExternalServicePort();
        } catch (MalformedURLException e) {
            throw new TreasuryDomainException("error.SIAGExternalService.error.creating.stub");
        }
    }

    @Override
    public String sendInfoOnline(DocumentsInformationInput documentsInformation) {
        return _internalService.sendInfoOnline(documentsInformation);
    }

    @Override
    public String sendInfoOffline(DocumentsInformationInput documentsInformation) {
        return _internalService.sendInfoOffline(documentsInformation);
    }

    @Override
    public List<IntegrationStatusOutput> getIntegrationStatusFor(String requestIdentification) {
        return _internalService.getIntegrationStatusFor(requestIdentification);
    }

}