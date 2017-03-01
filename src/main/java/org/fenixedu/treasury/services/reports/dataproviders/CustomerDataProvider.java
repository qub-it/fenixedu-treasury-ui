package org.fenixedu.treasury.services.reports.dataproviders;

import org.fenixedu.treasury.domain.Customer;

import com.qubit.terra.docs.util.IDocumentFieldsData;
import com.qubit.terra.docs.util.IReportDataProvider;

public class CustomerDataProvider extends AbstractDataProvider implements IReportDataProvider {

    private Customer customer;
    
    public CustomerDataProvider(final Customer customer, final String key) {
        this.customer = customer;
        registerKey(key, CustomerDataProvider::handleCustomerKey);
    }

    private static Object handleCustomerKey(IReportDataProvider provider) {
        CustomerDataProvider invoiceProvider = (CustomerDataProvider) provider;
        return invoiceProvider.customer;
    }

    @Override
    public void registerFieldsAndImages(IDocumentFieldsData arg0) {
        // TODO Auto-generated method stub

    }

}
