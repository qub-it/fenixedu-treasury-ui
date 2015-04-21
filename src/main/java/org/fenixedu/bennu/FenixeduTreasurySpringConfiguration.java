package org.fenixedu.bennu;

import org.fenixedu.bennu.spring.BennuSpringModule;

@BennuSpringModule(basePackages = "org.fenixedu.treasury", bundles = "FenixeduTreasuryResources")
public class FenixeduTreasurySpringConfiguration {
    
    public static final String BUNDLE = "resources/FenixeduTreasuryResources";
}
