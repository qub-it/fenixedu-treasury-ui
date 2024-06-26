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
package org.fenixedu.treasury.ui.administration.payments.sibs.managepaymentcodepool;

import static org.fenixedu.treasury.util.TreasuryConstants.treasuryBundle;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

import org.fenixedu.bennu.spring.portal.SpringFunctionality;
import org.fenixedu.treasury.domain.FinantialInstitution;
import org.fenixedu.treasury.domain.PaymentMethod;
import org.fenixedu.treasury.domain.document.DocumentNumberSeries;
import org.fenixedu.treasury.domain.document.FinantialDocumentType;
import org.fenixedu.treasury.domain.exceptions.TreasuryDomainException;
import org.fenixedu.treasury.domain.paymentcodes.integration.SibsPaymentCodePool;
import org.fenixedu.treasury.domain.payments.integration.DigitalPaymentPlatform;
import org.fenixedu.treasury.services.accesscontrol.TreasuryAccessControlAPI;
import org.fenixedu.treasury.services.integration.TreasuryPlataformDependentServicesFactory;
import org.fenixedu.treasury.ui.TreasuryBaseController;
import org.fenixedu.treasury.ui.TreasuryController;
import org.joda.time.LocalDate;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import pt.ist.fenixframework.FenixFramework;

//@Component("org.fenixedu.treasury.ui.administration.payments.sibs.managePaymentCodePool") <-- Use for duplicate controller name disambiguation
@SpringFunctionality(app = TreasuryController.class, title = "label.title.administration.payments.sibs.managePaymentCodePool",
        accessGroup = "treasuryManagers")
@RequestMapping(PaymentCodePoolController.CONTROLLER_URL)
public class PaymentCodePoolController extends TreasuryBaseController {

    public static final String CONTROLLER_URL = "/treasury/administration/payments/sibs/managepaymentcodepool/paymentcodepool";

    @RequestMapping
    public String home(Model model) {
        return "forward:" + CONTROLLER_URL + "/";
    }

    private DigitalPaymentPlatform getPaymentCodePool(Model model) {
        return (DigitalPaymentPlatform) model.asMap().get("paymentCodePool");
    }

    private static final String _SEARCH_URI = "/";
    public static final String SEARCH_URL = CONTROLLER_URL + _SEARCH_URI;

    @RequestMapping(value = _SEARCH_URI)
    public String search(Model model) {
        model.addAttribute("searchpaymentcodepoolResultsDataSet", filterSearchPaymentCodePool());

        return "treasury/administration/payments/sibs/managepaymentcodepool/paymentcodepool/search";
    }

    private List<DigitalPaymentPlatform> filterSearchPaymentCodePool() {
        String loggedUsername = TreasuryPlataformDependentServicesFactory.implementation().getLoggedUsername();

        return FinantialInstitution.findAll().filter(f -> TreasuryAccessControlAPI.isBackOfficeMember(loggedUsername, f))
                .flatMap(f -> SibsPaymentCodePool.find(f)).collect(Collectors.toList());
    }

    private static final String _SEARCH_TO_VIEW_ACTION_URI = "/search/view/";
    public static final String SEARCH_TO_VIEW_ACTION_URL = CONTROLLER_URL + _SEARCH_TO_VIEW_ACTION_URI;

    @RequestMapping(value = _SEARCH_TO_VIEW_ACTION_URI + "{oid}")
    public String processSearchToViewAction(@PathVariable("oid") DigitalPaymentPlatform paymentCodePool, Model model,
            RedirectAttributes redirectAttributes) {
        return redirect(READ_URL + paymentCodePool.getExternalId(), model, redirectAttributes);
    }

    private static final String _READ_URI = "/read/";
    public static final String READ_URL = CONTROLLER_URL + _READ_URI;

    @RequestMapping(value = _READ_URI + "{oid}")
    public String read(@PathVariable("oid") DigitalPaymentPlatform paymentCodePool, Model model) {
        model.addAttribute("paymentCodePool", paymentCodePool);
        return "treasury/administration/payments/sibs/managepaymentcodepool/paymentcodepool/read";
    }

//
    private static final String _DELETE_URI = "/delete/";
    public static final String DELETE_URL = CONTROLLER_URL + _DELETE_URI;

    @RequestMapping(value = _DELETE_URI + "{oid}", method = RequestMethod.POST)
    public String delete(@PathVariable("oid") DigitalPaymentPlatform paymentCodePool, Model model,
            RedirectAttributes redirectAttributes) {

        model.addAttribute("paymentCodePool", paymentCodePool);
        try {
            assertUserIsManager(model);

            //paymentCodePool.delete();

            addInfoMessage(treasuryBundle("label.success.delete"), model);
            return redirect("/treasury/administration/payments/sibs/managepaymentcodepool/paymentcodepool/", model,
                    redirectAttributes);
        } catch (TreasuryDomainException tex) {
            addErrorMessage(treasuryBundle("label.error.delete") + tex.getLocalizedMessage(), model);
        } catch (Exception ex) {
            addErrorMessage(treasuryBundle("label.error.delete") + ex.getLocalizedMessage(), model);
        }

        return "treasury/administration/payments/sibs/managepaymentcodepool/paymentcodepool/read/"
                + getPaymentCodePool(model).getExternalId();
    }

