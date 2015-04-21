package org.fenixedu.treasury.ui.administration.base.manageExemption;

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
import org.fenixedu.treasury.domain.Exemption;

//@Component("org.fenixedu.treasury.ui.administration.base.manageExemption") <-- Use for duplicate controller name disambiguation
@SpringFunctionality(app = TreasuryController.class, title = "label.title.administration.base.manageExemption",accessGroup = "anyone")// CHANGE_ME accessGroup = "group1 | group2 | groupXPTO"
@RequestMapping("/treasury/administration/base/manageexemption/exemption")
public class ExemptionController extends TreasuryBaseController {
	
//

				
	
				@RequestMapping
				public String home(Model model) {
					//this is the default behaviour, for handling in a Spring Functionality
					return "forward:/treasury/administration/base/manageexemption/exemption/";
				}

				private Exemption getExemption(Model m)
				{
					return (Exemption)m.asMap().get("exemption");
				}
								
				private void setExemption(Exemption exemption, Model m)
				{
					m.addAttribute("exemption", exemption);
				}
								
				@Atomic
				public void deleteExemption(Exemption exemption) {
					// CHANGE_ME: Do the processing for deleting the exemption
					// Do not catch any exception here
					
					// exemption.delete();
				}

//				
					@RequestMapping(value = "/")
					public String search(@RequestParam(value="code", required=false) 
										java.lang.String 
										  code
					,@RequestParam(value="name", required=false) 
										org.fenixedu.commons.i18n.LocalizedString 
										  name
					,@RequestParam(value="discountrate", required=false) 
										java.math.BigDecimal 
										  discountRate
					, Model model) {
							List<Exemption> searchexemptionResultsDataSet = filterSearchExemption(   code  ,   name  ,   discountRate  );
						
						//add the results dataSet to the model
						model.addAttribute("searchexemptionResultsDataSet",searchexemptionResultsDataSet);
						return "treasury/administration/base/manageexemption/exemption/search";
					}
					
				private List<Exemption> getSearchUniverseSearchExemptionDataSet() {
					//
					//The initialization of the result list must be done here
					//
					//
					return new ArrayList<Exemption>(Exemption.readAll()); //CHANGE_ME
				}
				
