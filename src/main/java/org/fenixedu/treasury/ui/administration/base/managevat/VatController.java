package org.fenixedu.treasury.ui.administration.base.managevat;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.fenixedu.bennu.FenixeduTreasurySpringConfiguration;
import org.fenixedu.bennu.core.domain.exceptions.DomainException;
import org.fenixedu.bennu.core.i18n.BundleUtil;
import org.fenixedu.bennu.spring.portal.SpringFunctionality;
import org.fenixedu.treasury.domain.Vat;
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

//@Component("org.fenixedu.treasury.ui.administration.base.manageVat") <-- Use for duplicate controller name disambiguation
@SpringFunctionality(app = TreasuryController.class, title = "label.title.administration.base.manageVat", accessGroup = "logged")
// CHANGE_ME accessGroup = "group1 | group2 | groupXPTO"
@RequestMapping("/treasury/administration/base/managevat/vat")
public class VatController extends TreasuryBaseController {

//

    @RequestMapping
    public String home(Model model) {
        //this is the default behaviour, for handling in a Spring Functionality
        return "forward:/treasury/administration/base/managevat/vat/";
    }

    private Vat getVat(Model m) {
        return (Vat) m.asMap().get("vat");
    }

    private void setVat(Vat vat, Model m) {
        m.addAttribute("vat", vat);
    }

    @Atomic
    public void deleteVat(Vat vat) {
        // CHANGE_ME: Do the processing for deleting the vat
        // Do not catch any exception here

        vat.delete();
    }

//				
    @RequestMapping(value = "/")
    public String search(
            @RequestParam(value = "taxrate", required = false) java.math.BigDecimal taxRate,
            @RequestParam(value = "begindate", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSZ") org.joda.time.DateTime beginDate,
            @RequestParam(value = "enddate", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSZ") org.joda.time.DateTime endDate,
            Model model) {
        List<Vat> searchvatResultsDataSet = filterSearchVat(taxRate, beginDate, endDate);

        //add the results dataSet to the model
        model.addAttribute("searchvatResultsDataSet", searchvatResultsDataSet);
        return "treasury/administration/base/managevat/vat/search";
    }

    private List<Vat> getSearchUniverseSearchVatDataSet() {
        //
        //The initialization of the result list must be done here
        //
        //
        return new ArrayList<Vat>(Vat.readAll()); //CHANGE_ME
    }

    private List<Vat> filterSearchVat(java.math.BigDecimal taxRate, org.joda.time.DateTime beginDate,
            org.joda.time.DateTime endDate) {

        return getSearchUniverseSearchVatDataSet().stream().filter(vat -> taxRate == null || taxRate.equals(vat.getTaxRate()))
                .filter(vat -> beginDate == null || beginDate.equals(vat.getBeginDate()))
                .filter(vat -> endDate == null || endDate.equals(vat.getEndDate())).collect(Collectors.toList());
    }

    @RequestMapping(value = "/search/view/{oid}")
    public String processSearchToViewAction(@PathVariable("oid") Vat vat, Model model, RedirectAttributes redirectAttributes) {

        // CHANGE_ME Insert code here for processing viewAction
        // If you selected multiple exists you must choose which one to use below	 
        return redirect("/treasury/administration/base/managevat/vat/read" + "/" + vat.getExternalId(), model, redirectAttributes);
    }

//				
    @RequestMapping(value = "/read/{oid}")
    public String read(@PathVariable("oid") Vat vat, Model model) {
        setVat(vat, model);
        return "treasury/administration/base/managevat/vat/read";
    }

//
    @RequestMapping(value = "/delete/{oid}")
    public String delete(@PathVariable("oid") Vat vat, Model model, RedirectAttributes redirectAttributes) {

        setVat(vat, model);
        try {
            //call the Atomic delete function
            deleteVat(vat);

            addInfoMessage(BundleUtil.getString(Constants.BUNDLE, "label.success.delete"), model);
            return redirect("/treasury/administration/base/managevat/vat/", model, redirectAttributes);

        } catch (DomainException ex) {
            //Add error messages to the list
            addErrorMessage(BundleUtil.getString(Constants.BUNDLE, "label.error.delete") + ex.getMessage(), model);

        } catch (Exception ex) {
            //Add error messages to the list
            addErrorMessage(BundleUtil.getString(Constants.BUNDLE, "label.error.delete") + ex.getMessage(), model);
        }

        //The default mapping is the same Read View
        return redirect("treasury/administration/base/managevat/vat/read/" + getVat(model).getExternalId(), model,
                redirectAttributes);
    }

//				
    @RequestMapping(value = "/create", method = RequestMethod.GET)
    public String create(Model model) {
        return "treasury/administration/base/managevat/vat/create";
    }

//				
    @RequestMapping(value = "/create", method = RequestMethod.POST)
    public String create(
            @RequestParam(value = "taxrate", required = false) java.math.BigDecimal taxRate,
            @RequestParam(value = "begindate", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSZ") org.joda.time.DateTime beginDate,
            @RequestParam(value = "enddate", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSZ") org.joda.time.DateTime endDate,
            Model model, RedirectAttributes redirectAttributes) {

//        if (binding.hasErrors()) {
//            addErrorMessage(binding.getErrorCount() + " errors...", model);
//            return create(model);
//        }
        /*
        *  Creation Logic
        *	
        	do something();
        *    		
        */
        try {
            Vat vat = createVat(taxRate, beginDate, endDate);

            /*
             * Success Validation
             */

            //Add the bean to be used in the View
            model.addAttribute("vat", vat);

            return redirect("/treasury/administration/base/managevat/vat/read/" + getVat(model).getExternalId(), model,
                    redirectAttributes);

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
    public Vat createVat(java.math.BigDecimal taxRate, org.joda.time.DateTime beginDate, org.joda.time.DateTime endDate) {
        /*
         * Modify the creation code here if you do not want to create
         * the object with the default constructor and use the setter
         * for each field
         */
        Vat vat = Vat.create(null, null, taxRate, beginDate, endDate);
        return vat;
    }

//				
    @RequestMapping(value = "/update/{oid}", method = RequestMethod.GET)
    public String update(@PathVariable("oid") Vat vat, Model model) {
        setVat(vat, model);
        return "treasury/administration/base/managevat/vat/update";
    }

//				
    @RequestMapping(value = "/update/{oid}", method = RequestMethod.POST)
    public String update(
            @PathVariable("oid") Vat vat,
            @RequestParam(value = "taxrate", required = false) java.math.BigDecimal taxRate,
            @RequestParam(value = "begindate", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSZ") org.joda.time.DateTime beginDate,
            @RequestParam(value = "enddate", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSZ") org.joda.time.DateTime endDate,
            Model model, RedirectAttributes redirectAttributes) {

        setVat(vat, model);

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
            updateVat(taxRate, beginDate, endDate, model);

            return redirect("/treasury/administration/base/managevat/vat/read/" + getVat(model).getExternalId(), model,
                    redirectAttributes);

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
            return update(vat, model);

        } catch (Exception de) {
            addErrorMessage(" Error updating due to " + de.getLocalizedMessage(), model);
            return update(vat, model);
        }
    }

    @Atomic
    public void updateVat(java.math.BigDecimal taxRate, org.joda.time.DateTime beginDate, org.joda.time.DateTime endDate, Model m) {
        /*
         * Modify the update code here if you do not want to update
         * the object with the default setter for each field
         */
        getVat(m).setTaxRate(taxRate);
        getVat(m).setBeginDate(beginDate);
        getVat(m).setEndDate(endDate);
    }

}
