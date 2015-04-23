package org.fenixedu.treasury.ui.administration.base.manageFiscalCountryRegion;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.fenixedu.bennu.core.domain.exceptions.DomainException;
import org.fenixedu.bennu.spring.portal.SpringFunctionality;
import org.fenixedu.treasury.domain.FiscalCountryRegion;
import org.fenixedu.treasury.ui.TreasuryBaseController;
import org.fenixedu.treasury.ui.TreasuryController;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import pt.ist.fenixframework.Atomic;

//@Component("org.fenixedu.treasury.ui.administration.base.manageFiscalCountryRegion") <-- Use for duplicate controller name disambiguation
@SpringFunctionality(app = TreasuryController.class, title = "label.title.administration.base.manageFiscalCountryRegion",
        accessGroup = "anyone")
// CHANGE_ME accessGroup = "group1 | group2 | groupXPTO"
@RequestMapping("/treasury/administration/base/managefiscalcountryregion/fiscalcountryregion")
public class FiscalCountryRegionController extends TreasuryBaseController {

//

    @RequestMapping
    public String home(Model model) {
        //this is the default behaviour, for handling in a Spring Functionality
        return "forward:/treasury/administration/base/managefiscalcountryregion/fiscalcountryregion/";
    }

    private FiscalCountryRegion getFiscalCountryRegion(Model m) {
        return (FiscalCountryRegion) m.asMap().get("fiscalCountryRegion");
    }

    private void setFiscalCountryRegion(FiscalCountryRegion fiscalCountryRegion, Model m) {
        m.addAttribute("fiscalCountryRegion", fiscalCountryRegion);
    }

    @Atomic
    public void deleteFiscalCountryRegion(FiscalCountryRegion fiscalCountryRegion) {
        // CHANGE_ME: Do the processing for deleting the fiscalCountryRegion
        // Do not catch any exception here

        fiscalCountryRegion.delete();
    }

//				
    @RequestMapping(value = "/")
    public String search(@RequestParam(value = "fiscalCode", required = false) java.lang.String fiscalCode, @RequestParam(
            value = "name", required = false) org.fenixedu.commons.i18n.LocalizedString name, Model model) {
        List<FiscalCountryRegion> searchfiscalcountryregionResultsDataSet = filterSearchFiscalCountryRegion(fiscalCode, name);

        //add the results dataSet to the model
        model.addAttribute("searchfiscalcountryregionResultsDataSet", searchfiscalcountryregionResultsDataSet);
        return "treasury/administration/base/managefiscalcountryregion/fiscalcountryregion/search";
    }

    private List<FiscalCountryRegion> getSearchUniverseSearchFiscalCountryRegionDataSet() {
        //
        //The initialization of the result list must be done here
        //
        //
        return new ArrayList<FiscalCountryRegion>(FiscalCountryRegion.readAll()); //CHANGE_ME
    }

    private List<FiscalCountryRegion> filterSearchFiscalCountryRegion(java.lang.String fiscalCode,
            org.fenixedu.commons.i18n.LocalizedString name) {

        return getSearchUniverseSearchFiscalCountryRegionDataSet()
                .stream()
                .filter(fiscalCountryRegion -> fiscalCode == null || fiscalCode.length() == 0
                        || fiscalCountryRegion.getFiscalCode() != null && fiscalCountryRegion.getFiscalCode().length() > 0
                        && fiscalCountryRegion.getFiscalCode().toLowerCase().contains(fiscalCode.toLowerCase()))
                .filter(fiscalCountryRegion -> name == null
                        || name.isEmpty()
                        || name.getLocales()
                                .stream()
                                .allMatch(
                                        locale -> fiscalCountryRegion.getName().getContent(locale) != null
                                                && fiscalCountryRegion.getName().getContent(locale).toLowerCase()
                                                        .contains(name.getContent(locale).toLowerCase())))
                .collect(Collectors.toList());
    }

