/**
 * This file was created by Quorum Born IT <http://www.qub-it.com/> and its 
 * copyright terms are bind to the legal agreement regulating the FenixEdu@ULisboa 
 * software development project between Quorum Born IT and Serviços Partilhados da
 * Universidade de Lisboa:
 *  - Copyright © 2015 Quorum Born IT (until any Go-Live phase)
 *  - Copyright © 2015 Universidade de Lisboa (after any Go-Live phase)
 *
 * Contributors: anil.mamede@qub-it.com
 *
 * 
 * This file is part of FenixEdu QubDocs.
 *
 * FenixEdu QubDocs is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * FenixEdu QubDocs is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with FenixEdu QubDocs.  If not, see <http://www.gnu.org/licenses/>.
 */

package org.fenixedu.treasury.services.reports.helpers;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.text.StrSubstitutor;
import org.fenixedu.commons.StringNormalizer;
import org.fenixedu.commons.i18n.I18N;
import org.fenixedu.commons.i18n.LocalizedString;

import com.qubit.terra.docs.util.helpers.IDocumentHelper;

public class StringsHelper extends StringUtils implements IDocumentHelper {

    static public String SINGLE_SPACE = " ";

    public static String trimToEmpty(final Object input) {
        return input == null ? EMPTY : trimToEmpty(input.toString());
    }

    public static String trimToNull(final Object input) {
        return input == null ? null : trimToNull(input.toString());
    }

    public static boolean equalsTrimmed(final String o1, final String o2) {
        return equals(trimToEmpty(o1), trimToEmpty(o2));
    }

    public static boolean equalsTrimmedIgnoreCase(final String o1, final String o2) {
        return equalsIgnoreCase(trimToEmpty(o1), trimToEmpty(o2));
    }

    public static boolean equals(final LocalizedString o1, final LocalizedString o2, final Locale locale) {
        boolean result = o1 != null && o2 != null;

        if (result) {
            result = equals(normalize(o1.getContent(locale)), normalize(o2.getContent(locale)));
        }

        return result;
    }

    public static String normalize(String string) {
        String result = null;

        if (StringUtils.isNotBlank(string)) {
            String spacesReplacedString = removeDuplicateSpaces(string.trim());
            result = StringNormalizer.normalize(spacesReplacedString).toLowerCase();
        }

        return result;
    }

    protected static String removeDuplicateSpaces(String string) {
        Pattern pattern = Pattern.compile("\\s+");
        Matcher matcher = pattern.matcher(string);
        return matcher.replaceAll(" ");
    }

    public static LocalizedString getI18N(final String defaultContent, final String english) {
        LocalizedString result = new LocalizedString();

        if (StringUtils.isNotBlank(defaultContent)) {
            result = result.with(I18N.getLocale(), defaultContent.trim());
        }
        if (StringUtils.isNotBlank(english)) {
            result = result.with(new Locale("en", "GB"), english.trim());
        }

        return result;
    }

    public static LocalizedString capitalize(final LocalizedString i18nString) {
        LocalizedString result = new LocalizedString();
        for (Locale locale : i18nString.getLocales()) {
            result = result.with(locale, StringUtils.capitalize(i18nString.getContent(locale)));
        }

        return result;
    }

    public static LocalizedString joinLS(final Collection<LocalizedString> collection, final String separator) {
        Set<Locale> locales = new HashSet<Locale>();
        for (LocalizedString iter : collection) {
            locales.addAll(iter.getLocales());
        }

        LocalizedString result = new LocalizedString();
        for (Locale locale : locales) {
            Collection<String> messages = new ArrayList<String>();
            for (LocalizedString i18nString : collection) {
                messages.add(i18nString.getContent(locale));
            }

            result = result.with(locale, join(messages, separator));
        }

        return result;
    }

    public static boolean isEmpty(LocalizedString i18nString) {

        if (i18nString == null || i18nString.isEmpty()) {
            return true;
        }

        if (i18nString.getLocales().isEmpty()) {
            return true;
        }

        for (final Locale locale : i18nString.getLocales()) {
            if (StringUtils.isNotEmpty(i18nString.getContent(locale))) {
                return false;
            }
        }

        return true;

    }

/**
     * Allows variables replacement with configurable prefix and suffix
     * 
     * Usage example using '<' for prefix and '>' for suffix:
     *  - Template: Hello <name>
     *  - Variables: {name=User}
     *  - Result: Hello User
     * @return
     */
    public static String replaceVariables(final String template, final String prefix, final String suffix,
            final Map<String, String> variables) {
        return new StrSubstitutor(variables, prefix, suffix).replace(template);
    }

}