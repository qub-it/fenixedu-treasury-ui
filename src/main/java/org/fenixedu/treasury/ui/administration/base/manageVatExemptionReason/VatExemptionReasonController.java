package org.fenixedu.treasury.ui.administration.base.manageVatExemptionReason;

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
import org.fenixedu.bennu.core.domain.Bennu;
import pt.ist.fenixframework.Atomic;

import org.fenixedu.treasury.ui.TreasuryBaseController;
import org.fenixedu.treasury.ui.TreasuryController;
import org.fenixedu.treasury.domain.VatExemptionReason;

//@Component("org.fenixedu.treasury.ui.administration.base.manageVatExemptionReason") <-- Use for duplicate controller name disambiguation
@SpringFunctionality(app = TreasuryController.class, title = "label.title.administration.base.manageVatExemptionReason",accessGroup = "anyone")// CHANGE_ME accessGroup = "group1 | group2 | groupXPTO"
@RequestMapping("/treasury/administration/base/managevatexemptionreason/vatexemptionreason")
public class VatExemptionReasonController extends TreasuryBaseController {
	
//

				
	
				@RequestMapping
				public String home(Model model) {
					//this is the default behaviour, for handling in a Spring Functionality
					return "forward:/treasury/administration/base/managevatexemptionreason/vatexemptionreason/";
				}

				private VatExemptionReason getVatExemptionReason(Model m)
				{
					return (VatExemptionReason)m.asMap().get("vatExemptionReason");
				}
								
				private void setVatExemptionReason(VatExemptionReason vatExemptionReason, Model m)
				{
					m.addAttribute("vatExemptionReason", vatExemptionReason);
				}
								
				@Atomic
				public void deleteVatExemptionReason(VatExemptionReason vatExemptionReason) {
					// CHANGE_ME: Do the processing for deleting the vatExemptionReason
					// Do not catch any exception here
					
					// vatExemptionReason.delete();
				}

//				
					@RequestMapping(value = "/")
					public String search(@RequestParam(value="code", required=false) 
										java.lang.String 
										  code
					,@RequestParam(value="name", required=false) 
										org.fenixedu.commons.i18n.LocalizedString 
										  name
					, Model model) {
							List<VatExemptionReason> searchvatexemptionreasonResultsDataSet = filterSearchVatExemptionReason(   code  ,   name  );
						
						//add the results dataSet to the model
						model.addAttribute("searchvatexemptionreasonResultsDataSet",searchvatexemptionreasonResultsDataSet);
						return "treasury/administration/base/managevatexemptionreason/vatexemptionreason/search";
					}
					
				private List<VatExemptionReason> getSearchUniverseSearchVatExemptionReasonDataSet() {
					//
					//The initialization of the result list must be done here
					//
					//
					 return new ArrayList<VatExemptionReason>(VatExemptionReason.readAll()); //CHANGE_ME
				}
				
