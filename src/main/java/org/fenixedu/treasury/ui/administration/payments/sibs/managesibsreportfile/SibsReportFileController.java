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
package org.fenixedu.treasury.ui.administration.payments.sibs.managesibsreportfile;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.servlet.http.HttpServletResponse;

import org.fenixedu.bennu.core.domain.exceptions.DomainException;
import org.fenixedu.bennu.core.i18n.BundleUtil;
import org.fenixedu.bennu.spring.portal.SpringFunctionality;
import org.fenixedu.treasury.domain.exceptions.TreasuryDomainException;
import org.fenixedu.treasury.domain.paymentcodes.SibsReportFile;
import org.fenixedu.treasury.ui.TreasuryBaseController;
import org.fenixedu.treasury.ui.TreasuryController;
import org.fenixedu.treasury.util.Constants;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import pt.ist.fenixframework.Atomic;

//@Component("org.fenixedu.treasury.ui.administration.payments.sibs.manageSibsReportFile") <-- Use for duplicate controller name disambiguation
@SpringFunctionality(app = TreasuryController.class, title = "label.title.administration.payments.sibs.manageSibsReportFile",
        accessGroup = "#managers")
// CHANGE_ME accessGroup = "group1 | group2 | groupXPTO"
//or
//@BennuSpringController(value=TreasuryController.class) 
@RequestMapping(SibsReportFileController.CONTROLLER_URL)
public class SibsReportFileController extends TreasuryBaseController {

    public static final String CONTROLLER_URL = "/treasury/administration/payments/sibs/managesibsreportfile/sibsreportfile";

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

    //private SibsReportFile getSibsReportFileBean(Model model)
    //{
    //	return (SibsReportFile)model.asMap().get("sibsReportFileBean");
    //}
    //				
    //private void setSibsReportFileBean (SibsReportFileBean bean, Model model)
    //{
    //	model.addAttribute("sibsReportFileBeanJson", getBeanJson(bean));
    //	model.addAttribute("sibsReportFileBean", bean);
    //}

    // @formatter: on

    private SibsReportFile getSibsReportFile(Model model) {
        return (SibsReportFile) model.asMap().get("sibsReportFile");
    }

    private void setSibsReportFile(SibsReportFile sibsReportFile, Model model) {
        model.addAttribute("sibsReportFile", sibsReportFile);
    }

    @Atomic
    public void deleteSibsReportFile(SibsReportFile sibsReportFile) {
        sibsReportFile.delete();
    }

//				
    private static final String _SEARCH_URI = "/";
    public static final String SEARCH_URL = CONTROLLER_URL + _SEARCH_URI;

    @RequestMapping(value = _SEARCH_URI)
    public String search(@RequestParam(value = "whenprocessedbysibs", required = false) @DateTimeFormat(
            pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSZ") org.joda.time.LocalDate whenProcessedBySibs, @RequestParam(
            value = "transactionstotalamount", required = false) java.math.BigDecimal transactionsTotalAmount, @RequestParam(
            value = "totalcost", required = false) java.math.BigDecimal totalCost, Model model) {
        List<SibsReportFile> searchsibsreportfileResultsDataSet =
                filterSearchSibsReportFile(whenProcessedBySibs, transactionsTotalAmount, totalCost);

        //add the results dataSet to the model
        model.addAttribute("searchsibsreportfileResultsDataSet", searchsibsreportfileResultsDataSet);
        return "treasury/administration/payments/sibs/managesibsreportfile/sibsreportfile/search";
    }

    private Stream<SibsReportFile> getSearchUniverseSearchSibsReportFileDataSet() {
        //
        //The initialization of the result list must be done here
        //
        //
        return SibsReportFile.findAll();
        //return new ArrayList<SibsReportFile>().stream();
    }

