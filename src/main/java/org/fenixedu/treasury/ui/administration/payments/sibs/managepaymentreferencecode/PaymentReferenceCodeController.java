/**
 * This file was created by Quorum Born IT <http://www.qub-it.com/> and its 
 * copyright terms are bind to the legal agreement regulating the FenixEdu@ULisboa 
 * software development project between Quorum Born IT and ServiÃ§os Partilhados da
 * Universidade de Lisboa:
 *  - Copyright Â© 2015 Quorum Born IT (until any Go-Live phase)
 *  - Copyright Â© 2015 Universidade de Lisboa (after any Go-Live phase)
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
package org.fenixedu.treasury.ui.administration.payments.sibs.managepaymentreferencecode;

import static org.fenixedu.treasury.util.TreasuryConstants.treasuryBundle;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.fenixedu.bennu.spring.portal.SpringFunctionality;
import org.fenixedu.treasury.domain.exceptions.TreasuryDomainException;
import org.fenixedu.treasury.domain.paymentcodes.PaymentReferenceCodeStateType;
import org.fenixedu.treasury.domain.paymentcodes.SibsPaymentRequest;
import org.fenixedu.treasury.domain.paymentcodes.SibsReferenceCode;
import org.fenixedu.treasury.domain.payments.PaymentRequest;
import org.fenixedu.treasury.ui.TreasuryBaseController;
import org.fenixedu.treasury.ui.TreasuryController;
import org.springframework.stereotype.Component;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import pt.ist.fenixframework.FenixFramework;

@Component("org.fenixedu.treasury.ui.administration.payments.sibs.managePaymentReferenceCode")
@SpringFunctionality(app = TreasuryController.class,
        title = "label.title.administration.payments.sibs.managePaymentReferenceCode", accessGroup = "treasuryFrontOffice")
// @BennuSpringController(value = PaymentCodePoolController.class)
@RequestMapping(PaymentReferenceCodeController.CONTROLLER_URL)
public class PaymentReferenceCodeController extends TreasuryBaseController {

    private static final int MAX_SEARCH = 500;
    public static final String CONTROLLER_URL =
            "/treasury/administration/payments/sibs/managepaymentreferencecode/paymentreferencecode";

    @RequestMapping
    public String home(Model model) {
        return "forward:" + CONTROLLER_URL + "/";
    }

    private static final String _SEARCH_URI = "/";
    public static final String SEARCH_URL = CONTROLLER_URL + _SEARCH_URI;

    @RequestMapping(value = _SEARCH_URI)
    public String search(@RequestParam(value = "referencecode", required = false) String referenceCode,
            @RequestParam(value = "state", required = false) PaymentReferenceCodeStateType state, Model model) {

        List<Object> searchpaymentreferencecodeResultsDataSet = filterSearchPaymentReferenceCode(referenceCode, state);

        if (searchpaymentreferencecodeResultsDataSet.size() > MAX_SEARCH) {
            searchpaymentreferencecodeResultsDataSet = searchpaymentreferencecodeResultsDataSet.subList(0, MAX_SEARCH);
        }

        model.addAttribute("searchpaymentreferencecodeResultsDataSet", searchpaymentreferencecodeResultsDataSet);
        model.addAttribute("stateValues", PaymentReferenceCodeStateType.values());
        return "treasury/administration/payments/sibs/managepaymentreferencecode/paymentreferencecode/search";
    }

    private List<Object> filterSearchPaymentReferenceCode(String referenceCode, PaymentReferenceCodeStateType state) {

        List<Object> result = new ArrayList<>();
        
        SibsReferenceCode.findAll()
                .filter(p -> p.isInCreatedState())
                .filter(p -> StringUtils.isEmpty(referenceCode) || p.getReferenceCode().toLowerCase().contains(referenceCode.toLowerCase()))
                .filter(p -> state == null || state.equals(p.getState()))
                .collect(Collectors.toCollection(() -> result));
        
        
        SibsPaymentRequest.findAll()
            .filter(p -> StringUtils.isEmpty(referenceCode) || p.getReferenceCode().toLowerCase().contains(referenceCode.toLowerCase()))
            .filter(p -> state == null || state.equals(p.getState()))
            .collect(Collectors.toCollection(() -> result));

        return result;
    }

    private static final String _READ_URI = "/read/";
    public static final String READ_URL = CONTROLLER_URL + _READ_URI;

    @RequestMapping(value = _READ_URI + "{oid}")
    public String read(@PathVariable("oid") PaymentRequest paymentReferenceCode, Model model) {
        model.addAttribute("paymentReferenceCode", paymentReferenceCode);
        model.addAttribute("sibsTransactionDetails", paymentReferenceCode.getPaymentTransactionsSet());

        return "treasury/administration/payments/sibs/managepaymentreferencecode/paymentreferencecode/read";
    }

    @RequestMapping(value = "/read/{oid}/anull", method = RequestMethod.POST)
    public String processReadToAnull(@PathVariable("oid") SibsPaymentRequest paymentReferenceCode, Model model,
            RedirectAttributes redirectAttributes) {
        try {
            FenixFramework.atomic(() -> {
                paymentReferenceCode.anull();
            });
            
            addInfoMessage(treasuryBundle("label.success.update"), model);
        } catch (TreasuryDomainException tex) {
            addErrorMessage(treasuryBundle("label.error.update") + tex.getLocalizedMessage(), model);
        } catch (Exception ex) {
            addErrorMessage(treasuryBundle("label.error.update") + ex.getLocalizedMessage(), model);
        }
        
        return redirect(READ_URL + paymentReferenceCode.getExternalId(), model, redirectAttributes);
    }
    
    
}
