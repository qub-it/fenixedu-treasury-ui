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
package org.fenixedu.treasury.ui.administration.payments.sibs.managepaymentcodepool;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.fenixedu.bennu.core.i18n.BundleUtil;
import org.fenixedu.bennu.spring.portal.SpringFunctionality;
import org.fenixedu.treasury.domain.FinantialInstitution;
import org.fenixedu.treasury.domain.PaymentMethod;
import org.fenixedu.treasury.domain.document.DocumentNumberSeries;
import org.fenixedu.treasury.domain.document.FinantialDocumentType;
import org.fenixedu.treasury.domain.paymentcodes.pool.PaymentCodePool;
import org.fenixedu.treasury.ui.TreasuryBaseController;
import org.fenixedu.treasury.ui.TreasuryController;
import org.fenixedu.treasury.util.Constants;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import pt.ist.fenixframework.Atomic;

//@Component("org.fenixedu.treasury.ui.administration.payments.sibs.managePaymentCodePool") <-- Use for duplicate controller name disambiguation
@SpringFunctionality(app = TreasuryController.class, title = "label.title.administration.payments.sibs.managePaymentCodePool",
        accessGroup = "#managers")
// CHANGE_ME accessGroup = "group1 | group2 | groupXPTO"
//or
//@BennuSpringController(value=TreasuryController.class) 
@RequestMapping(PaymentCodePoolController.CONTROLLER_URL)
public class PaymentCodePoolController extends TreasuryBaseController {

    public static final String CONTROLLER_URL = "/treasury/administration/payments/sibs/managepaymentcodepool/paymentcodepool";

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

    //private PaymentCodePoolBean getPaymentCodePoolBean(Model model)
    //{
    //	return (PaymentCodePoolBean)model.asMap().get("paymentCodePoolBean");
    //}
    //				
    //private void setPaymentCodePoolBean (PaymentCodePoolBean bean, Model model)
    //{
    //	model.addAttribute("paymentCodePoolBeanJson", getBeanJson(bean));
    //	model.addAttribute("paymentCodePoolBean", bean);
    //}

    // @formatter: on

    private PaymentCodePool getPaymentCodePool(Model model) {
        return (PaymentCodePool) model.asMap().get("paymentCodePool");
    }

    private void setPaymentCodePool(PaymentCodePool paymentCodePool, Model model) {
        model.addAttribute("paymentCodePool", paymentCodePool);
    }

    @Atomic
    public void deletePaymentCodePool(PaymentCodePool paymentCodePool) {
        // CHANGE_ME: Do the processing for deleting the paymentCodePool
        // Do not catch any exception here

        // paymentCodePool.delete();
    }

//				
    private static final String _SEARCH_URI = "/";
    public static final String SEARCH_URL = CONTROLLER_URL + _SEARCH_URI;

    @RequestMapping(value = _SEARCH_URI)
    public String search(
            @RequestParam(value = "finantialinstitution", required = false) org.fenixedu.treasury.domain.FinantialInstitution finantialInstitution,
            Model model) {
        List<PaymentCodePool> searchpaymentcodepoolResultsDataSet = filterSearchPaymentCodePool(finantialInstitution);

        //add the results dataSet to the model
        model.addAttribute("searchpaymentcodepoolResultsDataSet", searchpaymentcodepoolResultsDataSet);
        model.addAttribute("PaymentCodePool_finantialInstitution_options",
                FinantialInstitution.findAll().collect(Collectors.toList()));

        return "treasury/administration/payments/sibs/managepaymentcodepool/paymentcodepool/search";
    }

    private Stream<PaymentCodePool> getSearchUniverseSearchPaymentCodePoolDataSet() {
        //
        //The initialization of the result list must be done here
        //
        //
        return PaymentCodePool.findAll();
//        return new ArrayList<PaymentCodePool>().stream();
    }

