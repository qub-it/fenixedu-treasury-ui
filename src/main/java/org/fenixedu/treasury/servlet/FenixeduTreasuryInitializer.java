package org.fenixedu.treasury.servlet;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

import org.fenixedu.treasury.domain.Currency;
import org.fenixedu.treasury.domain.FiscalCountryRegion;
import org.fenixedu.treasury.domain.PaymentMethod;
import org.fenixedu.treasury.domain.ProductGroup;
import org.fenixedu.treasury.domain.VatExemptionReason;
import org.fenixedu.treasury.domain.VatType;
import org.fenixedu.treasury.util.TreasuryBootstrapUtil;

@WebListener
public class FenixeduTreasuryInitializer implements ServletContextListener {

    @Override
    public void contextInitialized(ServletContextEvent event) {

        TreasuryBootstrapUtil.InitializeDomain();
    }

    @Override
    public void contextDestroyed(ServletContextEvent event) {
    }
}