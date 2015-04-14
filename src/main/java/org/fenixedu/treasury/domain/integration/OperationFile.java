package org.fenixedu.treasury.domain.integration;

import org.fenixedu.bennu.core.domain.Bennu;

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
    
}
