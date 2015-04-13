package org.fenixedu.treasury.util;

import org.fenixedu.commons.i18n.LocalizedString;

public class LocalizedStringUtil {

    public static boolean isEmpty(final LocalizedString localizedString) {
        return localizedString == null || localizedString.getLocales().isEmpty();
    }

    public static boolean isEqualToAnyLocale(final LocalizedString localizedString, final String value) {
        return localizedString.getLocales().stream().filter(l -> localizedString.getContent(l).equals(value)).count() > 0;
    }
    
    public static boolean isEqualToAnyLocaleIgnoreCase(final LocalizedString localizedString, final String value) {
        return localizedString.getLocales().stream().filter(l -> localizedString.getContent(l).equalsIgnoreCase(value)).count() > 0;
    }
    
}
