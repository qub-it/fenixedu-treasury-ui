/**
 * This file was created by Quorum Born IT <http://www.qub-it.com/> and its 
 * copyright terms are bind to the legal agreement regulating the FenixEdu@ULisboa 
 * software development project between Quorum Born IT and ServiÃ§os Partilhados da
 * Universidade de Lisboa:
 *  - Copyright Â© 2015 Quorum Born IT (until any Go-Live phase)
 *  - Copyright Â© 2015 Universidade de Lisboa (after any Go-Live phase)
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
package org.fenixedu.treasury.ui.administration.payments.sibs.managesibsinputfile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.fenixedu.bennu.core.domain.User;
import org.fenixedu.bennu.core.domain.exceptions.DomainException;
import org.fenixedu.bennu.core.i18n.BundleUtil;
import org.fenixedu.bennu.spring.portal.SpringFunctionality;
import org.fenixedu.treasury.domain.paymentcodes.SibsInputFile;
import org.fenixedu.treasury.ui.TreasuryBaseController;
import org.fenixedu.treasury.ui.TreasuryController;
import org.fenixedu.treasury.util.Constants;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import pt.ist.fenixframework.Atomic;

//@Component("org.fenixedu.treasury.ui.administration.payments.sibs.manageSibsInputFile") <-- Use for duplicate controller name disambiguation
@SpringFunctionality(app = TreasuryController.class, title = "label.title.administration.payments.sibs.manageSibsInputFile",
        accessGroup = "logged")
// CHANGE_ME accessGroup = "group1 | group2 | groupXPTO"
//or
//@BennuSpringController(value=TreasuryController.class) 
@RequestMapping(SibsInputFileController.CONTROLLER_URL)
public class SibsInputFileController extends TreasuryBaseController {

    public static final String CONTROLLER_URL = "/treasury/administration/payments/sibs/managesibsinputfile/sibsinputfile";

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

    //private SibsInputFile getSibsInputFileBean(Model model)
    //{
    //	return (SibsInputFile)model.asMap().get("sibsInputFileBean");
    //}
    //				
    //private void setSibsInputFileBean (SibsInputFileBean bean, Model model)
    //{
    //	model.addAttribute("sibsInputFileBeanJson", getBeanJson(bean));
    //	model.addAttribute("sibsInputFileBean", bean);
    //}

    // @formatter: on

    private SibsInputFile getSibsInputFile(Model model) {
        return (SibsInputFile) model.asMap().get("sibsInputFile");
    }

    private void setSibsInputFile(SibsInputFile sibsInputFile, Model model) {
        model.addAttribute("sibsInputFile", sibsInputFile);
    }

    @Atomic
    public void deleteSibsInputFile(SibsInputFile sibsInputFile) {
        sibsInputFile.delete();
    }

//				
    private static final String _SEARCH_URI = "/";
    public static final String SEARCH_URL = CONTROLLER_URL + _SEARCH_URI;

    @RequestMapping(value = _SEARCH_URI)
    public String search(@RequestParam(value = "uploader", required = false) org.fenixedu.bennu.core.domain.User uploader,
            Model model) {
        List<SibsInputFile> searchsibsinputfileResultsDataSet = filterSearchSibsInputFile(uploader);

        //add the results dataSet to the model
        model.addAttribute("searchsibsinputfileResultsDataSet", searchsibsinputfileResultsDataSet);
        model.addAttribute("SibsInputFile_uploader_options", new ArrayList<org.fenixedu.bennu.core.domain.User>()); // CHANGE_ME - MUST DEFINE RELATION
        //model.addAttribute("SibsInputFile_uploader_options", org.fenixedu.bennu.core.domain.User.findAll()); // CHANGE_ME - MUST DEFINE RELATION
        return "treasury/administration/payments/sibs/managesibsinputfile/sibsinputfile/search";
    }

    private Stream<SibsInputFile> getSearchUniverseSearchSibsInputFileDataSet() {
        //
        //The initialization of the result list must be done here
        //
        //
        return SibsInputFile.findAll();
        //return new ArrayList<SibsInputFile>().stream();
    }

    private List<SibsInputFile> filterSearchSibsInputFile(org.fenixedu.bennu.core.domain.User uploader) {

        return getSearchUniverseSearchSibsInputFileDataSet().filter(
                sibsInputFile -> uploader == null || uploader == sibsInputFile.getUploader()).collect(Collectors.toList());
    }

    private static final String _SEARCH_TO_VIEW_ACTION_URI = "/search/view/";
    public static final String SEARCH_TO_VIEW_ACTION_URL = CONTROLLER_URL + _SEARCH_TO_VIEW_ACTION_URI;

    @RequestMapping(value = _SEARCH_TO_VIEW_ACTION_URI + "{oid}")
    public String processSearchToViewAction(@PathVariable("oid") SibsInputFile sibsInputFile, Model model,
            RedirectAttributes redirectAttributes) {

        // CHANGE_ME Insert code here for processing viewAction
        // If you selected multiple exists you must choose which one to use below	 
        return redirect(
                "/treasury/administration/payments/sibs/managesibsinputfile/sibsinputfile/read" + "/"
                        + sibsInputFile.getExternalId(), model, redirectAttributes);
    }

//				
    private static final String _READ_URI = "/read/";
    public static final String READ_URL = CONTROLLER_URL + _READ_URI;

    @RequestMapping(value = _READ_URI + "{oid}")
    public String read(@PathVariable("oid") SibsInputFile sibsInputFile, Model model) {
        setSibsInputFile(sibsInputFile, model);
        return "treasury/administration/payments/sibs/managesibsinputfile/sibsinputfile/read";
    }

//
    private static final String _DELETE_URI = "/delete/";
    public static final String DELETE_URL = CONTROLLER_URL + _DELETE_URI;

    @RequestMapping(value = _DELETE_URI + "{oid}", method = RequestMethod.POST)
    public String delete(@PathVariable("oid") SibsInputFile sibsInputFile, Model model, RedirectAttributes redirectAttributes) {

        setSibsInputFile(sibsInputFile, model);
        try {
            //call the Atomic delete function
            deleteSibsInputFile(sibsInputFile);

            addInfoMessage(BundleUtil.getString(Constants.BUNDLE, "label.success.delete"), model);
            return redirect("/treasury/administration/payments/sibs/managesibsinputfile/sibsinputfile/", model,
                    redirectAttributes);
        } catch (DomainException ex) {
            //Add error messages to the list
            addErrorMessage(BundleUtil.getString(Constants.BUNDLE, "label.error.delete") + ex.getLocalizedMessage(), model);
        }

        //The default mapping is the same Read View
        return "treasury/administration/payments/sibs/managesibsinputfile/sibsinputfile/read/"
                + getSibsInputFile(model).getExternalId();
    }

//				
    private static final String _CREATE_URI = "/create";
    public static final String CREATE_URL = CONTROLLER_URL + _CREATE_URI;

    @RequestMapping(value = _CREATE_URI, method = RequestMethod.GET)
    public String create(Model model) {
        model.addAttribute("SibsInputFile_uploader_options", new ArrayList<org.fenixedu.bennu.core.domain.User>()); // CHANGE_ME - MUST DEFINE RELATION
        //model.addAttribute("SibsInputFile_uploader_options", org.fenixedu.bennu.core.domain.User.findAll()); // CHANGE_ME - MUST DEFINE RELATION

        //IF ANGULAR, initialize the Bean
        //SibsInputFileBean bean = new SibsInputFileBean();
        //this.setSibsInputFileBean(bean, model);

        return "treasury/administration/payments/sibs/managesibsinputfile/sibsinputfile/create";
    }

//
//               THIS SHOULD BE USED ONLY WHEN USING ANGULAR 
//
//						// @formatter: off
//			
//				private static final String _CREATEPOSTBACK_URI ="/createpostback";
//				public static final String  CREATEPOSTBACK_URL = CONTROLLER_URL + _createPOSTBACK_URI;
//    			@RequestMapping(value = _CREATEPOSTBACK_URI, method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
//  			  	public @ResponseBody String createpostback(@RequestParam(value = "bean", required = false) SibsInputFileBean bean,
//            		Model model) {
//
//        			// Do validation logic ?!?!
//        			this.setSibsInputFileBean(bean, model);
//        			return getBeanJson(bean);
//    			}
//    			
//    			@RequestMapping(value = CREATE, method = RequestMethod.POST)
//  			  	public String create(@RequestParam(value = "bean", required = false) SibsInputFileBean bean,
//            		Model model, RedirectAttributes redirectAttributes ) {
//
//					/*
//					*  Creation Logic
//					*/
//					
//					try
//					{
//
//				     	SibsInputFile sibsInputFile = createSibsInputFile(... get properties from bean ...,model);
//				    	
//					//Success Validation
//				     //Add the bean to be used in the View
//					model.addAttribute("sibsInputFile",sibsInputFile);
//				    return redirect("/treasury/administration/payments/sibs/managesibsinputfile/sibsinputfile/read/" + getSibsInputFile(model).getExternalId(), model, redirectAttributes);
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
    public String create(@RequestParam(value = "uploader", required = false) User uploader, @RequestParam(
            value = "sibsInputFile", required = false) MultipartFile sibsInputFile, Model model,
            RedirectAttributes redirectAttributes) {
        /*
        *  Creation Logic
        */

        try {

            SibsInputFile file = createSibsInputFile(uploader, sibsInputFile);

            //Success Validation
            //Add the bean to be used in the View
            model.addAttribute("sibsInputFile", file);
            return redirect(
                    "/treasury/administration/payments/sibs/managesibsinputfile/sibsinputfile/read/"
                            + getSibsInputFile(model).getExternalId(), model, redirectAttributes);
        } catch (DomainException de) {

            // @formatter: off
            /*
             * If there is any error in validation 
             *
             * Add a error / warning message
             * 
             * addErrorMessage(BundleUtil.getString(TreasurySpringConfiguration.BUNDLE, "label.error.create") + de.getLocalizedMessage(),model);
             * addWarningMessage(" Warning creating due to "+ ex.getLocalizedMessage(),model); */
            // @formatter: on

            addErrorMessage(BundleUtil.getString(Constants.BUNDLE, "label.error.create") + de.getLocalizedMessage(), model);
            return create(model);
        }
    }

    @Atomic
    public SibsInputFile createSibsInputFile(User uploader, MultipartFile requestFile) {

        // @formatter: off

        /*
         * Modify the creation code here if you do not want to create
         * the object with the default constructor and use the setter
         * for each field
         * 
         */

        // CHANGE_ME It's RECOMMENDED to use "Create service" in DomainObject
        //SibsInputFile sibsInputFile = sibsInputFile.create(fields_to_create);

        //Instead, use individual SETTERS and validate "CheckRules" in the end
        // @formatter: on

        SibsInputFile sibsInputFile =
                SibsInputFile.create(requestFile.getName(), requestFile.getOriginalFilename(), getContent(requestFile), uploader);

        return sibsInputFile;
    }

    //ACFSILVA - how to handle this exception
    private byte[] getContent(MultipartFile requestFile) {
        try {
            return requestFile.getBytes();
        } catch (IOException e) {
            return null;
        }
    }

