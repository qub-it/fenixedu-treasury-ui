package org.fenixedu.treasury.domain;

import java.util.stream.Stream;

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
    public void edit(final String code, final String fiscalNumber, final String name, final String address,
            final String districtSubdivision, final String zipCode, final String countryCode) {
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

    @Atomic
    public static AdhocCustomer create(final String code, final String fiscalNumber, final String name, final String address,
            final String districtSubdivision, final String zipCode, final String countryCode) {
        return new AdhocCustomer(code, fiscalNumber, name, address, districtSubdivision, zipCode, countryCode);
    }

    // @formatter: off
    /************
     * SERVICES *
     ************/
    // @formatter: on

	public static Stream<AdhocCustomer> findAll() {
	    return Bennu.getInstance().getCustomersSet().stream().filter(x->x instanceof AdhocCustomer).map(AdhocCustomer.class::cast);
	}
	
	
	public static Stream<AdhocCustomer> findByFiscalNumber(final java.lang.String fiscalNumber) {
		return findAll().filter(i->fiscalNumber.equalsIgnoreCase(i.getFiscalNumber()));
	  }
	public static Stream<AdhocCustomer> findByName(final java.lang.String name) {
		return findAll().filter(i->name.equalsIgnoreCase(i.getName()));
	  }
	public static Stream<AdhocCustomer> findByAddress(final java.lang.String address) {
		return findAll().filter(i->address.equalsIgnoreCase(i.getAddress()));
	  }
	public static Stream<AdhocCustomer> findByDistrictSubdivision(final java.lang.String districtSubdivision) {
		return findAll().filter(i->districtSubdivision.equalsIgnoreCase(i.getDistrictSubdivision()));
	  }
	public static Stream<AdhocCustomer> findByZipCode(final java.lang.String zipCode) {
		return findAll().filter(i->zipCode.equalsIgnoreCase(i.getZipCode()));
	  }
	public static Stream<AdhocCustomer> findByCountryCode(final java.lang.String countryCode) {
		return findAll().filter(i->countryCode.equalsIgnoreCase(i.getCountryCode()));
	  }

	public static Stream<AdhocCustomer> findByCode(final java.lang.String code) {
		return findAll().filter(i->code.equalsIgnoreCase(i.getCode()));
	  }

}
