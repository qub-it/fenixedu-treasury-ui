package org.fenixedu.treasury.domain;

import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.fenixedu.bennu.core.domain.Bennu;
import org.fenixedu.treasury.domain.exceptions.TreasuryDomainException;
import org.fenixedu.treasury.util.LocalizedStringUtil;

import pt.ist.fenixframework.Atomic;

public class FinantialInstitution extends FinantialInstitution_Base implements IFiscalContributor {

    protected FinantialInstitution() {
        super();
        setBennu(Bennu.getInstance());
    }

    protected FinantialInstitution(String code, final String fiscalNumber, final String companyId, final String name,
            final String companyName, final String address, final String districtSubdivision, final String zipCode,
            final String countryCode) {
        this();
        setCode(code);
        setFiscalNumber(fiscalNumber);
        setCompanyId(companyId);
        setName(name);
        setCompanyName(companyName);
        setAddress(address);
        setDistrictSubdivision(districtSubdivision);
        setZipCode(zipCode);
        setCountryCode(countryCode);

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

        findByCode(getCode());
        IFiscalContributor.findByFiscalNumber(getFiscalNumber());
    }

    public String getComercialRegistrationCode() {
        return this.getFiscalNumber() + " " + this.getAddress();
    }

    @Atomic
    public void edit(String code, final String fiscalNumber, final String companyId, final String name, final String companyName,
            final String address, final String districtSubdivision, final String zipCode, final String countryCode) {
        setCode(code);
        setFiscalNumber(fiscalNumber);
        setCompanyId(companyId);
        setName(name);
        setCompanyName(companyName);
        setAddress(address);
        setDistrictSubdivision(districtSubdivision);
        setZipCode(zipCode);
        setCountryCode(countryCode);

        checkRules();
    }

    public boolean isDeletable() {
        return true;
    }

    @Atomic
    public void delete() {
        if (!isDeletable()) {
            throw new TreasuryDomainException("error.FinantialInstitution.cannot.delete");
        }

        setBennu(null);

        deleteDomainObject();
    }

    // @formatter: off
    /************
     * SERVICES *
     ************/
    // @formatter: on

    public static Set<FinantialInstitution> readAll() {
        return Bennu.getInstance().getFinantialInstitutionsSet();
    }

    public static FinantialInstitution findByCode(final String code) {
        FinantialInstitution result = null;

        for (final FinantialInstitution it : readAll()) {
            if (!it.getCode().equalsIgnoreCase(code)) {
                continue;
            }

            if (result != null) {
                throw new TreasuryDomainException("error.FinantialInstitution.duplicated.code");
            }

            result = it;
        }

        return result;
    }

    public static FinantialInstitution findByName(final String name) {
        FinantialInstitution result = null;

        for (final FinantialInstitution it : readAll()) {

            if (!it.getName().equalsIgnoreCase(name)) {
                continue;
            }

            if (result != null) {
                throw new TreasuryDomainException("error.FinantialInstitution.duplicated.name");
            }

            result = it;
        }

        return result;
    }

    @Atomic
    public static FinantialInstitution create(String code, final String fiscalNumber, final String companyId, final String name,
            final String companyName, final String address, final String districtSubdivision, final String zipCode,
            final String countryCode) {
        return new FinantialInstitution(code, fiscalNumber, companyId, name, companyName, address, districtSubdivision, zipCode,
                countryCode);
    }

}
