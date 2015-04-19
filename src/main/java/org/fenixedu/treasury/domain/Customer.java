package org.fenixedu.treasury.domain;

import java.util.Set;
import java.util.stream.Stream;

import org.fenixedu.bennu.core.domain.Bennu;
import org.fenixedu.treasury.domain.exceptions.TreasuryDomainException;

import pt.ist.fenixframework.Atomic;

public abstract class Customer extends Customer_Base implements IFiscalContributor {
    
    public static final String DEFAULT_FISCAL_NUMBER = "9999999990";

    protected Customer() {
        super();
        setBennu(Bennu.getInstance());
    }
    
    public abstract String getCode();
    public abstract String getFiscalNumber();
    public abstract String getName();
    public abstract String getAddress();
    public abstract String getDistrictSubdivision();
    public abstract String getZipCode();
    public abstract String getCountryCode();

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
	    return Bennu.getInstance().getCustomersSet().stream().filter(x->x instanceof AdhocCustomer).map(AdhocCustomer.class::cast);
	}

	public static Stream<? extends Customer> findByCode(final java.lang.String code) {
		return findAll().filter(i->code.equalsIgnoreCase(i.getCode()));
	  }

}
