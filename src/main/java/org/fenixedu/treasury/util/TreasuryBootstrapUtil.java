package org.fenixedu.treasury.util;

import java.util.Locale;

import org.fenixedu.commons.i18n.LocalizedString;
import org.fenixedu.treasury.domain.Currency;
import org.fenixedu.treasury.domain.FiscalCountryRegion;
import org.fenixedu.treasury.domain.PaymentMethod;
import org.fenixedu.treasury.domain.ProductGroup;
import org.fenixedu.treasury.domain.VatExemptionReason;
import org.fenixedu.treasury.domain.VatType;

import pt.ist.fenixframework.Atomic;

public class TreasuryBootstrapUtil {

    public static void InitializeDomain() {
        //HACK: This should be done elsewhere. 
        VatType.initializeVatType();
        VatExemptionReason.initializeVatExemption();
        FiscalCountryRegion.initializeFiscalRegion();
        PaymentMethod.initializePaymentMethod();
        Currency.initializeCurrency();
    }
}
