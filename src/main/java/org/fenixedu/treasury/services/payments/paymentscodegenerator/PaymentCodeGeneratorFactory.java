/**
 * Copyright © 2002 Instituto Superior Técnico
 *
 * This file is part of FenixEdu Academic.
 *
 * FenixEdu Academic is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * FenixEdu Academic is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with FenixEdu Academic.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.fenixedu.treasury.services.payments.paymentscodegenerator;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.fenixedu.treasury.domain.FinantialInstitution;
import org.fenixedu.treasury.domain.paymentcodes.PaymentReferenceCodeType;

public class PaymentCodeGeneratorFactory {

    private static Map<FinantialInstitution, List<PaymentCodeGenerator>> generators =
            new HashMap<FinantialInstitution, List<PaymentCodeGenerator>>();

    public static PaymentCodeGenerator getGenerator(PaymentReferenceCodeType type, FinantialInstitution finantialInstitution) {
        switch (type) {
        case TOTAL_GRATUITY:
        case GRATUITY_FIRST_INSTALLMENT:
        case GRATUITY_SECOND_INSTALLMENT:
        case ADMINISTRATIVE_OFFICE_FEE_AND_INSURANCE:
        case INSURANCE:
        case PRE_BOLONHA_MASTER_DEGREE_TOTAL_GRATUITY:
        case PRE_BOLONHA_MASTER_DEGREE_INSURANCE:
        case RESIDENCE_FEE:
//            return personRotationPaymentCodeGenerator;
        case INSTITUTION_ACCOUNT_CREDIT:
            return getCustomerPaymentCodeGeneratorFor(finantialInstitution);
//        case INTERNAL_DEGREE_CHANGE_INDIVIDUAL_CANDIDACY_PROCESS:
//        case EXTERNAL_DEGREE_CHANGE_INDIVIDUAL_CANDIDACY_PROCESS:
//        case INTERNAL_DEGREE_TRANSFER_INDIVIDUAL_CANDIDACY_PROCESS:
//        case EXTERNAL_DEGREE_TRANSFER_INDIVIDUAL_CANDIDACY_PROCESS:
//        case SECOND_CYCLE_INDIVIDUAL_CANDIDACY_PROCESS:
//        case INTERNAL_DEGREE_CANDIDACY_FOR_GRADUATED_PERSON_INDIVIDUAL_PROCESS:
//        case EXTERNAL_DEGREE_CANDIDACY_FOR_GRADUATED_PERSON_INDIVIDUAL_PROCESS:
//        case OVER_23_INDIVIDUAL_CANDIDACY_PROCESS:
//        case PHD_PROGRAM_CANDIDACY_PROCESS:
//            return individualCandidacyPaymentCodeGenerator;
//        case RECTORATE:
//            return rectoratePaymentCodeGenerator;
        default:
            return getSequentialPaymentCodeGenerator(finantialInstitution);
        }
    }

    private static PaymentCodeGenerator getSequentialPaymentCodeGenerator(FinantialInstitution finantialInstitution) {
        if (generators.containsKey(finantialInstitution)) {
            for (PaymentCodeGenerator generator : generators.get(finantialInstitution)) {
                if (generator instanceof CustomerPaymentCodeGenerator) {
                    return generator;
                }
            }

            //try to create/load a new one
            SequentialPaymentCodeGenerator sequentialGenerator = new SequentialPaymentCodeGenerator(finantialInstitution);
            generators.get(finantialInstitution).add(sequentialGenerator);
            return sequentialGenerator;
        } else {
            //try to create/load a new one
            SequentialPaymentCodeGenerator sequentialGenerator = new SequentialPaymentCodeGenerator(finantialInstitution);
            List<PaymentCodeGenerator> generatorsList = new ArrayList<PaymentCodeGenerator>();
            generatorsList.add(sequentialGenerator);
            generators.put(finantialInstitution, generatorsList);
            return sequentialGenerator;
        }
    }

    private static PaymentCodeGenerator getCustomerPaymentCodeGeneratorFor(FinantialInstitution finantialInstitution) {
        if (generators.containsKey(finantialInstitution)) {
            for (PaymentCodeGenerator generator : generators.get(finantialInstitution)) {
                if (generator instanceof CustomerPaymentCodeGenerator) {
                    return generator;
                }
            }

            //try to create/load a new one
            CustomerPaymentCodeGenerator customerGenerator = new CustomerPaymentCodeGenerator(finantialInstitution);
            generators.get(finantialInstitution).add(customerGenerator);
            return customerGenerator;
        } else {
            //try to create/load a new one
            CustomerPaymentCodeGenerator customerGenerator = new CustomerPaymentCodeGenerator(finantialInstitution);
            List<PaymentCodeGenerator> generatorsList = new ArrayList<PaymentCodeGenerator>();
            generatorsList.add(customerGenerator);
            generators.put(finantialInstitution, generatorsList);
            return customerGenerator;
        }
    }
}
