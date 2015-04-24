package org.fenixedu.treasury.ui.administration.base.managecustomertype;

import java.util.List;
import java.util.stream.Collectors;

import org.fenixedu.bennu.FenixeduTreasurySpringConfiguration;
import org.fenixedu.bennu.core.domain.exceptions.DomainException;
import org.fenixedu.bennu.core.i18n.BundleUtil;
import org.fenixedu.bennu.spring.portal.SpringFunctionality;
import org.fenixedu.treasury.domain.CustomerType;
import org.fenixedu.treasury.ui.TreasuryBaseController;
import org.fenixedu.treasury.ui.TreasuryController;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import pt.ist.fenixframework.Atomic;

//@Component("org.fenixedu.treasury.ui.administration.base.manageCustomerType") <-- Use for duplicate controller name disambiguation
@SpringFunctionality(app = TreasuryController.class, title = "label.title.administration.base.manageCustomerType",
        accessGroup = "logged")
// CHANGE_ME accessGroup = "group1 | group2 | groupXPTO"
@RequestMapping("/treasury/administration/base/managecustomertype/customertype")
public class CustomerTypeController extends TreasuryBaseController {
    //
    @RequestMapping
    public String home(Model model) {
        // this is the default behaviour, for handling in a Spring Functionality
        return "forward:/treasury/administration/base/managecustomertype/customertype/";
    }

    private CustomerType getCustomerType(Model m) {
        return (CustomerType) m.asMap().get("customerType");
    }

    private void setCustomerType(CustomerType customerType, Model m) {
        m.addAttribute("customerType", customerType);
    }

    @Atomic
    public void deleteCustomerType(CustomerType customerType) {
        // CHANGE_ME: Do the processing for deleting the customerType
        // Do not catch any exception here

        customerType.delete();
    }

    //
    @RequestMapping(value = "/")
    public String search(@RequestParam(value = "code", required = false) java.lang.String code, @RequestParam(value = "name",
            required = false) org.fenixedu.commons.i18n.LocalizedString name, Model model) {
        List<CustomerType> searchcustomertypeResultsDataSet = filterSearchCustomerType(code, name);

        // add the results dataSet to the model
        model.addAttribute("searchcustomertypeResultsDataSet", searchcustomertypeResultsDataSet);
        return "treasury/administration/base/managecustomertype/customertype/search";
    }

    private List<CustomerType> getSearchUniverseSearchCustomerTypeDataSet() {
        //
        // The initialization of the result list must be done here
        //
        //
        return CustomerType.findAll().collect(Collectors.toList());

    }

    private List<CustomerType> filterSearchCustomerType(java.lang.String code, org.fenixedu.commons.i18n.LocalizedString name) {

        return getSearchUniverseSearchCustomerTypeDataSet()
                .stream()
                .filter(customerType -> code == null || code.length() == 0 || customerType.getCode() != null
                        && customerType.getCode().length() > 0
                        && customerType.getCode().toLowerCase().contains(code.toLowerCase()))
                .filter(customerType -> name == null
                        || name.isEmpty()
                        || name.getLocales()
                                .stream()
                                .allMatch(
                                        locale -> customerType.getName().getContent(locale) != null
                                                && customerType.getName().getContent(locale).toLowerCase()
                                                        .contains(name.getContent(locale).toLowerCase())))
                .collect(Collectors.toList());
    }

    @RequestMapping(value = "/search/view/{oid}")
    public String processSearchToViewAction(@PathVariable("oid") CustomerType customerType, Model model,
            RedirectAttributes redirectAttributes) {

        // CHANGE_ME Insert code here for processing viewAction
        // If you selected multiple exists you must choose which one to use
        // below
        return redirect(
                "/treasury/administration/base/managecustomertype/customertype/read" + "/" + customerType.getExternalId(), model,
                redirectAttributes);
    }

    //
    @RequestMapping(value = "/read/{oid}")
    public String read(@PathVariable("oid") CustomerType customerType, Model model) {
        setCustomerType(customerType, model);
        return "treasury/administration/base/managecustomertype/customertype/read";
    }

