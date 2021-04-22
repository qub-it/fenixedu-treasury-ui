package org.fenixedu.treasury.ui.accounting.managecustomer;

import static org.fenixedu.treasury.util.TreasuryConstants.treasuryBundle;

import java.util.HashSet;

import org.fenixedu.bennu.spring.portal.BennuSpringController;
import org.fenixedu.treasury.domain.debt.DebtAccount;
import org.fenixedu.treasury.domain.exceptions.TreasuryDomainException;
import org.fenixedu.treasury.domain.payments.integration.DigitalPaymentPlatform;
import org.fenixedu.treasury.dto.document.managepayments.PaymentReferenceCodeBean;
import org.fenixedu.treasury.ui.TreasuryBaseController;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Component("org.fenixedu.treasury.ui.accounting.managecustomer")
@BennuSpringController(value = CustomerController.class)
@RequestMapping(PaymentReferenceCodeController.CONTROLLER_URL)
public class PaymentReferenceCodeController extends TreasuryBaseController {

    public static final String CONTROLLER_URL = "/treasury/accounting/managecustomer/paymentreferencecode";
    private static final String JSP_PATH = "treasury/accounting/managecustomer/paymentreferencecode";

    @RequestMapping
    public String home(Model model) {
        return "forward:" + CONTROLLER_URL + "/";
    }

    private static final String _CREATEPAYMENTCODEFORSEVERALDEBITENTRIES_URI = "/createpaymentcodeforseveraldebitentries";
    public static final String CREATEPAYMENTCODEFORSEVERALDEBITENTRIES_URL =
            CONTROLLER_URL + _CREATEPAYMENTCODEFORSEVERALDEBITENTRIES_URI;

    @RequestMapping(value = _CREATEPAYMENTCODEFORSEVERALDEBITENTRIES_URI + "/{debtAccountId}", method = RequestMethod.GET)
    public String createPaymentCodeForSeveralDebitEntries(@PathVariable("debtAccountId") DebtAccount debtAccount, Model model) {

        checkPermissions(debtAccount, model);

        final PaymentReferenceCodeBean bean = new PaymentReferenceCodeBean(DigitalPaymentPlatform
                .findForSibsPaymentCodeServiceByActive(debtAccount.getFinantialInstitution(), true).findFirst().orElse(null),
                debtAccount);
        bean.setUsePaymentAmountWithInterests(false);

        return _createPaymentCodeForSeveralDebitEntries(debtAccount, bean, model);
    }

    protected String _createPaymentCodeForSeveralDebitEntries(DebtAccount debtAccount, PaymentReferenceCodeBean bean,
            Model model) {

        model.addAttribute("debtAccount", debtAccount);
        model.addAttribute("bean", bean);
        model.addAttribute("beanJson", getBeanJson(bean));
        model.addAttribute("debtAccountUrl", readDebtAccountUrl(debtAccount));
        model.addAttribute("createUrl", getCreateUrl(debtAccount));
        model.addAttribute("createPostbackUrl", getCreatePostbackUrl(debtAccount));

        return jspPage("createpaymentcodeforseveraldebitentries");
    }

    private static final String _CREATEPAYMENTCODEFORSEVERALDEBITENTRIESPOSTBACK_URI =
            "/createpaymentcodeforseveraldebitentriespostback";
    public static final String CREATEPAYMENTCODEFORSEVERALDEBITENTRIESPOSTBACK_URL =
            CONTROLLER_URL + _CREATEPAYMENTCODEFORSEVERALDEBITENTRIESPOSTBACK_URI;

    @RequestMapping(value = _CREATEPAYMENTCODEFORSEVERALDEBITENTRIESPOSTBACK_URI + "/{debtAccountId}",
            method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public @ResponseBody ResponseEntity<String> createPaymentCodeForSeveralDebitEntriesPostback(
            @PathVariable("debtAccountId") DebtAccount debtAccount, @RequestParam("bean") PaymentReferenceCodeBean bean,
            Model model) {

        checkPermissions(debtAccount, model);

        bean.updateAmountOnSelectedDebitEntries();

        return new ResponseEntity<String>(getBeanJson(bean), HttpStatus.OK);
    }

    @RequestMapping(value = _CREATEPAYMENTCODEFORSEVERALDEBITENTRIES_URI + "/{debtAccountId}", method = RequestMethod.POST)
    public String createPaymentCodeForSeveralDebitEntries(@PathVariable("debtAccountId") DebtAccount debtAccount,
            @RequestParam("bean") PaymentReferenceCodeBean bean, Model model, RedirectAttributes redirectAttributes) {

        checkPermissions(debtAccount, model);

        try {

            boolean hasSelectedDebitEntries = bean.getSelectedDebitEntries() != null && !bean.getSelectedDebitEntries().isEmpty();
            boolean hasSelectedInstallments = bean.getSelectedInstallments() != null && !bean.getSelectedInstallments().isEmpty();
            if (!hasSelectedDebitEntries && !hasSelectedInstallments) {
                addErrorMessage(treasuryBundle("error.MultipleEntriesPaymentCode.select.at.least.one.debit.entry"), model);

                return _createPaymentCodeForSeveralDebitEntries(debtAccount, bean, model);
            }

            if (bean.getPaymentCodePool() == null) {
                addErrorMessage(treasuryBundle("error.MultipleEntriesPaymentCode.payment.code.pool.required"), model);

                return _createPaymentCodeForSeveralDebitEntries(debtAccount, bean, model);
            }

            bean.getPaymentCodePool().castToSibsPaymentCodePoolService().createSibsPaymentRequest(debtAccount,
                    new HashSet<>(bean.getSelectedDebitEntries()), new HashSet<>(bean.getSelectedInstallments()));

            addInfoMessage(treasuryBundle("label.document.managepayments.success.create.reference.code.selected.debit.entries"),
                    model);

            return redirect(String.format(readDebtAccountUrl(debtAccount)), model, redirectAttributes);
        } catch (final TreasuryDomainException e) {
            addErrorMessage(e.getLocalizedMessage(), model);

            return _createPaymentCodeForSeveralDebitEntries(debtAccount, bean, model);
        }
    }

    protected void checkPermissions(DebtAccount debtAccount, Model model) {
        assertUserIsFrontOfficeMember(debtAccount.getFinantialInstitution(), model);
    }

    protected String readDebtAccountUrl(DebtAccount debtAccount) {
        return String.format("%s/%s", DebtAccountController.READ_URL, debtAccount.getExternalId());
    }

    protected String getCreateUrl(DebtAccount debtAccount) {
        return CREATEPAYMENTCODEFORSEVERALDEBITENTRIES_URL + "/" + debtAccount.getExternalId();
    }

    protected String getCreatePostbackUrl(DebtAccount debtAccount) {
        return CREATEPAYMENTCODEFORSEVERALDEBITENTRIESPOSTBACK_URL + "/" + debtAccount.getExternalId();
    }

    private String jspPage(final String page) {
        return JSP_PATH + "/" + page;
    }

}
