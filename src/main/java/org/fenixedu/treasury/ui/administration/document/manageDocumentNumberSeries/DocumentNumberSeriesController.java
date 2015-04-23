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
package org.fenixedu.treasury.ui.administration.document.manageDocumentNumberSeries;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.fenixedu.bennu.FenixeduTreasurySpringConfiguration;
import org.fenixedu.bennu.core.domain.exceptions.DomainException;
import org.fenixedu.bennu.core.i18n.BundleUtil;
import org.fenixedu.bennu.spring.portal.BennuSpringController;
import org.fenixedu.bennu.spring.portal.SpringFunctionality;
import org.fenixedu.treasury.domain.document.DocumentNumberSeries;
import org.fenixedu.treasury.ui.TreasuryBaseController;
import org.fenixedu.treasury.ui.TreasuryController;
import org.fenixedu.treasury.ui.administration.document.manageSeries.SeriesController;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import pt.ist.fenixframework.Atomic;

//@Component("org.fenixedu.treasury.ui.administration.document.manageDocumentNumberSeries") <-- Use for duplicate controller name disambiguation
//@SpringFunctionality(app = TreasuryController.class, title = "label.title.administration.document.manageDocumentNumberSeries",
//        accessGroup = "logged")
// CHANGE_ME accessGroup = "group1 | group2 | groupXPTO"
//or
@BennuSpringController(value=SeriesController.class) 
@RequestMapping("/treasury/administration/document/managedocumentnumberseries/documentnumberseries")
public class DocumentNumberSeriesController extends TreasuryBaseController {

//

