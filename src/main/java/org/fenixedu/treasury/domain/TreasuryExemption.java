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
package org.fenixedu.treasury.domain;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.Set;
import java.util.stream.Stream;

import org.apache.commons.lang.StringUtils;
import org.fenixedu.bennu.core.domain.Bennu;
import org.fenixedu.commons.i18n.LocalizedString;
import org.fenixedu.treasury.domain.debt.DebtAccount;
import org.fenixedu.treasury.domain.exceptions.TreasuryDomainException;
import org.fenixedu.treasury.util.LocalizedStringUtil;

import pt.ist.fenixframework.Atomic;

public class TreasuryExemption extends TreasuryExemption_Base {
    protected TreasuryExemption() {
        super();
        setBennu(Bennu.getInstance());
    }

    protected void init(final java.lang.String code, final org.fenixedu.commons.i18n.LocalizedString name,
            final java.math.BigDecimal discountRate) {
        setCode(code);
        setName(name);
        setDiscountRate(discountRate);
        checkRules();
    }

    private void checkRules() {
        //
        //CHANGE_ME add more busines validations
        //

        //CHANGE_ME In order to validate UNIQUE restrictions
        //if (findByCode(getCode().count()>1)
        //{
        //	throw new TreasuryDomainException("error.TreasuryExemption.code.duplicated");
        //}	
        //if (findByName(getName().count()>1)
        //{
        //	throw new TreasuryDomainException("error.TreasuryExemption.name.duplicated");
        //}	
        //if (findByDiscountRate(getDiscountRate().count()>1)
        //{
        //	throw new TreasuryDomainException("error.TreasuryExemption.discountRate.duplicated");
        //}	
    }

    @Atomic
    public void edit(final java.lang.String code, final org.fenixedu.commons.i18n.LocalizedString name,
            final java.math.BigDecimal discountRate) {
        setCode(code);
        setName(name);
        setDiscountRate(discountRate);
        checkRules();
    }

    public boolean isDeletable() {
        return true;
    }

    @Atomic
    public void delete() {
        if (!isDeletable()) {
            throw new TreasuryDomainException("error.TreasuryExemption.cannot.delete");
        }

        setBennu(null);

        deleteDomainObject();
    }

    @Atomic
    public static TreasuryExemption create(final java.lang.String code, final org.fenixedu.commons.i18n.LocalizedString name,
            final java.math.BigDecimal discountRate) {
        TreasuryExemption treasuryExemption = new TreasuryExemption();
        treasuryExemption.init(code, name, discountRate);
        return treasuryExemption;
    }

    // @formatter: off
    /************
     * SERVICES *
     ************/
    // @formatter: on

    public static Stream<TreasuryExemption> findAll() {
        return Bennu.getInstance().getTreasuryExemptionsSet().stream();
    }

    public static Stream<TreasuryExemption> findByCode(final java.lang.String code) {
        return findAll().filter(i -> code.equalsIgnoreCase(i.getCode()));
    }

    public static Stream<TreasuryExemption> findByName(final org.fenixedu.commons.i18n.LocalizedString name) {
        return findAll().filter(i -> name.equals(i.getName()));
    }

    public static Stream<TreasuryExemption> findByDiscountRate(final java.math.BigDecimal discountRate) {
        return findAll().filter(i -> discountRate.equals(i.getDiscountRate()));
    }

    public static Stream<TreasuryExemption> findByDebtAccount(DebtAccount debtAccount) {
        return findAll().filter(x -> x.getDebitEntry() != null && debtAccount.equals(x.getDebitEntry().getDebtAccount()));
    }

}
