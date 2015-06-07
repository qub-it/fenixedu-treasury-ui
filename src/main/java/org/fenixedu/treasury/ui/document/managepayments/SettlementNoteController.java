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
package org.fenixedu.treasury.ui.document.managepayments;

import java.io.IOException;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletResponse;

import org.fenixedu.bennu.core.domain.exceptions.DomainException;
import org.fenixedu.bennu.core.i18n.BundleUtil;
import org.fenixedu.bennu.spring.portal.SpringFunctionality;
import org.fenixedu.commons.StringNormalizer;
import org.fenixedu.treasury.domain.Currency;
import org.fenixedu.treasury.domain.debt.DebtAccount;
import org.fenixedu.treasury.domain.document.DocumentNumberSeries;
import org.fenixedu.treasury.domain.document.FinantialDocument;
import org.fenixedu.treasury.domain.document.FinantialDocumentStateType;
import org.fenixedu.treasury.domain.document.FinantialDocumentType;
import org.fenixedu.treasury.domain.document.SettlementNote;
import org.fenixedu.treasury.dto.InterestRateBean;
import org.fenixedu.treasury.dto.SettlementNoteBean;
import org.fenixedu.treasury.dto.SettlementNoteBean.CreditEntryBean;
import org.fenixedu.treasury.dto.SettlementNoteBean.DebitEntryBean;
import org.fenixedu.treasury.dto.SettlementNoteBean.InterestEntryBean;
import org.fenixedu.treasury.services.integration.erp.ERPExporter;
import org.fenixedu.treasury.ui.TreasuryBaseController;
import org.fenixedu.treasury.ui.TreasuryController;
import org.fenixedu.treasury.ui.accounting.managecustomer.DebtAccountController;
import org.fenixedu.treasury.util.Constants;
import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import pt.ist.fenixframework.Atomic;

//@Component("org.fenixedu.treasury.ui.document.managePayments") <-- Use for duplicate controller name disambiguation
@SpringFunctionality(app = TreasuryController.class, title = "label.title.document.managePayments", accessGroup = "#managers")
@RequestMapping(SettlementNoteController.CONTROLLER_URL)
public class SettlementNoteController extends TreasuryBaseController {
    public static final String CONTROLLER_URL = "/treasury/document/managepayments/settlementnote";
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
    private static final String CHOOSE_INVOICE_ENTRIES_URI = "/chooseInvoiceEntries/";
    public static final String CHOOSE_INVOICE_ENTRIES_URL = CONTROLLER_URL + CHOOSE_INVOICE_ENTRIES_URI;
    private static final String CALCULATE_INTEREST_URI = "/calculateInterest/";
    public static final String CALCULATE_INTEREST_URL = CONTROLLER_URL + CALCULATE_INTEREST_URI;
    private static final String CREATE_DEBIT_NOTE_URI = "/createDebitNote/";
    public static final String CREATE_DEBIT_NOTE_URL = CONTROLLER_URL + CREATE_DEBIT_NOTE_URI;
    private static final String INSERT_PAYMENT_URI = "/insertpayment/";
    public static final String INSERT_PAYMENT_URL = CONTROLLER_URL + INSERT_PAYMENT_URI;
    private static final String SUMMARY_URI = "/summary/";
    public static final String SUMMARY_URL = CONTROLLER_URL + SUMMARY_URI;

    @RequestMapping
    public String home(Model model) {
        return "forward:" + SEARCH_URL;
    }

    private SettlementNoteBean getSettlementNoteBean(Model model) {
        return (SettlementNoteBean) model.asMap().get("settlementNoteBean");
    }

    private void setSettlementNoteBean(SettlementNoteBean bean, Model model) {
        model.addAttribute("settlementNoteBeanJson", getBeanJson(bean));
        model.addAttribute("settlementNoteBean", bean);
    }

    private SettlementNote getSettlementNote(Model model) {
        return (SettlementNote) model.asMap().get("settlementNote");
    }

    private void setSettlementNote(SettlementNote settlementNote, Model model) {
        model.addAttribute("settlementNote", settlementNote);
    }

