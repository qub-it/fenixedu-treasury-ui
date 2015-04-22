/**
 * This file was created by Quorum Born IT <http://www.qub-it.com/> and its 
 * copyright terms are bind to the legal agreement regulating the FenixEdu@ULisboa 
 * software development project between Quorum Born IT and Serviços Partilhados da
 * Universidade de Lisboa:
 *  - Copyright © 2015 Quorum Born IT (until any Go-Live phase)
 *  - Copyright © 2015 Universidade de Lisboa (after any Go-Live phase)
 *
 * Contributors: xpto@qub-it.com
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
package org.fenixedu.treasury.ui.accounting.manageCustomer;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.ArrayList;
import org.joda.time.DateTime;
import java.util.stream.Collectors;
import org.fenixedu.bennu.spring.portal.SpringApplication;
import org.fenixedu.bennu.spring.portal.SpringFunctionality;
import org.springframework.stereotype.Component;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.fenixedu.bennu.spring.portal.BennuSpringController;
import org.fenixedu.bennu.core.domain.exceptions.DomainException;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.fenixedu.bennu.core.domain.Bennu;
import pt.ist.fenixframework.Atomic;

import org.fenixedu.treasury.ui.TreasuryBaseController;
import org.fenixedu.treasury.ui.TreasuryController;
import org.fenixedu.treasury.domain.AdhocCustomer;

//@Component("org.fenixedu.treasury.ui.accounting.manageCustomer") <-- Use for duplicate controller name disambiguation
//@SpringFunctionality(app = TreasuryController.class, title = "label.title.accounting.manageCustomer",accessGroup = "logged")// CHANGE_ME accessGroup = "group1 | group2 | groupXPTO"
//or
@BennuSpringController(value=CustomerController.class) 
@RequestMapping("/treasury/accounting/managecustomer/adhoccustomer")
public class AdhocCustomerController extends TreasuryBaseController {
	
//

				
	
				private AdhocCustomer getAdhocCustomer(Model model)
				{
					return (AdhocCustomer)model.asMap().get("adhocCustomer");
				}
								
				private void setAdhocCustomer(AdhocCustomer adhocCustomer, Model model)
				{
					model.addAttribute("adhocCustomer", adhocCustomer);
				}
								
				@Atomic
				public void deleteAdhocCustomer(AdhocCustomer adhocCustomer) {
					// CHANGE_ME: Do the processing for deleting the adhocCustomer
					// Do not catch any exception here
					
					// adhocCustomer.delete();
				}

//				
					@RequestMapping(value = "/")
					public String search( Model model) {
							List<AdhocCustomer> searchadhoccustomerResultsDataSet = filterSearchAdhocCustomer(  );
						
						//add the results dataSet to the model
						model.addAttribute("searchadhoccustomerResultsDataSet",searchadhoccustomerResultsDataSet);
						return "treasury/accounting/managecustomer/adhoccustomer/search";
					}
					
				private List<AdhocCustomer> getSearchUniverseSearchAdhocCustomerDataSet() {
					//
					//The initialization of the result list must be done here
					//
					//
					// return new ArrayList<AdhocCustomer>(AdhocCustomer.findAll().collect(Collectors.toList())); //CHANGE_ME
					return new ArrayList<AdhocCustomer>();
				}
				
		private List<AdhocCustomer> filterSearchAdhocCustomer() {
			
			return getSearchUniverseSearchAdhocCustomerDataSet().stream()
				.collect(Collectors.toList());				
		}
		
		
				@RequestMapping(value = "/search/view/{oid}")
				public String processSearchToViewAction(@PathVariable("oid") AdhocCustomer adhocCustomer,  Model model, RedirectAttributes redirectAttributes) {
		
			// CHANGE_ME Insert code here for processing viewAction
			// If you selected multiple exists you must choose which one to use below	 
		return redirect("/treasury/accounting/managecustomer/customer/read" + "/" + adhocCustomer.getExternalId(), model, redirectAttributes);
		}
		
		
		
		
//				
				@RequestMapping(value = "/create", method = RequestMethod.GET)
				public String create(Model model) {
					return "treasury/accounting/managecustomer/adhoccustomer/create";
				}
						
//				
				@RequestMapping(value = "/create", method = RequestMethod.POST)
				public String create(@RequestParam(value="code", required=false) 
									java.lang.String 
									  code
				,@RequestParam(value="name", required=false) 
									java.lang.String 
									  name
				,@RequestParam(value="fiscalnumber", required=false) 
									java.lang.String 
									  fiscalNumber
				,@RequestParam(value="identificationnumber", required=false) 
									java.lang.String 
									  identificationNumber
				, Model model, RedirectAttributes redirectAttributes ) {
					/*
					*  Creation Logic
					*/
					
					try
					{

				     	AdhocCustomer adhocCustomer = createAdhocCustomer(   code  ,   name  ,   fiscalNumber  ,   identificationNumber  );
				    	
					//Success Validation
				     //Add the bean to be used in the View
					model.addAttribute("adhocCustomer",adhocCustomer);
return redirect("/treasury/accounting/managecustomer/customer/read/" + getAdhocCustomer(model).getExternalId(), model, redirectAttributes);
					}
					catch (DomainException de)
					{

						// @formatter: off
						/*
						 * If there is any error in validation 
					     *
					     * Add a error / warning message
					     * 
					     * addErrorMessage(" Error creating due to " + de.getLocalizedMessage(),model);
					     * addWarningMessage(" Warning creating due to "+ ex.getLocalizedMessage(),model); */
						// @formatter: on
						
						addErrorMessage(" Error creating due to " + de.getLocalizedMessage(),model);
				     	return create(model);
					}
				}
				
				@Atomic
				public AdhocCustomer createAdhocCustomer( java.lang.String code 
				, java.lang.String name 
				, java.lang.String fiscalNumber 
				, java.lang.String identificationNumber 
				) {
					
	// @formatter: off
					
						/*
						 * Modify the creation code here if you do not want to create
						 * the object with the default constructor and use the setter
						 * for each field
						 * 
						 */

						 // CHANGE_ME It's RECOMMENDED to use "Create service" in DomainObject
						 //AdhocCustomer adhocCustomer = adhocCustomer.create(fields_to_create);
						 
						 //Instead, use individual SETTERS and validate "CheckRules" in the end
	// @formatter: on

						AdhocCustomer adhocCustomer = AdhocCustomer.create(code, fiscalNumber, name, "", "", "", "");
						 return adhocCustomer;
				}
