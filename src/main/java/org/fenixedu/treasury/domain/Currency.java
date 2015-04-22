/**
 * This file was created by Quorum Born IT <http://www.qub-it.com/> and its 
 * copyright terms are bind to the legal agreement regulating the FenixEdu@ULisboa 
 * software development project between Quorum Born IT and Serviços Partilhados da
 * Universidade de Lisboa:
 *  - Copyright © 2015 Quorum Born IT (until any Go-Live phase)
 *  - Copyright © 2015 Universidade de Lisboa (after any Go-Live phase)
 *
 * Contributors: ricardo.pedro@qub-it.com, anil.mamede@qub-it.com
 * 
 *
 * 
 * This file is part of FenixEdu Treasury.
 *
 * FenixEdu Treasury is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * FenixEdu Treasury is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with FenixEdu Treasury.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.fenixedu.treasury.domain;


import java.util.Set;
import java.util.stream.Stream;

import org.fenixedu.bennu.core.domain.Bennu;
import org.fenixedu.commons.i18n.LocalizedString;
import org.fenixedu.treasury.domain.exceptions.TreasuryDomainException;
import org.fenixedu.treasury.util.LocalizedStringUtil;

import pt.ist.fenixframework.Atomic;

public class Currency extends Currency_Base {

	  protected Currency() {
	        super();
	        setBennu(Bennu.getInstance());
	    }
	    
	    protected void init(final java.lang.String code,final org.fenixedu.commons.i18n.LocalizedString name,final java.lang.String isoCode,final java.lang.String symbol) {
	setCode(code);
	setName(name);
	setIsoCode(isoCode);
	setSymbol(symbol);
	    	checkRules();
	    }

		private void checkRules() {
			//
			//CHANGE_ME add more busines validations
			//
			
			//CHANGE_ME In order to validate UNIQUE restrictions
			//if (findByCode(getCode().count()>1)
			//{
			//	throw new TreasuryDomainException("error.Currency.code.duplicated");
			//}	
			//if (findByName(getName().count()>1)
			//{
			//	throw new TreasuryDomainException("error.Currency.name.duplicated");
			//}	
			//if (findByIsoCode(getIsoCode().count()>1)
			//{
			//	throw new TreasuryDomainException("error.Currency.isoCode.duplicated");
			//}	
			//if (findBySymbol(getSymbol().count()>1)
			//{
			//	throw new TreasuryDomainException("error.Currency.symbol.duplicated");
			//}	
		}
		
		@Atomic
		public void edit(final java.lang.String code,final org.fenixedu.commons.i18n.LocalizedString name,final java.lang.String isoCode,final java.lang.String symbol) {
		    setCode(code);
		    setName(name);
		    setIsoCode(isoCode);
		    setSymbol(symbol);
		    checkRules();
		}
		
		public boolean isDeletable() {
		    return true;
		}
		
		@Atomic
		public void delete() {
		    if(!isDeletable()) {
		        throw new TreasuryDomainException("error.Currency.cannot.delete");
		    }
		    
		    setBennu(null);
		    
		    deleteDomainObject();
		}
		
		 
	    @Atomic
	    public static Currency create(final java.lang.String code,final org.fenixedu.commons.i18n.LocalizedString name,final java.lang.String isoCode,final java.lang.String symbol) {
	    	Currency currency = new Currency();
	        currency.init( code, name, isoCode, symbol);
	        return currency;
	    }

		// @formatter: off
		/************
		 * SERVICES *
		 ************/
	    // @formatter: on
		
		public static Stream<Currency> findAll() {
		    return Bennu.getInstance().getCurrenciesSet().stream();
		}
		
		public static Stream<Currency> findByCode(final java.lang.String code) {
			return findAll().filter(i->code.equalsIgnoreCase(i.getCode()));
		  }
		public static Stream<Currency> findByName(final org.fenixedu.commons.i18n.LocalizedString name) {
			return findAll().filter(i->name.equals(i.getName()));
		  }
		public static Stream<Currency> findByIsoCode(final java.lang.String isoCode) {
			return findAll().filter(i->isoCode.equalsIgnoreCase(i.getIsoCode()));
		  }
		public static Stream<Currency> findBySymbol(final java.lang.String symbol) {
			return findAll().filter(i->symbol.equalsIgnoreCase(i.getSymbol()));
		  }
	   
}
