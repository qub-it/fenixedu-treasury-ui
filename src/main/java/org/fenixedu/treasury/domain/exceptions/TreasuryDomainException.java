package org.fenixedu.treasury.domain.exceptions;

import javax.ws.rs.core.Response.Status;

import org.fenixedu.bennu.FenixeduTreasurySpringConfiguration;
import org.fenixedu.bennu.core.domain.exceptions.DomainException;

public class TreasuryDomainException extends DomainException {

    private static final long serialVersionUID = 1L;

    public TreasuryDomainException(String key, String... args) {
        super(FenixeduTreasurySpringConfiguration.BUNDLE, key, args);
    }

    public TreasuryDomainException(Status status, String key, String... args) {
        super(status, FenixeduTreasurySpringConfiguration.BUNDLE, key, args);
    }

    public TreasuryDomainException(Throwable cause, String key, String... args) {
        super(cause, FenixeduTreasurySpringConfiguration.BUNDLE, key, args);
    }

    public TreasuryDomainException(Throwable cause, Status status, String key, String... args) {
        super(cause, status, FenixeduTreasurySpringConfiguration.BUNDLE, key, args);
    }

}
