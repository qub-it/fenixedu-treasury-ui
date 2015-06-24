package org.fenixedu.treasury.services.accesscontrol;

import java.util.Set;

import org.fenixedu.bennu.core.domain.User;
import org.fenixedu.bennu.core.groups.DynamicGroup;
import org.fenixedu.treasury.domain.FinantialEntity;
import org.fenixedu.treasury.domain.FinantialInstitution;
import org.fenixedu.treasury.domain.accesscontrol.TreasuryAccessControl;
import org.fenixedu.treasury.services.accesscontrol.spi.ITreasuryAccessControlExtension;

public class TreasuryAccessControlAPI {

    public static void registerExtension(final ITreasuryAccessControlExtension extension) {
        TreasuryAccessControl.getInstance().registerExtension(extension);
    }
    
    public static void unregisterExtension(final ITreasuryAccessControlExtension extension) {
        TreasuryAccessControl.getInstance().unregisterExtension(extension);
    }
    
    public static boolean isFrontOfficeMember(final User user) {
        return TreasuryAccessControl.getInstance().isFrontOfficeMember(user);
    }
    
    public static boolean isFrontOfficeMember(final User user, final FinantialInstitution finantialInstitution) {
        return TreasuryAccessControl.getInstance().isFrontOfficeMember(user, finantialInstitution);
    }
    
    public static boolean isBackOfficeMember(final User user) {
        return TreasuryAccessControl.getInstance().isBackOfficeMember(user);
    }
    
    public static boolean isBackOfficeMember(final User user, final FinantialInstitution finantialInstitution) {
        return TreasuryAccessControl.getInstance().isBackOfficeMember(user, finantialInstitution);
    }
    
    public static boolean isBackOfficeMember(final User user, final FinantialEntity finantialEntity) {
        return TreasuryAccessControl.getInstance().isBackOfficeMember(user, finantialEntity);
    }
    
    public static boolean isManager(final User user) {
        return TreasuryAccessControl.getInstance().isManager(user);
    }
    
    public static Set<User> getFrontOfficeMembers() {
        return TreasuryAccessControl.getInstance().getFrontOfficeMembers();
    }
    
    public static Set<User> getBackOfficeMembers() {
        return TreasuryAccessControl.getInstance().getBackOfficeMembers();
    }
    
    public static Set<User> getTreasuryManagerMembers() {
        return TreasuryAccessControl.getInstance().getTreasuryManagerMembers();        
    }
    
}
