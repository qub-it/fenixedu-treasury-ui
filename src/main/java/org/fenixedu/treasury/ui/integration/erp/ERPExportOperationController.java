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
package org.fenixedu.treasury.ui.integration.erp;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.servlet.http.HttpServletResponse;

import org.fenixedu.bennu.core.i18n.BundleUtil;
import org.fenixedu.bennu.spring.portal.SpringFunctionality;
import org.fenixedu.treasury.domain.integration.ERPExportOperation;
import org.fenixedu.treasury.services.integration.erp.ERPExporter;
import org.fenixedu.treasury.ui.TreasuryBaseController;
import org.fenixedu.treasury.ui.TreasuryController;
import org.fenixedu.treasury.util.Constants;
import org.joda.time.DateTime;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import pt.ist.fenixframework.Atomic;

//@Component("org.fenixedu.treasury.ui.integration.erp") <-- Use for duplicate controller name disambiguation
@SpringFunctionality(app = TreasuryController.class, title = "label.title.integration.erp.export", accessGroup = "#managers")
// CHANGE_ME accessGroup = "group1 | group2 | groupXPTO"
//or
//@BennuSpringController(value = TreasuryController.class)
@RequestMapping(ERPExportOperationController.CONTROLLER_URL)
public class ERPExportOperationController extends TreasuryBaseController {

    public static final String CONTROLLER_URL = "/treasury/integration/erp/erpexportoperation";

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

    //private ERPExportOperationBean getERPExportOperationBean(Model model)
    //{
    //	return (ERPExportOperationBean)model.asMap().get("eRPExportOperationBean");
    //}
    //				
    //private void setERPExportOperationBean (ERPExportOperationBean bean, Model model)
    //{
    //	model.addAttribute("eRPExportOperationBeanJson", getBeanJson(bean));
    //	model.addAttribute("eRPExportOperationBean", bean);
    //}

    // @formatter: on

    private ERPExportOperation getERPExportOperation(Model model) {
        return (ERPExportOperation) model.asMap().get("eRPExportOperation");
    }

    private void setERPExportOperation(ERPExportOperation eRPExportOperation, Model model) {
        model.addAttribute("eRPExportOperation", eRPExportOperation);
    }

    @Atomic
    public void deleteERPExportOperation(ERPExportOperation eRPExportOperation) {
        // CHANGE_ME: Do the processing for deleting the eRPExportOperation
        // Do not catch any exception here

        eRPExportOperation.delete();
    }

//				
    private static final String _SEARCH_URI = "/";
    public static final String SEARCH_URL = CONTROLLER_URL + _SEARCH_URI;

