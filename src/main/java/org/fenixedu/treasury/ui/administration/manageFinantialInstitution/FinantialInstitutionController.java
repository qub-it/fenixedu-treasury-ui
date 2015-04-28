package org.fenixedu.treasury.ui.administration.manageFinantialInstitution;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.fenixedu.bennu.core.domain.exceptions.DomainException;
import org.fenixedu.bennu.spring.portal.SpringFunctionality;
import org.fenixedu.treasury.domain.FinantialInstitution;
import org.fenixedu.treasury.ui.TreasuryBaseController;
import org.fenixedu.treasury.ui.TreasuryController;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import pt.ist.fenixframework.Atomic;

//@Component("org.fenixedu.treasury.ui.administration.manageFinantialInstitution") <-- Use for duplicate controller name disambiguation
@SpringFunctionality(app = TreasuryController.class, title = "label.title.administration.manageFinantialInstitution",
        accessGroup = "logged")
// CHANGE_ME accessGroup = "group1 | group2 | groupXPTO"
//or
//@BennuSpringController(value = TreasuryController.class)
@RequestMapping("/treasury/administration/managefinantialinstitution/finantialinstitution")
public class FinantialInstitutionController extends TreasuryBaseController {

//

    @RequestMapping
    public String home(Model model) {
        //this is the default behaviour, for handling in a Spring Functionality
        return "forward:/treasury/administration/managefinantialinstitution/finantialinstitution/";
    }

    private FinantialInstitution getFinantialInstitution(Model m) {
        return (FinantialInstitution) m.asMap().get("finantialInstitution");
    }

    private void setFinantialInstitution(FinantialInstitution finantialInstitution, Model m) {
        m.addAttribute("finantialInstitution", finantialInstitution);
    }

    @Atomic
    public void deleteFinantialInstitution(FinantialInstitution finantialInstitution) {
        // CHANGE_ME: Do the processing for deleting the finantialInstitution
        // Do not catch any exception here

        // finantialInstitution.delete();
    }

//				
    @RequestMapping(value = "/")
    public String search(@RequestParam(value = "code", required = false) java.lang.String code, @RequestParam(
            value = "fiscalnumber", required = false) java.lang.String fiscalNumber, @RequestParam(value = "companyid",
            required = false) java.lang.String companyId, @RequestParam(value = "name", required = false) java.lang.String name,
            @RequestParam(value = "companyname", required = false) java.lang.String companyName, @RequestParam(value = "address",
                    required = false) java.lang.String address,
            @RequestParam(value = "country", required = false) pt.ist.standards.geographic.Country country, @RequestParam(
                    value = "district", required = false) pt.ist.standards.geographic.District district, @RequestParam(
                    value = "municipality", required = false) pt.ist.standards.geographic.Municipality municipality,
            @RequestParam(value = "locality", required = false) pt.ist.standards.geographic.Locality locality, @RequestParam(
                    value = "zipcode", required = false) pt.ist.standards.geographic.PostalCode zipCode, Model model) {
        List<FinantialInstitution> searchfinantialinstitutionResultsDataSet =
                filterSearchFinantialInstitution(code, fiscalNumber, companyId, name, companyName, address, country, district,
                        municipality, locality, zipCode);

        //add the results dataSet to the model
        model.addAttribute("searchfinantialinstitutionResultsDataSet", searchfinantialinstitutionResultsDataSet);
        return "treasury/administration/managefinantialinstitution/finantialinstitution/search";
    }

    private List<FinantialInstitution> getSearchUniverseSearchFinantialInstitutionDataSet() {
        //
        //The initialization of the result list must be done here
        //
        //
        // return new ArrayList<FinantialInstitution>(Bennu.getInstance().getFinantialInstitutionsSet()); //CHANGE_ME
        return new ArrayList<FinantialInstitution>();
    }

