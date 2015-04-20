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
