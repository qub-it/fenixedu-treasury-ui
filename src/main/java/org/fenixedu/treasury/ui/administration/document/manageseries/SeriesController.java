/**
 * This file was created by Quorum Born IT <http://www.qub-it.com/> and its 
 * copyright terms are bind to the legal agreement regulating the FenixEdu@ULisboa 
 * software development project between Quorum Born IT and ServiÃ§os Partilhados da
 * Universidade de Lisboa:
 *  - Copyright Â© 2015 Quorum Born IT (until any Go-Live phase)
 *  - Copyright Â© 2015 Universidade de Lisboa (after any Go-Live phase)
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
package org.fenixedu.treasury.ui.administration.document.manageseries;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.fenixedu.bennu.core.domain.exceptions.DomainException;
import org.fenixedu.bennu.spring.portal.SpringFunctionality;
import org.fenixedu.treasury.domain.document.Series;
import org.fenixedu.treasury.ui.TreasuryBaseController;
import org.fenixedu.treasury.ui.TreasuryController;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

//@Component("org.fenixedu.treasury.ui.administration.document.manageSeries") <-- Use for duplicate controller name disambiguation
@SpringFunctionality(app = TreasuryController.class, title = "label.title.administration.document.manageSeries",
        accessGroup = "logged")
// CHANGE_ME accessGroup = "group1 | group2 | groupXPTO"
//or
//@BennuSpringController(value=TreasuryController.class) 
@RequestMapping(SeriesController.CONTROLLER_URL)
public class SeriesController extends TreasuryBaseController {
    public static final String CONTROLLER_URL = "/treasury/administration/document/manageseries/series";
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
        return "forward:/treasury/administration/document/manageseries/series/";
    }

    private Series getSeries(Model model) {
        return (Series) model.asMap().get("series");
    }

    private void setSeries(Series series, Model model) {
        model.addAttribute("series", series);
    }

//    @Atomic
//    public void deleteSeries(Series series) {
//        // CHANGE_ME: Do the processing for deleting the series
//        // Do not catch any exception here
//
//        series.delete();
//    }

//				
    @RequestMapping(value = SEARCH_URI)
    public String search(Model model) {
        Set<Series> searchseriesResultsDataSet = getSearchUniverseSearchSeriesDataSet();

        //add the results dataSet to the model
        model.addAttribute("searchseriesResultsDataSet", searchseriesResultsDataSet);
        return "treasury/administration/document/manageseries/series/search";
    }

    private Set<Series> getSearchUniverseSearchSeriesDataSet() {
        //
        //The initialization of the result list must be done here
        //
        //
        // return new ArrayList<Series>(Series.findAll().collect(Collectors.toList())); //CHANGE_ME
        return Series.readAll();
    }

//    private List<Series> filterSearchSeries(java.lang.String code, org.fenixedu.commons.i18n.LocalizedString name,
//            boolean externSeries, boolean certificated, boolean legacy) {
//
//        return getSearchUniverseSearchSeriesDataSet()
//                .stream()
//                .filter(series -> code == null || code.length() == 0 || series.getCode() != null && series.getCode().length() > 0
//                        && series.getCode().toLowerCase().contains(code.toLowerCase()))
//                .filter(series -> name == null
//                        || name.isEmpty()
//                        || name.getLocales()
//                                .stream()
//                                .allMatch(
//                                        locale -> series.getName().getContent(locale) != null
//                                                && series.getName().getContent(locale).toLowerCase()
//                                                        .contains(name.getContent(locale).toLowerCase())))
//                .filter(series -> series.getExternSeries() == true).filter(series -> series.getCertificated() == true)
//                .filter(series -> series.getLegacy() == true).collect(Collectors.toList());
//    }

    @RequestMapping(value = "/search/view/{oid}")
    public String processSearchToViewAction(@PathVariable("oid") Series series, Model model, RedirectAttributes redirectAttributes) {

        // CHANGE_ME Insert code here for processing viewAction
        // If you selected multiple exists you must choose which one to use below	 
        return redirect("/treasury/administration/document/manageseries/series/read" + "/" + series.getExternalId(), model,
                redirectAttributes);
    }

//				
    @RequestMapping(value = READ_URI + "{oid}")
    public String read(@PathVariable("oid") Series series, Model model) {
        setSeries(series, model);
        return "treasury/administration/document/manageseries/series/read";
    }

//
//    @RequestMapping(value = DELETE_URI + "{oid}", method = RequestMethod.POST)
//    public String delete(@PathVariable("oid") Series series, Model model, RedirectAttributes redirectAttributes) {
//
//        setSeries(series, model);
//        try {
//            //call the Atomic delete function
//            deleteSeries(series);
//
//            addInfoMessage(BundleUtil.getString(Constants.BUNDLE, "label.success.delete"), model);
//            return redirect("/treasury/administration/document/manageseries/series/", model, redirectAttributes);
//
//        } catch (DomainException ex) {
//            //Add error messages to the list 
//            addErrorMessage(BundleUtil.getString(Constants.BUNDLE, "label.error.delete") + ex.getLocalizedMessage(), model);
//
//        } catch (Exception ex) {
//            //Add error messages to the list
//            addErrorMessage(BundleUtil.getString(Constants.BUNDLE, "label.error.delete") + ex.getLocalizedMessage(), model);
//        }
//
//        //The default mapping is the same Read View
//        return redirect("treasury/administration/document/manageseries/series/read/" + getSeries(model).getExternalId(), model,
//                redirectAttributes);
//    }

//				
//    @RequestMapping(value = CREATE_URI, method = RequestMethod.GET)
//    public String create(Model model) {
//        model.addAttribute("finantialInstitutionList", FinantialInstitution.findAll().collect(Collectors.toSet()));
//
//        //model.addAttribute("Series_finantialInstitution_options", org.fenixedu.treasury.domain.FinantialInstitution.findAll()); // CHANGE_ME - MUST DEFINE RELATION
//
//        model.addAttribute("Series_bennu_options", new ArrayList<org.fenixedu.bennu.core.domain.Bennu>()); // CHANGE_ME - MUST DEFINE RELATION
//        //model.addAttribute("Series_bennu_options", org.fenixedu.bennu.core.domain.Bennu.findAll()); // CHANGE_ME - MUST DEFINE RELATION
//        return "treasury/administration/document/manageseries/series/create";
//    }
//
////				
//    @RequestMapping(value = CREATE_URI, method = RequestMethod.POST)
//    public String create(@RequestParam(value = "code", required = false) java.lang.String code, @RequestParam(value = "name",
//            required = false) org.fenixedu.commons.i18n.LocalizedString name, @RequestParam(value = "externseries",
//            required = false) boolean externSeries, @RequestParam(value = "certificated", required = false) boolean certificated,
//            @RequestParam(value = "legacy", required = false) boolean legacy, @RequestParam(value = "finantialinstitution",
//                    required = false) org.fenixedu.treasury.domain.FinantialInstitution finantialInstitution, @RequestParam(
//                    value = "bennu", required = false) org.fenixedu.bennu.core.domain.Bennu bennu, Model model,
//            RedirectAttributes redirectAttributes) {
//        /*
//        *  Creation Logic
//        */
//
//        try {
//
//            Series series = createSeries(code, name, externSeries, certificated, legacy, finantialInstitution, bennu);
//
//            //Success Validation
//            //Add the bean to be used in the View
//            model.addAttribute("series", series);
//            return redirect("/treasury/administration/document/manageseries/series/read/" + getSeries(model).getExternalId(),
//                    model, redirectAttributes);
//
//        } catch (DomainException de) {
//
//            // @formatter: off
//            /*
//             * If there is any error in validation 
//             *
//             * Add a error / warning message
//             * 
//             * addErrorMessage(BundleUtil.getString(Constants.BUNDLE, "label.error.create") + de.getLocalizedMessage(),model);
//             * addWarningMessage(" Warning creating due to "+ ex.getLocalizedMessage(),model); */
//            // @formatter: on
//
//            addErrorMessage(BundleUtil.getString(Constants.BUNDLE, "label.error.create") + de.getLocalizedMessage(), model);
//            return create(model);
//
//        } catch (Exception de) {
//            addErrorMessage(BundleUtil.getString(Constants.BUNDLE, "label.error.create") + de.getLocalizedMessage(), model);
//            return create(model);
//        }
//    }
//
//    @Atomic
//    public Series createSeries(java.lang.String code, org.fenixedu.commons.i18n.LocalizedString name, boolean externSeries,
//            boolean certificated, boolean legacy, org.fenixedu.treasury.domain.FinantialInstitution finantialInstitution,
//            org.fenixedu.bennu.core.domain.Bennu bennu) {
//
//        // @formatter: off
//
//        /*
//         * Modify the creation code here if you do not want to create
//         * the object with the default constructor and use the setter
//         * for each field
//         * 
//         */
//
//        // CHANGE_ME It's RECOMMENDED to use "Create service" in DomainObject
//        //Series series = series.create(fields_to_create);
//
//        //Instead, use individual SETTERS and validate "CheckRules" in the end
//        // @formatter: on
//
//        Series series = Series.create(finantialInstitution, code, name, externSeries, certificated, legacy, false);
//
//        return series;
//    }

