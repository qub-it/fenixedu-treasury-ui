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

import org.fenixedu.treasury.domain.Currency;
import org.fenixedu.treasury.domain.debt.DebtAccount;
import org.fenixedu.treasury.domain.exceptions.TreasuryDomainException;
import org.joda.time.DateTime;

import pt.ist.fenixframework.Atomic;

public class DebitNote extends DebitNote_Base {

    protected DebitNote(final DebtAccount debtAccount, final DocumentNumberSeries documentNumberSeries,
            final DateTime documentDate) {
        super();

        this.init(debtAccount, documentNumberSeries, documentDate);
    }

    protected DebitNote(final DebtAccount debtAccount, final DebtAccount payorDebtAccount,
            final DocumentNumberSeries documentNumberSeries, final DateTime documentDate) {
        super();

        this.init(debtAccount, payorDebtAccount, documentNumberSeries, documentDate);
    }

    @Override
    public boolean isDebitNote() {
        return true;
    }

    @Override
    protected void checkRules() {
        if (!getDocumentNumberSeries().getFinantialDocumentType().getType().equals(FinantialDocumentTypeEnum.DEBIT_NOTE)) {
            throw new TreasuryDomainException("error.FinantialDocument.finantialDocumentType.invalid");
        }

        if (getPayorDebtAccount() != null
                && !(getPayorDebtAccount().getFinantialInstitution().equals(getDebtAccount().getFinantialInstitution()))) {
            throw new TreasuryDomainException("error.FinantialDocument.finantialinstitution.mismatch");
        }

        super.checkRules();
    }

    @Atomic
    public void delete() {
        super.delete();
    }

    @Override
    public BigDecimal getOpenAmount() {
        BigDecimal amount = BigDecimal.ZERO;
        for (DebitEntry entry : getDebitEntriesSet()) {
            amount = amount.add(entry.getOpenAmount());
        }
        return Currency.getValueWithScale(amount);
    }

    public Stream<? extends DebitEntry> getDebitEntries() {
        return DebitEntry.find(this);
    }

    public Set<? extends DebitEntry> getDebitEntriesSet() {
        return this.getDebitEntries().collect(Collectors. <DebitEntry> toSet());
    }

    public BigDecimal getDebitAmount() {
        return BigDecimal.ZERO;
    }

    public BigDecimal getCreditAmount() {
        return this.getTotalAmount();
    }

    // @formatter: off
    /************
     * SERVICES *
     ************/
    // @formatter: on

    public static Stream<DebitNote> findAll() {
        return FinantialDocument.findAll().filter(x -> x instanceof DebitNote).map(DebitNote.class::cast);
    }

    @Atomic
    public void edit(final DebtAccount payorDebtAccount, final org.joda.time.DateTime documentDueDate,
            final java.lang.String originDocumentNumber) {

        setPayorDebtAccount(payorDebtAccount);
        setDocumentDueDate(documentDueDate);
        setOriginDocumentNumber(originDocumentNumber);
        checkRules();
    }

    @Atomic
    public static DebitNote create(final DebtAccount debtAccount, final DocumentNumberSeries documentNumberSeries,
            final DateTime documentDate) {
        DebitNote note = new DebitNote(debtAccount, documentNumberSeries, documentDate);
        note.setFinantialDocumentType(FinantialDocumentType.findForDebitNote());
        note.setOriginDocumentNumber("");
        note.setDocumentDueDate(documentDate);
        note.checkRules();
        return note;
    }

    @Atomic
    public static DebitNote create(final DebtAccount debtAccount, final DebtAccount payorDebtAccount,
            final DocumentNumberSeries documentNumberSeries, final DateTime documentDate, final DateTime documentDueDate,
            final String originaNumber) {

        DebitNote note = new DebitNote(debtAccount, payorDebtAccount, documentNumberSeries, documentDate);
        note.setFinantialDocumentType(FinantialDocumentType.findForDebitNote());
        note.setOriginDocumentNumber(originaNumber);
        note.setDocumentDueDate(documentDueDate);
        note.checkRules();

        return note;
    }

    @Atomic
    public void closeDocument() {
        this.setState(FinantialDocumentStateType.CLOSED);
        this.checkRules();
    }
}
