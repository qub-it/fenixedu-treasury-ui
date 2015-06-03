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
package org.fenixedu.treasury.ui.integration.erp;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.fenixedu.bennu.core.i18n.BundleUtil;
import org.fenixedu.bennu.spring.portal.SpringFunctionality;
import org.fenixedu.treasury.domain.integration.ERPImportOperation;
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

//@Component("org.fenixedu.treasury.ui.integration.erp") <-- Use for duplicate controller name disambiguation
@SpringFunctionality(app = TreasuryController.class, title = "label.title.integration.erp.import", accessGroup = "logged")
// CHANGE_ME accessGroup = "group1 | group2 | groupXPTO"
//or
//@BennuSpringController(value = TreasuryController.class)
@RequestMapping(ERPImportOperationController.CONTROLLER_URL)
public class ERPImportOperationController extends TreasuryBaseController {

    public static final String CONTROLLER_URL = "/treasury/integration/erp/erpimportoperation";

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

    //private ERPImportOperationBean getERPImportOperationBean(Model model)
    //{
    //	return (ERPImportOperationBean)model.asMap().get("eRPImportOperationBean");
    //}
    //				
    //private void setERPImportOperationBean (ERPImportOperationBean bean, Model model)
    //{
    //	model.addAttribute("eRPImportOperationBeanJson", getBeanJson(bean));
    //	model.addAttribute("eRPImportOperationBean", bean);
    //}

    // @formatter: on

    private ERPImportOperation getERPImportOperation(Model model) {
        return (ERPImportOperation) model.asMap().get("eRPImportOperation");
    }

    private void setERPImportOperation(ERPImportOperation eRPImportOperation, Model model) {
        model.addAttribute("eRPImportOperation", eRPImportOperation);
    }

    @Atomic
    public void deleteERPImportOperation(ERPImportOperation eRPImportOperation) {
        // CHANGE_ME: Do the processing for deleting the eRPImportOperation
        // Do not catch any exception here

        // eRPImportOperation.delete();
    }

//				
    private static final String _SEARCH_URI = "/";
    public static final String SEARCH_URL = CONTROLLER_URL + _SEARCH_URI;

    @RequestMapping(value = _SEARCH_URI)
    public String search(@RequestParam(value = "executiondate", required = false) @DateTimeFormat(
            pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSZ") org.joda.time.DateTime executionDate, @RequestParam(value = "success",
            required = false) boolean success, Model model) {
        List<ERPImportOperation> searcherpimportoperationResultsDataSet = filterSearchERPImportOperation(executionDate, success);

        //add the results dataSet to the model
        model.addAttribute("searcherpimportoperationResultsDataSet", searcherpimportoperationResultsDataSet);
        return "treasury/integration/erp/erpimportoperation/search";
    }

    private Stream<ERPImportOperation> getSearchUniverseSearchERPImportOperationDataSet() {
        //
        //The initialization of the result list must be done here
        //
        //
        // return ERPImportOperation.findAll(); //CHANGE_ME
        return new ArrayList<ERPImportOperation>().stream();
    }

    private List<ERPImportOperation> filterSearchERPImportOperation(org.joda.time.DateTime executionDate, boolean success) {

        return getSearchUniverseSearchERPImportOperationDataSet()
                .filter(eRPImportOperation -> executionDate == null
                        || executionDate.equals(eRPImportOperation.getExecutionDate()))
                .filter(eRPImportOperation -> eRPImportOperation.getSuccess() == success).collect(Collectors.toList());
    }

    private static final String _SEARCH_TO_DELETEMULTIPLE_URI = "/search/deletemultiple";
    public static final String SEARCH_TO_DELETEMULTIPLE_URL = CONTROLLER_URL + _SEARCH_TO_DELETEMULTIPLE_URI;

