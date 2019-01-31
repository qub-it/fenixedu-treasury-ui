package org.fenixedu.bennu.io.domain;

import java.io.InputStream;

import org.joda.time.DateTime;

public interface ITreasuryGenericFile extends pt.ist.fenixframework.DomainObject {

    public boolean isAccessible(final String username);

    public void delete();
    
    default public byte[] getContent() {
//        return TreasuryPlataformDependentServicesFactory.implementation().getFileContent(this);
        return null;
    }

    default public long getSize() {
//        return TreasuryPlataformDependentServicesFactory.implementation().getFileSize(this);
        return 0l;
    }

    default public DateTime getCreationDate() {
//        return TreasuryPlataformDependentServicesFactory.implementation().getFileCreationDate(this);
        return null;
    }

    default public String getFilename() {
//        return TreasuryPlataformDependentServicesFactory.implementation().getFilename(this);
        return null;
    }

    default public InputStream getStream() {
//        return TreasuryPlataformDependentServicesFactory.implementation().getFileStream(this);
        return null;
    }
    
}
