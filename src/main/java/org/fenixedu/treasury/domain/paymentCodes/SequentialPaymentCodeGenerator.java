package org.fenixedu.treasury.domain.paymentCodes;

import java.util.Collections;
import java.util.Comparator;
import java.util.Random;
import java.util.Set;




import java.util.stream.Collectors;

import org.apache.commons.lang.StringUtils;
import org.fenixedu.bennu.core.domain.Bennu;
import org.fenixedu.bennu.core.domain.User;

import com.google.common.base.Predicate;
import com.google.common.collect.Sets;

public class SequentialPaymentCodeGenerator  {


    private static final String CODE_FILLER = "0";
    private static final int NUM_CONTROL_DIGITS = 2;
    private static final int NUM_SEQUENTIAL_NUMBERS = 7;

//    @Override
//    public boolean canGenerateNewCode( User person) {
//        final PaymentReferenceCode lastPaymentCode = findLastPaymentReferenceCode();
//        return lastPaymentCode == null ? true : Integer.valueOf(getSequentialNumber(lastPaymentCode)) < 9999999;
//    }
//
//    protected PaymentReferenceCode findLastPaymentCode() {
//        final Set<PaymentReferenceCode> paymentCodes = allPaymentCodes();
//        return paymentCodes.isEmpty() ? null : Collections.max(paymentCodes, COMPARATOR_BY_PAYMENT_SEQUENTIAL_DIGITS);
//    }
//
//    protected Set<PaymentReferenceCode> allPaymentCodes() {
//        return PaymentReferenceCode.findAll().filter(x-> !QubStringUtil.isEmpty(paymentCode.getCode())).collect(Collectors.toSet());
//    }
//
//    @Override
//    public String generateNewCodeFor(PaymentCodeType paymentCodeType, Person person) {
//
//        if (!canGenerateNewCode(paymentCodeType, person)) {
//            throw new RuntimeException("Cannot generate new payment codes");
//        }
//
//        final PaymentCode lastPaymentCode = findLastPaymentCode();
//        int nextSequentialNumber = lastPaymentCode != null ? Integer.valueOf(getSequentialNumber(lastPaymentCode)) + 1 : 0;
//
//        String sequentialNumberPadded =
//                StringUtils.leftPad(String.valueOf(nextSequentialNumber), NUM_SEQUENTIAL_NUMBERS, CODE_FILLER);
//        String controDigitsPadded =
//                StringUtils.leftPad(String.valueOf((new Random()).nextInt(99)), NUM_CONTROL_DIGITS, CODE_FILLER);
//
//        return sequentialNumberPadded + controDigitsPadded;
//    }
//
//    @Override
//    public boolean isCodeMadeByThisFactory(PaymentCode paymentCode) {
//        return true;
//    }
//
//    private static String getSequentialNumber(PaymentCode paymentCode) {
//        return paymentCode.getCode().substring(0, paymentCode.getCode().length() - NUM_CONTROL_DIGITS);
//    }

}