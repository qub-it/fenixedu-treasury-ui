package org.fenixedu.treasury.domain.integration;

import org.fenixedu.bennu.core.domain.Bennu;
import org.fenixedu.bennu.core.domain.User;

public class OperationFile extends OperationFile_Base {
    
    public OperationFile() {
        super();
    	this.setBennu(Bennu.getInstance());
    }
    
    public OperationFile(String fileName, byte[] content)
    {
    	this();
    	this.init(fileName, fileName, content);
    }

    @Override
    // TODO: Implement
    public boolean isAccessible(User arg0) {
        throw new RuntimeException("not implemented");
    }
    
}
