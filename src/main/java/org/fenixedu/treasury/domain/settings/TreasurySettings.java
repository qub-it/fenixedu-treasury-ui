package org.fenixedu.treasury.domain.settings;

import java.util.Optional;

import org.fenixedu.bennu.core.domain.Bennu;
import org.fenixedu.treasury.domain.Currency;
import org.fenixedu.treasury.domain.VatType;

import pt.ist.fenixframework.Atomic;

public class TreasurySettings extends TreasurySettings_Base {
    
    protected TreasurySettings() {
        super();
        setBennu(Bennu.getInstance());
    }

    @Atomic
    public void edit(final Currency defaultCurrency, final VatType defaultVatType) {
        setDefaultCurrency(defaultCurrency);
        setDefaultVatType(defaultVatType);
    }
    
    protected static Optional<TreasurySettings> findUnique() {
        return Bennu.getInstance().getTreasurySettingsSet().stream().findFirst();
    }
    
    @Atomic
    public synchronized static TreasurySettings getInstance() {
        if(!findUnique().isPresent()) {
            new TreasurySettings();
        }
        
        return findUnique().get();
    }
}
