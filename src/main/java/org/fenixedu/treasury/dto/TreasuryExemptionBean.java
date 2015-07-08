package org.fenixedu.treasury.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

import org.fenixedu.bennu.IBean;
import org.fenixedu.bennu.TupleDataSourceBean;
import org.fenixedu.treasury.domain.Product;
import org.fenixedu.treasury.domain.event.TreasuryEvent;
import org.fenixedu.treasury.domain.exemption.TreasuryExemptionType;

public class TreasuryExemptionBean implements IBean, Serializable {

    private static final long serialVersionUID = 1L;
    private TreasuryEvent treasuryEvent;

    private List<TupleDataSourceBean> treasuryExemptionTypes;
    private List<TupleDataSourceBean> products;
    private TreasuryExemptionType treasuryExemptionType;
    private Product product;
    private BigDecimal valuetoexempt;
    private String reason;

    public TreasuryExemptionBean() {
        this.setTreasuryExemptionTypes(TreasuryExemptionType.findAll().sorted(TreasuryExemptionType.COMPARE_BY_NAME)
                .collect(Collectors.toList()));

    }

    public TreasuryExemptionBean(TreasuryEvent treasuryEvent) {
        this();
        this.treasuryEvent = treasuryEvent;
        this.setProducts(treasuryEvent.getPossibleProductsToExempt().stream().sorted(Product.COMPARE_BY_NAME)
                .collect(Collectors.toList()));
    }

    public List<TupleDataSourceBean> getTreasuryExemptionTypes() {
        return treasuryExemptionTypes;
    }

    public TreasuryEvent getTreasuryEvent() {
        return treasuryEvent;
    }

    public void setTreasuryEvent(TreasuryEvent treasuryEvent) {
        this.treasuryEvent = treasuryEvent;
    }

    public void setTreasuryExemptionTypes(List<TreasuryExemptionType> treasuryExemptionTypes) {
        this.treasuryExemptionTypes = treasuryExemptionTypes.stream().map(treasuryExemptionType -> {
            TupleDataSourceBean tuple = new TupleDataSourceBean();
            tuple.setText(treasuryExemptionType.getName().getContent());
            tuple.setId(treasuryExemptionType.getExternalId());
            return tuple;
        }).collect(Collectors.toList());
    }

    public List<TupleDataSourceBean> getProducts() {
        return products;
    }

    public void setProducts(List<Product> products) {
        this.products = products.stream().map(product -> {
            TupleDataSourceBean tuple = new TupleDataSourceBean();
            tuple.setText(product.getName().getContent());
            tuple.setId(product.getExternalId());
            return tuple;
        }).collect(Collectors.toList());
    }

    public TreasuryExemptionType getTreasuryExemptionType() {
        return treasuryExemptionType;
    }

    public void setTreasuryExemptionType(TreasuryExemptionType treasuryExemptionType) {
        this.treasuryExemptionType = treasuryExemptionType;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public BigDecimal getValuetoexempt() {
        return valuetoexempt;
    }

    public void setValuetoexempt(BigDecimal valuetoexempt) {
        this.valuetoexempt = valuetoexempt;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

}
