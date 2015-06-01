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
//import org.apache.commons.lang.StringUtils;
//import org.fenixedu.treasury.domain.Customer;
//import org.fenixedu.treasury.domain.FinantialInstitution;
//import org.fenixedu.treasury.domain.paymentcodes.PaymentReferenceCode;
//import org.fenixedu.treasury.domain.paymentcodes.PaymentReferenceCodeStateType;
//import org.fenixedu.treasury.domain.paymentcodes.PaymentReferenceCodeType;
//import org.joda.time.DateTime;
//
///**
// * Code Format: <numericPartOfIstId{6}><typeDigit{2}><checkDigit{1}>
// */
//public class CustomerPaymentCodeGenerator extends PaymentCodeGenerator {
//    private static final String CODE_FILLER = "0";
//
//    private static final int PERSON_CODE_LENGTH = 6;
//
//    private static final int TYPE_CODE_LENGTH = 2;
//
//    private static final int CODE_LENGTH = 9;
//
//    private final FinantialInstitution finantialInstitution;
//
//    public CustomerPaymentCodeGenerator(FinantialInstitution finantialInstitution) {
//        super();
//        this.finantialInstitution = finantialInstitution;
//    }
//
//    @Override
//    public boolean canGenerateNewCode(PaymentReferenceCodeType paymentCodeType, final Customer customer) {
//        for (PaymentReferenceCode code : customer.getPaymentCodesBy(paymentCodeType, finantialInstitution)) {
//            if (isCodeMadeByThisFactory(code)) {
//                return false;
//            }
//        }
//        return true;
//    }
//
//    @Override
//    public PaymentReferenceCode generateNewCodeFor(final PaymentReferenceCodeType paymentCodeType, final Customer person) {
//
//        String baseCode =
//                getCustomerCodeDigits(person)
//                        + StringUtils.leftPad(Integer.toString(paymentCodeType.getTypeDigit()), TYPE_CODE_LENGTH, CODE_FILLER);
//        baseCode = baseCode + Verhoeff.generateVerhoeff(baseCode);
//        if (baseCode.length() != CODE_LENGTH) {
//            throw new RuntimeException("Unexpected code length for generated code");
//        }
//
//        PaymentReferenceCode referenceCode =
//                PaymentReferenceCode.create(baseCode, new DateTime().toLocalDate(), new DateTime().plusDays(30).toLocalDate(),
//                        PaymentReferenceCodeStateType.USED);
//        return referenceCode;
//    }
//
//    private String getCustomerCodeDigits(Customer customer) {
//        if (customer.getPaymentReferenceBaseCode().length() > Customer.MAX_CODE_LENGHT) {
//            throw new RuntimeException("SIBS Payment Code: " + customer.getPaymentReferenceBaseCode()
//                    + " exceeded maximun size accepted");
//        }
//        return StringUtils.leftPad(customer.getPaymentReferenceBaseCode(), PERSON_CODE_LENGTH, CODE_FILLER);
//    }
//
//    @Override
//    public boolean isCodeMadeByThisFactory(PaymentReferenceCode paymentCode) {
//        Customer customer = null;
//        if (paymentCode.getFinantialDocument() != null) {
//            customer = paymentCode.getFinantialDocument().getDebtAccount().getCustomer();
//        } else if (paymentCode.getInvoiceEntriesSet().size() > 0) {
//            customer = paymentCode.getInvoiceEntriesSet().stream().findFirst().get().getDebtAccount().getCustomer();
//        }
//        return paymentCode.getReferenceCode().startsWith(getCustomerCodeDigits(customer));
//    }
//
//    @Override
//    public void refreshReferenceCodeGenerator() {
//        // TODO Auto-generated method stub
//
//    }
//}
