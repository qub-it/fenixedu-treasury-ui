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
import java.util.stream.Stream;

import org.fenixedu.bennu.core.i18n.BundleUtil;
import org.fenixedu.treasury.domain.Product;
import org.fenixedu.treasury.domain.Vat;
import org.fenixedu.treasury.domain.debt.DebtAccount;
import org.fenixedu.treasury.domain.exceptions.TreasuryDomainException;
import org.fenixedu.treasury.util.Constants;
import org.joda.time.DateTime;

import pt.ist.fenixframework.Atomic;

public abstract class InvoiceEntry extends InvoiceEntry_Base {

    @Override
    protected void checkForDeletionBlockers(Collection<String> blockers) {
        super.checkForDeletionBlockers(blockers);

        if (getFinantialDocument() != null && getFinantialDocument().getState() != FinantialDocumentStateType.PREPARING) {
            blockers.add(BundleUtil.getString(Constants.BUNDLE, "error.invoiceentry.cannot.be.deleted.document.is.not.preparing"));
        }
    }

    public boolean isDebitNoteEntry() {
        return false;
    }

    public boolean isCreditNoteEntry() {
        return false;
    }

    @Override
    public void delete() {
        TreasuryDomainException.throwWhenDeleteBlocked(getDeletionBlockers());

        this.setCurrency(null);
        this.setDebtAccount(null);
        this.setVat(null);
        this.setProduct(null);
        super.delete();
    }

    @Override
    protected void init(FinantialDocument finantialDocument, FinantialEntryType finantialEntryType, BigDecimal amount,
            String description, DateTime entryDateTime) {
        throw new RuntimeException("error.InvoiceEntry.use.init.with.product");
    }

    protected void init(final FinantialDocument finantialDocument, final DebtAccount debtAccount, final Product product,
            final FinantialEntryType finantialEntryType, final Vat vat, final BigDecimal amount, String description,
            BigDecimal quantity, DateTime entryDateTime) {
        super.init(finantialDocument, finantialEntryType, amount, description, entryDateTime);
        this.setQuantity(quantity);
        this.setCurrency(debtAccount.getFinantialInstitution().getCurrency());
        this.setDebtAccount(debtAccount);
        this.setProduct(product);
        this.setVat(vat);
        this.setVatRate(vat.getTaxRate());
    }

    @Override
    protected void checkRules() {
        super.checkRules();

        if (getQuantity() == null) {
            throw new TreasuryDomainException("error.FinantialDocumentEntry.quantity.required");
        }

        if (getQuantity().compareTo(BigDecimal.ZERO) <= 0) {
            throw new TreasuryDomainException("error.FinantialDocumentEntry.quantity.less.than.zero");
        }

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

        if (getFinantialDocument() != null && getFinantialDocument().getDebtAccount() != this.getDebtAccount()) {
            throw new TreasuryDomainException("error.InvoiceEntry.invalidDebtAccount");
        }

        if (checkAmountValues() == false) {
            throw new TreasuryDomainException("error.InvoiceEntry.amount.invalid.consistency");
        }

    }

    protected boolean checkAmountValues() {
        //only check after the first initializations
        if (getNetAmount() != null && getVatAmount() != null && getAmountWithVat() != null) {
            BigDecimal netAmount = getCurrency().getValueWithScale(getQuantity().multiply(getAmount()));
            BigDecimal vatAmount = getCurrency().getValueWithScale(getNetAmount().multiply(getVatRate()));
            BigDecimal amountWithVat = getCurrency().getValueWithScale(getNetAmount().multiply(BigDecimal.ONE.add(getVatRate())));

            //Compare the re-calculated values with the original ones
            return netAmount.compareTo(getNetAmount()) == 0 && vatAmount.compareTo(getVatAmount()) == 0
                    && amountWithVat.compareTo(getTotalAmount()) == 0;
        }
        return true;
    }

    @Atomic
    protected void realculateAmountValues() {
        setNetAmount(getCurrency().getValueWithScale(getQuantity().multiply(getAmount())));
        setVatAmount(getCurrency().getValueWithScale(getNetAmount().multiply(getVatRate())));
        setAmountWithVat(getCurrency().getValueWithScale(getNetAmount().multiply(BigDecimal.ONE.add(getVatRate()))));
    }

    public static Stream<? extends InvoiceEntry> findAll() {
        return FinantialDocumentEntry.findAll().filter(f -> f instanceof InvoiceEntry).map(InvoiceEntry.class::cast);
    }

    public boolean isPendingForPayment() {
        if (this.getFinantialDocument() != null && this.getFinantialDocument().getState().isAnnuled()) {
            return false;
        }
        BigDecimal totalAmount = this.getAmount();
        BigDecimal totalPayed = BigDecimal.ZERO;
        this.getSettlementEntriesSet().stream().map(x -> totalPayed.add(x.getAmount()));
        return !totalAmount.equals(totalPayed);
    }

    @Override
    public BigDecimal getTotalAmount() {
        return this.getAmountWithVat();
    }

    public abstract BigDecimal getOpenAmount();

}
