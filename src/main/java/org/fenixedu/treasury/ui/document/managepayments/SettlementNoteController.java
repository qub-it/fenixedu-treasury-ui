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
import org.fenixedu.treasury.domain.exceptions.TreasuryDomainException;
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
@SpringFunctionality(app = TreasuryController.class, title = "label.title.document.managePayments",
        accessGroup = "treasuryFrontOffice")
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

    public static final long SEARCH_SETTLEMENT_NOTE_LIST_LIMIT_SIZE = 500;

    @RequestMapping
    public String home(Model model) {
        return "forward:" + SEARCH_URL;
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

    @RequestMapping(value = CHOOSE_INVOICE_ENTRIES_URI + "{debtAccountId}/{reimbursementNote}")
    public String chooseInvoiceEntries(@PathVariable(value = "debtAccountId") DebtAccount debtAccount, @PathVariable(
            value = "reimbursementNote") boolean reimbursementNote,
            @RequestParam(value = "bean", required = false) SettlementNoteBean bean, Model model) {
        if (bean == null) {
            bean = new SettlementNoteBean(debtAccount, reimbursementNote);
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
        if (bean.isReimbursementNote() && creditSum.compareTo(debitSum) < 0) {
            error = true;
            addErrorMessage(BundleUtil.getString(Constants.BUNDLE, "error.SettlementNote.positive.payment.value"), model);
        }
        if (!bean.isReimbursementNote() && creditSum.compareTo(debitSum) > 0) {
            error = true;
            addErrorMessage(BundleUtil.getString(Constants.BUNDLE, "error.SettlementNote.negative.payment.value"), model);
        }
        if (bean.isReimbursementNote() && creditSum.compareTo(BigDecimal.ZERO) == 0) {
            error = true;
            addErrorMessage(BundleUtil.getString(Constants.BUNDLE, "error.CreditEntry.no.creditEntries.selected"), model);
        }
        if (!bean.isReimbursementNote() && debitSum.compareTo(BigDecimal.ZERO) == 0) {
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
            if (debitEntryBean.isIncluded()
                    && Constants.isEqual(debitEntryBean.getDebitEntry().getOpenAmount(), debitEntryBean.getDebtAmount())) {

                //Calculate interest only if we are making a FullPayment
                InterestRateBean debitInterest = debitEntryBean.getDebitEntry().calculateUndebitedInterestValue(bean.getDate());
                if (debitInterest.getInterestAmount().compareTo(BigDecimal.ZERO) != 0) {
                    InterestEntryBean interestEntryBean =
                            bean.new InterestEntryBean(debitEntryBean.getDebitEntry(), debitInterest);
                    bean.getInterestEntries().add(interestEntryBean);
                }
            }
        }
        if (bean.getInterestEntries().size() == 0) {
            return calculateInterest(bean, model);
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
        BigDecimal debitSum = bean.isReimbursementNote() ? bean.getDebtAmountWithVat().negate() : bean.getDebtAmountWithVat();
        BigDecimal paymentSum = bean.getPaymentAmount();
        boolean error = false;
        if (bean.getPaymentEntries().stream().anyMatch(peb -> peb.getPaymentAmount().compareTo(BigDecimal.ZERO) == 0)) {
            error = true;
            String errorMessage =
                    bean.isReimbursementNote() ? "error.SettlementNote.reimbursement.equal.zero" : "error.SettlementNote.payment.equal.zero";
            addErrorMessage(BundleUtil.getString(Constants.BUNDLE, errorMessage), model);
        }
        if (debitSum.compareTo(paymentSum) != 0) {
            error = true;
            String errorMessage =
                    bean.isReimbursementNote() ? "error.SettlementNote.no.match.reimbursement.credit" : "error.SettlementNote.no.match.payment.debit";
            addErrorMessage(BundleUtil.getString(Constants.BUNDLE, errorMessage), model);
        }
        if (error) {
            setSettlementNoteBean(bean, model);
            return "treasury/document/managepayments/settlementnote/insertPayment";
        }
        setSettlementNoteBean(bean, model);
        return "treasury/document/managepayments/settlementnote/summary";
    }

    @RequestMapping(value = SUMMARY_URI, method = RequestMethod.POST)
    public String summary(@RequestParam(value = "bean", required = true) SettlementNoteBean bean, Model model,
            RedirectAttributes redirectAttributes) {
        try {
            SettlementNote settlementNote = processSettlementNoteCreation(bean);
            addInfoMessage(BundleUtil.getString(Constants.BUNDLE, "label.SettlementNote.create.success"), model);
            return redirect(READ_URL + settlementNote.getExternalId(), model, redirectAttributes);
        } catch (final TreasuryDomainException tde) {
            addErrorMessage(tde.getLocalizedMessage(), model);
        }
        return "treasury/document/managepayments/settlementnote/summary";
    }

    @Atomic
    public SettlementNote processSettlementNoteCreation(SettlementNoteBean bean) {
        SettlementNote settlementNote =
                SettlementNote.create(bean.getDebtAccount(), bean.getDocNumSeries(), bean.getDate().toDateTimeAtStartOfDay(),
                        bean.getOriginDocumentNumber());
        settlementNote.processSettlementNoteCreation(bean);
        return settlementNote;
    }

    @RequestMapping(value = SEARCH_URI)
    public String search(
            @RequestParam(value = "debtaccount", required = false) DebtAccount debtAccount,
            @RequestParam(value = "documentnumberseries", required = false) DocumentNumberSeries documentNumberSeries,
            @RequestParam(value = "currency", required = false) Currency currency,
            @RequestParam(value = "documentnumber", required = false) String documentNumber,
            @RequestParam(value = "documentdatefrom", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate documentDateFrom,
            @RequestParam(value = "documentdateto", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate documentDateTo,
            @RequestParam(value = "documentduedate", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") DateTime documentDueDate,
            @RequestParam(value = "origindocumentnumber", required = false) String originDocumentNumber, @RequestParam(
                    value = "state", required = false) FinantialDocumentStateType state, Model model) {
        List<SettlementNote> searchsettlementnoteResultsDataSet =
                filterSearchSettlementNote(debtAccount, documentNumberSeries, currency, documentNumber, documentDateFrom,
                        documentDateTo, documentDueDate, originDocumentNumber, state);

        // add the results dataSet to the model
        model.addAttribute("listSize", searchsettlementnoteResultsDataSet.size());
        model.addAttribute("searchsettlementnoteResultsDataSet", searchsettlementnoteResultsDataSet);
        searchsettlementnoteResultsDataSet =
                searchsettlementnoteResultsDataSet.stream().limit(SEARCH_SETTLEMENT_NOTE_LIST_LIMIT_SIZE)
                        .collect(Collectors.toList());

        model.addAttribute("SettlementNote_finantialDocumentType_options",
                FinantialDocumentType.findAll().collect(Collectors.toList()));
        model.addAttribute("SettlementNote_debtAccount_options", DebtAccount.findAll().collect(Collectors.toList()));
        model.addAttribute(
                "SettlementNote_documentNumberSeries_options",
                org.fenixedu.treasury.domain.document.DocumentNumberSeries.findAll()
                        .filter(dNS -> dNS.getFinantialDocumentType().equals(FinantialDocumentType.findForSettlementNote()))
                        .filter(x -> x.getSeries().getActive() == true).collect(Collectors.toList()));
        model.addAttribute("SettlementNote_currency_options",
                org.fenixedu.treasury.domain.Currency.findAll().collect(Collectors.toList()));
        model.addAttribute("stateValues", org.fenixedu.treasury.domain.document.FinantialDocumentStateType.values());
        return "treasury/document/managepayments/settlementnote/search";
    }

    private List<SettlementNote> getSearchUniverseSearchSettlementNoteDataSet() {
        return SettlementNote.findAll().collect(Collectors.toList());
    }

    private List<SettlementNote> filterSearchSettlementNote(DebtAccount debtAccount, DocumentNumberSeries documentNumberSeries,
            Currency currency, String documentNumber, LocalDate documentDateFrom, LocalDate documentDateTo,
            DateTime documentDueDate, String originDocumentNumber, FinantialDocumentStateType state) {

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
                .filter(creditNote -> documentDateFrom == null
                        || creditNote.getDocumentDate().toLocalDate().isEqual(documentDateFrom)
                        || creditNote.getDocumentDate().toLocalDate().isAfter(documentDateFrom))
                .filter(creditNote -> documentDateTo == null
                        || creditNote.getDocumentDate().toLocalDate().isEqual(documentDateTo)
                        || creditNote.getDocumentDate().toLocalDate().isBefore(documentDateTo))
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
        return redirect(READ_URL + settlementNote.getExternalId(), model, redirectAttributes);
    }

    @RequestMapping(value = READ_URI + "{oid}")
    public String read(@PathVariable("oid") SettlementNote settlementNote, Model model) {
        setSettlementNote(settlementNote, model);
        return "treasury/document/managepayments/settlementnote/read";
    }

    @RequestMapping(value = DELETE_URI + "{oid}", method = RequestMethod.POST)
    public String delete(@PathVariable("oid") SettlementNote settlementNote, Model model, RedirectAttributes redirectAttributes) {

        setSettlementNote(settlementNote, model);
        try {
            DebtAccount debtAccount = settlementNote.getDebtAccount();
            deleteSettlementNote(settlementNote);

            addInfoMessage(BundleUtil.getString(Constants.BUNDLE, "label.success.delete"), model);
            return redirect(DebtAccountController.READ_URL + debtAccount.getExternalId(), model, redirectAttributes);
        } catch (TreasuryDomainException tde) {
            addErrorMessage(BundleUtil.getString(Constants.BUNDLE, "label.error.delete") + tde.getLocalizedMessage(), model);
        } catch (Exception ex) {
            addErrorMessage(BundleUtil.getString(Constants.BUNDLE, "label.error.delete") + ex.getLocalizedMessage(), model);
        }

        return redirect(READ_URL + getSettlementNote(model).getExternalId(), model, redirectAttributes);
    }

    @RequestMapping(value = UPDATE_URI + "{oid}", method = RequestMethod.GET)
    public String update(@PathVariable("oid") SettlementNote settlementNote, Model model) {
        model.addAttribute("SettlementNote_finantialDocumentType_options",
                org.fenixedu.treasury.domain.document.FinantialDocumentType.findAll());
        model.addAttribute("SettlementNote_debtAccount_options", new ArrayList<org.fenixedu.treasury.domain.debt.DebtAccount>()); // CHANGE_ME
        model.addAttribute("SettlementNote_documentNumberSeries_options",
                org.fenixedu.treasury.domain.document.DocumentNumberSeries.findAll());
        model.addAttribute("SettlementNote_currency_options", org.fenixedu.treasury.domain.Currency.findAll());
        model.addAttribute("stateValues", org.fenixedu.treasury.domain.document.FinantialDocumentStateType.values());

        setSettlementNote(settlementNote, model);

        return "treasury/document/managepayments/settlementnote/update";
    }

    //
    @RequestMapping(value = UPDATE_URI + "{oid}", method = RequestMethod.POST)
    public String update(@PathVariable("oid") SettlementNote settlementNote, @RequestParam(value = "origindocumentnumber",
            required = false) java.lang.String originDocumentNumber, @RequestParam(value = "documentobservations",
            required = false) java.lang.String documentObservations, Model model, RedirectAttributes redirectAttributes) {

        setSettlementNote(settlementNote, model);

        try {
            updateSettlementNote(originDocumentNumber, documentObservations, model);

            return redirect(READ_URL + getSettlementNote(model).getExternalId(), model, redirectAttributes);
        } catch (TreasuryDomainException tde) {
            addErrorMessage(BundleUtil.getString(Constants.BUNDLE, "label.error.update") + tde.getLocalizedMessage(), model);
        } catch (Exception ex) {
            addErrorMessage(BundleUtil.getString(Constants.BUNDLE, "label.error.update") + ex.getLocalizedMessage(), model);
        }
        return update(settlementNote, model);
    }

    @Atomic
    public void updateSettlementNote(java.lang.String originDocumentNumber, String documentObservations, Model model) {
        getSettlementNote(model).setOriginDocumentNumber(originDocumentNumber);
        getSettlementNote(model).setDocumentObservations(documentObservations);
    }

    @RequestMapping(value = "/read/{oid}/anullsettlement", method = RequestMethod.POST)
    public String processReadToAnullSettlementNote(@PathVariable("oid") SettlementNote settlementNote,
            @RequestParam("anullReason") String anullReason, Model model, RedirectAttributes redirectAttributes) {
        setSettlementNote(settlementNote, model);
        try {
            settlementNote.changeState(FinantialDocumentStateType.ANNULED, anullReason);
            addInfoMessage(BundleUtil.getString(Constants.BUNDLE,
                    "label.document.managepayments.SettlementNote.document.anulled.sucess"), model);
        } catch (Exception ex) {
            addErrorMessage(ex.getLocalizedMessage(), model);
        }
        return redirect(READ_URL + getSettlementNote(model).getExternalId(), model, redirectAttributes);
    }

    @RequestMapping(value = "/read/{oid}/exportintegrationfile", produces = "text/xml;charset=Windows-1252")
    public void processReadToExportIntegrationFile(@PathVariable("oid") SettlementNote settlementNote, Model model,
            RedirectAttributes redirectAttributes, HttpServletResponse response) {
        try {
            String output =
                    ERPExporter.exportFinantialDocumentToXML(
                            settlementNote.getDebtAccount().getFinantialInstitution(),
                            settlementNote.findRelatedDocuments(new HashSet<FinantialDocument>(), settlementNote.getDebtAccount()
                                    .getFinantialInstitution().getErpIntegrationConfiguration()
                                    .getExportAnnulledRelatedDocuments()));
            response.setContentType("text/xml");
            response.setCharacterEncoding("Windows-1252");
            String filename =
                    URLEncoder.encode(
                            StringNormalizer.normalizePreservingCapitalizedLetters((settlementNote.getDebtAccount()
                                    .getFinantialInstitution().getFiscalNumber()
                                    + "_" + settlementNote.getUiDocumentNumber() + ".xml").replaceAll("/", "_")
                                    .replaceAll("\\s", "_").replaceAll(" ", "_")), "Windows-1252");
            response.setHeader("Content-disposition", "attachment; filename=" + filename);
            response.getOutputStream().write(output.getBytes("Windows-1252"));
        } catch (Exception ex) {
            addErrorMessage(ex.getLocalizedMessage(), model);
            try {
                response.sendRedirect(redirect(READ_URL + settlementNote.getExternalId(), model, redirectAttributes));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

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

        return redirect(SettlementNoteController.READ_URL + getSettlementNote(model).getExternalId(), model, redirectAttributes);
    }
}
