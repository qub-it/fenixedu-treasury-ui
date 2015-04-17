package org.fenixedu.treasury.domain;

import java.util.stream.Stream;

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
        if (LocalizedStringUtil.isTrimmedEmpty(getCode())) {
            throw new TreasuryDomainException("error.Currency.code.required");
        }

        if (LocalizedStringUtil.isTrimmedEmpty(getName())) {
            throw new TreasuryDomainException("error.Currency.name.required");
        }
        
        if(LocalizedStringUtil.isTrimmedEmpty(getIsoCode())) {
            throw new TreasuryDomainException("error.Currency.isoCode.required");
        }
        
        if(LocalizedStringUtil.isTrimmedEmpty(getSymbol())) {
            throw new TreasuryDomainException("error.Currency.symbol.required");
        }

        if(findByCode(getCode()).count() > 2) {
            throw new TreasuryDomainException("error.Currency.code.duplicated");
        };
        
        getName().getLocales().stream().forEach(l -> {
            if(findByName(getName().getContent(l)).count() > 2) {
                throw new TreasuryDomainException("error.Currency.name.duplicated", l.toString());
            }
        });
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

    public static Stream<Currency> readAll() {
        return Bennu.getInstance().getCurrenciesSet().stream();
    }

    public static Stream<Currency> findByCode(final String code) {
        return readAll().filter(c -> c.getCode().equalsIgnoreCase(code));
    }

    public static Stream<Currency> findByName(final String name) {
        return readAll().filter(c -> LocalizedStringUtil.isEqualToAnyLocaleIgnoreCase(c.getName(), name));
    }

    @Atomic
    public static Currency create(final String code, final LocalizedString name, final String isoCode, final String symbol) {
        return new Currency(code, name, isoCode, symbol);
    }

}
