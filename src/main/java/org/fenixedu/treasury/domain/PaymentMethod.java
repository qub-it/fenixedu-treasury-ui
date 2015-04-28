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
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.apache.commons.lang.StringUtils;
import org.fenixedu.bennu.core.domain.Bennu;
import org.fenixedu.bennu.core.i18n.BundleUtil;
import org.fenixedu.commons.i18n.LocalizedString;
import org.fenixedu.treasury.domain.exceptions.TreasuryDomainException;
import org.fenixedu.treasury.util.Constants;
import org.fenixedu.treasury.util.LocalizedStringUtil;

import pt.ist.fenixframework.Atomic;

public class PaymentMethod extends PaymentMethod_Base {

    protected PaymentMethod() {
        super();
        setBennu(Bennu.getInstance());
    }

    protected PaymentMethod(final String code, final LocalizedString name) {
        this();
        setCode(code);
        setName(name);

        checkRules();
    }

    private void checkRules() {
        if (LocalizedStringUtil.isTrimmedEmpty(getCode())) {
            throw new TreasuryDomainException("error.PaymentMethod.code.required");
        }

        if (LocalizedStringUtil.isTrimmedEmpty(getName())) {
            throw new TreasuryDomainException("error.PaymentMethod.name.required");
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
        return true;
    }

    @Atomic
    public void delete() {
        if (!isDeletable()) {
            throw new TreasuryDomainException("error.PaymentMethod.cannot.delete");
        }

        setBennu(null);

        deleteDomainObject();
    }

    // @formatter: off
    /************
     * SERVICES *
     ************/
    // @formatter: on

    
	@Atomic
	public static void InitializePaymentMethod()
	{
		if (PaymentMethod.findAll().count() == 0)
		{
			PaymentMethod.create("MON", new LocalizedString(Locale.getDefault(),BundleUtil.getString(Constants.BUNDLE, "label.PaymentMethod.MON")));
			PaymentMethod.create("WTR", new LocalizedString(Locale.getDefault(),BundleUtil.getString(Constants.BUNDLE, "label.PaymentMethod.WTR")));
			PaymentMethod.create("ELE", new LocalizedString(Locale.getDefault(),BundleUtil.getString(Constants.BUNDLE, "label.PaymentMethod.ELE")));
			PaymentMethod.create("CCR", new LocalizedString(Locale.getDefault(),BundleUtil.getString(Constants.BUNDLE, "label.PaymentMethod.CCR")));
		}
	}


    public static Stream<PaymentMethod> findAll() {
        return Bennu.getInstance().getPaymentMethodsSet().stream();
    }

    public static PaymentMethod findByCode(final String code) {
        PaymentMethod result = null;

        for (final PaymentMethod it : findAll().collect(Collectors.toList())) {
            if (!it.getCode().equalsIgnoreCase(code)) {
                continue;
            }

            if (result != null) {
                throw new TreasuryDomainException("error.PaymentMethod.duplicated.code");
            }

            result = it;
        }

        return result;
    }

    public static PaymentMethod findByName(final String name) {
        PaymentMethod result = null;

        for (final PaymentMethod it : findAll().collect(Collectors.toList())) {

            if (!LocalizedStringUtil.isEqualToAnyLocaleIgnoreCase(it.getName(), name)) {
                continue;
            }

            if (result != null) {
                throw new TreasuryDomainException("error.PaymentMethod.duplicated.name");
            }

            result = it;
        }

        return result;
    }

    @Atomic
    public static PaymentMethod create(final String code, final LocalizedString name) {
        return new PaymentMethod(code, name);
    }

}
