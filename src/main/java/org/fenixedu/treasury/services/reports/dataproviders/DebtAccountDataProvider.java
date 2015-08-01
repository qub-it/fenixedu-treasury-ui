package org.fenixedu.treasury.services.reports.dataproviders;

import org.fenixedu.treasury.domain.debt.DebtAccount;

import com.qubit.terra.docs.util.IDocumentFieldsData;
import com.qubit.terra.docs.util.IFieldsExporter;
import com.qubit.terra.docs.util.IReportDataProvider;

public class DebtAccountDataProvider extends AbstractDataProvider implements IReportDataProvider {

    protected static final String DEBT_ACCOUNT_KEY = "debtAccount";

    private DebtAccount debtAccount;

    public DebtAccountDataProvider(final DebtAccount debtAccount) {
        this.debtAccount = debtAccount;
        registerKey(DEBT_ACCOUNT_KEY, DebtAccountDataProvider::handleDebtAccountKey);
    }

    private static Object handleDebtAccountKey(IReportDataProvider provider) {
        DebtAccountDataProvider invoiceProvider = (DebtAccountDataProvider) provider;
        return invoiceProvider.debtAccount;
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