    private List<PaymentCodePool> filterSearchPaymentCodePool(
            org.fenixedu.treasury.domain.FinantialInstitution finantialInstitution) {

        return getSearchUniverseSearchPaymentCodePoolDataSet().filter(
                paymentCodePool -> finantialInstitution == null
                        || finantialInstitution == paymentCodePool.getFinantialInstitution()).collect(Collectors.toList());
    }

    private static final String _SEARCH_TO_VIEW_ACTION_URI = "/search/view/";
    public static final String SEARCH_TO_VIEW_ACTION_URL = CONTROLLER_URL + _SEARCH_TO_VIEW_ACTION_URI;

    @RequestMapping(value = _SEARCH_TO_VIEW_ACTION_URI + "{oid}")
    public String processSearchToViewAction(@PathVariable("oid") PaymentCodePool paymentCodePool, Model model,
            RedirectAttributes redirectAttributes) {

        // CHANGE_ME Insert code here for processing viewAction
        // If you selected multiple exists you must choose which one to use below	 
        return redirect("/treasury/administration/payments/sibs/managepaymentcodepool/paymentcodepool/read" + "/"
                + paymentCodePool.getExternalId(), model, redirectAttributes);
    }

//				
    private static final String _READ_URI = "/read/";
    public static final String READ_URL = CONTROLLER_URL + _READ_URI;

    @RequestMapping(value = _READ_URI + "{oid}")
    public String read(@PathVariable("oid") PaymentCodePool paymentCodePool, Model model) {
        setPaymentCodePool(paymentCodePool, model);
        return "treasury/administration/payments/sibs/managepaymentcodepool/paymentcodepool/read";
    }

//
    private static final String _DELETE_URI = "/delete/";
    public static final String DELETE_URL = CONTROLLER_URL + _DELETE_URI;

    @RequestMapping(value = _DELETE_URI + "{oid}", method = RequestMethod.POST)
    public String delete(@PathVariable("oid") PaymentCodePool paymentCodePool, Model model, RedirectAttributes redirectAttributes) {

        setPaymentCodePool(paymentCodePool, model);
        try {
            //call the Atomic delete function
            deletePaymentCodePool(paymentCodePool);

            addInfoMessage(BundleUtil.getString(Constants.BUNDLE, "label.success.delete"), model);
            return redirect("/treasury/administration/payments/sibs/managepaymentcodepool/paymentcodepool/", model,
                    redirectAttributes);
        } catch (Exception ex) {
            //Add error messages to the list
            addErrorMessage(BundleUtil.getString(Constants.BUNDLE, "label.error.delete") + ex.getLocalizedMessage(), model);
        }

        //The default mapping is the same Read View
        return "treasury/administration/payments/sibs/managepaymentcodepool/paymentcodepool/read/"
                + getPaymentCodePool(model).getExternalId();
    }

//				
    private static final String _CREATE_URI = "/create";
    public static final String CREATE_URL = CONTROLLER_URL + _CREATE_URI;

