package org.fenixedu.treasury.domain;

import java.util.stream.Stream;

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

    protected FinantialEntity(final FinantialInstitution finantialInstitution, final String code, final LocalizedString name) {
        this();
        setFinantialInstitution(finantialInstitution);
        setCode(code);
        setName(name);

        checkRules();
    }

    private void checkRules() {
        if(getFinantialInstitution() == null) {
            throw new TreasuryDomainException("error.FinantialEntity.finantialInstitution.required");
        }
        
        if (LocalizedStringUtil.isTrimmedEmpty(getCode())) {
            throw new TreasuryDomainException("error.FinantialEntity.code.required");
        }

        if (LocalizedStringUtil.isTrimmedEmpty(getName())) {
            throw new TreasuryDomainException("error.FinantialEntity.name.required");
        }

        if(findByCode(getFinantialInstitution(), getCode()).count() > 1) {
            throw new TreasuryDomainException("error.FinantialEntity.code.duplicated");
        }
        
        getName().getLocales().stream().forEach(l -> {
            if(findByName(getFinantialInstitution(), getName().getContent(l)).count() > 1) {
                throw new TreasuryDomainException("error.FinantialEntity.name.duplicated", l.toString());
            };
        });
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

    public static Stream<FinantialEntity> findAll() {
        return Bennu.getInstance().getFinantialEntitiesSet().stream();
    }
    
    public static Stream<FinantialEntity> find(final FinantialInstitution finantialInstitution) {
        return findAll().filter(fe -> fe.getFinantialInstitution() == finantialInstitution);
    }

    public static Stream<FinantialEntity> findByCode(final FinantialInstitution finantialInstitution, final String code) {
        return find(finantialInstitution).filter(fe -> fe.getCode().equalsIgnoreCase(code));
    }

    public static Stream<FinantialEntity> findByName(final FinantialInstitution finantialInstitution, final String name) {
        return findAll().filter(fe -> LocalizedStringUtil.isEqualToAnyLocaleIgnoreCase(fe.getName(), name));
    }

    @Atomic
    public static FinantialEntity create(final FinantialInstitution finantialInstitution, final String code, final LocalizedString name) {
        return new FinantialEntity(finantialInstitution, code, name);
    }

}
