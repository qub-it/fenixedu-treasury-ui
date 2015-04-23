package org.fenixedu.treasury.ui.administration.base.manageVatExemptionReason;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.fenixedu.bennu.FenixeduTreasurySpringConfiguration;
import org.fenixedu.bennu.core.domain.exceptions.DomainException;
import org.fenixedu.bennu.core.i18n.BundleUtil;
import org.fenixedu.bennu.spring.portal.SpringFunctionality;
import org.fenixedu.treasury.domain.VatExemptionReason;
import org.fenixedu.treasury.ui.TreasuryBaseController;
import org.fenixedu.treasury.ui.TreasuryController;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import pt.ist.fenixframework.Atomic;

//@Component("org.fenixedu.treasury.ui.administration.base.manageVatExemptionReason") <-- Use for duplicate controller name disambiguation
@SpringFunctionality(app = TreasuryController.class, title = "label.title.administration.base.manageVatExemptionReason",
        accessGroup = "anyone")
// CHANGE_ME accessGroup = "group1 | group2 | groupXPTO"
@RequestMapping("/treasury/administration/base/managevatexemptionreason/vatexemptionreason")
public class VatExemptionReasonController extends TreasuryBaseController {

//

    @RequestMapping
    public String home(Model model) {
        //this is the default behaviour, for handling in a Spring Functionality
        return "forward:/treasury/administration/base/managevatexemptionreason/vatexemptionreason/";
    }

    private VatExemptionReason getVatExemptionReason(Model m) {
        return (VatExemptionReason) m.asMap().get("vatExemptionReason");
    }

    private void setVatExemptionReason(VatExemptionReason vatExemptionReason, Model m) {
        m.addAttribute("vatExemptionReason", vatExemptionReason);
    }

    @Atomic
    public void deleteVatExemptionReason(VatExemptionReason vatExemptionReason) {
        // CHANGE_ME: Do the processing for deleting the vatExemptionReason
        // Do not catch any exception here

        vatExemptionReason.delete();
    }

//				
    @RequestMapping(value = "/")
    public String search(@RequestParam(value = "code", required = false) java.lang.String code, @RequestParam(value = "name",
            required = false) org.fenixedu.commons.i18n.LocalizedString name, Model model) {
        List<VatExemptionReason> searchvatexemptionreasonResultsDataSet = filterSearchVatExemptionReason(code, name);

        //add the results dataSet to the model
        model.addAttribute("searchvatexemptionreasonResultsDataSet", searchvatexemptionreasonResultsDataSet);
        return "treasury/administration/base/managevatexemptionreason/vatexemptionreason/search";
    }

    private List<VatExemptionReason> getSearchUniverseSearchVatExemptionReasonDataSet() {
        //
        //The initialization of the result list must be done here
        //
        //
        return new ArrayList<VatExemptionReason>(VatExemptionReason.readAll()); //CHANGE_ME
    }

    private List<VatExemptionReason> filterSearchVatExemptionReason(java.lang.String code,
            org.fenixedu.commons.i18n.LocalizedString name) {

        return getSearchUniverseSearchVatExemptionReasonDataSet()
                .stream()
                .filter(vatExemptionReason -> code == null || code.length() == 0 || vatExemptionReason.getCode() != null
                        && vatExemptionReason.getCode().length() > 0
                        && vatExemptionReason.getCode().toLowerCase().contains(code.toLowerCase()))
                .filter(vatExemptionReason -> name == null
                        || name.isEmpty()
                        || name.getLocales()
                                .stream()
                                .allMatch(
                                        locale -> vatExemptionReason.getName().getContent(locale) != null
                                                && vatExemptionReason.getName().getContent(locale).toLowerCase()
                                                        .contains(name.getContent(locale).toLowerCase())))
                .collect(Collectors.toList());
    }

