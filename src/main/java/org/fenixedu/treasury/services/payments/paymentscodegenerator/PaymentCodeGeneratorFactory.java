///**
// * Copyright © 2002 Instituto Superior Técnico
// *
// * This file is part of FenixEdu Academic.
// *
// * FenixEdu Academic is free software: you can redistribute it and/or modify
// * it under the terms of the GNU Lesser General Public License as published by
// * the Free Software Foundation, either version 3 of the License, or
// * (at your option) any later version.
// *
// * FenixEdu Academic is distributed in the hope that it will be useful,
// * but WITHOUT ANY WARRANTY; without even the implied warranty of
// * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
// * GNU Lesser General Public License for more details.
// *
// * You should have received a copy of the GNU Lesser General Public License
// * along with FenixEdu Academic.  If not, see <http://www.gnu.org/licenses/>.
// */
//package org.fenixedu.treasury.services.payments.paymentscodegenerator;
//
//import java.util.ArrayList;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//
//import org.fenixedu.treasury.domain.FinantialInstitution;
//
//public class PaymentCodeGeneratorFactory {
//
//    private static Map<FinantialInstitution, List<PaymentCodeGenerator>> generators =
//            new HashMap<FinantialInstitution, List<PaymentCodeGenerator>>();
//
//    public static PaymentCodeGenerator getGenerator(FinantialInstitution finantialInstitution) {
////            return getCustomerPaymentCodeGeneratorFor(finantialInstitution);
//        return getSequentialPaymentCodeGenerator(finantialInstitution);
//    }
//
//    private static PaymentCodeGenerator getSequentialPaymentCodeGenerator(FinantialInstitution finantialInstitution) {
//        if (generators.containsKey(finantialInstitution)) {
//            for (PaymentCodeGenerator generator : generators.get(finantialInstitution)) {
//                if (generator instanceof SequentialPaymentWithCheckDigitCodeGenerator) {
//                    return generator;
//                }
//            }
//
//            //try to create/load a new one
//            SequentialPaymentWithCheckDigitCodeGenerator sequentialGenerator =
//                    new SequentialPaymentWithCheckDigitCodeGenerator(finantialInstitution);
//            generators.get(finantialInstitution).add(sequentialGenerator);
//            return sequentialGenerator;
//        } else {
//            //try to create/load a new one
//            SequentialPaymentWithCheckDigitCodeGenerator sequentialGenerator =
//                    new SequentialPaymentWithCheckDigitCodeGenerator(finantialInstitution);
//            List<PaymentCodeGenerator> generatorsList = new ArrayList<PaymentCodeGenerator>();
//            generatorsList.add(sequentialGenerator);
//            generators.put(finantialInstitution, generatorsList);
//            return sequentialGenerator;
//        }
//    }
//
////    private static PaymentCodeGenerator getCustomerPaymentCodeGeneratorFor(FinantialInstitution finantialInstitution) {
////        if (generators.containsKey(finantialInstitution)) {
////            for (PaymentCodeGenerator generator : generators.get(finantialInstitution)) {
////                if (generator instanceof CustomerPaymentCodeGenerator) {
////                    return generator;
////                }
////            }
////
////            //try to create/load a new one
////            CustomerPaymentCodeGenerator customerGenerator = new CustomerPaymentCodeGenerator(finantialInstitution);
////            generators.get(finantialInstitution).add(customerGenerator);
////            return customerGenerator;
////        } else {
////            //try to create/load a new one
////            CustomerPaymentCodeGenerator customerGenerator = new CustomerPaymentCodeGenerator(finantialInstitution);
////            List<PaymentCodeGenerator> generatorsList = new ArrayList<PaymentCodeGenerator>();
////            generatorsList.add(customerGenerator);
////            generators.put(finantialInstitution, generatorsList);
////            return customerGenerator;
////        }
////    }
//}
