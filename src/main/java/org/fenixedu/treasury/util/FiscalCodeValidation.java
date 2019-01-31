package org.fenixedu.treasury.util;

import java.util.List;

import org.fenixedu.treasury.domain.Customer;

import com.google.common.base.Strings;
import com.google.common.collect.Lists;

import eu.europa.ec.taxud.tin.algorithm.TINValid;

public class FiscalCodeValidation {

    private static final List<String> VALIDATED_COUNTRIES = Lists.newArrayList(
            "PT", "AT", "BE", "BG", "CY", "DE", "DK", "EE", "ES", "FI", 
            "FR", "HR", "HU", "IE", "IT", "LT", "LU", "NL", "PL", "SE",
            "SI", "GB");
    
    public static boolean isValidationAppliedToFiscalCountry(final String countryCode) {
        return VALIDATED_COUNTRIES.contains(countryCode.toUpperCase());
    }
    
    public static boolean isValidFiscalNumber(final String countryCode, final String fiscalNumber) {
        if (Strings.isNullOrEmpty(countryCode)) {
            return false;
        }

        if (Strings.isNullOrEmpty(fiscalNumber)) {
            return false;
        }

        if (!TreasuryConstants.isDefaultCountry(countryCode) && Customer.DEFAULT_FISCAL_NUMBER.equals(fiscalNumber)) {
            return false;
        }
        
        if(VALIDATED_COUNTRIES.contains(countryCode.toUpperCase())) {
            return TINValid.checkTIN(translateCountry(countryCode.toUpperCase()), fiscalNumber) == 0;
        }

        return true;
    }

    private static String translateCountry(String countryCode) {
        if("GB".equals(countryCode)) {
            return "UK";
        }
        
        return countryCode;
    }
    
}
