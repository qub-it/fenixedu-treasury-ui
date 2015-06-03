/**
 * This file was created by Quorum Born IT <http://www.qub-it.com/> and its 
 * copyright terms are bind to the legal agreement regulating the FenixEdu@ULisboa 
 * software development project between Quorum Born IT and Serviços Partilhados da
 * Universidade de Lisboa:
 *  - Copyright © 2015 Quorum Born IT (until any Go-Live phase)
 *  - Copyright © 2015 Universidade de Lisboa (after any Go-Live phase)
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
package org.fenixedu.treasury.ui.administration.base.managepaymentmethod;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.fenixedu.bennu.core.domain.exceptions.DomainException;
import org.fenixedu.bennu.core.i18n.BundleUtil;
import org.fenixedu.bennu.spring.portal.SpringFunctionality;
import org.fenixedu.treasury.domain.PaymentMethod;
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

//@Component("org.fenixedu.treasury.ui.administration.base.managePaymentMethod") <-- Use for duplicate controller name disambiguation
@SpringFunctionality(app = TreasuryController.class, title = "label.title.administration.base.managePaymentMethod",
        accessGroup = "logged")
// CHANGE_ME accessGroup = "group1 | group2 | groupXPTO"
@RequestMapping(PaymentMethodController.CONTROLLER_URL)
public class PaymentMethodController extends TreasuryBaseController {
    public static final String CONTROLLER_URL = "/treasury/administration/base/managepaymentmethod/paymentmethod";
    private static final String SEARCH_URI = "/";
    public static final String SEARCH_URL = CONTROLLER_URL + SEARCH_URI;
    private static final String UPDATE_URI = "/update/";
    public static final String UPDATE_URL = CONTROLLER_URL + UPDATE_URI;
    private static final String CREATE_URI = "/create";
    public static final String CREATE_URL = CONTROLLER_URL + CREATE_URI;
    private static final String READ_URI = "/read/";
    public static final String READ_URL = CONTROLLER_URL + READ_URI;
    private static final String DELETE_URI = "/delete/";
    public static final String DELETE_URL = CONTROLLER_URL + DELETE_URI;

//

    @RequestMapping
    public String home(Model model) {
        //this is the default behaviour, for handling in a Spring Functionality
        return "forward:/treasury/administration/base/managepaymentmethod/paymentmethod/";
    }

    private PaymentMethod getPaymentMethod(Model m) {
        return (PaymentMethod) m.asMap().get("paymentMethod");
    }

    private void setPaymentMethod(PaymentMethod paymentMethod, Model m) {
        m.addAttribute("paymentMethod", paymentMethod);
    }

    @Atomic
    public void deletePaymentMethod(PaymentMethod paymentMethod) {
        // CHANGE_ME: Do the processing for deleting the paymentMethod
        // Do not catch any exception here

        paymentMethod.delete();
    }

//				
    @RequestMapping(value = SEARCH_URI)
    public String search(@RequestParam(value = "code", required = false) java.lang.String code, @RequestParam(value = "name",
            required = false) org.fenixedu.commons.i18n.LocalizedString name, Model model) {
        List<PaymentMethod> searchpaymentmethodResultsDataSet = filterSearchPaymentMethod(code, name);

        //add the results dataSet to the model
        model.addAttribute("searchpaymentmethodResultsDataSet", searchpaymentmethodResultsDataSet);
        return "treasury/administration/base/managepaymentmethod/paymentmethod/search";
    }

    private Stream<PaymentMethod> getSearchUniverseSearchPaymentMethodDataSet() {
        //
        //The initialization of the result list must be done here
        //
        //
        return PaymentMethod.findAll(); //CHANGE_ME
    }

    private List<PaymentMethod> filterSearchPaymentMethod(java.lang.String code, org.fenixedu.commons.i18n.LocalizedString name) {

        return getSearchUniverseSearchPaymentMethodDataSet()
                .filter(paymentMethod -> code == null || code.length() == 0 || paymentMethod.getCode() != null
                        && paymentMethod.getCode().length() > 0
                        && paymentMethod.getCode().toLowerCase().contains(code.toLowerCase()))
                .filter(paymentMethod -> name == null
                        || name.isEmpty()
                        || name.getLocales()
                                .stream()
                                .allMatch(
                                        locale -> paymentMethod.getName().getContent(locale) != null
                                                && paymentMethod.getName().getContent(locale).toLowerCase()
                                                        .contains(name.getContent(locale).toLowerCase())))
                .collect(Collectors.toList());
    }

    @RequestMapping(value = "/search/view/{oid}")
    public String processSearchToViewAction(@PathVariable("oid") PaymentMethod paymentMethod, Model model,
            RedirectAttributes redirectAttributes) {

        // CHANGE_ME Insert code here for processing viewAction
        // If you selected multiple exists you must choose which one to use below	 
        return redirect(
                "/treasury/administration/base/managepaymentmethod/paymentmethod/read" + "/" + paymentMethod.getExternalId(),
                model, redirectAttributes);
    }

//				
    @RequestMapping(value = READ_URI + "{oid}")
    public String read(@PathVariable("oid") PaymentMethod paymentMethod, Model model) {
        setPaymentMethod(paymentMethod, model);
        return "treasury/administration/base/managepaymentmethod/paymentmethod/read";
    }

//
    @RequestMapping(value = DELETE_URI + "{oid}", method = RequestMethod.POST)
    public String delete(@PathVariable("oid") PaymentMethod paymentMethod, Model model, RedirectAttributes redirectAttributes) {

        setPaymentMethod(paymentMethod, model);
        try {
            //call the Atomic delete function
            deletePaymentMethod(paymentMethod);

            addInfoMessage(BundleUtil.getString(Constants.BUNDLE, "label.success.delete"), model);
            return redirect("/treasury/administration/base/managepaymentmethod/paymentmethod/", model, redirectAttributes);
        } catch (DomainException ex) {
            //Add error messages to the list
            addErrorMessage(BundleUtil.getString(Constants.BUNDLE, "label.error.delete") + ex.getLocalizedMessage(), model);

        } catch (Exception ex) {
            //Add error messages to the list
            addErrorMessage(BundleUtil.getString(Constants.BUNDLE, "label.error.delete") + ex.getLocalizedMessage(), model);
        }

        //The default mapping is the same Read View
        return redirect("/treasury/administration/base/managepaymentmethod/paymentmethod/read/"
                + getPaymentMethod(model).getExternalId(), model, redirectAttributes);
    }

//				
    @RequestMapping(value = CREATE_URI, method = RequestMethod.GET)
    public String create(Model model) {
        return "treasury/administration/base/managepaymentmethod/paymentmethod/create";
    }

//				
    @RequestMapping(value = CREATE_URI, method = RequestMethod.POST)
    public String create(@RequestParam(value = "code", required = false) java.lang.String code, @RequestParam(value = "name",
            required = false) org.fenixedu.commons.i18n.LocalizedString name, Model model, RedirectAttributes redirectAttributes) {
        /*
        *  Creation Logic
        *	
        	do something();
        *    		
        */
        try {
            PaymentMethod paymentMethod = createPaymentMethod(code, name);

            /*
             * Success Validation
             */

            //Add the bean to be used in the View
            model.addAttribute("paymentMethod", paymentMethod);

            return redirect("/treasury/administration/base/managepaymentmethod/paymentmethod/read/"
                    + getPaymentMethod(model).getExternalId(), model, redirectAttributes);

        } catch (DomainException de) {

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
            addErrorMessage(BundleUtil.getString(Constants.BUNDLE, "label.error.create") + de.getLocalizedMessage(), model);
            return create(model);

        } catch (Exception de) {
            addErrorMessage(BundleUtil.getString(Constants.BUNDLE, "label.error.create") + de.getLocalizedMessage(), model);
            return create(model);
        }
    }

    @Atomic
    public PaymentMethod createPaymentMethod(java.lang.String code, org.fenixedu.commons.i18n.LocalizedString name) {
        /*
         * Modify the creation code here if you do not want to create
         * the object with the default constructor and use the setter
         * for each field
         */
        PaymentMethod paymentMethod = PaymentMethod.create(code, name);
        return paymentMethod;
    }

