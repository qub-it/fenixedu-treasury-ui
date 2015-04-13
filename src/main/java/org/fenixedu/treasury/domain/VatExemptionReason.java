package org.fenixedu.treasury.domain;

import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.fenixedu.bennu.core.domain.Bennu;
import org.fenixedu.commons.i18n.LocalizedString;
import org.fenixedu.treasury.domain.exceptions.TreasuryDomainException;
import org.fenixedu.treasury.util.LocalizedStringUtil;

import pt.ist.fenixframework.Atomic;

public class VatExemptionReason extends VatExemptionReason_Base {

    protected VatExemptionReason() {
        super();
        setBennu(Bennu.getInstance());
    }

    protected VatExemptionReason(final String code, final LocalizedString name) {
        this();
        setCode(code);
        setName(name);

        checkRules();
    }

    private void checkRules() {
        if (StringUtils.isEmpty(getCode())) {
            throw new TreasuryDomainException("error.VatExemptionReason.code.required");
        }

        if (LocalizedStringUtil.isEmpty(getName())) {
            throw new TreasuryDomainException("error.VatExemptionReason.name.required");
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
            throw new TreasuryDomainException("error.VatExemptionReason.cannot.delete");
        }

        setBennu(null);

        deleteDomainObject();
    }

    // @formatter: off
    /************
     * SERVICES *
     ************/
    // @formatter: on

    public static Set<VatExemptionReason> readAll() {
        return Bennu.getInstance().getVatExemptionReasonsSet();
    }

    public static VatExemptionReason findByCode(final String code) {
        VatExemptionReason result = null;

        for (final VatExemptionReason it : readAll()) {
            if (!it.getCode().equalsIgnoreCase(code)) {
                continue;
            }

            if (result != null) {
                throw new TreasuryDomainException("error.VatExemptionReason.duplicated.code");
            }

            result = it;
        }

        return result;
    }

    public static VatExemptionReason findByName(final String name) {
        VatExemptionReason result = null;

        for (final VatExemptionReason it : readAll()) {

            if (!LocalizedStringUtil.isEqualToAnyLocaleIgnoreCase(it.getName(), name)) {
                continue;
            }

            if (result != null) {
                throw new TreasuryDomainException("error.VatExemptionReason.duplicated.name");
            }

            result = it;
        }

        return result;
    }

    @Atomic
    public static VatExemptionReason create(final String code, final LocalizedString name) {
        return new VatExemptionReason(code, name);
    }

}
