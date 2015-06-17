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
package org.fenixedu.treasury.ui.administration.document.managedocumentnumberseries;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.fenixedu.bennu.core.domain.exceptions.DomainException;
import org.fenixedu.bennu.core.i18n.BundleUtil;
import org.fenixedu.bennu.spring.portal.BennuSpringController;
import org.fenixedu.treasury.domain.document.DocumentNumberSeries;
import org.fenixedu.treasury.domain.document.Series;
import org.fenixedu.treasury.ui.TreasuryBaseController;
import org.fenixedu.treasury.ui.administration.managefinantialinstitution.FinantialInstitutionController;
import org.fenixedu.treasury.ui.administration.managefinantialinstitution.SeriesController;
import org.fenixedu.treasury.util.Constants;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import pt.ist.fenixframework.Atomic;

//@Component("org.fenixedu.treasury.ui.administration.document.manageDocumentNumberSeries") <-- Use for duplicate controller name disambiguation
//@SpringFunctionality(app = TreasuryController.class, title = "label.title.administration.document.manageDocumentNumberSeries",
//        accessGroup = "#managers")
// CHANGE_ME accessGroup = "group1 | group2 | groupXPTO"
//or
@BennuSpringController(value = FinantialInstitutionController.class)
@RequestMapping(DocumentNumberSeriesController.CONTROLLER_URL)
public class DocumentNumberSeriesController extends TreasuryBaseController {
    public static final String CONTROLLER_URL =
            "/treasury/administration/document/managedocumentnumberseries/documentnumberseries";
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
        return "forward:" + SEARCH_URL;
    }

    private DocumentNumberSeries getDocumentNumberSeries(Model model) {
        return (DocumentNumberSeries) model.asMap().get("documentNumberSeries");
    }

    private void setDocumentNumberSeries(DocumentNumberSeries documentNumberSeries, Model model) {
        model.addAttribute("documentNumberSeries", documentNumberSeries);
    }

    @Atomic
    public void deleteDocumentNumberSeries(DocumentNumberSeries documentNumberSeries) {
        // CHANGE_ME: Do the processing for deleting the documentNumberSeries
        // Do not catch any exception here

        documentNumberSeries.delete();
    }

//				

    @RequestMapping(value = SEARCH_URI)
    public String search(@RequestParam(value = "series", required = false) Series series, Model model) {
        List<DocumentNumberSeries> searchdocumentnumberseriesResultsDataSet = filterSearchDocumentNumberSeries(series);
        //add the results dataSet to the model
        model.addAttribute("searchdocumentnumberseriesResultsDataSet", searchdocumentnumberseriesResultsDataSet);
        return "treasury/administration/document/managedocumentnumberseries/documentnumberseries/search";
    }

    private List<DocumentNumberSeries> getSearchUniverseSearchDocumentNumberSeriesDataSet() {
        //
        //The initialization of the result list must be done here
        //
        //
        return DocumentNumberSeries.findAll().collect(Collectors.toList());
    }

    private List<DocumentNumberSeries> filterSearchDocumentNumberSeries(Series series) {

        Stream<DocumentNumberSeries> result = getSearchUniverseSearchDocumentNumberSeriesDataSet().stream();
        if (series != null) {
            result = result.filter(documentNumberSeries -> documentNumberSeries.getSeries() == series);
        }
        return result.collect(Collectors.toList());
    }

    @RequestMapping(value = "/search/view/{oid}")
    public String processSearchToViewAction(@PathVariable("oid") DocumentNumberSeries documentNumberSeries, Model model,
            RedirectAttributes redirectAttributes) {

        // CHANGE_ME Insert code here for processing viewAction
        // If you selected multiple exists you must choose which one to use below	 
        return redirect("/treasury/administration/document/managedocumentnumberseries/documentnumberseries/read" + "/"
                + documentNumberSeries.getExternalId(), model, redirectAttributes);
    }

//				
    @RequestMapping(value = READ_URI + "{oid}")
    public String read(@PathVariable("oid") DocumentNumberSeries documentNumberSeries, Model model) {
        setDocumentNumberSeries(documentNumberSeries, model);
        return "treasury/administration/document/managedocumentnumberseries/documentnumberseries/read";
    }

