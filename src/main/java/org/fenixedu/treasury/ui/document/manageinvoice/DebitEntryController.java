/**
 * This file was created by Quorum Born IT <http://www.qub-it.com/> and its 
 * copyright terms are bind to the legal agreement regulating the FenixEdu@ULisboa 
 * software development project between Quorum Born IT and Serviços Partilhados da
 * Universidade de Lisboa:
 *  - Copyright © 2015 Quorum Born IT (until any Go-Live phase)
 *  - Copyright © 2015 Universidade de Lisboa (after any Go-Live phase)
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
package org.fenixedu.treasury.ui.document.manageinvoice;

import javax.servlet.http.HttpServletRequest;

import java.util.List;
import java.util.ArrayList;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Stream;

import org.joda.time.DateTime;
import org.joda.time.LocalDate;

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
import org.springframework.web.bind.annotation.ResponseBody;
import org.fenixedu.bennu.spring.portal.BennuSpringController;
import org.fenixedu.bennu.core.domain.exceptions.DomainException;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.fenixedu.bennu.core.domain.Bennu;
import org.fenixedu.bennu.core.i18n.BundleUtil;

import pt.ist.fenixframework.Atomic;

import org.fenixedu.treasury.dto.DebitEntryBean;
import org.fenixedu.treasury.ui.TreasuryBaseController;
import org.fenixedu.treasury.ui.TreasuryController;
import org.fenixedu.treasury.util.Constants;
import org.fenixedu.treasury.domain.Product;
import org.fenixedu.treasury.domain.VatType;
import org.fenixedu.treasury.domain.debt.DebtAccount;
import org.fenixedu.treasury.domain.document.DebitEntry;
import org.fenixedu.treasury.domain.document.DebitNote;
import org.fenixedu.treasury.domain.tariff.FixedTariff;
import org.fenixedu.treasury.domain.tariff.Tariff;

//@Component("org.fenixedu.treasury.ui.document.manageInvoice") <-- Use for duplicate controller name disambiguation
//@SpringFunctionality(app = TreasuryController.class, title = "label.title.document.manageInvoice",accessGroup = "logged")// CHANGE_ME accessGroup = "group1 | group2 | groupXPTO"
//or
@BennuSpringController(value = DebitNoteController.class)
@RequestMapping(DebitEntryController.CONTROLLER_URL)
public class DebitEntryController extends TreasuryBaseController {
    public static final String CONTROLLER_URL = "/treasury/document/manageinvoice/debitentry";
    private static final String SEARCH_URI = "/";
    public static final String SEARCH_URL = CONTROLLER_URL + SEARCH_URI;
    private static final String UPDATE_URI = "/update/";
    public static final String UPDATE_URL = CONTROLLER_URL + UPDATE_URI;
    private static final String CREATE_URI = "/create/";
    public static final String CREATE_URL = CONTROLLER_URL + CREATE_URI;
    private static final String READ_URI = "/read/";
    public static final String READ_URL = CONTROLLER_URL + READ_URI;

//

    @RequestMapping
    public String home(Model model) {
        //this is the default behaviour, for handling in a Spring Functionality
        return "forward:" + DebitNoteController.SEARCH_URL;
    }

    // @formatter: off

    /*
    * This should be used when using AngularJS in the JSP
    */

    private DebitEntryBean getDebitEntryBean(Model model) {
        return (DebitEntryBean) model.asMap().get("debitEntryBean");
    }

    private void setDebitEntryBean(DebitEntryBean bean, Model model) {
        model.addAttribute("debitEntryBeanJson", getBeanJson(bean));
        model.addAttribute("debitEntryBean", bean);
    }

    // @formatter: on

    private DebitEntry getDebitEntry(Model model) {
        return (DebitEntry) model.asMap().get("debitEntry");
    }

    private void setDebitEntry(DebitEntry debitEntry, Model model) {
        model.addAttribute("debitEntry", debitEntry);
    }

    @Atomic
    public void deleteDebitEntry(DebitEntry debitEntry) {
        // CHANGE_ME: Do the processing for deleting the debitEntry
        // Do not catch any exception here

        // debitEntry.delete();
    }

