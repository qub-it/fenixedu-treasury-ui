package org.fenixedu.treasury.services.payments.paymentscodegenerator;

import java.math.BigDecimal;
import java.math.RoundingMode;

import com.google.common.base.Strings;

public class CheckDigitGenerator {

    public static String generateReferenceCodeWithCheckDigit(String entityCode, String referenceCode, BigDecimal amount) {
        String fullCode = entityCode + referenceCode;

        String amountString = amount.setScale(2, RoundingMode.HALF_EVEN).toPlainString().replace("\\.", "").replace(",", "");
        amountString = Strings.padStart(amountString, 8, '0');
        fullCode = fullCode + amountString;
        CheckISO7064Mod97_10 checkDigitGenerator = new CheckISO7064Mod97_10();
        int checkDigit = checkDigitGenerator.computeCheck(fullCode);
        return referenceCode + checkDigit;
    }

}
