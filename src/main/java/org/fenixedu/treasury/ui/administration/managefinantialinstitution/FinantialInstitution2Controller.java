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
package org.fenixedu.treasury.ui.administration.managefinantialinstitution;

import java.util.ArrayList;
import java.util.List;

import org.fenixedu.bennu.core.domain.Bennu;
import org.fenixedu.bennu.spring.portal.SpringFunctionality;
import org.fenixedu.treasury.domain.Currency;
import org.fenixedu.treasury.domain.FinantialInstitution;
import org.fenixedu.treasury.domain.FiscalCountryRegion;
import org.fenixedu.treasury.domain.exceptions.TreasuryDomainException;
import org.fenixedu.treasury.dto.FinantialInstitutionBean;
import org.fenixedu.treasury.ui.TreasuryBaseController;
import org.fenixedu.treasury.ui.TreasuryController;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import pt.ist.fenixframework.Atomic;
import pt.ist.standards.geographic.Country;
import pt.ist.standards.geographic.District;
import pt.ist.standards.geographic.Municipality;

//@Component("org.fenixedu.treasury.ui.administration.manageFinantialInstitution") <-- Use for duplicate controller name disambiguation
@SpringFunctionality(app = TreasuryController.class, title = "label.title.administration.manageFinantialInstitution2",
        accessGroup = "logged")
// CHANGE_ME accessGroup = "group1 | group2 | groupXPTO"
//or
//@BennuSpringController(value = TreasuryController.class)
@RequestMapping(FinantialInstitution2Controller.CONTROLLER_URL)
public class FinantialInstitution2Controller extends TreasuryBaseController {
    public static final String CONTROLLER_URL = "/treasury/administration/managefinantialinstitution/finantialinstitution2";

//

    @RequestMapping
    public String home(Model model) {
        //this is the default behavior, for handling in a Spring Functionality
        return "forward:" + SEARCH_URL;
    }

    private FinantialInstitution getFinantialInstitution(Model m) {
        return (FinantialInstitution) m.asMap().get("finantialInstitution");
    }

    private void setFinantialInstitution(FinantialInstitution finantialInstitution, Model m) {
        m.addAttribute("finantialInstitution", finantialInstitution);
    }

    @Atomic
    public void deleteFinantialInstitution(FinantialInstitution finantialInstitution) {
        finantialInstitution.delete();
    }

    private static final String SEARCH_URI = "/";
    public static final String SEARCH_URL = CONTROLLER_URL + SEARCH_URI;

    @RequestMapping(value = SEARCH_URI)
    public String search(Model model) {
        List<FinantialInstitution> searchfinantialinstitutionResultsDataSet =
                getSearchUniverseSearchFinantialInstitutionDataSet();

        //add the results dataSet to the model
        model.addAttribute("searchfinantialinstitutionResultsDataSet", searchfinantialinstitutionResultsDataSet);
        return "treasury/administration/managefinantialinstitution/finantialinstitution2/search";
    }

    private List<FinantialInstitution> getSearchUniverseSearchFinantialInstitutionDataSet() {
        return new ArrayList<FinantialInstitution>(Bennu.getInstance().getFinantialInstitutionsSet());
    }

    private List<FiscalCountryRegion> getSearchUniverseFiscalCountryRegionsDataSet() {
        return new ArrayList<FiscalCountryRegion>(Bennu.getInstance().getFiscalCountryRegionsSet());
    }

    private static final String SEARCH_TO_VIEW_ACTION_URI = "/search/view/";
    public static final String SEARCH_TO_VIEW_ACTION_URL = CONTROLLER_URL + SEARCH_TO_VIEW_ACTION_URI;

    @RequestMapping(value = SEARCH_TO_VIEW_ACTION_URI + "{oid}")
    public String processSearchToViewAction(@PathVariable("oid") FinantialInstitution finantialInstitution, Model model,
            RedirectAttributes redirectAttributes) {
        return redirect(READ_URL + finantialInstitution.getExternalId(), model, redirectAttributes);
    }