//				
    @RequestMapping(value = CREATE_URI + "{oid}", method = RequestMethod.GET)
    public String create(@PathVariable("oid") DebtAccount debtAccount,
            @RequestParam(value = "debitNote", required = false) DebitNote debitNote, Model model) {

        DebitEntryBean bean = new DebitEntryBean();

        bean.setProductDataSource(Product.findAll().collect(Collectors.toList()));
        bean.setDebtAccount(debtAccount);
        bean.setFinantialDocument(debitNote);
        bean.setCurrency(debtAccount.getFinantialInstitution().getCurrency());
        if (debitNote != null) {
            bean.setDueDate(debitNote.getDocumentDueDate().toLocalDate());
        }
        this.setDebitEntryBean(bean, model);

        return "treasury/document/manageinvoice/debitentry/create";
    }

    // @formatter: off

    @RequestMapping(value = "/createpostback", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public @ResponseBody String createpostback(@RequestParam(value = "bean", required = true) DebitEntryBean bean, Model model) {

        Product product = bean.getProduct();
        if (product != null) {
            FixedTariff tariff =
                    (FixedTariff) product.getActiveTariffs(bean.getDebtAccount().getFinantialInstitution(), new DateTime())
                            .findFirst().orElse(null);

            if (tariff != null) {
                bean.setAmount(tariff.getAmount());
                bean.setDueDate(tariff.calculateDueDate(bean.getFinantialDocument()));
            }
            if (bean.getDescription() == null || bean.getDescription().isEmpty()) {
                bean.setDescription(product.getName().getContent());
            }
        }
        return getBeanJson(bean);
    }

    @RequestMapping(value = CREATE_URI + "{oid}", method = RequestMethod.POST)
    public String create(@RequestParam(value = "bean", required = false) DebitEntryBean bean,
            @PathVariable("oid") DebtAccount debtAccount, Model model, RedirectAttributes redirectAttributes) {

        /*
        *  Creation Logic
        */

        try {

            DebitEntry debitEntry =
                    createDebitEntry(bean.getDebtAccount(), bean.getDescription(), bean.getProduct(), bean.getAmount(),
                            bean.getQuantity(), bean.getDueDate());

            //Success Validation
            //Add the bean to be used in the View
            model.addAttribute("debitEntry", debitEntry);
            return redirect(DebitNoteController.READ_URL + getDebitEntry(model).getExternalId(), model, redirectAttributes);
        } catch (DomainException de) {

            /*
             * If there is any error in validation 
             *
             * Add a error / warning message
             * 
             * addErrorMessage(BundleUtil.getString(TreasurySpringConfiguration.BUNDLE, "label.error.create") + de.getLocalizedMessage(),model);
             * addWarningMessage(" Warning creating due to "+ ex.getLocalizedMessage(),model); */

            addErrorMessage(BundleUtil.getString(Constants.BUNDLE, "label.error.create") + de.getLocalizedMessage(), model);
            return create(bean.getDebtAccount(), bean.getFinantialDocument(), model);
        }
    }

    // @formatter: on

//				

    @Atomic
    public DebitEntry createDebitEntry(DebtAccount debtAccount, java.lang.String description,
            org.fenixedu.treasury.domain.Product product, java.math.BigDecimal amount, java.math.BigDecimal quantity,
            LocalDate dueDate) {

        // @formatter: off

        /*
         * Modify the creation code here if you do not want to create
         * the object with the default constructor and use the setter
         * for each field
         * 
         */

        // CHANGE_ME It's RECOMMENDED to use "Create service" in DomainObject
        //DebitEntry debitEntry = debitEntry.create(fields_to_create);

        //Instead, use individual SETTERS and validate "CheckRules" in the end
        // @formatter: on

        Optional<Tariff> tariff = product.getActiveTariffs(debtAccount.getFinantialInstitution(), new DateTime()).findFirst();
        VatType vatType = tariff.isPresent() ? tariff.get().getVatType() : null;

        DebitEntry debitEntry = DebitEntry.create(debtAccount, null, vatType, amount, dueDate, null, product);
        debitEntry.setDescription(description);
        debitEntry.setAmount(amount);
        debitEntry.setQuantity(quantity);

        debitEntry.checkRules();

        return debitEntry;
    }

//

//               THIS SHOULD BE USED ONLY WHEN USING ANGULAR 
//
    // @formatter: off

    @RequestMapping(value = "/updatepostback/{oid}", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public @ResponseBody String updatepostback(@PathVariable("oid") DebitEntry debitEntry, @RequestParam(value = "bean",
            required = false) DebitEntryBean bean, Model model) {

        // Do validation logic ?!?!
        this.setDebitEntryBean(bean, model);
        return getBeanJson(bean);
    }

//  
    @RequestMapping(value = UPDATE_URI + "{oid}", method = RequestMethod.GET)
    public String update(@PathVariable("oid") DebitEntry debitEntry, Model model) {
        setDebitEntryBean(new DebitEntryBean(debitEntry), model);
        return "treasury/document/manageinvoice/debitentry/update";
    }

    @RequestMapping(value = UPDATE_URI + "{oid}", method = RequestMethod.POST)
    public String update(@PathVariable("oid") DebitEntry debitEntry,
            @RequestParam(value = "bean", required = false) DebitEntryBean bean, Model model,
            RedirectAttributes redirectAttributes) {
        setDebitEntry(debitEntry, model);

        try {
            /*
            *  UpdateLogic here
            */

            updateDebitEntry(bean.getDescription(), bean.getProduct(), bean.getAmount(), bean.getQuantity(), model);

            /*Succes Update */

            return redirect(DebitNoteController.READ_URL + debitEntry.getExternalId(), model, redirectAttributes);
        } catch (DomainException de) {

            /*
            * If there is any error in validation 
            *
            * Add a error / warning message
            * 
            * addErrorMessage(BundleUtil.getString(TreasurySpringConfiguration.BUNDLE, "label.error.update") + de.getLocalizedMessage(),model);
            * addWarningMessage(" Warning updating due to " + de.getLocalizedMessage(),model);
            */

            addErrorMessage(BundleUtil.getString(Constants.BUNDLE, "label.error.update") + de.getLocalizedMessage(), model);
            return update(debitEntry, model);

        }
    }

    @Atomic
    public void updateDebitEntry(java.lang.String description, org.fenixedu.treasury.domain.Product product,
            java.math.BigDecimal amount, java.math.BigDecimal quantity, Model model) {

        // @formatter: off				
        /*
         * Modify the update code here if you do not want to update
         * the object with the default setter for each field
         */

        // CHANGE_ME It's RECOMMENDED to use "Edit service" in DomainObject
        //getDebitEntry(model).edit(fields_to_edit);

        //Instead, use individual SETTERS and validate "CheckRules" in the end
        // @formatter: on

        DebitEntry debitEntry = getDebitEntry(model);
        debitEntry.setDescription(description);
        debitEntry.setProduct(product);
        debitEntry.setAmount(amount);
        debitEntry.setQuantity(quantity);

        debitEntry.checkRules();
    }

}
