package org.fenixedu.bennu.io.domain;

import java.util.Set;

public class FileSupportUtils {

    public static Set<LocalFileToDelete> retrieveDeleteSet(final FileSupport fileSupport) {
        return fileSupport.getDeleteSet();
    }
    
}
