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
package org.fenixedu.treasury.ui.document.manageinvoice;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import org.fenixedu.bennu.core.i18n.BundleUtil;
import org.fenixedu.bennu.spring.portal.SpringFunctionality;
import org.fenixedu.treasury.domain.FinantialInstitution;
import org.fenixedu.treasury.domain.debt.DebtAccount;
import org.fenixedu.treasury.domain.document.DebitNote;
import org.fenixedu.treasury.domain.document.DocumentNumberSeries;
import org.fenixedu.treasury.domain.document.FinantialDocumentStateType;
import org.fenixedu.treasury.domain.document.FinantialDocumentType;
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

//@Component("org.fenixedu.treasury.ui.document.manageInvoice") <-- Use for duplicate controller name disambiguation
@SpringFunctionality(app = TreasuryController.class, title = "label.title.document.manageInvoice.debitNote",
        accessGroup = "logged")
// CHANGE_ME accessGroup = "group1 | group2 | groupXPTO"
// or
// @BennuSpringController(value=TreasuryController.class)
@RequestMapping(DebitNoteController.CONTROLLER_URL)
public class DebitNoteController extends TreasuryBaseController {
    public static final String CONTROLLER_URL = "/treasury/document/manageinvoice/debitnote";
    private static final String SEARCH_URI = "/";
    public static final String SEARCH_URL = CONTROLLER_URL + SEARCH_URI;
    private static final String UPDATE_URI = "/update/";
    public static final String UPDATE_URL = CONTROLLER_URL + UPDATE_URI;
    private static final String CREATE_URI = "/create";
    public static final String CREATE_URL = CONTROLLER_URL + CREATE_URI;
    private static final String READ_URI = "/read/";
    public static final String READ_URL = CONTROLLER_URL + READ_URI;

    //

    @RequestMapping
    public String home(Model model) {
        // this is the default behaviour, for handling in a Spring Functionality
        return "forward:" + SEARCH_URL;
    }

    private DebitNote getDebitNote(Model model) {
        return (DebitNote) model.asMap().get("debitNote");
    }

    private void setDebitNote(DebitNote debitNote, Model model) {

        model.addAttribute("debitNote", debitNote);
    }

    @Atomic
    public void deleteDebitNote(DebitNote debitNote) {
        // CHANGE_ME: Do the processing for deleting the debitNote
        // Do not catch any exception here

        debitNote.delete();
    }

    //
    @RequestMapping(value = READ_URI + "{oid}")
    public String read(@PathVariable("oid") DebitNote debitNote, Model model) {
        setDebitNote(debitNote, model);
        return "treasury/document/manageinvoice/debitnote/read";
    }

