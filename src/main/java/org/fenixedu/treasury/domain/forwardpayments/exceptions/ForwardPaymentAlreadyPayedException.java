package org.fenixedu.treasury.domain.forwardpayments.exceptions;

import javax.ws.rs.core.Response.Status;

import org.fenixedu.treasury.domain.exceptions.TreasuryDomainException;
import org.fenixedu.treasury.util.TreasuryConstants;

public class ForwardPaymentAlreadyPayedException extends TreasuryDomainException {

    public ForwardPaymentAlreadyPayedException(String key, String... args) {
        super(key, args);
    }

    public ForwardPaymentAlreadyPayedException(Status status, String key, String... args) {
        super(status, key, args);
    }

    public ForwardPaymentAlreadyPayedException(Throwable cause, String key, String... args) {
        super(cause, key, args);
    }

    public ForwardPaymentAlreadyPayedException(Throwable cause, Status status, String key, String... args) {
        super(cause, status, key, args);
    }
    
}
