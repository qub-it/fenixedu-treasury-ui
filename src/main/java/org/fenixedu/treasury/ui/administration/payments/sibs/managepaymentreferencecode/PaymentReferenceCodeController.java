/**
 * This file was created by Quorum Born IT <http://www.qub-it.com/> and its 
 * copyright terms are bind to the legal agreement regulating the FenixEdu@ULisboa 
 * software development project between Quorum Born IT and ServiÃ§os Partilhados da
 * Universidade de Lisboa:
 *  - Copyright Â© 2015 Quorum Born IT (until any Go-Live phase)
 *  - Copyright Â© 2015 Universidade de Lisboa (after any Go-Live phase)
 *
 * Contributors: ricardo.pedro@qub-it.com, anil.mamede@qub-it.com
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
package org.fenixedu.treasury.ui.administration.payments.sibs.managepaymentreferencecode;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.fenixedu.bennu.core.domain.exceptions.DomainException;
import org.fenixedu.bennu.core.i18n.BundleUtil;
import org.fenixedu.bennu.spring.portal.SpringFunctionality;
import org.fenixedu.treasury.domain.paymentcodes.PaymentReferenceCode;
import org.fenixedu.treasury.domain.paymentcodes.PaymentReferenceCodeStateType;
import org.fenixedu.treasury.domain.paymentcodes.pool.PaymentCodePool;
import org.fenixedu.treasury.ui.TreasuryBaseController;
import org.fenixedu.treasury.ui.TreasuryController;
import org.fenixedu.treasury.util.Constants;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Component;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import pt.ist.fenixframework.Atomic;

@Component("org.fenixedu.treasury.ui.administration.payments.sibs.managePaymentReferenceCode")
@SpringFunctionality(app = TreasuryController.class,
        title = "label.title.administration.payments.sibs.managePaymentReferenceCode", accessGroup = "treasuryFrontOffice")
//@BennuSpringController(value=TreasuryController.class) 
@RequestMapping(PaymentReferenceCodeController.CONTROLLER_URL)
public class PaymentReferenceCodeController extends TreasuryBaseController {

    public static final String CONTROLLER_URL =
            "/treasury/administration/payments/sibs/managepaymentreferencecode/paymentreferencecode";

    @RequestMapping
    public String home(Model model) {
        //this is the default behaviour, for handling in a Spring Functionality
        return "forward:" + CONTROLLER_URL + "/";
    }

    // @formatter: off

    /*
    * This should be used when using AngularJS in the JSP
    */

    //private PaymentReferenceCode getPaymentReferenceCodeBean(Model model)
    //{
    //	return (PaymentReferenceCode)model.asMap().get("paymentReferenceCodeBean");
    //}
    //				
    //private void setPaymentReferenceCodeBean (PaymentReferenceCodeBean bean, Model model)
    //{
    //	model.addAttribute("paymentReferenceCodeBeanJson", getBeanJson(bean));
    //	model.addAttribute("paymentReferenceCodeBean", bean);
    //}

    // @formatter: on

    private PaymentReferenceCode getPaymentReferenceCode(Model model) {
        return (PaymentReferenceCode) model.asMap().get("paymentReferenceCode");
    }

    private void setPaymentReferenceCode(PaymentReferenceCode paymentReferenceCode, Model model) {
        model.addAttribute("paymentReferenceCode", paymentReferenceCode);
    }

    @Atomic
    public void deletePaymentReferenceCode(PaymentReferenceCode paymentReferenceCode) {
        paymentReferenceCode.delete();
    }

