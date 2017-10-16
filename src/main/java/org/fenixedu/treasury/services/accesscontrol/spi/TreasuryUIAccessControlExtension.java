package org.fenixedu.treasury.services.accesscontrol.spi;

import org.fenixedu.bennu.core.domain.User;
import org.fenixedu.bennu.core.groups.DynamicGroup;
import org.fenixedu.treasury.domain.FinantialEntity;
import org.fenixedu.treasury.domain.FinantialInstitution;

public class TreasuryUIAccessControlExtension implements ITreasuryAccessControlExtension {

    private static final String TREASURY_MANAGERS = "treasuryManagers";

    private static final String TREASURY_BACK_OFFICE = "treasuryBackOffice";

    private static final String TREASURY_FRONT_OFFICE = "treasuryFrontOffice";

	@Override
	public boolean isFrontOfficeMember(final String username) {
		final User user = User.findByUsername(username);
		
        return getOrCreateDynamicGroup(TREASURY_FRONT_OFFICE).isMember(user);
	}

	@Override
	public boolean isFrontOfficeMember(final String username, final FinantialInstitution finantialInstitution) {
		final User user = User.findByUsername(username);
		
		return getOrCreateDynamicGroup(TREASURY_FRONT_OFFICE).isMember(user);
	}

	@Override
	public boolean isBackOfficeMember(final String username) {
		final User user = User.findByUsername(username);
		
		return getOrCreateDynamicGroup(TREASURY_BACK_OFFICE).isMember(user);
	}

	@Override
	public boolean isBackOfficeMember(final String username, final FinantialInstitution finantialInstitution) {
		final User user = User.findByUsername(username);
		
		return getOrCreateDynamicGroup(TREASURY_BACK_OFFICE).isMember(user);
	}

	@Override
	public boolean isBackOfficeMember(final String username, final FinantialEntity finantialEntity) {
		final User user = User.findByUsername(username);
		
		return getOrCreateDynamicGroup(TREASURY_BACK_OFFICE).isMember(user);
	}
	
	@Override
	public boolean isManager(final String username) {
		final User user = User.findByUsername(username);
		
		return getOrCreateDynamicGroup(TREASURY_MANAGERS).isMember(user);
	}

	@Override
	public boolean isAllowToModifySettlements(final String username, final FinantialInstitution finantialInstitution) {
		final User user = User.findByUsername(username);
		
		return getOrCreateDynamicGroup(TREASURY_FRONT_OFFICE).isMember(user);
	}

	@Override
	public boolean isAllowToModifyInvoices(final String username, final FinantialInstitution finantialInstitution) {
		final User user = User.findByUsername(username);
		
		return getOrCreateDynamicGroup(TREASURY_FRONT_OFFICE).isMember(user);
	}

    private DynamicGroup getOrCreateDynamicGroup(final String dynamicGroupName) {
        final DynamicGroup dynamicGroup = DynamicGroup.get(dynamicGroupName);

        if (!dynamicGroup.isDefined()) {
            User manager = User.findByUsername("manager");
            if (manager != null) {
                dynamicGroup.mutator().grant(manager);
            } else {
                dynamicGroup.toPersistentGroup();
            }
        }

        return dynamicGroup;
    }
	
}
