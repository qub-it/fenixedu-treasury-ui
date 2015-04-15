package org.fenixedu.treasury.domain.document;

import java.util.Set;
import java.util.concurrent.locks.ReentrantReadWriteLock.ReadLock;
import java.util.stream.Stream;

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

    protected FinantialDocumentType(final FinantialDocumentTypeEnum type, final String code, final LocalizedString name,
            final String documentNumberSeriesPrefix, final boolean invoice) {
        this();
        setType(type);
        setCode(code);
        setName(name);
        setDocumentNumberSeriesPrefix(documentNumberSeriesPrefix);
        setInvoice(invoice);

        checkRules();
    }

    private void checkRules() {
        if (getType() == null) {
            throw new TreasuryDomainException("error.FinantialDocumentType.type.required");
        }

        if (LocalizedStringUtil.isTrimmedEmpty(getCode())) {
            throw new TreasuryDomainException("error.FinantialDocumentType.code.required");
        }

        if (LocalizedStringUtil.isTrimmedEmpty(getName())) {
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

    protected static FinantialDocumentType findByName(final String name) {
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

    protected static FinantialDocumentType findByDocumentNumberSeriesPrefix(final String documentNumberSeriesPrefix) {
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

    protected static FinantialDocumentType findByFinantialDocumentType(final FinantialDocumentTypeEnum type) {
        final Stream<FinantialDocumentType> stream =
                readAll().stream().filter(fdt -> fdt.getType() == type);

        if (stream.count() > 1) {
            throw new TreasuryDomainException("error.FinantialDocumentType.not.unique.in.finantial.document.type");
        }

        return stream.findFirst().orElse(null);
    }

    public static FinantialDocumentType findForDebitNote() {
        return findByFinantialDocumentType(FinantialDocumentTypeEnum.DEBIT_NOTE);
    }
    
    public static FinantialDocumentType findForCreditNote() {
        return findByFinantialDocumentType(FinantialDocumentTypeEnum.CREDIT_NOTE);
    }
    
    public static FinantialDocumentType findForSettlementNote() {
        return findByFinantialDocumentType(FinantialDocumentTypeEnum.SETTLEMENT_NOTE);
    }
    
    public static FinantialDocumentType findForReimbursementNote() {
        return findByFinantialDocumentType(FinantialDocumentTypeEnum.REIMBURSEMENT_NOTE);
    }

    @Atomic 
    public static FinantialDocumentType createForDebitNote(final String code, final LocalizedString name,
            final String documentNumberSeriesPrefix, boolean invoice) {
        return new FinantialDocumentType(FinantialDocumentTypeEnum.DEBIT_NOTE, code, name, documentNumberSeriesPrefix, invoice);
    }

    @Atomic
    public static FinantialDocumentType createForCreditNote(final String code, final LocalizedString name,
            final String documentNumberSeriesPrefix, boolean invoice) {
        return new FinantialDocumentType(FinantialDocumentTypeEnum.CREDIT_NOTE, code, name, documentNumberSeriesPrefix, invoice);
    }

    @Atomic
    public static FinantialDocumentType createForSettlementNote(final String code, final LocalizedString name,
            final String documentNumberSeriesPrefix, boolean invoice) {
        return new FinantialDocumentType(FinantialDocumentTypeEnum.SETTLEMENT_NOTE, code, name, documentNumberSeriesPrefix, invoice);
    }

    @Atomic
    public static FinantialDocumentType createForReimbursementNote(final String code, final LocalizedString name,
            final String documentNumberSeriesPrefix, boolean invoice) {
        return new FinantialDocumentType(FinantialDocumentTypeEnum.REIMBURSEMENT_NOTE, code, name, documentNumberSeriesPrefix, invoice);
    }

}
