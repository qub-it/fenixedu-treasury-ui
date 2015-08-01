/**
 * This file was created by Quorum Born IT <http://www.qub-it.com/> and its 
 * copyright terms are bind to the legal agreement regulating the FenixEdu@ULisboa 
 * software development project between Quorum Born IT and Serviços Partilhados da
 * Universidade de Lisboa:
 *  - Copyright © 2015 Quorum Born IT (until any Go-Live phase)
 *  - Copyright © 2015 Universidade de Lisboa (after any Go-Live phase)
 *
 * Contributors: anil.mamede@qub-it.com
 *
 * 
 * This file is part of FenixEdu QubDocs.
 *
 * FenixEdu QubDocs is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * FenixEdu QubDocs is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with FenixEdu QubDocs.  If not, see <http://www.gnu.org/licenses/>.
 */

package org.fenixedu.treasury.services.reports.helpers;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Collection;

import com.qubit.terra.docs.core.DocumentGenerator;
import com.qubit.terra.docs.util.helpers.IDocumentHelper;

public class NumbersHelper implements IDocumentHelper {

    public String onePlace(final BigDecimal value) {
        if (value == null) {
            return DocumentGenerator.DASH;
        }
        return (value.scale() > 1 ? value.setScale(1, RoundingMode.HALF_EVEN) : value).toPlainString();
    }

    public String twoPlaces(final BigDecimal value) {
        if (value == null) {
            return DocumentGenerator.DASH;
        }
        return (value.scale() > 2 ? value.setScale(2, RoundingMode.HALF_EVEN) : value).toPlainString();
    }

    public Integer count(final Collection collection) {
        return collection.size();
    }
}