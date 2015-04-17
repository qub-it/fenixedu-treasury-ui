package org.fenixedu.treasury.domain.tariff;

public enum DueDateCalculationType {
    FIXED_DATE,
    DAYS_AFTER_CREATION;
    
    public boolean isFixedDate() {
        return this == FIXED_DATE;
    }
    
    public boolean isDaysAfterCreation() {
        return this == DAYS_AFTER_CREATION;
    }
}