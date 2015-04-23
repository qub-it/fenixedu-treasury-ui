package org.fenixedu.treasury.ui.administration.base.managePaymentMethod;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.fenixedu.bennu.FenixeduTreasurySpringConfiguration;
import org.fenixedu.bennu.core.domain.exceptions.DomainException;
import org.fenixedu.bennu.core.i18n.BundleUtil;
import org.fenixedu.bennu.spring.portal.SpringFunctionality;
import org.fenixedu.treasury.domain.PaymentMethod;
import org.fenixedu.treasury.ui.TreasuryBaseController;
import org.fenixedu.treasury.ui.TreasuryController;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import pt.ist.fenixframework.Atomic;

//@Component("org.fenixedu.treasury.ui.administration.base.managePaymentMethod") <-- Use for duplicate controller name disambiguation
@SpringFunctionality(app = TreasuryController.class, title = "label.title.administration.base.managePaymentMethod",
        accessGroup = "anyone")
// CHANGE_ME accessGroup = "group1 | group2 | groupXPTO"
@RequestMapping("/treasury/administration/base/managepaymentmethod/paymentmethod")
public class PaymentMethodController extends TreasuryBaseController {

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
    @RequestMapping(value = "/")
    public String search(@RequestParam(value = "code", required = false) java.lang.String code, @RequestParam(value = "name",
            required = false) org.fenixedu.commons.i18n.LocalizedString name, Model model) {
        List<PaymentMethod> searchpaymentmethodResultsDataSet = filterSearchPaymentMethod(code, name);

        //add the results dataSet to the model
        model.addAttribute("searchpaymentmethodResultsDataSet", searchpaymentmethodResultsDataSet);
        return "treasury/administration/base/managepaymentmethod/paymentmethod/search";
    }

    private List<PaymentMethod> getSearchUniverseSearchPaymentMethodDataSet() {
        //
        //The initialization of the result list must be done here
        //
        //
        return new ArrayList<PaymentMethod>(PaymentMethod.readAll()); //CHANGE_ME
    }

    private List<PaymentMethod> filterSearchPaymentMethod(java.lang.String code, org.fenixedu.commons.i18n.LocalizedString name) {

        return getSearchUniverseSearchPaymentMethodDataSet()
                .stream()
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
    @RequestMapping(value = "/read/{oid}")
    public String read(@PathVariable("oid") PaymentMethod paymentMethod, Model model) {
        setPaymentMethod(paymentMethod, model);
        return "treasury/administration/base/managepaymentmethod/paymentmethod/read";
    }

//
    @RequestMapping(value = "/delete/{oid}")
    public String delete(@PathVariable("oid") PaymentMethod paymentMethod, Model model, RedirectAttributes redirectAttributes) {

        setPaymentMethod(paymentMethod, model);
        try {
            //call the Atomic delete function
            deletePaymentMethod(paymentMethod);

            addInfoMessage("Sucess deleting PaymentMethod ...", model);
            return redirect("/treasury/administration/base/managepaymentmethod/paymentmethod/", model, redirectAttributes);
        } catch (DomainException ex) {
            //Add error messages to the list
            addErrorMessage(BundleUtil.getString(FenixeduTreasurySpringConfiguration.BUNDLE, "label.error.delete") + ex.getMessage(), model);

        } catch (Exception ex) {
            //Add error messages to the list
            addErrorMessage(BundleUtil.getString(FenixeduTreasurySpringConfiguration.BUNDLE, "label.error.delete") + ex.getMessage(), model);
        }

        //The default mapping is the same Read View
        return redirect("/treasury/administration/base/managepaymentmethod/paymentmethod/read/"
                + getPaymentMethod(model).getExternalId(), model, redirectAttributes);
    }

//				
    @RequestMapping(value = "/create", method = RequestMethod.GET)
    public String create(Model model) {
        return "treasury/administration/base/managepaymentmethod/paymentmethod/create";
    }

//				
    @RequestMapping(value = "/create", method = RequestMethod.POST)
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
            addErrorMessage(BundleUtil.getString(FenixeduTreasurySpringConfiguration.BUNDLE, "label.error.create") + de.getLocalizedMessage(), model);
            return create(model);

        } catch (Exception de) {
            addErrorMessage(BundleUtil.getString(FenixeduTreasurySpringConfiguration.BUNDLE, "label.error.create") + de.getLocalizedMessage(), model);
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
    @RequestMapping(value = "/update/{oid}", method = RequestMethod.GET)
    public String update(@PathVariable("oid") PaymentMethod paymentMethod, Model model) {
        setPaymentMethod(paymentMethod, model);
        return "treasury/administration/base/managepaymentmethod/paymentmethod/update";
    }

//				
    @RequestMapping(value = "/update/{oid}", method = RequestMethod.POST)
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
             * addErrorMessage(" Error updating due to " +
             * de.getLocalizedMessage(),model);
             * addWarningMessage(" Warning updating due to " +
             * de.getLocalizedMessage(),model);
             */
            // @formatter: on

            addErrorMessage(" Error updating due to " + de.getLocalizedMessage(), model);
            return update(paymentMethod, model);

        } catch (Exception de) {
            addErrorMessage(" Error updating due to " + de.getLocalizedMessage(), model);
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