//				
    @RequestMapping(value = UPDATE_URI + "{oid}", method = RequestMethod.GET)
    public String update(@PathVariable("oid") PaymentMethod paymentMethod, Model model) {
        setPaymentMethod(paymentMethod, model);
        return "treasury/administration/base/managepaymentmethod/paymentmethod/update";
    }

//				
    @RequestMapping(value = UPDATE_URI + "{oid}", method = RequestMethod.POST)
    public String update(@PathVariable("oid") PaymentMethod paymentMethod,
            @RequestParam(value = "code", required = false) java.lang.String code,
            @RequestParam(value = "name", required = false) org.fenixedu.commons.i18n.LocalizedString name, Model model,
            RedirectAttributes redirectAttributes) {

        setPaymentMethod(paymentMethod, model);

        /*
        *  UpdateLogic here
        *	
        	do something();
        *    		
        */

        /*
         * Succes Update
         */
        try {
            updatePaymentMethod(code, name, model);

            return redirect("/treasury/administration/base/managepaymentmethod/paymentmethod/read/"
                    + getPaymentMethod(model).getExternalId(), model, redirectAttributes);

        } catch (DomainException de) {
            // @formatter: off

            /*
             * If there is any error in validation
             * 
             * Add a error / warning message
             * 
             * addErrorMessage(BundleUtil.getString(Constants.BUNDLE, "label.error.update") +
             * de.getLocalizedMessage(),model);
             * addWarningMessage(" Warning updating due to " +
             * de.getLocalizedMessage(),model);
             */
            // @formatter: on

            addErrorMessage(BundleUtil.getString(Constants.BUNDLE, "label.error.update") + de.getLocalizedMessage(), model);

            return update(paymentMethod, model);

        } catch (Exception de) {
            addErrorMessage(BundleUtil.getString(Constants.BUNDLE, "label.error.update") + de.getLocalizedMessage(), model);

            return update(paymentMethod, model);
        }
    }

    @Atomic
    public void updatePaymentMethod(java.lang.String code, org.fenixedu.commons.i18n.LocalizedString name, Model m) {
        /*
         * Modify the update code here if you do not want to update
         * the object with the default setter for each field
         */
        getPaymentMethod(m).setCode(code);
        getPaymentMethod(m).setName(name);
    }

}
