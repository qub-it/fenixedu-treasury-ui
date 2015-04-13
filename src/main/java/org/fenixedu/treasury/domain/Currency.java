package org.fenixedu.treasury.domain;

import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.fenixedu.bennu.core.domain.Bennu;
import org.fenixedu.commons.i18n.LocalizedString;
import org.fenixedu.treasury.domain.exceptions.TreasuryDomainException;
import org.fenixedu.treasury.util.LocalizedStringUtil;

import pt.ist.fenixframework.Atomic;

public class Currency extends Currency_Base {

    protected Currency() {
        super();
        setBennu(Bennu.getInstance());
    }

    protected Currency(final String code, final LocalizedString name, final String isoCode, final String symbol) {
        this();
        setCode(code);
        setName(name);
        setIsoCode(isoCode);
        setSymbol(symbol);

        checkRules();
    }

    private void checkRules() {
        if (StringUtils.isEmpty(getCode())) {
            throw new TreasuryDomainException("error.Currency.code.required");
        }

        if (LocalizedStringUtil.isEmpty(getName())) {
            throw new TreasuryDomainException("error.Currency.name.required");
        }
        
        if(StringUtils.isEmpty(getIsoCode())) {
            throw new TreasuryDomainException("error.Currency.isoCode.required");
        }
        
        if(StringUtils.isEmpty(getSymbol())) {
            throw new TreasuryDomainException("error.Currency.symbol.required");
        }

        findByCode(getCode());
        getName().getLocales().stream().forEach(l -> findByName(getName().getContent(l)));
    }

    @Atomic
    public void edit(final String code, final LocalizedString name, final String isoCode, final String symbol) {
        setCode(code);
        setName(name);
        setIsoCode(isoCode);
        setSymbol(symbol);

        checkRules();
    }

    public boolean isDeletable() {
        return true;
    }

    @Atomic
    public void delete() {
        if (!isDeletable()) {
            throw new TreasuryDomainException("error.Currency.cannot.delete");
        }

        setBennu(null);

        deleteDomainObject();
    }

    // @formatter: off
    /************
     * SERVICES *
     ************/
    // @formatter: on

    public static Set<Currency> readAll() {
        return Bennu.getInstance().getCurrenciesSet();
    }

    public static Currency findByCode(final String code) {
        Currency result = null;

        for (final Currency it : readAll()) {
            if (!it.getCode().equalsIgnoreCase(code)) {
                continue;
            }

            if (result != null) {
                throw new TreasuryDomainException("error.Currency.duplicated.code");
            }

            result = it;
        }

        return result;
    }

    public static Currency findByName(final String name) {
        Currency result = null;

        for (final Currency it : readAll()) {

            if (!LocalizedStringUtil.isEqualToAnyLocaleIgnoreCase(it.getName(), name)) {
                continue;
            }

            if (result != null) {
                throw new TreasuryDomainException("error.Currency.duplicated.name");
            }

            result = it;
        }

        return result;
    }

    @Atomic
    public static Currency create(final String code, final LocalizedString name, final String isoCode, final String symbol) {
        return new Currency(code, name, isoCode, symbol);
    }

}