//				
				@RequestMapping(value = "/update/{oid}", method = RequestMethod.GET)
				public String update(@PathVariable("oid") AdhocCustomer adhocCustomer, Model model) {
					setAdhocCustomer(adhocCustomer, model);
					return "treasury/accounting/managecustomer/adhoccustomer/update";
				}
					 		
//				
				@RequestMapping(value = "/update/{oid}", method = RequestMethod.POST)
				public String update(@PathVariable("oid") AdhocCustomer adhocCustomer, @RequestParam(value="code", required=false) 
									java.lang.String 
									  code
				,@RequestParam(value="name", required=false) 
									java.lang.String 
									  name
				,@RequestParam(value="fiscalnumber", required=false) 
									java.lang.String 
									  fiscalNumber
				,@RequestParam(value="identificationnumber", required=false) 
									java.lang.String 
									  identificationNumber
				, Model model, RedirectAttributes redirectAttributes) {
					
					setAdhocCustomer(adhocCustomer,model);

				     try
				     {
					/*
					*  UpdateLogic here
					*/
				    		
						updateAdhocCustomer( code ,  name ,  fiscalNumber ,  identificationNumber ,  model);

					/*Succes Update */

return redirect("/treasury/accounting/managecustomer/customer/read/" + getAdhocCustomer(model).getExternalId(), model, redirectAttributes);
					}
					catch (DomainException de) 
					{
						// @formatter: off
				
						/*
					 	* If there is any error in validation 
				     	*
				     	* Add a error / warning message
				     	* 
				     	* addErrorMessage(" Error updating due to " + de.getLocalizedMessage(),model);
				     	* addWarningMessage(" Warning updating due to " + de.getLocalizedMessage(),model);
				     	*/
						// @formatter: on
										     
				     	addErrorMessage(" Error updating due to " + de.getLocalizedMessage(),model);
				     	return update(adhocCustomer,model);
					 

					}
				}
				
				@Atomic
				public void updateAdhocCustomer(  java.lang.String code 
				, java.lang.String name 
				, java.lang.String fiscalNumber 
				, java.lang.String identificationNumber 
				 ,  Model model) {
	
	// @formatter: off				
						/*
						 * Modify the update code here if you do not want to update
						 * the object with the default setter for each field
						 */

						 // CHANGE_ME It's RECOMMENDED to use "Edit service" in DomainObject
						//getAdhocCustomer(model).edit(fields_to_edit);
						
						//Instead, use individual SETTERS and validate "CheckRules" in the end
	// @formatter: on
	
						 getAdhocCustomer(model).setCode(code);
						 getAdhocCustomer(model).setName(name);
						 getAdhocCustomer(model).setFiscalNumber(fiscalNumber);
						 getAdhocCustomer(model).setIdentificationNumber(identificationNumber);
				}
				
}
