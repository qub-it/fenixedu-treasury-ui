package org.fenixedu.treasury.domain.document;

import java.math.BigDecimal;
import java.util.Set;


import org.fenixedu.bennu.core.domain.Bennu;
import org.fenixedu.treasury.domain.debt.DebtAccount;
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

    protected void init(final DebtAccount debtAccount, final DocumentNumberSeries documentNumberSeries, final DateTime documentDate) {
        setDebtAccount(debtAccount);
        setFinantialDocumentType(documentNumberSeries.getFinantialDocumentType());
        setDocumentNumberSeries(documentNumberSeries);
        setDocumentNumber(String.valueOf(documentNumberSeries.getSequenceNumberAndIncrement()));
        setDocumentDate(documentDate);
        
        checkRules();
    }

    protected void checkRules() {
        if(getDebtAccount() == null) {
            throw new TreasuryDomainException("error.FinantialDocument.debtAccount.required");
        }
        
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

    public boolean isClosed() {
        return this.getState().isClosed();
    }
    
    public boolean isInvoice() {
        return false;
    }
    
    public boolean isDebitNote() {
        return false;
    }
    
    public boolean isCreditNote() {
        return false;
    }
    
    public boolean isSettlementNote() {
        return false;
    }
    
    public boolean isDeletable() {
        return true;
    }
    
    public void closeDocument() {
        setState(FinantialDocumentStateType.CLOSED);
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
