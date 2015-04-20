package org.fenixedu.treasury.domain.document;

public enum FinantialDocumentStateType {
	PREPARING,
    CLOSED,
	ANNULED;
	
	public boolean isPreparing() {
	    return this == PREPARING;
	}
	
	public boolean isClosed() {
	    return this == CLOSED;
	}
	
	public boolean isAnnuled() {
	    return this == ANNULED;
	}
}