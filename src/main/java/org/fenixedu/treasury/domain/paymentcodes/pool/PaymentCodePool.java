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
package org.fenixedu.treasury.domain.paymentcodes.pool;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Stream;

import org.fenixedu.bennu.core.domain.Bennu;
import org.fenixedu.treasury.domain.FinantialInstitution;
import org.fenixedu.treasury.domain.exceptions.TreasuryDomainException;
import org.fenixedu.treasury.services.payments.paymentscodegenerator.PaymentCodeGenerator;
import org.fenixedu.treasury.services.payments.paymentscodegenerator.SequentialPaymentWithCheckDigitCodeGenerator;

import pt.ist.fenixframework.Atomic;

public class PaymentCodePool extends PaymentCodePool_Base {

    protected PaymentCodePool() {
        super();
//		setBennu(Bennu.getInstance());
    }

    protected PaymentCodePool(final String name, final Long minPaymentCodes, final Long maxPaymentCodes,
            final BigDecimal minAmount, final BigDecimal maxAmount, final Boolean active,
            final FinantialInstitution finantialInstitution) {
        this();
        init(name, minPaymentCodes, maxPaymentCodes, minAmount, maxAmount, active, finantialInstitution);
    }

    protected void init(final String name, final Long minPaymentCodes, final Long maxPaymentCodes, final BigDecimal minAmount,
            final BigDecimal maxAmount, final Boolean active, final FinantialInstitution finantialInstitution) {
        setName(name);
        setMinReferenceCode(minPaymentCodes);
        setMaxReferenceCode(maxPaymentCodes);
        setMinAmount(minAmount);
        setMaxAmount(maxAmount);
        setActive(active);
        setFinantialInstitution(finantialInstitution);
        checkRules();
    }

    private void checkRules() {
        //
        // CHANGE_ME add more busines validations
        //

        // CHANGE_ME In order to validate UNIQUE restrictions
        // if (findByName(getName().count()>1)
        // {
        // throw new
        // TreasuryDomainException("error.PaymentCodePool.name.duplicated");
        // }
        // if (findByMinPaymentCodes(getMinPaymentCodes().count()>1)
        // {
        // throw new
        // TreasuryDomainException("error.PaymentCodePool.minPaymentCodes.duplicated");
        // }
        // if (findByMaxPaymentCodes(getMaxPaymentCodes().count()>1)
        // {
        // throw new
        // TreasuryDomainException("error.PaymentCodePool.maxPaymentCodes.duplicated");
        // }
        // if (findByMinAmount(getMinAmount().count()>1)
        // {
        // throw new
        // TreasuryDomainException("error.PaymentCodePool.minAmount.duplicated");
        // }
        // if (findByMaxAmount(getMaxAmount().count()>1)
        // {
        // throw new
        // TreasuryDomainException("error.PaymentCodePool.maxAmount.duplicated");
        // }
        // if (findByActive(getActive().count()>1)
        // {
        // throw new
        // TreasuryDomainException("error.PaymentCodePool.active.duplicated");
        // }
    }

    @Atomic
    public void edit(final java.lang.String name, final java.lang.Integer minPaymentCodes,
            final java.lang.Integer maxPaymentCodes, final java.math.BigDecimal minAmount, final java.math.BigDecimal maxAmount,
            final java.lang.Boolean active) {
        setName(name);
        setMinReferenceCode(minPaymentCodes);
        setMaxReferenceCode(maxPaymentCodes);
        setMinAmount(minAmount);
        setMaxAmount(maxAmount);
        setActive(active);
        checkRules();
    }

    public boolean isDeletable() {
        return true;
    }

    @Atomic
    public void delete() {
        if (!isDeletable()) {
            throw new TreasuryDomainException("error.PaymentCodePool.cannot.delete");
        }

//		setBennu(null);

        deleteDomainObject();
    }

    @Atomic
    public static PaymentCodePool create(final String name, final Integer minPaymentCodes, final Integer maxPaymentCodes,
            final BigDecimal minAmount, final BigDecimal maxAmount, final Boolean active,
            final FinantialInstitution finantialInstitution) {
        return new PaymentCodePool(name, minPaymentCodes, maxPaymentCodes, minAmount, maxAmount, active, finantialInstitution);

    }

    // @formatter: off
    /************
     * SERVICES *
     ************/
    // @formatter: on

    public static Stream<PaymentCodePool> findAll() {
        Set<PaymentCodePool> codes = new HashSet<PaymentCodePool>();

        return Bennu.getInstance().getFinantialInstitutionsSet().stream().map(x -> x.getPaymentCodePoolsSet())
                .reduce(codes, (a, b) -> {
                    a.addAll(b);
                    return a;
                }).stream();
    }

    public static Stream<PaymentCodePool> findByName(final java.lang.String name, final FinantialInstitution finantialInstitution) {
        return findByFinantialInstitution(finantialInstitution).filter(i -> name.equalsIgnoreCase(i.getName()));
    }

    public static Stream<PaymentCodePool> findByMinPaymentCodes(final java.lang.Integer minPaymentCodes,
            final FinantialInstitution finantialInstitution) {
        return findByFinantialInstitution(finantialInstitution).filter(i -> minPaymentCodes.equals(i.getMinReferenceCode()));
    }

    public static Stream<PaymentCodePool> findByMaxPaymentCodes(final java.lang.Integer maxPaymentCodes,
            final FinantialInstitution finantialInstitution) {
        return findByFinantialInstitution(finantialInstitution).filter(i -> maxPaymentCodes.equals(i.getMaxReferenceCode()));
    }

    public static Stream<PaymentCodePool> findByMinAmount(final java.math.BigDecimal minAmount,
            final FinantialInstitution finantialInstitution) {
        return findByFinantialInstitution(finantialInstitution).filter(i -> minAmount.equals(i.getMinAmount()));
    }

    public static Stream<PaymentCodePool> findByMaxAmount(final java.math.BigDecimal maxAmount,
            final FinantialInstitution finantialInstitution) {
        return findAll().filter(i -> maxAmount.equals(i.getMaxAmount()));
    }

    public static Stream<PaymentCodePool> findByActive(final java.lang.Boolean active,
            final FinantialInstitution finantialInstitution) {
        return findAll().filter(i -> active.equals(i.getActive()));
    }

    public static Stream<PaymentCodePool> findByFinantialInstitution(final FinantialInstitution finantialInstitution) {
        return findAll().filter(i -> i.getFinantialInstitution().equals(finantialInstitution));
    }

    private static PaymentCodeGenerator _referenceCodeGenerator;

    protected PaymentCodeGenerator getReferenceCodeGenerator() {

        if (_referenceCodeGenerator == null) {
            if (Boolean.TRUE.equals(this.getUseCheckDigit())) {
                _referenceCodeGenerator = new SequentialPaymentWithCheckDigitCodeGenerator(this);
            } else {
                //Create a Sequencial CustomerFileCodeGenerator
            }
        }
        return _referenceCodeGenerator;
    }

}