    @Atomic
    public void deleteSettlementNote(SettlementNote settlementNote) {
        settlementNote.delete(true);
    }

    @RequestMapping(value = CHOOSE_INVOICE_ENTRIES_URI + "{debtAccountId}")
    public String chooseInvoiceEntries(@PathVariable(value = "debtAccountId") DebtAccount debtAccount, @RequestParam(
            value = "bean", required = false) SettlementNoteBean bean, Model model) {
        if (bean == null) {
            bean = new SettlementNoteBean(debtAccount);
        }
        setSettlementNoteBean(bean, model);
        return "treasury/document/managepayments/settlementnote/chooseInvoiceEntries";
    }

    @RequestMapping(value = CHOOSE_INVOICE_ENTRIES_URI, method = RequestMethod.POST)
    public String chooseInvoiceEntries(@RequestParam(value = "bean", required = true) SettlementNoteBean bean, Model model) {
        BigDecimal debitSum = BigDecimal.ZERO;
        BigDecimal creditSum = BigDecimal.ZERO;
        boolean error = false;

        for (int i = 0; i < bean.getDebitEntries().size(); i++) {
            DebitEntryBean debitEntryBean = bean.getDebitEntries().get(i);
            if (debitEntryBean.isIncluded()) {
                if (debitEntryBean.getDebtAmountWithVat().compareTo(BigDecimal.ZERO) == 0) {
                    debitEntryBean.setNotValid(true);
                    error = true;
                    addErrorMessage(
                            BundleUtil.getString(Constants.BUNDLE, "error.DebitEntry.debtAmount.equal.zero",
                                    Integer.toString(i + 1)), model);
                } else if (debitEntryBean.getDebtAmountWithVat().compareTo(debitEntryBean.getDebitEntry().getOpenAmount()) > 0) {
                    debitEntryBean.setNotValid(true);
                    error = true;
                    addErrorMessage(
                            BundleUtil.getString(Constants.BUNDLE, "error.DebitEntry.exceeded.openAmount",
                                    Integer.toString(i + 1)), model);
                } else {
                    debitEntryBean.setNotValid(false);
                }
                //Always perform the sum, in order to verify if creditSum is not higher than debitSum
                debitSum = debitSum.add(debitEntryBean.getDebtAmountWithVat());
            } else {
                debitEntryBean.setNotValid(false);
            }
        }
        for (CreditEntryBean creditEntryBean : bean.getCreditEntries()) {
            if (creditEntryBean.isIncluded()) {
                creditSum = creditSum.add(creditEntryBean.getCreditEntry().getOpenAmount());
            }
        }
        if (creditSum.compareTo(debitSum) > 0) {
            error = true;
            addErrorMessage(BundleUtil.getString(Constants.BUNDLE, "error.CreditEntry.negative.payment.value"), model);
        }
        if (debitSum.compareTo(BigDecimal.ZERO) == 0) {
            error = true;
            addErrorMessage(BundleUtil.getString(Constants.BUNDLE, "error.DebiEntry.no.debitEntries.selected"), model);
        }
        if (bean.getDate().isAfter(new LocalDate())) {
            error = true;
            addErrorMessage(BundleUtil.getString(Constants.BUNDLE, "error.SettlementNote.date.is.after"), model);
        }
        if (bean.getDocNumSeries() == null) {
            error = true;
            addErrorMessage(BundleUtil.getString(Constants.BUNDLE, "error.SettlementNote.need.documentSeries"), model);
        }
        if (error) {
            setSettlementNoteBean(bean, model);
            return "treasury/document/managepayments/settlementnote/chooseInvoiceEntries";
        }

        bean.setInterestEntries(new ArrayList<InterestEntryBean>());
        for (DebitEntryBean debitEntryBean : bean.getDebitEntries()) {
            if (debitEntryBean.isIncluded()) {
                InterestRateBean debitInterest = debitEntryBean.getDebitEntry().calculateInterestValue(bean.getDate());
                if (debitInterest.getInterestAmount().compareTo(BigDecimal.ZERO) != 0) {
                    bean.getInterestEntries().add(bean.new InterestEntryBean(debitEntryBean.getDebitEntry(), debitInterest));
                }
            }
        }
        setSettlementNoteBean(bean, model);
        return "treasury/document/managepayments/settlementnote/calculateInterest";
    }

