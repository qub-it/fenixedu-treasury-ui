package org.fenixedu.treasury.domain;

import java.util.Set;

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

    public static Set<? extends Customer> readAll() {
        return Bennu.getInstance().getCustomersSet();
    }

    public static Customer findByCode(final String code) {
        Customer result = null;

        for (final Customer it : readAll()) {
            if (!it.getCode().equalsIgnoreCase(code)) {
                continue;
            }

            if (result != null) {
                throw new TreasuryDomainException("error.Customer.duplicated.code");
            }

            result = it;
        }

        return result;
    }

}
