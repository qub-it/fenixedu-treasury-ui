package org.fenixedu.treasury.domain;

import java.util.Set;

import org.fenixedu.treasury.domain.exceptions.TreasuryDomainException;

import com.google.common.collect.Sets;

public interface IFiscalContributor {
    
    public String getFiscalNumber();
    
    public static IFiscalContributor findByFiscalNumber(final String fiscalNumber) {
        IFiscalContributor result = null;
        
        for(IFiscalContributor it : readAll()) {
            if(!it.getFiscalNumber().equalsIgnoreCase(fiscalNumber)) {
                continue;
            }
            
            if(result != null) {
                throw new TreasuryDomainException("error.IFiscalContributor.duplicate.fiscal.number");
            }
            
            result = it;
        }
        
        return result;
    }
    
    public static Set<IFiscalContributor> readAll() {
        final Set<IFiscalContributor> iterable = Sets.newHashSet(FinantialInstitution.readAll());
        iterable.addAll(Customer.readAll());
        
        return iterable;
    }
}
