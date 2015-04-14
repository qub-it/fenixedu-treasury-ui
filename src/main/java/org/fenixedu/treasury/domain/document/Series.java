package org.fenixedu.treasury.domain.document;

import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.fenixedu.bennu.core.domain.Bennu;
import org.fenixedu.commons.i18n.LocalizedString;
import org.fenixedu.treasury.domain.FinantialInstitution;
import org.fenixedu.treasury.domain.exceptions.TreasuryDomainException;
import org.fenixedu.treasury.util.LocalizedStringUtil;

import pt.ist.fenixframework.Atomic;

import com.google.common.collect.Sets;

public class Series extends Series_Base {

    protected Series() {
        super();
        setBennu(Bennu.getInstance());
    }

    protected Series(final FinantialInstitution finantialInstitution, final String code, final LocalizedString name,
            final boolean externSeries, final boolean certificated, final boolean legacy) {
        this();
        setFinantialInstitution(finantialInstitution);
        setCode(code);
        setName(name);
        setExternSeries(externSeries);
        setCertificated(certificated);
        setLegacy(legacy);

        checkRules();
    }

    private void checkRules() {
        if (getFinantialInstitution() == null) {
            throw new TreasuryDomainException("error.Series.finantialInstitution.required");
        }

        if (StringUtils.isEmpty(getCode())) {
            throw new TreasuryDomainException("error.Series.code.required");
        }

        if (LocalizedStringUtil.isEmpty(getName())) {
            throw new TreasuryDomainException("error.Series.name.required");
        }

        findByCode(getFinantialInstitution(), getCode());
        getName().getLocales().stream().forEach(l -> findByName(getFinantialInstitution(), getName().getContent(l)));
    }

    @Atomic
    public void edit(final String code, final LocalizedString name, final boolean externSeries, final boolean certificated,
            final boolean legacy) {
        setCode(code);
        setName(name);
        setExternSeries(externSeries);
        setCertificated(certificated);
        setLegacy(legacy);

        checkRules();
    }

    public boolean isDeletable() {
        return true;
    }

    @Atomic
    public void delete() {
        if (!isDeletable()) {
            throw new TreasuryDomainException("error.Series.cannot.delete");
        }

        setBennu(null);

        deleteDomainObject();
    }

    // @formatter: off
    /************
     * SERVICES *
     ************/
    // @formatter: on

    public static Set<Series> readAll() {
        return Bennu.getInstance().getSeriesSet();
    }
    
    public static Set<Series> find(final FinantialInstitution finantialInstitution) {
        Set<Series> result = Sets.newHashSet();

        for (final Series it : readAll()) {
            if (it.getFinantialInstitution() == finantialInstitution) {
                result.add(it);
            }
        }

        return result;
    }

    public static Series findByCode(final FinantialInstitution finantialInstitution, final String code) {
        Series result = null;

        for (final Series it : find(finantialInstitution)) {
            if (!it.getCode().equalsIgnoreCase(code)) {
                continue;
            }

            if (result != null) {
                throw new TreasuryDomainException("error.Series.duplicated.code");
            }

            result = it;
        }

        return result;
    }

    public static Series findByName(final FinantialInstitution finantialInstitution, final String name) {
        Series result = null;

        for (final Series it : find(finantialInstitution)) {

            if (!LocalizedStringUtil.isEqualToAnyLocaleIgnoreCase(it.getName(), name)) {
                continue;
            }

            if (result != null) {
                throw new TreasuryDomainException("error.Series.duplicated.name");
            }

            result = it;
        }

        return result;
    }

    @Atomic
    public static Series create(final FinantialInstitution finantialInstitution, final String code, final LocalizedString name,
            final boolean externSeries, final boolean certificated, final boolean legacy) {
        return new Series(finantialInstitution, code, name, externSeries, certificated, legacy);
    }

}
