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
package org.fenixedu.treasury.dto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.fenixedu.bennu.IBean;
import org.fenixedu.bennu.TupleDataSourceBean;
import org.fenixedu.commons.i18n.I18N;
import org.fenixedu.treasury.domain.FinantialInstitution;
import org.fenixedu.treasury.domain.FiscalCountryRegion;

import pt.ist.standards.geographic.Country;
import pt.ist.standards.geographic.District;
import pt.ist.standards.geographic.GeographicInfoLoader;
import pt.ist.standards.geographic.Municipality;
import pt.ist.standards.geographic.Place;

public class FinantialInstitutionBean implements IBean, Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    private String fiscalNumber;

    private Country country;

    private District district;

    private Municipality municipality;

    private FiscalCountryRegion fiscalcountryregion;

    private List<TupleDataSourceBean> countries;

    private List<TupleDataSourceBean> districts;

    private List<TupleDataSourceBean> municipalities;

    private List<TupleDataSourceBean> fiscalcountryregions;

    private String code;
    private String companyId;
    private String name;
    private String companyName;
    private String address;
    private String locality;
    private String zipCode;

    public FinantialInstitutionBean() {
        this.updateModelLists();
    }

    public FinantialInstitutionBean(FinantialInstitution finantialInstitution) {
        this.code = finantialInstitution.getCode();
        this.address = finantialInstitution.getAddress();
        this.companyId = finantialInstitution.getCompanyId();
        this.companyName = finantialInstitution.getCompanyName();
        this.country = finantialInstitution.getCountry();
        this.district = finantialInstitution.getDistrict();
        this.fiscalcountryregion = finantialInstitution.getFiscalCountryRegion();
        this.fiscalNumber = finantialInstitution.getFiscalNumber();
        this.locality = finantialInstitution.getLocality();
        this.municipality = finantialInstitution.getMunicipality();
        this.name = finantialInstitution.getName();
        this.zipCode = finantialInstitution.getZipCode();
        this.updateModelLists();
    }

    public Country getCountry() {
        return country;
    }

    public void setCountry(Country country) {
        this.country = country;
    }

    public District getDistrict() {
        return district;
    }

    public void setDistrict(District district) {
        this.district = district;
    }

    public Municipality getMunicipality() {
        return municipality;
    }

    public void setMunicipality(Municipality municipality) {
        this.municipality = municipality;
    }

    public List<TupleDataSourceBean> getCountries() {
        return countries;
    }

    public void setCountries(List<Country> countries) {
        this.countries = countries.stream().map(x -> {
            TupleDataSourceBean tuple = new TupleDataSourceBean();

            tuple.setId(x.exportAsString());
            tuple.setText(x.getLocalizedName(I18N.getLocale()));
            return tuple;
        }).collect(Collectors.toList());
    }

    public List<TupleDataSourceBean> getDistricts() {
        return districts;
    }

    public void setDistricts(List<District> districts) {
        this.districts = districts.stream().map(x -> {
            TupleDataSourceBean tuple = new TupleDataSourceBean();

            tuple.setId(x.exportAsString());
            tuple.setText(x.getLocalizedName(I18N.getLocale()));
            return tuple;
        }).collect(Collectors.toList());
    }

    public List<TupleDataSourceBean> getMunicipalities() {
        return municipalities;
    }

    public void setMunicipalities(List<Municipality> municipalities) {
        this.municipalities = municipalities.stream().map(x -> {
            TupleDataSourceBean tuple = new TupleDataSourceBean();

            tuple.setId(x.exportAsString());
            tuple.setText(x.getLocalizedName(I18N.getLocale()));
            return tuple;
        }).collect(Collectors.toList());
    }

    public String getFiscalNumber() {
        return fiscalNumber;
    }

    public void setFiscalNumber(String fiscalNumber) {
        this.fiscalNumber = fiscalNumber;
    }

    public List<TupleDataSourceBean> getFiscalcountryregions() {
        return fiscalcountryregions;
    }

    public void setFiscalcountryregions(List<FiscalCountryRegion> fiscalcountryregions) {
        this.fiscalcountryregions = fiscalcountryregions.stream().map(x -> {
            TupleDataSourceBean tuple = new TupleDataSourceBean();

            tuple.setId(x.getExternalId());
            tuple.setText(x.getName().getContent());
            return tuple;
        }).collect(Collectors.toList());
    }

    public FiscalCountryRegion getFiscalcountryregion() {
        return fiscalcountryregion;
    }

    public void setFiscalcountryregion(FiscalCountryRegion fiscalcountryregion) {
        this.fiscalcountryregion = fiscalcountryregion;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getCompanyId() {
        return companyId;
    }

    public void setCompanyId(String companyId) {
        this.companyId = companyId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getLocality() {
        return locality;
    }

    public void setLocality(String locality) {
        this.locality = locality;
    }

    public String getZipCode() {
        return zipCode;
    }

    public void setZipCode(String zipCode) {
        this.zipCode = zipCode;
    }

    public void updateModelLists() {

        this.setCountries(GeographicInfoLoader.getInstance().findAllCountries().sorted((s1, s2) -> {
            return s1.getLocalizedName(I18N.getLocale()).compareTo(s2.getLocalizedName(I18N.getLocale()));
        }).collect(Collectors.toList()));

        if (this.getCountry() != null) {
            this.setDistricts(this.getCountry().getPlaces().stream().sorted((s1, s2) -> {
                return s1.getLocalizedName(I18N.getLocale()).compareTo(s2.getLocalizedName(I18N.getLocale()));
            }).collect(Collectors.toList()));
            this.setMunicipalities(new ArrayList<Municipality>());
        }
        if (this.getDistrict() != null) {
            this.setMunicipalities(this.getDistrict().getPlaces().stream().sorted((s1, s2) -> {
                return s1.getLocalizedName(I18N.getLocale()).compareTo(s2.getLocalizedName(I18N.getLocale()));
            }).collect(Collectors.toList()));
        }

    }

}
