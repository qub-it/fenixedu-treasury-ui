package org.fenixedu.treasury.domain.paymentCodes;

import org.fenixedu.bennu.core.domain.User;

public class SibsInputFile extends SibsInputFile_Base {
    
    public SibsInputFile(String name, byte[] bytes) {
        super();
        init(name,name,bytes);
    }

	@Override
	public boolean isAccessible(User arg0) {
		// TODO Auto-generated method stub
		return true;
	}
    
}
