package org.fenixedu.treasury.ui.administration.manageFiscalCountryRegion;

import java.util.ArrayList;
import java.util.List;

import org.fenixedu.bennu.spring.portal.SpringFunctionality;
import org.fenixedu.commons.i18n.LocalizedString;
import org.fenixedu.treasury.domain.FiscalCountryRegion;
import org.fenixedu.treasury.ui.FenixeduTreasuryApplication;
import org.fenixedu.treasury.ui.TreasuryBaseController;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.google.common.collect.Lists;

@SpringFunctionality(app = FenixeduTreasuryApplication.class, title = "label.title.manageFiscalCountryRegion",
        accessGroup = "anyone")
@RequestMapping("/administration/treasury/managefiscalcountryregion/fiscalcountryregion")
public class FiscalCountryRegionController extends TreasuryBaseController {

    @RequestMapping
    public String home(Model model) {
        return "forward:/administration/treasury/managefiscalcountryregion/fiscalcountryregion/";
    }

    private FiscalCountryRegion getFiscalCountryRegion(Model m) {
        return (FiscalCountryRegion) m.asMap().get("fiscalCountryRegion");
    }

    private void setFiscalCountryRegion(FiscalCountryRegion fiscalCountryRegion, Model m) {
        m.addAttribute("fiscalCountryRegion", fiscalCountryRegion);
    }

    public void deleteFiscalCountryRegion(FiscalCountryRegion fiscalCountryRegion) {
        fiscalCountryRegion.delete();
    }

    @RequestMapping(value = "/")
    public String search(Model model) {
        List<FiscalCountryRegion> searchfiscalcountryregionResultsDataSet = filterSearchFiscalCountryRegion();

        model.addAttribute("searchfiscalcountryregionResultsDataSet", searchfiscalcountryregionResultsDataSet);
        return "domain/managefiscalcountryregion/fiscalcountryregion/search";
    }

    private List<FiscalCountryRegion> getSearchUniverseSearchFiscalCountryRegionDataSet() {
        return new ArrayList<FiscalCountryRegion>(FiscalCountryRegion.readAll());
    }

    private List<FiscalCountryRegion> filterSearchFiscalCountryRegion() {
        return Lists.newArrayList(FiscalCountryRegion.readAll());
    }

    @RequestMapping(value = "/search/view/{oid}")
    public String processSearchToViewAction(@PathVariable("oid") FiscalCountryRegion fiscalCountryRegion, Model model) {

        return "redirect:/administration/treasury/managefiscalcountryregion/fiscalcountryregion/read" + "/" + fiscalCountryRegion.getExternalId();
    }

    @RequestMapping(value = "/search/delete/{oid}")
    public String processSearchToDeleteAction(@PathVariable("oid") FiscalCountryRegion fiscalCountryRegion, Model model) {
        deleteFiscalCountryRegion(fiscalCountryRegion);
        return "redirect:/administration/treasury/managefiscalcountryregion/fiscalcountryregion/";
    }

    @RequestMapping(value = "/read/{oid}")
    public String read(@PathVariable("oid") FiscalCountryRegion fiscalCountryRegion, Model model) {
        setFiscalCountryRegion(fiscalCountryRegion, model);
        return "domain/managefiscalcountryregion/fiscalcountryregion/read";
    }

    @RequestMapping(value = "/create", method = RequestMethod.GET)
    public String create(Model model) {
        return "domain/managefiscalcountryregion/fiscalcountryregion/create";
    }

    @RequestMapping(value = "/create", method = RequestMethod.POST)
    public String create(@RequestParam(value = "regioncode", required = false) java.lang.String regionCode, @RequestParam(
            value = "name", required = false) org.fenixedu.commons.i18n.LocalizedString name, Model model) {
        /*
        *  Creation Logic
        *	
        	do something();
        *    		
        */

        FiscalCountryRegion fiscalCountryRegion = createFiscalCountryRegion(regionCode, name);

        /*
         * Success Validation
         */

        //Add the bean to be used in the View
        model.addAttribute("fiscalCountryRegion", fiscalCountryRegion);

        return "redirect:/administration/treasury/managefiscalcountryregion/fiscalcountryregion/read/"
                + getFiscalCountryRegion(model).getExternalId();

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
    }

    public FiscalCountryRegion createFiscalCountryRegion(java.lang.String regionCode,
            org.fenixedu.commons.i18n.LocalizedString name) {
        /*
         * Modify the creation code here if you do not want to create
         * the object with the default constructor and use the setter
         * for each field
         */

        return FiscalCountryRegion.create(regionCode, name);
    }

    @RequestMapping(value = "/update/{oid}", method = RequestMethod.GET)
    public String update(@PathVariable("oid") FiscalCountryRegion fiscalCountryRegion, Model model) {
        setFiscalCountryRegion(fiscalCountryRegion, model);
        return "domain/managefiscalcountryregion/fiscalcountryregion/update";
    }

    @RequestMapping(value = "/update/{oid}", method = RequestMethod.POST)
    public String update(@PathVariable("oid") FiscalCountryRegion fiscalCountryRegion, @RequestParam(value = "regioncode",
            required = false) java.lang.String regionCode,
            @RequestParam(value = "name", required = false) org.fenixedu.commons.i18n.LocalizedString name, Model model) {

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
        updateFiscalCountryRegion(fiscalCountryRegion, regionCode, name);

        return "redirect:/administration/treasury/managefiscalcountryregion/fiscalcountryregion/read/"
                + getFiscalCountryRegion(model).getExternalId();

        /*
         * If there is any error in validation 
         *
         * Add a error / warning message
         * 
         * addErrorMessage(" Error because ...",model);
         * addWarningMessage(" Waring becaus ...",model);
         
         * return update(fiscalCountryRegion,model);
         */
    }

    public void updateFiscalCountryRegion(FiscalCountryRegion fiscalCountryRegion, String regionCode, LocalizedString name) {
        fiscalCountryRegion.edit(regionCode, name);
    }

}