    @RequestMapping(value = _CREATE_URI, method = RequestMethod.GET)
    public String create(Model model) {
        model.addAttribute("finantialInstitutionList", FinantialInstitution.findAll().collect(Collectors.toList()));

        model.addAttribute(
                "PaymentCodePool_documentSeriesForPayments_options",
                DocumentNumberSeries.findAll()
                        .filter(x -> x.getFinantialDocumentType().equals(FinantialDocumentType.findForSettlementNote()))
                        .collect(Collectors.toList()));

        model.addAttribute("PaymentCodePool_paymentMethod_options", PaymentMethod.findAll().collect(Collectors.toList()));

        //IF ANGULAR, initialize the Bean
        //PaymentCodePoolBean bean = new PaymentCodePoolBean();
        //this.setPaymentCodePoolBean(bean, model);

        return "treasury/administration/payments/sibs/managepaymentcodepool/paymentcodepool/create";
    }

//
//               THIS SHOULD BE USED ONLY WHEN USING ANGULAR 
//
//						// @formatter: off
//			
//				private static final String _CREATEPOSTBACK_URI ="/createpostback";
//				public static final String  CREATEPOSTBACK_URL = CONTROLLER_URL + _CREATEPOSTBACK_URI;
//    			@RequestMapping(value = _CREATEPOSTBACK_URI, method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
//  			  	public @ResponseBody String createpostback(@RequestParam(value = "bean", required = false) PaymentCodePoolBean bean,
//            		Model model) {
//
//        			// Do validation logic ?!?!
//        			this.setPaymentCodePoolBean(bean, model);
//        			return getBeanJson(bean);
//    			}
//    			
//    			@RequestMapping(value = CREATE, method = RequestMethod.POST)
//  			  	public String create(@RequestParam(value = "bean", required = false) PaymentCodePoolBean bean,
//            		Model model, RedirectAttributes redirectAttributes ) {
//
//					/*
//					*  Creation Logic
//					*/
//					
//					try
//					{
//
//				     	PaymentCodePool paymentCodePool = createPaymentCodePool(... get properties from bean ...,model);
//				    	
//					//Success Validation
//				     //Add the bean to be used in the View
//					model.addAttribute("paymentCodePool",paymentCodePool);
//				    return redirect("/treasury/administration/payments/sibs/managepaymentcodepool/paymentcodepool/read/" + getPaymentCodePool(model).getExternalId(), model, redirectAttributes);
//					}
//					catch (Exception de)
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
//						this.setPaymentCodePoolBean(bean, model);				
//						return "treasury/administration/payments/sibs/managepaymentcodepool/paymentcodepool/create";
//                      
//					}
//    			}
//						// @formatter: on

//				
    @RequestMapping(value = _CREATE_URI, method = RequestMethod.POST)
    public String create(
            @RequestParam(value = "finantialinstitution") org.fenixedu.treasury.domain.FinantialInstitution finantialInstitution,
            @RequestParam(value = "name") java.lang.String name,
            @RequestParam(value = "entityreferencecode", required = false) java.lang.String entityReferenceCode,
            @RequestParam(value = "minreferencecode", required = false) java.lang.Long minReferenceCode,
            @RequestParam(value = "maxreferencecode", required = false) java.lang.Long maxReferenceCode,
            @RequestParam(value = "minamount", required = false) java.math.BigDecimal minAmount,
            @RequestParam(value = "maxamount", required = false) java.math.BigDecimal maxAmount,
            @RequestParam(value = "validfrom", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") org.joda.time.LocalDate validFrom,
            @RequestParam(value = "validto", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") org.joda.time.LocalDate validTo,
            @RequestParam(value = "active", required = false) java.lang.Boolean active, @RequestParam(value = "usecheckdigit",
                    required = false) java.lang.Boolean useCheckDigit, @RequestParam(value = "useamounttovalidatecheckdigit",
                    required = false) java.lang.Boolean useAmountToValidateCheckDigit, @RequestParam(
                    value = "documentnumberseries") DocumentNumberSeries documentNumberSeries, @RequestParam(
                    value = "paymentmethod") PaymentMethod paymentMethod, Model model, RedirectAttributes redirectAttributes) {
        /*
        *  Creation Logic
        */

        try {

            PaymentCodePool paymentCodePool =
                    createPaymentCodePool(finantialInstitution, name, entityReferenceCode, minReferenceCode, maxReferenceCode,
                            minAmount, maxAmount, validFrom, validTo, active, useCheckDigit, useAmountToValidateCheckDigit,
                            documentNumberSeries, paymentMethod);

            //Success Validation
            //Add the bean to be used in the View
            model.addAttribute("paymentCodePool", paymentCodePool);
            return redirect("/treasury/administration/payments/sibs/managepaymentcodepool/paymentcodepool/read/"
                    + getPaymentCodePool(model).getExternalId(), model, redirectAttributes);
        } catch (Exception de) {

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
    public PaymentCodePool createPaymentCodePool(org.fenixedu.treasury.domain.FinantialInstitution finantialInstitution,
            java.lang.String name, java.lang.String entityReferenceCode, java.lang.Long minReferenceCode,
            java.lang.Long maxReferenceCode, java.math.BigDecimal minAmount, java.math.BigDecimal maxAmount,
            org.joda.time.LocalDate validFrom, org.joda.time.LocalDate validTo, java.lang.Boolean active,
            java.lang.Boolean useCheckDigit, java.lang.Boolean useAmountToValidateCheckDigit,
            DocumentNumberSeries documentNumberSeries, PaymentMethod paymentMethod) {

        // @formatter: off

        /*
         * Modify the creation code here if you do not want to create
         * the object with the default constructor and use the setter
         * for each field
         * 
         */

        // CHANGE_ME It's RECOMMENDED to use "Create service" in DomainObject
        //PaymentCodePool paymentCodePool = paymentCodePool.create(fields_to_create);

        //Instead, use individual SETTERS and validate "CheckRules" in the end
        // @formatter: on

        PaymentCodePool paymentCodePool =
                PaymentCodePool.create(name, entityReferenceCode, minReferenceCode, maxReferenceCode, minAmount, maxAmount,
                        validFrom, validTo, active, useCheckDigit, useAmountToValidateCheckDigit, finantialInstitution,
                        documentNumberSeries, paymentMethod);

        return paymentCodePool;
    }

//				
    private static final String _UPDATE_URI = "/update/";
    public static final String UPDATE_URL = CONTROLLER_URL + _UPDATE_URI;

    @RequestMapping(value = _UPDATE_URI + "{oid}", method = RequestMethod.GET)
    public String update(@PathVariable("oid") PaymentCodePool paymentCodePool, Model model) {
        model.addAttribute("finantialInstitutionList", FinantialInstitution.findAll().collect(Collectors.toList()));

        model.addAttribute(
                "PaymentCodePool_documentSeriesForPayments_options",
                DocumentNumberSeries.findAll()
                        .filter(x -> x.getFinantialDocumentType().equals(FinantialDocumentType.findForSettlementNote()))
                        .collect(Collectors.toList()));

        model.addAttribute("PaymentCodePool_paymentMethod_options", PaymentMethod.findAll().collect(Collectors.toList()));

        setPaymentCodePool(paymentCodePool, model);

        //IF ANGULAR, initialize the Bean
        //PaymentCodePoolBean bean = new PaymentCodePoolBean(paymentCodePool);
        //this.setPaymentCodePoolBean(bean, model);

        return "treasury/administration/payments/sibs/managepaymentcodepool/paymentcodepool/update";

    }

//

//               THIS SHOULD BE USED ONLY WHEN USING ANGULAR 
//
//						// @formatter: off
//			
//				private static final String _UPDATEPOSTBACK_URI ="/updatepostback/";
//				public static final String  UPDATEPOSTBACK_URL = CONTROLLER_URL + _UPDATEPOSTBACK_URI;
//    			@RequestMapping(value = _UPDATEPOSTBACK_URI + "{oid}", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
//  			  	public @ResponseBody String updatepostback(@PathVariable("oid") PaymentCodePool paymentCodePool, @RequestParam(value = "bean", required = false) PaymentCodePoolBean bean,
//            		Model model) {
//
//        			// Do validation logic ?!?!
//        			this.setPaymentCodePoolBean(bean, model);
//        			return getBeanJson(bean);
//    			} 
//    			
//    			@RequestMapping(value = _UPDATE_URI + "{oid}", method = RequestMethod.POST)
//  			  	public String update(@PathVariable("oid") PaymentCodePool paymentCodePool, @RequestParam(value = "bean", required = false) PaymentCodePoolBean bean,
//            		Model model, RedirectAttributes redirectAttributes ) {
//					setPaymentCodePool(paymentCodePool,model);
//
//				     try
//				     {
//					/*
//					*  UpdateLogic here
//					*/
//				    		
//						updatePaymentCodePool( .. get fields from bean..., model);
//
//					/*Succes Update */
//
//				    return redirect("/treasury/administration/payments/sibs/managepaymentcodepool/paymentcodepool/read/" + getPaymentCodePool(model).getExternalId(), model, redirectAttributes);
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
//						setPaymentCodePool(paymentCodePool, model);
//						this.setPaymentCodePoolBean(bean, model);
//
//						return "treasury/administration/payments/sibs/managepaymentcodepool/paymentcodepool/update";
//					}
//				}
//						// @formatter: on    			
//				
    @RequestMapping(value = _UPDATE_URI + "{oid}", method = RequestMethod.POST)
    public String update(
            @PathVariable("oid") PaymentCodePool paymentCodePool,
            @RequestParam(value = "finantialinstitution", required = false) org.fenixedu.treasury.domain.FinantialInstitution finantialInstitution,
            @RequestParam(value = "name", required = false) java.lang.String name,
            @RequestParam(value = "entityreferencecode", required = false) java.lang.String entityReferenceCode,
            @RequestParam(value = "minreferencecode", required = false) java.lang.Long minReferenceCode,
            @RequestParam(value = "maxreferencecode", required = false) java.lang.Long maxReferenceCode,
            @RequestParam(value = "minamount", required = false) java.math.BigDecimal minAmount,
            @RequestParam(value = "maxamount", required = false) java.math.BigDecimal maxAmount,
            @RequestParam(value = "validfrom", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") org.joda.time.LocalDate validFrom,
            @RequestParam(value = "validto", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") org.joda.time.LocalDate validTo,
            @RequestParam(value = "active", required = false) java.lang.Boolean active, @RequestParam(value = "usecheckdigit",
                    required = false) java.lang.Boolean useCheckDigit, @RequestParam(value = "useamounttovalidatecheckdigit",
                    required = false) java.lang.Boolean useAmountToValidateCheckDigit, @RequestParam(
                    value = "documentnumberseries") DocumentNumberSeries documentNumberSeries, @RequestParam(
                    value = "paymentmethod") PaymentMethod paymentMethod,

            Model model, RedirectAttributes redirectAttributes) {

        setPaymentCodePool(paymentCodePool, model);

        try {
            /*
            *  UpdateLogic here
            */

            updatePaymentCodePool(finantialInstitution, name, entityReferenceCode, minReferenceCode, maxReferenceCode, minAmount,
                    maxAmount, validFrom, validTo, active, useCheckDigit, useAmountToValidateCheckDigit, documentNumberSeries,
                    paymentMethod, model);

            /*Succes Update */

            return redirect("/treasury/administration/payments/sibs/managepaymentcodepool/paymentcodepool/read/"
                    + getPaymentCodePool(model).getExternalId(), model, redirectAttributes);
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
            return update(paymentCodePool, model);

        }
    }

    @Atomic
    public void updatePaymentCodePool(org.fenixedu.treasury.domain.FinantialInstitution finantialInstitution,
            java.lang.String name, java.lang.String entityReferenceCode, java.lang.Long minReferenceCode,
            java.lang.Long maxReferenceCode, java.math.BigDecimal minAmount, java.math.BigDecimal maxAmount,
            org.joda.time.LocalDate validFrom, org.joda.time.LocalDate validTo, java.lang.Boolean active,
            java.lang.Boolean useCheckDigit, java.lang.Boolean useAmountToValidateCheckDigit,
            DocumentNumberSeries seriesToUseInPayments, PaymentMethod paymentMethod, Model model) {

        getPaymentCodePool(model).setNewValidPeriod(validFrom, validTo);
        getPaymentCodePool(model).edit(name, minReferenceCode, maxReferenceCode, minAmount, maxAmount, active,
                seriesToUseInPayments, paymentMethod);
        getPaymentCodePool(model).changeFinantialInstitution(finantialInstitution);
        getPaymentCodePool(model).changePooltype(useCheckDigit, useAmountToValidateCheckDigit);

    }

}
