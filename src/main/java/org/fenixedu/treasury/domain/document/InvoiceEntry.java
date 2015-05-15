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
import java.util.Optional;
import java.util.stream.Stream;

import org.fenixedu.treasury.domain.Product;
import org.fenixedu.treasury.domain.Vat;
import org.fenixedu.treasury.domain.VatType;
import org.fenixedu.treasury.domain.debt.DebtAccount;
import org.fenixedu.treasury.domain.exceptions.TreasuryDomainException;
import org.fenixedu.treasury.domain.settings.TreasurySettings;
import org.joda.time.DateTime;

public abstract class InvoiceEntry extends InvoiceEntry_Base {

    @Override
    protected void init(FinantialDocument finantialDocument, FinantialEntryType finantialEntryType, BigDecimal amount) {
        throw new RuntimeException("error.InvoiceEntry.use.init.with.product");
    }

    protected void init(final FinantialDocument finantialDocument, final DebtAccount debtAccount, final Product product,
            final FinantialEntryType finantialEntryType, final VatType vatType, final BigDecimal amount) {
        super.init(finantialDocument, finantialEntryType, amount);

        this.setCurrency(TreasurySettings.getInstance().getDefaultCurrency());
        this.setDebtAccount(debtAccount);
        this.setProduct(product);
        this.setCurrency(this.getDebtAccount().getFinantialInstitution().getCurrency());
        this.setVat(Vat.findActiveUnique(vatType, debtAccount.getFinantialInstitution(), new DateTime()).orElse(null));
    }

    @Override
    protected void checkRules() {
        super.checkRules();

        if (getFinantialDocument() != null && !(getFinantialDocument() instanceof Invoice)) {
            throw new TreasuryDomainException("error.InvoiceEntry.finantialDocument.not.invoice.type");
        }

        if (getProduct() == null) {
            throw new TreasuryDomainException("error.InvoiceEntry.product.required");
        }

        if (getDebtAccount() == null) {
            throw new TreasuryDomainException("error.InvoiceEntry.debtAccount.required");
        }

        if (getCurrency() == null) {
            throw new TreasuryDomainException("error.InvoiceEntry.currency.required");
        }

        if (getVat() == null) {
            throw new TreasuryDomainException("error.InvoiceEntry.vat.required");
        }
    }

    public static Stream<? extends InvoiceEntry> findAll() {
        return FinantialDocumentEntry.findAll().filter(f -> f instanceof InvoiceEntry).map(InvoiceEntry.class::cast);
    }

    public boolean isPending() {
        BigDecimal totalAmount = this.getAmount();
        BigDecimal totalPayed = BigDecimal.ZERO;
        this.getSettlementEntriesSet().stream().map(x -> totalPayed.add(x.getAmount()));
        return !totalAmount.equals(totalPayed);
    }

}
