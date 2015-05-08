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

import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.util.Map;
import java.util.stream.Stream;

import org.fenixedu.treasury.domain.Product;
import org.fenixedu.treasury.domain.VatType;
import org.fenixedu.treasury.domain.debt.DebtAccount;
import org.fenixedu.treasury.domain.event.TreasuryEvent;
import org.fenixedu.treasury.domain.exceptions.TreasuryDomainException;
import org.joda.time.LocalDate;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

public class DebitEntry extends DebitEntry_Base {

    protected DebitEntry(final DebtAccount debtAccount, final TreasuryEvent treasuryEvent, final VatType vatType,
            final BigDecimal amount, final LocalDate dueDate, final Map<String, String> propertiesMap) {
        init(debtAccount, treasuryEvent, vatType, amount, dueDate, propertiesMap);
    }

    @Override
    protected void init(final FinantialDocument finantialDocument, final DebtAccount debtAccount, final Product product,
            final FinantialEntryType finantialEntryType, final VatType vatType, final BigDecimal amount) {
        throw new RuntimeException("error.CreditEntry.use.init.without.finantialEntryType");
    }

    protected void init(final DebtAccount debtAccount, final TreasuryEvent treasuryEvent, final VatType vatType,
            final BigDecimal amount, final LocalDate dueDate, final Map<String, String> propertiesMap) {
        super.init(null, debtAccount, treasuryEvent.getProduct(), FinantialEntryType.DEBIT_ENTRY, vatType, amount);

        setDebtAccount(debtAccount);
        setTreasuryEvent(treasuryEvent);
        setDueDate(dueDate);

        setPropertiesJsonMap(propertiesMapToJson(propertiesMap));
        
        checkRules();
    }

    @Override
    protected void checkRules() {
        super.checkRules();

        if (getFinantialDocument() != null && !(getFinantialDocument() instanceof DebitNote)) {
            throw new TreasuryDomainException("error.DebitEntry.finantialDocument.not.debit.entry.type");
        }

        if (getDebtAccount() == null) {
            throw new TreasuryDomainException("error.DebitEntry.debtAccount.required");
        }

        if (getDueDate() == null) {
            throw new TreasuryDomainException("error.DebitEntry.dueDate.required");
        }
    }

    protected String propertiesMapToJson(final Map<String, String> propertiesMap) {
        final GsonBuilder builder = new GsonBuilder();
        
        final Gson gson = builder.create();
        final Type stringStringMapType = new TypeToken<Map<String, String>>(){}.getType();
        
        return gson.toJson(propertiesMap, stringStringMapType);
    }
    
    @Override
    public boolean isFinantialDocumentRequired() {
        return false;
    }

    public boolean isEventAnnuled() {
        return getEventAnnuled();
    }

    public BigDecimal getOpenAmount() {
        final BigDecimal openAmount =
                this.getAmount().subtract(
                        getSettlementEntriesSet().stream().map((x) -> x.getAmount()).reduce((x, y) -> x.add(y)).get());

        return isPositive(openAmount) ? openAmount : BigDecimal.ZERO;
    }

    // @formatter: off
    /************
     * SERVICES *
     ************/
    // @formatter: on

    public static Stream<? extends DebitEntry> findAll() {
        return FinantialDocumentEntry.findAll().filter(f -> f instanceof DebitEntry).map(DebitEntry.class::cast);
    }

    public static Stream<? extends DebitEntry> find(final DebitNote debitNote) {
        return findAll().filter(d -> d.getFinantialDocument() == debitNote);
    }

    public static DebitEntry create(final DebtAccount debtAccount, final TreasuryEvent treasuryEvent, final VatType vatType,
            final BigDecimal amount, final LocalDate dueDate, final Map<String, String> propertiesMap) {
        return new DebitEntry(debtAccount, treasuryEvent, vatType, amount, dueDate, propertiesMap);
    }

}
