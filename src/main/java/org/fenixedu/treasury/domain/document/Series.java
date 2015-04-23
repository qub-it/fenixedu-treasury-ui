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
package org.fenixedu.treasury.domain.document;

import java.util.Set;

import org.fenixedu.bennu.core.domain.Bennu;
import org.fenixedu.commons.i18n.LocalizedString;
import org.fenixedu.treasury.domain.FinantialInstitution;
import org.fenixedu.treasury.domain.exceptions.TreasuryDomainException;
import org.fenixedu.treasury.util.LocalizedStringUtil;

import pt.ist.fenixframework.Atomic;

import com.google.common.collect.Sets;

public class Series extends Series_Base {

    protected Series() {
        super();
        setBennu(Bennu.getInstance());
    }

    protected Series(final FinantialInstitution finantialInstitution, final String code, final LocalizedString name,
            final boolean externSeries, final boolean certificated, final boolean legacy) {
        this();
        setFinantialInstitution(finantialInstitution);
        setCode(code);
        setName(name);
        setExternSeries(externSeries);
        setCertificated(certificated);
        setLegacy(legacy);

        checkRules();
    }

    private void checkRules() {
        if (getFinantialInstitution() == null) {
            throw new TreasuryDomainException("error.Series.finantialInstitution.required");
        }

        if (LocalizedStringUtil.isTrimmedEmpty(getCode())) {
            throw new TreasuryDomainException("error.Series.code.required");
        }

        if (LocalizedStringUtil.isTrimmedEmpty(getName())) {
            throw new TreasuryDomainException("error.Series.name.required");
        }

        findByCode(getFinantialInstitution(), getCode());
        getName().getLocales().stream().forEach(l -> findByName(getFinantialInstitution(), getName().getContent(l)));
    }

    @Atomic
    public void edit(final String code, final LocalizedString name, final boolean externSeries, final boolean certificated,
            final boolean legacy) {
        setCode(code);
        setName(name);
        setExternSeries(externSeries);
        setCertificated(certificated);
        setLegacy(legacy);

        checkRules();
    }

    public boolean isDeletable() {
        return true;
    }

    @Atomic
    public void delete() {
        if (!isDeletable()) {
            throw new TreasuryDomainException("error.Series.cannot.delete");
        }

        setBennu(null);

        deleteDomainObject();
    }

    // @formatter: off
    /************
     * SERVICES *
     ************/
    // @formatter: on

    public static Set<Series> readAll() {
        return Bennu.getInstance().getSeriesSet();
    }

    public static Set<Series> find(final FinantialInstitution finantialInstitution) {
        Set<Series> result = Sets.newHashSet();

        for (final Series it : readAll()) {
            if (it.getFinantialInstitution() == finantialInstitution) {
                result.add(it);
            }
        }

        return result;
    }

    public static Series findByCode(final FinantialInstitution finantialInstitution, final String code) {
        Series result = null;

        for (final Series it : find(finantialInstitution)) {
            if (!it.getCode().equalsIgnoreCase(code)) {
                continue;
            }

            if (result != null) {
                throw new TreasuryDomainException("error.Series.duplicated.code");
            }

            result = it;
        }

        return result;
    }

    public static Series findByName(final FinantialInstitution finantialInstitution, final String name) {
        Series result = null;

        for (final Series it : find(finantialInstitution)) {

            if (!LocalizedStringUtil.isEqualToAnyLocaleIgnoreCase(it.getName(), name)) {
                continue;
            }

            if (result != null) {
                throw new TreasuryDomainException("error.Series.duplicated.name");
            }

            result = it;
        }

        return result;
    }

    @Atomic
    public static Series create(final FinantialInstitution finantialInstitution, final String code, final LocalizedString name,
            final boolean externSeries, final boolean certificated, final boolean legacy) {
        return new Series(finantialInstitution, code, name, externSeries, certificated, legacy);
    }

    public boolean isExternSeries() {
        return getExternSeries();
    }

    public boolean isCertificated() {
        return getCertificated();
    }

    public boolean isLegacy() {
        return getLegacy();
    }
}
