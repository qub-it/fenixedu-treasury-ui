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
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Stream;

import org.fenixedu.treasury.domain.FinantialInstitution;
import org.fenixedu.treasury.domain.debt.DebtAccount;
import org.fenixedu.treasury.domain.exceptions.TreasuryDomainException;
import org.joda.time.DateTime;

import pt.ist.fenixframework.Atomic;

import com.google.common.base.Splitter;
import com.google.common.collect.Lists;

public abstract class Invoice extends Invoice_Base {

    protected Invoice() {
        super();
    }

    @Override
    protected void checkRules() {
        //check if all invoiceEntries are for the same debtaccount of invoice
        for (FinantialDocumentEntry entry : getFinantialDocumentEntriesSet()) {
            InvoiceEntry invoiceEntry = (InvoiceEntry) entry;
            if (!invoiceEntry.getDebtAccount().equals(this.getDebtAccount())) {
                throw new TreasuryDomainException("error.Invoice.debtaccount.mismatch.invoiceentries.debtaccount");
            }
        }
        // TODO Auto-generated method stub
        super.checkRules();
    }

    @Override
    protected void init(final DebtAccount debtAccount, final DocumentNumberSeries documentNumberSeries,
            final DateTime documentDate) {
        super.init(debtAccount, documentNumberSeries, documentDate);
    }

    protected void init(final DebtAccount debtAccount, final DebtAccount payorDebtAccount,
            final DocumentNumberSeries documentNumberSeries, final DateTime documentDate) {
        super.init(debtAccount, documentNumberSeries, documentDate);

        if (payorDebtAccount == null) {
            throw new TreasuryDomainException("error.Invoice.payorDebtAccount.null");
        }

        setPayorDebtAccount(payorDebtAccount);
    }

    @Override
    public boolean isInvoice() {
        return true;
    }

    @Override
    public boolean isDeletable() {
        if (getState() != FinantialDocumentStateType.PREPARING) {
            return false;
        }
        return super.isDeletable();
    }

    @Atomic
    public void recalculateAmountValues() {
        for (FinantialDocumentEntry entry : this.getFinantialDocumentEntriesSet()) {
            ((InvoiceEntry) entry).recalculateAmountValues();
        }
    }

    @Override
    @Atomic
    public void delete(boolean deleteEntries) {
        if (!isDeletable()) {
            throw new TreasuryDomainException("error.Invoice.cannot.delete");
        }

        TreasuryDomainException.throwWhenDeleteBlocked(getDeletionBlockers());
        super.delete(deleteEntries);
    }

    // @formatter: off
    /************
     * SERVICES *
     ************/
    // @formatter: on

    public static Stream<? extends Invoice> findAll() {
        return FinantialDocument.findAll().filter(x -> x instanceof Invoice).map(Invoice.class::cast);
    }

    public static Stream<? extends Invoice> find(final DebtAccount debtAccount) {
        return Invoice.findAll().filter(x -> x.getDebtAccount() == debtAccount);
    }

//    public abstract BigDecimal getDebitAmount();
//
//    public abstract BigDecimal getCreditAmount();

    public BigDecimal getTotalVatAmount() {
        BigDecimal vat = BigDecimal.ZERO;
        for (FinantialDocumentEntry entry : getFinantialDocumentEntriesSet()) {
            vat = vat.add(((InvoiceEntry) entry).getVatAmount());
        }
        return vat;
    }

    public static Optional<Invoice> findByUIDocumentNumber(FinantialInstitution finantialInstitution,
            String invoiceReferenceNumber) {

        final String documentTypeCode;
        final String seriesPrefixCode;
        final String documentNumberCode;

        List<String> splitValues =
                Lists.newArrayList(Splitter.on(' ').trimResults().omitEmptyStrings().split(invoiceReferenceNumber));
        if (splitValues.size() != 2) {
            throw new TreasuryDomainException("");
        }

        documentTypeCode = splitValues.get(0);

        splitValues = Lists.newArrayList(Splitter.on('/').trimResults().omitEmptyStrings().split(splitValues.get(1)));

        if (splitValues.size() != 2) {
            throw new TreasuryDomainException("");
        }

        seriesPrefixCode = splitValues.get(0);
        documentNumberCode = splitValues.get(1);

        FinantialDocumentType documentType = FinantialDocumentType.findByCode(documentTypeCode);

        Series series = Series.findByCode(finantialInstitution, seriesPrefixCode);

        DocumentNumberSeries ser;
        DocumentNumberSeries documentNumberSeries =
                series.getDocumentNumberSeriesSet().stream().filter(dns -> dns.getFinantialDocumentType().equals(documentType))
                        .findFirst().orElse(null);

        if (documentNumberSeries == null) {
            throw new TreasuryDomainException("");
        }

        FinantialDocument document =
                documentNumberSeries.getFinantialDocumentsSet().stream()
                        .filter(x -> x.getDocumentNumber().equals(documentNumberCode)).findFirst().orElse(null);

        if (document instanceof Invoice) {
            return Optional.of((Invoice) document);
        } else {
            return Optional.of(null);
        }
    }

    public Set<SettlementEntry> getRelatedSettlementEntries() {
        Set<SettlementEntry> result = new HashSet<SettlementEntry>();
        for (FinantialDocumentEntry entry : this.getFinantialDocumentEntriesSet()) {
            InvoiceEntry invoiceEntry = (InvoiceEntry) entry;
            if (invoiceEntry.getSettlementEntriesSet().size() > 0) {
                for (SettlementEntry settlementEntry : invoiceEntry.getSettlementEntriesSet()) {
                    if (settlementEntry.getFinantialDocument().isClosed()) {
                        result.add(settlementEntry);
                    }
                }
            }
        }
        return result;
    }

    public InvoiceEntry getEntryInOrder(Integer lineNumber) {
        FinantialDocumentEntry entry =
                this.getFinantialDocumentEntriesSet().stream().filter(x -> x.getEntryOrder().equals(lineNumber)).findFirst()
                        .orElse(null);
        if (entry != null) {
            return (InvoiceEntry) entry;
        }
        return null;
    }
}