//
    @RequestMapping(value = DELETE_URI + "{oid}", method = RequestMethod.POST)
    public String delete(@PathVariable("oid") DocumentNumberSeries documentNumberSeries, Model model,
            RedirectAttributes redirectAttributes) {

        setDocumentNumberSeries(documentNumberSeries, model);
        try {
            //call the Atomic delete function
            deleteDocumentNumberSeries(documentNumberSeries);

            addInfoMessage(BundleUtil.getString(Constants.BUNDLE, "label.success.delete"), model);
            return redirect("/treasury/administration/document/managedocumentnumberseries/documentnumberseries/", model,
                    redirectAttributes);

        } catch (DomainException ex) {
            //Add error messages to the list
            addErrorMessage(BundleUtil.getString(Constants.BUNDLE, "label.error.delete") + ex.getLocalizedMessage(), model);

        } catch (Exception ex) {
            //Add error messages to the list
            addErrorMessage(BundleUtil.getString(Constants.BUNDLE, "label.error.delete") + ex.getLocalizedMessage(), model);
        }

        //The default mapping is the same Read View
        return redirect("/treasury/administration/document/managedocumentnumberseries/documentnumberseries/read/"
                + getDocumentNumberSeries(model).getExternalId(), model, redirectAttributes);
    }

//				
    @RequestMapping(value = CREATE_URI, method = RequestMethod.GET)
    public String create(Model model) {
        model.addAttribute("DocumentNumberSeries_series_options", org.fenixedu.treasury.domain.document.Series.readAll());
        model.addAttribute("DocumentNumberSeries_finantialDocumentType_options",
                org.fenixedu.treasury.domain.document.FinantialDocumentType.findAll().collect(Collectors.toList()));
        return "treasury/administration/document/managedocumentnumberseries/documentnumberseries/create";
    }

//				
    @RequestMapping(value = CREATE_URI, method = RequestMethod.POST)
    public String create(@RequestParam(value = "counter", required = false) int counter, @RequestParam(value = "series",
            required = false) org.fenixedu.treasury.domain.document.Series series, @RequestParam(value = "finantialdocumenttype",
            required = false) org.fenixedu.treasury.domain.document.FinantialDocumentType finantialDocumentType, @RequestParam(
            value = "bennu", required = false) org.fenixedu.bennu.core.domain.Bennu bennu, Model model,
            RedirectAttributes redirectAttributes) {
        /*
        *  Creation Logic
        */

        try {

            DocumentNumberSeries documentNumberSeries = createDocumentNumberSeries(counter, series, finantialDocumentType, bennu);

            //Success Validation
            //Add the bean to be used in the View
            model.addAttribute("documentNumberSeries", documentNumberSeries);
            return redirect("/treasury/administration/document/managedocumentnumberseries/documentnumberseries/read/"
                    + getDocumentNumberSeries(model).getExternalId(), model, redirectAttributes);

        } catch (DomainException de) {

            // @formatter: off
            /*
             * If there is any error in validation 
             *
             * Add a error / warning message
             * 
             * addErrorMessage(BundleUtil.getString(Constants.BUNDLE, "label.error.create") + de.getLocalizedMessage(),model);
             * addWarningMessage(" Warning creating due to "+ ex.getLocalizedMessage(),model); */
            // @formatter: on

            addErrorMessage(BundleUtil.getString(Constants.BUNDLE, "label.error.create") + de.getLocalizedMessage(), model);
            return create(model);

        } catch (Exception de) {
            addErrorMessage(BundleUtil.getString(Constants.BUNDLE, "label.error.create") + de.getLocalizedMessage(), model);
            return create(model);
        }
    }

    @Atomic
    public DocumentNumberSeries createDocumentNumberSeries(int counter, org.fenixedu.treasury.domain.document.Series series,
            org.fenixedu.treasury.domain.document.FinantialDocumentType finantialDocumentType,
            org.fenixedu.bennu.core.domain.Bennu bennu) {

        // @formatter: off

        /*
         * Modify the creation code here if you do not want to create
         * the object with the default constructor and use the setter
         * for each field
         * 
         */

        // CHANGE_ME It's RECOMMENDED to use "Create service" in DomainObject
        //DocumentNumberSeries documentNumberSeries = documentNumberSeries.create(fields_to_create);

        //Instead, use individual SETTERS and validate "CheckRules" in the end
        // @formatter: on

        DocumentNumberSeries documentNumberSeries = DocumentNumberSeries.create(finantialDocumentType, series);
        // ACFSILVA documentNumberSeries.setCounter(counter);

        return documentNumberSeries;
    }