    private List<FinantialInstitution> filterSearchFinantialInstitution(java.lang.String code, java.lang.String fiscalNumber,
            java.lang.String companyId, java.lang.String name, java.lang.String companyName, java.lang.String address,
            pt.ist.standards.geographic.Country country, pt.ist.standards.geographic.District district,
            pt.ist.standards.geographic.Municipality municipality, pt.ist.standards.geographic.Locality locality,
            pt.ist.standards.geographic.PostalCode zipCode) {

        return getSearchUniverseSearchFinantialInstitutionDataSet()
                .stream()
                .filter(finantialInstitution -> code == null
                        || code.length() == 0
                        || (finantialInstitution.getCode() != null && finantialInstitution.getCode().length() > 0 && finantialInstitution
                                .getCode().toLowerCase().contains(code.toLowerCase())))
                .filter(finantialInstitution -> fiscalNumber == null
                        || fiscalNumber.length() == 0
                        || (finantialInstitution.getFiscalNumber() != null && finantialInstitution.getFiscalNumber().length() > 0 && finantialInstitution
                                .getFiscalNumber().toLowerCase().contains(fiscalNumber.toLowerCase())))
                .filter(finantialInstitution -> companyId == null
                        || companyId.length() == 0
                        || (finantialInstitution.getCompanyId() != null && finantialInstitution.getCompanyId().length() > 0 && finantialInstitution
                                .getCompanyId().toLowerCase().contains(companyId.toLowerCase())))
                .filter(finantialInstitution -> name == null
                        || name.length() == 0
                        || (finantialInstitution.getName() != null && finantialInstitution.getName().length() > 0 && finantialInstitution
                                .getName().toLowerCase().contains(name.toLowerCase())))
                .filter(finantialInstitution -> companyName == null
                        || companyName.length() == 0
                        || (finantialInstitution.getCompanyName() != null && finantialInstitution.getCompanyName().length() > 0 && finantialInstitution
                                .getCompanyName().toLowerCase().contains(companyName.toLowerCase())))
                .filter(finantialInstitution -> address == null
                        || address.length() == 0
                        || (finantialInstitution.getAddress() != null && finantialInstitution.getAddress().length() > 0 && finantialInstitution
                                .getAddress().toLowerCase().contains(address.toLowerCase())))
                .filter(finantialInstitution -> country == null || country.equals(finantialInstitution.getCountry()))
                .filter(finantialInstitution -> district == null || district.equals(finantialInstitution.getDistrict()))
                .filter(finantialInstitution -> municipality == null
                        || municipality.equals(finantialInstitution.getMunicipality()))
                .filter(finantialInstitution -> locality == null || locality.equals(finantialInstitution.getLocality()))
                .filter(finantialInstitution -> zipCode == null || zipCode.equals(finantialInstitution.getZipCode()))
                .collect(Collectors.toList());
    }

