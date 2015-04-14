package org.fenixedu.treasury.domain;

import java.math.BigDecimal;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.fenixedu.bennu.core.domain.Bennu;
import org.fenixedu.commons.i18n.LocalizedString;
import org.fenixedu.treasury.domain.exceptions.TreasuryDomainException;
import org.fenixedu.treasury.util.LocalizedStringUtil;

import pt.ist.fenixframework.Atomic;

public class TreasuryExemption extends TreasuryExemption_Base {

    protected TreasuryExemption() {
        super();
        setBennu(Bennu.getInstance());
    }

    protected TreasuryExemption(final String code, final LocalizedString name, final BigDecimal discountRate) {
        this();
        setCode(code);
        setName(name);
        setDiscountRate(discountRate);

        checkRules();
    }

    private void checkRules() {
        if (StringUtils.isEmpty(getCode())) {
            throw new TreasuryDomainException("error.TreasuryExemption.code.required");
        }

        if (LocalizedStringUtil.isEmpty(getName())) {
            throw new TreasuryDomainException("error.TreasuryExemption.name.required");
        }

        findByCode(getCode());
        getName().getLocales().stream().forEach(l -> findByName(getName().getContent(l)));
    }

    @Atomic
    public void edit(final String code, final LocalizedString name, final BigDecimal discountRate) {
        setCode(code);
        setName(name);
        setDiscountRate(discountRate);

        checkRules();
    }
    
    public boolean isWithDiscountRate() {
        return getDiscountRate() != null;
    }

    public boolean isDeletable() {
        return true;
    }

    @Atomic
    public void delete() {
        if (!isDeletable()) {
            throw new TreasuryDomainException("error.Exemption.cannot.delete");
        }

        setBennu(null);

        deleteDomainObject();
    }

    // @formatter: off
    /************
     * SERVICES *
     ************/
    // @formatter: on

    public static Set<TreasuryExemption> readAll() {
        return Bennu.getInstance().getTreasuryExemptionsSet();
    }

    public static TreasuryExemption findByCode(final String code) {
        TreasuryExemption result = null;

        for (final TreasuryExemption it : readAll()) {
            if (!it.getCode().equalsIgnoreCase(code)) {
                continue;
            }

            if (result != null) {
                throw new TreasuryDomainException("error.Exemption.duplicated.code");
            }

            result = it;
        }

        return result;
    }

    public static TreasuryExemption findByName(final String name) {
        TreasuryExemption result = null;

        for (final TreasuryExemption it : readAll()) {

            if (!LocalizedStringUtil.isEqualToAnyLocaleIgnoreCase(it.getName(), name)) {
                continue;
            }

            if (result != null) {
                throw new TreasuryDomainException("error.Exemption.duplicated.name");
            }

            result = it;
        }

        return result;
    }

    @Atomic
    public static TreasuryExemption create(final String code, final LocalizedString name, final BigDecimal discountRate) {
        return new TreasuryExemption(code, name, discountRate);
    }

}
