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

import java.math.BigDecimal;
import java.util.Set;

import org.fenixedu.treasury.domain.exceptions.TreasuryDomainException;
import org.fenixedu.treasury.domain.paymentcodes.PaymentReferenceCode;
import org.fenixedu.treasury.domain.paymentcodes.PaymentReferenceCodeStateType;
import org.fenixedu.treasury.domain.paymentcodes.pool.PaymentCodePool;
import org.joda.time.LocalDate;

import pt.ist.fenixframework.Atomic;

public class SequentialPaymentWithCheckDigitCodeGenerator extends PaymentCodeGenerator {

    private static final int NUM_CONTROL_DIGITS = 2;

    private final PaymentCodePool referenceCodePool;

    @Override
    public PaymentCodePool getReferenceCodePool() {
        return referenceCodePool;
    }

    public SequentialPaymentWithCheckDigitCodeGenerator(PaymentCodePool pool) {
        super();
        this.referenceCodePool = pool;
    }

    @Override
    @Atomic
    public PaymentReferenceCode generateNewCodeFor(final BigDecimal amount, LocalDate validFrom, LocalDate validTo,
            boolean useFixedAmount) {
        return generateNewCodeFor(amount, validFrom, validTo, useFixedAmount, false);
    }

    @Override
    @Atomic
    public PaymentReferenceCode generateNewCodeFor(final BigDecimal amount, LocalDate validFrom, LocalDate validTo,
            boolean useFixedAmount, final boolean forceGeneration) {

        if (validFrom == null) {
            validFrom = referenceCodePool.getValidFrom();
        }
        if (validTo == null) {
            validTo = referenceCodePool.getValidTo();
        }

        Long nextReferenceCode = referenceCodePool.getAndIncrementNextReferenceCode();
        if (nextReferenceCode > referenceCodePool.getMaxReferenceCode()) {
            //The pool is "OVER"... Try to get the first unused code

            PaymentReferenceCode availableReferenceCode = this.referenceCodePool.getPaymentReferenceCodesSet().stream()
                    .filter(x -> x.isAvailableForReuse()).findFirst().orElse(null);
            if (availableReferenceCode != null) {
                nextReferenceCode = Long.parseLong(availableReferenceCode.getReferenceCodeWithoutCheckDigits());
            } else {
                throw new TreasuryDomainException("error.PaymentCodeGenerator.not.paymentreferences.available.in.pool");
            }
        }

        String referenceCodeString = "";

//        if (Boolean.TRUE.equals(referenceCodePool.getUseAmountToValidateCheckDigit())) {
        referenceCodeString = CheckDigitGenerator.generateReferenceCodeWithCheckDigit(referenceCodePool.getEntityReferenceCode(),
                "" + nextReferenceCode, amount);
//        } else {
//            referenceCodeString =
//                    CheckDigitGenerator.generateReferenceCodeWithCheckDigit(referenceCodePool.getEntityReferenceCode(), ""
//                            + nextReferenceCode, BigDecimal.ZERO);
//        }

        BigDecimal minAmount = referenceCodePool.getMinAmount();
        BigDecimal maxAmount = referenceCodePool.getMaxAmount();
        if (useFixedAmount) {
            minAmount = amount;
            maxAmount = amount;
        }
        PaymentReferenceCode newPaymentReference = PaymentReferenceCode.create(referenceCodeString, validFrom, validTo,
                PaymentReferenceCodeStateType.UNUSED, referenceCodePool, minAmount, maxAmount);
        newPaymentReference.setPayableAmount(amount);

        return newPaymentReference;
    }

    @Override
    public boolean isCodeMadeByThisFactory(PaymentReferenceCode paymentCode) {
        return paymentCode.getPaymentCodePool().equals(this);
    }

    protected Set<PaymentReferenceCode> allPaymentCodes(PaymentCodePool referenceCodePool) {
        return referenceCodePool.getPaymentReferenceCodesSet();
    }

}
