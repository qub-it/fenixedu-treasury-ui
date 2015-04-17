package org.fenixedu.treasury.domain.document;

public enum FinantialDocumentStateType {
	TEMPORARY,
    CLOSED,
	ANNULED;
	
	public boolean isTemporary() {
	    return this == TEMPORARY;
	}
	
	public boolean isClosed() {
	    return this == CLOSED;
	}
	
	public boolean isAnnuled() {
	    return this == ANNULED;
	}
}