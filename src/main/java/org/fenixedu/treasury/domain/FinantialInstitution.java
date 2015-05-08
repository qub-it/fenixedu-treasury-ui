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
import java.util.stream.Stream;

import org.fenixedu.bennu.core.domain.Bennu;
import org.fenixedu.treasury.domain.document.FinantialDocument;
import org.fenixedu.treasury.domain.document.Series;
import org.fenixedu.treasury.domain.exceptions.TreasuryDomainException;
import org.fenixedu.treasury.util.LocalizedStringUtil;
import org.joda.time.DateTime;

import pt.ist.fenixframework.Atomic;
import pt.ist.standards.geographic.Country;
import pt.ist.standards.geographic.District;
import pt.ist.standards.geographic.Municipality;

public class FinantialInstitution extends FinantialInstitution_Base implements IFiscalContributor {

    protected FinantialInstitution() {
        super();
        setBennu(Bennu.getInstance());
    }

    protected FinantialInstitution(final FiscalCountryRegion fiscalCountryRegion, final Currency currency, final String code,
            final String fiscalNumber, final String companyId, final String name, final String companyName, final String address,
            final Country country, final District district, final Municipality municipality, final String locality,
            final String zipCode) {
        this();
        setFiscalCountryRegion(fiscalCountryRegion);
        setCode(code);
        setFiscalNumber(fiscalNumber);
        setCompanyId(companyId);
        setName(name);
        setCompanyName(companyName);
        setAddress(address);
        setCountry(country);
        setDistrict(district);
        setMunicipality(municipality);
        setLocality(locality);
        setZipCode(zipCode);
        setCurrency(currency);
        checkRules();
    }

    private void checkRules() {
        if (LocalizedStringUtil.isTrimmedEmpty(getCode())) {
            throw new TreasuryDomainException("error.FinantialInstitution.code.required");
        }

        if (LocalizedStringUtil.isTrimmedEmpty(getFiscalNumber())) {
            throw new TreasuryDomainException("error.FinantialInstitution.fiscalNumber.required");
        }

        if (LocalizedStringUtil.isTrimmedEmpty(getName())) {
            throw new TreasuryDomainException("error.FinantialInstitution.name.required");
        }

        if (findByCode(getCode()).count() > 1) {
            throw new TreasuryDomainException("error.FinantialInstitution.code.duplicated");
        }
        if (findByName(getName()).count() > 1) {
            throw new TreasuryDomainException("error.FinantialInstitution.name.duplicated");
        }

        IFiscalContributor.findByFiscalNumber(getFiscalNumber());
    }

    public String getComercialRegistrationCode() {
        return this.getFiscalNumber() + " " + this.getAddress();
    }

    @Atomic
    public void edit(final FiscalCountryRegion fiscalCountryRegion, final Currency currency, final String code,
            final String fiscalNumber, final String companyId, final String name, final String companyName, final String address,
            final Country country, final District district, final Municipality municipality, final String locality,
            final String zipCode) {
        setFiscalCountryRegion(fiscalCountryRegion);
        setCurrency(currency);
        setCode(code);
        setFiscalNumber(fiscalNumber);
        setCompanyId(companyId);
        setName(name);
        setCompanyName(companyName);
        setAddress(address);
        setCountry(country);
        setDistrict(district);
        setMunicipality(municipality);
        setLocality(locality);
        setZipCode(zipCode);

        checkRules();
    }

    public boolean isDeletable() {
        //TODOJN
        return false;
    }

    @Atomic
    public void delete() {
        if (!isDeletable()) {
            throw new TreasuryDomainException("error.FinantialInstitution.cannot.delete");
        }

        setBennu(null);
        deleteDomainObject();
    }

    @Atomic
    public void markSeriesAsDefault(final Series series) {
        for (final Series s : getSeriesSet()) {
            s.setDefaultSeries(false);
        }

        series.setDefaultSeries(true);
    }

    // @formatter: off
    /************
     * SERVICES *
     ************/
    // @formatter: on

    public static Stream<FinantialInstitution> findAll() {
        return Bennu.getInstance().getFinantialInstitutionsSet().stream();
    }

    public static Stream<FinantialInstitution> findByCode(final String code) {
        return findAll().filter(fi -> fi.getCode().equalsIgnoreCase(code));
    }

    public static Stream<FinantialInstitution> findByName(final String name) {
        return findAll().filter(fi -> fi.getName().equalsIgnoreCase(name));
    }

    @Atomic
    public static FinantialInstitution create(final FiscalCountryRegion fiscalCountryRegion, final Currency currency,
            final String code, final String fiscalNumber, final String companyId, final String name, final String companyName,
            final String address, final Country country, final District district, final Municipality municipality,
            final String locality, final String zipCode) {
        return new FinantialInstitution(fiscalCountryRegion, currency, code, fiscalNumber, companyId, name, companyName, address,
                country, district, municipality, locality, zipCode);
    }

    public List<FinantialDocument> findPendingDocumentsNotExported(DateTime fromDate, DateTime toDate) {
        // TODO Auto-generated method stub
        return null;
    }

    public List<FinantialDocument> getExportableDocuments(DateTime fromDate, DateTime toDate) {
        // TODO Auto-generated method stub
        return null;
    }

}
