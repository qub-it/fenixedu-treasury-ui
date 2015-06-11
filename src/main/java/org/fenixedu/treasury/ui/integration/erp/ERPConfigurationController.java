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
package org.fenixedu.treasury.ui.integration.erp;

import java.util.stream.Collectors;

import org.fenixedu.bennu.core.i18n.BundleUtil;
import org.fenixedu.bennu.spring.portal.BennuSpringController;
import org.fenixedu.treasury.domain.integration.ERPConfiguration;
import org.fenixedu.treasury.ui.TreasuryBaseController;
import org.fenixedu.treasury.ui.administration.managefinantialinstitution.FinantialInstitutionController;
import org.fenixedu.treasury.util.Constants;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import pt.ist.fenixframework.Atomic;

//@Component("org.fenixedu.treasury.ui.integration.erp") <-- Use for duplicate controller name disambiguation
//@SpringFunctionality(app = TreasuryController.class, title = "label.title.integration.erp",accessGroup = "logged")// CHANGE_ME accessGroup = "group1 | group2 | groupXPTO"
//or
@BennuSpringController(value = FinantialInstitutionController.class)
@RequestMapping(ERPConfigurationController.CONTROLLER_URL)
public class ERPConfigurationController extends TreasuryBaseController {

    public static final String CONTROLLER_URL = "/treasury/integration/erp/erpconfiguration";

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

    //private ERPConfigurationBean getERPConfigurationBean(Model model)
    //{
    //	return (ERPConfigurationBean)model.asMap().get("eRPConfigurationBean");
    //}
    //				
    //private void setERPConfigurationBean (ERPConfigurationBean bean, Model model)
    //{
    //	model.addAttribute("eRPConfigurationBeanJson", getBeanJson(bean));
    //	model.addAttribute("eRPConfigurationBean", bean);
    //}

    // @formatter: on

    private ERPConfiguration getERPConfiguration(Model model) {
        return (ERPConfiguration) model.asMap().get("eRPConfiguration");
    }

    private void setERPConfiguration(ERPConfiguration eRPConfiguration, Model model) {
        model.addAttribute("eRPConfiguration", eRPConfiguration);
    }

    @Atomic
    public void deleteERPConfiguration(ERPConfiguration eRPConfiguration) {
        // CHANGE_ME: Do the processing for deleting the eRPConfiguration
        // Do not catch any exception here

        // eRPConfiguration.delete();
    }

//				
    private static final String _READ_URI = "/read/";
    public static final String READ_URL = CONTROLLER_URL + _READ_URI;

    @RequestMapping(value = _READ_URI + "{oid}")
    public String read(@PathVariable("oid") ERPConfiguration eRPConfiguration, Model model) {
        setERPConfiguration(eRPConfiguration, model);
        return "treasury/integration/erp/erpconfiguration/read";
    }

//				
    private static final String _UPDATE_URI = "/update/";
    public static final String UPDATE_URL = CONTROLLER_URL + _UPDATE_URI;

