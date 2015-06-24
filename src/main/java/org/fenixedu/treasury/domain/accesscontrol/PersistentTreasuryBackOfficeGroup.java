package org.fenixedu.treasury.domain.accesscontrol;

import org.fenixedu.bennu.core.domain.Bennu;
import org.fenixedu.bennu.core.domain.groups.PersistentGroup;
import org.fenixedu.bennu.core.groups.Group;
import org.fenixedu.treasury.services.groups.TreasuryBackOfficeGroup;
import org.fenixedu.treasury.services.groups.TreasuryFrontOfficeGroup;

import pt.ist.fenixframework.Atomic;

public class PersistentTreasuryBackOfficeGroup extends PersistentTreasuryBackOfficeGroup_Base {
    
    public PersistentTreasuryBackOfficeGroup() {
        super();
        setBennuForPersistentTreasuryBackOfficeGroup(Bennu.getInstance());
    }

    @Override
    public Group toGroup() {
        return TreasuryBackOfficeGroup.get();
    }

    @Atomic
    public static PersistentGroup getInstance() {
        if(Bennu.getInstance().getPersistentTreasuryBackOfficeGroup() == null) {
            new PersistentTreasuryBackOfficeGroup();
        }
        
        return Bennu.getInstance().getPersistentTreasuryBackOfficeGroup();
    }
}
