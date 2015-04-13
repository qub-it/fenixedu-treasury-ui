package org.fenixedu.treasury.domain;

import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.fenixedu.bennu.core.domain.Bennu;
import org.fenixedu.commons.i18n.LocalizedString;
import org.fenixedu.treasury.domain.exceptions.TreasuryDomainException;
import org.fenixedu.treasury.util.LocalizedStringUtil;

import pt.ist.fenixframework.Atomic;

public class FinantialEntity extends FinantialEntity_Base {

    protected FinantialEntity() {
        super();
        setBennu(Bennu.getInstance());
    }

    protected FinantialEntity(final String code, final LocalizedString name) {
        this();
        setCode(code);
        setName(name);

        checkRules();
    }

    private void checkRules() {
        if (StringUtils.isEmpty(getCode())) {
            throw new TreasuryDomainException("error.FinantialEntity.code.required");
        }

        if (LocalizedStringUtil.isEmpty(getName())) {
            throw new TreasuryDomainException("error.FinantialEntity.name.required");
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
            throw new TreasuryDomainException("error.FinantialEntity.cannot.delete");
        }

        setBennu(null);

        deleteDomainObject();
    }

    // @formatter: off
    /************
     * SERVICES *
     ************/
    // @formatter: on

    public static Set<FinantialEntity> readAll() {
        return Bennu.getInstance().getFinantialEntitiesSet();
    }

    public static FinantialEntity findByCode(final String code) {
        FinantialEntity result = null;

        for (final FinantialEntity it : readAll()) {
            if (!it.getCode().equalsIgnoreCase(code)) {
                continue;
            }

            if (result != null) {
                throw new TreasuryDomainException("error.FinantialEntity.duplicated.code");
            }

            result = it;
        }

        return result;
    }

    public static FinantialEntity findByName(final String name) {
        FinantialEntity result = null;

        for (final FinantialEntity it : readAll()) {

            if (!LocalizedStringUtil.isEqualToAnyLocaleIgnoreCase(it.getName(), name)) {
                continue;
            }

            if (result != null) {
                throw new TreasuryDomainException("error.FinantialEntity.duplicated.name");
            }

            result = it;
        }

        return result;
    }

    @Atomic
    public static FinantialEntity create(final String code, final LocalizedString name) {
        return new FinantialEntity(code, name);
    }

}