//				
    private static final String _SEARCH_URI = "/";
    public static final String SEARCH_URL = CONTROLLER_URL + _SEARCH_URI;

    @RequestMapping(value = _SEARCH_URI)
    public String search(
            @RequestParam(value = "referencecode", required = false) java.lang.String referenceCode,
            @RequestParam(value = "begindate", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") org.joda.time.DateTime beginDate,
            @RequestParam(value = "enddate", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") org.joda.time.DateTime endDate,
            @RequestParam(value = "state", required = false) PaymentReferenceCodeStateType state, Model model) {
        List<PaymentReferenceCode> searchpaymentreferencecodeResultsDataSet =
                filterSearchPaymentReferenceCode(referenceCode, beginDate, endDate, state);

        //add the results dataSet to the model
        model.addAttribute("searchpaymentreferencecodeResultsDataSet", searchpaymentreferencecodeResultsDataSet);
        model.addAttribute("stateValues", PaymentReferenceCodeStateType.values());
        return "treasury/administration/payments/sibs/managepaymentreferencecode/paymentreferencecode/search";
    }

    private Stream<PaymentReferenceCode> getSearchUniverseSearchPaymentReferenceCodeDataSet() {
        //
        //The initialization of the result list must be done here
        //
        //
        return PaymentReferenceCode.findAll();
        //return new ArrayList<PaymentReferenceCode>().stream();
    }

    private List<PaymentReferenceCode> filterSearchPaymentReferenceCode(java.lang.String referenceCode,
            org.joda.time.DateTime beginDate, org.joda.time.DateTime endDate, PaymentReferenceCodeStateType state) {

        return getSearchUniverseSearchPaymentReferenceCodeDataSet()
                .filter(paymentReferenceCode -> referenceCode == null || referenceCode.length() == 0
                        || paymentReferenceCode.getReferenceCode() != null
                        && paymentReferenceCode.getReferenceCode().length() > 0
                        && paymentReferenceCode.getReferenceCode().toLowerCase().contains(referenceCode.toLowerCase()))
                .filter(paymentReferenceCode -> beginDate == null || beginDate.equals(paymentReferenceCode.getBeginDate()))
                .filter(paymentReferenceCode -> endDate == null || endDate.equals(paymentReferenceCode.getEndDate()))
                .filter(paymentReferenceCode -> state == null || state.equals(paymentReferenceCode.getState()))
                .collect(Collectors.toList());
    }

    private static final String _SEARCH_TO_VIEW_ACTION_URI = "/search/view/";
    public static final String SEARCH_TO_VIEW_ACTION_URL = CONTROLLER_URL + _SEARCH_TO_VIEW_ACTION_URI;

    @RequestMapping(value = _SEARCH_TO_VIEW_ACTION_URI + "{oid}")
    public String processSearchToViewAction(@PathVariable("oid") PaymentReferenceCode paymentReferenceCode, Model model,
            RedirectAttributes redirectAttributes) {

        // CHANGE_ME Insert code here for processing viewAction
        // If you selected multiple exists you must choose which one to use below	 
        return redirect("/treasury/administration/payments/sibs/managepaymentreferencecode/paymentreferencecode/read" + "/"
                + paymentReferenceCode.getExternalId(), model, redirectAttributes);
    }

//				
    private static final String _READ_URI = "/read/";
    public static final String READ_URL = CONTROLLER_URL + _READ_URI;

    @RequestMapping(value = _READ_URI + "{oid}")
    public String read(@PathVariable("oid") PaymentReferenceCode paymentReferenceCode, Model model) {
        setPaymentReferenceCode(paymentReferenceCode, model);
        return "treasury/administration/payments/sibs/managepaymentreferencecode/paymentreferencecode/read";
    }

//
    private static final String _DELETE_URI = "/delete/";
    public static final String DELETE_URL = CONTROLLER_URL + _DELETE_URI;

    @RequestMapping(value = _DELETE_URI + "{oid}", method = RequestMethod.POST)
    public String delete(@PathVariable("oid") PaymentReferenceCode paymentReferenceCode, Model model,
            RedirectAttributes redirectAttributes) {

        setPaymentReferenceCode(paymentReferenceCode, model);
        try {
            //call the Atomic delete function
            deletePaymentReferenceCode(paymentReferenceCode);

            addInfoMessage(BundleUtil.getString(Constants.BUNDLE, "label.success.delete"), model);
            return redirect("/treasury/administration/payments/sibs/managepaymentreferencecode/paymentreferencecode/", model,
                    redirectAttributes);
        } catch (DomainException ex) {
            //Add error messages to the list
            addErrorMessage(BundleUtil.getString(Constants.BUNDLE, "label.error.delete") + ex.getLocalizedMessage(), model);
        }

        //The default mapping is the same Read View
        return "treasury/administration/payments/sibs/managepaymentreferencecode/paymentreferencecode/read/"
                + getPaymentReferenceCode(model).getExternalId();
    }

//				
    private static final String _CREATE_URI = "/create";
    public static final String CREATE_URL = CONTROLLER_URL + _CREATE_URI;

    @RequestMapping(value = _CREATE_URI, method = RequestMethod.GET)
    public String create(Model model) {
        model.addAttribute("stateValues", PaymentReferenceCodeStateType.values());

        //IF ANGULAR, initialize the Bean
        //PaymentReferenceCodeBean bean = new PaymentReferenceCodeBean();
        //this.setPaymentReferenceCodeBean(bean, model);

        return "treasury/administration/payments/sibs/managepaymentreferencecode/paymentreferencecode/create";
    }

//
//               THIS SHOULD BE USED ONLY WHEN USING ANGULAR 
//
//						// @formatter: off
//			
//				private static final String _CREATEPOSTBACK_URI ="/createpostback";
//				public static final String  CREATEPOSTBACK_URL = CONTROLLER_URL + _createPOSTBACK_URI;
//    			@RequestMapping(value = _CREATEPOSTBACK_URI, method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
//  			  	public @ResponseBody String createpostback(@RequestParam(value = "bean", required = false) PaymentReferenceCodeBean bean,
//            		Model model) {
//
//        			// Do validation logic ?!?!
//        			this.setPaymentReferenceCodeBean(bean, model);
//        			return getBeanJson(bean);
//    			}
//    			
//    			@RequestMapping(value = CREATE, method = RequestMethod.POST)
//  			  	public String create(@RequestParam(value = "bean", required = false) PaymentReferenceCodeBean bean,
//            		Model model, RedirectAttributes redirectAttributes ) {
//
//					/*
//					*  Creation Logic
//					*/
//					
//					try
//					{
//
//				     	PaymentReferenceCode paymentReferenceCode = createPaymentReferenceCode(... get properties from bean ...,model);
//				    	
//					//Success Validation
//				     //Add the bean to be used in the View
//					model.addAttribute("paymentReferenceCode",paymentReferenceCode);
//				    return redirect("/treasury/administration/payments/sibs/managepaymentreferencecode/paymentreferencecode/read/" + getPaymentReferenceCode(model).getExternalId(), model, redirectAttributes);
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
    public String create(
            @RequestParam(value = "referencecode", required = false) java.lang.String referenceCode,
            @RequestParam(value = "begindate", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") org.joda.time.LocalDate beginDate,
            @RequestParam(value = "enddate", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") org.joda.time.LocalDate endDate,
            @RequestParam(value = "state", required = false) PaymentReferenceCodeStateType state, @RequestParam(
                    value = "paymentcodepool", required = false) PaymentCodePool pool, Model model,
            RedirectAttributes redirectAttributes) {
        /*
        *  Creation Logic
        */

        try {

            PaymentReferenceCode paymentReferenceCode =
                    createPaymentReferenceCode(referenceCode, beginDate, endDate, state, pool);

            //Success Validation
            //Add the bean to be used in the View
            model.addAttribute("paymentReferenceCode", paymentReferenceCode);
            return redirect("/treasury/administration/payments/sibs/managepaymentreferencecode/paymentreferencecode/read/"
                    + getPaymentReferenceCode(model).getExternalId(), model, redirectAttributes);
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
    public PaymentReferenceCode createPaymentReferenceCode(java.lang.String referenceCode, org.joda.time.LocalDate beginDate,
            org.joda.time.LocalDate endDate, PaymentReferenceCodeStateType state, PaymentCodePool pool) {

        // @formatter: off

        /*
         * Modify the creation code here if you do not want to create
         * the object with the default constructor and use the setter
         * for each field
         * 
         */

        // CHANGE_ME It's RECOMMENDED to use "Create service" in DomainObject
        //PaymentReferenceCode paymentReferenceCode = paymentReferenceCode.create(fields_to_create);

        //Instead, use individual SETTERS and validate "CheckRules" in the end
        // @formatter: on

        PaymentReferenceCode paymentReferenceCode =
                PaymentReferenceCode.create(referenceCode, beginDate, endDate, state, pool, BigDecimal.ZERO, BigDecimal.ZERO);

        return paymentReferenceCode;
    }

//				
    private static final String _UPDATE_URI = "/update/";
    public static final String UPDATE_URL = CONTROLLER_URL + _UPDATE_URI;

    @RequestMapping(value = _UPDATE_URI + "{oid}", method = RequestMethod.GET)
    public String update(@PathVariable("oid") PaymentReferenceCode paymentReferenceCode, Model model) {
        model.addAttribute("stateValues", PaymentReferenceCodeStateType.values());
        setPaymentReferenceCode(paymentReferenceCode, model);
        return "treasury/administration/payments/sibs/managepaymentreferencecode/paymentreferencecode/update";
    }

//

//               THIS SHOULD BE USED ONLY WHEN USING ANGULAR 
//
//						// @formatter: off
//			
//				private static final String _UPDATEPOSTBACK_URI ="/updatepostback/";
//				public static final String  UPDATEPOSTBACK_URL = CONTROLLER_URL + _updatePOSTBACK_URI;
//    			@RequestMapping(value = _UPDATEPOSTBACK_URI + "{oid}", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
//  			  	public @ResponseBody String updatepostback(@PathVariable("oid") PaymentReferenceCode paymentReferenceCode, @RequestParam(value = "bean", required = false) PaymentReferenceCodeBean bean,
//            		Model model) {
//
//        			// Do validation logic ?!?!
//        			this.setPaymentReferenceCodeBean(bean, model);
//        			return getBeanJson(bean);
//    			} 
//    			
//    			@RequestMapping(value = _UPDATE_URI + "{oid}", method = RequestMethod.POST)
//  			  	public String update(@PathVariable("oid") PaymentReferenceCode paymentReferenceCode, @RequestParam(value = "bean", required = false) PaymentReferenceCodeBean bean,
//            		Model model, RedirectAttributes redirectAttributes ) {
//					setPaymentReferenceCode(paymentReferenceCode,model);
//
//				     try
//				     {
//					/*
//					*  UpdateLogic here
//					*/
//				    		
//						updatePaymentReferenceCode( .. get fields from bean..., model);
//
//					/*Succes Update */
//
//				    return redirect("/treasury/administration/payments/sibs/managepaymentreferencecode/paymentreferencecode/read/" + getPaymentReferenceCode(model).getExternalId(), model, redirectAttributes);
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
//				     	return update(paymentReferenceCode,model);
//					 
//
//					}
//				}
//						// @formatter: on    			
//				
    @RequestMapping(value = _UPDATE_URI + "{oid}", method = RequestMethod.POST)
    public String update(
            @PathVariable("oid") PaymentReferenceCode paymentReferenceCode,
            @RequestParam(value = "referencecode", required = false) java.lang.String referenceCode,
            @RequestParam(value = "begindate", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") org.joda.time.LocalDate beginDate,
            @RequestParam(value = "enddate", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") org.joda.time.LocalDate endDate,
            @RequestParam(value = "state", required = false) PaymentReferenceCodeStateType state, Model model,
            RedirectAttributes redirectAttributes) {

        setPaymentReferenceCode(paymentReferenceCode, model);

        try {
            /*
            *  UpdateLogic here
            */

            updatePaymentReferenceCode(referenceCode, beginDate, endDate, state, model);

            /*Succes Update */

            return redirect("/treasury/administration/payments/sibs/managepaymentreferencecode/paymentreferencecode/read/"
                    + getPaymentReferenceCode(model).getExternalId(), model, redirectAttributes);
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
            return update(paymentReferenceCode, model);

        }
    }

    @Atomic
    public void updatePaymentReferenceCode(java.lang.String referenceCode, org.joda.time.LocalDate beginDate,
            org.joda.time.LocalDate endDate, PaymentReferenceCodeStateType state, Model model) {

        // @formatter: off				
        /*
         * Modify the update code here if you do not want to update
         * the object with the default setter for each field
         */

        // CHANGE_ME It's RECOMMENDED to use "Edit service" in DomainObject
        //getPaymentReferenceCode(model).edit(fields_to_edit);

        //Instead, use individual SETTERS and validate "CheckRules" in the end
        // @formatter: on

        getPaymentReferenceCode(model).setReferenceCode(referenceCode);
        getPaymentReferenceCode(model).setBeginDate(beginDate);
        getPaymentReferenceCode(model).setEndDate(endDate);
        getPaymentReferenceCode(model).setState(state);
    }

    @RequestMapping(value = "/read/{oid}/anull", method = RequestMethod.POST)
    public String processReadToAnull(@PathVariable("oid") PaymentReferenceCode paymentReferenceCode, Model model,
            RedirectAttributes redirectAttributes) {
        paymentReferenceCode.anullPaymentReferenceCode();
        return redirect(READ_URL + paymentReferenceCode.getExternalId(), model, redirectAttributes);
    }
}
