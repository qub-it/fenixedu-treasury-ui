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
        if (LocalizedStringUtil.isTrimmedEmpty(getCode())) {
            throw new TreasuryDomainException("error.TreasuryExemption.code.required");
        }

        if (LocalizedStringUtil.isTrimmedEmpty(getName())) {
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
