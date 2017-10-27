package org.fenixedu.treasury.util;

import java.util.List;

import org.fenixedu.treasury.domain.Customer;

import com.google.common.base.Strings;
import com.google.common.collect.Lists;

import eu.europa.ec.taxud.tin.algorithm.TINAlgorithm;
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
        return FiscalCodeValidation.isValidcontrib(countryCode, fiscalNumber);
    }

    private static boolean isValidcontrib(final String countryCode, final String contrib) {
        if (Strings.isNullOrEmpty(countryCode)) {
            return false;
        }

        if (Strings.isNullOrEmpty(contrib)) {
            return false;
        }

        if (!Constants.isDefaultCountry(countryCode) && Customer.DEFAULT_FISCAL_NUMBER.equals(contrib)) {
            return false;
        }
        
        if(Constants.isDefaultCountry(countryCode)) {
            boolean functionReturnValue = false;
            functionReturnValue = false;
            int i = 0;
            long checkDigit = 0;

            if (contrib.length() == 9) {
                int numericValue = Character.getNumericValue(contrib.charAt(0));
                if (contrib.charAt(0) == '1' || contrib.charAt(0) == '2' || contrib.charAt(0) == '5' || contrib.charAt(0) == '6'
                        || contrib.charAt(0) == '9') {
                    checkDigit = numericValue * 9;
                    for (i = 2; i <= 8; i++) {
                        checkDigit = checkDigit + (Character.getNumericValue(contrib.charAt(i - 1)) * (10 - i));
                    }
                    checkDigit = 11 - (checkDigit % 11);
                    if ((checkDigit >= 10))
                        checkDigit = 0;
                    if ((checkDigit == Character.getNumericValue(contrib.charAt(8))))
                        functionReturnValue = true;
                }
            }
            
            return functionReturnValue;
        }
        
        if(VALIDATED_COUNTRIES.contains(countryCode.toUpperCase())) {
            return TINValid.checkTIN(translateCountry(countryCode.toUpperCase()), contrib) == 0;
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
