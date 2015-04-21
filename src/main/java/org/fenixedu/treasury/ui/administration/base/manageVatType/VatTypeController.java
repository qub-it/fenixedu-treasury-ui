package org.fenixedu.treasury.ui.administration.base.manageVatType;

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
import org.fenixedu.treasury.domain.VatType;

//@Component("org.fenixedu.treasury.ui.administration.base.manageVatType") <-- Use for duplicate controller name disambiguation
@SpringFunctionality(app = TreasuryController.class, title = "label.title.administration.base.manageVatType",accessGroup = "anyone")// CHANGE_ME accessGroup = "group1 | group2 | groupXPTO"
@RequestMapping("/treasury/administration/base/managevattype/vattype")
public class VatTypeController extends TreasuryBaseController {
	
//

				
	
				@RequestMapping
				public String home(Model model) {
					//this is the default behaviour, for handling in a Spring Functionality
					return "forward:/treasury/administration/base/managevattype/vattype/";
				}

				private VatType getVatType(Model m)
				{
					return (VatType)m.asMap().get("vatType");
				}
								
				private void setVatType(VatType vatType, Model m)
				{
					m.addAttribute("vatType", vatType);
				}
								
				@Atomic
				public void deleteVatType(VatType vatType) {
					// CHANGE_ME: Do the processing for deleting the vatType
					// Do not catch any exception here
					
					// vatType.delete();
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
							List<VatType> searchvattypeResultsDataSet = filterSearchVatType(   code  ,   name  );
						
						//add the results dataSet to the model
						model.addAttribute("searchvattypeResultsDataSet",searchvattypeResultsDataSet);
						return "treasury/administration/base/managevattype/vattype/search";
					}
					
				private List<VatType> getSearchUniverseSearchVatTypeDataSet() {
					//
					//The initialization of the result list must be done here
					//
					//
					return new ArrayList<VatType>(VatType.readAll()); //CHANGE_ME
				}
				
		private List<VatType> filterSearchVatType( java.lang.String code 
		, org.fenixedu.commons.i18n.LocalizedString name 
		) {
			
			return getSearchUniverseSearchVatTypeDataSet().stream()
				.filter(vatType -> code == null || 
					 code.length() == 0 ||  (vatType.getCode() != null && vatType.getCode().length() > 0 && vatType.getCode().toLowerCase().contains(code.toLowerCase())))
				.filter(vatType -> name == null
					|| name.isEmpty()
					|| name.getLocales()
							.stream()
							.allMatch(
									locale -> vatType.getName().getContent(locale) != null
											&& vatType.getName().getContent(locale)
													.toLowerCase()
													.contains(name.getContent(locale).toLowerCase())))
				.collect(Collectors.toList());				
		}
		
		
				@RequestMapping(value = "/search/view/{oid}")
				public String processSearchToViewAction(@PathVariable("oid") VatType vatType,  Model model) {
		
			// CHANGE_ME Insert code here for processing viewAction
			// If you selected multiple exists you must choose which one to use below	 
		return "redirect:/treasury/administration/base/managevattype/vattype/read" + "/" + vatType.getExternalId();
		}
		
		
		
		
//				
					@RequestMapping(value = "/read/{oid}")
					public String read(@PathVariable("oid") VatType vatType, Model model) {
						setVatType(vatType,model);
						return "treasury/administration/base/managevattype/vattype/read";
					}

					
//
					@RequestMapping(value = "/delete/{oid}")
					public String delete(@PathVariable("oid") VatType vatType, Model model) {

						setVatType(vatType,model);
						try{
							//call the Atomic delete function
							deleteVatType(vatType);
							
							addInfoMessage("Sucess deleting VatType ...",model);
		return "redirect:/treasury/administration/base/managevattype/vattype/";
						}
						catch(DomainException ex)
						{
							//Add error messages to the list
							addErrorMessage("Error deleting the VatType due to " + ex.getMessage(),model);
						}
						
						//The default mapping is the same Read View
						return "treasury/administration/base/managevattype/vattype/read/" + getVatType(model).getExternalId();
					}  
//				
				@RequestMapping(value = "/create", method = RequestMethod.GET)
				public String create(Model model) {
					return "treasury/administration/base/managevattype/vattype/create";
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

				     VatType vatType = createVatType(   code  ,   name  );
				    		
					/*
				     * Success Validation
				     */					
				     
				     //Add the bean to be used in the View
					model.addAttribute("vatType",vatType);

return "redirect:/treasury/administration/base/managevattype/vattype/read/" + getVatType(model).getExternalId();

				
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
				public VatType createVatType( java.lang.String code 
				, org.fenixedu.commons.i18n.LocalizedString name 
				) {
						/*
						 * Modify the creation code here if you do not want to create
						 * the object with the default constructor and use the setter
						 * for each field
						 */
						VatType vatType =  VatType.create(code,name);
						 return vatType;
				}
//				
				@RequestMapping(value = "/update/{oid}", method = RequestMethod.GET)
				public String update(@PathVariable("oid") VatType vatType, Model model) {
					setVatType(vatType, model);
					return "treasury/administration/base/managevattype/vattype/update";
				}
					 		
//				
				@RequestMapping(value = "/update/{oid}", method = RequestMethod.POST)
				public String update(@PathVariable("oid") VatType vatType, @RequestParam(value="code", required=false) 
									java.lang.String 
									  code
				,@RequestParam(value="name", required=false) 
									org.fenixedu.commons.i18n.LocalizedString 
									  name
				, Model model) {
					
					setVatType(vatType,model);

					/*
					*  UpdateLogic here
					*	
						do something();
					*    		
					*/
				    		
					/*
				     * Succes Update
				     */
					updateVatType( code ,  name ,  model);

return "redirect:/treasury/administration/base/managevattype/vattype/read/" + getVatType(model).getExternalId();
				
					/*
					 * If there is any error in validation 
				     *
				     * Add a error / warning message
				     * 
				     * addErrorMessage(" Error because ...",model);
				     * addWarningMessage(" Waring becaus ...",model);
				     
				     * return update(vatType,model);
					 */
				}
				
				@Atomic
				public void updateVatType(  java.lang.String code 
				, org.fenixedu.commons.i18n.LocalizedString name 
				 ,  Model m) {
						/*
						 * Modify the update code here if you do not want to update
						 * the object with the default setter for each field
						 */
						 getVatType(m).setCode(code);
						 getVatType(m).setName(name);
				}
				
}