    private List<SibsReportFile> filterSearchSibsReportFile(org.joda.time.LocalDate whenProcessedBySibs,
            java.math.BigDecimal transactionsTotalAmount, java.math.BigDecimal totalCost) {

        return getSearchUniverseSearchSibsReportFileDataSet()
                .filter(sibsReportFile -> whenProcessedBySibs == null
                        || whenProcessedBySibs.equals(sibsReportFile.getWhenProcessedBySibs()))
                .filter(sibsReportFile -> transactionsTotalAmount == null
                        || transactionsTotalAmount.equals(sibsReportFile.getTransactionsTotalAmount()))
                .filter(sibsReportFile -> totalCost == null || totalCost.equals(sibsReportFile.getTotalCost()))
                .collect(Collectors.toList());
    }

    private static final String _SEARCH_TO_VIEW_ACTION_URI = "/search/view/";
    public static final String SEARCH_TO_VIEW_ACTION_URL = CONTROLLER_URL + _SEARCH_TO_VIEW_ACTION_URI;

    @RequestMapping(value = _SEARCH_TO_VIEW_ACTION_URI + "{oid}")
    public String processSearchToViewAction(@PathVariable("oid") SibsReportFile sibsReportFile, Model model,
            RedirectAttributes redirectAttributes) {

        // CHANGE_ME Insert code here for processing viewAction
        // If you selected multiple exists you must choose which one to use below	 
        return redirect(
                "/treasury/administration/payments/sibs/managesibsreportfile/sibsreportfile/read" + "/"
                        + sibsReportFile.getExternalId(), model, redirectAttributes);
    }

//				
    private static final String _READ_URI = "/read/";
    public static final String READ_URL = CONTROLLER_URL + _READ_URI;

    @RequestMapping(value = _READ_URI + "{oid}")
    public String read(@PathVariable("oid") SibsReportFile sibsReportFile, Model model) {
        setSibsReportFile(sibsReportFile, model);
        return "treasury/administration/payments/sibs/managesibsreportfile/sibsreportfile/read";
    }

//
    private static final String _DELETE_URI = "/delete/";
    public static final String DELETE_URL = CONTROLLER_URL + _DELETE_URI;

    @RequestMapping(value = _DELETE_URI + "{oid}", method = RequestMethod.POST)
    public String delete(@PathVariable("oid") SibsReportFile sibsReportFile, Model model, RedirectAttributes redirectAttributes) {

        setSibsReportFile(sibsReportFile, model);
        try {
            //call the Atomic delete function
            deleteSibsReportFile(sibsReportFile);

            addInfoMessage(BundleUtil.getString(Constants.BUNDLE, "label.success.delete"), model);
            return redirect("/treasury/administration/payments/sibs/managesibsreportfile/sibsreportfile/", model,
                    redirectAttributes);
        } catch (DomainException ex) {
            //Add error messages to the list
            addErrorMessage(BundleUtil.getString(Constants.BUNDLE, "label.error.delete") + ex.getLocalizedMessage(), model);
        }

        //The default mapping is the same Read View
        return "treasury/administration/payments/sibs/managesibsreportfile/sibsreportfile/read/"
                + getSibsReportFile(model).getExternalId();
    }

//				
    private static final String _CREATE_URI = "/create";
    public static final String CREATE_URL = CONTROLLER_URL + _CREATE_URI;

