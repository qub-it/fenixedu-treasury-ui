package org.fenixedu.treasury.domain.accesscontrol;

import org.fenixedu.bennu.core.domain.Bennu;
import org.fenixedu.bennu.core.domain.groups.PersistentGroup;
import org.fenixedu.bennu.core.groups.Group;
import org.fenixedu.treasury.services.groups.TreasuryFrontOfficeGroup;
import org.fenixedu.treasury.services.groups.TreasuryManagersGroup;

import pt.ist.fenixframework.Atomic;

public class PersistentTreasuryManagersGroup extends PersistentTreasuryManagersGroup_Base {
    
    public PersistentTreasuryManagersGroup() {
        super();
        
        setBennuForPersistentTreasuryManagersGroup(Bennu.getInstance());
    }

    @Override
    public Group toGroup() {
        return TreasuryManagersGroup.get();
    }

    @Atomic
    public static PersistentGroup getInstance() {
        if(Bennu.getInstance().getPersistentTreasuryManagersGroup() == null) {
            new PersistentTreasuryManagersGroup();
        }
        
        return Bennu.getInstance().getPersistentTreasuryManagersGroup();
    }
}
