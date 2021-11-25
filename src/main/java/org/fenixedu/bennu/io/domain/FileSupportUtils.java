package org.fenixedu.bennu.io.domain;

import java.util.Set;

@Deprecated
public class FileSupportUtils {

    public static Set<LocalFileToDelete> retrieveDeleteSet(final FileSupport fileSupport) {
        return fileSupport.getDeleteSet();
    }
    
}
