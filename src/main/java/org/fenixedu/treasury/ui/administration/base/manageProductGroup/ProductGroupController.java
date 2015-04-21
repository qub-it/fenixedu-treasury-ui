package org.fenixedu.treasury.ui.administration.base.manageProductGroup;

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
import org.fenixedu.treasury.domain.ProductGroup;

//@Component("org.fenixedu.treasury.ui.administration.base.manageProductGroup") <-- Use for duplicate controller name disambiguation
@SpringFunctionality(app = TreasuryController.class, title = "label.title.administration.base.manageProductGroup",accessGroup = "anyone")// CHANGE_ME accessGroup = "group1 | group2 | groupXPTO"
@RequestMapping("/treasury/administration/base/manageproductgroup/productgroup")
public class ProductGroupController extends TreasuryBaseController {
	
//

				
	
				@RequestMapping
				public String home(Model model) {
					//this is the default behaviour, for handling in a Spring Functionality
					return "forward:/treasury/administration/base/manageproductgroup/productgroup/";
				}

				private ProductGroup getProductGroup(Model m)
				{
					return (ProductGroup)m.asMap().get("productGroup");
				}
								
				private void setProductGroup(ProductGroup productGroup, Model m)
				{
					m.addAttribute("productGroup", productGroup);
				}
								
				@Atomic
				public void deleteProductGroup(ProductGroup productGroup) {
					// CHANGE_ME: Do the processing for deleting the productGroup
					// Do not catch any exception here
					
					// productGroup.delete();
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
							List<ProductGroup> searchproductgroupResultsDataSet = filterSearchProductGroup(   code  ,   name  );
						
						//add the results dataSet to the model
						model.addAttribute("searchproductgroupResultsDataSet",searchproductgroupResultsDataSet);
						return "treasury/administration/base/manageproductgroup/productgroup/search";
					}
					
				private List<ProductGroup> getSearchUniverseSearchProductGroupDataSet() {
					//
					//The initialization of the result list must be done here
					//
					//
					return new ArrayList<ProductGroup>(ProductGroup.readAll()); //CHANGE_ME
				}
				
		private List<ProductGroup> filterSearchProductGroup( java.lang.String code 
		, org.fenixedu.commons.i18n.LocalizedString name 
		) {
			
			return getSearchUniverseSearchProductGroupDataSet().stream()
				.filter(productGroup -> code == null || 
					 code.length() == 0 ||  (productGroup.getCode() != null && productGroup.getCode().length() > 0 && productGroup.getCode().toLowerCase().contains(code.toLowerCase())))
				.filter(productGroup -> name == null
					|| name.isEmpty()
					|| name.getLocales()
							.stream()
							.allMatch(
									locale -> productGroup.getName().getContent(locale) != null
											&& productGroup.getName().getContent(locale)
													.toLowerCase()
													.contains(name.getContent(locale).toLowerCase())))
				.collect(Collectors.toList());				
		}
		
		
				@RequestMapping(value = "/search/view/{oid}")
				public String processSearchToViewAction(@PathVariable("oid") ProductGroup productGroup,  Model model) {
		
			// CHANGE_ME Insert code here for processing viewAction
			// If you selected multiple exists you must choose which one to use below	 
		return "redirect:/treasury/administration/base/manageproductgroup/productgroup/read" + "/" + productGroup.getExternalId();
		}
		
		
		
		
//				
					@RequestMapping(value = "/read/{oid}")
					public String read(@PathVariable("oid") ProductGroup productGroup, Model model) {
						setProductGroup(productGroup,model);
						return "treasury/administration/base/manageproductgroup/productgroup/read";
					}

					
//
					@RequestMapping(value = "/delete/{oid}")
					public String delete(@PathVariable("oid") ProductGroup productGroup, Model model) {

						setProductGroup(productGroup,model);
						try{
							//call the Atomic delete function
							deleteProductGroup(productGroup);
							
							addInfoMessage("Sucess deleting ProductGroup ...",model);
		return "redirect:/treasury/administration/base/manageproductgroup/productgroup/";
						}
						catch(DomainException ex)
						{
							//Add error messages to the list
							addErrorMessage("Error deleting the ProductGroup due to " + ex.getMessage(),model);
						}
						
						//The default mapping is the same Read View
						return "treasury/administration/base/manageproductgroup/productgroup/read/" + getProductGroup(model).getExternalId();
					}  
//				
				@RequestMapping(value = "/create", method = RequestMethod.GET)
				public String create(Model model) {
					return "treasury/administration/base/manageproductgroup/productgroup/create";
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

				     ProductGroup productGroup = createProductGroup(   code  ,   name  );
				    		
					/*
				     * Success Validation
				     */					
				     
				     //Add the bean to be used in the View
					model.addAttribute("productGroup",productGroup);

return "redirect:/treasury/administration/base/manageproductgroup/productgroup/read/" + getProductGroup(model).getExternalId();

				
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
				public ProductGroup createProductGroup( java.lang.String code 
				, org.fenixedu.commons.i18n.LocalizedString name 
				) {
						/*
						 * Modify the creation code here if you do not want to create
						 * the object with the default constructor and use the setter
						 * for each field
						 */
						ProductGroup productGroup = ProductGroup.create(code,name);
						 return productGroup;
				}
//				
				@RequestMapping(value = "/update/{oid}", method = RequestMethod.GET)
				public String update(@PathVariable("oid") ProductGroup productGroup, Model model) {
					setProductGroup(productGroup, model);
					return "treasury/administration/base/manageproductgroup/productgroup/update";
				}
					 		
//				
				@RequestMapping(value = "/update/{oid}", method = RequestMethod.POST)
				public String update(@PathVariable("oid") ProductGroup productGroup, @RequestParam(value="code", required=false) 
									java.lang.String 
									  code
				,@RequestParam(value="name", required=false) 
									org.fenixedu.commons.i18n.LocalizedString 
									  name
				, Model model) {
					
					setProductGroup(productGroup,model);

					/*
					*  UpdateLogic here
					*	
						do something();
					*    		
					*/
				    		
					/*
				     * Succes Update
				     */
					updateProductGroup( code ,  name ,  model);

return "redirect:/treasury/administration/base/manageproductgroup/productgroup/read/" + getProductGroup(model).getExternalId();
				
					/*
					 * If there is any error in validation 
				     *
				     * Add a error / warning message
				     * 
				     * addErrorMessage(" Error because ...",model);
				     * addWarningMessage(" Waring becaus ...",model);
				     
				     * return update(productGroup,model);
					 */
				}
				
				@Atomic
				public void updateProductGroup(  java.lang.String code 
				, org.fenixedu.commons.i18n.LocalizedString name 
				 ,  Model m) {
						/*
						 * Modify the update code here if you do not want to update
						 * the object with the default setter for each field
						 */
						 getProductGroup(m).setCode(code);
						 getProductGroup(m).setName(name);
				}
				
}