    @RequestMapping(value = "/search/view/{oid}")
    public String processSearchToViewAction(@PathVariable("oid") FinantialInstitution finantialInstitution, Model model) {

        // CHANGE_ME Insert code here for processing viewAction
        // If you selected multiple exists you must choose which one to use below	 
        return "redirect:/treasury/administration/managefinantialinstitution/finantialinstitution/read" + "/"
                + finantialInstitution.getExternalId();
    }

//				
    @RequestMapping(value = "/read/{oid}")
    public String read(@PathVariable("oid") FinantialInstitution finantialInstitution, Model model) {
        setFinantialInstitution(finantialInstitution, model);
        return "treasury/administration/managefinantialinstitution/finantialinstitution/read";
    }

//
    @RequestMapping(value = "/delete/{oid}")
    public String delete(@PathVariable("oid") FinantialInstitution finantialInstitution, Model model) {

        setFinantialInstitution(finantialInstitution, model);
        try {
            //call the Atomic delete function
            deleteFinantialInstitution(finantialInstitution);

            addInfoMessage("Sucess deleting FinantialInstitution ...", model);
            return "redirect:/treasury/administration/managefinantialinstitution/finantialinstitution/";
        } catch (DomainException ex) {
            //Add error messages to the list
            addErrorMessage("Error deleting the FinantialInstitution due to " + ex.getMessage(), model);
        }

        //The default mapping is the same Read View
        return "treasury/administration/managefinantialinstitution/finantialinstitution/read/"
                + getFinantialInstitution(model).getExternalId();
    }

//				
    @RequestMapping(value = "/create", method = RequestMethod.GET)
    public String create(Model model) {
        return "treasury/administration/managefinantialinstitution/finantialinstitution/create";
    }

//				
    @RequestMapping(value = "/create", method = RequestMethod.POST)
    public String create(@RequestParam(value = "code", required = false) java.lang.String code, @RequestParam(
            value = "fiscalnumber", required = false) java.lang.String fiscalNumber, @RequestParam(value = "companyid",
            required = false) java.lang.String companyId, @RequestParam(value = "name", required = false) java.lang.String name,
            @RequestParam(value = "companyname", required = false) java.lang.String companyName, @RequestParam(value = "address",
                    required = false) java.lang.String address,
            @RequestParam(value = "country", required = false) pt.ist.standards.geographic.Country country, @RequestParam(
                    value = "district", required = false) pt.ist.standards.geographic.District district, @RequestParam(
                    value = "municipality", required = false) pt.ist.standards.geographic.Municipality municipality,
            @RequestParam(value = "locality", required = false) java.lang.String locality, @RequestParam(value = "zipcode",
                    required = false) java.lang.String zipCode, Model model) {
        /*
        *  Creation Logic
        *	
        	do something();
        *    		
        */

        FinantialInstitution finantialInstitution =
                createFinantialInstitution(code, fiscalNumber, companyId, name, companyName, address, country, district,
                        municipality, locality, zipCode);

        /*
         * Success Validation
         */

        //Add the bean to be used in the View
        model.addAttribute("finantialInstitution", finantialInstitution);

        return "redirect:/treasury/administration/managefinantialinstitution/finantialinstitution/read/"
                + getFinantialInstitution(model).getExternalId();

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

    @Atomic
    public FinantialInstitution createFinantialInstitution(java.lang.String code, java.lang.String fiscalNumber,
            java.lang.String companyId, java.lang.String name, java.lang.String companyName, java.lang.String address,
            pt.ist.standards.geographic.Country country, pt.ist.standards.geographic.District district,
            pt.ist.standards.geographic.Municipality municipality, java.lang.String locality, java.lang.String zipCode) {
        /*
         * Modify the creation code here if you do not want to create
         * the object with the default constructor and use the setter
         * for each field
         */
        FinantialInstitution finantialInstitution =
                FinantialInstitution.create(code, fiscalNumber, companyId, name, companyName, address, country, district,
                        municipality, locality, zipCode);
        return finantialInstitution;
    }

//				
    @RequestMapping(value = "/update/{oid}", method = RequestMethod.GET)
    public String update(@PathVariable("oid") FinantialInstitution finantialInstitution, Model model) {
        setFinantialInstitution(finantialInstitution, model);
        return "treasury/administration/managefinantialinstitution/finantialinstitution/update";
    }

//				
    @RequestMapping(value = "/update/{oid}", method = RequestMethod.POST)
    public String update(@PathVariable("oid") FinantialInstitution finantialInstitution, @RequestParam(value = "code",
            required = false) java.lang.String code,
            @RequestParam(value = "fiscalnumber", required = false) java.lang.String fiscalNumber, @RequestParam(
                    value = "companyid", required = false) java.lang.String companyId, @RequestParam(value = "name",
                    required = false) java.lang.String name,
            @RequestParam(value = "companyname", required = false) java.lang.String companyName, @RequestParam(value = "address",
                    required = false) java.lang.String address,
            @RequestParam(value = "country", required = false) pt.ist.standards.geographic.Country country, @RequestParam(
                    value = "district", required = false) pt.ist.standards.geographic.District district, @RequestParam(
                    value = "municipality", required = false) pt.ist.standards.geographic.Municipality municipality,
            @RequestParam(value = "locality", required = false) java.lang.String locality, @RequestParam(value = "zipcode",
                    required = false) java.lang.String zipCode, Model model) {

        setFinantialInstitution(finantialInstitution, model);

        /*
        *  UpdateLogic here
        *	
        	do something();
        *    		
        */

        /*
         * Succes Update
         */
        updateFinantialInstitution(code, fiscalNumber, companyId, name, companyName, address, country, district, municipality,
                locality, zipCode, model);

        return "redirect:/treasury/administration/managefinantialinstitution/finantialinstitution/read/"
                + getFinantialInstitution(model).getExternalId();

        /*
         * If there is any error in validation 
         *
         * Add a error / warning message
         * 
         * addErrorMessage(" Error because ...",model);
         * addWarningMessage(" Waring becaus ...",model);
         
         * return update(finantialInstitution,model);
         */
    }

    @Atomic
    public void updateFinantialInstitution(java.lang.String code, java.lang.String fiscalNumber, java.lang.String companyId,
            java.lang.String name, java.lang.String companyName, java.lang.String address,
            pt.ist.standards.geographic.Country country, pt.ist.standards.geographic.District district,
            pt.ist.standards.geographic.Municipality municipality, java.lang.String locality, java.lang.String zipCode, Model m) {
        /*
         * Modify the update code here if you do not want to update
         * the object with the default setter for each field
         */
        getFinantialInstitution(m).edit(code, fiscalNumber, companyId, name, companyName, address, country, district,
                municipality, locality, zipCode);
    }

}
