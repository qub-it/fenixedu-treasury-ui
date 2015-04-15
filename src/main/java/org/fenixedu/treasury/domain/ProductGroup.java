package org.fenixedu.treasury.domain;

import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.fenixedu.bennu.core.domain.Bennu;
import org.fenixedu.commons.i18n.LocalizedString;
import org.fenixedu.treasury.domain.exceptions.TreasuryDomainException;
import org.fenixedu.treasury.util.LocalizedStringUtil;

import pt.ist.fenixframework.Atomic;

public class ProductGroup extends ProductGroup_Base {

    protected ProductGroup() {
        super();
        setBennu(Bennu.getInstance());
    }

    protected ProductGroup(final String code, final LocalizedString name) {
        this();
        setCode(code);
        setName(name);

        checkRules();
    }

    private void checkRules() {
        if (LocalizedStringUtil.isTrimmedEmpty(getCode())) {
            throw new TreasuryDomainException("error.ProductGroup.code.required");
        }

        if (LocalizedStringUtil.isTrimmedEmpty(getName())) {
            throw new TreasuryDomainException("error.ProductGroup.name.required");
        }

        findByCode(getCode());
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
            throw new TreasuryDomainException("error.ProductGroup.cannot.delete");
        }

        setBennu(null);

        deleteDomainObject();
    }

    // @formatter: off
    /************
     * SERVICES *
     ************/
    // @formatter: on

    public static Set<ProductGroup> readAll() {
        return Bennu.getInstance().getProductGroupsSet();
    }

    public static ProductGroup findByCode(final String code) {
        ProductGroup result = null;

        for (final ProductGroup it : readAll()) {
            if (!it.getCode().equalsIgnoreCase(code)) {
                continue;
            }

            if (result != null) {
                throw new TreasuryDomainException("error.ProductGroup.duplicated.code");
            }

            result = it;
        }

        return result;
    }

    public static ProductGroup findByName(final String name) {
        ProductGroup result = null;

        for (final ProductGroup it : readAll()) {

            if (!LocalizedStringUtil.isEqualToAnyLocaleIgnoreCase(it.getName(), name)) {
                continue;
            }

            if (result != null) {
                throw new TreasuryDomainException("error.ProductGroup.duplicated.name");
            }

            result = it;
        }

        return result;
    }

    @Atomic
    public static ProductGroup create(final String code, final LocalizedString name) {
        return new ProductGroup(code, name);
    }

}