    @RequestMapping
    public String home(Model model) {
        //this is the default behaviour, for handling in a Spring Functionality
        return "forward:/treasury/administration/document/managedocumentnumberseries/documentnumberseries/";
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
    @RequestMapping(value = "/")
    public String search(@RequestParam(value = "counter", required = false) int counter, Model model) {
        List<DocumentNumberSeries> searchdocumentnumberseriesResultsDataSet = filterSearchDocumentNumberSeries(counter);

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

    private List<DocumentNumberSeries> filterSearchDocumentNumberSeries(int counter) {

        return getSearchUniverseSearchDocumentNumberSeriesDataSet().stream()
                .filter(documentNumberSeries -> documentNumberSeries.getCounter() == counter).collect(Collectors.toList());
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
    @RequestMapping(value = "/read/{oid}")
    public String read(@PathVariable("oid") DocumentNumberSeries documentNumberSeries, Model model) {
        setDocumentNumberSeries(documentNumberSeries, model);
        return "treasury/administration/document/managedocumentnumberseries/documentnumberseries/read";
    }

//
    @RequestMapping(value = "/delete/{oid}", method = RequestMethod.POST)
    public String delete(@PathVariable("oid") DocumentNumberSeries documentNumberSeries, Model model,
            RedirectAttributes redirectAttributes) {

        setDocumentNumberSeries(documentNumberSeries, model);
        try {
            //call the Atomic delete function
            deleteDocumentNumberSeries(documentNumberSeries);

            addInfoMessage(BundleUtil.getString(FenixeduTreasurySpringConfiguration.BUNDLE, "label.success.delete"), model);
            return redirect("/treasury/administration/document/managedocumentnumberseries/documentnumberseries/", model,
                    redirectAttributes);

        } catch (DomainException ex) {
            //Add error messages to the list
            addErrorMessage(BundleUtil.getString(FenixeduTreasurySpringConfiguration.BUNDLE, "label.error.delete")+ ex.getLocalizedMessage(), model);

        } catch (Exception ex) {
            //Add error messages to the list
            addErrorMessage(BundleUtil.getString(FenixeduTreasurySpringConfiguration.BUNDLE, "label.error.delete") + ex.getLocalizedMessage(), model);
        }

        //The default mapping is the same Read View
        return redirect("treasury/administration/document/managedocumentnumberseries/documentnumberseries/read/"
                + getDocumentNumberSeries(model).getExternalId(), model, redirectAttributes);
    }

//				
    @RequestMapping(value = "/create", method = RequestMethod.GET)
    public String create(Model model) {
        model.addAttribute("DocumentNumberSeries_series_options", new ArrayList<org.fenixedu.treasury.domain.document.Series>()); // CHANGE_ME - MUST DEFINE RELATION
        //model.addAttribute("DocumentNumberSeries_series_options", org.fenixedu.treasury.domain.document.Series.findAll()); // CHANGE_ME - MUST DEFINE RELATION
        model.addAttribute("DocumentNumberSeries_finantialDocumentType_options",
                new ArrayList<org.fenixedu.treasury.domain.document.FinantialDocumentType>()); // CHANGE_ME - MUST DEFINE RELATION
        //model.addAttribute("DocumentNumberSeries_finantialDocumentType_options", org.fenixedu.treasury.domain.document.FinantialDocumentType.findAll()); // CHANGE_ME - MUST DEFINE RELATION
        model.addAttribute("DocumentNumberSeries_bennu_options", new ArrayList<org.fenixedu.bennu.core.domain.Bennu>()); // CHANGE_ME - MUST DEFINE RELATION
        //model.addAttribute("DocumentNumberSeries_bennu_options", org.fenixedu.bennu.core.domain.Bennu.findAll()); // CHANGE_ME - MUST DEFINE RELATION
        return "treasury/administration/document/managedocumentnumberseries/documentnumberseries/create";
    }

//				
    @RequestMapping(value = "/create", method = RequestMethod.POST)
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
             * addErrorMessage(BundleUtil.getString(FenixeduTreasurySpringConfiguration.BUNDLE, "label.error.create") + de.getLocalizedMessage(),model);
             * addWarningMessage(" Warning creating due to "+ ex.getLocalizedMessage(),model); */
            // @formatter: on

            addErrorMessage(BundleUtil.getString(FenixeduTreasurySpringConfiguration.BUNDLE, "label.error.create") + de.getLocalizedMessage(), model);
            return create(model);

        } catch (Exception de) {
            addErrorMessage(BundleUtil.getString(FenixeduTreasurySpringConfiguration.BUNDLE, "label.error.create") + de.getLocalizedMessage(), model);
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
    @RequestMapping(value = "/update/{oid}", method = RequestMethod.GET)
    public String update(@PathVariable("oid") DocumentNumberSeries documentNumberSeries, Model model) {
        model.addAttribute("DocumentNumberSeries_series_options", new ArrayList<org.fenixedu.treasury.domain.document.Series>()); // CHANGE_ME - MUST DEFINE RELATION
        //model.addAttribute("DocumentNumberSeries_series_options", org.fenixedu.treasury.domain.document.Series.findAll()); // CHANGE_ME - MUST DEFINE RELATION
        model.addAttribute("DocumentNumberSeries_finantialDocumentType_options",
                new ArrayList<org.fenixedu.treasury.domain.document.FinantialDocumentType>()); // CHANGE_ME - MUST DEFINE RELATION
        //model.addAttribute("DocumentNumberSeries_finantialDocumentType_options", org.fenixedu.treasury.domain.document.FinantialDocumentType.findAll()); // CHANGE_ME - MUST DEFINE RELATION
        model.addAttribute("DocumentNumberSeries_bennu_options", new ArrayList<org.fenixedu.bennu.core.domain.Bennu>()); // CHANGE_ME - MUST DEFINE RELATION
        //model.addAttribute("DocumentNumberSeries_bennu_options", org.fenixedu.bennu.core.domain.Bennu.findAll()); // CHANGE_ME - MUST DEFINE RELATION
        setDocumentNumberSeries(documentNumberSeries, model);
        return "treasury/administration/document/managedocumentnumberseries/documentnumberseries/update";
    }

//				
    @RequestMapping(value = "/update/{oid}", method = RequestMethod.POST)
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
            * addErrorMessage(" Error updating due to " + de.getLocalizedMessage(),model);
            * addWarningMessage(" Warning updating due to " + de.getLocalizedMessage(),model);
            */
            // @formatter: on

            addErrorMessage(" Error updating due to " + de.getLocalizedMessage(), model);
            return update(documentNumberSeries, model);

        } catch (Exception de) {
            addErrorMessage(" Error updating due to " + de.getLocalizedMessage(), model);
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
