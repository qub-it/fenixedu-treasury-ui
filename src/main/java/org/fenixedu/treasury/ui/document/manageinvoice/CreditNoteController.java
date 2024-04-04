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

import static org.fenixedu.treasury.util.TreasuryConstants.treasuryBundle;

import java.net.URLEncoder;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.servlet.http.HttpServletResponse;

import org.fenixedu.bennu.spring.portal.SpringFunctionality;
import org.fenixedu.commons.StringNormalizer;
import org.fenixedu.treasury.domain.debt.DebtAccount;
import org.fenixedu.treasury.domain.document.CreditNote;
import org.fenixedu.treasury.domain.document.ERPCustomerFieldsBean;
import org.fenixedu.treasury.domain.document.FinantialDocumentStateType;
import org.fenixedu.treasury.domain.exceptions.TreasuryDomainException;
import org.fenixedu.treasury.domain.integration.ERPExportOperation;
import org.fenixedu.treasury.services.integration.erp.ERPExporterManager;
import org.fenixedu.treasury.services.integration.erp.IERPExporter;
import org.fenixedu.treasury.ui.TreasuryBaseController;
import org.fenixedu.treasury.ui.TreasuryController;
import org.fenixedu.treasury.ui.integration.erp.ERPExportOperationController;
import org.joda.time.LocalDate;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.google.common.base.Strings;
import com.google.common.collect.Lists;

//@Component("org.fenixedu.treasury.ui.document.manageInvoice") <-- Use for duplicate controller name disambiguation
@SpringFunctionality(app = TreasuryController.class, title = "label.title.document.manageCreditNote",
        accessGroup = "treasuryFrontOffice")
@RequestMapping(CreditNoteController.CONTROLLER_URL)
public class CreditNoteController extends TreasuryBaseController {

    public static final String CONTROLLER_URL = "/treasury/document/manageinvoice/creditnote";

    public static final long SEARCH_LIMIT_SIZE = 500;

    @RequestMapping
    public String home(Model model) {
        return "forward:" + CONTROLLER_URL + "/";
    }

    private CreditNote getCreditNote(Model model) {
        return (CreditNote) model.asMap().get("creditNote");
    }

    private void setCreditNote(CreditNote creditNote, Model model) {
        model.addAttribute("creditNote", creditNote);
    }

    private static final String _READ_URI = "/read/";
    public static final String READ_URL = CONTROLLER_URL + _READ_URI;

    @RequestMapping(value = _READ_URI + "{oid}")
    public String read(@PathVariable("oid") CreditNote creditNote, Model model) {
        setCreditNote(creditNote, model);

        final List<String> errorMessages = Lists.newArrayList();
        boolean validAddress = ERPCustomerFieldsBean.validateAddress(creditNote.getDebtAccount().getCustomer(), errorMessages);

        if (creditNote.getPayorDebtAccount() != null) {
            validAddress = ERPCustomerFieldsBean.validateAddress(creditNote.getPayorDebtAccount().getCustomer(), errorMessages);
        }

        model.addAttribute("validAddress", validAddress);
        model.addAttribute("addressErrorMessages", errorMessages);

        return "treasury/document/manageinvoice/creditnote/read";
    }

    @RequestMapping(value = "/read/{oid}/closecreditnote", method = RequestMethod.POST)
    public String processReadToCloseCreditNote(@PathVariable("oid") CreditNote creditNote, Model model,
            RedirectAttributes redirectAttributes) {
        setCreditNote(creditNote, model);
        try {
            assertUserIsAllowToModifyInvoices(creditNote.getDocumentNumberSeries().getSeries().getFinantialInstitution(), model);

            creditNote.closeDocument();

            addInfoMessage(treasuryBundle("label.document.manageinvoice.CreditNote.document.closed.sucess"), model);
        } catch (Exception ex) {
            addErrorMessage(ex.getLocalizedMessage(), model);
        }
        return redirect(CreditNoteController.READ_URL + getCreditNote(model).getExternalId(), model, redirectAttributes);
    }

