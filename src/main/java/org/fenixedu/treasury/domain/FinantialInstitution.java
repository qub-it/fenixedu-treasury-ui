package org.fenixedu.treasury.domain;

import java.util.List;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.fenixedu.bennu.core.domain.Bennu;
import org.fenixedu.treasury.domain.document.FinantialDocument;
import org.fenixedu.treasury.domain.exceptions.TreasuryDomainException;
import org.fenixedu.treasury.util.LocalizedStringUtil;
import org.joda.time.DateTime;

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

        if (findByCode(getCode()).count() > 1) {
            throw new TreasuryDomainException("error.FinantialInstitution.code.duplicated");
        findByCode(getCode());

        if (findByName(getName()).count() > 1) {
            throw new TreasuryDomainException("error.FinantialInstitution.name.duplicated");
        }

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
    public static FinantialInstitution create(String code, final String fiscalNumber, final String companyId, final String name,
            final String companyName, final String address, final String districtSubdivision, final String zipCode,
            final String countryCode) {
        return new FinantialInstitution(code, fiscalNumber, companyId, name, companyName, address, districtSubdivision, zipCode,
                countryCode);
    }

	public List<FinantialDocument> findPendingDocumentsNotExported(
			DateTime fromDate, DateTime toDate) {
		// TODO Auto-generated method stub
		return null;
	}

	public List<FinantialDocument> getExportableDocuments(DateTime fromDate,
			DateTime toDate) {
		// TODO Auto-generated method stub
		return null;
	}

}