    private static final String READ_URI = "/read/";
    public static final String READ_URL = CONTROLLER_URL + READ_URI;

    @RequestMapping(value = READ_URI + "{oid}")
    public String read(@PathVariable("oid") FinantialInstitution finantialInstitution, Model model) {
        setFinantialInstitution(finantialInstitution, model);
        return "treasury/administration/managefinantialinstitution/finantialinstitution2/read";
    }

    private static final String DELETE_URI = "/delete/";
    public static final String DELETE_URL = CONTROLLER_URL + DELETE_URI;

    @RequestMapping(value = DELETE_URI + "{oid}")
    public String delete(@PathVariable("oid") FinantialInstitution finantialInstitution, Model model,
            RedirectAttributes redirectAttributes) {

        setFinantialInstitution(finantialInstitution, model);
        try {
            deleteFinantialInstitution(finantialInstitution);

            addInfoMessage("Sucess deleting FinantialInstitution ...", model);
            return redirect(SEARCH_URL, model, redirectAttributes);
        } catch (TreasuryDomainException ex) {
            addErrorMessage("Error deleting the FinantialInstitution due to " + ex.getMessage(), model);
        }

        //The default mapping is the same Read View
        return redirect(READ_URL + getFinantialInstitution(model).getExternalId(), model, redirectAttributes);
    }

    @RequestMapping(value = CREATE_URI, method = RequestMethod.GET)
    public String create(Model model) {
        FinantialInstitutionBean bean = new FinantialInstitutionBean();
        return _create(bean, model);
    }

    private static final String CREATEPOSTBACK_URI = "/createpostback";
    public static final String CREATEPOSTBACK_URL = CONTROLLER_URL + DELETE_URI;

