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

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.fenixedu.bennu.core.domain.exceptions.DomainException;
import org.fenixedu.bennu.core.i18n.BundleUtil;
import org.fenixedu.bennu.spring.portal.SpringFunctionality;
import org.fenixedu.treasury.domain.document.CreditNote;
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
@SpringFunctionality(app = TreasuryController.class, title = "label.title.document.manageInvoice", accessGroup = "logged")
// CHANGE_ME accessGroup = "group1 | group2 | groupXPTO"
//or
//@BennuSpringController(value = TreasuryController.class)
@RequestMapping(CreditNoteController.CONTROLLER_URL)
public class CreditNoteController extends TreasuryBaseController {

    public static final String CONTROLLER_URL = "/treasury/document/manageinvoice/creditnote";

//

    @RequestMapping
    public String home(Model model) {
        //this is the default behaviour, for handling in a Spring Functionality
        return "forward:" + CONTROLLER_URL + "/";
    }

    // @formatter: off

    /*
    * This should be used when using AngularJS in the JSP
    */

    //private CreditNote getCreditNoteBean(Model model)
    //{
    //	return (CreditNote)model.asMap().get("creditNoteBean");
    //}
    //				
    //private void setCreditNoteBean (CreditNoteBean bean, Model model)
    //{
    //	model.addAttribute("creditNoteBeanJson", getBeanJson(bean));
    //	model.addAttribute("creditNoteBean", bean);
    //}

    // @formatter: on

    private CreditNote getCreditNote(Model model) {
        return (CreditNote) model.asMap().get("creditNote");
    }

    private void setCreditNote(CreditNote creditNote, Model model) {
        model.addAttribute("creditNote", creditNote);
    }

    @Atomic
    public void deleteCreditNote(CreditNote creditNote) {
        // CHANGE_ME: Do the processing for deleting the creditNote
        // Do not catch any exception here

        // creditNote.delete();
    }

//				
    private static final String _READ_URI = "/read/";
    public static final String READ_URL = CONTROLLER_URL + _READ_URI;

    @RequestMapping(value = _READ_URI + "{oid}")
    public String read(@PathVariable("oid") CreditNote creditNote, Model model) {
        setCreditNote(creditNote, model);
        return "treasury/document/manageinvoice/creditnote/read";
    }

//

    //
    // This is the EventcloseCreditNote Method for Screen read
    //
    @RequestMapping(value = "/read/{oid}/closecreditnote")
    public String processReadToCloseCreditNote(@PathVariable("oid") CreditNote creditNote, Model model,
            RedirectAttributes redirectAttributes) {
        setCreditNote(creditNote, model);
//
        /* Put here the logic for processing Event closeCreditNote 	*/
        //doSomething();

        // Now choose what is the Exit Screen	 
        return redirect("/treasury/document/manageinvoice/creditnote/read/" + getCreditNote(model).getExternalId(), model,
                redirectAttributes);
    }

    //
    // This is the EventanullCreditNote Method for Screen read
    //
    @RequestMapping(value = "/read/{oid}/anullcreditnote")
    public String processReadToAnullCreditNote(@PathVariable("oid") CreditNote creditNote, Model model,
            RedirectAttributes redirectAttributes) {
        setCreditNote(creditNote, model);
//
        /* Put here the logic for processing Event anullCreditNote 	*/
        //doSomething();

        // Now choose what is the Exit Screen	 
        return redirect("/treasury/document/manageinvoice/creditnote/read/" + getCreditNote(model).getExternalId(), model,
                redirectAttributes);
    }

    //
    // This is the EventaddEntry Method for Screen read
    //
    @RequestMapping(value = "/read/{oid}/addentry")
    public String processReadToAddEntry(@PathVariable("oid") CreditNote creditNote, Model model,
            RedirectAttributes redirectAttributes) {
        setCreditNote(creditNote, model);
//
        /* Put here the logic for processing Event addEntry 	*/
        //doSomething();

        // Now choose what is the Exit Screen	 
        return redirect("/treasury/document/manageinvoice/creditentry/create/" + getCreditNote(model).getExternalId(), model,
                redirectAttributes);
    }

//				
    private static final String _UPDATE_URI = "/update/";
    public static final String UPDATE_URL = CONTROLLER_URL + _UPDATE_URI;

