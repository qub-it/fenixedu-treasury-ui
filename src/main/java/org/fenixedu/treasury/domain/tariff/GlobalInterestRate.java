/**
 * This file was created by Quorum Born IT <http://www.qub-it.com/> and its 
 * copyright terms are bind to the legal agreement regulating the FenixEdu@ULisboa 
 * software development project between Quorum Born IT and ServiÃ§os Partilhados da
 * Universidade de Lisboa:
 *  - Copyright Â© 2015 Quorum Born IT (until any Go-Live phase)
 *  - Copyright Â© 2015 Universidade de Lisboa (after any Go-Live phase)
 *
 * Contributors: ricardo.pedro@qub-it.com
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

package org.fenixedu.treasury.domain.tariff;

import java.util.Optional;
import java.util.stream.Stream;

import org.fenixedu.bennu.core.domain.Bennu;
import org.fenixedu.treasury.domain.exceptions.TreasuryDomainException;

import pt.ist.fenixframework.Atomic;

public class GlobalInterestRate extends GlobalInterestRate_Base {

    protected GlobalInterestRate() {
        super();
        setBennu(Bennu.getInstance());
    }

    protected void init(final int year, final org.fenixedu.commons.i18n.LocalizedString description,
            final java.math.BigDecimal rate) {
        setYear(year);
        setDescription(description);
        setRate(rate);
        checkRules();
    }

    private void checkRules() {
        //
        //CHANGE_ME add more busines validations
        //

        //CHANGE_ME In order to validate UNIQUE restrictions
        if (findByYear(getYear()).count() > 1) {
            throw new TreasuryDomainException("error.GlobalInterestRate.year.duplicated");
        }
        //if (findByDescription(getDescription().count()>1)
        //{
        //	throw new TreasuryDomainException("error.GlobalInterestRate.description.duplicated");
        //}	
        //if (findByRate(getRate().count()>1)
        //{
        //	throw new TreasuryDomainException("error.GlobalInterestRate.rate.duplicated");
        //}	
    }

    @Atomic
    public void edit(final int year, final org.fenixedu.commons.i18n.LocalizedString description, final java.math.BigDecimal rate) {
        setYear(year);
        setDescription(description);
        setRate(rate);
        checkRules();
    }

    public boolean isDeletable() {
        return true;
    }

    @Atomic
    public void delete() {
        if (!isDeletable()) {
            throw new TreasuryDomainException("error.GlobalInterestRate.cannot.delete");
        }

        setBennu(null);

        deleteDomainObject();
    }

    @Atomic
    public static GlobalInterestRate create(final int year, final org.fenixedu.commons.i18n.LocalizedString description,
            final java.math.BigDecimal rate) {
        GlobalInterestRate globalInterestRate = new GlobalInterestRate();
        globalInterestRate.init(year, description, rate);
        return globalInterestRate;
    }

    // @formatter: off
    /************
     * SERVICES *
     ************/
    // @formatter: on

    public static Stream<GlobalInterestRate> findAll() {
        return Bennu.getInstance().getGlobalInterestRatesSet().stream();
    }

    public static Stream<GlobalInterestRate> findByYear(final int year) {
        return findAll().filter(i -> year == i.getYear());
    }

    public static Optional<GlobalInterestRate> findUniqueByYear(final int year) {
        return findByYear(year).findFirst();
    }

    public static Stream<GlobalInterestRate> findByDescription(final org.fenixedu.commons.i18n.LocalizedString description) {
        return findAll().filter(i -> description.equals(i.getDescription()));
    }

    public static Stream<GlobalInterestRate> findByRate(final java.math.BigDecimal rate) {
        return findAll().filter(i -> rate.equals(i.getRate()));
    }

}
