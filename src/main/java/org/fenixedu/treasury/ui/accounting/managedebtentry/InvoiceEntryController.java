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
package org.fenixedu.treasury.ui.accounting.managedebtentry;

import javax.servlet.http.HttpServletRequest;

import java.util.List;
import java.util.ArrayList;

import org.joda.time.DateTime;

import java.util.stream.Collectors;

import org.fenixedu.bennu.spring.portal.SpringApplication;
import org.fenixedu.bennu.spring.portal.SpringFunctionality;
import org.springframework.stereotype.Component;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.fenixedu.bennu.spring.portal.BennuSpringController;
import org.fenixedu.bennu.core.domain.exceptions.DomainException;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.fenixedu.bennu.core.domain.Bennu;

import pt.ist.fenixframework.Atomic;

import org.fenixedu.treasury.ui.TreasuryBaseController;
import org.fenixedu.treasury.ui.TreasuryController;
import org.fenixedu.treasury.domain.document.InvoiceEntry;

//@Component("org.fenixedu.treasury.ui.accounting.manageDebtEntry") <-- Use for duplicate controller name disambiguation
@SpringFunctionality(app = TreasuryController.class, title = "label.title.accounting.manageDebtEntry", accessGroup = "#managers")
// CHANGE_ME accessGroup = "group1 | group2 | groupXPTO"
// or
// @BennuSpringController(value=TreasuryController.class)
@RequestMapping(InvoiceEntryController.CONTROLLER_URL)
public class InvoiceEntryController extends TreasuryBaseController {
    public static final String CONTROLLER_URL = "/treasury/accounting/managedebtentry/invoiceentry";

    //

    @RequestMapping
    public String home(Model model) {
        // this is the default behaviour, for handling in a Spring Functionality
        return "forward:/treasury/accounting/managedebtentry/invoiceentry/";
    }

    private InvoiceEntry getInvoiceEntry(Model model) {
        return (InvoiceEntry) model.asMap().get("invoiceEntry");
    }

    private void setInvoiceEntry(InvoiceEntry invoiceEntry, Model model) {
        model.addAttribute("invoiceEntry", invoiceEntry);
    }

    @Atomic
    public void deleteInvoiceEntry(InvoiceEntry invoiceEntry) {
        // CHANGE_ME: Do the processing for deleting the invoiceEntry
        // Do not catch any exception here

        // invoiceEntry.delete();
    }

    //
    @RequestMapping(value = "/")
    public String search(Model model) {
        List<InvoiceEntry> searchinvoiceentryResultsDataSet = filterSearchInvoiceEntry();

        // add the results dataSet to the model
        model.addAttribute("searchinvoiceentryResultsDataSet", searchinvoiceentryResultsDataSet);
        return "treasury/accounting/managedebtentry/invoiceentry/search";
    }

    private List<InvoiceEntry> getSearchUniverseSearchInvoiceEntryDataSet() {
        //
        // The initialization of the result list must be done here
        //
        //
        // return new
        // ArrayList<InvoiceEntry>(InvoiceEntry.findAll().collect(Collectors.toList()));
        // //CHANGE_ME
        return new ArrayList<InvoiceEntry>();
    }

    private List<InvoiceEntry> filterSearchInvoiceEntry() {

        return getSearchUniverseSearchInvoiceEntryDataSet().stream().collect(Collectors.toList());
    }

    @RequestMapping(value = "/search/view/{oid}")
    public String processSearchToViewAction(@PathVariable("oid") InvoiceEntry invoiceEntry, Model model,
            RedirectAttributes redirectAttributes) {

        // CHANGE_ME Insert code here for processing viewAction
        // If you selected multiple exists you must choose which one to use
        // below

        return redirect("/treasury/document/manageinvoice/debitnote/read" + "/" + invoiceEntry.getExternalId(), model,
                redirectAttributes);

        // return redirect("/treasury/document/manageinvoice/creditnote/read" +
        // "/" + invoiceEntry.getExternalId(), model, redirectAttributes);
    }

}
