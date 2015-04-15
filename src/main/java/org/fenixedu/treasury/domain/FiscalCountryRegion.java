package org.fenixedu.treasury.domain;

import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.fenixedu.bennu.core.domain.Bennu;
import org.fenixedu.commons.i18n.LocalizedString;
import org.fenixedu.treasury.domain.exceptions.TreasuryDomainException;
import org.fenixedu.treasury.util.LocalizedStringUtil;

import pt.ist.fenixframework.Atomic;

public class FiscalCountryRegion extends FiscalCountryRegion_Base {
    
    protected FiscalCountryRegion() {
        super();
        setBennu(Bennu.getInstance());
    }
    
    protected FiscalCountryRegion(final String fiscalCode, final LocalizedString name) {
    	this();
        setFiscalCode(fiscalCode);
    	setName(name);
    	
    	checkRules();
    }

	private void checkRules() {
		if(LocalizedStringUtil.isTrimmedEmpty(getFiscalCode())) {
		    throw new TreasuryDomainException("error.FiscalCountryRegion.fiscalCode.required");
		}
		
		if(LocalizedStringUtil.isTrimmedEmpty(getName())) {
		    throw new TreasuryDomainException("error.FiscalCountryRegion.name.required");
		}
		
		findByRegionCode(getFiscalCode());
		
		getName().getLocales().stream().forEach(l -> findByName(getName().getContent(l)));
	}
	
	@Atomic
	public void edit(final String fiscalCode, final LocalizedString name) {
	    setFiscalCode(fiscalCode);
	    setName(name);
	    
	    checkRules();
	}
	
	public boolean isDeletable() {
	    return true;
	}
	
	@Atomic
	public void delete() {
	    if(!isDeletable()) {
	        throw new TreasuryDomainException("error.FiscalCountryRegion.cannot.delete");
	    }
	    
	    setBennu(null);
	    
	    deleteDomainObject();
	}

	// @formatter: off
	/************
	 * SERVICES *
	 ************/
    // @formatter: on
	
	public static Set<FiscalCountryRegion> readAll() {
	    return Bennu.getInstance().getFiscalCountryRegionsSet();
	}
	
    public static FiscalCountryRegion findByRegionCode(final String fiscalCode) {
        FiscalCountryRegion result = null;
        
        for (final FiscalCountryRegion it : readAll()) {
            if(!it.getFiscalCode().equalsIgnoreCase(fiscalCode)) {
                continue;
            }
            
            if(result != null) {
                throw new TreasuryDomainException("error.FiscalCountryRegion.duplicated.fiscalCode");
            }
            
            result = it;
        }
        
        return result;
    }
    
    public static FiscalCountryRegion findByName(final String name) {
        FiscalCountryRegion result = null;
        
        for (final FiscalCountryRegion it : readAll()) {
            
            if(!LocalizedStringUtil.isEqualToAnyLocaleIgnoreCase(it.getName(), name)) {
                continue;
            }
            
            if(result != null) {
                throw new TreasuryDomainException("error.FiscalCountryRegion.duplicated.name");
            }
            
            result = it;
        }
        
        return result;
    }
    
    @Atomic
    public static FiscalCountryRegion create(final String fiscalCode, final LocalizedString name) {
        return new FiscalCountryRegion(fiscalCode, name);
    }
    
}
