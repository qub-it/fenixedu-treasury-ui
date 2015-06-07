package org.fenixedu.treasury.domain.exemption;

import java.util.stream.Stream;

import org.fenixedu.treasury.domain.debt.DebtAccount;

public class TreasuryExemption extends TreasuryExemption_Base {
    
    public TreasuryExemption() {
        super();
    }

    public static Stream<TreasuryExemption> findByDebtAccount(final DebtAccount debtAccount) {
        // TODO Auto-generated method stub
        return null;
    }
    
}