    @RequestMapping(value = _UPDATE_URI + "{oid}", method = RequestMethod.GET)
    public String update(@PathVariable("oid") ERPConfiguration eRPConfiguration, Model model) {
        model.addAttribute(
                "ERPConfiguration_paymentsIntegrationSeries_options",
                org.fenixedu.treasury.domain.document.Series.find(eRPConfiguration.getFinantialInstitution()).stream()
                        .filter(x -> x.getExternSeries() == true).collect(Collectors.toList()));
        setERPConfiguration(eRPConfiguration, model);

        //IF ANGULAR, initialize the Bean
        //ERPConfigurationBean bean = new ERPConfigurationBean(eRPConfiguration);
        //this.setERPConfigurationBean(bean, model);

        return "treasury/integration/erp/erpconfiguration/update";

    }

//

//               THIS SHOULD BE USED ONLY WHEN USING ANGULAR 
//
//						// @formatter: off
//			
//				private static final String _UPDATEPOSTBACK_URI ="/updatepostback/";
//				public static final String  UPDATEPOSTBACK_URL = CONTROLLER_URL + _UPDATEPOSTBACK_URI;
//    			@RequestMapping(value = _UPDATEPOSTBACK_URI + "{oid}", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
//  			  	public @ResponseBody String updatepostback(@PathVariable("oid") ERPConfiguration eRPConfiguration, @RequestParam(value = "bean", required = false) ERPConfigurationBean bean,
//            		Model model) {
//
//        			// Do validation logic ?!?!
//        			this.setERPConfigurationBean(bean, model);
//        			return getBeanJson(bean);
//    			} 
//    			
//    			@RequestMapping(value = _UPDATE_URI + "{oid}", method = RequestMethod.POST)
//  			  	public String update(@PathVariable("oid") ERPConfiguration eRPConfiguration, @RequestParam(value = "bean", required = false) ERPConfigurationBean bean,
//            		Model model, RedirectAttributes redirectAttributes ) {
//					setERPConfiguration(eRPConfiguration,model);
//
//				     try
//				     {
//					/*
//					*  UpdateLogic here
//					*/
//				    		
//						updateERPConfiguration( .. get fields from bean..., model);
//
//					/*Succes Update */
//
//				    return redirect("/treasury/integration/erp/erpconfiguration/read/" + getERPConfiguration(model).getExternalId(), model, redirectAttributes);
//					}
//					catch (Exception de) 
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
//						setERPConfiguration(eRPConfiguration, model);
//						this.setERPConfigurationBean(bean, model);
//
//						return "treasury/integration/erp/erpconfiguration/update";
//					}
//				}
//						// @formatter: on    			
//				
    @RequestMapping(value = _UPDATE_URI + "{oid}", method = RequestMethod.POST)
    public String update(
            @PathVariable("oid") ERPConfiguration eRPConfiguration,
            @RequestParam(value = "exportannulledrelateddocuments", required = false) boolean exportAnnulledRelatedDocuments,
            @RequestParam(value = "externalurl", required = false) java.lang.String externalURL,
            @RequestParam(value = "username", required = false) java.lang.String username,
            @RequestParam(value = "password", required = false) java.lang.String password,
            @RequestParam(value = "paymentsintegrationseries", required = false) org.fenixedu.treasury.domain.document.Series paymentsIntegrationSeries,
            Model model, RedirectAttributes redirectAttributes) {

        setERPConfiguration(eRPConfiguration, model);

        try {
            /*
            *  UpdateLogic here
            */

            updateERPConfiguration(exportAnnulledRelatedDocuments, externalURL, username, password, paymentsIntegrationSeries,
                    model);

            /*Succes Update */

            return redirect("/treasury/integration/erp/erpconfiguration/read/" + getERPConfiguration(model).getExternalId(),
                    model, redirectAttributes);
        } catch (Exception de) {
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

            addErrorMessage(BundleUtil.getString(Constants.BUNDLE, "label.error.update") + de.getLocalizedMessage(), model);
            return update(eRPConfiguration, model);

        }
    }

    @Atomic
    public void updateERPConfiguration(boolean exportAnnulledRelatedDocuments, java.lang.String externalURL,
            java.lang.String username, java.lang.String password,
            org.fenixedu.treasury.domain.document.Series paymentsIntegrationSeries, Model model) {

        // @formatter: off				
        /*
         * Modify the update code here if you do not want to update
         * the object with the default setter for each field
         */

        // CHANGE_ME It's RECOMMENDED to use "Edit service" in DomainObject
        //getERPConfiguration(model).edit(fields_to_edit);

        //Instead, use individual SETTERS and validate "CheckRules" in the end
        // @formatter: on

        getERPConfiguration(model).setExportAnnulledRelatedDocuments(exportAnnulledRelatedDocuments);
        getERPConfiguration(model).setExternalURL(externalURL);
        getERPConfiguration(model).setUsername(username);
        getERPConfiguration(model).setPassword(password);
        getERPConfiguration(model).setPaymentsIntegrationSeries(paymentsIntegrationSeries);
    }

//

    //
    // This is the Eventtest Method for Screen update
    //
    @RequestMapping(value = "/update/{oid}/test")
    public String processUpdateToTest(@PathVariable("oid") ERPConfiguration eRPConfiguration, Model model,
            RedirectAttributes redirectAttributes) {
        setERPConfiguration(eRPConfiguration, model);
//
        /* Put here the logic for processing Event test 	*/
        //doSomething();

        // Now choose what is the Exit Screen	 
        return redirect("/treasury/integration/erp/erpconfiguration/update/" + getERPConfiguration(model).getExternalId(), model,
                redirectAttributes);
    }

}
