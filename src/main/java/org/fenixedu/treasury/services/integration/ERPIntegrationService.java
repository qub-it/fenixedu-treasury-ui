package org.fenixedu.treasury.services.integration;

import javax.jws.WebMethod;
import javax.jws.WebService;

import com.qubit.solution.fenixedu.bennu.webservices.services.server.BennuWebService;

@WebService
public class ERPIntegrationService extends BennuWebService {

	@WebMethod
	public void testMethod(int x, int y, String z)
	{
		
	}
}