    @RequestMapping(value = "/search/view/{oid}")
    public String processSearchToViewAction(@PathVariable("oid") VatExemptionReason vatExemptionReason, Model model,
            RedirectAttributes redirectAttributes) {

        // CHANGE_ME Insert code here for processing viewAction
        // If you selected multiple exists you must choose which one to use below	 
        return redirect("/treasury/administration/base/managevatexemptionreason/vatexemptionreason/read" + "/"
                + vatExemptionReason.getExternalId(), model, redirectAttributes);
    }

//				
    @RequestMapping(value = "/read/{oid}")
    public String read(@PathVariable("oid") VatExemptionReason vatExemptionReason, Model model) {
        setVatExemptionReason(vatExemptionReason, model);
        return "treasury/administration/base/managevatexemptionreason/vatexemptionreason/read";
    }

//
    @RequestMapping(value = "/delete/{oid}")
    public String delete(@PathVariable("oid") VatExemptionReason vatExemptionReason, Model model,
            RedirectAttributes redirectAttributes) {

        setVatExemptionReason(vatExemptionReason, model);
        try {
            //call the Atomic delete function
            deleteVatExemptionReason(vatExemptionReason);

            addInfoMessage("Sucess deleting VatExemptionReason ...", model);
            return redirect("/treasury/administration/base/managevatexemptionreason/vatexemptionreason/", model,
                    redirectAttributes);

        } catch (DomainException ex) {
            //Add error messages to the list
            addErrorMessage(BundleUtil.getString(FenixeduTreasurySpringConfiguration.BUNDLE, "label.error.delete") + ex.getMessage(), model);

        } catch (Exception ex) {
            //Add error messages to the list
            addErrorMessage(BundleUtil.getString(FenixeduTreasurySpringConfiguration.BUNDLE, "label.error.delete") + ex.getMessage(), model);
        }

        //The default mapping is the same Read View
        return redirect(
                "treasury/administration/base/managevatexemptionreason/vatexemptionreason/read/"
                        + getVatExemptionReason(model).getExternalId(), model, redirectAttributes);
    }

//				
    @RequestMapping(value = "/create", method = RequestMethod.GET)
    public String create(Model model) {
        return "treasury/administration/base/managevatexemptionreason/vatexemptionreason/create";
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

        /*
         * Success Validation
         */

        //Add the bean to be used in the View
        try {
            VatExemptionReason vatExemptionReason = createVatExemptionReason(code, name);
            model.addAttribute("vatExemptionReason", vatExemptionReason);

            return redirect("/treasury/administration/base/managevatexemptionreason/vatexemptionreason/read/"
                    + getVatExemptionReason(model).getExternalId(), model, redirectAttributes);

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
    public VatExemptionReason createVatExemptionReason(java.lang.String code, org.fenixedu.commons.i18n.LocalizedString name) {
        /*
         * Modify the creation code here if you do not want to create
         * the object with the default constructor and use the setter
         * for each field
         */
        VatExemptionReason vatExemptionReason = VatExemptionReason.create(code, name);
        return vatExemptionReason;
    }

//				
    @RequestMapping(value = "/update/{oid}", method = RequestMethod.GET)
    public String update(@PathVariable("oid") VatExemptionReason vatExemptionReason, Model model) {
        setVatExemptionReason(vatExemptionReason, model);
        return "treasury/administration/base/managevatexemptionreason/vatexemptionreason/update";
    }

//				
    @RequestMapping(value = "/update/{oid}", method = RequestMethod.POST)
    public String update(@PathVariable("oid") VatExemptionReason vatExemptionReason, @RequestParam(value = "code",
            required = false) java.lang.String code,
            @RequestParam(value = "name", required = false) org.fenixedu.commons.i18n.LocalizedString name, Model model,
            RedirectAttributes redirectAttributes) {

        setVatExemptionReason(vatExemptionReason, model);

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
            updateVatExemptionReason(code, name, model);

            return redirect("/treasury/administration/base/managevatexemptionreason/vatexemptionreason/read/"
                    + getVatExemptionReason(model).getExternalId(), model, redirectAttributes);

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
            return update(vatExemptionReason, model);

        } catch (Exception de) {
            addErrorMessage(" Error updating due to " + de.getLocalizedMessage(), model);
            return update(vatExemptionReason, model);

        }
    }

    @Atomic
    public void updateVatExemptionReason(java.lang.String code, org.fenixedu.commons.i18n.LocalizedString name, Model m) {
        /*
         * Modify the update code here if you do not want to update
         * the object with the default setter for each field
         */
        getVatExemptionReason(m).setCode(code);
        getVatExemptionReason(m).setName(name);
    }

}