    @RequestMapping(value = "/anull/{oid}", method = RequestMethod.POST)
    public String anull(@PathVariable("oid") final CreditNote creditNote,
            @RequestParam(value = "reason", required = false) final String reason, final Model model,
            final RedirectAttributes redirectAttributes) {
        setCreditNote(creditNote, model);

        try {
            assertUserIsAllowToModifyInvoices(creditNote.getDocumentNumberSeries().getSeries().getFinantialInstitution(), model);

            if (creditNote.isAdvancePayment()) {
                throw new RuntimeException("error.CreditNoteController.advancedPayment.annulment.not.allowed");
            }

            creditNote.anullDocument(reason);
            addInfoMessage(treasuryBundle("label.document.manageinvoice.CreditNote.document.anulled.sucess"), model);
        } catch (Exception ex) {
            addErrorMessage(ex.getLocalizedMessage(), model);
        }

        return redirect(CreditNoteController.READ_URL + getCreditNote(model).getExternalId(), model, redirectAttributes);
    }

    @RequestMapping(value = "/read/{oid}/addentry")
    public String processReadToAddEntry(@PathVariable("oid") CreditNote creditNote, Model model,
            RedirectAttributes redirectAttributes) {
        setCreditNote(creditNote, model);
        return redirect(CreditEntryController.CREATE_URL + "?creditnote=" + getCreditNote(model).getExternalId(), model,
                redirectAttributes);
    }

    private static final String _UPDATE_URI = "/update/";
    public static final String UPDATE_URL = CONTROLLER_URL + _UPDATE_URI;

    @RequestMapping(value = _UPDATE_URI + "{oid}", method = RequestMethod.GET)
    public String update(@PathVariable("oid") CreditNote creditNote, Model model) {
        model.addAttribute("stateValues", org.fenixedu.treasury.domain.document.FinantialDocumentStateType.values());
        setCreditNote(creditNote, model);
        return "treasury/document/manageinvoice/creditnote/update";
    }

    @RequestMapping(value = _UPDATE_URI + "{oid}", method = RequestMethod.POST)
    public String update(@PathVariable("oid") CreditNote creditNote,
            @RequestParam(value = "origindocumentnumber", required = false) String originDocumentNumber,
            @RequestParam(value = "documentobservations", required = false) String documentObservations, Model model,
            RedirectAttributes redirectAttributes) {

        setCreditNote(creditNote, model);

        try {
            assertUserIsAllowToModifyInvoices(creditNote.getDocumentNumberSeries().getSeries().getFinantialInstitution(), model);

            getCreditNote(model).updateCreditNote(originDocumentNumber, documentObservations,
                    getCreditNote(model).getDocumentTermsAndConditions());

            return redirect(CreditNoteController.READ_URL + getCreditNote(model).getExternalId(), model, redirectAttributes);
        } catch (TreasuryDomainException tde) {
            addErrorMessage(treasuryBundle("label.error.update") + tde.getLocalizedMessage(), model);
        } catch (Exception ex) {
            addErrorMessage(treasuryBundle("label.error.update") + ex.getLocalizedMessage(), model);
        }
        return update(creditNote, model);
    }

    private static final String _SEARCH_URI = "/";
    public static final String SEARCH_URL = CONTROLLER_URL + _SEARCH_URI;

