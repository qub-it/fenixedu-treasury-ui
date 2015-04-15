package org.fenixedu.treasury.domain.document;

import java.util.Set;
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

    public static Set<DocumentNumberSeries> readAll() {
        return Bennu.getInstance().getDocumentNumberSeriesSet();
    }

    public static DocumentNumberSeries find(final FinantialDocumentType finantialDocumentType, final Series series) {
        final Stream<DocumentNumberSeries> stream =
                finantialDocumentType.getDocumentNumberSeriesSet().stream().filter(dns -> dns.getSeries() == series);
        if (stream.count() > 1) {
            throw new TreasuryDomainException("error.DocumentNumberSeries.not.unique.in.finantialDocumentType.and.series");
        }

        return stream.findFirst().orElse(null);
    }

    @Atomic
    public static DocumentNumberSeries create(final FinantialDocumentType finantialDocumentType, final Series series) {
        return new DocumentNumberSeries(finantialDocumentType, series);
    }

}
