package org.fenixedu.treasury.services.reports;

import org.fenixedu.treasury.domain.document.FinantialDocument;
import org.fenixedu.treasury.domain.document.Invoice;
import org.fenixedu.treasury.domain.document.SettlementNote;
import org.fenixedu.treasury.domain.document.TreasuryDocumentTemplate;
import org.fenixedu.treasury.domain.exceptions.TreasuryDomainException;
import org.fenixedu.treasury.services.reports.dataproviders.CustomerDataProvider;
import org.fenixedu.treasury.services.reports.dataproviders.DebtAccountDataProvider;
import org.fenixedu.treasury.services.reports.dataproviders.FinantialInstitutionDataProvider;
import org.fenixedu.treasury.services.reports.dataproviders.InvoiceDataProvider;
import org.fenixedu.treasury.services.reports.dataproviders.SettlementNoteDataProvider;
import org.fenixedu.treasury.services.reports.helpers.DateHelper;
import org.fenixedu.treasury.services.reports.helpers.EnumerationHelper;
import org.fenixedu.treasury.services.reports.helpers.LanguageHelper;
import org.fenixedu.treasury.services.reports.helpers.MoneyHelper;
import org.fenixedu.treasury.services.reports.helpers.NumbersHelper;
import org.fenixedu.treasury.services.reports.helpers.StringsHelper;

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

    private static void registerHelpers(DocumentGenerator generator) {
        generator.registerHelper("dates", new DateHelper());
        generator.registerHelper("lang", new LanguageHelper());
        generator.registerHelper("numbers", new NumbersHelper());
        generator.registerHelper("enumeration", new EnumerationHelper());
        generator.registerHelper("strings", new StringsHelper());
        generator.registerHelper("money", new MoneyHelper());
    }

    //https://github.com/qub-it/fenixedu-qubdocs-reports/blob/master/src/main/java/org/fenixedu/academic/util/report/DocumentPrinter.java
    public static byte[] printDocumentToODT(FinantialDocument document) {

        TreasuryDocumentTemplate templateInEntity =
                TreasuryDocumentTemplate
                        .findByFinantialDocumentTypeAndFinantialEntity(document.getFinantialDocumentType(),
                                document.getDebtAccount().getFinantialInstitution().getFinantialEntitiesSet().iterator().next())
                        .filter(x -> x.isActive()).findFirst().orElse(null);
        DocumentGenerator generator = null;

        if (templateInEntity != null) {
            generator = DocumentGenerator.create(templateInEntity, DocumentGenerator.ODT);

        } else {
            //HACK...
//            generator =
//                    DocumentGenerator.create(
//                            "F:\\O\\fenixedu\\fenixedu-treasury\\src\\main\\resources\\document_templates\\settlementNote.odt",
//                            DocumentGenerator.ODT);
            throw new TreasuryDomainException("error.ReportExecutor.document.template.not.available");
        }

        registerHelpers(generator);
        if (document.isInvoice()) {
            generator.registerDataProvider(new InvoiceDataProvider((Invoice) document));
        } else if (document.isSettlementNote()) {
            generator.registerDataProvider(new SettlementNoteDataProvider((SettlementNote) document));
        }
        generator.registerDataProvider(new DebtAccountDataProvider(document.getDebtAccount()));
        generator.registerDataProvider(new CustomerDataProvider(document.getDebtAccount().getCustomer()));
        generator.registerDataProvider(new FinantialInstitutionDataProvider(document.getDebtAccount().getFinantialInstitution()));

        //... add more providers...

        byte[] outputReport = generator.generateReport();

        return outputReport;
    }
}
