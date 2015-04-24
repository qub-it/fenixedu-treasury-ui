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
package org.fenixedu.treasury.domain.paymentcodes;

import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.apache.commons.lang.StringUtils;
import org.fenixedu.bennu.core.domain.Bennu;
import org.fenixedu.bennu.core.domain.User;
import org.fenixedu.commons.i18n.LocalizedString;
import org.fenixedu.treasury.domain.exceptions.TreasuryDomainException;
import org.fenixedu.treasury.domain.paymentcodes.SibsInputFile_Base;
import org.fenixedu.treasury.util.LocalizedStringUtil;

import pt.ist.fenixframework.Atomic;

public class SibsInputFile extends SibsInputFile_Base {
    
    protected SibsInputFile() {
        super();
        setBennu(Bennu.getInstance());
    }
    
    protected void init() {
    	checkRules();
    }

	private void checkRules() {
		//
		//CHANGE_ME add more busines validations
		//
		
		//CHANGE_ME In order to validate UNIQUE restrictions
	}
	
	@Atomic
	public void edit() {
	    checkRules();
	}
	
	public boolean isDeletable() {
	    return true;
	}
	
	@Atomic
	public void delete() {
	    if(!isDeletable()) {
	        throw new TreasuryDomainException("error.SibsInputFile.cannot.delete");
	    }
	    
	    setBennu(null);
	    
	    deleteDomainObject();
	}
	
	 
    @Atomic
    public static SibsInputFile create() {
    	SibsInputFile sibsInputFile = new SibsInputFile();
        sibsInputFile.init();
        return sibsInputFile;
    }

	// @formatter: off
	/************
	 * SERVICES *
	 ************/
    // @formatter: on
	
	public static Stream<SibsInputFile> findAll() {
	    return Bennu.getInstance().getSibsInputFilesSet().stream();
	}
	
	public static Stream<SibsInputFile> findByUploader(final User uploader) {
		return findAll().filter(i->uploader.equals(i.getUploader()));
	  }
	public static Stream<SibsInputFile> findByBennu(final Bennu bennu) {
		return findAll().filter(i->bennu.equals(i.getBennu()));
	  }

	@Override
	public boolean isAccessible(User arg0) {
		// TODO Auto-generated method stub
		return false;
	}
   
    
}
