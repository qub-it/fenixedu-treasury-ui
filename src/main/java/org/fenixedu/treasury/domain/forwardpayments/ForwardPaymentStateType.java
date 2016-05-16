package org.fenixedu.treasury.domain.forwardpayments;

import org.fenixedu.commons.i18n.LocalizedString;
import org.fenixedu.treasury.util.Constants;

public enum ForwardPaymentStateType {
    CREATED,
    REQUESTED,
    AUTHENTICATED,
    AUTHORIZED,
    PAYED,
    REJECTED;
    
    public boolean isCreated() {
        return this == CREATED;
    }
    
    public boolean isRequested() {
        return this == REQUESTED;
    }
    
    public boolean isAuthenticated() {
        return this == AUTHENTICATED;
    }
    
    public boolean isAuthorized() {
        return this == AUTHORIZED;
    }
    
    public boolean isPayed() {
        return this == PAYED;
    }
    
    public boolean isRejected() {
        return this == REJECTED;
    }
    
    public boolean isInStateToPostProcessPayment() {
        return isCreated() || isRequested();
    }
    
    public LocalizedString getLocalizedName() {
        return Constants.bundleI18N(getClass().getSimpleName() + "." + name());
    }
}
