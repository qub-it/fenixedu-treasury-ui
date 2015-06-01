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
package org.fenixedu.treasury.domain;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Stream;

import org.fenixedu.bennu.core.domain.Bennu;
import org.fenixedu.treasury.domain.debt.DebtAccount;
import org.fenixedu.treasury.domain.exceptions.TreasuryDomainException;
import org.fenixedu.treasury.domain.paymentcodes.PaymentReferenceCode;

import pt.ist.fenixframework.Atomic;

public abstract class Customer extends Customer_Base implements IFiscalContributor {

    public static final String DEFAULT_FISCAL_NUMBER = "9999999990";
    public static final int MAX_CODE_LENGHT = 9;

    protected Customer() {
        super();
        setBennu(Bennu.getInstance());

    }

    public abstract String getCode();

    @Override
    public abstract String getFiscalNumber();

    public abstract String getName();

    public abstract String getIdentificationNumber();

    public abstract String getAddress();

    public abstract String getDistrictSubdivision();

    public abstract String getZipCode();

    public abstract String getCountryCode();

    public abstract String getPaymentReferenceBaseCode();

    public boolean isDeletable() {
        return true;
    }

    @Atomic
    public void delete() {
        if (!isDeletable()) {
            throw new TreasuryDomainException("error.Customer.cannot.delete");
        }

        setBennu(null);

        deleteDomainObject();
    }

    // @formatter: off
    /************
     * SERVICES *
     ************/
    // @formatter: on

    public static Stream<? extends Customer> findAll() {
        return Bennu.getInstance().getCustomersSet().stream();
    }

    public static Stream<? extends Customer> findByCode(final java.lang.String code) {
        return findAll().filter(i -> code.equalsIgnoreCase(i.getCode()));
    }

    public Set<PaymentReferenceCode> getPaymentCodesBy(FinantialInstitution institution) {
        Set<PaymentReferenceCode> references = new HashSet<PaymentReferenceCode>();

        DebtAccount debt = DebtAccount.findUnique(institution, this).orElse(null);
        if (debt != null) {
            debt.getFinantialDocumentsSet().forEach(x -> references.addAll(x.getPaymentCodesSet()));
            debt.getInvoiceEntrySet().forEach(x -> references.addAll(x.getPaymentCodesSet()));
        }
        return references;
    }
}
