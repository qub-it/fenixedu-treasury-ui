/**
 * This file was created by Quorum Born IT <http://www.qub-it.com/> and its 
 * copyright terms are bind to the legal agreement regulating the FenixEdu@ULisboa 
 * software development project between Quorum Born IT and Serviços Partilhados da
 * Universidade de Lisboa:
 *  - Copyright © 2015 Quorum Born IT (until any Go-Live phase)
 *  - Copyright © 2015 Universidade de Lisboa (after any Go-Live phase)
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

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.fenixedu.bennu.spring.portal.BennuSpringController;
import org.fenixedu.treasury.domain.TreasuryExemption;
import org.fenixedu.treasury.domain.debt.DebtAccount;
import org.fenixedu.treasury.domain.document.InvoiceEntry;
import org.fenixedu.treasury.domain.document.SettlementEntry;
import org.fenixedu.treasury.domain.document.SettlementNote;
import org.fenixedu.treasury.dto.DebtAccountBean;
import org.fenixedu.treasury.ui.TreasuryBaseController;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import pt.ist.fenixframework.Atomic;

//@Component("org.fenixedu.treasury.ui.accounting.manageCustomer") <-- Use for duplicate controller name disambiguation
//@SpringFunctionality(app = TreasuryController.class, title = "label.title.accounting.manageCustomer2", accessGroup = "logged")
// CHANGE_ME accessGroup = "group1 | group2 | groupXPTO"
//or
@BennuSpringController(value = CustomerController.class)
@RequestMapping(DebtAccountController.CONTROLLER_URL)
public class DebtAccountController extends TreasuryBaseController {
    public static final String CONTROLLER_URL = "/treasury/accounting/managecustomer/debtaccount";
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
        return "forward:/treasury/accounting/managecustomer/customer/";
    }

    // @formatter: off

    /*
    * This should be used when using AngularJS in the JSP
    */

    private DebtAccount getDebtAccountBean(Model model) {
        return (DebtAccount) model.asMap().get("debtAccountBean");
    }

    private void setDebtAccountBean(DebtAccountBean bean, Model model) {
        model.addAttribute("debtAccountBeanJson", getBeanJson(bean));
        model.addAttribute("debtAccountBean", bean);
    }

    // @formatter: on

    private DebtAccount getDebtAccount(Model model) {
        return (DebtAccount) model.asMap().get("debtAccount");
    }

    private void setDebtAccount(DebtAccount debtAccount, Model model) {
        model.addAttribute("debtAccount", debtAccount);
    }

    @Atomic
    public void deleteDebtAccount(DebtAccount debtAccount) {
        // CHANGE_ME: Do the processing for deleting the debtAccount
        // Do not catch any exception here

        // debtAccount.delete();
    }

//				
    @RequestMapping(value = READ_URI + "{oid}")
    public String read(@PathVariable("oid") DebtAccount debtAccount, Model model) {

        setDebtAccount(debtAccount, model);

        List<InvoiceEntry> allInvoiceEntries = new ArrayList<InvoiceEntry>();
        List<SettlementEntry> paymentEntries = new ArrayList<SettlementEntry>();
        List<TreasuryExemption> exemptionEntries = new ArrayList<TreasuryExemption>();
        List<InvoiceEntry> pendingInvoiceEntries = new ArrayList<InvoiceEntry>();
        allInvoiceEntries.addAll(debtAccount.getInvoiceEntrySet());
        SettlementNote.findByDebtAccount(debtAccount).map(
                x -> paymentEntries.addAll(x.getSettlemetEntriesSet().collect(Collectors.toList())));

        exemptionEntries.addAll(TreasuryExemption.findByDebtAccount(debtAccount).collect(Collectors.toList()));

        pendingInvoiceEntries.addAll(debtAccount.getPendingInvoiceEntriesSet());

        model.addAttribute("pendingDocumentsDataSet", pendingInvoiceEntries);
        model.addAttribute("allDocumentsDataSet", allInvoiceEntries);
        model.addAttribute("paymentsDataSet", paymentEntries);
        model.addAttribute("exemptionDataSet", exemptionEntries);

        return "treasury/accounting/managecustomer/debtaccount/read";
    }

//

    //
    // This is the EventcreatePayment Method for Screen read
    //
    @RequestMapping(value = "/read/{oid}/createpayment")
    public String processReadToCreatePayment(@PathVariable("oid") DebtAccount debtAccount, Model model,
            RedirectAttributes redirectAttributes) {
        setDebtAccount(debtAccount, model);
//
        /* Put here the logic for processing Event createPayment 	*/
        //doSomething();

        // Now choose what is the Exit Screen	 
        return redirect("/treasury/document/managepayments/settlementnote/create/" + getDebtAccount(model).getExternalId(),
                model, redirectAttributes);
    }

    //
    // This is the EventcreateDebtEntry Method for Screen read
    //
    @RequestMapping(value = "/read/{oid}/createdebtentry")
    public String processReadToCreateDebtEntry(@PathVariable("oid") DebtAccount debtAccount, Model model,
            RedirectAttributes redirectAttributes) {
        setDebtAccount(debtAccount, model);
//
        /* Put here the logic for processing Event createDebtEntry 	*/
        //doSomething();

        // Now choose what is the Exit Screen	 
        return redirect("/<COULD_NOT_GET_THE_VIEW_FROM_PSL_FOR_SCREEN_createDebt>/" + getDebtAccount(model).getExternalId(),
                model, redirectAttributes);
    }

    //
    // This is the EventcreateExemption Method for Screen read
    //
    @RequestMapping(value = "/read/{oid}/createexemption")
    public String processReadToCreateExemption(@PathVariable("oid") DebtAccount debtAccount, Model model,
            RedirectAttributes redirectAttributes) {
        setDebtAccount(debtAccount, model);
//
        /* Put here the logic for processing Event createExemption 	*/
        //doSomething();

        // Now choose what is the Exit Screen	 
        return redirect("/treasury/document/manageexemption/treasuryexemption/create/" + getDebtAccount(model).getExternalId(),
                model, redirectAttributes);
    }

    //
    // This is the EventreadEvent Method for Screen read
    //
    @RequestMapping(value = "/read/{oid}/readevent")
    public String processReadToReadEvent(@PathVariable("oid") DebtAccount debtAccount, Model model,
            RedirectAttributes redirectAttributes) {
        setDebtAccount(debtAccount, model);
//
        /* Put here the logic for processing Event readEvent  */
        //doSomething();

        // Now choose what is the Exit Screen    
        return redirect(TreasuryEventController.SEARCH_URL + "?debtAccount=" + getDebtAccount(model).getExternalId(), model,
                redirectAttributes);
    }

}
