package org.fenixedu.treasury.services.integration.dto;

import java.io.Serializable;
import java.math.BigDecimal;

import org.fenixedu.bennu.IBean;

public class InterestRequestValueOuptut implements IBean, Serializable {

    private static final long serialVersionUID = 1L;

    private BigDecimal interestAmount;

    private String description;

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

}
