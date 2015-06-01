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
package org.fenixedu.treasury.services.payments.paymentscodegenerator;

import java.util.Set;
import java.util.stream.Collectors;

import org.fenixedu.treasury.domain.Customer;
import org.fenixedu.treasury.domain.FinantialInstitution;
import org.fenixedu.treasury.domain.paymentcodes.PaymentReferenceCode;
import org.fenixedu.treasury.domain.paymentcodes.PaymentReferenceCodeType;

public class SequentialPaymentCodeGenerator extends PaymentCodeGenerator {

    private static final String CODE_FILLER = "0";
    private static final int NUM_CONTROL_DIGITS = 2;
    private static final int NUM_SEQUENTIAL_NUMBERS = 7;

    private final FinantialInstitution finantialInstitution;

    public SequentialPaymentCodeGenerator(FinantialInstitution finantialInstitution) {
        super();
        this.finantialInstitution = finantialInstitution;
    }

    @Override
    public boolean canGenerateNewCode(PaymentReferenceCodeType paymentCodeType, Customer customer) {
        final PaymentReferenceCode lastPaymentCode = findLastPaymentReferenceCode(finantialInstitution);
        return lastPaymentCode == null ? true : Integer.valueOf(getSequentialNumber(lastPaymentCode)) < 9999999;
    }

    @Override
    public PaymentReferenceCode generateNewCodeFor(PaymentReferenceCodeType codeType, Customer customer) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public boolean isCodeMadeByThisFactory(PaymentReferenceCode paymentCode) {
        return true;
    }

    protected PaymentReferenceCode findLastPaymentReferenceCode(FinantialInstitution finantialInstitution) {
        final Set<PaymentReferenceCode> paymentCodes = allPaymentCodes(finantialInstitution);
        return paymentCodes.stream().sorted((x, y) -> y.getReferenceCode().compareTo(x.getReferenceCode())).findFirst()
                .orElse(null);
    }

    protected Set<PaymentReferenceCode> allPaymentCodes(FinantialInstitution finantialInstitution) {
        return PaymentReferenceCode.findAll()
                .filter(x -> x.getPaymentCodePool().getFinantialInstitution().equals(finantialInstitution))
                .collect(Collectors.toSet());
    }

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

//
    private static String getSequentialNumber(PaymentReferenceCode paymentCode) {
        return paymentCode.getReferenceCode().substring(0, paymentCode.getReferenceCode().length() - NUM_CONTROL_DIGITS);
    }

    @Override
    public void refreshReferenceCodeGenerator() {
        // TODO Auto-generated method stub

    }

}