    @RequestMapping(value = _CREATE_URI, method = RequestMethod.GET)
    public String create(Model model) {

        //IF ANGULAR, initialize the Bean
        //SibsReportFileBean bean = new SibsReportFileBean();
        //this.setSibsReportFileBean(bean, model);

        return "treasury/administration/payments/sibs/managesibsreportfile/sibsreportfile/create";
    }

//
//               THIS SHOULD BE USED ONLY WHEN USING ANGULAR 
//
//						// @formatter: off
//			
//				private static final String _CREATEPOSTBACK_URI ="/createpostback";
//				public static final String  CREATEPOSTBACK_URL = CONTROLLER_URL + _createPOSTBACK_URI;
//    			@RequestMapping(value = _CREATEPOSTBACK_URI, method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
//  			  	public @ResponseBody String createpostback(@RequestParam(value = "bean", required = false) SibsReportFileBean bean,
//            		Model model) {
//
//        			// Do validation logic ?!?!
//        			this.setSibsReportFileBean(bean, model);
//        			return getBeanJson(bean);
//    			}
//    			
//    			@RequestMapping(value = CREATE, method = RequestMethod.POST)
//  			  	public String create(@RequestParam(value = "bean", required = false) SibsReportFileBean bean,
//            		Model model, RedirectAttributes redirectAttributes ) {
//
//					/*
//					*  Creation Logic
//					*/
//					
//					try
//					{
//
//				     	SibsReportFile sibsReportFile = createSibsReportFile(... get properties from bean ...,model);
//				    	
//					//Success Validation
//				     //Add the bean to be used in the View
//					model.addAttribute("sibsReportFile",sibsReportFile);
//				    return redirect("/treasury/administration/payments/sibs/managesibsreportfile/sibsreportfile/read/" + getSibsReportFile(model).getExternalId(), model, redirectAttributes);
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
    public String create(@RequestParam(value = "whenprocessedbysibs", required = false) @DateTimeFormat(
            pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSZ") org.joda.time.DateTime whenProcessedBySibs, @RequestParam(
            value = "documentSibsReportFile", required = true) MultipartFile documentSibsReportFile, @RequestParam(
            value = "transactionstotalamount", required = false) java.math.BigDecimal transactionsTotalAmount, @RequestParam(
            value = "totalcost", required = false) java.math.BigDecimal totalCost, Model model,
            RedirectAttributes redirectAttributes) {
        /*
        *  Creation Logic
        */

        try {

            SibsReportFile sibsReportFile =
                    createSibsReportFile(whenProcessedBySibs, documentSibsReportFile, transactionsTotalAmount, totalCost);

            //Success Validation
            //Add the bean to be used in the View
            model.addAttribute("sibsReportFile", sibsReportFile);
            return redirect(READ_URL + getSibsReportFile(model).getExternalId(), model, redirectAttributes);
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
    public SibsReportFile createSibsReportFile(org.joda.time.DateTime whenProcessedBySibs, MultipartFile documentSibsReportFile,
            java.math.BigDecimal transactionsTotalAmount, java.math.BigDecimal totalCost) {

        // @formatter: off

        /*
         * Modify the creation code here if you do not want to create
         * the object with the default constructor and use the setter
         * for each field
         * 
         */

        // CHANGE_ME It's RECOMMENDED to use "Create service" in DomainObject
        //SibsReportFile sibsReportFile = sibsReportFile.create(fields_to_create);

        //Instead, use individual SETTERS and validate "CheckRules" in the end
        // @formatter: on

        if (!documentSibsReportFile.getContentType().equals(SibsReportFile.CONTENT_TYPE)) {
            throw new TreasuryDomainException("error.file.different.content.type");
        }

        SibsReportFile sibsReportFile =
                SibsReportFile.create(whenProcessedBySibs, transactionsTotalAmount, totalCost, documentSibsReportFile.getName(),
                        documentSibsReportFile.getOriginalFilename(), getContent(documentSibsReportFile));

        return sibsReportFile;
    }

    private static final String _DOWNLOAD_URI = "/read/download/";
    public static final String DOWNLOAD_URL = CONTROLLER_URL + _DOWNLOAD_URI;

    @RequestMapping(value = _DOWNLOAD_URI + "{oid}", method = RequestMethod.GET)
    public void processReadToDownloadFile(@PathVariable("oid") SibsReportFile sibsReportFile, Model model,
            RedirectAttributes redirectAttributes, HttpServletResponse response) {
        setSibsReportFile(sibsReportFile, model);
        try {
            response.setContentType(sibsReportFile.getContentType());
            String filename = sibsReportFile.getFilename();
            response.setHeader("Content-disposition", "attachment; filename=" + filename);
            response.getOutputStream().write(sibsReportFile.getContent());
        } catch (Exception ex) {
            addErrorMessage(ex.getLocalizedMessage(), model);
            try {
                response.sendRedirect(redirect(READ_URL + getSibsReportFile(model).getExternalId(), model, redirectAttributes));
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }

    public void uploadSibsReportFile(SibsReportFile sibsReportFile, MultipartFile requestFile, Model model) {

    }

    //TODOJN - how to handle this exception
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
    public String update(@PathVariable("oid") SibsReportFile sibsReportFile, Model model) {
        setSibsReportFile(sibsReportFile, model);
        return "treasury/administration/payments/sibs/managesibsreportfile/sibsreportfile/update";
    }

//

//               THIS SHOULD BE USED ONLY WHEN USING ANGULAR 
//
//						// @formatter: off
//			
//				private static final String _UPDATEPOSTBACK_URI ="/updatepostback/";
//				public static final String  UPDATEPOSTBACK_URL = CONTROLLER_URL + _updatePOSTBACK_URI;
//    			@RequestMapping(value = _UPDATEPOSTBACK_URI + "{oid}", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
//  			  	public @ResponseBody String updatepostback(@PathVariable("oid") SibsReportFile sibsReportFile, @RequestParam(value = "bean", required = false) SibsReportFileBean bean,
//            		Model model) {
//
//        			// Do validation logic ?!?!
//        			this.setSibsReportFileBean(bean, model);
//        			return getBeanJson(bean);
//    			} 
//    			
//    			@RequestMapping(value = _UPDATE_URI + "{oid}", method = RequestMethod.POST)
//  			  	public String update(@PathVariable("oid") SibsReportFile sibsReportFile, @RequestParam(value = "bean", required = false) SibsReportFileBean bean,
//            		Model model, RedirectAttributes redirectAttributes ) {
//					setSibsReportFile(sibsReportFile,model);
//
//				     try
//				     {
//					/*
//					*  UpdateLogic here
//					*/
//				    		
//						updateSibsReportFile( .. get fields from bean..., model);
//
//					/*Succes Update */
//
//				    return redirect("/treasury/administration/payments/sibs/managesibsreportfile/sibsreportfile/read/" + getSibsReportFile(model).getExternalId(), model, redirectAttributes);
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
//				     	return update(sibsReportFile,model);
//					 
//
//					}
//				}
//						// @formatter: on    			
//				
    @RequestMapping(value = _UPDATE_URI + "{oid}", method = RequestMethod.POST)
    public String update(@PathVariable("oid") SibsReportFile sibsReportFile, @RequestParam(value = "whenprocessedbysibs",
            required = false) @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSZ") org.joda.time.DateTime whenProcessedBySibs,
            @RequestParam(value = "transactionstotalamount", required = false) java.math.BigDecimal transactionsTotalAmount,
            @RequestParam(value = "totalcost", required = false) java.math.BigDecimal totalCost, Model model,
            RedirectAttributes redirectAttributes) {

        setSibsReportFile(sibsReportFile, model);

        try {
            /*
            *  UpdateLogic here
            */

            updateSibsReportFile(whenProcessedBySibs, transactionsTotalAmount, totalCost, model);

            /*Succes Update */

            return redirect("/treasury/administration/payments/sibs/managesibsreportfile/sibsreportfile/read/"
                    + getSibsReportFile(model).getExternalId(), model, redirectAttributes);
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
            return update(sibsReportFile, model);

        }
    }

    @Atomic
    public void updateSibsReportFile(org.joda.time.DateTime whenProcessedBySibs, java.math.BigDecimal transactionsTotalAmount,
            java.math.BigDecimal totalCost, Model model) {

        // @formatter: off				
        /*
         * Modify the update code here if you do not want to update
         * the object with the default setter for each field
         */

        // CHANGE_ME It's RECOMMENDED to use "Edit service" in DomainObject
        //getSibsReportFile(model).edit(fields_to_edit);

        //Instead, use individual SETTERS and validate "CheckRules" in the end
        // @formatter: on

        getSibsReportFile(model).setWhenProcessedBySibs(whenProcessedBySibs);
        getSibsReportFile(model).setTransactionsTotalAmount(transactionsTotalAmount);
        getSibsReportFile(model).setTotalCost(totalCost);
    }

}
