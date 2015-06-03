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
package org.fenixedu.treasury.ui.administration.payments.sibs.managepaymentcodepool;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.fenixedu.bennu.core.domain.exceptions.DomainException;
import org.fenixedu.bennu.core.i18n.BundleUtil;
import org.fenixedu.bennu.spring.portal.SpringFunctionality;
import org.fenixedu.treasury.domain.FinantialInstitution;
import org.fenixedu.treasury.domain.paymentcodes.pool.PaymentCodePool;
import org.fenixedu.treasury.ui.TreasuryBaseController;
import org.fenixedu.treasury.ui.TreasuryController;
import org.fenixedu.treasury.util.Constants;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import pt.ist.fenixframework.Atomic;

//@Component("org.fenixedu.treasury.ui.administration.payments.sibs.managePaymentCodePool") <-- Use for duplicate controller name disambiguation
@SpringFunctionality(app = TreasuryController.class, title = "label.title.administration.payments.sibs.managePaymentCodePool",
        accessGroup = "logged")
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

    //private PaymentCodePool getPaymentCodePoolBean(Model model)
    //{
    //	return (PaymentCodePool)model.asMap().get("paymentCodePoolBean");
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
        paymentCodePool.delete();
    }

//				
    private static final String _SEARCH_URI = "/";
    public static final String SEARCH_URL = CONTROLLER_URL + _SEARCH_URI;

    @RequestMapping(value = _SEARCH_URI)
    public String search(@RequestParam(value = "name", required = false) java.lang.String name, @RequestParam(
            value = "minpaymentcodes", required = false) java.lang.Integer minPaymentCodes, @RequestParam(
            value = "maxpaymentcodes", required = false) java.lang.Integer maxPaymentCodes, @RequestParam(value = "minamount",
            required = false) java.math.BigDecimal minAmount,
            @RequestParam(value = "maxamount", required = false) java.math.BigDecimal maxAmount, @RequestParam(value = "active",
                    required = false) java.lang.Boolean active, Model model) {
        List<PaymentCodePool> searchpaymentcodepoolResultsDataSet =
                filterSearchPaymentCodePool(name, minPaymentCodes, maxPaymentCodes, minAmount, maxAmount, active);

        //add the results dataSet to the model
        model.addAttribute("searchpaymentcodepoolResultsDataSet", searchpaymentcodepoolResultsDataSet);
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

    private List<PaymentCodePool> filterSearchPaymentCodePool(java.lang.String name, java.lang.Integer minPaymentCodes,
            java.lang.Integer maxPaymentCodes, java.math.BigDecimal minAmount, java.math.BigDecimal maxAmount,
            java.lang.Boolean active) {

        return getSearchUniverseSearchPaymentCodePoolDataSet()
                .filter(paymentCodePool -> name == null || name.length() == 0 || paymentCodePool.getName() != null
                        && paymentCodePool.getName().length() > 0
                        && paymentCodePool.getName().toLowerCase().contains(name.toLowerCase()))
                .filter(paymentCodePool -> minPaymentCodes == null
                        || minPaymentCodes.equals(paymentCodePool.getMinReferenceCode()))
                .filter(paymentCodePool -> maxPaymentCodes == null
                        || maxPaymentCodes.equals(paymentCodePool.getMaxReferenceCode()))
                .filter(paymentCodePool -> minAmount == null || minAmount.equals(paymentCodePool.getMinAmount()))
                .filter(paymentCodePool -> maxAmount == null || maxAmount.equals(paymentCodePool.getMaxAmount()))
                .filter(paymentCodePool -> active == null || active.equals(paymentCodePool.getActive()))
                .collect(Collectors.toList());
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
        } catch (DomainException ex) {
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
//				public static final String  CREATEPOSTBACK_URL = CONTROLLER_URL + _createPOSTBACK_URI;
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
    public String create(@RequestParam(value = "name", required = false) java.lang.String name, @RequestParam(
            value = "minpaymentcodes", required = false) java.lang.Long minPaymentCodes, @RequestParam(value = "maxpaymentcodes",
            required = false) java.lang.Long maxPaymentCodes,
            @RequestParam(value = "minamount", required = false) java.math.BigDecimal minAmount, @RequestParam(
                    value = "maxamount", required = false) java.math.BigDecimal maxAmount, @RequestParam(value = "active",
                    required = false) java.lang.Boolean active,
            @RequestParam(value = "finantialInstitution", required = false) FinantialInstitution finantialInstitution,
            Model model, RedirectAttributes redirectAttributes) {
        /*
        *  Creation Logic
        */

        try {

            PaymentCodePool paymentCodePool =
                    createPaymentCodePool(name, minPaymentCodes, maxPaymentCodes, minAmount, maxAmount, active,
                            finantialInstitution);

            //Success Validation
            //Add the bean to be used in the View
            model.addAttribute("paymentCodePool", paymentCodePool);
            return redirect("/treasury/administration/payments/sibs/managepaymentcodepool/paymentcodepool/read/"
                    + getPaymentCodePool(model).getExternalId(), model, redirectAttributes);
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
    public PaymentCodePool createPaymentCodePool(final String name, final Long minPaymentCodes, final Long maxPaymentCodes,
            final BigDecimal minAmount, final BigDecimal maxAmount, final Boolean active,
            final FinantialInstitution finantialInstitution) {

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
                PaymentCodePool
                        .create(name, minPaymentCodes, maxPaymentCodes, minAmount, maxAmount, active, finantialInstitution);

        return paymentCodePool;
    }

//				
    private static final String _UPDATE_URI = "/update/";
    public static final String UPDATE_URL = CONTROLLER_URL + _UPDATE_URI;

    @RequestMapping(value = _UPDATE_URI + "{oid}", method = RequestMethod.GET)
    public String update(@PathVariable("oid") PaymentCodePool paymentCodePool, Model model) {
        setPaymentCodePool(paymentCodePool, model);
        return "treasury/administration/payments/sibs/managepaymentcodepool/paymentcodepool/update";
    }

//

//               THIS SHOULD BE USED ONLY WHEN USING ANGULAR 
//
//						// @formatter: off
//			
//				private static final String _UPDATEPOSTBACK_URI ="/updatepostback/";
//				public static final String  UPDATEPOSTBACK_URL = CONTROLLER_URL + _updatePOSTBACK_URI;
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
//				     	return update(paymentCodePool,model);
//					 
//
//					}
//				}
//						// @formatter: on    			
//				
    @RequestMapping(value = _UPDATE_URI + "{oid}", method = RequestMethod.POST)
    public String update(@PathVariable("oid") PaymentCodePool paymentCodePool,
            @RequestParam(value = "name", required = false) java.lang.String name, @RequestParam(value = "minpaymentcodes",
                    required = false) java.lang.Long minPaymentCodes,
            @RequestParam(value = "maxpaymentcodes", required = false) java.lang.Long maxPaymentCodes, @RequestParam(
                    value = "minamount", required = false) java.math.BigDecimal minAmount, @RequestParam(value = "maxamount",
                    required = false) java.math.BigDecimal maxAmount,
            @RequestParam(value = "active", required = false) java.lang.Boolean active, Model model,
            RedirectAttributes redirectAttributes) {

        setPaymentCodePool(paymentCodePool, model);

        try {
            /*
            *  UpdateLogic here
            */

            updatePaymentCodePool(name, minPaymentCodes, maxPaymentCodes, minAmount, maxAmount, active, model);

            /*Succes Update */

            return redirect("/treasury/administration/payments/sibs/managepaymentcodepool/paymentcodepool/read/"
                    + getPaymentCodePool(model).getExternalId(), model, redirectAttributes);
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
            return update(paymentCodePool, model);

        }
    }

    @Atomic
    public void updatePaymentCodePool(java.lang.String name, java.lang.Long minPaymentCodes, java.lang.Long maxPaymentCodes,
            java.math.BigDecimal minAmount, java.math.BigDecimal maxAmount, java.lang.Boolean active, Model model) {

        // @formatter: off				
        /*
         * Modify the update code here if you do not want to update
         * the object with the default setter for each field
         */

        // CHANGE_ME It's RECOMMENDED to use "Edit service" in DomainObject
        //getPaymentCodePool(model).edit(fields_to_edit);

        //Instead, use individual SETTERS and validate "CheckRules" in the end
        // @formatter: on

        getPaymentCodePool(model).setName(name);
        getPaymentCodePool(model).setMinReferenceCode(minPaymentCodes);
        getPaymentCodePool(model).setMaxReferenceCode(maxPaymentCodes);
        getPaymentCodePool(model).setMinAmount(minAmount);
        getPaymentCodePool(model).setMaxAmount(maxAmount);
        getPaymentCodePool(model).setActive(active);
    }

}
