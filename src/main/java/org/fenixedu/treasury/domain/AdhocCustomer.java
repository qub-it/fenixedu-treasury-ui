package org.fenixedu.treasury.domain;

import org.apache.commons.lang.StringUtils;
import org.fenixedu.bennu.core.domain.Bennu;
import org.fenixedu.treasury.domain.exceptions.TreasuryDomainException;

import pt.ist.fenixframework.Atomic;

public class AdhocCustomer extends AdhocCustomer_Base {

    protected AdhocCustomer() {
        super();
        setBennu(Bennu.getInstance());
    }

    protected AdhocCustomer(final String code, final String fiscalNumber, final String name, final String address,
            final String districtSubdivision, final String zipCode, final String countryCode) {
        this();
        setCode(code);
        setFiscalNumber(fiscalNumber);
        setName(name);
        setAddress(address);
        setDistrictSubdivision(districtSubdivision);
        setZipCode(zipCode);
        setCountryCode(countryCode);

        checkRules();
    }

    private void checkRules() {
        if (StringUtils.isEmpty(getCode())) {
            throw new TreasuryDomainException("error.AdhocCustomer.code.required");
        }

        if (StringUtils.isEmpty(getName())) {
            throw new TreasuryDomainException("error.AdhocCustomer.name.required");
        }

        findByCode(getCode());
    }

    @Atomic
    public void edit(final String code, final String fiscalNumber, final String name, final String address,
            final String districtSubdivision, final String zipCode, final String countryCode) {
        setCode(code);
        setFiscalNumber(fiscalNumber);
        setName(name);
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
            throw new TreasuryDomainException("error.AdhocCustomer.cannot.delete");
        }

        setBennu(null);

        deleteDomainObject();
    }

    // @formatter: off
    /************
     * SERVICES *
     ************/
    // @formatter: on

    @Atomic
    public static AdhocCustomer create(final String code, final String fiscalNumber, final String name, final String address,
            final String districtSubdivision, final String zipCode, final String countryCode) {
        return new AdhocCustomer(code, fiscalNumber, name, address, districtSubdivision, zipCode, countryCode);
    }

}