		private List<VatExemptionReason> filterSearchVatExemptionReason( java.lang.String code 
		, org.fenixedu.commons.i18n.LocalizedString name 
		) {
			
			return getSearchUniverseSearchVatExemptionReasonDataSet().stream()
				.filter(vatExemptionReason -> code == null || 
					 code.length() == 0 ||  (vatExemptionReason.getCode() != null && vatExemptionReason.getCode().length() > 0 && vatExemptionReason.getCode().toLowerCase().contains(code.toLowerCase())))
				.filter(vatExemptionReason -> name == null
					|| name.isEmpty()
					|| name.getLocales()
							.stream()
							.allMatch(
									locale -> vatExemptionReason.getName().getContent(locale) != null
											&& vatExemptionReason.getName().getContent(locale)
													.toLowerCase()
													.contains(name.getContent(locale).toLowerCase())))
				.collect(Collectors.toList());				
		}
		
		
				@RequestMapping(value = "/search/view/{oid}")
				public String processSearchToViewAction(@PathVariable("oid") VatExemptionReason vatExemptionReason,  Model model) {
		
			// CHANGE_ME Insert code here for processing viewAction
			// If you selected multiple exists you must choose which one to use below	 
		return "redirect:/treasury/administration/base/managevatexemptionreason/vatexemptionreason/read" + "/" + vatExemptionReason.getExternalId();
		}
		
		
		
		
//				
					@RequestMapping(value = "/read/{oid}")
					public String read(@PathVariable("oid") VatExemptionReason vatExemptionReason, Model model) {
						setVatExemptionReason(vatExemptionReason,model);
						return "treasury/administration/base/managevatexemptionreason/vatexemptionreason/read";
					}

					
//
					@RequestMapping(value = "/delete/{oid}")
					public String delete(@PathVariable("oid") VatExemptionReason vatExemptionReason, Model model) {

						setVatExemptionReason(vatExemptionReason,model);
						try{
							//call the Atomic delete function
							deleteVatExemptionReason(vatExemptionReason);
							
							addInfoMessage("Sucess deleting VatExemptionReason ...",model);
		return "redirect:/treasury/administration/base/managevatexemptionreason/vatexemptionreason/";
						}
						catch(DomainException ex)
						{
							//Add error messages to the list
							addErrorMessage("Error deleting the VatExemptionReason due to " + ex.getMessage(),model);
						}
						
						//The default mapping is the same Read View
						return "treasury/administration/base/managevatexemptionreason/vatexemptionreason/read/" + getVatExemptionReason(model).getExternalId();
					}  
//				
				@RequestMapping(value = "/create", method = RequestMethod.GET)
				public String create(Model model) {
					return "treasury/administration/base/managevatexemptionreason/vatexemptionreason/create";
				}
						
//				
				@RequestMapping(value = "/create", method = RequestMethod.POST)
				public String create(@RequestParam(value="code", required=false) 
									java.lang.String 
									  code
				,@RequestParam(value="name", required=false) 
									org.fenixedu.commons.i18n.LocalizedString 
									  name
				, Model model ) {
					/*
					*  Creation Logic
					*	
						do something();
					*    		
					*/

				     VatExemptionReason vatExemptionReason = createVatExemptionReason(   code  ,   name  );
				    		
					/*
				     * Success Validation
				     */					
				     
				     //Add the bean to be used in the View
					model.addAttribute("vatExemptionReason",vatExemptionReason);

return "redirect:/treasury/administration/base/managevatexemptionreason/vatexemptionreason/read/" + getVatExemptionReason(model).getExternalId();

				
					/*
					 * If there is any error in validation 
				     *
				     * Add a error / warning message
				     * 
				     * addErrorMessage(" Error because ...",model);
				     * addWarningMessage(" Waring becaus ...",model);
				     
				     
					 * 
				     * return create(model);
					 */
				}
				
				@Atomic
				public VatExemptionReason createVatExemptionReason( java.lang.String code 
				, org.fenixedu.commons.i18n.LocalizedString name 
				) {
						/*
						 * Modify the creation code here if you do not want to create
						 * the object with the default constructor and use the setter
						 * for each field
						 */
						VatExemptionReason vatExemptionReason = VatExemptionReason.create(code,name);
						 return vatExemptionReason;
				}
//				
				@RequestMapping(value = "/update/{oid}", method = RequestMethod.GET)
				public String update(@PathVariable("oid") VatExemptionReason vatExemptionReason, Model model) {
					setVatExemptionReason(vatExemptionReason, model);
					return "treasury/administration/base/managevatexemptionreason/vatexemptionreason/update";
				}
					 		
//				
				@RequestMapping(value = "/update/{oid}", method = RequestMethod.POST)
				public String update(@PathVariable("oid") VatExemptionReason vatExemptionReason, @RequestParam(value="code", required=false) 
									java.lang.String 
									  code
				,@RequestParam(value="name", required=false) 
									org.fenixedu.commons.i18n.LocalizedString 
									  name
				, Model model) {
					
					setVatExemptionReason(vatExemptionReason,model);

					/*
					*  UpdateLogic here
					*	
						do something();
					*    		
					*/
				    		
					/*
				     * Succes Update
				     */
					updateVatExemptionReason( code ,  name ,  model);

return "redirect:/treasury/administration/base/managevatexemptionreason/vatexemptionreason/read/" + getVatExemptionReason(model).getExternalId();
				
					/*
					 * If there is any error in validation 
				     *
				     * Add a error / warning message
				     * 
				     * addErrorMessage(" Error because ...",model);
				     * addWarningMessage(" Waring becaus ...",model);
				     
				     * return update(vatExemptionReason,model);
					 */
				}
				
				@Atomic
				public void updateVatExemptionReason(  java.lang.String code 
				, org.fenixedu.commons.i18n.LocalizedString name 
				 ,  Model m) {
						/*
						 * Modify the update code here if you do not want to update
						 * the object with the default setter for each field
						 */
						 getVatExemptionReason(m).setCode(code);
						 getVatExemptionReason(m).setName(name);
				}
				
}