		private List<Exemption> filterSearchExemption( java.lang.String code 
		, org.fenixedu.commons.i18n.LocalizedString name 
		, java.math.BigDecimal discountRate 
		) {
			
			return getSearchUniverseSearchExemptionDataSet().stream()
				.filter(exemption -> code == null || 
					 code.length() == 0 ||  (exemption.getCode() != null && exemption.getCode().length() > 0 && exemption.getCode().toLowerCase().contains(code.toLowerCase())))
				.filter(exemption -> name == null
					|| name.isEmpty()
					|| name.getLocales()
							.stream()
							.allMatch(
									locale -> exemption.getName().getContent(locale) != null
											&& exemption.getName().getContent(locale)
													.toLowerCase()
													.contains(name.getContent(locale).toLowerCase())))
				.filter(exemption -> discountRate == null || discountRate.equals(exemption.getDiscountRate()))
				.collect(Collectors.toList());				
		}
		
		
				@RequestMapping(value = "/search/view/{oid}")
				public String processSearchToViewAction(@PathVariable("oid") Exemption exemption,  Model model) {
		
			// CHANGE_ME Insert code here for processing viewAction
			// If you selected multiple exists you must choose which one to use below	 
		return "redirect:/treasury/administration/base/manageexemption/exemption/read" + "/" + exemption.getExternalId();
		}
		
		
		
		
//				
					@RequestMapping(value = "/read/{oid}")
					public String read(@PathVariable("oid") Exemption exemption, Model model) {
						setExemption(exemption,model);
						return "treasury/administration/base/manageexemption/exemption/read";
					}

					
//
					@RequestMapping(value = "/delete/{oid}")
					public String delete(@PathVariable("oid") Exemption exemption, Model model) {

						setExemption(exemption,model);
						try{
							//call the Atomic delete function
							deleteExemption(exemption);
							
							addInfoMessage("Sucess deleting Exemption ...",model);
		return "redirect:/treasury/administration/base/manageexemption/exemption/";
						}
						catch(DomainException ex)
						{
							//Add error messages to the list
							addErrorMessage("Error deleting the Exemption due to " + ex.getMessage(),model);
						}
						
						//The default mapping is the same Read View
						return "treasury/administration/base/manageexemption/exemption/read/" + getExemption(model).getExternalId();
					}  
//				
				@RequestMapping(value = "/create", method = RequestMethod.GET)
				public String create(Model model) {
					return "treasury/administration/base/manageexemption/exemption/create";
				}
						
//				
				@RequestMapping(value = "/create", method = RequestMethod.POST)
				public String create(@RequestParam(value="code", required=false) 
									java.lang.String 
									  code
				,@RequestParam(value="name", required=false) 
									org.fenixedu.commons.i18n.LocalizedString 
									  name
				,@RequestParam(value="discountrate", required=false) 
									java.math.BigDecimal 
									  discountRate
				, Model model ) {
					/*
					*  Creation Logic
					*	
						do something();
					*    		
					*/

				     Exemption exemption = createExemption(   code  ,   name  ,   discountRate  );
				    		
					/*
				     * Success Validation
				     */					
				     
				     //Add the bean to be used in the View
					model.addAttribute("exemption",exemption);

return "redirect:/treasury/administration/base/manageexemption/exemption/read/" + getExemption(model).getExternalId();

				
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
				public Exemption createExemption( java.lang.String code 
				, org.fenixedu.commons.i18n.LocalizedString name 
				, java.math.BigDecimal discountRate 
				) {
						/*
						 * Modify the creation code here if you do not want to create
						 * the object with the default constructor and use the setter
						 * for each field
						 */
						Exemption exemption = Exemption.create(code, name,discountRate);
						 return exemption;
				}
//				
				@RequestMapping(value = "/update/{oid}", method = RequestMethod.GET)
				public String update(@PathVariable("oid") Exemption exemption, Model model) {
					setExemption(exemption, model);
					return "treasury/administration/base/manageexemption/exemption/update";
				}
					 		
//				
				@RequestMapping(value = "/update/{oid}", method = RequestMethod.POST)
				public String update(@PathVariable("oid") Exemption exemption, @RequestParam(value="code", required=false) 
									java.lang.String 
									  code
				,@RequestParam(value="name", required=false) 
									org.fenixedu.commons.i18n.LocalizedString 
									  name
				,@RequestParam(value="discountrate", required=false) 
									java.math.BigDecimal 
									  discountRate
				, Model model) {
					
					setExemption(exemption,model);

					/*
					*  UpdateLogic here
					*	
						do something();
					*    		
					*/
				    		
					/*
				     * Succes Update
				     */
					updateExemption( code ,  name ,  discountRate ,  model);

return "redirect:/treasury/administration/base/manageexemption/exemption/read/" + getExemption(model).getExternalId();
				
					/*
					 * If there is any error in validation 
				     *
				     * Add a error / warning message
				     * 
				     * addErrorMessage(" Error because ...",model);
				     * addWarningMessage(" Waring becaus ...",model);
				     
				     * return update(exemption,model);
					 */
				}
				
				@Atomic
				public void updateExemption(  java.lang.String code 
				, org.fenixedu.commons.i18n.LocalizedString name 
				, java.math.BigDecimal discountRate 
				 ,  Model m) {
						/*
						 * Modify the update code here if you do not want to update
						 * the object with the default setter for each field
						 */
						 getExemption(m).setCode(code);
						 getExemption(m).setName(name);
						 getExemption(m).setDiscountRate(discountRate);
				}
				
}
