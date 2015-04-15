package org.fenixedu.treasury.domain.document;

import java.math.BigDecimal;
import java.util.Set;


import org.fenixedu.bennu.core.domain.Bennu;
import org.fenixedu.treasury.domain.exceptions.TreasuryDomainException;
import org.joda.time.DateTime;

import pt.ist.fenixframework.Atomic;

import com.google.common.collect.Sets;

public abstract class FinantialDocument extends FinantialDocument_Base {

    protected FinantialDocument() {

        super();
        setBennu(Bennu.getInstance());
        setState(FinantialDocumentStateType.TEMPORARY);
    }

    protected void init(final DocumentNumberSeries documentNumberSeries, final DateTime documentDate) {
        setFinantialDocumentType(documentNumberSeries.getFinantialDocumentType());
        setDocumentNumberSeries(documentNumberSeries);
        setDocumentNumber(String.valueOf(documentNumberSeries.getSequenceNumberAndIncrement()));
        setDocumentDate(documentDate);
        
        checkRules();
    }

    protected void checkRules() {
        if (getFinantialDocumentType() == null) {
            throw new TreasuryDomainException("error.FinantialDocument.finantialDocumentType.required");
        }

        if (getDocumentNumberSeries() == null) {
            throw new TreasuryDomainException("error.FinantialDocument.documentNumber.required");
        }
        
        if(getDocumentDate() == null) {
            throw new TreasuryDomainException("error.FinantialDocument.documentDate.required");
        }
        
        if(getDocumentDueDate() == null) {
            throw new TreasuryDomainException("error.FinantialDocument.documentDueDate.required");
        }
    }

    public String getUiDocumentNumber() {
        return String.format("%s %s/%s", this.getDocumentNumberSeries().getFinantialDocumentType()
                .getDocumentNumberSeriesPrefix(), this.getDocumentNumberSeries().getSeries().getCode(), this.getDocumentNumber());
    }

    public BigDecimal getTotalValue() {
        return BigDecimal.ZERO;
    }

    public BigDecimal getTotalNetValue() {
        return BigDecimal.ZERO;
    }

    public boolean isDeletable() {
        return true;
    }
    
    public void closeDocument() {
        setState(FinantialDocumentStateType.FINALIZED);
    }

    @Atomic
    public void delete() {
        if (!isDeletable()) {
            throw new TreasuryDomainException("error.FinantialDocument.cannot.delete");
        }

        setBennu(null);

        deleteDomainObject();
    }

    // @formatter: off
    /************
     * SERVICES *
     ************/
    // @formatter: on

    public static Set<? extends FinantialDocument> readAll() {
        return Bennu.getInstance().getFinantialDocumentsSet();
    }

    public static Set<FinantialDocument> find(final FinantialDocumentType finantialDocumentType) {
        final Set<FinantialDocument> result = Sets.newHashSet();

        for (final FinantialDocument it : readAll()) {
            if (it.getFinantialDocumentType() == finantialDocumentType) {
                result.add(it);
            }
        }

        return result;
    }

    public static Set<FinantialDocument> find(final DocumentNumberSeries documentNumberSeries) {
        final Set<FinantialDocument> result = Sets.newHashSet();

        for (final FinantialDocument it : readAll()) {
            if (it.getDocumentNumberSeries() == documentNumberSeries) {
                result.add(it);
            }
        }

        return result;
    }

    public static Set<FinantialDocument> find(final FinantialDocumentType FinantialDocumentType,
            final DocumentNumberSeries documentNumberSeries) {
        final Set<FinantialDocument> result = Sets.newHashSet();

        for (final FinantialDocument it : readAll()) {
            if (it.getFinantialDocumentType() != FinantialDocumentType) {
                continue;
            }

            if (it.getDocumentNumberSeries() != documentNumberSeries) {
                continue;
            }

            result.add(it);
        }

        return result;
    }

	public Boolean getClosed() {
		return this.getState().equals(FinantialDocumentState.CLOSED);
	}

	public DateTime getWhenCreated() {
		// TODO Auto-generated method stub
		return null;
	}

	public String getUserChanged() {
		// TODO Auto-generated method stub
		return null;
	}

	public String getUserCreated() {
		// TODO Auto-generated method stub
		return null;
	}
	

}
