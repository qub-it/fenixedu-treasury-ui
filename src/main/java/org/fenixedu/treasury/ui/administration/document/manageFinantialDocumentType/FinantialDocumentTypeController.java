/**
 * This file was created by Quorum Born IT <http://www.qub-it.com/> and its 
 * copyright terms are bind to the legal agreement regulating the FenixEdu@ULisboa 
 * software development project between Quorum Born IT and ServiÃ§os Partilhados da
 * Universidade de Lisboa:
 *  - Copyright Â© 2015 Quorum Born IT (until any Go-Live phase)
 *  - Copyright Â© 2015 Universidade de Lisboa (after any Go-Live phase)
 *
 * Contributors: xpto@qub-it.com
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
package org.fenixedu.treasury.ui.administration.document.manageFinantialDocumentType;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.fenixedu.bennu.core.domain.exceptions.DomainException;
import org.fenixedu.bennu.spring.portal.SpringFunctionality;
import org.fenixedu.treasury.domain.document.FinantialDocumentType;
import org.fenixedu.treasury.ui.TreasuryBaseController;
import org.fenixedu.treasury.ui.TreasuryController;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import pt.ist.fenixframework.Atomic;

//@Component("org.fenixedu.treasury.ui.administration.document.manageFinantialDocumentType") <-- Use for duplicate controller name disambiguation
@SpringFunctionality(app = TreasuryController.class, title = "label.title.administration.document.manageFinantialDocumentType",
        accessGroup = "anyone")
// CHANGE_ME accessGroup = "group1 | group2 | groupXPTO"
//or
//@BennuSpringController(value=TreasuryController.class) 
@RequestMapping("/treasury/administration/document/managefinantialdocumenttype/finantialdocumenttype")
public class FinantialDocumentTypeController extends TreasuryBaseController {

//

    @RequestMapping
    public String home(Model model) {
        //this is the default behaviour, for handling in a Spring Functionality
        return "forward:/treasury/administration/document/managefinantialdocumenttype/finantialdocumenttype/";
    }

    private FinantialDocumentType getFinantialDocumentType(Model model) {
        return (FinantialDocumentType) model.asMap().get("finantialDocumentType");
    }

    private void setFinantialDocumentType(FinantialDocumentType finantialDocumentType, Model model) {
        model.addAttribute("finantialDocumentType", finantialDocumentType);
    }

    @Atomic
    public void deleteFinantialDocumentType(FinantialDocumentType finantialDocumentType) {
        // CHANGE_ME: Do the processing for deleting the finantialDocumentType
        // Do not catch any exception here

        finantialDocumentType.delete();
    }

//				
    @RequestMapping(value = "/")
    public String search(
            @RequestParam(value = "type", required = false) org.fenixedu.treasury.domain.document.FinantialDocumentTypeEnum type,
            @RequestParam(value = "code", required = false) java.lang.String code,
            @RequestParam(value = "name", required = false) org.fenixedu.commons.i18n.LocalizedString name, @RequestParam(
                    value = "documentnumberseriesprefix", required = false) java.lang.String documentNumberSeriesPrefix,
            @RequestParam(value = "invoice", required = false) boolean invoice, Model model) {
        List<FinantialDocumentType> searchfinantialdocumenttypeResultsDataSet =
                filterSearchFinantialDocumentType(type, code, name, documentNumberSeriesPrefix, invoice);

        //add the results dataSet to the model
        model.addAttribute("searchfinantialdocumenttypeResultsDataSet", searchfinantialdocumenttypeResultsDataSet);
        model.addAttribute("typeValues", org.fenixedu.treasury.domain.document.FinantialDocumentTypeEnum.values());
        return "treasury/administration/document/managefinantialdocumenttype/finantialdocumenttype/search";
    }

    private List<FinantialDocumentType> getSearchUniverseSearchFinantialDocumentTypeDataSet() {
        //
        //The initialization of the result list must be done here
        //
        //
         return FinantialDocumentType.findAll().collect(Collectors.toList()); //CHANGE_ME
//        return new ArrayList<FinantialDocumentType>(FinantialDocumentType.readAll());
    }

    private List<FinantialDocumentType> filterSearchFinantialDocumentType(
            org.fenixedu.treasury.domain.document.FinantialDocumentTypeEnum type, java.lang.String code,
            org.fenixedu.commons.i18n.LocalizedString name, java.lang.String documentNumberSeriesPrefix, boolean invoice) {

        return getSearchUniverseSearchFinantialDocumentTypeDataSet()
                .stream()
                .filter(finantialDocumentType -> type == null || type.equals(finantialDocumentType.getType()))
                .filter(finantialDocumentType -> code == null || code.length() == 0 || finantialDocumentType.getCode() != null
                        && finantialDocumentType.getCode().length() > 0
                        && finantialDocumentType.getCode().toLowerCase().contains(code.toLowerCase()))
                .filter(finantialDocumentType -> name == null
                        || name.isEmpty()
                        || name.getLocales()
                                .stream()
                                .allMatch(
                                        locale -> finantialDocumentType.getName().getContent(locale) != null
                                                && finantialDocumentType.getName().getContent(locale).toLowerCase()
                                                        .contains(name.getContent(locale).toLowerCase())))
                .filter(finantialDocumentType -> documentNumberSeriesPrefix == null
                        || documentNumberSeriesPrefix.length() == 0
                        || finantialDocumentType.getDocumentNumberSeriesPrefix() != null
                        && finantialDocumentType.getDocumentNumberSeriesPrefix().length() > 0
                        && finantialDocumentType.getDocumentNumberSeriesPrefix().toLowerCase()
                                .contains(documentNumberSeriesPrefix.toLowerCase()))
                .filter(finantialDocumentType -> finantialDocumentType.getInvoice() == true).collect(Collectors.toList());
    }

    @RequestMapping(value = "/search/view/{oid}")
    public String processSearchToViewAction(@PathVariable("oid") FinantialDocumentType finantialDocumentType, Model model,
            RedirectAttributes redirectAttributes) {

        // CHANGE_ME Insert code here for processing viewAction
        // If you selected multiple exists you must choose which one to use below	 
        return redirect("/treasury/administration/document/managefinantialdocumenttype/finantialdocumenttype/read" + "/"
                + finantialDocumentType.getExternalId(), model, redirectAttributes);
    }

//				
    @RequestMapping(value = "/read/{oid}")
    public String read(@PathVariable("oid") FinantialDocumentType finantialDocumentType, Model model) {
        setFinantialDocumentType(finantialDocumentType, model);
        return "treasury/administration/document/managefinantialdocumenttype/finantialdocumenttype/read";
    }

//
    @RequestMapping(value = "/delete/{oid}", method = RequestMethod.POST)
    public String delete(@PathVariable("oid") FinantialDocumentType finantialDocumentType, Model model,
            RedirectAttributes redirectAttributes) {

        setFinantialDocumentType(finantialDocumentType, model);
        try {
            //call the Atomic delete function
            deleteFinantialDocumentType(finantialDocumentType);

            addInfoMessage("Sucess deleting FinantialDocumentType ...", model);
            return redirect("/treasury/administration/document/managefinantialdocumenttype/finantialdocumenttype/", model,
                    redirectAttributes);

        } catch (DomainException ex) {
            //Add error messages to the list
            addErrorMessage("Error deleting the FinantialDocumentType due to " + ex.getLocalizedMessage(), model);

        } catch (Exception ex) {
            //Add error messages to the list
            addErrorMessage("Error deleting the FinantialDocumentType due to " + ex.getLocalizedMessage(), model);
        }

        //The default mapping is the same Read View
        return redirect("treasury/administration/document/managefinantialdocumenttype/finantialdocumenttype/read/"
                + getFinantialDocumentType(model).getExternalId(), model, redirectAttributes);
    }

//				
    @RequestMapping(value = "/create", method = RequestMethod.GET)
    public String create(Model model) {
        model.addAttribute("typeValues", org.fenixedu.treasury.domain.document.FinantialDocumentTypeEnum.values());
        model.addAttribute("FinantialDocumentType_bennu_options", new ArrayList<org.fenixedu.bennu.core.domain.Bennu>()); // CHANGE_ME - MUST DEFINE RELATION
        //model.addAttribute("FinantialDocumentType_bennu_options", org.fenixedu.bennu.core.domain.Bennu.findAll()); // CHANGE_ME - MUST DEFINE RELATION
        return "treasury/administration/document/managefinantialdocumenttype/finantialdocumenttype/create";
    }

//				
    @RequestMapping(value = "/create", method = RequestMethod.POST)
    public String create(
            @RequestParam(value = "type", required = false) org.fenixedu.treasury.domain.document.FinantialDocumentTypeEnum type,
            @RequestParam(value = "code", required = false) java.lang.String code,
            @RequestParam(value = "name", required = false) org.fenixedu.commons.i18n.LocalizedString name, @RequestParam(
                    value = "documentnumberseriesprefix", required = false) java.lang.String documentNumberSeriesPrefix,
            @RequestParam(value = "invoice", required = false) boolean invoice,
            @RequestParam(value = "bennu", required = false) org.fenixedu.bennu.core.domain.Bennu bennu, Model model,
            RedirectAttributes redirectAttributes) {
        /*
        *  Creation Logic
        */

        try {

            FinantialDocumentType finantialDocumentType =
                    createFinantialDocumentType(type, code, name, documentNumberSeriesPrefix, invoice, bennu);

            //Success Validation
            //Add the bean to be used in the View
            model.addAttribute("finantialDocumentType", finantialDocumentType);
            return redirect("/treasury/administration/document/managefinantialdocumenttype/finantialdocumenttype/read/"
                    + getFinantialDocumentType(model).getExternalId(), model, redirectAttributes);

        } catch (DomainException de) {

            // @formatter: off
            /*
             * If there is any error in validation 
             *
             * Add a error / warning message
             * 
             * addErrorMessage(" Error creating due to " + de.getLocalizedMessage(),model);
             * addWarningMessage(" Warning creating due to "+ ex.getLocalizedMessage(),model); */
            // @formatter: on

            addErrorMessage(" Error creating due to " + de.getLocalizedMessage(), model);
            return create(model);

        } catch (Exception de) {
            // ACFSILVA
            addErrorMessage(" Error creating due to " + de.getLocalizedMessage(), model);
            return create(model);
        }
    }

    @Atomic
    public FinantialDocumentType createFinantialDocumentType(
            org.fenixedu.treasury.domain.document.FinantialDocumentTypeEnum type, java.lang.String code,
            org.fenixedu.commons.i18n.LocalizedString name, java.lang.String documentNumberSeriesPrefix, boolean invoice,
            org.fenixedu.bennu.core.domain.Bennu bennu) {

        // @formatter: off

        /*
         * Modify the creation code here if you do not want to create
         * the object with the default constructor and use the setter
         * for each field
         * 
         */

        // CHANGE_ME It's RECOMMENDED to use "Create service" in DomainObject
        //FinantialDocumentType finantialDocumentType = finantialDocumentType.create(fields_to_create);

        //Instead, use individual SETTERS and validate "CheckRules" in the end
        // @formatter: on

        FinantialDocumentType finantialDocumentType = null;
        switch (type) {
        case CREDIT_NOTE:
            finantialDocumentType = FinantialDocumentType.createForCreditNote(code, name, documentNumberSeriesPrefix, invoice);
            break;
        case REIMBURSEMENT_NOTE:
            finantialDocumentType =
                    FinantialDocumentType.createForReimbursementNote(code, name, documentNumberSeriesPrefix, invoice);
            break;
        case DEBIT_NOTE:
            finantialDocumentType = FinantialDocumentType.createForDebitNote(code, name, documentNumberSeriesPrefix, invoice);
            break;
        case SETTLEMENT_NOTE:
            finantialDocumentType =
                    FinantialDocumentType.createForSettlementNote(code, name, documentNumberSeriesPrefix, invoice);
            break;
        }
        return finantialDocumentType;
    }

