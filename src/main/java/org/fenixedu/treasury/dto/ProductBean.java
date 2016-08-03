package org.fenixedu.treasury.dto;

import java.util.Set;

import org.fenixedu.commons.i18n.LocalizedString;
import org.fenixedu.treasury.domain.FinantialInstitution;
import org.fenixedu.treasury.domain.ProductGroup;
import org.fenixedu.treasury.domain.VatExemptionReason;
import org.fenixedu.treasury.domain.VatType;

import com.google.common.collect.Sets;

public class ProductBean {
    
    private ProductGroup productGroup;
    private String code;
    private LocalizedString description;
    private LocalizedString unitOfMeasure;
    private boolean active;
    private boolean legacy;
    private int tuitionInstallmentOrder;
    private VatType vatType;
    private VatExemptionReason vatExemptionReason;
    private Set<FinantialInstitution> finantialInstitutionsSet = Sets.newHashSet();
    
    public ProductBean() {
    }
    
    // @formatter:off
    /* *****************
     * GETTERS & SETTERS
     * *****************
     */
    // @formatter:on
    
    public ProductGroup getProductGroup() {
        return productGroup;
    }
    
    public void setProductGroup(ProductGroup productGroup) {
        this.productGroup = productGroup;
    }
    
    public String getCode() {
        return code;
    }
    
    public void setCode(String code) {
        this.code = code;
    }
    
    public LocalizedString getDescription() {
        return description;
    }
    
    public void setDescription(LocalizedString description) {
        this.description = description;
    }
    
    public LocalizedString getUnitOfMeasure() {
        return unitOfMeasure;
    }
    
    public void setUnitOfMeasure(LocalizedString unitOfMeasure) {
        this.unitOfMeasure = unitOfMeasure;
    }
    
    public boolean isActive() {
        return active;
    }
    
    public void setActive(boolean active) {
        this.active = active;
    }
    
    public boolean isLegacy() {
        return legacy;
    }
    
    public void setLegacy(boolean legacy) {
        this.legacy = legacy;
    }
    
    public int getTuitionInstallmentOrder() {
        return tuitionInstallmentOrder;
    }
    
    public void setTuitionInstallmentOrder(int tuitionInstallmentOrder) {
        this.tuitionInstallmentOrder = tuitionInstallmentOrder;
    }
    
    public VatType getVatType() {
        return vatType;
    }
    
    public void setVatType(VatType vatType) {
        this.vatType = vatType;
    }
    
    public VatExemptionReason getVatExemptionReason() {
        return vatExemptionReason;
    }
    
    public void setVatExemptionReason(VatExemptionReason vatExemptionReason) {
        this.vatExemptionReason = vatExemptionReason;
    }
    
    public Set<FinantialInstitution> getFinantialInstitutionsSet() {
        return finantialInstitutionsSet;
    }
    
    public void setFinantialInstitutionsSet(Set<FinantialInstitution> finantialInstitutionsSet) {
        this.finantialInstitutionsSet = finantialInstitutionsSet;
    }
}
