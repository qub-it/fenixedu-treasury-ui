package org.fenixedu.treasury.services.groups;

import java.util.Collections;
import java.util.Set;
import java.util.stream.Collectors;

import org.fenixedu.bennu.core.annotation.GroupArgument;
import org.fenixedu.bennu.core.annotation.GroupArgumentParser;
import org.fenixedu.bennu.core.annotation.GroupOperator;
import org.fenixedu.bennu.core.domain.Bennu;
import org.fenixedu.bennu.core.domain.User;
import org.fenixedu.bennu.core.domain.groups.PersistentGroup;
import org.fenixedu.bennu.core.domain.groups.PersistentLoggedGroup;
import org.fenixedu.bennu.core.domain.groups.PersistentUserGroup;
import org.fenixedu.bennu.core.groups.AnonymousGroup;
import org.fenixedu.bennu.core.groups.AnyoneGroup;
import org.fenixedu.bennu.core.groups.CustomGroup;
import org.fenixedu.bennu.core.groups.Group;
import org.fenixedu.bennu.core.groups.LoggedGroup;
import org.fenixedu.bennu.core.groups.NobodyGroup;
import org.fenixedu.bennu.core.i18n.BundleUtil;
import org.fenixedu.treasury.util.Constants;
import org.joda.time.DateTime;

import com.google.common.base.Objects;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Sets;

@GroupOperator("treasuryManagers")
public/* final */class TreasuryManagersGroup extends CustomGroup {
    private static final long serialVersionUID = -933441447959188208L;

    private static final TreasuryManagersGroup INSTANCE = new TreasuryManagersGroup();

    private TreasuryManagersGroup() {
        super();
    }

    public static TreasuryManagersGroup get() {
        return INSTANCE;
    }

    @Override
    public String getPresentationName() {
        return BundleUtil.getString(Constants.BUNDLE, "label.bennu.group.treasuryManagers");
    }

    @Override
    public Set<User> getMembers() {
        return Collections.unmodifiableSet(Bennu.getInstance().getUserSet());
    }

    @Override
    public boolean isMember(final User user) {
        return user != null;
    }

    @Override
    public Set<User> getMembers(DateTime when) {
        return getMembers();
    }

    @Override
    public boolean isMember(User user, DateTime when) {
        return isMember(user);
    }

    @Override
    public Group and(Group group) {
        if (group instanceof AnonymousGroup) {
            return NobodyGroup.get();
        }
        if (!group.isMember(null)) {
            return group;
        }
        return super.and(group);
    }

    @Override
    public Group or(Group group) {
        if (group instanceof AnonymousGroup) {
            return AnyoneGroup.get();
        }
        if (!group.isMember(null)) {
            return this;
        }
        return super.or(group);
    }

    @Override
    public Group minus(Group group) {
        if (group instanceof AnonymousGroup) {
            return this;
        }
        return super.minus(group);
    }

    @Override
    public Group not() {
        return AnonymousGroup.get();
    }

    @Override
    public boolean equals(Object object) {
        return object instanceof LoggedGroup;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(LoggedGroup.class);
    }

    @Override
    public PersistentGroup toPersistentGroup() {
        // TODO Auto-generated method stub
        return null;
    }
}
