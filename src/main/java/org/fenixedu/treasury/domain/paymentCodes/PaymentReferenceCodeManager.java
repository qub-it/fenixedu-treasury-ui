package org.fenixedu.treasury.domain.paymentCodes;

import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.apache.commons.lang.StringUtils;
import org.fenixedu.bennu.core.domain.Bennu;
import org.fenixedu.commons.i18n.LocalizedString;
import org.fenixedu.treasury.domain.exceptions.TreasuryDomainException;
import org.fenixedu.treasury.util.LocalizedStringUtil;

import pt.ist.fenixframework.Atomic;

public class PaymentReferenceCodeManager extends PaymentReferenceCodeManager_Base {
    
    protected PaymentReferenceCodeManager() {
        super();
        setBennu(Bennu.getInstance());
    }
    
    protected void init(final java.lang.String code,final java.lang.String description) {
setCode(code);
setDescription(description);
    	checkRules();
    }

	private void checkRules() {
		//
		//CHANGE_ME add more busines validations
		//
		
		//CHANGE_ME In order to validate UNIQUE restrictions
		//if (findByCode(getCode().count()>1)
		//{
		//	throw new TreasuryDomainException("error.PaymentReferenceCodeManager.code.duplicated");
		//}	
		//if (findByDescription(getDescription().count()>1)
		//{
		//	throw new TreasuryDomainException("error.PaymentReferenceCodeManager.description.duplicated");
		//}	
	}
	
	@Atomic
	public void edit(final java.lang.String code,final java.lang.String description) {
	    setCode(code);
	    setDescription(description);
	    checkRules();
	}
	
	public boolean isDeletable() {
	    return true;
	}
	
	@Atomic
	public void delete() {
	    if(!isDeletable()) {
	        throw new TreasuryDomainException("error.PaymentReferenceCodeManager.cannot.delete");
	    }
	    
	    setBennu(null);
	    
	    deleteDomainObject();
	}
	
	 
    @Atomic
    public static PaymentReferenceCodeManager create(final java.lang.String code,final java.lang.String description) {
    	PaymentReferenceCodeManager paymentReferenceCodeManager = new PaymentReferenceCodeManager();
        paymentReferenceCodeManager.init( code, description);
        return paymentReferenceCodeManager;
    }

	// @formatter: off
	/************
	 * SERVICES *
	 ************/
    // @formatter: on
	
	public static Stream<PaymentReferenceCodeManager> findAll() {
	    return Bennu.getInstance().getPaymentReferenceCodeManagersSet().stream();
	}
	
	public static Stream<PaymentReferenceCodeManager> findByCode(final java.lang.String code) {
		return findAll().filter(i->code.equalsIgnoreCase(i.getCode()));
	  }
	public static Stream<PaymentReferenceCodeManager> findByDescription(final java.lang.String description) {
		return findAll().filter(i->description.equalsIgnoreCase(i.getDescription()));
	  }
   
    
}
