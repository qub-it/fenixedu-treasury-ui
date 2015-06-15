/**
 * This file was created by Quorum Born IT <http://www.qub-it.com/> and its 
 * copyright terms are bind to the legal agreement regulating the FenixEdu@ULisboa 
 * software development project between Quorum Born IT and Serviços Partilhados da
 * Universidade de Lisboa:
 *  - Copyright © 2015 Quorum Born IT (until any Go-Live phase)
 *  - Copyright © 2015 Universidade de Lisboa (after any Go-Live phase)
 *
 * Contributors: ricardo.pedro@qub-it.com, anil.mamede@qub-it.com
 *
 * 
 * This file is part of FenixEdu Treasury.
 *
 * FenixEdu Treasury is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * FenixEdu Treasury is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with FenixEdu Treasury.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.fenixedu.treasury.services.integration.erp;

import java.util.Optional;

import javax.jws.WebMethod;
import javax.jws.WebService;

import org.fenixedu.treasury.domain.FinantialInstitution;
import org.fenixedu.treasury.domain.integration.ERPConfiguration;
import org.fenixedu.treasury.services.integration.erp.dto.DocumentsInformationInput;
import org.fenixedu.treasury.services.integration.erp.dto.IntegrationStatusOutput;
import org.fenixedu.treasury.services.integration.erp.dto.InterestRequestValueInput;
import org.fenixedu.treasury.services.integration.erp.dto.InterestRequestValueOuptut;

import com.qubit.solution.fenixedu.bennu.webservices.services.server.BennuWebService;

@WebService
public class ERPIntegrationService extends BennuWebService {

    public static boolean validate(String username, String password) {
        Optional<FinantialInstitution> findUniqueByFiscalCode = FinantialInstitution.findUniqueByFiscalCode(username);
        if (findUniqueByFiscalCode.isPresent()) {

            ERPConfiguration configuration = findUniqueByFiscalCode.get().getErpIntegrationConfiguration();
            if (configuration != null) {
                return password.compareToIgnoreCase(configuration.getPassword()) == 0;
            }
        }
        return false;
    }

    @WebMethod
    public String sendInfoOnline(DocumentsInformationInput documentsInformation) {
        validateRequestHeader(documentsInformation.getFinantialInstitution());
        String requestId = "Random_requestId";
        //Integrate the information from XML SAFT

        return requestId;
    }

    @WebMethod
    public String sendInfoOffline(DocumentsInformationInput documentsInformation) {
        validateRequestHeader(documentsInformation.getFinantialInstitution());
        String requestId = "Random_requestId";
        //Integrate the information from XML SAFT

        return requestId;
    }

    @WebMethod
    public IntegrationStatusOutput[] getIntegrationStatusFor(String requestIdentification) {

//        validateRequestHeader();
        IntegrationStatusOutput[] status = null;
        return status;
    }

    @WebMethod
    public InterestRequestValueOuptut getInterestValueFor(InterestRequestValueInput interestRequest) {
        validateRequestHeader(interestRequest.getFinantialInstitutionFiscalNumber());
        //1. Check if the the lineNumber+DebitNoteNumber is for the Customer of the FinantialInstitution

        //2. Check if the lineNumber+DebitNoteNumber Amount is correct

        //3 . calculate the amount of interest

        InterestRequestValueOuptut bean = new InterestRequestValueOuptut();

        if (interestRequest.getGenerateInterestDebitNote()) {
            //Create DebitNote for the InterestRate
        }
        return bean;
    }

    private void validateRequestHeader(String finantialInstitution) {
        if (finantialInstitution == null || getSecurityHeader() == null
                || !finantialInstitution.equalsIgnoreCase(getSecurityHeader().getUsername())) {
            throw new SecurityException("invalid request permission");
        }
    }
}
