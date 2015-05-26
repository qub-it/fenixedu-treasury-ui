package org.fenixedu.treasury.services.integration;

import java.math.BigDecimal;

import javax.jws.WebMethod;
import javax.jws.WebService;

import com.qubit.solution.fenixedu.bennu.webservices.services.server.BennuWebService;

@WebService
public class ERPIntegrationService extends BennuWebService {

    @WebMethod
    public void sendInfo(String requestID, Byte[] data) {

        //Integrate the information from XML SAFT

    }

    public InterestRateBeanWS getInterest(final String finantialInstitutionFiscalNumber, final String customerCode,
            final String debitNoteNumber, final Integer lineNumber, final BigDecimal amount,
            final Boolean generateInterestDebitNote) {

        //1. Check if the the lineNumber+DebitNoteNumber is for the Customer of the FinantialInstitution

        //2. Check if the lineNumber+DebitNoteNumber Amount is correct

        //3 . calculate the amount of interest

        InterestRateBeanWS bean = new InterestRateBeanWS();

        if (generateInterestDebitNote) {
            //Create DebitNote for the InterestRate
        }
        return bean;
    }
}
