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
import java.math.RoundingMode;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.fenixedu.bennu.core.domain.Bennu;
import org.fenixedu.bennu.core.i18n.BundleUtil;
import org.fenixedu.treasury.domain.Currency;
import org.fenixedu.treasury.domain.debt.DebtAccount;
import org.fenixedu.treasury.domain.exceptions.TreasuryDomainException;
import org.fenixedu.treasury.domain.settings.TreasurySettings;
import org.fenixedu.treasury.util.Constants;
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
        setDocumentNumber("000000000");
        setDocumentDate(documentDate);
        setDocumentDueDate(documentDate);
        setCurrency(debtAccount.getFinantialInstitution().getCurrency());
        setState(FinantialDocumentStateType.PREPARING);
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

        if (getCurrency() == null) {
            throw new TreasuryDomainException("error.FinantialDocument.currency.required");
        }
        if (!getDocumentNumberSeries().getSeries().getFinantialInstitution().equals(getDebtAccount().getFinantialInstitution())) {
            throw new TreasuryDomainException("error.FinantialDocument.finantialinstitution.mismatch");
        }

        if (getDocumentDueDate().isBefore(getDocumentDate())) {
            throw new TreasuryDomainException("error.FinantialDocument.documentDueDate.invalid");
        }

    }

    public String getUiDocumentNumber() {
        if (this.isClosed()) {
            return String.format("%s %s/%s", this.getDocumentNumberSeries().getFinantialDocumentType()
                    .getDocumentNumberSeriesPrefix(), this.getDocumentNumberSeries().getSeries().getCode(),
                    this.getDocumentNumber());
        } else {
            return String.format("%s %s/%s", this.getDocumentNumberSeries().getFinantialDocumentType()
                    .getDocumentNumberSeriesPrefix(), this.getDocumentNumberSeries().getSeries().getCode(), "000000000");

        }
    }

    public BigDecimal getTotalAmount() {
        BigDecimal amount = BigDecimal.ZERO;
        for (FinantialDocumentEntry entry : this.getFinantialDocumentEntriesSet()) {
            amount = amount.add(entry.getTotalAmount());
        }

        return Currency.getValueWithScale(amount);
    }

    public String getUiTotalAmount() {
        return this.getDebtAccount().getFinantialInstitution().getCurrency().getValueFor(this.getTotalAmount());
    }

    public BigDecimal getTotalNetAmount() {
        BigDecimal amount = BigDecimal.ZERO;
        for (FinantialDocumentEntry entry : this.getFinantialDocumentEntriesSet()) {
            amount.add(entry.getAmount());
        }

        return Currency.getValueWithScale(amount);
    }

    public String getUiTotalNetAmount() {
        return this.getDebtAccount().getFinantialInstitution().getCurrency().getValueFor(this.getTotalNetAmount());
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

    public boolean isAnnulled() {
        return this.getState().equals(FinantialDocumentStateType.ANNULED);
    }

    public boolean isPreparing() {
        return this.getState().equals(FinantialDocumentStateType.PREPARING);
    }

    @Atomic
    public void closeDocument() {
        if (this.isPreparing()) {
            this.setDocumentNumber("" + this.getDocumentNumberSeries().getSequenceNumberAndIncrement());
            setState(FinantialDocumentStateType.CLOSED);
        } else {
            throw new TreasuryDomainException(BundleUtil.getString(Constants.BUNDLE,
                    "error.FinantialDocumentState.invalid.state.change.request"));

        }
        checkRules();
    }

    @Atomic
    public void anullDocument() {
        if (this.isPreparing() || this.isClosed()) {
            if (Boolean.TRUE.booleanValue() == this.getDocumentNumberSeries().getSeries().getCertificated()) {
                throw new TreasuryDomainException("error.FinantialDocument.certificatedseris.cannot.anulled");
            }
            setState(FinantialDocumentStateType.ANNULED);
        }
        checkRules();
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
        return getTotalAmount();
    }

    @Atomic
    public void changeState(FinantialDocumentStateType newState) {

        if (this.getState() == FinantialDocumentStateType.PREPARING) {
            if (newState == FinantialDocumentStateType.ANNULED) {
                this.anullDocument();
            }

            if (newState == FinantialDocumentStateType.CLOSED) {
                this.closeDocument();
            }
        } else {
            throw new TreasuryDomainException(BundleUtil.getString(Constants.BUNDLE,
                    "error.FinantialDocumentState.invalid.state.change.request"));
        }
        checkRules();
    }

}
