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

import java.math.BigDecimal;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.fenixedu.bennu.core.domain.Bennu;
import org.fenixedu.treasury.domain.debt.DebtAccount;
import org.fenixedu.treasury.domain.exceptions.TreasuryDomainException;
import org.fenixedu.treasury.domain.settings.TreasurySettings;
import org.joda.time.DateTime;

import pt.ist.fenixframework.Atomic;

import com.google.common.collect.Sets;

public abstract class FinantialDocument extends FinantialDocument_Base {

    protected FinantialDocument() {

        super();
        setBennu(Bennu.getInstance());
        setState(FinantialDocumentStateType.PREPARING);
    }

    protected void init(final DebtAccount debtAccount, final DocumentNumberSeries documentNumberSeries,
            final DateTime documentDate) {

        setDebtAccount(debtAccount);
        setFinantialDocumentType(documentNumberSeries.getFinantialDocumentType());
        setDocumentNumberSeries(documentNumberSeries);
        setDocumentNumber(String.valueOf(documentNumberSeries.getSequenceNumberAndIncrement()));
        setDocumentDate(documentDate);
        setCurrency(TreasurySettings.getInstance().getDefaultCurrency());
        
        checkRules();
    }

    protected void checkRules() {
        if (getDebtAccount() == null) {
            throw new TreasuryDomainException("error.FinantialDocument.debtAccount.required");
        }

        if (getFinantialDocumentType() == null) {
            throw new TreasuryDomainException("error.FinantialDocument.finantialDocumentType.required");
        }

        if (getDocumentNumberSeries() == null) {
            throw new TreasuryDomainException("error.FinantialDocument.documentNumber.required");
        }

        if (getDocumentDate() == null) {
            throw new TreasuryDomainException("error.FinantialDocument.documentDate.required");
        }

        if (getDocumentDueDate() == null) {
            throw new TreasuryDomainException("error.FinantialDocument.documentDueDate.required");
        }
        
        if(getCurrency() == null) {
            throw new TreasuryDomainException("error.FinantialDocument.currency.required");
        }
    }

    public String getUiDocumentNumber() {
        return String.format("%s %s/%s", this.getDocumentNumberSeries().getFinantialDocumentType()
                .getDocumentNumberSeriesPrefix(), this.getDocumentNumberSeries().getSeries().getCode(), this.getDocumentNumber());
    }

    public BigDecimal getTotalAmount() {
        return BigDecimal.ZERO;
    }

    public BigDecimal getTotalNetAmount() {
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

    public static Stream<? extends FinantialDocument> findAll() {
        return Bennu.getInstance().getFinantialDocumentsSet().stream();
    }

    public static Stream<? extends FinantialDocument> find(final FinantialDocumentType finantialDocumentType) {
        return findAll().filter(i -> finantialDocumentType.equals(i.getFinantialDocumentType()));
    }

    public static Stream<? extends FinantialDocument> find(final DocumentNumberSeries documentNumberSeries) {
        return findAll().filter(x -> x.getDocumentNumberSeries() == documentNumberSeries);
    }

    public static Stream<? extends FinantialDocument> find(final FinantialDocumentType finantialDocumentType,
            final DocumentNumberSeries documentNumberSeries) {

        return findAll()
                .filter(x -> x.getDocumentNumberSeries() == documentNumberSeries
                        && x.getFinantialDocumentType() == finantialDocumentType);
    }

    public Boolean getClosed() {
        return this.getState().equals(FinantialDocumentStateType.CLOSED);
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

    public BigDecimal getOpenAmount() {
        // TODO Auto-generated method stub
        return getTotalAmount();
    }

}