    @RequestMapping(value = _UPDATE_URI + "{oid}", method = RequestMethod.GET)
    public String update(@PathVariable("oid") CreditNote creditNote, Model model) {
        model.addAttribute("stateValues", org.fenixedu.treasury.domain.document.FinantialDocumentStateType.values());
        setCreditNote(creditNote, model);
        return "treasury/document/manageinvoice/creditnote/update";
    }

//

//               THIS SHOULD BE USED ONLY WHEN USING ANGULAR 
//
//						// @formatter: off
//			
//				private static final String _UPDATEPOSTBACK_URI ="/updatepostback/";
//				public static final String  UPDATEPOSTBACK_URL = CONTROLLER_URL + _updatePOSTBACK_URI;
//    			@RequestMapping(value = _UPDATEPOSTBACK_URI + "{oid}", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
//  			  	public @ResponseBody String updatepostback(@PathVariable("oid") CreditNote creditNote, @RequestParam(value = "bean", required = false) CreditNoteBean bean,
//            		Model model) {
//
//        			// Do validation logic ?!?!
//        			this.setCreditNoteBean(bean, model);
//        			return getBeanJson(bean);
//    			} 
//    			
//    			@RequestMapping(value = _UPDATE_URI + "{oid}", method = RequestMethod.POST)
//  			  	public String update(@PathVariable("oid") CreditNote creditNote, @RequestParam(value = "bean", required = false) CreditNoteBean bean,
//            		Model model, RedirectAttributes redirectAttributes ) {
//					setCreditNote(creditNote,model);
//
//				     try
//				     {
//					/*
//					*  UpdateLogic here
//					*/
//				    		
//						updateCreditNote( .. get fields from bean..., model);
//
//					/*Succes Update */
//
//				    return redirect("/treasury/document/manageinvoice/creditnote/read/" + getCreditNote(model).getExternalId(), model, redirectAttributes);
//					}
//					catch (DomainException de) 
//					{
//				
//						/*
//					 	* If there is any error in validation 
//				     	*
//				     	* Add a error / warning message
//				     	* 
//				     	* addErrorMessage(BundleUtil.getString(TreasurySpringConfiguration.BUNDLE, "label.error.update") + de.getLocalizedMessage(),model);
//				     	* addWarningMessage(" Warning updating due to " + de.getLocalizedMessage(),model);
//				     	*/
//										     
//				     	addErrorMessage(BundleUtil.getString(TreasurySpringConfiguration.BUNDLE, "label.error.update") + de.getLocalizedMessage(),model);
//				     	return update(creditNote,model);
//					 
//
//					}
//				}
//						// @formatter: on    			
//				
    @RequestMapping(value = _UPDATE_URI + "{oid}", method = RequestMethod.POST)
    public String update(
            @PathVariable("oid") CreditNote creditNote,
            @RequestParam(value = "origindocumentnumber", required = false) java.lang.String originDocumentNumber,
            @RequestParam(value = "state", required = false) org.fenixedu.treasury.domain.document.FinantialDocumentStateType state,
            Model model, RedirectAttributes redirectAttributes) {

        setCreditNote(creditNote, model);

        try {
            /*
            *  UpdateLogic here
            */

            updateCreditNote(originDocumentNumber, state, model);

            /*Succes Update */

            return redirect("/treasury/document/manageinvoice/creditnote/read/" + getCreditNote(model).getExternalId(), model,
                    redirectAttributes);
        } catch (DomainException de) {
            // @formatter: off

            /*
            * If there is any error in validation 
            *
            * Add a error / warning message
            * 
            * addErrorMessage(BundleUtil.getString(TreasurySpringConfiguration.BUNDLE, "label.error.update") + de.getLocalizedMessage(),model);
            * addWarningMessage(" Warning updating due to " + de.getLocalizedMessage(),model);
            */
            // @formatter: on

            addErrorMessage(BundleUtil.getString(Constants.BUNDLE, "label.error.update") + de.getLocalizedMessage(), model);
            return update(creditNote, model);

        }
    }

    @Atomic
    public void updateCreditNote(java.lang.String originDocumentNumber,
            org.fenixedu.treasury.domain.document.FinantialDocumentStateType state, Model model) {

        // @formatter: off				
        /*
         * Modify the update code here if you do not want to update
         * the object with the default setter for each field
         */

        // CHANGE_ME It's RECOMMENDED to use "Edit service" in DomainObject
        //getCreditNote(model).edit(fields_to_edit);

        //Instead, use individual SETTERS and validate "CheckRules" in the end
        // @formatter: on

        getCreditNote(model).setOriginDocumentNumber(originDocumentNumber);
        getCreditNote(model).setState(state);
    }

//				
    private static final String _SEARCH_URI = "/";
    public static final String SEARCH_URL = CONTROLLER_URL + _SEARCH_URI;