    @RequestMapping(value = _SEARCH_URI)
    public String search(@RequestParam(value = "debtaccount", required = false) DebtAccount debtAccount,
            @RequestParam(value = "documentnumber", required = false) String documentNumber,
            @RequestParam(value = "documentdatefrom",
                    required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate documentDateFrom,
            @RequestParam(value = "documentdateto",
                    required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate documentDateTo,
            @RequestParam(value = "origindocumentnumber", required = false) String originDocumentNumber,
            @RequestParam(value = "state", required = false) FinantialDocumentStateType state, Model model) {

        final List<CreditNote> result =
                filterSearch(debtAccount, documentNumber, documentDateFrom, documentDateTo, originDocumentNumber, state);
        model.addAttribute("limit_exceeded", result.size() > SEARCH_LIMIT_SIZE);
        model.addAttribute("searchcreditnoteResultsDataSet_totalCount", result.size());
        model.addAttribute("searchcreditnoteResultsDataSet",
                result.stream().limit(SEARCH_LIMIT_SIZE).collect(Collectors.toList()));

        model.addAttribute("stateValues", FinantialDocumentStateType.findAll());

        return "treasury/document/manageinvoice/creditnote/search";
    }

    private List<CreditNote> getSearchUniverse(final DebtAccount debtAccount) {
        final Stream<CreditNote> result = debtAccount == null ? CreditNote.findAll() : CreditNote.find(debtAccount);
        return result.collect(Collectors.toList());
    }

    private List<CreditNote> filterSearch(DebtAccount debtAccount, String documentNumber, LocalDate documentDateFrom,
            LocalDate documentDateTo, String originDocumentNumber, FinantialDocumentStateType state) {

        final List<CreditNote> result = Lists.newArrayList();
        // FIXME legidio, wish there was a way to test an empty Predicate
        boolean search = false;

        Predicate<CreditNote> predicate = i -> true;
        if (debtAccount != null) {
            search = true;
        }
        if (!Strings.isNullOrEmpty(documentNumber)) {
            search = true;
            predicate = predicate.and(i -> !Strings.isNullOrEmpty(i.getDocumentNumber())
                    && i.getUiDocumentNumber().toLowerCase().contains(documentNumber.trim().toLowerCase()));
        }
        if (documentDateFrom != null) {
            search = true;
            predicate = predicate.and(i -> i.getDocumentDate().toLocalDate().isEqual(documentDateFrom)
                    || i.getDocumentDate().toLocalDate().isAfter(documentDateFrom));
        }
        if (documentDateTo != null) {
            search = true;
            predicate = predicate.and(i -> i.getDocumentDate().toLocalDate().isEqual(documentDateTo)
                    || i.getDocumentDate().toLocalDate().isBefore(documentDateTo));
        }
        if (!StringUtils.isEmpty(originDocumentNumber)) {
            search = true;
            predicate = predicate.and(i -> !StringUtils.isEmpty(i.getOriginDocumentNumber())
                    && i.getOriginDocumentNumber().toLowerCase().contains(originDocumentNumber.trim().toLowerCase()));
        }
        if (state != null) {
            search = true;
            predicate = predicate.and(i -> state == i.getState());
        }

        if (search) {
            getSearchUniverse(debtAccount).stream().filter(predicate).collect(Collectors.toCollection(() -> result));
        }

        return result;
    }

    private static final String _SEARCH_TO_VIEW_ACTION_URI = "/search/view/";
    public static final String SEARCH_TO_VIEW_ACTION_URL = CONTROLLER_URL + _SEARCH_TO_VIEW_ACTION_URI;

    @RequestMapping(value = _SEARCH_TO_VIEW_ACTION_URI + "{oid}")
    public String processSearchToViewAction(@PathVariable("oid") CreditNote creditNote, Model model,
            RedirectAttributes redirectAttributes) {

        return redirect(CreditNoteController.READ_URL + creditNote.getExternalId(), model, redirectAttributes);
    }

    @RequestMapping(value = "/read/{oid}/exportintegrationfile", produces = "text/xml")
    public String processReadToExportIntegrationFile(@PathVariable("oid") CreditNote creditNote, Model model,
            RedirectAttributes redirectAttributes, HttpServletResponse response) {
        try {
            assertUserIsFrontOfficeMember(creditNote.getDocumentNumberSeries().getSeries().getFinantialInstitution(), model);
            final String saftEncoding = ERPExporterManager.saftEncoding(creditNote.getDebtAccount().getFinantialInstitution());

            final String output = ERPExporterManager.exportFinantialDocumentToXML(creditNote);

            response.setContentType("text/xml");
            response.setCharacterEncoding(saftEncoding);
            String filename = URLEncoder.encode(StringNormalizer.normalizePreservingCapitalizedLetters(
                    (creditNote.getDebtAccount().getFinantialInstitution().getFiscalNumber() + "_"
                            + creditNote.getUiDocumentNumber() + ".xml").replaceAll("/", "_").replaceAll("\\s", "_")
                                    .replaceAll(" ", "_")),
                    saftEncoding);
            response.setHeader("Content-disposition", "attachment; filename=" + filename);
            response.getOutputStream().write(output.getBytes(saftEncoding));

            return null;
        } catch (Exception ex) {
            addErrorMessage(ex.getLocalizedMessage(), model);
            return read(creditNote, model);
        }
    }

    @RequestMapping(value = "/read/{oid}/exportintegrationonline")
    public String processReadToExportIntegrationOnline(@PathVariable("oid") CreditNote creditNote, Model model,
            RedirectAttributes redirectAttributes) {
        try {
            assertUserIsFrontOfficeMember(creditNote.getDocumentNumberSeries().getSeries().getFinantialInstitution(), model);

            //Force a check status first of the document
            try {
                final IERPExporter erpExporter = creditNote.getDebtAccount().getFinantialInstitution()
                        .getErpIntegrationConfiguration().getERPExternalServiceImplementation().getERPExporter();

                erpExporter.checkIntegrationDocumentStatus(creditNote);
            } catch (Exception ex) {
            }

            final ERPExportOperation output = ERPExporterManager.exportSingleDocument(creditNote);

            if (output == null) {
                addInfoMessage(treasuryBundle("label.integration.erp.document.not.exported"), model);
                return read(creditNote, model);
            }

            addInfoMessage(treasuryBundle("label.integration.erp.exportoperation.success"), model);
            return redirect(ERPExportOperationController.READ_URL + output.getExternalId(), model, redirectAttributes);
        } catch (Exception ex) {
            addErrorMessage(treasuryBundle("label.integration.erp.exportoperation.error") + ex.getLocalizedMessage(), model);
        }

        return read(creditNote, model);
    }

    @RequestMapping(value = "/read/{oid}/cleardocumenttoexport", method = RequestMethod.POST)
    public String cleardocumenttoexport(@PathVariable("oid") final CreditNote creditNote,
            @RequestParam(value = "reason", required = false) final String reason, final Model model,
            final RedirectAttributes redirectAttributes) {
        try {

            if (!creditNote.isDocumentToExport()) {
                addErrorMessage(treasuryBundle("error.FinantialDocument.document.not.marked.to.export"), model);
                return redirect(READ_URL + creditNote.getExternalId(), model, redirectAttributes);
            }

            if (Strings.isNullOrEmpty(reason)) {
                addErrorMessage(treasuryBundle("error.FinantialDocument.clear.document.to.export.requires.reason"), model);
                return redirect(READ_URL + creditNote.getExternalId(), model, redirectAttributes);
            }

            assertUserIsBackOfficeMember(model);

            creditNote.clearDocumentToExport(reason);

            return redirect(READ_URL + creditNote.getExternalId(), model, redirectAttributes);
        } catch (final Exception e) {
            addErrorMessage(e.getLocalizedMessage(), model);
            return read(creditNote, model);
        }
    }

    private static final String _DOWNLOAD_CERTIFIED_DOCUMENT_PRINT_URI = "/downloadcertifieddocumentprint";
    public static final String DOWNLOAD_CERTIFIED_DOCUMENT_PRINT_URL = CONTROLLER_URL + _DOWNLOAD_CERTIFIED_DOCUMENT_PRINT_URI;

    @RequestMapping(value = _DOWNLOAD_CERTIFIED_DOCUMENT_PRINT_URI + "/{oid}", method = RequestMethod.GET)
    public String downloadcertifieddocumentprint(@PathVariable("oid") final CreditNote creditNote, final Model model,
            final RedirectAttributes redirectAttributes, final HttpServletResponse response) {

        try {

            final byte[] contents = ERPExporterManager.downloadCertifiedDocumentPrint(creditNote);

            response.setContentType("application/pdf");
            String filename = URLEncoder.encode(StringNormalizer.normalizePreservingCapitalizedLetters(
                    (creditNote.getDebtAccount().getFinantialInstitution().getFiscalNumber() + "_"
                            + creditNote.getUiDocumentNumber() + ".pdf").replaceAll("/", "_").replaceAll("\\s", "_")
                                    .replaceAll(" ", "_")),
                    "Windows-1252");

            response.setHeader("Content-disposition", "attachment; filename=" + filename);
            response.getOutputStream().write(contents);

            return null;
        } catch (final Exception e) {
            addErrorMessage(e.getLocalizedMessage(), model);
            return read(creditNote, model);
        }
    }

}
