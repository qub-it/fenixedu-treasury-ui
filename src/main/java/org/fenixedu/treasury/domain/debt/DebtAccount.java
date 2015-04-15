package org.fenixedu.treasury.domain.debt;

import java.util.Set;
import java.util.stream.Stream;

import org.fenixedu.bennu.core.domain.Bennu;
import org.fenixedu.treasury.domain.Customer;
import org.fenixedu.treasury.domain.FinantialInstitution;
import org.fenixedu.treasury.domain.exceptions.TreasuryDomainException;

import pt.ist.fenixframework.Atomic;

public class DebtAccount extends DebtAccount_Base {

    protected DebtAccount() {
        super();
        setBennu(Bennu.getInstance());
    }

    protected DebtAccount(final FinantialInstitution finantialInstitution, final Customer customer) {
        this();
        setFinantialInstitution(finantialInstitution);
        setCustomer(customer);
        
        checkRules();
    }

    private void checkRules() {
        if(getFinantialInstitution() == null) {
            throw new TreasuryDomainException("error.DebtAccount.finantialInstitution.required");
        }
        
        if(getCustomer() == null) {
            throw new TreasuryDomainException("error.DebtAccount.customer.required");
        }
        
        find(getFinantialInstitution(), getCustomer());
    }

    public boolean isDeletable() {
        return true;
    }

    @Atomic
    public void delete() {
        if (!isDeletable()) {
            throw new TreasuryDomainException("error.DebtAccount.cannot.delete");
        }

        setBennu(null);

        deleteDomainObject();
    }

    // @formatter: off
    /************
     * SERVICES *
     ************/
    // @formatter: on

    public static Set<DebtAccount> readAll() {
        return Bennu.getInstance().getDebtsAccountsSet();
    }
    
    public static Set<DebtAccount> find(final FinantialInstitution finantialInstitution) {
        return finantialInstitution.getDebtAccountsSet();
    }
    
    public static Set<DebtAccount> find(final Customer customer) {
        return customer.getDebtAccountsSet();
    }
    
    public static DebtAccount find(final FinantialInstitution finantialInstitution, final Customer customer) {
        final Stream<DebtAccount> stream = find(finantialInstitution).stream().filter(da -> da.getCustomer() == customer);
        
        if(stream.count() > 1) {
            throw new TreasuryDomainException("error.DebtAccount.not.unique.in.finantial.institution.and.customer");
        }
        
        return stream.findFirst().orElse(null);
    }

    @Atomic
    public static DebtAccount create(final FinantialInstitution finantialInstitution, final Customer customer) {
        return new DebtAccount(finantialInstitution, customer);
    }

}
