package org.fenixedu.treasury.services.integration;

import javax.jws.WebMethod;
import javax.jws.WebService;

import org.fenixedu.treasury.services.integration.dto.DocumentsInformationInput;
import org.fenixedu.treasury.services.integration.dto.IntegrationStatusOutput;
import org.fenixedu.treasury.services.integration.dto.InterestRequestValueInput;
import org.fenixedu.treasury.services.integration.dto.InterestRequestValueOuptut;

import com.qubit.solution.fenixedu.bennu.webservices.services.server.BennuWebService;

@WebService
public class ERPIntegrationService extends BennuWebService {

    @WebMethod
    public String sendInfoOnline(DocumentsInformationInput documentsInformation) {

        String requestId = "Random_requestId";
        //Integrate the information from XML SAFT

        return requestId;
    }

    @WebMethod
    public String sendInfoOffline(DocumentsInformationInput documentsInformation) {

        String requestId = "Random_requestId";
        //Integrate the information from XML SAFT

        return requestId;
    }

    @WebMethod
    public IntegrationStatusOutput[] getIntegrationStatusFor(String requestIdentification) {
        IntegrationStatusOutput[] status = null;
        return status;
    }

    @WebMethod
    public InterestRequestValueOuptut getInterestValueFor(InterestRequestValueInput interestRequest) {

        //1. Check if the the lineNumber+DebitNoteNumber is for the Customer of the FinantialInstitution

        //2. Check if the lineNumber+DebitNoteNumber Amount is correct

        //3 . calculate the amount of interest

        InterestRequestValueOuptut bean = new InterestRequestValueOuptut();

        if (interestRequest.getGenerateInterestDebitNote()) {
            //Create DebitNote for the InterestRate
        }
        return bean;
    }
}
