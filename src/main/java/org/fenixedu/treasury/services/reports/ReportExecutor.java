package org.fenixedu.treasury.services.reports;

import org.fenixedu.treasury.domain.document.Invoice;
import org.fenixedu.treasury.domain.document.TreasuryDocumentTemplate;
import org.fenixedu.treasury.services.reports.dataproviders.InvoiceDataProvider;

import com.qubit.terra.docs.core.DocumentGenerator;
import com.qubit.terra.docs.core.DocumentTemplateEngine;
import com.qubit.terra.docs.core.IDocumentTemplateService;

public class ReportExecutor {
    static {
        registerService();
    }

    public static synchronized void registerService() {
        IDocumentTemplateService service = new DocumentPrinterConfiguration();
        DocumentTemplateEngine.registerServiceImplementations(service);
    }

    //https://github.com/qub-it/fenixedu-qubdocs-reports/blob/master/src/main/java/org/fenixedu/academic/util/report/DocumentPrinter.java
    public static byte[] executTestReport(Invoice invoice) {

        TreasuryDocumentTemplate templateInEntity =
                TreasuryDocumentTemplate
                        .findByFinantialDocumentTypeAndFinantialEntity(invoice.getFinantialDocumentType(),
                                invoice.getDebtAccount().getFinantialInstitution().getFinantialEntitiesSet().iterator().next())
                        .filter(x -> x.isActive()).findFirst().orElse(null);

        if (templateInEntity != null) {
            DocumentGenerator generator2 = DocumentGenerator.create(templateInEntity, DocumentGenerator.ODT);

//        Invoice invoice = Invoice.findAll().findFirst().orElse(null);
            generator2.registerDataProvider(new InvoiceDataProvider(invoice));
            //... add more providers...
            byte[] outputReport = generator2.generateReport();

            return outputReport;
        } else {
            //HACK...
            DocumentGenerator create =
                    DocumentGenerator.create("C:\\Users\\Ricardo\\Downloads\\declaracaoInscricao_fl_pt.odt",
                            DocumentGenerator.ODT);
            create.registerDataProvider(new InvoiceDataProvider(invoice));
            byte[] outputReport = create.generateReport();

            return outputReport;
        }
    }
}