    @RequestMapping(value = CREATEPOSTBACK_URI, method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public @ResponseBody String createpostback(@RequestParam(value = "bean", required = false) FinantialInstitutionBean bean,
            Model model) {

        // Do validation logic ?!?!
        setFinantialInstitutionBean(bean, model);
        return getBeanJson(bean);
    }

    public String _create(FinantialInstitutionBean bean, Model model) {
        setFinantialInstitutionBean(bean, model);
        return "treasury/administration/managefinantialinstitution/finantialinstitution2/create";
    }

    private void setFinantialInstitutionBean(FinantialInstitutionBean bean, Model model) {
        bean.updateModelLists();
        bean.setFiscalcountryregions(getSearchUniverseFiscalCountryRegionsDataSet());

        model.addAttribute("finantialInstitutionBeanJson", getBeanJson(bean));
        model.addAttribute("finantialInstitutionBean", bean);
    }

    private static final String CREATE_URI = "/create";
    public static final String CREATE_URL = CONTROLLER_URL + CREATE_URI;

    @RequestMapping(value = CREATE_URI, method = RequestMethod.POST)
    public String create(@RequestParam(value = "bean", required = false) FinantialInstitutionBean bean, Model model,
            RedirectAttributes redirectAttributes) {
        try {
            FinantialInstitution finantialInstitution =
                    createFinantialInstitution(bean.getFiscalcountryregion(), bean.getCode(), bean.getFiscalNumber(),
                            bean.getCompanyId(), bean.getName(), bean.getCompanyName(), bean.getAddress(), bean.getCountry(),
                            bean.getDistrict(), bean.getMunicipality(), bean.getLocality(), bean.getZipCode(), bean.getCurrency());
            //Add the bean to be used in the View
            setFinantialInstitution(finantialInstitution, model);
            setFinantialInstitutionBean(bean, model);
            addInfoMessage("Sucess creating FinantialInstitution ...", model);
            return redirect(READ_URL + getFinantialInstitution(model).getExternalId(), model, redirectAttributes);
        } catch (Exception ex) {
            //Add error messages to the list
            addErrorMessage("Error creating the FinantialInstitution due to " + ex.getMessage(), model);
        }
        return _create(bean, model);
    }

    @Atomic
    public FinantialInstitution createFinantialInstitution(FiscalCountryRegion fiscalCountryRegion, String code,
            String fiscalNumber, String companyId, String name, String companyName, String address, Country country,
            District district, Municipality municipality, String locality, String zipCode, Currency currency) {
        FinantialInstitution finantialInstitution =
                FinantialInstitution.create(fiscalCountryRegion, currency, code, fiscalNumber, companyId, name, companyName,
                        address, country, district, municipality, locality, zipCode);
        return finantialInstitution;
    }

//
    private static final String UPDATE_URI = "/update/";
    public static final String UPDATE_URL = CONTROLLER_URL + UPDATE_URI;

    @RequestMapping(value = UPDATE_URI + "{oid}", method = RequestMethod.GET)
    public String update(@PathVariable("oid") FinantialInstitution finantialInstitution, Model model) {
        setFinantialInstitution(finantialInstitution, model);

        FinantialInstitutionBean bean = new FinantialInstitutionBean(finantialInstitution);
        return _update(bean, finantialInstitution, model);
    }

    private static final String UPDATEPOSTBACK_URI = "/updatepostback/";
    public static final String UPDATEPOSTBACK_URL = CONTROLLER_URL + UPDATE_URI;

    @RequestMapping(value = UPDATEPOSTBACK_URI + "{oid}", method = RequestMethod.POST,
            produces = "application/json;charset=UTF-8")
    public @ResponseBody String updatepostback(@PathVariable("oid") FinantialInstitution finantialInstitution, @RequestParam(
            value = "bean", required = false) FinantialInstitutionBean bean, Model model) {
        setFinantialInstitutionBean(bean, model);
        return getBeanJson(bean);
    }

    public String _update(FinantialInstitutionBean bean, FinantialInstitution finantialInstitution, Model model) {
        setFinantialInstitution(finantialInstitution, model);
        setFinantialInstitutionBean(bean, model);
        return "treasury/administration/managefinantialinstitution/finantialinstitution2/update";
    }

//				
    @RequestMapping(value = UPDATE_URI + "{oid}", method = RequestMethod.POST)
    public String update(@PathVariable("oid") FinantialInstitution finantialInstitution, @RequestParam(value = "bean",
            required = false) FinantialInstitutionBean bean, Model model, RedirectAttributes redirectAttributes) {

        setFinantialInstitution(finantialInstitution, model);
        setFinantialInstitutionBean(bean, model);

        try {
            updateFinantialInstitution(bean.getFiscalcountryregion(), bean.getCode(), bean.getFiscalNumber(),
                    bean.getCompanyId(), bean.getName(), bean.getCompanyName(), bean.getAddress(), bean.getCountry(),
                    bean.getDistrict(), bean.getMunicipality(), bean.getLocality(), bean.getZipCode(), bean.getCurrency(), model);

            addInfoMessage("Sucess updating FinantialInstitution ...", model);
            return redirect(READ_URL + getFinantialInstitution(model).getExternalId(), model, redirectAttributes);
        } catch (Exception ex) {
            //Add error messages to the list
            addErrorMessage("Error creating the FinantialInstitution due to " + ex.getMessage(), model);
        }

        return _update(bean, finantialInstitution, model);
    }

    @Atomic
    public void updateFinantialInstitution(FiscalCountryRegion region, String code, String fiscalNumber, String companyId,
            String name, String companyName, String address, Country country, District district, Municipality municipality,
            String locality, String zipCode, Currency currency, Model m) {

        getFinantialInstitution(m).setFiscalCountryRegion(region);
        getFinantialInstitution(m).setCurrency(currency);
        getFinantialInstitution(m).edit(region, currency, code, fiscalNumber, companyId, name, companyName, address, country,
                district, municipality, locality, zipCode);
    }
}
