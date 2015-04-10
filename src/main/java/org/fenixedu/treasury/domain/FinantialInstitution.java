package org.fenixedu.treasury.domain;

public class FinantialInstitution extends FinantialInstitution_Base {

	public FinantialInstitution() {
		super();
	}

	public String getComercialRegistrationCode() {
		return this.getFiscalNumber() + " " + this.getAddress();
	}

}
