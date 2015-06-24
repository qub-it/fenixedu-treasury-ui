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

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.fenixedu.bennu.core.domain.Bennu;
import org.fenixedu.treasury.domain.debt.DebtAccount;
import org.fenixedu.treasury.domain.exceptions.TreasuryDomainException;

import pt.ist.fenixframework.Atomic;

public class AdhocCustomer extends AdhocCustomer_Base {

    protected AdhocCustomer() {
        super();
        setBennu(Bennu.getInstance());
    }

    @Override
    public boolean isAdhocCustomer() {
        return true;
    }

    protected AdhocCustomer(final String code, final String fiscalNumber, final String name, final String address,
            final String districtSubdivision, final String zipCode, final String countryCode, final String identificationNumber) {
        this();
        setCode(code);
        setFiscalNumber(fiscalNumber);
        setName(name);
        setAddress(address);
        setDistrictSubdivision(districtSubdivision);
        setZipCode(zipCode);
        setCountryCode(countryCode);
        setIdentificationNumber(identificationNumber);

        checkRules();
    }

    @Override
    protected void checkRules() {
        super.checkRules();

    }

    @Override
    public String getPaymentReferenceBaseCode() {
        return this.getCode();
    }

    @Atomic
    public void edit(final String code, final String fiscalNumber, final String name, final String address,
            final String districtSubdivision, final String zipCode, final String countryCode, final String identificationNumber) {
        setFiscalNumber(fiscalNumber);
        setName(name);
        setAddress(address);
        setDistrictSubdivision(districtSubdivision);
        setZipCode(zipCode);
        setCountryCode(countryCode);
        setIdentificationNumber(identificationNumber);

        checkRules();
    }

    @Override
    public boolean isDeletable() {
        return getDebtAccountsSet().isEmpty();
    }

    @Override
    @Atomic
    public void delete() {
        if (!isDeletable()) {
            throw new TreasuryDomainException("error.AdhocCustomer.cannot.delete");
        }

        setBennu(null);
        setCustomerType(null);

        deleteDomainObject();
    }

    @Atomic
    public static AdhocCustomer create(final String code, final String fiscalNumber, final String name, final String address,
            final String districtSubdivision, final String zipCode, final String countryCode, final String identificationNumber) {
        return new AdhocCustomer(code, fiscalNumber, name, address, districtSubdivision, zipCode, countryCode,
                identificationNumber);
    }

    public static Stream<AdhocCustomer> findAll() {
        return Bennu.getInstance().getCustomersSet().stream().filter(x -> x instanceof AdhocCustomer)
                .map(AdhocCustomer.class::cast);
    }

    public static Stream<AdhocCustomer> findByFiscalNumber(final java.lang.String fiscalNumber) {
        return findAll().filter(i -> fiscalNumber.equalsIgnoreCase(i.getFiscalNumber()));
    }

    public static Stream<AdhocCustomer> findByName(final java.lang.String name) {
        return findAll().filter(i -> name.equalsIgnoreCase(i.getName()));
    }

    public static Stream<AdhocCustomer> findByAddress(final java.lang.String address) {
        return findAll().filter(i -> address.equalsIgnoreCase(i.getAddress()));
    }

    public static Stream<AdhocCustomer> findByDistrictSubdivision(final java.lang.String districtSubdivision) {
        return findAll().filter(i -> districtSubdivision.equalsIgnoreCase(i.getDistrictSubdivision()));
    }

    public static Stream<AdhocCustomer> findByZipCode(final java.lang.String zipCode) {
        return findAll().filter(i -> zipCode.equalsIgnoreCase(i.getZipCode()));
    }

    public static Stream<AdhocCustomer> findByCountryCode(final java.lang.String countryCode) {
        return findAll().filter(i -> countryCode.equalsIgnoreCase(i.getCountryCode()));
    }

    public static Stream<AdhocCustomer> findByCode(final java.lang.String code) {
        return findAll().filter(i -> code.equalsIgnoreCase(i.getCode()));
    }

    public Set<FinantialInstitution> getFinantialInstitutions() {
        return getDebtAccountsSet().stream().map(x -> x.getFinantialInstitution()).collect(Collectors.toSet());
    }

    public DebtAccount getDebtAccountFor(FinantialInstitution institution) {
        return getDebtAccountsSet().stream().filter(x -> x.getFinantialInstitution().equals(institution)).findFirst()
                .orElse(null);
    }

    @Atomic
    public void registerFinantialInstitutions(List<FinantialInstitution> newFinantialInstitutions) {

        Set<FinantialInstitution> actualInstitutions = getFinantialInstitutions();
        for (FinantialInstitution newInst : newFinantialInstitutions) {
            if (actualInstitutions.contains(newInst)) {
            } else {
                DebtAccount.create(newInst, this);
            }
        }

        for (FinantialInstitution actualInst : actualInstitutions) {
            if (newFinantialInstitutions.contains(actualInst)) {
            } else {
                DebtAccount account = getDebtAccountFor(actualInst);
                account.closeDebtAccount();
            }
        }

    }

    public static CustomerType getDefaultCustomerType() {
        return CustomerType.findByCode("ADHOC").findFirst().orElse(null);
    }

    @Override
    public String getBusinessIdentification() {
        return this.getIdentificationNumber();
    }

}