//				
    private static final String _UPDATE_URI = "/update/";
    public static final String UPDATE_URL = CONTROLLER_URL + _UPDATE_URI;

    @RequestMapping(value = _UPDATE_URI + "{oid}", method = RequestMethod.GET)
    public String update(@PathVariable("oid") SibsInputFile sibsInputFile, Model model) {
        model.addAttribute("SibsInputFile_uploader_options", new ArrayList<org.fenixedu.bennu.core.domain.User>()); // CHANGE_ME - MUST DEFINE RELATION
        //model.addAttribute("SibsInputFile_uploader_options", org.fenixedu.bennu.core.domain.User.findAll()); // CHANGE_ME - MUST DEFINE RELATION
        setSibsInputFile(sibsInputFile, model);
        return "treasury/administration/payments/sibs/managesibsinputfile/sibsinputfile/update";
    }

//

//               THIS SHOULD BE USED ONLY WHEN USING ANGULAR 
//
//						// @formatter: off
//			
//				private static final String _UPDATEPOSTBACK_URI ="/updatepostback/";
//				public static final String  UPDATEPOSTBACK_URL = CONTROLLER_URL + _updatePOSTBACK_URI;
//    			@RequestMapping(value = _UPDATEPOSTBACK_URI + "{oid}", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
//  			  	public @ResponseBody String updatepostback(@PathVariable("oid") SibsInputFile sibsInputFile, @RequestParam(value = "bean", required = false) SibsInputFileBean bean,
//            		Model model) {
//
//        			// Do validation logic ?!?!
//        			this.setSibsInputFileBean(bean, model);
//        			return getBeanJson(bean);
//    			} 
//    			
//    			@RequestMapping(value = _UPDATE_URI + "{oid}", method = RequestMethod.POST)
//  			  	public String update(@PathVariable("oid") SibsInputFile sibsInputFile, @RequestParam(value = "bean", required = false) SibsInputFileBean bean,
//            		Model model, RedirectAttributes redirectAttributes ) {
//					setSibsInputFile(sibsInputFile,model);
//
//				     try
//				     {
//					/*
//					*  UpdateLogic here
//					*/
//				    		
//						updateSibsInputFile( .. get fields from bean..., model);
//
//					/*Succes Update */
//
//				    return redirect("/treasury/administration/payments/sibs/managesibsinputfile/sibsinputfile/read/" + getSibsInputFile(model).getExternalId(), model, redirectAttributes);
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
//				     	return update(sibsInputFile,model);
//					 
//
//					}
//				}
//						// @formatter: on    			
//				
    @RequestMapping(value = _UPDATE_URI + "{oid}", method = RequestMethod.POST)
    public String update(@PathVariable("oid") SibsInputFile sibsInputFile,
            @RequestParam(value = "uploader", required = false) org.fenixedu.bennu.core.domain.User uploader, Model model,
            RedirectAttributes redirectAttributes) {

        setSibsInputFile(sibsInputFile, model);

        try {
            /*
            *  UpdateLogic here
            */

            updateSibsInputFile(uploader, model);

            /*Succes Update */

            return redirect(
                    "/treasury/administration/payments/sibs/managesibsinputfile/sibsinputfile/read/"
                            + getSibsInputFile(model).getExternalId(), model, redirectAttributes);
        } catch (DomainException de) {
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
            return update(sibsInputFile, model);

        }
    }

    @Atomic
    public void updateSibsInputFile(org.fenixedu.bennu.core.domain.User uploader, Model model) {

        // @formatter: off				
        /*
         * Modify the update code here if you do not want to update
         * the object with the default setter for each field
         */

        // CHANGE_ME It's RECOMMENDED to use "Edit service" in DomainObject
        //getSibsInputFile(model).edit(fields_to_edit);

        //Instead, use individual SETTERS and validate "CheckRules" in the end
        // @formatter: on

        getSibsInputFile(model).setUploader(uploader);
    }

}
