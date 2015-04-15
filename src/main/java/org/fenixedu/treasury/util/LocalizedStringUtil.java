package org.fenixedu.treasury.util;

import java.util.Locale;

import org.apache.commons.lang.StringUtils;
import org.fenixedu.commons.i18n.LocalizedString;

public class LocalizedStringUtil {

    public static boolean isTrimmedEmpty(final LocalizedString localizedString) {
        if(localizedString == null || localizedString.getLocales().isEmpty()) {
            return true;
        }
        
        boolean empty = true;
        for (final Locale locale : localizedString.getLocales()) {
            empty &= isTrimmedEmpty(localizedString.getContent(locale));
        }
        
        return empty;
    }

    public static boolean isTrimmedEmpty(final String content) {
        return content == null || StringUtils.isEmpty(StringUtils.trimToEmpty(content));
    }

    public static boolean isEqualToAnyLocale(final LocalizedString localizedString, final String value) {
        return localizedString.getLocales().stream().filter(l -> localizedString.getContent(l).equals(value)).count() > 0;
    }
    
    public static boolean isEqualToAnyLocaleIgnoreCase(final LocalizedString localizedString, final String value) {
        return localizedString.getLocales().stream().filter(l -> localizedString.getContent(l).equalsIgnoreCase(value)).count() > 0;
    }
    
}
