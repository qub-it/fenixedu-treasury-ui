package org.fenixedu.treasury.services.integration.erp.ERPExternalServiceImplementation;

import org.fenixedu.treasury.services.integration.erp.IERPExporter;
import org.fenixedu.treasury.services.integration.erp.sap.test.SAPExporterTestQuantityDifferentOfOne;

public class SapExternalServiceForTestQuantityDifferentOfOne extends SAPExternalService {

    @Override
    public IERPExporter getERPExporter() {
        return new SAPExporterTestQuantityDifferentOfOne();
    }
    
}