    @RequestMapping(value = _SEARCH_URI)
    public String search(
            @RequestParam(value = "fromexecutiondate", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") org.joda.time.DateTime fromExecutionDate,
            @RequestParam(value = "toexecutiondate", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") org.joda.time.DateTime toExecutionDate,
            @RequestParam(value = "success", required = false) boolean success, Model model) {
        List<ERPExportOperation> searcherpexportoperationResultsDataSet =
                filterSearchERPExportOperation(fromExecutionDate, toExecutionDate, success);

        //add the results dataSet to the model
        model.addAttribute("searcherpexportoperationResultsDataSet", searcherpexportoperationResultsDataSet);
        return "treasury/integration/erp/erpexportoperation/search";
    }

    private Stream<ERPExportOperation> getSearchUniverseSearchERPExportOperationDataSet() {
        //
        //The initialization of the result list must be done here
        //
        //
        return ERPExportOperation.findAll();

    }

    private List<ERPExportOperation> filterSearchERPExportOperation(DateTime fromExecutionDate, DateTime toExecutionDate,
            boolean success) {

        return getSearchUniverseSearchERPExportOperationDataSet()
                .filter(eRPExportOperation -> fromExecutionDate == null || toExecutionDate == null
                        || eRPExportOperation.getExecutionDate().isAfter(fromExecutionDate)
                        && eRPExportOperation.getExecutionDate().isBefore(toExecutionDate))
                .filter(eRPExportOperation -> eRPExportOperation.getSuccess() == success).collect(Collectors.toList());
    }

    private static final String _SEARCH_TO_DELETEMULTIPLE_URI = "/search/deletemultiple";
    public static final String SEARCH_TO_DELETEMULTIPLE_URL = CONTROLLER_URL + _SEARCH_TO_DELETEMULTIPLE_URI;

    @RequestMapping(value = _SEARCH_TO_DELETEMULTIPLE_URI)
    public String processSearchToDeleteMultiple(
            @RequestParam("eRPExportOperations") List<ERPExportOperation> eRPExportOperations, Model model,
            RedirectAttributes redirectAttributes) {

        // CHANGE_ME Insert code here for processing deleteMultiple
        // If you selected multiple exists you must choose which one to use below
        return redirect("/treasury/integration/erp/erpexportoperation/", model, redirectAttributes);
    }

    private static final String _SEARCH_TO_VIEW_ACTION_URI = "/search/view/";
    public static final String SEARCH_TO_VIEW_ACTION_URL = CONTROLLER_URL + _SEARCH_TO_VIEW_ACTION_URI;

    @RequestMapping(value = _SEARCH_TO_VIEW_ACTION_URI + "{oid}")
    public String processSearchToViewAction(@PathVariable("oid") ERPExportOperation eRPExportOperation, Model model,
            RedirectAttributes redirectAttributes) {

        // CHANGE_ME Insert code here for processing viewAction
        // If you selected multiple exists you must choose which one to use below	 
        return redirect("/treasury/integration/erp/erpexportoperation/read" + "/" + eRPExportOperation.getExternalId(), model,
                redirectAttributes);
    }

//				
    private static final String _READ_URI = "/read/";
    public static final String READ_URL = CONTROLLER_URL + _READ_URI;

    @RequestMapping(value = _READ_URI + "{oid}")
    public String read(@PathVariable("oid") ERPExportOperation eRPExportOperation, Model model) {
        setERPExportOperation(eRPExportOperation, model);
        return "treasury/integration/erp/erpexportoperation/read";
    }

//
    private static final String _DELETE_URI = "/delete/";
    public static final String DELETE_URL = CONTROLLER_URL + _DELETE_URI;

    @RequestMapping(value = _DELETE_URI + "{oid}", method = RequestMethod.POST)
    public String delete(@PathVariable("oid") ERPExportOperation eRPExportOperation, Model model,
            RedirectAttributes redirectAttributes) {

        setERPExportOperation(eRPExportOperation, model);
        try {
            //call the Atomic delete function
            deleteERPExportOperation(eRPExportOperation);

            addInfoMessage("Sucess deleting ERPExportOperation ...", model);
            return redirect("/treasury/integration/erp/erpexportoperation/", model, redirectAttributes);
        } catch (Exception ex) {
            //Add error messages to the list
            addErrorMessage(BundleUtil.getString(Constants.BUNDLE, "label.error.delete") + ex.getLocalizedMessage(), model);
        }

        //The default mapping is the same Read View
        return "treasury/integration/erp/erpexportoperation/read/" + getERPExportOperation(model).getExternalId();
    }

//

    //
    // This is the EventdownloadFile Method for Screen read
    //
    @RequestMapping(value = "/read/{oid}/downloadfile")
    public void processReadToDownloadFile(@PathVariable("oid") ERPExportOperation eRPExportOperation, Model model,
            RedirectAttributes redirectAttributes, HttpServletResponse response) {
        setERPExportOperation(eRPExportOperation, model);
        try {
            response.setContentType(eRPExportOperation.getFile().getContentType());
            String filename = eRPExportOperation.getFile().getFilename();
            response.setHeader("Content-disposition", "attachment; filename=" + filename);
            response.getOutputStream().write(eRPExportOperation.getFile().getContent());
        } catch (Exception ex) {
            addErrorMessage(ex.getLocalizedMessage(), model);
            try {
                response.sendRedirect(redirect(READ_URL + getERPExportOperation(model).getExternalId(), model, redirectAttributes));
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }

    //
    // This is the EventretryImport Method for Screen read
    //
    @RequestMapping(value = "/read/{oid}/retryimport")
    public String processReadToRetryImport(@PathVariable("oid") ERPExportOperation eRPExportOperation, Model model,
            RedirectAttributes redirectAttributes) {
        setERPExportOperation(eRPExportOperation, model);
        //
        /* Put here the logic for processing Event retryImport  */

        try {
            ERPExportOperation retryExportOperation = ERPExporter.retryExportToIntegration(eRPExportOperation);
            addInfoMessage(BundleUtil.getString(Constants.BUNDLE, "label.integration.erp.exportoperation.retry"), model);

            //redirect to the retried export operation
            return redirect(READ_URL + retryExportOperation.getExternalId(), model, redirectAttributes);
        } catch (Exception ex) {
            //Add error messages to the list
            addErrorMessage(BundleUtil.getString(Constants.BUNDLE, "label.error.create") + ex.getLocalizedMessage(), model);

        }
        // Now choose what is the Exit Screen    
        return redirect(READ_URL + eRPExportOperation.getExternalId(), model, redirectAttributes);
    }

}