//				
//    @RequestMapping(value = UPDATE_URI + "{oid}", method = RequestMethod.GET)
//    public String update(@PathVariable("oid") Series series, Model model) {
//        model.addAttribute("finantialInstitutionList", FinantialInstitution.findAll().collect(Collectors.toSet()));
//        //model.addAttribute("Series_finantialInstitution_options", org.fenixedu.treasury.domain.FinantialInstitution.findAll()); // CHANGE_ME - MUST DEFINE RELATION
//        model.addAttribute("Series_bennu_options", new ArrayList<org.fenixedu.bennu.core.domain.Bennu>()); // CHANGE_ME - MUST DEFINE RELATION
//        //model.addAttribute("Series_bennu_options", org.fenixedu.bennu.core.domain.Bennu.findAll()); // CHANGE_ME - MUST DEFINE RELATION
//        setSeries(series, model);
//        return "treasury/administration/document/manageseries/series/update";
//    }
//
////				
//    @RequestMapping(value = UPDATE_URI + "{oid}", method = RequestMethod.POST)
//    public String update(
//            @PathVariable("oid") Series series,
//            @RequestParam(value = "code", required = false) java.lang.String code,
//            @RequestParam(value = "name", required = false) org.fenixedu.commons.i18n.LocalizedString name,
//            @RequestParam(value = "externseries", required = false) boolean externSeries,
//            @RequestParam(value = "certificated", required = false) boolean certificated,
//            @RequestParam(value = "legacy", required = false) boolean legacy,
//            @RequestParam(value = "finantialinstitution", required = false) org.fenixedu.treasury.domain.FinantialInstitution finantialInstitution,
//            @RequestParam(value = "bennu", required = false) org.fenixedu.bennu.core.domain.Bennu bennu, Model model,
//            RedirectAttributes redirectAttributes) {
//
//        setSeries(series, model);
//
//        try {
//            /*
//            *  UpdateLogic here
//            */
//
//            updateSeries(code, name, externSeries, certificated, legacy, finantialInstitution, bennu, model);
//
//            /*Succes Update */
//
//            return redirect("/treasury/administration/document/manageseries/series/read/" + getSeries(model).getExternalId(),
//                    model, redirectAttributes);
//
//        } catch (DomainException de) {
//            // @formatter: off
//
//            /*
//            * If there is any error in validation 
//            *
//            * Add a error / warning message
//            * 
//            * addErrorMessage(" Error updating due to " + de.getLocalizedMessage(),model);
//            * addWarningMessage(" Warning updating due to " + de.getLocalizedMessage(),model);
//            */
//            // @formatter: on
//
//            addErrorMessage(" Error updating due to " + de.getLocalizedMessage(), model);
//            return update(series, model);
//
//        } catch (Exception de) {
//            addErrorMessage(" Error updating due to " + de.getLocalizedMessage(), model);
//            return update(series, model);
//
//        }
//    }
//
//    @Atomic
//    public void updateSeries(java.lang.String code, org.fenixedu.commons.i18n.LocalizedString name, boolean externSeries,
//            boolean certificated, boolean legacy, org.fenixedu.treasury.domain.FinantialInstitution finantialInstitution,
//            org.fenixedu.bennu.core.domain.Bennu bennu, Model model) {
//
//        // @formatter: off				
//        /*
//         * Modify the update code here if you do not want to update
//         * the object with the default setter for each field
//         */
//
//        // CHANGE_ME It's RECOMMENDED to use "Edit service" in DomainObject
//        //getSeries(model).edit(fields_to_edit);
//
//        //Instead, use individual SETTERS and validate "CheckRules" in the end
//        // @formatter: on
//
//        getSeries(model).setCode(code);
//        getSeries(model).setName(name);
//        getSeries(model).setExternSeries(externSeries);
//        getSeries(model).setCertificated(certificated);
//        getSeries(model).setLegacy(legacy);
//        getSeries(model).setFinantialInstitution(finantialInstitution);
//        getSeries(model).setBennu(bennu);
//    }

}
