/**
 * This file was created by Quorum Born IT <http://www.qub-it.com/> and its 
 * copyright terms are bind to the legal agreement regulating the FenixEdu@ULisboa 
 * software development project between Quorum Born IT and ServiÃ§os Partilhados da
 * Universidade de Lisboa:
 *  - Copyright Â© 2015 Quorum Born IT (until any Go-Live phase)
 *  - Copyright Â© 2015 Universidade de Lisboa (after any Go-Live phase)
 *
 * Contributors: ricardo.pedro@qub-it.com
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
package org.fenixedu.treasury.ui.accounting.managecustomer;

import static org.fenixedu.treasury.util.TreasuryConstants.treasuryBundle;

import java.util.List;
import java.util.stream.Collectors;

import org.fenixedu.bennu.spring.portal.BennuSpringController;
import org.fenixedu.treasury.domain.debt.DebtAccount;
import org.fenixedu.treasury.domain.document.CreditEntry;
import org.fenixedu.treasury.domain.document.DebitEntry;
import org.fenixedu.treasury.domain.event.TreasuryEvent;
import org.fenixedu.treasury.domain.exceptions.TreasuryDomainException;
import org.fenixedu.treasury.domain.exemption.TreasuryExemption;
import org.fenixedu.treasury.ui.TreasuryBaseController;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.google.common.base.Strings;

import pt.ist.fenixframework.Atomic;

//@Component("org.fenixedu.treasury.ui.viewCustomerTreasuryEvents") <-- Use for duplicate controller name disambiguation
//@SpringFunctionality(app = TreasuryController.class, title = "label.title.viewCustomerTreasuryEvents",accessGroup = "#managers")// CHANGE_ME accessGroup = "group1 | group2 | groupXPTO"
//or
@BennuSpringController(value = CustomerController.class)
@RequestMapping(TreasuryEventController.CONTROLLER_URL)
public class TreasuryEventController extends TreasuryBaseController {
    public static final String CONTROLLER_URL = "/treasury/accounting/managecustomer/treasuryevent";
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

    private static final String ANNULDEBITENTRY_URI = "/anulldebitentry/";
    public static final String ANNULDEBITENTRY_URL = CONTROLLER_URL + ANNULDEBITENTRY_URI;

    private static final String REVERTANNULDEBITENTRY_URI = "/revertanulldebitentry/";
    public static final String REVERTANNULDEBITENTRY_URL = CONTROLLER_URL + REVERTANNULDEBITENTRY_URI;

    @RequestMapping
    public String home(Model model) {
        return "forward:" + SEARCH_URL;
    }

    private TreasuryEvent getTreasuryEvent(Model model) {
        return (TreasuryEvent) model.asMap().get("treasuryEvent");
    }

    private void setTreasuryEvent(TreasuryEvent treasuryEvent, Model model) {
        model.addAttribute("treasuryEvent", treasuryEvent);
    }

    @Atomic
    public void deleteTreasuryEvent(TreasuryEvent treasuryEvent) {
        // treasuryEvent.delete();
    }

    @RequestMapping(value = "/")
    public String searchTreasuryEvents(@RequestParam(value = "debtaccount", required = true) DebtAccount debtAccount,
            Model model) {
        List<? extends TreasuryEvent> searchtreasuryeventsResultsDataSet = filterSearchTreasuryEvents(debtAccount);

        model.addAttribute("debtAccount", debtAccount);
        model.addAttribute("searchtreasuryeventsResultsDataSet", searchtreasuryeventsResultsDataSet);
        return "treasury/accounting/managecustomer/treasuryevent/search";
    }

    private List<? extends TreasuryEvent> filterSearchTreasuryEvents(final DebtAccount debtAccount) {
        return TreasuryEvent.find(debtAccount.getCustomer()).collect(Collectors.<TreasuryEvent> toList());
    }

    @RequestMapping(value = "/search/view/{debtAccountId}/{oid}")
    public String processSearchTreasuryEventsToViewAction(
            @PathVariable("debtAccountId") final DebtAccount debtAccount,
            @PathVariable("oid") final TreasuryEvent treasuryEvent, final Model model,
            final RedirectAttributes redirectAttributes) {

        return redirect(String.format("/treasury/accounting/managecustomer/treasuryevent/read/%s/%s", debtAccount.getExternalId(),
                treasuryEvent.getExternalId()), model, redirectAttributes);
    }

    @RequestMapping(value = "/read/{debtAccountId}/{oid}")
    public String read(
            @PathVariable("debtAccountId") final DebtAccount debtAccount,
            @PathVariable("oid") final TreasuryEvent treasuryEvent, final Model model) {

        model.addAttribute("debtAccount", debtAccount);
        model.addAttribute("allActiveDebitEntriesDataSet", DebitEntry.find(treasuryEvent)
                .sorted(DebitEntry.COMPARE_BY_EVENT_ANNULED_AND_DUE_DATE).collect(Collectors.<DebitEntry> toList()));

        model.addAttribute("allActiveCreditEntriesDataSet",
                CreditEntry.find(treasuryEvent).collect(Collectors.<CreditEntry> toList()));

        setTreasuryEvent(treasuryEvent, model);
        return "treasury/accounting/managecustomer/treasuryevent/read";
    }

    @RequestMapping(value = ANNULDEBITENTRY_URI + "{debtAccountId}/{treasuryEventId}/{debitEntryId}")
    public String anullDebitEntry(
            @PathVariable("debtAccountId") final DebtAccount debtAccount,
            @PathVariable("treasuryEventId") final TreasuryEvent treasuryEvent,
            @PathVariable("debitEntryId") final DebitEntry debitEntry, final Model model) {

        try {
            assertUserIsFrontOfficeMember(debitEntry.getDebtAccount().getFinantialInstitution(), model);

            if (!debitEntry.isEventAnnuled()) {
                debitEntry.annulOnEvent();
            }

        } catch (final Exception e) {
            addErrorMessage(e.getLocalizedMessage(), model);
        }

        return read(debtAccount, treasuryEvent, model);
    }

    @RequestMapping(value = REVERTANNULDEBITENTRY_URI + "{debtAccountId}/{treasuryEventId}/{debitEntryId}")
    public String revertAnullDebitEntry(
            @PathVariable("debtAccountId") final DebtAccount debtAccount,
            @PathVariable("treasuryEventId") final TreasuryEvent treasuryEvent,
            @PathVariable("debitEntryId") final DebitEntry debitEntry, final Model model) {

        try {
            assertUserIsFrontOfficeMember(debitEntry.getDebtAccount().getFinantialInstitution(), model);

            if (debitEntry.isEventAnnuled()) {
                debitEntry.revertEventAnnuled();
            }

        } catch (final Exception e) {
            addErrorMessage(e.getLocalizedMessage(), model);
        }

        return read(debtAccount, treasuryEvent, model);
    }

    private static final String ANNULALLDEBITENTRIES_URI = "/annulalldebitentries/";
    public static final String ANNULALLDEBITENTRIES_URL = CONTROLLER_URL + ANNULALLDEBITENTRIES_URI;

    @RequestMapping(value = ANNULALLDEBITENTRIES_URI + "{debtAccountId}/{treasuryEventId}")
    public String annulAllDebitEntries(
            @PathVariable("debtAccountId") final DebtAccount debtAccount,
            @PathVariable("treasuryEventId") final TreasuryEvent treasuryEvent,
            @RequestParam(value = "treasuryEventAnullDebitEntriesReason", required = false) final String reason,
            final Model model) {
        try {
            assertUserIsFrontOfficeMember(model);

            if (Strings.isNullOrEmpty(reason)) {
                addErrorMessage(treasuryBundle("label.TreasuryEvent.annulAllDebitEntries.reason.required"), model);
            }

            treasuryEvent.annulAllDebitEntries(reason);

            addInfoMessage(treasuryBundle("label.TreasuryEvent.annulAllDebitEntries.success"), model);

        } catch (final Exception e) {
            addErrorMessage(e.getLocalizedMessage(), model);
        }

        return read(debtAccount, treasuryEvent, model);
    }

    private static final String _SEARCH_TO_DELETE_ACTION_URI = "/treasuryexemption/delete/";
    public static final String SEARCH_TO_DELETE_ACTION_URL = CONTROLLER_URL + _SEARCH_TO_DELETE_ACTION_URI;

    @RequestMapping(value = _SEARCH_TO_DELETE_ACTION_URI + "{debtAccountId}/{oid}", method = RequestMethod.POST)
    public String processSearchToDeleteAction(@PathVariable("debtAccountId") final DebtAccount debtAccount,
            @PathVariable("oid") final TreasuryExemption treasuryExemption, Model model, RedirectAttributes redirectAttributes) {

        final TreasuryEvent treasuryEvent = treasuryExemption.getDebitEntry().getTreasuryEvent();

        try {
            assertUserIsFrontOfficeMember(model);
            
            if(treasuryExemption.getDebitEntry().isAnnulled() 
                    || treasuryExemption.getDebitEntry().getTreasuryEvent() == null
                    || treasuryExemption.getDebitEntry().isEventAnnuled()) {
                throw new TreasuryDomainException("error.TreasuryExemption.delete.impossible");
            }
            
            treasuryExemption.revertExemption();

            addInfoMessage(treasuryBundle("label.success.delete"), model);
        } catch (final Exception ex) {
            addErrorMessage(ex.getLocalizedMessage(), model);
            return read(debtAccount, treasuryEvent, model);
        }

        return redirect(treasuryEventUrl(debtAccount, treasuryEvent), model, redirectAttributes);
    }

    private String treasuryEventUrl(final DebtAccount debtAccount, final TreasuryEvent treasuryEvent) {
        return String.format("%s/%s/%s", TreasuryEventController.READ_URL, debtAccount.getExternalId(),
                treasuryEvent.getExternalId());
    }

}
