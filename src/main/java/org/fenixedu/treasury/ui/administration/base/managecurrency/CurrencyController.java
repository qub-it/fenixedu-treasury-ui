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
package org.fenixedu.treasury.ui.administration.base.managecurrency;

import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.stream.Collectors;

import org.fenixedu.bennu.core.domain.exceptions.DomainException;
import org.fenixedu.bennu.core.i18n.BundleUtil;
import org.fenixedu.bennu.spring.portal.SpringFunctionality;
import org.fenixedu.treasury.domain.Currency;
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

//@Component("org.fenixedu.treasury.ui.administration.base.manageCurrency") <-- Use for duplicate controller name disambiguation
@SpringFunctionality(app = TreasuryController.class, title = "label.title.administration.base.manageCurrency",
        accessGroup = "logged")
// CHANGE_ME accessGroup = "group1 | group2 | groupXPTO"
@RequestMapping(CurrencyController.CONTROLLER_URL)
public class CurrencyController extends TreasuryBaseController {
//
    public static final String CONTROLLER_URL = "/treasury/administration/base/managecurrency/currency";
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

    @RequestMapping
    public String home(Model model) {
        //this is the default behaviour, for handling in a Spring Functionality
        return "forward:/treasury/administration/base/managecurrency/currency/";
    }

    private Currency getCurrency(Model m) {
        return (Currency) m.asMap().get("currency");
    }

    private void setCurrency(Currency currency, Model m) {
        m.addAttribute("currency", currency);
    }

    @Atomic
    public void deleteCurrency(Currency currency) {
        // CHANGE_ME: Do the processing for deleting the currency
        // Do not catch any exception here

        currency.delete();
    }

//				
    @RequestMapping(value = SEARCH_URI)
    public String search(@RequestParam(value = "code", required = false) java.lang.String code, @RequestParam(value = "name",
            required = false) org.fenixedu.commons.i18n.LocalizedString name,
            @RequestParam(value = "isocode", required = false) java.lang.String isoCode, @RequestParam(value = "symbol",
                    required = false) java.lang.String symbol, Model model) {
        List<Currency> searchcurrencyResultsDataSet = filterSearchCurrency(code, name, isoCode, symbol);

        //add the results dataSet to the model
        model.addAttribute("searchcurrencyResultsDataSet", searchcurrencyResultsDataSet);
        return "treasury/administration/base/managecurrency/currency/search";
    }

    private List<Currency> getSearchUniverseSearchCurrencyDataSet() {
        //
        //The initialization of the result list must be done here
        //
        //
        return Currency.findAll().collect(Collectors.toList());
    }

    private List<Currency> filterSearchCurrency(java.lang.String code, org.fenixedu.commons.i18n.LocalizedString name,
            java.lang.String isoCode, java.lang.String symbol) {

        return getSearchUniverseSearchCurrencyDataSet()
                .stream()
                .filter(currency -> code == null || code.length() == 0 || currency.getCode() != null
                        && currency.getCode().length() > 0 && currency.getCode().toLowerCase().contains(code.toLowerCase()))
                .filter(currency -> name == null
                        || name.isEmpty()
                        || name.getLocales()
                                .stream()
                                .allMatch(
                                        locale -> currency.getName().getContent(locale) != null
                                                && currency.getName().getContent(locale).toLowerCase()
                                                        .contains(name.getContent(locale).toLowerCase())))
                .filter(currency -> isoCode == null || isoCode.length() == 0 || currency.getIsoCode() != null
                        && currency.getIsoCode().length() > 0
                        && currency.getIsoCode().toLowerCase().contains(isoCode.toLowerCase()))
                .filter(currency -> symbol == null || symbol.length() == 0 || currency.getSymbol() != null
                        && currency.getSymbol().length() > 0 && currency.getSymbol().toLowerCase().contains(symbol.toLowerCase()))
                .collect(Collectors.toList());
    }

