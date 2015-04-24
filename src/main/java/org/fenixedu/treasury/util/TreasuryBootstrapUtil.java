package org.fenixedu.treasury.util;

import java.util.Locale;

import org.fenixedu.commons.i18n.LocalizedString;
import org.fenixedu.treasury.domain.FiscalCountryRegion;
import org.fenixedu.treasury.domain.PaymentMethod;
import org.fenixedu.treasury.domain.ProductGroup;
import org.fenixedu.treasury.domain.VatExemptionReason;
import org.fenixedu.treasury.domain.VatType;

import pt.ist.fenixframework.Atomic;

public class TreasuryBootstrapUtil {
	
	@Atomic
	public static void InitializePaymentMethod()
	{
		PaymentMethod method = PaymentMethod.findByCode("MON");
	}
	
	@Atomic
	public static void InitializeVatType()
	{
		VatType reduced = VatType.findByCode("RED");
		if (reduced == null)
		{
			VatType.create("RED", new LocalizedString(Locale.getDefault(),"Reduced"));
		}

		VatType intermediate = VatType.findByCode("INT");
		if (intermediate == null)
		{
			VatType.create("INT", new LocalizedString(Locale.getDefault(),"Intermediate"));
		}

		VatType regular = VatType.findByCode("NOR");
		if (regular == null)
		{
			VatType.create("NOR", new LocalizedString(Locale.getDefault(),"Regular"));
		}

		VatType free = VatType.findByCode("ISE");
		if (free == null)
		{
			VatType.create("ISE", new LocalizedString(Locale.getDefault(),"Vat Free"));
		}

	}

	
	@Atomic
	public static void InitializeProductGroup()
	{
	}
	
	@Atomic
	public static void InitializeVatExemption()
	{
		String[] codes = new String[]{"M01","M02","M03","M04","M05","M06","M07","M08","M09","M10","M11","M12","M13","M14","M15","M16"};
		
		for (String code : codes)
		{
			VatExemptionReason reason = VatExemptionReason.findByCode(code);
			if (reason == null)
			{
				VatExemptionReason.create(code, new LocalizedString(Locale.getDefault(),code));
			}
		}
	}
	
	@Atomic
	public static void InitializeFiscalRegion()
	{
		FiscalCountryRegion pt = FiscalCountryRegion.findByRegionCode("PT");
		if (pt == null)
		{
			FiscalCountryRegion.create("PT", new LocalizedString(Locale.getDefault(),"Portuagal Mainland"));
		}
		FiscalCountryRegion pt_ma = FiscalCountryRegion.findByRegionCode("PT_MA");
		if (pt_ma == null)
		{
			FiscalCountryRegion.create("PT_MA", new LocalizedString(Locale.getDefault(),"Madeira"));
		}
		FiscalCountryRegion pt_ac = FiscalCountryRegion.findByRegionCode("PT_AZ");
		if (pt_ac == null)
		{
			FiscalCountryRegion.create("PT_AZ", new LocalizedString(Locale.getDefault(),"Azores"));
		}
	}
}
