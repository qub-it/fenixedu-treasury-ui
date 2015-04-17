package org.fenixedu.treasury.domain;

import org.apache.commons.lang.StringUtils;
import org.fenixedu.bennu.core.domain.Bennu;
import org.fenixedu.treasury.domain.exceptions.TreasuryDomainException;
import org.fenixedu.treasury.util.LocalizedStringUtil;

import pt.ist.fenixframework.Atomic;

public class AdhocCustomer extends AdhocCustomer_Base {

    protected AdhocCustomer() {
        super();
        setBennu(Bennu.getInstance());
    }

    protected AdhocCustomer(final String fiscalNumber, final String name, final String address, final String districtSubdivision,
            final String zipCode, final String countryCode) {
        this();
        setCode(getExternalId());
        setFiscalNumber(fiscalNumber);
        setName(name);
        setAddress(address);
        setDistrictSubdivision(districtSubdivision);
        setZipCode(zipCode);
        setCountryCode(countryCode);

        checkRules();
    }

    private void checkRules() {
        if (LocalizedStringUtil.isTrimmedEmpty(getCode())) {
            throw new TreasuryDomainException("error.AdhocCustomer.code.required");
        }

        if (LocalizedStringUtil.isTrimmedEmpty(getName())) {
            throw new TreasuryDomainException("error.AdhocCustomer.name.required");
        }

        if (findByCode(getCode()).count() > 1) {
            throw new TreasuryDomainException("error.AdhocCustomer.code.duplicated");
        }
    }

    @Atomic
    public void edit(final String fiscalNumber, final String name, final String address, final String districtSubdivision,
            final String zipCode, final String countryCode) {
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
    public static AdhocCustomer create(final String fiscalNumber, final String name, final String address,
            final String districtSubdivision, final String zipCode, final String countryCode) {
        return new AdhocCustomer(fiscalNumber, name, address, districtSubdivision, zipCode, countryCode);
    }

}
