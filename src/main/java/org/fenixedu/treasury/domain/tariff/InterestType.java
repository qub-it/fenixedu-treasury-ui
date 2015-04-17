package org.fenixedu.treasury.domain.tariff;

public enum InterestType {

    DAILY,
    MONTHLY,
    FIXED_AMOUNT;
    
    public boolean isDaily() {
        return this == DAILY;
    }
    
    public boolean isMonthly() {
        return this == MONTHLY;
    }
    
    public boolean isFixedAmount() {
        return this == FIXED_AMOUNT;
    }
    
}