package org.fenixedu.treasury.domain;

import java.math.BigDecimal;
import java.util.Set;

import org.fenixedu.bennu.core.domain.Bennu;
import org.fenixedu.commons.i18n.LocalizedString;
import org.fenixedu.treasury.domain.exceptions.TreasuryDomainException;
import org.fenixedu.treasury.util.LocalizedStringUtil;
import org.joda.time.DateTime;
import org.joda.time.Interval;

import pt.ist.fenixframework.Atomic;

import com.google.common.collect.Sets;

public class Vat extends Vat_Base {

    protected Vat() {
        super();
        setBennu(Bennu.getInstance());
    }

    protected Vat(final VatType vatType, final FinantialInstitution finantialInstitution, final BigDecimal taxRate, final DateTime beginDate, final DateTime endDate) {
        this();
        setVatType(vatType);
        setFinantialInstitution(finantialInstitution);
        setTaxRate(taxRate);
        setBeginDate(beginDate);
        setEndDate(endDate);
        
        checkRules();
    }

    private void checkRules() {
        if(getTaxRate() == null) {
            throw new TreasuryDomainException("error.Vat.taxRate.required");
        }
        
        if(getFinantialInstitution() == null) {
            throw new TreasuryDomainException("error.Vat.finantialInstitution.required");
        }
        
        if(getTaxRate().compareTo(BigDecimal.ZERO) < 0) {
            throw new TreasuryDomainException("error.Vat.taxRate.cannot.be.negative");
        }
        
        if(getBeginDate() == null) {
            throw new TreasuryDomainException("error.Vat.beginDate.required");
        }
        
        if(!getEndDate().isAfter(getBeginDate())) {
            throw new TreasuryDomainException("error.Vat.endDate.end.date.must.be.after.begin.date");
        }
        
        checkPeriodsOverlap();
        
    }

    private void checkPeriodsOverlap() {
        for (final Vat vat : find(getVatType())) {
            if(vat == this) {
                continue;
            }
            
            final Interval vatInterval = new Interval(vat.getBeginDate(), vat.getEndDate());
            final Interval thisInterval = new Interval(getBeginDate(), getEndDate());
            
            if(vatInterval.overlaps(thisInterval)) {
                throw new TreasuryDomainException("error.Vat.date.interval.overlap.with.another");
            }
        }
    }

    @Atomic
    public void edit(final BigDecimal taxRate, final DateTime beginDate, final DateTime endDate) {
        setTaxRate(taxRate);
        setBeginDate(beginDate);
        setEndDate(endDate);
        
        checkRules();
    }

    public boolean isDeletable() {
        return true;
    }

    @Atomic
    public void delete() {
        if (!isDeletable()) {
            throw new TreasuryDomainException("error.Vat.cannot.delete");
        }

        setBennu(null);

        deleteDomainObject();
    }

    // @formatter: off
    /************
     * SERVICES *
     ************/
    // @formatter: on

    public static Set<Vat> readAll() {
        return Bennu.getInstance().getVatsSet();
    }
    
    public static Set<Vat> find(final VatType vatType) {
        final Set<Vat> result = Sets.newHashSet();

        for (final Vat it : readAll()) {
            if (it.getVatType() == vatType) {
                result.add(it);
            }
        }

        return result;
    }

    @Atomic
    public static Vat create(final VatType vatType, final FinantialInstitution finantialInstitution, final BigDecimal taxRate, final DateTime beginDate, final DateTime endDate) {
        return new Vat(vatType, finantialInstitution, taxRate, beginDate, endDate);
    }

}
