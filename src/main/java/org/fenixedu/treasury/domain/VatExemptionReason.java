/**
 * This file was created by Quorum Born IT <http://www.qub-it.com/> and its 
 * copyright terms are bind to the legal agreement regulating the FenixEdu@ULisboa 
 * software development project between Quorum Born IT and Serviços Partilhados da
 * Universidade de Lisboa:
 *  - Copyright © 2015 Quorum Born IT (until any Go-Live phase)
 *  - Copyright © 2015 Universidade de Lisboa (after any Go-Live phase)
 *
 * Contributors: ricardo.pedro@qub-it.com, anil.mamede@qub-it.com
 * 
 *
 * 
 * This file is part of FenixEdu Treasury.
 *
 * FenixEdu Treasury is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * FenixEdu Treasury is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with FenixEdu Treasury.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.fenixedu.treasury.domain;

import java.util.Locale;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.fenixedu.bennu.core.domain.Bennu;
import org.fenixedu.commons.i18n.LocalizedString;
import org.fenixedu.treasury.domain.exceptions.TreasuryDomainException;
import org.fenixedu.treasury.util.LocalizedStringUtil;

import pt.ist.fenixframework.Atomic;

public class VatExemptionReason extends VatExemptionReason_Base {

    protected VatExemptionReason() {
        super();
        setBennu(Bennu.getInstance());
    }

    @Atomic
    public static void initializeVatExemption() {
        if (VatExemptionReason.findAll().count() == 0) {
            String[] codes =
                    new String[] { "M01", "M02", "M03", "M04", "M05", "M06", "M07", "M08", "M09", "M10", "M11", "M12", "M13",
                            "M14", "M15", "M16" };

            for (String code : codes) {
                VatExemptionReason.create(code, new LocalizedString(Locale.getDefault(), code));
            }
        }
    }

    protected VatExemptionReason(final String code, final LocalizedString name) {
        this();
        setCode(code);
        setName(name);

        checkRules();
    }

    private void checkRules() {
        if (LocalizedStringUtil.isTrimmedEmpty(getCode())) {
            throw new TreasuryDomainException("error.VatExemptionReason.code.required");
        }

        if (LocalizedStringUtil.isTrimmedEmpty(getName())) {
            throw new TreasuryDomainException("error.VatExemptionReason.name.required");
        }

        findByCode(getCode());
        getName().getLocales().stream().forEach(l -> findByName(getName().getContent(l)));
    }

    @Atomic
    public void edit(final String code, final LocalizedString name) {
        setCode(code);
        setName(name);

        checkRules();
    }

    public boolean isDeletable() {
        return getVatsSet().isEmpty();
    }

    @Atomic
    public void delete() {
        if (!isDeletable()) {
            throw new TreasuryDomainException("error.VatExemptionReason.cannot.delete");
        }

        setBennu(null);

        deleteDomainObject();
    }

    public static Stream<VatExemptionReason> findAll() {
        return Bennu.getInstance().getVatExemptionReasonsSet().stream();
    }

    public static VatExemptionReason findByCode(final String code) {
        VatExemptionReason result = null;

        for (final VatExemptionReason it : findAll().collect(Collectors.toList())) {
            if (!it.getCode().equalsIgnoreCase(code)) {
                continue;
            }

            if (result != null) {
                throw new TreasuryDomainException("error.VatExemptionReason.duplicated.code");
            }

            result = it;
        }

        return result;
    }

    public static VatExemptionReason findByName(final String name) {
        VatExemptionReason result = null;

        for (final VatExemptionReason it : findAll().collect(Collectors.toList())) {

            if (!LocalizedStringUtil.isEqualToAnyLocaleIgnoreCase(it.getName(), name)) {
                continue;
            }

            if (result != null) {
                throw new TreasuryDomainException("error.VatExemptionReason.duplicated.name");
            }

            result = it;
        }

        return result;
    }

    @Atomic
    public static VatExemptionReason create(final String code, final LocalizedString name) {
        return new VatExemptionReason(code, name);
    }

}