//				
    @RequestMapping(value = UPDATE_URI + "{oid}", method = RequestMethod.GET)
    public String update(@PathVariable("oid") DocumentNumberSeries documentNumberSeries, Model model) {
        model.addAttribute("DocumentNumberSeries_series_options", org.fenixedu.treasury.domain.document.Series.readAll()); // CHANGE_ME - MUST DEFINE RELATION
        model.addAttribute("DocumentNumberSeries_finantialDocumentType_options",
                org.fenixedu.treasury.domain.document.FinantialDocumentType.findAll().collect(Collectors.toList())); // CHANGE_ME - MUST DEFINE RELATION
        setDocumentNumberSeries(documentNumberSeries, model);
        return "treasury/administration/document/managedocumentnumberseries/documentnumberseries/update";
    }

//				
    @RequestMapping(value = UPDATE_URI + "{oid}", method = RequestMethod.POST)
    public String update(
            @PathVariable("oid") DocumentNumberSeries documentNumberSeries,
            @RequestParam(value = "counter", required = false) int counter,
            @RequestParam(value = "series", required = false) org.fenixedu.treasury.domain.document.Series series,
            @RequestParam(value = "finantialdocumenttype", required = false) org.fenixedu.treasury.domain.document.FinantialDocumentType finantialDocumentType,
            @RequestParam(value = "bennu", required = false) org.fenixedu.bennu.core.domain.Bennu bennu, Model model,
            RedirectAttributes redirectAttributes) {

        setDocumentNumberSeries(documentNumberSeries, model);

        try {
            /*
            *  UpdateLogic here
            */

            updateDocumentNumberSeries(counter, series, finantialDocumentType, bennu, model);

            /*Succes Update */

            return redirect("/treasury/administration/document/managedocumentnumberseries/documentnumberseries/read/"
                    + getDocumentNumberSeries(model).getExternalId(), model, redirectAttributes);

        } catch (DomainException de) {
            // @formatter: off

            /*
            * If there is any error in validation 
            *
            * Add a error / warning message
            * 
            * addErrorMessage(BundleUtil.getString(Constants.BUNDLE, "label.error.update") + de.getLocalizedMessage(),model);
            * addWarningMessage(" Warning updating due to " + de.getLocalizedMessage(),model);
            */
            // @formatter: on

            addErrorMessage(BundleUtil.getString(Constants.BUNDLE, "label.error.update") + de.getLocalizedMessage(), model);
            return update(documentNumberSeries, model);

        } catch (Exception de) {
            addErrorMessage(BundleUtil.getString(Constants.BUNDLE, "label.error.update") + de.getLocalizedMessage(), model);
            return update(documentNumberSeries, model);

        }
    }

    @Atomic
    public void updateDocumentNumberSeries(int counter, org.fenixedu.treasury.domain.document.Series series,
            org.fenixedu.treasury.domain.document.FinantialDocumentType finantialDocumentType,
            org.fenixedu.bennu.core.domain.Bennu bennu, Model model) {

        // @formatter: off				
        /*
         * Modify the update code here if you do not want to update
         * the object with the default setter for each field
         */

        // CHANGE_ME It's RECOMMENDED to use "Edit service" in DomainObject
        //getDocumentNumberSeries(model).edit(fields_to_edit);

        //Instead, use individual SETTERS and validate "CheckRules" in the end
        // @formatter: on

        getDocumentNumberSeries(model).setCounter(counter);
        getDocumentNumberSeries(model).setSeries(series);
        getDocumentNumberSeries(model).setFinantialDocumentType(finantialDocumentType);
        getDocumentNumberSeries(model).setBennu(bennu);
    }

}
