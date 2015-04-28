package test.not.commit;

import org.fenixedu.bennu.scheduler.custom.CustomTask;
import org.fenixedu.treasury.domain.FinantialInstitution;
import org.fenixedu.treasury.domain.FiscalCountryRegion;

import pt.ist.fenixframework.FenixFramework;

public class RunCustomTask extends CustomTask {

	@Override
	public void runTask() throws Exception {
		FiscalCountryRegion region = FiscalCountryRegion.findAll().findFirst()
				.orElse(null);
		final FinantialInstitution institution = FinantialInstitution.create(
				region, "Instituicao Financeira", "999999991", "999999991",
				"Instituicao Financeira", "Instituicao Financeira", "R MORADA",
				"LISBOA", "1234-123", "PT");

		// AcademicTreasurySettings.getInstance().editEmolumentsProductGroup(ProductGroup.findByCode("EMOLUMENTOS"));
	}

}
