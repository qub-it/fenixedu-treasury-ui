package org.fenixedu.treasury.services.reports.dataproviders;

import org.fenixedu.treasury.domain.Customer;

import com.qubit.terra.docs.util.IDocumentFieldsData;
import com.qubit.terra.docs.util.IFieldsExporter;
import com.qubit.terra.docs.util.IReportDataProvider;

public class CustomerDataProvider extends AbstractDataProvider implements IReportDataProvider {

    protected static final String CUSTOMER_KEY = "customer";

    private Customer customer;

    public CustomerDataProvider(final Customer customer) {
        this.customer = customer;
        registerKey(CUSTOMER_KEY, CustomerDataProvider::handleCustomerKey);
    }

    private static Object handleCustomerKey(IReportDataProvider provider) {
        CustomerDataProvider invoiceProvider = (CustomerDataProvider) provider;
        return invoiceProvider.customer;
    }

    @Override
    public void registerFieldsAndImages(IDocumentFieldsData arg0) {
        // TODO Auto-generated method stub

    }

    @Override
    public void registerFieldsMetadata(IFieldsExporter arg0) {
        // TODO Auto-generated method stub

    }

}