    //
    @RequestMapping(value = SEARCH_URI)
    public String search(
            @RequestParam(value = "payordebtaccount", required = false) org.fenixedu.treasury.domain.debt.DebtAccount payorDebtAccount,
            @RequestParam(value = "finantialdocumenttype", required = false) org.fenixedu.treasury.domain.document.FinantialDocumentType finantialDocumentType,
            @RequestParam(value = "debtaccount", required = false) org.fenixedu.treasury.domain.debt.DebtAccount debtAccount,
            @RequestParam(value = "documentnumberseries", required = false) org.fenixedu.treasury.domain.document.DocumentNumberSeries documentNumberSeries,
            @RequestParam(value = "currency", required = false) org.fenixedu.treasury.domain.Currency currency,
            @RequestParam(value = "documentnumber", required = false) java.lang.String documentNumber,
            @RequestParam(value = "documentdate", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") org.joda.time.LocalDate documentDate,
            @RequestParam(value = "documentduedate", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") org.joda.time.LocalDate documentDueDate,
            @RequestParam(value = "origindocumentnumber", required = false) java.lang.String originDocumentNumber, @RequestParam(
                    value = "state", required = false) org.fenixedu.treasury.domain.document.FinantialDocumentStateType state,
            Model model) {

        List<DebitNote> searchdebitnoteResultsDataSet =
                filterSearchDebitNote(payorDebtAccount, finantialDocumentType, debtAccount, documentNumberSeries, currency,
                        documentNumber, documentDate, documentDueDate, originDocumentNumber, state);

        // add the results dataSet to the model
        model.addAttribute("searchdebitnoteResultsDataSet", searchdebitnoteResultsDataSet);
        model.addAttribute("DebitNote_payorDebtAccount_options", new ArrayList<org.fenixedu.treasury.domain.debt.DebtAccount>()); // CHANGE_ME
                                                                                                                                  // -
                                                                                                                                  // MUST
                                                                                                                                  // DEFINE
                                                                                                                                  // RELATION
        model.addAttribute("DebitNote_payorDebtAccount_options",
                org.fenixedu.treasury.domain.debt.DebtAccount.findAll().collect(Collectors.toList()));
        model.addAttribute("DebitNote_finantialDocumentType_options", org.fenixedu.treasury.domain.document.FinantialDocumentType
                .findAll().collect(Collectors.toList()));
        model.addAttribute("DebitNote_debtAccount_options", new ArrayList<org.fenixedu.treasury.domain.debt.DebtAccount>()); // CHANGE_ME
                                                                                                                             // -
                                                                                                                             // MUST
                                                                                                                             // DEFINE
                                                                                                                             // RELATION
        model.addAttribute("DebitNote_debtAccount_options",
                org.fenixedu.treasury.domain.debt.DebtAccount.findAll().collect(Collectors.toList()));
        model.addAttribute("DebitNote_documentNumberSeries_options", org.fenixedu.treasury.domain.document.DocumentNumberSeries
                .findAll().collect(Collectors.toList()));
        model.addAttribute("DebitNote_currency_options",
                org.fenixedu.treasury.domain.Currency.findAll().collect(Collectors.toList()));
        model.addAttribute("stateValues", org.fenixedu.treasury.domain.document.FinantialDocumentStateType.values());
        return "treasury/document/manageinvoice/debitnote/search";
    }

    private List<DebitNote> getSearchUniverseSearchDebitNoteDataSet() {
        //
        // The initialization of the result list must be done here
        //
        //
        return DebitNote.findAll().collect(Collectors.toList());
    }

    private List<DebitNote> filterSearchDebitNote(org.fenixedu.treasury.domain.debt.DebtAccount payorDebtAccount,
            org.fenixedu.treasury.domain.document.FinantialDocumentType finantialDocumentType,
            org.fenixedu.treasury.domain.debt.DebtAccount debtAccount,
            org.fenixedu.treasury.domain.document.DocumentNumberSeries documentNumberSeries,
            org.fenixedu.treasury.domain.Currency currency, java.lang.String documentNumber,
            org.joda.time.LocalDate documentDate, org.joda.time.LocalDate documentDueDate, java.lang.String originDocumentNumber,
            org.fenixedu.treasury.domain.document.FinantialDocumentStateType state) {

        return getSearchUniverseSearchDebitNoteDataSet()
                .stream()
                .filter(debitNote -> payorDebtAccount == null || payorDebtAccount == debitNote.getPayorDebtAccount())
                .filter(debitNote -> finantialDocumentType == null
                        || finantialDocumentType == debitNote.getFinantialDocumentType())
                .filter(debitNote -> debtAccount == null || debtAccount == debitNote.getDebtAccount())
                .filter(debitNote -> documentNumberSeries == null || documentNumberSeries == debitNote.getDocumentNumberSeries())
                .filter(debitNote -> currency == null || currency == debitNote.getCurrency())
                .filter(debitNote -> documentNumber == null || documentNumber.length() == 0
                        || debitNote.getDocumentNumber() != null && debitNote.getDocumentNumber().length() > 0
                        && debitNote.getDocumentNumber().toLowerCase().contains(documentNumber.toLowerCase()))
                .filter(debitNote -> documentDate == null || documentDate.equals(debitNote.getDocumentDate()))
                .filter(debitNote -> documentDueDate == null || documentDueDate.equals(debitNote.getDocumentDueDate()))
                .filter(debitNote -> originDocumentNumber == null || originDocumentNumber.length() == 0
                        || debitNote.getOriginDocumentNumber() != null && debitNote.getOriginDocumentNumber().length() > 0
                        && debitNote.getOriginDocumentNumber().toLowerCase().contains(originDocumentNumber.toLowerCase()))
                .filter(debitNote -> state == null || state.equals(debitNote.getState())).collect(Collectors.toList());
    }

    @RequestMapping(value = "/search/view/{oid}")
    public String processSearchToViewAction(@PathVariable("oid") DebitNote debitNote, Model model,
            RedirectAttributes redirectAttributes) {

        // CHANGE_ME Insert code here for processing viewAction
        // If you selected multiple exists you must choose which one to use
        // below
        return redirect(READ_URL + debitNote.getExternalId(), model, redirectAttributes);
    }

    //
    @RequestMapping(value = CREATE_URI, method = RequestMethod.GET)
    public String create(
            @RequestParam(value = "documentnumberseries", required = false) DocumentNumberSeries documentNumberSeries,
            @RequestParam(value = "debtaccount", required = false) DebtAccount debtAccount, Model model,
            RedirectAttributes redirectAttributes) {

        FinantialInstitution finantialInstitution = null;
        if (documentNumberSeries == null && debtAccount == null) {
            addErrorMessage(BundleUtil.getString(Constants.BUNDLE,
                    "label.error.document.manageinvoice.finantialinstitution.mismatch.debtaccount.series"), model);
            return redirect(SEARCH_URL, model, redirectAttributes);
        }

        if (documentNumberSeries != null && debtAccount != null) {
            if (!documentNumberSeries.getSeries().equals(debtAccount.getFinantialInstitution())) {
                addErrorMessage(BundleUtil.getString(Constants.BUNDLE,
                        "label.error.document.manageinvoice.finantialinstitution.mismatch.debtaccount.series"), model);
                return redirect(SEARCH_URL, model, redirectAttributes);
            }
        }

        if (documentNumberSeries != null) {
            finantialInstitution = documentNumberSeries.getSeries().getFinantialInstitution();
        }
        if (debtAccount != null) {
            finantialInstitution = debtAccount.getFinantialInstitution();
        }

        model.addAttribute("DebitNote_payorDebtAccount_options",
                DebtAccount.find(finantialInstitution).collect(Collectors.toList()));

        if (debtAccount != null) {
            model.addAttribute("DebitNote_debtAccount_options", Collections.singleton(debtAccount));
        } else {
            model.addAttribute("DebitNote_debtAccount_options",
                    DebtAccount.find(finantialInstitution).collect(Collectors.toList()));
        }
        if (documentNumberSeries != null) {
            model.addAttribute("DebitNote_documentNumberSeries_options", Collections.singletonList(documentNumberSeries));

        } else {
            List<DocumentNumberSeries> availableSeries =
                    org.fenixedu.treasury.domain.document.DocumentNumberSeries.find(FinantialDocumentType.findForDebitNote(),
                            debtAccount.getFinantialInstitution()).collect(Collectors.toList());
            if (availableSeries.size() > 0) {
                model.addAttribute("DebitNote_documentNumberSeries_options", availableSeries);
            } else {
                addErrorMessage(BundleUtil.getString(Constants.BUNDLE,
                        "label.error.document.manageinvoice.finantialinstitution.no.available.series.found"), model);
                return redirect(SEARCH_URL, model, redirectAttributes);
            }
        }
        return "treasury/document/manageinvoice/debitnote/create";
    }

    //
    @RequestMapping(value = CREATE_URI, method = RequestMethod.POST)
    public String create(
            @RequestParam(value = "payordebtaccount", required = false) org.fenixedu.treasury.domain.debt.DebtAccount payorDebtAccount,
            @RequestParam(value = "debtaccount") org.fenixedu.treasury.domain.debt.DebtAccount debtAccount,
            @RequestParam(value = "documentnumberseries") org.fenixedu.treasury.domain.document.DocumentNumberSeries documentNumberSeries,
            @RequestParam(value = "documentdate") @DateTimeFormat(pattern = "yyyy-MM-dd") org.joda.time.LocalDate documentDate,
            @RequestParam(value = "documentduedate", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") org.joda.time.LocalDate documentDueDate,
            @RequestParam(value = "origindocumentnumber", required = false) java.lang.String originDocumentNumber, Model model,
            RedirectAttributes redirectAttributes) {
        /*
         * Creation Logic
         */

        try {

            DebitNote debitNote =
                    createDebitNote(payorDebtAccount, debtAccount, documentNumberSeries, documentDate, documentDueDate,
                            originDocumentNumber);

            // Success Validation
            // Add the bean to be used in the View
            model.addAttribute("debitNote", debitNote);
            return redirect(READ_URL + getDebitNote(model).getExternalId(), model, redirectAttributes);
        } catch (Exception de) {

            // @formatter: off
            /*
             * If there is any error in validation
             * 
             * Add a error / warning message
             * 
             * addErrorMessage(BundleUtil.getString(Constants.BUNDLE, "label.error.create") +
             * de.getLocalizedMessage(),model);
             * addWarningMessage(" Warning creating due to "+
             * ex.getLocalizedMessage(),model);
             */
            // @formatter: on

            addErrorMessage(BundleUtil.getString(Constants.BUNDLE, "label.error.create") + de.getLocalizedMessage(), model);
            return create(documentNumberSeries, debtAccount, model, redirectAttributes);
        }
    }

    @Atomic
    public DebitNote createDebitNote(org.fenixedu.treasury.domain.debt.DebtAccount payorDebtAccount,
            org.fenixedu.treasury.domain.debt.DebtAccount debtAccount,
            org.fenixedu.treasury.domain.document.DocumentNumberSeries documentNumberSeries,
            org.joda.time.LocalDate documentDate, org.joda.time.LocalDate documentDueDate, java.lang.String originDocumentNumber) {

        // @formatter: off

        /*
         * Modify the creation code here if you do not want to create the object
         * with the default constructor and use the setter for each field
         */

        // CHANGE_ME It's RECOMMENDED to use "Create service" in DomainObject
        // DebitNote debitNote = debitNote.create(fields_to_create);

        // Instead, use individual SETTERS and validate "CheckRules" in the end
        // @formatter: on

        if (payorDebtAccount == null) {
            payorDebtAccount = debtAccount;
        }

        DebitNote debitNote =
                DebitNote.create(debtAccount, payorDebtAccount, documentNumberSeries, documentDate.toDateTimeAtCurrentTime(),
                        documentDueDate, originDocumentNumber);

        return debitNote;
    }

    //
    @RequestMapping(value = UPDATE_URI + "{oid}", method = RequestMethod.GET)
    public String update(@PathVariable("oid") DebitNote debitNote, Model model) {

        model.addAttribute("DebitNote_payorDebtAccount_options", debitNote.getDebtAccount().getFinantialInstitution()
                .getDebtAccountsSet()); //

        model.addAttribute("stateValues", org.fenixedu.treasury.domain.document.FinantialDocumentStateType.values());
        setDebitNote(debitNote, model);
        return "treasury/document/manageinvoice/debitnote/update";
    }

    //
    @RequestMapping(value = UPDATE_URI + "{oid}", method = RequestMethod.POST)
    public String update(
            @PathVariable("oid") DebitNote debitNote,
            @RequestParam(value = "payordebtaccount", required = false) org.fenixedu.treasury.domain.debt.DebtAccount payorDebtAccount,
            @RequestParam(value = "documentduedate", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") org.joda.time.LocalDate documentDueDate,
            @RequestParam(value = "origindocumentnumber", required = false) java.lang.String originDocumentNumber, @RequestParam(
                    value = "state", required = false) FinantialDocumentStateType state, Model model,
            RedirectAttributes redirectAttributes) {

        setDebitNote(debitNote, model);

        try {
            /*
             * UpdateLogic here
             */

            updateDebitNote(payorDebtAccount, documentDueDate, originDocumentNumber, state, model);

            /* Succes Update */

            return redirect(READ_URL + getDebitNote(model).getExternalId(), model, redirectAttributes);
        } catch (Exception de) {
            // @formatter: off

            /*
             * If there is any error in validation
             * 
             * Add a error / warning message
             * 
             * addErrorMessage(BundleUtil.getString(Constants.BUNDLE, "label.error.update") +
             * de.getLocalizedMessage(),model);
             * addWarningMessage(" Warning updating due to " +
             * de.getLocalizedMessage(),model);
             */
            // @formatter: on

            addErrorMessage(BundleUtil.getString(Constants.BUNDLE, "label.error.update") + de.getLocalizedMessage(), model);
            return update(debitNote, model);

        }
    }

    @Atomic
    public void updateDebitNote(org.fenixedu.treasury.domain.debt.DebtAccount payorDebtAccount,
            org.joda.time.LocalDate documentDueDate, java.lang.String originDocumentNumber, FinantialDocumentStateType state,
            Model model) {

        // @formatter: off
        /*
         * Modify the update code here if you do not want to update the object
         * with the default setter for each field
         */

        // CHANGE_ME It's RECOMMENDED to use "Edit service" in DomainObject
        // getDebitNote(model).edit(fields_to_edit);

        // Instead, use individual SETTERS and validate "CheckRules" in the end
        // @formatter: on
        DebitNote note = getDebitNote(model);
        note.edit(payorDebtAccount, documentDueDate, originDocumentNumber);
    }

    //
    // This is the EventaddEntry Method for Screen read
    //
    @RequestMapping(value = "/read/{oid}/addentry")
    public String processReadToAddEntry(@PathVariable("oid") DebitNote debitNote, Model model,
            RedirectAttributes redirectAttributes) {
        setDebitNote(debitNote, model);
//
        /* Put here the logic for processing Event addEntry     */
        //doSomething();

        // Now choose what is the Exit Screen    
        return redirect(DebitEntryController.CREATE_URL + getDebitNote(model).getDebtAccount().getExternalId() + "?debitNote="
                + debitNote.getExternalId(), model, redirectAttributes);
    }

    //
    // This is the EventcloseDebitNote Method for Screen read
    //
    @RequestMapping(value = "/read/{oid}/closedebitnote", method = RequestMethod.POST)
    public String processReadToCloseDebitNote(@PathVariable("oid") DebitNote debitNote, Model model,
            RedirectAttributes redirectAttributes) {
        setDebitNote(debitNote, model);

        try {
            debitNote.changeState(FinantialDocumentStateType.CLOSED);
            addInfoMessage(
                    BundleUtil.getString(Constants.BUNDLE, "label.document.manageinvoice.DebitNote.document.closed.sucess"),
                    model);
        } catch (Exception ex) {
            addErrorMessage(ex.getLocalizedMessage(), model);
        }

        // Now choose what is the Exit Screen    
        return redirect("/treasury/document/manageinvoice/debitnote/read/" + getDebitNote(model).getExternalId(), model,
                redirectAttributes);
    }

    //
    // This is the EventanullDebitNote Method for Screen read
    //
    @RequestMapping(value = "/read/{oid}/anulldebitnote", method = RequestMethod.POST)
    public String processReadToAnullDebitNote(@PathVariable("oid") DebitNote debitNote, Model model,
            RedirectAttributes redirectAttributes) {
        setDebitNote(debitNote, model);
//
        try {
            debitNote.changeState(FinantialDocumentStateType.ANNULED);
            addInfoMessage(
                    BundleUtil.getString(Constants.BUNDLE, "label.document.manageinvoice.DebitNote.document.anulled.sucess"),
                    model);
        } catch (Exception ex) {
            addErrorMessage(ex.getLocalizedMessage(), model);
        }

        // Now choose what is the Exit Screen    
        return redirect("/treasury/document/manageinvoice/debitnote/read/" + getDebitNote(model).getExternalId(), model,
                redirectAttributes);
    }

    @RequestMapping(value = "/read/{oid}/addpendingentries")
    public String processReadToAddPendingEntries(@PathVariable("oid") DebitNote debitNote, Model model,
            RedirectAttributes redirectAttributes) {
        setDebitNote(debitNote, model);
//
        /* Put here the logic for processing Event addPendingEntries    */
        //doSomething();

        // Now choose what is the Exit Screen    
        return redirect(DebitEntryController.SEARCHPENDINGENTRIES_URL + "?debitNote=" + debitNote.getExternalId(), model,
                redirectAttributes);
    }
}
