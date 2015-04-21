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
package org.fenixedu.treasury.domain.paymentCodes;

import java.util.stream.Stream;

import org.fenixedu.bennu.core.domain.Bennu;
import org.fenixedu.bennu.core.domain.User;
import org.fenixedu.treasury.domain.exceptions.TreasuryDomainException;

import pt.ist.fenixframework.Atomic;

public class SibsReportFile extends SibsReportFile_Base {

    protected SibsReportFile() {
        super();
        setBennu(Bennu.getInstance());
    }

    protected void init(final org.joda.time.LocalDate whenProcessedBySibs, final java.math.BigDecimal transactionsTotalAmount,
            final java.math.BigDecimal totalCost) {
        setWhenProcessedBySibs(whenProcessedBySibs);
        setTransactionsTotalAmount(transactionsTotalAmount);
        setTotalCost(totalCost);
        checkRules();
    }

    private void checkRules() {
        //
        //CHANGE_ME add more busines validations
        //

        //CHANGE_ME In order to validate UNIQUE restrictions
        //if (findByWhenProcessedBySibs(getWhenProcessedBySibs().count()>1)
        //{
        //	throw new TreasuryDomainException("error.SibsReportFile.whenProcessedBySibs.duplicated");
        //}	
        //if (findByTransactionsTotalAmount(getTransactionsTotalAmount().count()>1)
        //{
        //	throw new TreasuryDomainException("error.SibsReportFile.transactionsTotalAmount.duplicated");
        //}	
        //if (findByTotalCost(getTotalCost().count()>1)
        //{
        //	throw new TreasuryDomainException("error.SibsReportFile.totalCost.duplicated");
        //}	
    }

    @Atomic
    public void edit(final org.joda.time.LocalDate whenProcessedBySibs, final java.math.BigDecimal transactionsTotalAmount,
            final java.math.BigDecimal totalCost) {
        setWhenProcessedBySibs(whenProcessedBySibs);
        setTransactionsTotalAmount(transactionsTotalAmount);
        setTotalCost(totalCost);
        checkRules();
    }

    public boolean isDeletable() {
        return true;
    }

    @Override
    @Atomic
    public void delete() {
        if (!isDeletable()) {
            throw new TreasuryDomainException("error.SibsReportFile.cannot.delete");
        }

        setBennu(null);

        deleteDomainObject();
    }

    @Atomic
    public static SibsReportFile create(final org.joda.time.LocalDate whenProcessedBySibs,
            final java.math.BigDecimal transactionsTotalAmount, final java.math.BigDecimal totalCost) {
        SibsReportFile sibsReportFile = new SibsReportFile();
        sibsReportFile.init(whenProcessedBySibs, transactionsTotalAmount, totalCost);
        return sibsReportFile;
    }

    // @formatter: off
    /************
     * SERVICES *
     ************/
    // @formatter: on

    public static Stream<SibsReportFile> findAll() {
        return Bennu.getInstance().getSibsReportFilesSet().stream();
    }

    public static Stream<SibsReportFile> findByBennu(final Bennu bennu) {
        return findAll().filter(i -> bennu.equals(i.getBennu()));
    }

    public static Stream<SibsReportFile> findByWhenProcessedBySibs(final org.joda.time.LocalDate whenProcessedBySibs) {
        return findAll().filter(i -> whenProcessedBySibs.equals(i.getWhenProcessedBySibs()));
    }

    public static Stream<SibsReportFile> findByTransactionsTotalAmount(final java.math.BigDecimal transactionsTotalAmount) {
        return findAll().filter(i -> transactionsTotalAmount.equals(i.getTransactionsTotalAmount()));
    }

    public static Stream<SibsReportFile> findByTotalCost(final java.math.BigDecimal totalCost) {
        return findAll().filter(i -> totalCost.equals(i.getTotalCost()));
    }

    @Override
    public boolean isAccessible(User arg0) {
        // TODO Auto-generated method stub
        return false;
    }

}
