package org.fenixedu.treasury.domain;

import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.fenixedu.bennu.core.domain.Bennu;
import org.fenixedu.commons.i18n.LocalizedString;
import org.fenixedu.treasury.domain.exceptions.TreasuryDomainException;
import org.fenixedu.treasury.util.LocalizedStringUtil;

import pt.ist.fenixframework.Atomic;

public class Product extends Product_Base {

    protected Product() {
        super();
        setBennu(Bennu.getInstance());
    }

    protected Product(final String code, final LocalizedString name, final LocalizedString unitOfMeasure, boolean active) {
        this();
        setCode(code);
        setName(name);
        setUnitOfMeasure(unitOfMeasure);
        setActive(active);

        checkRules();
    }

    private void checkRules() {
        if (StringUtils.isEmpty(getCode())) {
            throw new TreasuryDomainException("error.Product.code.required");
        }

        if (LocalizedStringUtil.isEmpty(getName())) {
            throw new TreasuryDomainException("error.Product.name.required");
        }

        findByCode(getCode());
        getName().getLocales().stream().forEach(l -> findByName(getName().getContent(l)));

        if (LocalizedStringUtil.isEmpty(getUnitOfMeasure())) {
            throw new TreasuryDomainException("error.Product.unitOfMeasure.required");
        }
    }

    @Atomic
    public void edit(final String code, final LocalizedString name, final LocalizedString unitOfMeasure, boolean active) {
        setCode(code);
        setName(name);
        setUnitOfMeasure(unitOfMeasure);
        setActive(active);

        checkRules();
    }

    public boolean isDeletable() {
        return true;
    }

    @Atomic
    public void delete() {
        if (!isDeletable()) {
            throw new TreasuryDomainException("error.Product.cannot.delete");
        }

        setBennu(null);

        deleteDomainObject();
    }

    // @formatter: off
    /************
     * SERVICES *
     ************/
    // @formatter: on

    public static Set<Product> readAll() {
        return Bennu.getInstance().getProductsSet();
    }

    public static Product findByCode(final String code) {
        Product result = null;

        for (final Product it : readAll()) {
            if (!it.getCode().equalsIgnoreCase(code)) {
                continue;
            }

            if (result != null) {
                throw new TreasuryDomainException("error.Product.duplicated.code");
            }

            result = it;
        }

        return result;
    }

    public static Product findByName(final String name) {
        Product result = null;

        for (final Product it : readAll()) {

            if (!LocalizedStringUtil.isEqualToAnyLocaleIgnoreCase(it.getName(), name)) {
                continue;
            }

            if (result != null) {
                throw new TreasuryDomainException("error.Product.duplicated.name");
            }

            result = it;
        }

        return result;
    }

    @Atomic
    public static Product create(final String code, final LocalizedString name, final LocalizedString unitOfMeasure, boolean active) {
        return new Product(code, name, unitOfMeasure, active);
    }

}
