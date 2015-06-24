package org.fenixedu.treasury.domain.accesscontrol;

import org.fenixedu.bennu.core.domain.Bennu;
import org.fenixedu.bennu.core.domain.groups.PersistentGroup;
import org.fenixedu.bennu.core.groups.Group;
import org.fenixedu.treasury.services.groups.TreasuryFrontOfficeGroup;

public class PersistentTreasuryFrontOfficeGroup extends PersistentTreasuryFrontOfficeGroup_Base {
    
    public PersistentTreasuryFrontOfficeGroup() {
        super();
        
        setBennuForPersistentTreasuryFrontOfficeGroup(Bennu.getInstance());
    }

    @Override
    public Group toGroup() {
        return TreasuryFrontOfficeGroup.get();
    }

    public static PersistentGroup getInstance() {
        if(Bennu.getInstance().getPersistentTreasuryFrontOfficeGroup() == null) {
            new PersistentTreasuryFrontOfficeGroup();
        }
        
        return Bennu.getInstance().getPersistentTreasuryFrontOfficeGroup();
    }
    
}
