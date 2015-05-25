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
package org.fenixedu.treasury.ui.document.manageinvoice;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.ArrayList;
import java.util.stream.Stream;
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
import org.fenixedu.bennu.core.i18n.BundleUtil;
import org.fenixedu.bennu.TreasurySpringConfiguration;
import pt.ist.fenixframework.Atomic;

import org.fenixedu.treasury.ui.TreasuryBaseController;
import org.fenixedu.treasury.ui.TreasuryController;
import org.fenixedu.treasury.domain.document.CreditEntry;

//@Component("org.fenixedu.treasury.ui.document.manageInvoice") <-- Use for duplicate controller name disambiguation
//@SpringFunctionality(app = TreasuryController.class, title = "label.title.document.manageInvoice",accessGroup = "logged")// CHANGE_ME accessGroup = "group1 | group2 | groupXPTO"
//or
@BennuSpringController(value=TreasuryController.class) 
@RequestMapping(CreditEntryController.CONTROLLER_URL)
public class CreditEntryController extends TreasuryBaseController {
	
	
		public static final String CONTROLLER_URL ="/treasury/document/manageinvoice/creditentry"; 
//

				
	
				@RequestMapping
				public String home(Model model) {
					//this is the default behaviour, for handling in a Spring Functionality
					return "forward:" + CONTROLLER_URL + "/";
				}
				
				// @formatter: off
				
				/*
				* This should be used when using AngularJS in the JSP
				*/
				
				//private CreditEntry getCreditEntryBean(Model model)
				//{
				//	return (CreditEntry)model.asMap().get("creditEntryBean");
				//}
				//				
				//private void setCreditEntryBean (CreditEntryBean bean, Model model)
				//{
				//	model.addAttribute("creditEntryBeanJson", getBeanJson(bean));
        		//	model.addAttribute("creditEntryBean", bean);
				//}
								
				// @formatter: on

				private CreditEntry getCreditEntry(Model model)
				{
					return (CreditEntry)model.asMap().get("creditEntry");
				}
								
				private void setCreditEntry(CreditEntry creditEntry, Model model)
				{
					model.addAttribute("creditEntry", creditEntry);
				}
								
				@Atomic
				public void deleteCreditEntry(CreditEntry creditEntry) {
					// CHANGE_ME: Do the processing for deleting the creditEntry
					// Do not catch any exception here
					
					// creditEntry.delete();
				}

//				
				private static final String _CREATE_URI ="/create";
				public static final String  CREATE_URL = CONTROLLER_URL + _create_URI;
				@RequestMapping(value = _CREATE_URI, method = RequestMethod.GET)
				public String create(Model model) {
					model.addAttribute("CreditEntry_debitEntry_options", new ArrayList<org.fenixedu.treasury.domain.document.DebitEntry>()); // CHANGE_ME - MUST DEFINE RELATION
					//model.addAttribute("CreditEntry_debitEntry_options", org.fenixedu.treasury.domain.document.DebitEntry.findAll()); // CHANGE_ME - MUST DEFINE RELATION
					
					//IF ANGULAR, initialize the Bean
					//CreditEntryBean bean = new CreditEntryBean();
					//this.setCreditEntryBean(bean, model);
					
					return "treasury/document/manageinvoice/creditentry/create";
				}
						
//
//               THIS SHOULD BE USED ONLY WHEN USING ANGULAR 
//
//						// @formatter: off
//			
//				private static final String _CREATEPOSTBACK_URI ="/createpostback";
//				public static final String  CREATEPOSTBACK_URL = CONTROLLER_URL + _createPOSTBACK_URI;
//    			@RequestMapping(value = _CREATEPOSTBACK_URI, method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
//  			  	public @ResponseBody String createpostback(@RequestParam(value = "bean", required = false) CreditEntryBean bean,
//            		Model model) {
//
//        			// Do validation logic ?!?!
//        			this.setCreditEntryBean(bean, model);
//        			return getBeanJson(bean);
//    			}
//    			
//    			@RequestMapping(value = CREATE, method = RequestMethod.POST)
//  			  	public String create(@RequestParam(value = "bean", required = false) CreditEntryBean bean,
//            		Model model, RedirectAttributes redirectAttributes ) {
//
//					/*
//					*  Creation Logic
//					*/
//					
//					try
//					{
//
//				     	CreditEntry creditEntry = createCreditEntry(... get properties from bean ...,model);
//				    	
//					//Success Validation
//				     //Add the bean to be used in the View
//					model.addAttribute("creditEntry",creditEntry);
//				    return redirect("/treasury/document/manageinvoice/creditnote/read/" + getCreditEntry(model).getExternalId(), model, redirectAttributes);
//					}
//					catch (DomainException de)
//					{
//
//						/*
//						 * If there is any error in validation 
//					     *
//					     * Add a error / warning message
//					     * 
//					     * addErrorMessage(BundleUtil.getString(TreasurySpringConfiguration.BUNDLE, "label.error.create") + de.getLocalizedMessage(),model);
//					     * addWarningMessage(" Warning creating due to "+ ex.getLocalizedMessage(),model); */
//						
//						addErrorMessage(BundleUtil.getString(TreasurySpringConfiguration.BUNDLE, "label.error.create") + de.getLocalizedMessage(),model);
//				     	return create(model);
//					}
//    			}
//						// @formatter: on
    			
//				
				@RequestMapping(value = _CREATE_URI, method = RequestMethod.POST)
				public String create(@RequestParam(value="description", required=false) 
									java.lang.String 
									  description
				,@RequestParam(value="amount", required=false) 
									java.math.BigDecimal 
									  amount
				,@RequestParam(value="debitentry", required=false) 
									 org.fenixedu.treasury.domain.document.DebitEntry 
				 debitEntry
				, Model model, RedirectAttributes redirectAttributes ) {
					/*
					*  Creation Logic
					*/
					
					try
					{

				     	CreditEntry creditEntry = createCreditEntry(   description  ,   amount  ,   debitEntry  );
				    	
					//Success Validation
				     //Add the bean to be used in the View
					model.addAttribute("creditEntry",creditEntry);
return redirect("/treasury/document/manageinvoice/creditnote/read/" + getCreditEntry(model).getExternalId(), model, redirectAttributes);
					}
					catch (DomainException de)
					{

						// @formatter: off
						/*
						 * If there is any error in validation 
					     *
					     * Add a error / warning message
					     * 
					     * addErrorMessage(BundleUtil.getString(TreasurySpringConfiguration.BUNDLE, "label.error.create") + de.getLocalizedMessage(),model);
					     * addWarningMessage(" Warning creating due to "+ ex.getLocalizedMessage(),model); */
						// @formatter: on
						
						addErrorMessage(BundleUtil.getString(TreasurySpringConfiguration.BUNDLE, "label.error.create") + de.getLocalizedMessage(),model);
				     	return create(model);
					}
				}
				
