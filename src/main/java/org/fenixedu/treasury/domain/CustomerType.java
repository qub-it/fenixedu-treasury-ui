package org.fenixedu.treasury.domain;

import java.util.stream.Stream;

import org.fenixedu.bennu.core.domain.Bennu;
import org.fenixedu.commons.i18n.LocalizedString;
import org.fenixedu.treasury.domain.exceptions.TreasuryDomainException;
import org.fenixedu.treasury.util.LocalizedStringUtil;

import pt.ist.fenixframework.Atomic;

public class CustomerType extends CustomerType_Base {

    protected CustomerType() {
        super();
        setBennu(Bennu.getInstance());
    }

    protected CustomerType(final String code, final LocalizedString name) {
        this();
        setCode(code);
        setName(name);

        checkRules();
    }

    private void checkRules() {
        if (LocalizedStringUtil.isTrimmedEmpty(getCode())) {
            throw new TreasuryDomainException("error.CustomerType.code.required");
        }

        if (LocalizedStringUtil.isTrimmedEmpty(getName())) {
            throw new TreasuryDomainException("error.CustomerType.name.required");
        }

        if(findByCode(getCode()).count() > 1) {
            throw new TreasuryDomainException("error.CustomerType.code.duplicated");
        }
        
        getName().getLocales().stream().forEach(l -> findByName(getName().getContent(l)));
    }

    @Atomic
    public void edit(final String code, final LocalizedString name) {
        setCode(code);
        setName(name);

        checkRules();
    }

    public boolean isDeletable() {
        return true;
    }

    @Atomic
    public void delete() {
        if (!isDeletable()) {
            throw new TreasuryDomainException("error.CustomerType.cannot.delete");
        }

        setBennu(null);

        deleteDomainObject();
    }

    // @formatter: off
    /************
     * SERVICES *
     ************/
    // @formatter: on

    public static Stream<CustomerType> findAll() {
        return Bennu.getInstance().getCustomerTypesSet().stream();
    }

    public static Stream<CustomerType> findByCode(final String code) {
        return findAll().filter(ct -> ct.getCode().equalsIgnoreCase(code));
    }

    public static Stream<CustomerType> findByName(final String name) {
        return findAll().filter(ct -> ct.getName().equals(name));
    }

    @Atomic
    public static CustomerType create(final String code, final LocalizedString name) {
        return new CustomerType(code, name);
    }
    
}