    @RequestMapping(value = CALCULATE_INTEREST_URI, method = RequestMethod.POST)
    public String calculateInterest(@RequestParam(value = "bean", required = true) SettlementNoteBean bean, Model model) {
        for (DebitEntryBean debitEntryBean : bean.getDebitEntries()) {
            if (debitEntryBean.isIncluded() && debitEntryBean.getDebitEntry().getFinantialDocument() == null) {
                setSettlementNoteBean(bean, model);
                return "treasury/document/managepayments/settlementnote/createDebitNote";
            }
        }
        for (InterestEntryBean interestEntryBean : bean.getInterestEntries()) {
            if (interestEntryBean.isIncluded()) {
                setSettlementNoteBean(bean, model);
                return "treasury/document/managepayments/settlementnote/createDebitNote";
            }
        }
        //It is not necessary to create a debit note
        return createDebitNote(bean, model);
    }

    @RequestMapping(value = CREATE_DEBIT_NOTE_URI, method = RequestMethod.POST)
    public String createDebitNote(@RequestParam(value = "bean", required = true) SettlementNoteBean bean, Model model) {
        setSettlementNoteBean(bean, model);
        return "treasury/document/managepayments/settlementnote/insertPayment";
    }

    @RequestMapping(value = INSERT_PAYMENT_URI, method = RequestMethod.POST)
    public String insertPayment(@RequestParam(value = "bean", required = true) SettlementNoteBean bean, Model model) {
        BigDecimal debitSum = bean.getDebtAmountWithVat();
        BigDecimal paymentSum = bean.getPaymentAmount();
        if (debitSum.compareTo(paymentSum) != 0) {
            addErrorMessage(BundleUtil.getString(Constants.BUNDLE, "error.SettlementNote.no.match.payment.debit"), model);
            setSettlementNoteBean(bean, model);
            return "treasury/document/managepayments/settlementnote/insertPayment";
        }

        setSettlementNoteBean(bean, model);
        return "treasury/document/managepayments/settlementnote/summary";
    }

    @RequestMapping(value = SUMMARY_URI, method = RequestMethod.POST)
    public String summary(@RequestParam(value = "bean", required = true) SettlementNoteBean bean, Model model,
            RedirectAttributes redirectAttributes) {
        //TODOJN
        //Surround by try/catch block
        /////////////
        processSettlementNoteCreation(bean);
        ////////////

        addInfoMessage(BundleUtil.getString(Constants.BUNDLE, "label.SettlementNote.create.success"), model);
        return redirect(DebtAccountController.READ_URL + bean.getDebtAccount().getExternalId(), model, redirectAttributes);
    }

    @Atomic
    public void processSettlementNoteCreation(SettlementNoteBean bean) {
        SettlementNote settlementNote =
                SettlementNote.create(bean.getDebtAccount(), bean.getDocNumSeries(), bean.getDate().toDateTimeAtStartOfDay(),
                        bean.getOriginDocumentNumber());
        settlementNote.processSettlementNoteCreation(bean);
    }

