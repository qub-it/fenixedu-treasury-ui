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
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.fenixedu.bennu.core.domain.Bennu;
import org.fenixedu.commons.i18n.LocalizedString;
import org.fenixedu.treasury.domain.exceptions.TreasuryDomainException;
import org.fenixedu.treasury.util.LocalizedStringUtil;

import pt.ist.fenixframework.Atomic;

public class DocumentNumberSeries extends DocumentNumberSeries_Base {

    protected DocumentNumberSeries() {
        super();
        setBennu(Bennu.getInstance());
    }

    protected DocumentNumberSeries(final FinantialDocumentType finantialDocumentType, final Series series) {
        this();
        setFinantialDocumentType(finantialDocumentType);
        setSeries(series);

        checkRules();
    }

    private void checkRules() {
        if (getFinantialDocumentType() == null) {
            throw new TreasuryDomainException("error.DocumentNumberSeries.finantialDocumentType.required");
        }

        if (getSeries() == null) {
            throw new TreasuryDomainException("error.DocumentNumberSeries.series.required");
        }

        // Try to find it and throw 
        find(getFinantialDocumentType(), getSeries());
    }

    public int getSequenceNumber() {
        return getCounter();
    }

    public int getSequenceNumberAndIncrement() {
        int count = getCounter();
        setCounter(count++);

        return count;
    }

    public boolean isDeletable() {
        return true;
    }

    @Atomic
    public void delete() {
        if (!isDeletable()) {
            throw new TreasuryDomainException("error.DocumentNumberSeries.cannot.delete");
        }

        setBennu(null);

        deleteDomainObject();
    }

    // @formatter: off
    /************
     * SERVICES *
     ************/
    // @formatter: on

    public static Stream<DocumentNumberSeries> findAll() {
        return Bennu.getInstance().getDocumentNumberSeriesSet().stream();
    }

    public static DocumentNumberSeries find(final FinantialDocumentType finantialDocumentType, final Series series) {
        final Set<DocumentNumberSeries> result =
                finantialDocumentType.getDocumentNumberSeriesSet().stream().filter(dns -> dns.getSeries() == series)
                        .collect(Collectors.toSet());
        if (result.size() > 1) {
            throw new TreasuryDomainException("error.DocumentNumberSeries.not.unique.in.finantialDocumentType.and.series");
        }

        return result.stream().findFirst().orElse(null);
    }

    @Atomic
    public static DocumentNumberSeries create(final FinantialDocumentType finantialDocumentType, final Series series) {
        return new DocumentNumberSeries(finantialDocumentType, series);
    }

}