    private static final String _CREATE_URI = "/create";
    public static final String CREATE_URL = CONTROLLER_URL + _CREATE_URI;

    @RequestMapping(value = _CREATE_URI, method = RequestMethod.GET)
    public String create(Model model) {
        model.addAttribute("finantialInstitutionList", FinantialInstitution.findAll().collect(Collectors.toList()));

        model.addAttribute("PaymentCodePool_documentSeriesForPayments_options",
                DocumentNumberSeries.findAll()
                        .filter(x -> x.getFinantialDocumentType().equals(FinantialDocumentType.findForSettlementNote()))
                        .filter(x -> x.getSeries().getActive()).collect(Collectors.toList()));

        model.addAttribute("PaymentCodePool_paymentMethod_options", PaymentMethod.findAll().collect(Collectors.toList()));
        return "treasury/administration/payments/sibs/managepaymentcodepool/paymentcodepool/create";
    }

    @RequestMapping(value = _CREATE_URI, method = RequestMethod.POST)
    public String create(@RequestParam(value = "finantialinstitution") FinantialInstitution finantialInstitution,
            @RequestParam(value = "name") String name,
            @RequestParam(value = "entityreferencecode", required = false) String entityReferenceCode,
            @RequestParam(value = "minreferencecode", required = false) Long minReferenceCode,
            @RequestParam(value = "maxreferencecode", required = false) Long maxReferenceCode,
            @RequestParam(value = "minamount", required = false) BigDecimal minAmount,
            @RequestParam(value = "maxamount", required = false) BigDecimal maxAmount,
            @RequestParam(value = "validfrom", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate validFrom,
            @RequestParam(value = "validto", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate validTo,
            @RequestParam(value = "active", required = false) boolean active,
            @RequestParam(value = "usecheckdigit", required = false) boolean useCheckDigit,
            @RequestParam(value = "sourceinstitutionid", required = false) String sourceInstitutionId,
            @RequestParam(value = "destinationinstitutionid", required = false) String destinationInstitutionId, Model model,
            RedirectAttributes redirectAttributes) {

        throw new RuntimeException("deprecated");
    }

    private static final String _UPDATE_URI = "/update/";
    public static final String UPDATE_URL = CONTROLLER_URL + _UPDATE_URI;

    @RequestMapping(value = _UPDATE_URI + "{oid}", method = RequestMethod.GET)
    public String update(@PathVariable("oid") SibsPaymentCodePool paymentCodePool, Model model) {
        model.addAttribute("finantialInstitutionList", FinantialInstitution.findAll().collect(Collectors.toList()));

        model.addAttribute("PaymentCodePool_documentSeriesForPayments_options",
                DocumentNumberSeries.findAll()
                        .filter(x -> x.getFinantialDocumentType().equals(FinantialDocumentType.findForSettlementNote()))
                        .collect(Collectors.toList()));

        model.addAttribute("PaymentCodePool_paymentMethod_options", PaymentMethod.findAll().collect(Collectors.toList()));

        model.addAttribute("paymentCodePool", paymentCodePool);

        return "treasury/administration/payments/sibs/managepaymentcodepool/paymentcodepool/update";

    }

    @RequestMapping(value = _UPDATE_URI + "{oid}", method = RequestMethod.POST)
    public String update(@PathVariable("oid") SibsPaymentCodePool paymentCodePool, @RequestParam(value = "name") String name,
            @RequestParam(value = "active", required = false) boolean active,
            @RequestParam(value = "validfrom", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate validFrom,
            @RequestParam(value = "validto", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate validTo,
            @RequestParam(value = "sourceinstitutionid", required = false) String sourceInstitutionId,
            @RequestParam(value = "destinationinstitutionid", required = false) String destinationInstitutionId, Model model,
            RedirectAttributes redirectAttributes) {

        model.addAttribute("paymentCodePool", paymentCodePool);

        try {
            assertUserIsManager(model);

            FenixFramework.atomic(() -> {
                paymentCodePool.edit(name, active, validFrom, validTo, sourceInstitutionId, destinationInstitutionId);
            });

            return redirect(READ_URL + getPaymentCodePool(model).getExternalId(), model, redirectAttributes);
        } catch (TreasuryDomainException tex) {
            addErrorMessage(treasuryBundle("label.error.update") + tex.getLocalizedMessage(), model);
        } catch (Exception de) {
            addErrorMessage(treasuryBundle("label.error.update") + de.getLocalizedMessage(), model);
        }
        return update(paymentCodePool, model);
    }

}
