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
package org.fenixedu.treasury.ui.document.managepayments;

import java.util.List;
import java.util.stream.Collectors;

import org.fenixedu.bennu.core.i18n.BundleUtil;
import org.fenixedu.bennu.spring.portal.BennuSpringController;
import org.fenixedu.treasury.domain.document.DebitNote;
import org.fenixedu.treasury.domain.paymentcodes.PaymentReferenceCode;
import org.fenixedu.treasury.domain.paymentcodes.pool.PaymentCodePool;
import org.fenixedu.treasury.dto.document.managepayments.PaymentReferenceCodeBean;
import org.fenixedu.treasury.ui.TreasuryBaseController;
import org.fenixedu.treasury.ui.TreasuryController;
import org.fenixedu.treasury.util.Constants;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import pt.ist.fenixframework.Atomic;

//@Component("org.fenixedu.treasury.ui.document.managePayments") <-- Use for duplicate controller name disambiguation
//@SpringFunctionality(app = TreasuryController.class, title = "label.title.document.managePayments",accessGroup = "managers")// CHANGE_ME accessGroup = "group1 | group2 | groupXPTO"
//or
@BennuSpringController(value = TreasuryController.class)
@RequestMapping(PaymentReferenceCodeController.CONTROLLER_URL)
public class PaymentReferenceCodeController extends TreasuryBaseController {

    public static final String CONTROLLER_URL = "/treasury/document/managepayments/paymentreferencecode";

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

    private PaymentReferenceCodeBean getPaymentReferenceCodeBean(Model model) {
        return (PaymentReferenceCodeBean) model.asMap().get("paymentReferenceCodeBean");
    }

    private void setPaymentReferenceCodeBean(PaymentReferenceCodeBean bean, Model model) {
        model.addAttribute("paymentReferenceCodeBeanJson", getBeanJson(bean));
        model.addAttribute("paymentReferenceCodeBean", bean);
    }

    // @formatter: on

    private PaymentReferenceCode getPaymentReferenceCode(Model model) {
        return (PaymentReferenceCode) model.asMap().get("paymentReferenceCode");
    }

    private void setPaymentReferenceCode(PaymentReferenceCode paymentReferenceCode, Model model) {
        model.addAttribute("paymentReferenceCode", paymentReferenceCode);
    }

    @Atomic
    public void deletePaymentReferenceCode(PaymentReferenceCode paymentReferenceCode) {
        // CHANGE_ME: Do the processing for deleting the paymentReferenceCode
        // Do not catch any exception here

        // paymentReferenceCode.delete();
    }

//				
    private static final String _CREATEPAYMENTCODEINDEBITNOTE_URI = "/createpaymentcodeindebitnote";
    public static final String CREATEPAYMENTCODEINDEBITNOTE_URL = CONTROLLER_URL + _CREATEPAYMENTCODEINDEBITNOTE_URI;

    @RequestMapping(value = _CREATEPAYMENTCODEINDEBITNOTE_URI, method = RequestMethod.GET)
    public String createpaymentcodeindebitnote(@RequestParam(value = "debitNote") DebitNote debitNote, Model model) {
        model.addAttribute("stateValues", org.fenixedu.treasury.domain.paymentcodes.PaymentReferenceCodeStateType.values());

        //IF ANGULAR, initialize the Bean
        PaymentReferenceCodeBean bean = new PaymentReferenceCodeBean();
        bean.setDebitNote(debitNote);
        List<PaymentCodePool> activePools =
                debitNote.getDebtAccount().getFinantialInstitution().getPaymentCodePoolsSet().stream()
                        .filter(x -> Boolean.TRUE.equals(x.getActive())).collect(Collectors.toList());
        bean.setPaymentCodePoolDataSource(activePools);
        this.setPaymentReferenceCodeBean(bean, model);

        return "treasury/document/managepayments/paymentreferencecode/createpaymentcodeindebitnote";
    }

//
//               THIS SHOULD BE USED ONLY WHEN USING ANGULAR 
//
    // @formatter: off

