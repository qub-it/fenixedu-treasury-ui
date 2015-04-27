/**
 * This file was created by Quorum Born IT <http://www.qub-it.com/> and its 
 * copyright terms are bind to the legal agreement regulating the FenixEdu@ULisboa 
 * software development project between Quorum Born IT and Serviços Partilhados da
 * Universidade de Lisboa:
 *  - Copyright © 2015 Quorum Born IT (until any Go-Live phase)
 *  - Copyright © 2015 Universidade de Lisboa (after any Go-Live phase)
 *
 * Contributors: ricardo.pedro@qub-it.com, anil.mamede@qub-it.com
 * 
 *
 * 
 * This file is part of FenixEdu Treasury.
 *
 * FenixEdu Treasury is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * FenixEdu Treasury is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with FenixEdu Treasury.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.fenixedu.treasury.domain;

import java.util.stream.Stream;

import org.fenixedu.bennu.core.domain.Bennu;
import org.fenixedu.bennu.core.domain.User;
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
    
    public static Stream<FinantialEntity> findWithPermissionsFor(final User user) {
        // TODO: ACCESS CONTROL
        return findAll();
    }

    @Atomic
    public static FinantialEntity create(final FinantialInstitution finantialInstitution, final String code, final LocalizedString name) {
        return new FinantialEntity(finantialInstitution, code, name);
    }

}