				@Atomic
				public CreditEntry createCreditEntry( java.lang.String description 
				, java.math.BigDecimal amount 
				, org.fenixedu.treasury.domain.document.DebitEntry debitEntry 
				) {
					
	// @formatter: off
					
						/*
						 * Modify the creation code here if you do not want to create
						 * the object with the default constructor and use the setter
						 * for each field
						 * 
						 */

						 // CHANGE_ME It's RECOMMENDED to use "Create service" in DomainObject
						 //CreditEntry creditEntry = creditEntry.create(fields_to_create);
						 
						 //Instead, use individual SETTERS and validate "CheckRules" in the end
	// @formatter: on

						CreditEntry creditEntry = new CreditEntry();
						 creditEntry.setDescription(description);
						 creditEntry.setAmount(amount);
						 creditEntry.setDebitEntry(debitEntry);
						 
						 return creditEntry;
				}
//				
					private static final String _READ_URI ="/read/";
					public static final String  READ_URL = CONTROLLER_URL + _READ_URI;
					@RequestMapping(value = _READ_URI + "{oid}")
					public String read(@PathVariable("oid") CreditEntry creditEntry, Model model) {
						setCreditEntry(creditEntry,model);
						return "treasury/document/manageinvoice/creditentry/read";
					}

					
//
					private static final String _DELETE_URI ="/delete/";
					public static final String  DELETE_URL = CONTROLLER_URL + DELETE_URI;
					@RequestMapping(value = _DELETE_URI + "{oid}" , method = RequestMethod.POST)
					public String delete(@PathVariable("oid") CreditEntry creditEntry, Model model, RedirectAttributes redirectAttributes) {

						setCreditEntry(creditEntry,model);
						try{
							//call the Atomic delete function
							deleteCreditEntry(creditEntry);
							
							addInfoMessage("Sucess deleting CreditEntry ...",model);
		return redirect("/treasury/document/manageinvoice/creditnote/read", model, redirectAttributes);
						}
						catch(DomainException ex)
						{
							//Add error messages to the list
							addErrorMessage(BundleUtil.getString(TreasurySpringConfiguration.BUNDLE, "label.error.delete") + ex.getLocalizedMessage(),model);
						}
						
						//The default mapping is the same Read View
						return "treasury/document/manageinvoice/creditentry/read/" + getCreditEntry(model).getExternalId();
					}  
//				
				private static final String _UPDATE_URI ="/update/";
				public static final String  UPDATE_URL = CONTROLLER_URL + _update_URI;
				@RequestMapping(value = _UPDATE_URI + "{oid}", method = RequestMethod.GET)
				public String update(@PathVariable("oid") CreditEntry creditEntry, Model model) {
					setCreditEntry(creditEntry, model);
					return "treasury/document/manageinvoice/creditentry/update";
				}
					 		
//

//               THIS SHOULD BE USED ONLY WHEN USING ANGULAR 
//
//						// @formatter: off
//			
//				private static final String _UPDATEPOSTBACK_URI ="/updatepostback/";
//				public static final String  UPDATEPOSTBACK_URL = CONTROLLER_URL + _updatePOSTBACK_URI;
//    			@RequestMapping(value = _UPDATEPOSTBACK_URI + "{oid}", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
//  			  	public @ResponseBody String updatepostback(@PathVariable("oid") CreditEntry creditEntry, @RequestParam(value = "bean", required = false) CreditEntryBean bean,
//            		Model model) {
//
//        			// Do validation logic ?!?!
//        			this.setCreditEntryBean(bean, model);
//        			return getBeanJson(bean);
//    			} 
//    			
//    			@RequestMapping(value = _UPDATE_URI + "{oid}", method = RequestMethod.POST)
//  			  	public String update(@PathVariable("oid") CreditEntry creditEntry, @RequestParam(value = "bean", required = false) CreditEntryBean bean,
//            		Model model, RedirectAttributes redirectAttributes ) {
//					setCreditEntry(creditEntry,model);
//
//				     try
//				     {
//					/*
//					*  UpdateLogic here
//					*/
//				    		
//						updateCreditEntry( .. get fields from bean..., model);
//
//					/*Succes Update */
//
//				    return redirect("/treasury/document/manageinvoice/creditnote/read/" + getCreditEntry(model).getExternalId(), model, redirectAttributes);
//					}
//					catch (DomainException de) 
//					{
//				
//						/*
//					 	* If there is any error in validation 
//				     	*
//				     	* Add a error / warning message
//				     	* 
//				     	* addErrorMessage(BundleUtil.getString(TreasurySpringConfiguration.BUNDLE, "label.error.update") + de.getLocalizedMessage(),model);
//				     	* addWarningMessage(" Warning updating due to " + de.getLocalizedMessage(),model);
//				     	*/
//										     
//				     	addErrorMessage(BundleUtil.getString(TreasurySpringConfiguration.BUNDLE, "label.error.update") + de.getLocalizedMessage(),model);
//				     	return update(creditEntry,model);
//					 
//
//					}
//				}
//						// @formatter: on    			
//				
				@RequestMapping(value = _UPDATE_URI + "{oid}", method = RequestMethod.POST)
				public String update(@PathVariable("oid") CreditEntry creditEntry, @RequestParam(value="description", required=false) 
									java.lang.String 
									  description
				,@RequestParam(value="amount", required=false) 
									java.math.BigDecimal 
									  amount
				, Model model, RedirectAttributes redirectAttributes) {
					
					setCreditEntry(creditEntry,model);

				     try
				     {
					/*
					*  UpdateLogic here
					*/
				    		
						updateCreditEntry( description ,  amount ,  model);

					/*Succes Update */

return redirect("/treasury/document/manageinvoice/creditnote/read/" + getCreditEntry(model).getExternalId(), model, redirectAttributes);
					}
					catch (DomainException de) 
					{
						// @formatter: off
				
						/*
					 	* If there is any error in validation 
				     	*
				     	* Add a error / warning message
				     	* 
				     	* addErrorMessage(BundleUtil.getString(TreasurySpringConfiguration.BUNDLE, "label.error.update") + de.getLocalizedMessage(),model);
				     	* addWarningMessage(" Warning updating due to " + de.getLocalizedMessage(),model);
				     	*/
						// @formatter: on
										     
				     	addErrorMessage(BundleUtil.getString(TreasurySpringConfiguration.BUNDLE, "label.error.update") + de.getLocalizedMessage(),model);
				     	return update(creditEntry,model);
					 

					}
				}
				
				@Atomic
				public void updateCreditEntry(  java.lang.String description 
				, java.math.BigDecimal amount 
				 ,  Model model) {
	
	// @formatter: off				
						/*
						 * Modify the update code here if you do not want to update
						 * the object with the default setter for each field
						 */

						 // CHANGE_ME It's RECOMMENDED to use "Edit service" in DomainObject
						//getCreditEntry(model).edit(fields_to_edit);
						
						//Instead, use individual SETTERS and validate "CheckRules" in the end
	// @formatter: on
	
						 getCreditEntry(model).setDescription(description);
						 getCreditEntry(model).setAmount(amount);
				}
				
}