    @RequestMapping(value = _SEARCH_URI)
    public String search(
            @RequestParam(value = "debitnote", required = false) org.fenixedu.treasury.domain.document.DebitNote debitNote,
            @RequestParam(value = "payordebtaccount", required = false) org.fenixedu.treasury.domain.debt.DebtAccount payorDebtAccount,
            @RequestParam(value = "finantialdocumenttype", required = false) org.fenixedu.treasury.domain.document.FinantialDocumentType finantialDocumentType,
            @RequestParam(value = "debtaccount", required = false) org.fenixedu.treasury.domain.debt.DebtAccount debtAccount,
            @RequestParam(value = "documentnumberseries", required = false) org.fenixedu.treasury.domain.document.DocumentNumberSeries documentNumberSeries,
            @RequestParam(value = "currency", required = false) org.fenixedu.treasury.domain.Currency currency,
            @RequestParam(value = "documentnumber", required = false) java.lang.String documentNumber,
            @RequestParam(value = "documentdate", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSZ") org.joda.time.DateTime documentDate,
            @RequestParam(value = "documentduedate", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSZ") org.joda.time.DateTime documentDueDate,
            @RequestParam(value = "origindocumentnumber", required = false) java.lang.String originDocumentNumber, @RequestParam(
                    value = "state", required = false) org.fenixedu.treasury.domain.document.FinantialDocumentStateType state,
            Model model) {
        List<CreditNote> searchcreditnoteResultsDataSet =
                filterSearchCreditNote(debitNote, payorDebtAccount, finantialDocumentType, debtAccount, documentNumberSeries,
                        currency, documentNumber, documentDate, documentDueDate, originDocumentNumber, state);

        //add the results dataSet to the model
        model.addAttribute("searchcreditnoteResultsDataSet", searchcreditnoteResultsDataSet);
        model.addAttribute("CreditNote_debitNote_options", new ArrayList<org.fenixedu.treasury.domain.document.DebitNote>()); // CHANGE_ME - MUST DEFINE RELATION
        //model.addAttribute("CreditNote_debitNote_options", org.fenixedu.treasury.domain.document.DebitNote.findAll()); // CHANGE_ME - MUST DEFINE RELATION
        model.addAttribute("CreditNote_payorDebtAccount_options", new ArrayList<org.fenixedu.treasury.domain.debt.DebtAccount>()); // CHANGE_ME - MUST DEFINE RELATION
        //model.addAttribute("CreditNote_payorDebtAccount_options", org.fenixedu.treasury.domain.debt.DebtAccount.findAll()); // CHANGE_ME - MUST DEFINE RELATION
        model.addAttribute("CreditNote_finantialDocumentType_options",
                new ArrayList<org.fenixedu.treasury.domain.document.FinantialDocumentType>()); // CHANGE_ME - MUST DEFINE RELATION
        //model.addAttribute("CreditNote_finantialDocumentType_options", org.fenixedu.treasury.domain.document.FinantialDocumentType.findAll()); // CHANGE_ME - MUST DEFINE RELATION
        model.addAttribute("CreditNote_debtAccount_options", new ArrayList<org.fenixedu.treasury.domain.debt.DebtAccount>()); // CHANGE_ME - MUST DEFINE RELATION
        //model.addAttribute("CreditNote_debtAccount_options", org.fenixedu.treasury.domain.debt.DebtAccount.findAll()); // CHANGE_ME - MUST DEFINE RELATION
        model.addAttribute("CreditNote_documentNumberSeries_options",
                new ArrayList<org.fenixedu.treasury.domain.document.DocumentNumberSeries>()); // CHANGE_ME - MUST DEFINE RELATION
        //model.addAttribute("CreditNote_documentNumberSeries_options", org.fenixedu.treasury.domain.document.DocumentNumberSeries.findAll()); // CHANGE_ME - MUST DEFINE RELATION
        model.addAttribute("CreditNote_currency_options", new ArrayList<org.fenixedu.treasury.domain.Currency>()); // CHANGE_ME - MUST DEFINE RELATION
        //model.addAttribute("CreditNote_currency_options", org.fenixedu.treasury.domain.Currency.findAll()); // CHANGE_ME - MUST DEFINE RELATION
        model.addAttribute("stateValues", org.fenixedu.treasury.domain.document.FinantialDocumentStateType.values());
        return "treasury/document/manageinvoice/creditnote/search";
    }

    private Stream<CreditNote> getSearchUniverseSearchCreditNoteDataSet() {
        //
        //The initialization of the result list must be done here
        //
        //
        // return CreditNote.findAll(); //CHANGE_ME
        return new ArrayList<CreditNote>().stream();
    }

    private List<CreditNote> filterSearchCreditNote(org.fenixedu.treasury.domain.document.DebitNote debitNote,
            org.fenixedu.treasury.domain.debt.DebtAccount payorDebtAccount,
            org.fenixedu.treasury.domain.document.FinantialDocumentType finantialDocumentType,
            org.fenixedu.treasury.domain.debt.DebtAccount debtAccount,
            org.fenixedu.treasury.domain.document.DocumentNumberSeries documentNumberSeries,
            org.fenixedu.treasury.domain.Currency currency, java.lang.String documentNumber, org.joda.time.DateTime documentDate,
            org.joda.time.DateTime documentDueDate, java.lang.String originDocumentNumber,
            org.fenixedu.treasury.domain.document.FinantialDocumentStateType state) {

        return getSearchUniverseSearchCreditNoteDataSet()
                .filter(creditNote -> debitNote == null || debitNote == creditNote.getDebitNote())
                .filter(creditNote -> payorDebtAccount == null || payorDebtAccount == creditNote.getPayorDebtAccount())
                .filter(creditNote -> finantialDocumentType == null
                        || finantialDocumentType == creditNote.getFinantialDocumentType())
                .filter(creditNote -> debtAccount == null || debtAccount == creditNote.getDebtAccount())
                .filter(creditNote -> documentNumberSeries == null
                        || documentNumberSeries == creditNote.getDocumentNumberSeries())
                .filter(creditNote -> currency == null || currency == creditNote.getCurrency())
                .filter(creditNote -> documentNumber == null || documentNumber.length() == 0
                        || creditNote.getDocumentNumber() != null && creditNote.getDocumentNumber().length() > 0
                        && creditNote.getDocumentNumber().toLowerCase().contains(documentNumber.toLowerCase()))
                .filter(creditNote -> documentDate == null || documentDate.equals(creditNote.getDocumentDate()))
                .filter(creditNote -> documentDueDate == null || documentDueDate.equals(creditNote.getDocumentDueDate()))
                .filter(creditNote -> originDocumentNumber == null || originDocumentNumber.length() == 0
                        || creditNote.getOriginDocumentNumber() != null && creditNote.getOriginDocumentNumber().length() > 0
                        && creditNote.getOriginDocumentNumber().toLowerCase().contains(originDocumentNumber.toLowerCase()))
                .filter(creditNote -> state == null || state.equals(creditNote.getState())).collect(Collectors.toList());
    }

    private static final String _SEARCH_TO_VIEW_ACTION_URI = "/search/view/";
    public static final String SEARCH_TO_VIEW_ACTION_URL = CONTROLLER_URL + _SEARCH_TO_VIEW_ACTION_URI;

    @RequestMapping(value = _SEARCH_TO_VIEW_ACTION_URI + "{oid}")
    public String processSearchToViewAction(@PathVariable("oid") CreditNote creditNote, Model model,
            RedirectAttributes redirectAttributes) {

        // CHANGE_ME Insert code here for processing viewAction
        // If you selected multiple exists you must choose which one to use below	 
        return redirect("/treasury/document/manageinvoice/creditnote/read" + "/" + creditNote.getExternalId(), model,
                redirectAttributes);
    }

//				
    private static final String _CREATE_URI = "/create";
    public static final String CREATE_URL = CONTROLLER_URL + _CREATE_URI;

    @RequestMapping(value = _CREATE_URI, method = RequestMethod.GET)
    public String create(Model model) {
        model.addAttribute("CreditNote_debitNote_options", new ArrayList<org.fenixedu.treasury.domain.document.DebitNote>()); // CHANGE_ME - MUST DEFINE RELATION
        //model.addAttribute("CreditNote_debitNote_options", org.fenixedu.treasury.domain.document.DebitNote.findAll()); // CHANGE_ME - MUST DEFINE RELATION
        model.addAttribute("CreditNote_payorDebtAccount_options", new ArrayList<org.fenixedu.treasury.domain.debt.DebtAccount>()); // CHANGE_ME - MUST DEFINE RELATION
        //model.addAttribute("CreditNote_payorDebtAccount_options", org.fenixedu.treasury.domain.debt.DebtAccount.findAll()); // CHANGE_ME - MUST DEFINE RELATION
        model.addAttribute("CreditNote_finantialDocumentType_options",
                new ArrayList<org.fenixedu.treasury.domain.document.FinantialDocumentType>()); // CHANGE_ME - MUST DEFINE RELATION
        //model.addAttribute("CreditNote_finantialDocumentType_options", org.fenixedu.treasury.domain.document.FinantialDocumentType.findAll()); // CHANGE_ME - MUST DEFINE RELATION
        model.addAttribute("CreditNote_debtAccount_options", new ArrayList<org.fenixedu.treasury.domain.debt.DebtAccount>()); // CHANGE_ME - MUST DEFINE RELATION
        //model.addAttribute("CreditNote_debtAccount_options", org.fenixedu.treasury.domain.debt.DebtAccount.findAll()); // CHANGE_ME - MUST DEFINE RELATION
        model.addAttribute("CreditNote_documentNumberSeries_options",
                new ArrayList<org.fenixedu.treasury.domain.document.DocumentNumberSeries>()); // CHANGE_ME - MUST DEFINE RELATION
        //model.addAttribute("CreditNote_documentNumberSeries_options", org.fenixedu.treasury.domain.document.DocumentNumberSeries.findAll()); // CHANGE_ME - MUST DEFINE RELATION
        model.addAttribute("CreditNote_currency_options", new ArrayList<org.fenixedu.treasury.domain.Currency>()); // CHANGE_ME - MUST DEFINE RELATION
        //model.addAttribute("CreditNote_currency_options", org.fenixedu.treasury.domain.Currency.findAll()); // CHANGE_ME - MUST DEFINE RELATION
        model.addAttribute("stateValues", org.fenixedu.treasury.domain.document.FinantialDocumentStateType.values());

        //IF ANGULAR, initialize the Bean
        //CreditNoteBean bean = new CreditNoteBean();
        //this.setCreditNoteBean(bean, model);

        return "treasury/document/manageinvoice/creditnote/create";
    }

//
//               THIS SHOULD BE USED ONLY WHEN USING ANGULAR 
//
//						// @formatter: off
//			
//				private static final String _CREATEPOSTBACK_URI ="/createpostback";
//				public static final String  CREATEPOSTBACK_URL = CONTROLLER_URL + _createPOSTBACK_URI;
//    			@RequestMapping(value = _CREATEPOSTBACK_URI, method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
//  			  	public @ResponseBody String createpostback(@RequestParam(value = "bean", required = false) CreditNoteBean bean,
//            		Model model) {
//
//        			// Do validation logic ?!?!
//        			this.setCreditNoteBean(bean, model);
//        			return getBeanJson(bean);
//    			}
//    			
//    			@RequestMapping(value = CREATE, method = RequestMethod.POST)
//  			  	public String create(@RequestParam(value = "bean", required = false) CreditNoteBean bean,
//            		Model model, RedirectAttributes redirectAttributes ) {
//
//					/*
//					*  Creation Logic
//					*/
//					
//					try
//					{
//
//				     	CreditNote creditNote = createCreditNote(... get properties from bean ...,model);
//				    	
//					//Success Validation
//				     //Add the bean to be used in the View
//					model.addAttribute("creditNote",creditNote);
//				    return redirect("/treasury/document/manageinvoice/creditnote/read/" + getCreditNote(model).getExternalId(), model, redirectAttributes);
//					}
//					catch (DomainException de)
//					{
//
//						/*
//						 * If there is any error in validation 
//					     *
//					     * Add a error / warning message
//					     * 
//					     * addErrorMessage(BundleUtil.getString(TreasurySpringConfiguration.BUNDLE, "label.error.create") + de.getLocalizedMessage(),model);
//					     * addWarningMessage(" Warning creating due to "+ ex.getLocalizedMessage(),model); */
//						
//						addErrorMessage(BundleUtil.getString(TreasurySpringConfiguration.BUNDLE, "label.error.create") + de.getLocalizedMessage(),model);
//				     	return create(model);
//					}
//    			}
//						// @formatter: on

//				
    @RequestMapping(value = _CREATE_URI, method = RequestMethod.POST)
    public String create(
            @RequestParam(value = "debitnote", required = false) org.fenixedu.treasury.domain.document.DebitNote debitNote,
            @RequestParam(value = "payordebtaccount", required = false) org.fenixedu.treasury.domain.debt.DebtAccount payorDebtAccount,
            @RequestParam(value = "finantialdocumenttype", required = false) org.fenixedu.treasury.domain.document.FinantialDocumentType finantialDocumentType,
            @RequestParam(value = "debtaccount", required = false) org.fenixedu.treasury.domain.debt.DebtAccount debtAccount,
            @RequestParam(value = "documentnumberseries", required = false) org.fenixedu.treasury.domain.document.DocumentNumberSeries documentNumberSeries,
            @RequestParam(value = "currency", required = false) org.fenixedu.treasury.domain.Currency currency,
            @RequestParam(value = "documentnumber", required = false) java.lang.String documentNumber,
            @RequestParam(value = "documentdate", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSZ") org.joda.time.DateTime documentDate,
            @RequestParam(value = "origindocumentnumber", required = false) java.lang.String originDocumentNumber, @RequestParam(
                    value = "state", required = false) org.fenixedu.treasury.domain.document.FinantialDocumentStateType state,
            Model model, RedirectAttributes redirectAttributes) {
        /*
        *  Creation Logic
        */

        try {

            CreditNote creditNote =
                    createCreditNote(debitNote, payorDebtAccount, finantialDocumentType, debtAccount, documentNumberSeries,
                            currency, documentNumber, documentDate, originDocumentNumber, state);

            //Success Validation
            //Add the bean to be used in the View
            model.addAttribute("creditNote", creditNote);
            return redirect("/treasury/document/manageinvoice/creditnote/read/" + getCreditNote(model).getExternalId(), model,
                    redirectAttributes);
        } catch (DomainException de) {

            // @formatter: off
            /*
             * If there is any error in validation 
             *
             * Add a error / warning message
             * 
             * addErrorMessage(BundleUtil.getString(TreasurySpringConfiguration.BUNDLE, "label.error.create") + de.getLocalizedMessage(),model);
             * addWarningMessage(" Warning creating due to "+ ex.getLocalizedMessage(),model); */
            // @formatter: on

            addErrorMessage(BundleUtil.getString(Constants.BUNDLE, "label.error.create") + de.getLocalizedMessage(), model);
            return create(model);
        }
    }

    @Atomic
    public CreditNote createCreditNote(org.fenixedu.treasury.domain.document.DebitNote debitNote,
            org.fenixedu.treasury.domain.debt.DebtAccount payorDebtAccount,
            org.fenixedu.treasury.domain.document.FinantialDocumentType finantialDocumentType,
            org.fenixedu.treasury.domain.debt.DebtAccount debtAccount,
            org.fenixedu.treasury.domain.document.DocumentNumberSeries documentNumberSeries,
            org.fenixedu.treasury.domain.Currency currency, java.lang.String documentNumber, org.joda.time.DateTime documentDate,
            java.lang.String originDocumentNumber, org.fenixedu.treasury.domain.document.FinantialDocumentStateType state) {

        // @formatter: off

        /*
         * Modify the creation code here if you do not want to create
         * the object with the default constructor and use the setter
         * for each field
         * 
         */

        // CHANGE_ME It's RECOMMENDED to use "Create service" in DomainObject
        //CreditNote creditNote = creditNote.create(fields_to_create);

        //Instead, use individual SETTERS and validate "CheckRules" in the end
        // @formatter: on

        CreditNote creditNote = CreditNote.create(debtAccount, documentNumberSeries, documentDate);
        creditNote.setDebitNote(debitNote);
        creditNote.setPayorDebtAccount(payorDebtAccount);
        creditNote.setFinantialDocumentType(finantialDocumentType);
        creditNote.setDebtAccount(debtAccount);
        creditNote.setDocumentNumberSeries(documentNumberSeries);
        creditNote.setCurrency(currency);
        creditNote.setDocumentNumber(documentNumber);
        creditNote.setDocumentDate(documentDate);
        creditNote.setOriginDocumentNumber(originDocumentNumber);
        creditNote.setState(state);

        return creditNote;
    }
}