//				
    @RequestMapping(value = "/update/{oid}", method = RequestMethod.GET)
    public String update(@PathVariable("oid") FinantialDocumentType finantialDocumentType, Model model) {
        model.addAttribute("typeValues", org.fenixedu.treasury.domain.document.FinantialDocumentTypeEnum.values());
        model.addAttribute("FinantialDocumentType_bennu_options", new ArrayList<org.fenixedu.bennu.core.domain.Bennu>()); // CHANGE_ME - MUST DEFINE RELATION
        //model.addAttribute("FinantialDocumentType_bennu_options", org.fenixedu.bennu.core.domain.Bennu.findAll()); // CHANGE_ME - MUST DEFINE RELATION
        setFinantialDocumentType(finantialDocumentType, model);
        return "treasury/administration/document/managefinantialdocumenttype/finantialdocumenttype/update";
    }

//				
    @RequestMapping(value = "/update/{oid}", method = RequestMethod.POST)
    public String update(@PathVariable("oid") FinantialDocumentType finantialDocumentType, @RequestParam(value = "type",
            required = false) org.fenixedu.treasury.domain.document.FinantialDocumentTypeEnum type, @RequestParam(value = "code",
            required = false) java.lang.String code,
            @RequestParam(value = "name", required = false) org.fenixedu.commons.i18n.LocalizedString name, @RequestParam(
                    value = "documentnumberseriesprefix", required = false) java.lang.String documentNumberSeriesPrefix,
            @RequestParam(value = "invoice", required = false) boolean invoice,
            @RequestParam(value = "bennu", required = false) org.fenixedu.bennu.core.domain.Bennu bennu, Model model,
            RedirectAttributes redirectAttributes) {

        setFinantialDocumentType(finantialDocumentType, model);

        try {
            /*
            *  UpdateLogic here
            */

            updateFinantialDocumentType(type, code, name, documentNumberSeriesPrefix, invoice, bennu, model);

            /*Succes Update */

            return redirect("/treasury/administration/document/managefinantialdocumenttype/finantialdocumenttype/read/"
                    + getFinantialDocumentType(model).getExternalId(), model, redirectAttributes);

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
            return update(finantialDocumentType, model);

        } catch (Exception de) {
            addErrorMessage(" Error updating due to " + de.getLocalizedMessage(), model);
            return update(finantialDocumentType, model);

        }
    }

    @Atomic
    public void updateFinantialDocumentType(org.fenixedu.treasury.domain.document.FinantialDocumentTypeEnum type,
            java.lang.String code, org.fenixedu.commons.i18n.LocalizedString name, java.lang.String documentNumberSeriesPrefix,
            boolean invoice, org.fenixedu.bennu.core.domain.Bennu bennu, Model model) {

        // @formatter: off				
        /*
         * Modify the update code here if you do not want to update
         * the object with the default setter for each field
         */

        // CHANGE_ME It's RECOMMENDED to use "Edit service" in DomainObject
        //getFinantialDocumentType(model).edit(fields_to_edit);

        //Instead, use individual SETTERS and validate "CheckRules" in the end
        // @formatter: on

        getFinantialDocumentType(model).setType(type);
        getFinantialDocumentType(model).setCode(code);
        getFinantialDocumentType(model).setName(name);
        getFinantialDocumentType(model).setDocumentNumberSeriesPrefix(documentNumberSeriesPrefix);
        getFinantialDocumentType(model).setInvoice(invoice);
        getFinantialDocumentType(model).setBennu(bennu);
    }

}
