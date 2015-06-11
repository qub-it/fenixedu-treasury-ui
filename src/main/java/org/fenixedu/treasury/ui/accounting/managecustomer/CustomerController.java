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
package org.fenixedu.treasury.ui.accounting.managecustomer;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.fenixedu.bennu.spring.portal.SpringFunctionality;
import org.fenixedu.treasury.domain.Customer;
import org.fenixedu.treasury.domain.FinantialInstitution;
import org.fenixedu.treasury.domain.debt.DebtAccount;
import org.fenixedu.treasury.domain.document.InvoiceEntry;
import org.fenixedu.treasury.ui.TreasuryBaseController;
import org.fenixedu.treasury.ui.TreasuryController;
import org.fenixedu.treasury.ui.document.managepayments.SettlementNoteController;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import pt.ist.fenixframework.Atomic;

//@Component("org.fenixedu.treasury.ui.accounting.manageCustomer") <-- Use for duplicate controller name disambiguation
@SpringFunctionality(app = TreasuryController.class, title = "label.title.accounting.manageCustomer", accessGroup = "#managers")
// CHANGE_ME accessGroup = "group1 | group2 | groupXPTO"
// or
// @BennuSpringController(value=TreasuryController.class)
@RequestMapping(CustomerController.CONTROLLER_URI)
public class CustomerController extends TreasuryBaseController {
    public static final String CONTROLLER_URI = "/treasury/accounting/managecustomer/customer";
    private static final String SEARCH_URI = "/";
    public static final String SEARCH_FULL_URI = CONTROLLER_URI + SEARCH_URI;

    private static final String READ_URI = "/read/";
    public static final String READ_URL = CONTROLLER_URI + READ_URI;

    //

    @RequestMapping
    public String home(Model model) {
        // this is the default behaviour, for handling in a Spring Functionality
        return "forward:" + CONTROLLER_URI + SEARCH_URI;
    }

    private Customer getCustomer(Model model) {
        return (Customer) model.asMap().get("customer");
    }

    private void setCustomer(Customer customer, Model model) {
        model.addAttribute("customer", customer);
    }

    @Atomic
    public void deleteCustomer(Customer customer) {
        // CHANGE_ME: Do the processing for deleting the customer
        // Do not catch any exception here

//         customer.delete(); 
    }

    //
    @RequestMapping(value = SEARCH_URI)
    public String search(@RequestParam(value = "finantialInstitution", required = false) FinantialInstitution institution,
            Model model) {
        List<Customer> searchcustomerResultsDataSet = filterSearchCustomer(institution);

        model.addAttribute("finantialinstitution_options", FinantialInstitution.findAll().collect(Collectors.toList())); // CHANGE_ME

        // add the results dataSet to the model
        model.addAttribute("searchcustomerResultsDataSet", searchcustomerResultsDataSet);
        return "treasury/accounting/managecustomer/customer/search";
    }

    private List<Customer> getSearchUniverseSearchCustomerDataSet() {

        return Customer.findAll().sorted((x, y) -> x.getName().compareToIgnoreCase(y.getName()))
                .collect(Collectors.<Customer> toList());
    }

    private List<Customer> filterSearchCustomer(FinantialInstitution institution) {

        if (institution == null) {
            return getSearchUniverseSearchCustomerDataSet().stream().collect(Collectors.toList());
        } else {
            return getSearchUniverseSearchCustomerDataSet()
                    .stream()
                    .filter(customer -> customer.getDebtAccountsSet().stream()
                            .anyMatch(debtAccount -> debtAccount.getFinantialInstitution().equals(institution)))
                    .collect(Collectors.toList());
        }
    }

    public static final String SEARCH_TO_VIEW_ACTION_URI = "/search/view/";

    @RequestMapping(value = SEARCH_TO_VIEW_ACTION_URI + "{oid}")
    public String processSearchToViewAction(@PathVariable("oid") Customer customer, Model model,
            RedirectAttributes redirectAttributes) {

        // CHANGE_ME Insert code here for processing viewAction
        // If you selected multiple exists you must choose which one to use
        // below
        return redirect(READ_URL + customer.getExternalId(), model, redirectAttributes);
    }

    //
    @RequestMapping(value = READ_URI + "{oid}")
    public String read(@PathVariable("oid") Customer customer, Model model) {
        setCustomer(customer, model);

        List<InvoiceEntry> pendingInvoiceEntries = new ArrayList<InvoiceEntry>();
        for (DebtAccount debtAccount : customer.getDebtAccountsSet()) {
            pendingInvoiceEntries.addAll(debtAccount.getPendingInvoiceEntriesSet());
        }
        model.addAttribute("pendingDocumentsDataSet", pendingInvoiceEntries);

        return "treasury/accounting/managecustomer/customer/read";
    }

    //

    //
    // This is the EventcreatePayment Method for Screen read
    //
    public static final String READ_TO_CREATEPAYEMENT_EVENT_URI = "/read/{oid}/createpayment";

    @RequestMapping(value = READ_TO_CREATEPAYEMENT_EVENT_URI)
    public String processReadToCreatePayment(@PathVariable("oid") Customer customer, Model model,
            RedirectAttributes redirectAttributes) {
        setCustomer(customer, model);
        //
        /* Put here the logic for processing Event createPayment */
        // doSomething();

        // Now choose what is the Exit Screen
        return redirect(SettlementNoteController.CREATE_URL + getCustomer(model).getExternalId(), model, redirectAttributes);
    }

    //
    // This is the EventcreateDebtEntry Method for Screen read
    //
    @RequestMapping(value = "/read/{oid}/createdebtentry")
    public String processReadToCreateDebtEntry(@PathVariable("oid") Customer customer, Model model,
            RedirectAttributes redirectAttributes) {
        setCustomer(customer, model);
        //
        /* Put here the logic for processing Event createDebtEntry */
        // doSomething();

        // Now choose what is the Exit Screen
        return redirect("/<COULD_NOT_GET_THE_VIEW_FROM_PSL_FOR_SCREEN_createDebt>/" + getCustomer(model).getExternalId(), model,
                redirectAttributes);
    }

    //
    // This is the EventcreateExemption Method for Screen read
    //
    @RequestMapping(value = "/read/{oid}/createexemption")
    public String processReadToCreateExemption(@PathVariable("oid") Customer customer, Model model,
            RedirectAttributes redirectAttributes) {
        setCustomer(customer, model);
        //
        /* Put here the logic for processing Event createExemption */
        // doSomething();

        // Now choose what is the Exit Screen
        return redirect("/treasury/document/manageexemption/treasuryexemption/create/" + getCustomer(model).getExternalId(),
                model, redirectAttributes);
    }

}
