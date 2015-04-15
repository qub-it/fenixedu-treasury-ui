package org.fenixedu.treasury.domain;

import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.fenixedu.bennu.core.domain.Bennu;
import org.fenixedu.commons.i18n.LocalizedString;
import org.fenixedu.treasury.domain.exceptions.TreasuryDomainException;
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

    public static Set<PaymentMethod> readAll() {
        return Bennu.getInstance().getPaymentMethodsSet();
    }

    public static PaymentMethod findByCode(final String code) {
        PaymentMethod result = null;

        for (final PaymentMethod it : readAll()) {
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

        for (final PaymentMethod it : readAll()) {

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
