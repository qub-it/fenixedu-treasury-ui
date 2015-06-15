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
package org.fenixedu.treasury.services.integration.erp.dto;

import java.io.Serializable;
import java.math.BigDecimal;

import org.fenixedu.bennu.IBean;

public class InterestRequestValueOuptut implements IBean, Serializable {

    private static final long serialVersionUID = 1L;

    private BigDecimal interestAmount;

    private String description;

    private byte[] interestDocumentsContent;

    public InterestRequestValueOuptut() {
        setInterestAmount(BigDecimal.ZERO);
        setDescription(new String());
    }

    public InterestRequestValueOuptut(BigDecimal interestAmount, String description) {
        this.setInterestAmount(interestAmount);
        this.setDescription(description);
    }

    public BigDecimal getInterestAmount() {
        return interestAmount;
    }

    public void setInterestAmount(BigDecimal interestAmount) {
        this.interestAmount = interestAmount;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public byte[] getInterestDocumentsContent() {
        return interestDocumentsContent;
    }

    public void setInterestDocumentsContent(byte[] interestDocumentsContent) {
        this.interestDocumentsContent = interestDocumentsContent;
    }

}
