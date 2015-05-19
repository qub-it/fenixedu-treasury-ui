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
import java.util.stream.Stream;

import org.fenixedu.treasury.domain.exceptions.TreasuryDomainException;

import pt.ist.fenixframework.Atomic;

public class SettlementEntry extends SettlementEntry_Base {

    protected SettlementEntry(final FinantialDocument finantialDocument, final BigDecimal amount, final String description) {
        init(finantialDocument, amount, description);
    }

    @Override
    protected void init(final FinantialDocument finantialDocument, final FinantialEntryType finantialEntryType,
            final BigDecimal amount, String description) {
        throw new RuntimeException("error.SettlementEntry.use.init.without.finantialEntryType");
    }

    protected void init(final FinantialDocument finantialDocument, final BigDecimal amount, String description) {
        super.init(finantialDocument, FinantialEntryType.SETTLEMENT_ENTRY, amount, description);

        checkRules();
    }

    @Override
    public void checkRules() {
        super.checkRules();

        if (!(getFinantialDocument() instanceof SettlementNote)) {
            throw new TreasuryDomainException("error.SettlementEntry.finantialDocument.not.settlement.note.type");
        }
    }

    // @formatter: off
    /************
     * SERVICES *
     ************/
    // @formatter: on

    public static Stream<SettlementEntry> findAll() {
        return (Stream<SettlementEntry>) FinantialDocumentEntry.findAll().filter(f -> f instanceof SettlementEntry);
    }

    @Atomic
    public SettlementEntry create(final FinantialDocument finantialDocument, final BigDecimal amount, final String description) {
        return new SettlementEntry(finantialDocument, amount, description);
    }

    @Override
    public BigDecimal getTotalAmount() {
        return this.getAmount();
    }

}
