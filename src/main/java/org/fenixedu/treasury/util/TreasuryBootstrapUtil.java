package org.fenixedu.treasury.util;

import org.fenixedu.bennu.core.groups.Group;
import org.fenixedu.treasury.domain.Currency;
import org.fenixedu.treasury.domain.CustomerType;
import org.fenixedu.treasury.domain.FiscalCountryRegion;
import org.fenixedu.treasury.domain.PaymentMethod;
import org.fenixedu.treasury.domain.ProductGroup;
import org.fenixedu.treasury.domain.VatExemptionReason;
import org.fenixedu.treasury.domain.VatType;
import org.fenixedu.treasury.domain.document.FinantialDocumentType;

public class TreasuryBootstrapUtil {

    public static void InitializeDomain() {
        //HACK: This should be done elsewhere. 
        CustomerType.initializeCustomerType();
        ProductGroup.initializeProductGroup();
        VatType.initializeVatType();
        VatExemptionReason.initializeVatExemption();
        FiscalCountryRegion.initializeFiscalRegion();
        PaymentMethod.initializePaymentMethod();
        Currency.initializeCurrency();
        FinantialDocumentType.initializeFinantialDocumentType();
        checkTreasuryAuthorizations();
    }

    private static void checkTreasuryAuthorizations() {
        Group managersGroup = Group.parse("#treasuryManager");
        if (managersGroup == null) {

        }

    }
}
