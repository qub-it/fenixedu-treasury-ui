package org.fenixedu.treasury.domain.event;

import java.util.Set;

import org.fenixedu.bennu.core.domain.Bennu;
import org.fenixedu.treasury.domain.Product;
import org.fenixedu.treasury.domain.exceptions.TreasuryDomainException;

import pt.ist.fenixframework.Atomic;

public abstract class TreasuryEvent extends TreasuryEvent_Base {

    protected TreasuryEvent() {
        super();
        setBennu(Bennu.getInstance());
    }

    protected TreasuryEvent(final Product product) {
        this();
        setProduct(product);

        checkRules();
    }

    private void checkRules() {
        if(getProduct() == null) {
            throw new TreasuryDomainException("error.TreasuryEvent.product.required");
        }
    }
    
    public boolean isDeletable() {
        return true;
    }

    @Atomic
    public void delete() {
        if (!isDeletable()) {
            throw new TreasuryDomainException("error.TreasuryEvent.cannot.delete");
        }

        setBennu(null);

        deleteDomainObject();
    }

    // @formatter: off
    /************
     * SERVICES *
     ************/
    // @formatter: on

    public static Set<TreasuryEvent> readAll() {
        return Bennu.getInstance().getTreasuryEventsSet();
    }

}