    private static final String _CREATEPAYMENTCODEINDEBITNOTEPOSTBACK_URI = "/createpaymentcodeindebitnotepostback";
    public static final String CREATEPAYMENTCODEINDEBITNOTEPOSTBACK_URL = CONTROLLER_URL
            + _CREATEPAYMENTCODEINDEBITNOTEPOSTBACK_URI;

    @RequestMapping(value = _CREATEPAYMENTCODEINDEBITNOTEPOSTBACK_URI, method = RequestMethod.POST,
            produces = "application/json;charset=UTF-8")
    public @ResponseBody String createpaymentcodeindebitnotepostback(
            @RequestParam(value = "bean", required = false) PaymentReferenceCodeBean bean, Model model) {

        // Do validation logic ?!?!
        this.setPaymentReferenceCodeBean(bean, model);
        return getBeanJson(bean);
    }

    @RequestMapping(value = _CREATEPAYMENTCODEINDEBITNOTE_URI, method = RequestMethod.POST)
    public String createpaymentcodeindebitnote(@RequestParam(value = "bean", required = false) PaymentReferenceCodeBean bean,
            Model model, RedirectAttributes redirectAttributes) {

        /*
        *  Creation Logic
        */

        try {

            PaymentReferenceCode paymentReferenceCode =
                    bean.getPaymentCodePool()
                            .getReferenceCodeGenerator()
                            .generateNewCodeFor(bean.getDebitNote().getDebtAccount().getCustomer(),
                                    bean.getDebitNote().getOpenAmount());

            paymentReferenceCode.createPaymentTargetTo(bean.getDebitNote());
            //Success Validation
            //Add the bean to be used in the View
            addInfoMessage(BundleUtil.getString(Constants.BUNDLE,
                    "label.document.managepayments.success.create.reference.code.debitnote"), model);

            //Success Validation
            //Add the bean to be used in the View
            model.addAttribute("paymentReferenceCode", paymentReferenceCode);
            return redirect("/treasury/document/manageinvoice/debitnote/read/" + bean.getDebitNote().getExternalId(), model,
                    redirectAttributes);
        } catch (Exception de) {

            /*
             * If there is any error in validation 
             *
             * Add a error / warning message
             * 
             * addErrorMessage(BundleUtil.getString(TreasurySpringConfiguration.BUNDLE, "label.error.create") + de.getLocalizedMessage(),model);
             * addWarningMessage(" Warning creating due to "+ ex.getLocalizedMessage(),model); */

            addErrorMessage(BundleUtil.getString(Constants.BUNDLE, "label.error.create") + de.getLocalizedMessage(), model);
            this.setPaymentReferenceCodeBean(bean, model);
            return "treasury/document/managepayments/paymentreferencecode/createpaymentcodeindebitnote";

        }
    }
    // @formatter: on

//				
//    @Atomic
//    public PaymentReferenceCode createPaymentReferenceCode(java.lang.String referenceCode, Deb) {
//
//        // @formatter: off
//
//        /*
//         * Modify the creation code here if you do not want to create
//         * the object with the default constructor and use the setter
//         * for each field
//         * 
//         */
//
//        // CHANGE_ME It's RECOMMENDED to use "Create service" in DomainObject
//        //PaymentReferenceCode paymentReferenceCode = paymentReferenceCode.create(fields_to_create);
//
//        //Instead, use individual SETTERS and validate "CheckRules" in the end
//        // @formatter: on
//
//        PaymentReferenceCode paymentReferenceCode = PaymentReferenceCode.create(referenceCode, beginDate, endDate, state)
//        paymentReferenceCode.setState(state);
//        paymentReferenceCode.setReferenceCode(referenceCode);
//
//        return paymentReferenceCode;
//    }
}