    @RequestMapping(value = _SEARCH_TO_DELETEMULTIPLE_URI)
    public String processSearchToDeleteMultiple(
            @RequestParam("eRPImportOperations") List<ERPImportOperation> eRPImportOperations, Model model,
            RedirectAttributes redirectAttributes) {

        // CHANGE_ME Insert code here for processing deleteMultiple
        // If you selected multiple exists you must choose which one to use below
        return redirect("/treasury/integration/erp/erpimportoperation/", model, redirectAttributes);
    }

    private static final String _SEARCH_TO_VIEW_ACTION_URI = "/search/view/";
    public static final String SEARCH_TO_VIEW_ACTION_URL = CONTROLLER_URL + _SEARCH_TO_VIEW_ACTION_URI;

    @RequestMapping(value = _SEARCH_TO_VIEW_ACTION_URI + "{oid}")
    public String processSearchToViewAction(@PathVariable("oid") ERPImportOperation eRPImportOperation, Model model,
            RedirectAttributes redirectAttributes) {

        // CHANGE_ME Insert code here for processing viewAction
        // If you selected multiple exists you must choose which one to use below	 
        return redirect("/treasury/integration/erp/erpimportoperation/read" + "/" + eRPImportOperation.getExternalId(), model,
                redirectAttributes);
    }

//				
    private static final String _READ_URI = "/read/";
    public static final String READ_URL = CONTROLLER_URL + _READ_URI;

    @RequestMapping(value = _READ_URI + "{oid}")
    public String read(@PathVariable("oid") ERPImportOperation eRPImportOperation, Model model) {
        setERPImportOperation(eRPImportOperation, model);
        return "treasury/integration/erp/erpimportoperation/read";
    }

//
    private static final String _DELETE_URI = "/delete/";
    public static final String DELETE_URL = CONTROLLER_URL + _DELETE_URI;

    @RequestMapping(value = _DELETE_URI + "{oid}", method = RequestMethod.POST)
    public String delete(@PathVariable("oid") ERPImportOperation eRPImportOperation, Model model,
            RedirectAttributes redirectAttributes) {

        setERPImportOperation(eRPImportOperation, model);
        try {
            //call the Atomic delete function
            deleteERPImportOperation(eRPImportOperation);

            addInfoMessage("Sucess deleting ERPImportOperation ...", model);
            return redirect("/treasury/integration/erp/erpimportoperation/", model, redirectAttributes);
        } catch (Exception ex) {
            //Add error messages to the list
            addErrorMessage(BundleUtil.getString(Constants.BUNDLE, "label.error.delete") + ex.getLocalizedMessage(), model);
        }

        //The default mapping is the same Read View
        return "treasury/integration/erp/erpimportoperation/read/" + getERPImportOperation(model).getExternalId();
    }

//

    //
    // This is the EventdownloadFile Method for Screen read
    //
    @RequestMapping(value = "/read/{oid}/downloadfile")
    public String processReadToDownloadFile(@PathVariable("oid") ERPImportOperation eRPImportOperation, Model model,
            RedirectAttributes redirectAttributes) {
        setERPImportOperation(eRPImportOperation, model);
//
        /* Put here the logic for processing Event downloadFile 	*/
        //doSomething();

        // Now choose what is the Exit Screen	 
        return redirect("/treasury/integration/erp/erpimportoperation/read/" + getERPImportOperation(model).getExternalId(),
                model, redirectAttributes);
    }

    //
    // This is the EventretryImport Method for Screen read
    //
    @RequestMapping(value = "/read/{oid}/retryimport")
    public String processReadToRetryImport(@PathVariable("oid") ERPImportOperation eRPImportOperation, Model model,
            RedirectAttributes redirectAttributes) {
        setERPImportOperation(eRPImportOperation, model);
//
        /* Put here the logic for processing Event retryImport 	*/
        //doSomething();

        // Now choose what is the Exit Screen	 
        return redirect("/treasury/integration/erp/erpimportoperation/read/" + getERPImportOperation(model).getExternalId(),
                model, redirectAttributes);
    }

}
