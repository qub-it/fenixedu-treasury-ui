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
import java.util.Collection;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.fenixedu.treasury.domain.Currency;
import org.fenixedu.treasury.domain.debt.DebtAccount;
import org.fenixedu.treasury.domain.exceptions.TreasuryDomainException;
import org.joda.time.DateTime;

import pt.ist.fenixframework.Atomic;

public class CreditNote extends CreditNote_Base {

    protected CreditNote(final DebtAccount debtAccount, final DocumentNumberSeries documentNumberSeries,
            final DateTime documentDate) {
        super();

        init(debtAccount, documentNumberSeries, documentDate);
        checkRules();
    }

    protected CreditNote(final DebtAccount debtAccount, final DebtAccount payorDebtAccount,
            final DocumentNumberSeries documentNumberSeries, final DateTime documentDate) {
        super();

        init(debtAccount, payorDebtAccount, documentNumberSeries, documentDate);
        checkRules();
    }

    @Override
    public boolean isCreditNote() {
        return true;
    }

    @Override
    protected void checkRules() {
        if (!getDocumentNumberSeries().getFinantialDocumentType().getType().equals(FinantialDocumentTypeEnum.CREDIT_NOTE)) {
            throw new TreasuryDomainException("error.CreditNote.finantialDocumentType.invalid");
        }

        // TODO Auto-generated method stub
        super.checkRules();
    }

    public boolean isDeletable() {
        return true;
    }

    @Atomic
    public void delete() {
        if (!isDeletable()) {
            throw new TreasuryDomainException("error.CreditNote.cannot.delete");
        }

        setBennu(null);
        deleteDomainObject();
    }

    // @formatter: off
    /************
     * SERVICES *
     ************/
    // @formatter: on

    public static Stream<? extends CreditNote> findAll() {
        return Invoice.findAll().filter(i -> i instanceof CreditNote).map(CreditNote.class::cast);
    }

    @Atomic
    public static CreditNote create(final DebtAccount debtAccount, final DocumentNumberSeries documentNumberSeries,
            final DateTime documentDate) {
        return new CreditNote(debtAccount, documentNumberSeries, documentDate);
    }

    @Atomic
    public static CreditNote create(final DebtAccount debtAccount, final DebtAccount payorDebtAccount,
            final DocumentNumberSeries documentNumberSeries, final DateTime documentDate) {
        return new CreditNote(debtAccount, payorDebtAccount, documentNumberSeries, documentDate);
    }

    @Atomic
    public void edit(final DebitNote debitNote, final DebtAccount payorDebtAccount,
            final FinantialDocumentType finantialDocumentType, final DebtAccount debtAccount,
            final DocumentNumberSeries documentNumberSeries, final Currency currency, final java.lang.String documentNumber,
            final org.joda.time.DateTime documentDate, final org.joda.time.DateTime documentDueDate,
            final java.lang.String originDocumentNumber,
            final org.fenixedu.treasury.domain.document.FinantialDocumentStateType state) {
        setDebitNote(debitNote);
        setPayorDebtAccount(payorDebtAccount);
        setFinantialDocumentType(finantialDocumentType);
        setDebtAccount(debtAccount);
        setDocumentNumberSeries(documentNumberSeries);
        setCurrency(currency);
        setDocumentNumber(documentNumber);
        setDocumentDate(documentDate);
        setDocumentDueDate(documentDueDate);
        setOriginDocumentNumber(originDocumentNumber);
        setState(state);
        checkRules();
    }

    @Override
    public BigDecimal getOpenAmount() {
        BigDecimal amount = BigDecimal.ZERO;
        for (CreditEntry entry : this.getFinantialDocumentEntriesSet().stream().map(CreditEntry.class::cast)
                .collect(Collectors.toList())) {
            amount = amount.add(entry.getOpenAmount());
        }
        return amount;
    }
}