    @RequestMapping(value = SEARCH_URI)
    public String search(
            @RequestParam(value = "debtaccount", required = false) DebtAccount debtAccount,
            @RequestParam(value = "documentnumberseries", required = false) DocumentNumberSeries documentNumberSeries,
            @RequestParam(value = "currency", required = false) Currency currency,
            @RequestParam(value = "documentnumber", required = false) String documentNumber,
            @RequestParam(value = "documentdate", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") DateTime documentDate,
            @RequestParam(value = "documentduedate", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") DateTime documentDueDate,
            @RequestParam(value = "origindocumentnumber", required = false) String originDocumentNumber, @RequestParam(
                    value = "state", required = false) FinantialDocumentStateType state, Model model) {
        List<SettlementNote> searchsettlementnoteResultsDataSet =
                filterSearchSettlementNote(debtAccount, documentNumberSeries, currency, documentNumber, documentDate,
                        documentDueDate, originDocumentNumber, state);

        // add the results dataSet to the model
        model.addAttribute("searchsettlementnoteResultsDataSet", searchsettlementnoteResultsDataSet);
        model.addAttribute("SettlementNote_finantialDocumentType_options",
                FinantialDocumentType.findAll().collect(Collectors.toList()));
        model.addAttribute("SettlementNote_debtAccount_options", DebtAccount.findAll().collect(Collectors.toList()));
        model.addAttribute(
                "SettlementNote_documentNumberSeries_options",
                org.fenixedu.treasury.domain.document.DocumentNumberSeries.findAll()
                        .filter(dNS -> dNS.getFinantialDocumentType().equals(FinantialDocumentType.findForSettlementNote()))
                        .collect(Collectors.toList()));
        model.addAttribute("SettlementNote_currency_options",
                org.fenixedu.treasury.domain.Currency.findAll().collect(Collectors.toList()));
        model.addAttribute("stateValues", org.fenixedu.treasury.domain.document.FinantialDocumentStateType.values());
        return "treasury/document/managepayments/settlementnote/search";
    }

    private List<SettlementNote> getSearchUniverseSearchSettlementNoteDataSet() {
        return SettlementNote.findAll().collect(Collectors.toList());
    }

    private List<SettlementNote> filterSearchSettlementNote(org.fenixedu.treasury.domain.debt.DebtAccount debtAccount,
            org.fenixedu.treasury.domain.document.DocumentNumberSeries documentNumberSeries,
            org.fenixedu.treasury.domain.Currency currency, java.lang.String documentNumber, org.joda.time.DateTime documentDate,
            org.joda.time.DateTime documentDueDate, java.lang.String originDocumentNumber,
            org.fenixedu.treasury.domain.document.FinantialDocumentStateType state) {

        return getSearchUniverseSearchSettlementNoteDataSet()
                .stream()
                .filter(settlementNote -> FinantialDocumentType.findForSettlementNote() == settlementNote
                        .getFinantialDocumentType())
                .filter(settlementNote -> debtAccount == null || debtAccount == settlementNote.getDebtAccount())
                .filter(settlementNote -> documentNumberSeries == null
                        || documentNumberSeries == settlementNote.getDocumentNumberSeries())
                .filter(settlementNote -> currency == null || currency == settlementNote.getCurrency())
                .filter(settlementNote -> documentNumber == null || documentNumber.length() == 0
                        || settlementNote.getDocumentNumber() != null && settlementNote.getDocumentNumber().length() > 0
                        && settlementNote.getDocumentNumber().toLowerCase().contains(documentNumber.toLowerCase()))
                .filter(settlementNote -> documentDate == null || documentDate.equals(settlementNote.getDocumentDate()))
                .filter(settlementNote -> documentDueDate == null || documentDueDate.equals(settlementNote.getDocumentDueDate()))
                .filter(settlementNote -> originDocumentNumber == null || originDocumentNumber.length() == 0
                        || settlementNote.getOriginDocumentNumber() != null
                        && settlementNote.getOriginDocumentNumber().length() > 0
                        && settlementNote.getOriginDocumentNumber().toLowerCase().contains(originDocumentNumber.toLowerCase()))
                .filter(settlementNote -> state == null || state.equals(settlementNote.getState())).collect(Collectors.toList());
    }

    @RequestMapping(value = "/search/view/{oid}")
    public String processSearchToViewAction(@PathVariable("oid") SettlementNote settlementNote, Model model,
            RedirectAttributes redirectAttributes) {
        return redirect("/treasury/document/managepayments/settlementnote/read" + "/" + settlementNote.getExternalId(), model,
                redirectAttributes);
    }

    @RequestMapping(value = READ_URI + "{oid}")
    public String read(@PathVariable("oid") SettlementNote settlementNote, Model model) {
        setSettlementNote(settlementNote, model);
        return "treasury/document/managepayments/settlementnote/read";
    }

////////AUTO GENERATED ////////    
//
    //
    @RequestMapping(value = DELETE_URI + "{oid}", method = RequestMethod.POST)
    public String delete(@PathVariable("oid") SettlementNote settlementNote, Model model, RedirectAttributes redirectAttributes) {

        setSettlementNote(settlementNote, model);
        try {
            // call the Atomic delete function
            deleteSettlementNote(settlementNote);

            addInfoMessage(BundleUtil.getString(Constants.BUNDLE, "label.success.delete"), model);
            return redirect("/treasury/document/managepayments/settlementnote/", model, redirectAttributes);
        } catch (DomainException ex) {
            // Add error messages to the list
            addErrorMessage(BundleUtil.getString(Constants.BUNDLE, "label.error.delete") + ex.getLocalizedMessage(), model);
        }

        // The default mapping is the same Read View
        return "treasury/document/managepayments/settlementnote/read/" + getSettlementNote(model).getExternalId();
    }

    //
    @RequestMapping(value = UPDATE_URI + "{oid}", method = RequestMethod.GET)
    public String update(@PathVariable("oid") SettlementNote settlementNote, Model model) {
        model.addAttribute("SettlementNote_finantialDocumentType_options",
                org.fenixedu.treasury.domain.document.FinantialDocumentType.findAll());
        // // CHANGE_ME - MUST DEFINE RELATION
        model.addAttribute("SettlementNote_debtAccount_options", new ArrayList<org.fenixedu.treasury.domain.debt.DebtAccount>()); // CHANGE_ME
                                                                                                                                  // -
                                                                                                                                  // MUST
                                                                                                                                  // DEFINE
                                                                                                                                  // RELATION
        model.addAttribute("SettlementNote_documentNumberSeries_options",
                org.fenixedu.treasury.domain.document.DocumentNumberSeries.findAll());
        model.addAttribute("SettlementNote_currency_options", org.fenixedu.treasury.domain.Currency.findAll());
        model.addAttribute("stateValues", org.fenixedu.treasury.domain.document.FinantialDocumentStateType.values());
        setSettlementNote(settlementNote, model);
        return "treasury/document/managepayments/settlementnote/update";
    }

    //
    @RequestMapping(value = UPDATE_URI + "{oid}", method = RequestMethod.POST)
    public String update(
            @PathVariable("oid") SettlementNote settlementNote,
            @RequestParam(value = "finantialdocumenttype", required = false) org.fenixedu.treasury.domain.document.FinantialDocumentType finantialDocumentType,
            @RequestParam(value = "debtaccount", required = false) org.fenixedu.treasury.domain.debt.DebtAccount debtAccount,
            @RequestParam(value = "documentnumberseries", required = false) org.fenixedu.treasury.domain.document.DocumentNumberSeries documentNumberSeries,
            @RequestParam(value = "currency", required = false) org.fenixedu.treasury.domain.Currency currency,
            @RequestParam(value = "documentnumber", required = false) java.lang.String documentNumber,
            @RequestParam(value = "documentdate", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSZ") org.joda.time.DateTime documentDate,
            @RequestParam(value = "origindocumentnumber", required = false) java.lang.String originDocumentNumber, @RequestParam(
                    value = "state", required = false) org.fenixedu.treasury.domain.document.FinantialDocumentStateType state,
            Model model, RedirectAttributes redirectAttributes) {

        setSettlementNote(settlementNote, model);

        try {
            /*
             * UpdateLogic here
             */

            updateSettlementNote(finantialDocumentType, debtAccount, documentNumberSeries, currency, documentNumber,
                    documentDate, originDocumentNumber, state, model);

            /* Succes Update */

            return redirect("/treasury/document/managepayments/settlementnote/read/" + getSettlementNote(model).getExternalId(),
                    model, redirectAttributes);
        } catch (DomainException de) {
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
            return update(settlementNote, model);

        }
    }

    @Atomic
    public void updateSettlementNote(org.fenixedu.treasury.domain.document.FinantialDocumentType finantialDocumentType,
            org.fenixedu.treasury.domain.debt.DebtAccount debtAccount,
            org.fenixedu.treasury.domain.document.DocumentNumberSeries documentNumberSeries,
            org.fenixedu.treasury.domain.Currency currency, java.lang.String documentNumber, org.joda.time.DateTime documentDate,
            java.lang.String originDocumentNumber, org.fenixedu.treasury.domain.document.FinantialDocumentStateType state,
            Model model) {

        // @formatter: off
        /*
         * Modify the update code here if you do not want to update the object
         * with the default setter for each field
         */

        // CHANGE_ME It's RECOMMENDED to use "Edit service" in DomainObject
        // getSettlementNote(model).edit(fields_to_edit);

        // Instead, use individual SETTERS and validate "CheckRules" in the end
        // @formatter: on

        getSettlementNote(model).edit(finantialDocumentType, debtAccount, documentNumberSeries, currency, documentNumber,
                documentDate, originDocumentNumber, state);
    }

    //
    // This is the EventanullSettlementNote Method for Screen read
    //
    @RequestMapping(value = "/read/{oid}/anullsettlement", method = RequestMethod.POST)
    public String processReadToAnullSettlementNote(@PathVariable("oid") SettlementNote settlementNote,
            @RequestParam("anullReason") String anullReason, Model model, RedirectAttributes redirectAttributes) {
        setSettlementNote(settlementNote, model);
//
        try {
            settlementNote.changeState(FinantialDocumentStateType.ANNULED, anullReason);
            addInfoMessage(BundleUtil.getString(Constants.BUNDLE,
                    "label.document.managepayments.SettlementNote.document.anulled.sucess"), model);
        } catch (Exception ex) {
            addErrorMessage(ex.getLocalizedMessage(), model);
        }

        // Now choose what is the Exit Screen    
        return redirect(READ_URL + getSettlementNote(model).getExternalId(), model, redirectAttributes);
    }

    @RequestMapping(value = "/read/{oid}/exportintegrationfile", produces = "text/xml;charset=Windows-1252")
    public void processReadToExportIntegrationFile(@PathVariable("oid") SettlementNote settlementNote, Model model,
            RedirectAttributes redirectAttributes, HttpServletResponse response) {
        try {
            String output =
                    ERPExporter.exportFinantialDocument(
                            settlementNote.getDebtAccount().getFinantialInstitution(),
                            settlementNote.findRelatedDocuments(new HashSet<FinantialDocument>(), settlementNote.getDebtAccount()
                                    .getFinantialInstitution().getErpIntegrationConfiguration()
                                    .getExportAnnulledRelatedDocuments()));
            response.setContentType("text/xml");
            response.setCharacterEncoding("Windows-1252");
            String filename =
                    URLEncoder.encode(
                            StringNormalizer
                                    .normalizePreservingCapitalizedLetters(
                                            settlementNote.getDebtAccount().getFinantialInstitution().getFiscalNumber() + "_"
                                                    + settlementNote.getUiDocumentNumber() + ".xml").replaceAll("\\s", "_")
                                    .replace(" ", "_"), "Windows-1252");
            response.setHeader("Content-disposition", "attachment; filename=" + filename);
            response.getOutputStream().write(output.getBytes("Windows-1252"));
        } catch (Exception ex) {
            addErrorMessage(ex.getLocalizedMessage(), model);
            try {
                response.sendRedirect(redirect(READ_URL + settlementNote.getExternalId(), model, redirectAttributes));
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }

    // This is the EventcloseDebitNote Method for Screen read
    //
    @RequestMapping(value = "/read/{oid}/closesettlementnote", method = RequestMethod.POST)
    public String processReadToCloseDebitNote(@PathVariable("oid") SettlementNote settlementNote, Model model,
            RedirectAttributes redirectAttributes) {
        setSettlementNote(settlementNote, model);

        try {
            settlementNote.changeState(FinantialDocumentStateType.CLOSED, "");
            addInfoMessage(
                    BundleUtil.getString(Constants.BUNDLE, "label.document.manageinvoice.Settlement.document.closed.sucess"),
                    model);
        } catch (Exception ex) {
            addErrorMessage(ex.getLocalizedMessage(), model);
        }

        // Now choose what is the Exit Screen    
        return redirect(SettlementNoteController.READ_URL + getSettlementNote(model).getExternalId(), model, redirectAttributes);
    }

}
