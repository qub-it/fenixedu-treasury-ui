///**
// * This file was created by Quorum Born IT <http://www.qub-it.com/> and its 
// * copyright terms are bind to the legal agreement regulating the FenixEdu@ULisboa 
// * software development project between Quorum Born IT and Serviços Partilhados da
// * Universidade de Lisboa:
// *  - Copyright © 2015 Quorum Born IT (until any Go-Live phase)
// *  - Copyright © 2015 Universidade de Lisboa (after any Go-Live phase)
// *
// * Contributors: xpto@qub-it.com
// *
// * 
// * This file is part of FenixEdu Treasury.
// *
// * FenixEdu Treasury is free software: you can redistribute it and/or modify
// * it under the terms of the GNU Lesser General Public License as published by
// * the Free Software Foundation, either version 3 of the License, or
// * (at your option) any later version.
// *
// * FenixEdu Treasury is distributed in the hope that it will be useful,
// * but WITHOUT ANY WARRANTY; without even the implied warranty of
// * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
// * GNU Lesser General Public License for more details.
// *
// * You should have received a copy of the GNU Lesser General Public License
// * along with FenixEdu Treasury.  If not, see <http://www.gnu.org/licenses/>.
// */
//
//package org.fenixedu.treasury.domain.paymentCodes;
//
//import org.apache.commons.lang.StringUtils;
//import org.fenixedu.bennu.core.domain.User;
//
///**
// * Code Format: <numericPartOfIstId{6}><typeDigit{2}><checkDigit{1}>
// */
//public class PersonPaymentReferenceCodeGenerator  {
//    private static final String CODE_FILLER = "0";
//
//    private static final int PERSON_CODE_LENGTH = 6;
//
//    private static final int TYPE_CODE_LENGTH = 2;
//
//    private static final int CODE_LENGTH = 9;
//
//    public PersonPaymentReferenceCodeGenerator() {
//    }
//
//    public boolean canGenerateNewCode( final User person) {
//        
//        return true;
//    }
//
//    public String generateNewCodeFor( final User person) {
//        String baseCode =
//                getUserCodeDigits(person)
//                        + StringUtils.leftPad(Integer.toString(paymentCodeType.getTypeDigit()), TYPE_CODE_LENGTH, CODE_FILLER);
//        baseCode = baseCode + Verhoeff.generateVerhoeff(baseCode);
//        if (baseCode.length() != CODE_LENGTH) {
//            throw new RuntimeException("Unexpected code length for generated code");
//        }
//        return baseCode;
//    }
//
//    private String getUserCodeDigits(Userperson) {
//        if (person.getUsername().length() > 9) {
//            throw new RuntimeException("SIBS Payment Code: " + person.getUsername() + " exceeded maximun size accepted");
//        }
//        return StringUtils.leftPad(person.getUsername().replace("ist", ""), PERSON_CODE_LENGTH, CODE_FILLER);
//    }
//
//    @Override
//    public boolean isCodeMadeByThisFactory(PaymentCode paymentCode) {
//        return paymentCode.getCode().startsWith(getPersonCodeDigits(paymentCode.getPerson()));
//    }
//}