    //
    @RequestMapping(value = "/delete/{oid}")
    public String delete(@PathVariable("oid") CustomerType customerType, Model model, RedirectAttributes redirectAttributes) {

        setCustomerType(customerType, model);
        try {
            // call the Atomic delete function
            deleteCustomerType(customerType);

            addInfoMessage(BundleUtil.getString(FenixeduTreasurySpringConfiguration.BUNDLE, "label.success.delete"), model);
            return redirect("/treasury/administration/base/managecustomertype/customertype/", model, redirectAttributes);

        } catch (DomainException ex) {
            // Add error messages to the list
            addErrorMessage(BundleUtil.getString(FenixeduTreasurySpringConfiguration.BUNDLE, "label.error.delete") + ex.getMessage(), model);

        } catch (Exception ex) {
            // Add error messages to the list
            addErrorMessage(BundleUtil.getString(FenixeduTreasurySpringConfiguration.BUNDLE, "label.error.delete") + ex.getMessage(), model);
        }

        // The default mapping is the same Read View
        return redirect("/treasury/administration/base/managecustomertype/customertype/read/"
                + getCustomerType(model).getExternalId(), model, redirectAttributes);
    }

    //
    @RequestMapping(value = "/create", method = RequestMethod.GET)
    public String create(Model model) {
        return "treasury/administration/base/managecustomertype/customertype/create";
    }

    //
    @RequestMapping(value = "/create", method = RequestMethod.POST)
    public String create(@RequestParam(value = "code", required = false) java.lang.String code, @RequestParam(value = "name",
            required = false) org.fenixedu.commons.i18n.LocalizedString name, Model model, RedirectAttributes redirectAttributes) {
        /*
         * Creation Logic
         * 
         * do something();
         */

        CustomerType customerType;
        try {
            customerType = createCustomerType(code, name);

            /*
             * Success Validation
             */

            // Add the bean to be used in the View
            model.addAttribute("customerType", customerType);

            return redirect("/treasury/administration/base/managecustomertype/customertype/read/"
                    + getCustomerType(model).getExternalId(), model, redirectAttributes);

        } catch (DomainException tde) {

            addErrorMessage(tde.getLocalizedMessage(), model);
            return create(model);

        } catch (Exception tde) {

            addErrorMessage(tde.getLocalizedMessage(), model);
            return create(model);
        }
    }

    @Atomic
    public CustomerType createCustomerType(java.lang.String code, org.fenixedu.commons.i18n.LocalizedString name) {
        /*
         * Modify the creation code here if you do not want to create the object
         * with the default constructor and use the setter for each field
         */
        CustomerType customerType = CustomerType.create(code, name);
        return customerType;
    }

    //
    @RequestMapping(value = "/update/{oid}", method = RequestMethod.GET)
    public String update(@PathVariable("oid") CustomerType customerType, Model model) {
        setCustomerType(customerType, model);
        return "treasury/administration/base/managecustomertype/customertype/update";
    }

    //
    @RequestMapping(value = "/update/{oid}", method = RequestMethod.POST)
    public String update(@PathVariable("oid") CustomerType customerType,
            @RequestParam(value = "code", required = false) java.lang.String code,
            @RequestParam(value = "name", required = false) org.fenixedu.commons.i18n.LocalizedString name, Model model,
            RedirectAttributes redirectAttributes) {

        setCustomerType(customerType, model);

        /*
         * UpdateLogic here
         * 
         * do something();
         */

        /*
         * Succes Update
         */
        try {
            updateCustomerType(code, name, model);

            return redirect("/treasury/administration/base/managecustomertype/customertype/read/"
                    + getCustomerType(model).getExternalId(), model, redirectAttributes);

        } catch (DomainException de) {
            addErrorMessage(" Error updating due to " + de.getLocalizedMessage(), model);
            return update(customerType, model);

        } catch (Exception de) {
            addErrorMessage(" Error updating due to " + de.getLocalizedMessage(), model);
            return update(customerType, model);

        }
    }

    @Atomic
    public void updateCustomerType(java.lang.String code, org.fenixedu.commons.i18n.LocalizedString name, Model m) {
        /*
         * Modify the update code here if you do not want to update the object
         * with the default setter for each field
         */
        getCustomerType(m).edit(code, name);
    }

}
