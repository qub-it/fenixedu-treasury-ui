package org.fenixedu.treasury.domain.document;

import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.fenixedu.bennu.core.domain.Bennu;
import org.fenixedu.commons.i18n.LocalizedString;
import org.fenixedu.treasury.domain.exceptions.TreasuryDomainException;
import org.fenixedu.treasury.util.LocalizedStringUtil;

import pt.ist.fenixframework.Atomic;

public class FinantialDocumentType extends FinantialDocumentType_Base {

    protected FinantialDocumentType() {
        super();
        setBennu(Bennu.getInstance());
    }

    protected FinantialDocumentType(final String code, final LocalizedString name, final String documentNumberSeriesPrefix, boolean invoice) {
        this();
        setCode(code);
        setName(name);
        setDocumentNumberSeriesPrefix(documentNumberSeriesPrefix);
        setInvoice(invoice);

        checkRules();
    }

    private void checkRules() {
        if (StringUtils.isEmpty(getCode())) {
            throw new TreasuryDomainException("error.FinantialDocumentType.code.required");
        }

        if (LocalizedStringUtil.isEmpty(getName())) {
            throw new TreasuryDomainException("error.FinantialDocumentType.name.required");
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
            throw new TreasuryDomainException("error.FinantialDocumentType.cannot.delete");
        }

        setBennu(null);

        deleteDomainObject();
    }

    // @formatter: off
    /************
     * SERVICES *
     ************/
    // @formatter: on

    public static Set<FinantialDocumentType> readAll() {
        return Bennu.getInstance().getFinantialDocumentTypesSet();
    }

    public static FinantialDocumentType findByCode(final String code) {
        FinantialDocumentType result = null;

        for (final FinantialDocumentType it : readAll()) {
            if (!it.getCode().equalsIgnoreCase(code)) {
                continue;
            }

            if (result != null) {
                throw new TreasuryDomainException("error.FinantialDocumentType.duplicated.code");
            }

            result = it;
        }

        return result;
    }

    public static FinantialDocumentType findByName(final String name) {
        FinantialDocumentType result = null;

        for (final FinantialDocumentType it : readAll()) {

            if (!LocalizedStringUtil.isEqualToAnyLocaleIgnoreCase(it.getName(), name)) {
                continue;
            }

            if (result != null) {
                throw new TreasuryDomainException("error.FinantialDocumentType.duplicated.name");
            }

            result = it;
        }

        return result;
    }

    public static FinantialDocumentType findByDocumentNumberSeriesPrefix(final String documentNumberSeriesPrefix) {
        FinantialDocumentType result = null;

        for (final FinantialDocumentType it : readAll()) {
            if (!it.getDocumentNumberSeriesPrefix().equalsIgnoreCase(documentNumberSeriesPrefix)) {
                continue;
            }

            if (result != null) {
                throw new TreasuryDomainException("error.FinantialDocumentType.duplicated.documentNumberSeriesPrefix");
            }

            result = it;
        }

        return result;
    }
<<<<<<< HEAD
=======
    
    @Atomic
    public static FinantialDocumentType create(final String code, final LocalizedString name, final String documentNumberSeriesPrefix, boolean invoice) {
        return new FinantialDocumentType(code, name, documentNumberSeriesPrefix, invoice);
    }
>>>>>>> origin/master

}
