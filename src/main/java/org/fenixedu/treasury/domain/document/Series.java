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

import java.util.Comparator;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Stream;

import org.fenixedu.bennu.core.domain.Bennu;
import org.fenixedu.commons.i18n.LocalizedString;
import org.fenixedu.treasury.domain.FinantialInstitution;
import org.fenixedu.treasury.domain.exceptions.TreasuryDomainException;
import org.fenixedu.treasury.util.LocalizedStringUtil;

import pt.ist.fenixframework.Atomic;

import com.google.common.collect.Sets;

public class Series extends Series_Base {

    private static final Comparator<Series> COMPARATOR_BY_CODE = new Comparator<Series>() {

        @Override
        public int compare(Series o1, Series o2) {
            int c = o1.getCode().compareTo(o2.getCode());

            return c != 0 ? c : o1.getExternalId().compareTo(o2.getExternalId());
        }
    };

    public static final Comparator<Series> COMPARATOR_BY_DEFAULT = new Comparator<Series>() {

        @Override
        public int compare(final Series o1, final Series o2) {
            if (o1.isDefaultSeries() && o2.isDefaultSeries()) {
                return 1;
            } else if (!o1.isDefaultSeries() && o2.isDefaultSeries()) {
                return -1;
            }

            return COMPARATOR_BY_CODE.compare(o1, o2);
        }
    };

    protected Series() {
        super();
        setBennu(Bennu.getInstance());
    }

    protected Series(final FinantialInstitution finantialInstitution, final String code, final LocalizedString name,
            final boolean externSeries, final boolean certificated, final boolean legacy, final boolean defaultSeries) {
        this();
        setActive(true);
        setFinantialInstitution(finantialInstitution);
        setCode(code);
        setName(name);
        setExternSeries(externSeries);
        setCertificated(certificated);
        setLegacy(legacy);
        setDefaultSeries(defaultSeries);

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

        //Check if the Series exists for All DocumentNumberSeries
        FinantialDocumentType.findAll().forEach(x -> {
            if (this.getDocumentNumberSeriesSet().stream().anyMatch(series -> series.getFinantialDocumentType().equals(x))) {
                //do nothing
            } else {
                this.addDocumentNumberSeries(new DocumentNumberSeries(x, this));
            }
        });

        if (findDefault(getFinantialInstitution()).count() > 1) {
            throw new TreasuryDomainException("error.Series.default.not.unique");
        }
    }

    @Atomic
    public void edit(final String code, final LocalizedString name, final boolean externSeries, final boolean certificated,
            final boolean legacy, final boolean active) {
        setName(name);
        setActive(active);
        if (!code.equalsIgnoreCase(getCode())) {
            if (this.isSeriesUsedForAnyDocument()) {
                throw new TreasuryDomainException("error.Series.invalid.series.type.in.used.series");
            }
            setCode(code);
        }
        if (externSeries != getExternSeries()) {
            if (this.isSeriesUsedForAnyDocument()) {
                throw new TreasuryDomainException("error.Series.invalid.series.type.in.used.series");
            }
            setExternSeries(externSeries);
        }
        if (certificated != getCertificated()) {
            if (this.isSeriesUsedForAnyDocument()) {
                throw new TreasuryDomainException("error.Series.invalid.series.type.in.used.series");
            }
            setCertificated(certificated);
        }

        if (legacy != getLegacy()) {
            if (this.isSeriesUsedForAnyDocument()) {
                throw new TreasuryDomainException("error.Series.invalid.series.type.in.used.series");
            }
            setLegacy(legacy);
        }
        checkRules();
    }

    private boolean isSeriesUsedForAnyDocument() {
        return this.getDocumentNumberSeriesSet().stream().anyMatch(x -> !x.getFinantialDocumentsSet().isEmpty());
    }

    public boolean isDeletable() {
        if (this.getDocumentNumberSeriesSet().stream().anyMatch(x -> x.isDeletable() == false)) {
            return false;
        }
        return true;
    }

    public boolean isDefaultSeries() {
        return super.getDefaultSeries();
    }

    @Atomic
    public void delete() {
        if (!isDeletable()) {
            throw new TreasuryDomainException("error.Series.cannot.delete");
        }

        setBennu(null);
        for (DocumentNumberSeries ser : getDocumentNumberSeriesSet()) {
            removeDocumentNumberSeries(ser);
            ser.delete();
        }
        setFinantialInstitution(null);

        deleteDomainObject();
    }

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

    protected static Stream<Series> findDefault(final FinantialInstitution finantialInstitution) {
        return find(finantialInstitution).stream().filter(s -> s.isDefaultSeries());
    }

    public static Optional<Series> findUniqueDefault(final FinantialInstitution finantialInstitution) {
        return findDefault(finantialInstitution).findFirst();
    }

    @Atomic
    public static Series create(final FinantialInstitution finantialInstitution, final String code, final LocalizedString name,
            final boolean externSeries, final boolean certificated, final boolean legacy, final boolean defaultSeries) {
        return new Series(finantialInstitution, code, name, externSeries, certificated, legacy, defaultSeries);

    }

}
