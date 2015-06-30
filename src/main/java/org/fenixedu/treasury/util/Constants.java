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
package org.fenixedu.treasury.util;

import java.math.BigDecimal;
import java.math.RoundingMode;

import org.fenixedu.bennu.FenixeduTreasurySpringConfiguration;
import org.joda.time.DateTime;
import org.joda.time.LocalDate;

public class Constants {

    private static final int SCALE = 20;

    public static final BigDecimal HUNDRED_PERCENT = new BigDecimal("100.00");

    public static final String BUNDLE = FenixeduTreasurySpringConfiguration.BUNDLE.replace('/', '.');
    
    // HACK: org.joda.time.Interval does not allow open end dates so use this date in the future
    public static final DateTime INFINITY_DATE = new DateTime().plusYears(500);
    
    // @formatter: off
    /**************
     * MATH UTILS *
     **************/
    // @formatter: on

    public static boolean isNegative(final BigDecimal value) {
        return !isZero(value) && !isPositive(value);
    }

    public static boolean isZero(final BigDecimal value) {
        return BigDecimal.ZERO.compareTo(value) == 0;
    }

    public static boolean isPositive(final BigDecimal value) {
        return BigDecimal.ZERO.compareTo(value) < 0;
    }

    public static boolean isGreaterThan(final BigDecimal v1, final BigDecimal v2) {
        return v1.compareTo(v2) > 0;
    }
    
    public static boolean isEqual(final BigDecimal v1, final BigDecimal v2) {
        return v1.compareTo(v2) == 0;
    }
    
    public static boolean isLessThan(final BigDecimal v1, final BigDecimal v2) {
        return v1.compareTo(v2) < 0;
    }
    
    public static BigDecimal defaultScale(final BigDecimal v) {
        return v.setScale(20, RoundingMode.HALF_EVEN);
    }

    public static BigDecimal divide(final BigDecimal a, BigDecimal b) {
        return a.divide(b, SCALE, RoundingMode.HALF_EVEN);
    }
 
    
    // @formatter: off
    /**************
     * DATE UTILS *
     **************/
    // @formatter: on
    
    public static int numberOfDaysInYear(final int year) {
        
        if(new LocalDate(year, 1, 1).year().isLeap()) {
            return 366;
        }
        
        return 365;
    }

    public static LocalDate lastDayInYear(final int year) {
        return new LocalDate(year, 12, 31);
    }
    
    public static LocalDate firstDayInYear(final int year) {
        return new LocalDate(year, 1, 1);
    }
}