    @RequestMapping(value = "/search/view/{oid}")
    public String processSearchToViewAction(@PathVariable("oid") Currency currency, Model model,
            RedirectAttributes redirectAttributes) {

        // CHANGE_ME Insert code here for processing viewAction
        // If you selected multiple exists you must choose which one to use below	 
        return redirect("/treasury/administration/base/managecurrency/currency/read" + "/" + currency.getExternalId(), model,
                redirectAttributes);
    }

//				
    @RequestMapping(value = READ_URI + "{oid}")
    public String read(@PathVariable("oid") Currency currency, Model model) {
        setCurrency(currency, model);
        return "treasury/administration/base/managecurrency/currency/read";
    }

//
    @RequestMapping(value = "/delete/{oid}")
    public String delete(@PathVariable("oid") Currency currency, Model model, RedirectAttributes redirectAttributes) {

        setCurrency(currency, model);
        try {
            //call the Atomic delete function
            deleteCurrency(currency);

            addInfoMessage(BundleUtil.getString(Constants.BUNDLE, "label.success.delete"), model);
            return redirect("/treasury/administration/base/managecurrency/currency/", model, redirectAttributes);
        } catch (DomainException ex) {
            //Add error messages to the list
            addErrorMessage(BundleUtil.getString(Constants.BUNDLE, "label.error.delete") + ex.getMessage(), model);
        } catch (Exception ex) {
            //Add error messages to the list
            addErrorMessage(BundleUtil.getString(Constants.BUNDLE, "label.error.delete") + ex.getMessage(), model);
        }

        //The default mapping is the same Read View
        return redirect("/treasury/administration/base/managecurrency/currency/read/" + getCurrency(model).getExternalId(),
                model, redirectAttributes);
    }

//				
    @RequestMapping(value = CREATE_URI, method = RequestMethod.GET)
    public String create(Model model) {
        return "treasury/administration/base/managecurrency/currency/create";
    }

//				
    @RequestMapping(value = CREATE_URI, method = RequestMethod.POST)
    public String create(@RequestParam(value = "code", required = false) java.lang.String code, @RequestParam(value = "name",
            required = false) org.fenixedu.commons.i18n.LocalizedString name,
            @RequestParam(value = "isocode", required = false) java.lang.String isoCode, @RequestParam(value = "symbol",
                    required = false) java.lang.String symbol, Model model, RedirectAttributes redirectAttributes) {
        /*
        *  Creation Logic
        *	
        	do something();
        *    		
        */
        try {
            Currency currency = createCurrency(code, name, isoCode, symbol);

            /*
             * Success Validation
             */

            //Add the bean to be used in the View
            model.addAttribute("currency", currency);

            return redirect("/treasury/administration/base/managecurrency/currency/read/" + getCurrency(model).getExternalId(),
                    model, redirectAttributes);

        } catch (DomainException de) {
            addErrorMessage(BundleUtil.getString(Constants.BUNDLE, "label.error.create") + de.getLocalizedMessage(), model);
            return create(model);

        } catch (Exception de) {
            addErrorMessage(BundleUtil.getString(Constants.BUNDLE, "label.error.create") + de.getLocalizedMessage(), model);
            return create(model);
        }
    }

    @Atomic
    public Currency createCurrency(java.lang.String code, org.fenixedu.commons.i18n.LocalizedString name,
            java.lang.String isoCode, java.lang.String symbol) {
        /*
         * Modify the creation code here if you do not want to create
         * the object with the default constructor and use the setter
         * for each field
         */
        Currency currency = Currency.create(code, name, isoCode, symbol);
        return currency;
    }

//				
    @RequestMapping(value = UPDATE_URI + "{oid}", method = RequestMethod.GET)
    public String update(@PathVariable("oid") Currency currency, Model model) {
        setCurrency(currency, model);
        return "treasury/administration/base/managecurrency/currency/update";
    }

//				
    @RequestMapping(value = UPDATE_URI + "{oid}", method = RequestMethod.POST)
    public String update(@PathVariable("oid") Currency currency,
            @RequestParam(value = "code", required = false) java.lang.String code,
            @RequestParam(value = "name", required = false) org.fenixedu.commons.i18n.LocalizedString name, @RequestParam(
                    value = "isocode", required = false) java.lang.String isoCode, @RequestParam(value = "symbol",
                    required = false) java.lang.String symbol, Model model, RedirectAttributes redirectAttributes) {

        setCurrency(currency, model);

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
            updateCurrency(code, name, isoCode, symbol, model);

            return redirect("/treasury/administration/base/managecurrency/currency/read/" + getCurrency(model).getExternalId(),
                    model, redirectAttributes);

        } catch (DomainException de) {
            addErrorMessage(BundleUtil.getString(Constants.BUNDLE, "label.error.update") + de.getLocalizedMessage(), model);
            return update(currency, model);

        } catch (Exception de) {
            addErrorMessage(BundleUtil.getString(Constants.BUNDLE, "label.error.update") + de.getLocalizedMessage(), model);
            return update(currency, model);
        }
    }

    @Atomic
    public void updateCurrency(java.lang.String code, org.fenixedu.commons.i18n.LocalizedString name, java.lang.String isoCode,
            java.lang.String symbol, Model m) {
        /*
         * Modify the update code here if you do not want to update
         * the object with the default setter for each field
         */
        getCurrency(m).setCode(code);
        getCurrency(m).setName(name);
        getCurrency(m).setIsoCode(isoCode);
        getCurrency(m).setSymbol(symbol);
    }

}
