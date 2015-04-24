package org.fenixedu.treasury.servlet;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

import org.fenixedu.treasury.util.TreasuryBootstrapUtil;

@WebListener
public class FenixeduTreasuryInitializer implements ServletContextListener {

        @Override
        public void contextInitialized(ServletContextEvent event) {
        	TreasuryBootstrapUtil.InitializeVatType();
        	TreasuryBootstrapUtil.InitializeVatExemption();
        	TreasuryBootstrapUtil.InitializeProductGroup();
        	TreasuryBootstrapUtil.InitializeFiscalRegion();
        }

        @Override
        public void contextDestroyed(ServletContextEvent event){
        }
}