    @RequestMapping(value = "/search/view/{oid}")
    public String processSearchToViewAction(@PathVariable("oid") FiscalCountryRegion fiscalCountryRegion, Model model,
            RedirectAttributes redirectAttributes) {

        // CHANGE_ME Insert code here for processing viewAction
        // If you selected multiple exists you must choose which one to use below	 
        return redirect("/treasury/administration/base/managefiscalcountryregion/fiscalcountryregion/read" + "/"
                + fiscalCountryRegion.getExternalId(), model, redirectAttributes);
    }

//				
    @RequestMapping(value = "/read/{oid}")
    public String read(@PathVariable("oid") FiscalCountryRegion fiscalCountryRegion, Model model) {
        setFiscalCountryRegion(fiscalCountryRegion, model);
        return "treasury/administration/base/managefiscalcountryregion/fiscalcountryregion/read";
    }

//
    @RequestMapping(value = "/delete/{oid}")
    public String delete(@PathVariable("oid") FiscalCountryRegion fiscalCountryRegion, Model model,
            RedirectAttributes redirectAttributes) {

        setFiscalCountryRegion(fiscalCountryRegion, model);
        try {
            //call the Atomic delete function
            deleteFiscalCountryRegion(fiscalCountryRegion);

            addInfoMessage("Sucess deleting FiscalCountryRegion ...", model);
            return redirect("/treasury/administration/base/managefiscalcountryregion/fiscalcountryregion/", model,
                    redirectAttributes);

        } catch (DomainException ex) {
            //Add error messages to the list
            addErrorMessage("Error deleting the FiscalCountryRegion due to " + ex.getMessage(), model);

        } catch (Exception ex) {
            //Add error messages to the list
            addErrorMessage("Error deleting the FiscalCountryRegion due to " + ex.getMessage(), model);
        }

        //The default mapping is the same Read View
        return redirect("/treasury/administration/base/managefiscalcountryregion/fiscalcountryregion/read/"
                + getFiscalCountryRegion(model).getExternalId(), model, redirectAttributes);
    }

//				
    @RequestMapping(value = "/create", method = RequestMethod.GET)
    public String create(Model model) {
        return "treasury/administration/base/managefiscalcountryregion/fiscalcountryregion/create";
    }

//				
    @RequestMapping(value = "/create", method = RequestMethod.POST)
    public String create(@RequestParam(value = "fiscalCode", required = false) java.lang.String fiscalCode, @RequestParam(
            value = "name", required = false) org.fenixedu.commons.i18n.LocalizedString name, Model model,
            RedirectAttributes redirectAttributes) {
        /*
        *  Creation Logic
        *	
        	do something();
        *    		
        */
        try {
            FiscalCountryRegion fiscalCountryRegion = createFiscalCountryRegion(fiscalCode, name);

            /*
             * Success Validation
             */

            //Add the bean to be used in the View
            model.addAttribute("fiscalCountryRegion", fiscalCountryRegion);

            return redirect("/treasury/administration/base/managefiscalcountryregion/fiscalcountryregion/read/"
                    + getFiscalCountryRegion(model).getExternalId(), model, redirectAttributes);

        } catch (DomainException tde) {

            addErrorMessage(tde.getLocalizedMessage(), model);
            return create(model);

        } catch (Exception tde) {

            addErrorMessage(tde.getLocalizedMessage(), model);
            return create(model);
        }
    }

    @Atomic
    public FiscalCountryRegion createFiscalCountryRegion(java.lang.String fiscalCode,
            org.fenixedu.commons.i18n.LocalizedString name) {
        /*
         * Modify the creation code here if you do not want to create
         * the object with the default constructor and use the setter
         * for each field
         */
        FiscalCountryRegion fiscalCountryRegion = FiscalCountryRegion.create(fiscalCode, name);
        return fiscalCountryRegion;
    }

//				
    @RequestMapping(value = "/update/{oid}", method = RequestMethod.GET)
    public String update(@PathVariable("oid") FiscalCountryRegion fiscalCountryRegion, Model model) {
        setFiscalCountryRegion(fiscalCountryRegion, model);
        return "treasury/administration/base/managefiscalcountryregion/fiscalcountryregion/update";
    }

//				
    @RequestMapping(value = "/update/{oid}", method = RequestMethod.POST)
    public String update(@PathVariable("oid") FiscalCountryRegion fiscalCountryRegion, @RequestParam(value = "fiscalCode",
            required = false) java.lang.String fiscalCode,
            @RequestParam(value = "name", required = false) org.fenixedu.commons.i18n.LocalizedString name, Model model,
            RedirectAttributes redirectAttributes) {

        setFiscalCountryRegion(fiscalCountryRegion, model);

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
            updateFiscalCountryRegion(fiscalCode, name, model);

            return redirect("/treasury/administration/base/managefiscalcountryregion/fiscalcountryregion/read/"
                    + getFiscalCountryRegion(model).getExternalId(), model, redirectAttributes);

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
            return update(fiscalCountryRegion, model);

        } catch (Exception de) {
            addErrorMessage(" Error updating due to " + de.getLocalizedMessage(), model);
            return update(fiscalCountryRegion, model);

        }
    }

    @Atomic
    public void updateFiscalCountryRegion(java.lang.String fiscalCode, org.fenixedu.commons.i18n.LocalizedString name, Model m) {
        /*
         * Modify the update code here if you do not want to update
         * the object with the default setter for each field
         */
        getFiscalCountryRegion(m).setFiscalCode(fiscalCode);
        getFiscalCountryRegion(m).setName(name);
    }

}
