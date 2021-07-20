package org.fenixedu.treasury.ui.accounting.managecustomer;

import static org.fenixedu.treasury.util.TreasuryConstants.treasuryBundle;

import java.util.HashSet;

import org.fenixedu.bennu.spring.portal.BennuSpringController;
import org.fenixedu.treasury.domain.debt.DebtAccount;
import org.fenixedu.treasury.domain.exceptions.TreasuryDomainException;
import org.fenixedu.treasury.domain.payments.IMbwayPaymentPlatformService;
import org.fenixedu.treasury.domain.sibspaymentsgateway.MbwayRequest;
import org.fenixedu.treasury.domain.sibspaymentsgateway.integration.SibsPaymentsGateway;
import org.fenixedu.treasury.dto.SettlementNoteBean;
import org.fenixedu.treasury.dto.document.managepayments.PaymentReferenceCodeBean;
import org.fenixedu.treasury.ui.TreasuryBaseController;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@BennuSpringController(value = CustomerController.class)
@RequestMapping(MbwayPaymentRequestController.CONTROLLER_URL)
public class MbwayPaymentRequestController extends TreasuryBaseController {

    public static final String CONTROLLER_URL = "/treasury/accounting/managecustomer/mbwaypaymentrequest";
    private static final String JSP_PATH = "treasury/document/managepayments/mbwaypaymentrequest";

    protected void checkPermissions(DebtAccount debtAccount, Model model) {
        assertUserIsFrontOfficeMember(debtAccount.getFinantialInstitution(), model);
    }

    private static final String _CREATE_URI = "/create";
    public static final String CREATE_URL = CONTROLLER_URL + _CREATE_URI;

    @RequestMapping(value = _CREATE_URI + "/{debtAccountId}", method = RequestMethod.GET)
    public String create(@PathVariable("debtAccountId") final DebtAccount debtAccount, final Model model) {
        checkPermissions(debtAccount, model);

        if (!SibsPaymentsGateway.isMbwayServiceActive(debtAccount.getFinantialInstitution())) {
            throw new TreasuryDomainException("error.MbwayPaymentRequest.not.active");
        }

        final PaymentReferenceCodeBean bean = new PaymentReferenceCodeBean(null, debtAccount);
        bean.setUsePaymentAmountWithInterests(true);
        bean.setPhoneNumberCountryPrefix("351");

        return _create(debtAccount, bean, model);
    }

    private String _create(final DebtAccount debtAccount, final PaymentReferenceCodeBean bean, final Model model) {

        model.addAttribute("debtAccount", debtAccount);
        model.addAttribute("bean", bean);
        model.addAttribute("beanJson", getBeanJson(bean));
        model.addAttribute("debtAccountUrl", readDebtAccountUrl(debtAccount));
        model.addAttribute("createUrl", getCreateUrl());
        model.addAttribute("createPostbackUrl", getCreatePostbackUrl());

        return jspPage(_CREATE_URI);
    }

    private static final String _CREATEPOSTBACK_URI = "/createpostback";
    public static final String CREATEPOSTBACK_URL = CONTROLLER_URL + _CREATEPOSTBACK_URI;

    @RequestMapping(value = _CREATEPOSTBACK_URI + "/{debtAccountId}", method = RequestMethod.POST,
            produces = "application/json;charset=UTF-8")
    @ResponseBody
    public ResponseEntity<String> createpostback(@PathVariable("debtAccountId") final DebtAccount debtAccount,
            @RequestParam("bean") final PaymentReferenceCodeBean bean, final Model model) {
        bean.updateAmountOnSelectedDebitEntries();

        return new ResponseEntity<String>(getBeanJson(bean), HttpStatus.OK);
    }

    @RequestMapping(value = _CREATE_URI + "/{debtAccountId}", method = RequestMethod.POST)
    public String createpost(@PathVariable("debtAccountId") final DebtAccount debtAccount,
            @RequestParam("bean") final PaymentReferenceCodeBean bean, final Model model,
            final RedirectAttributes redirectAttributes) {

        if (!SibsPaymentsGateway.isMbwayServiceActive(debtAccount.getFinantialInstitution())) {
            throw new TreasuryDomainException("error.MbwayPaymentRequest.not.active");
        }

        checkPermissions(debtAccount, model);

        try {
            IMbwayPaymentPlatformService sibsOnlinePaymentsGateway =
                    SibsPaymentsGateway.findUniqueActive(debtAccount.getFinantialInstitution()).get();

            if (bean.getSelectedDebitEntries() == null || bean.getSelectedDebitEntries().isEmpty()) {
                addErrorMessage(treasuryBundle("error.MbwayPaymentRequest.invoiceEntriesSet.required"), model);

                return _create(debtAccount, bean, model);
            }

            bean.setUsePaymentAmountWithInterests(true);

            MbwayRequest mbwayPaymentRequest = sibsOnlinePaymentsGateway.createMbwayRequest(debtAccount,
                    new HashSet<>(bean.getSelectedDebitEntries()), new HashSet<>(bean.getSelectedInstallments()),
                    bean.getPhoneNumberCountryPrefix(), bean.getPhoneNumber());

            return redirect(String.format("%s/%s/%s", getShowMbwayPaymentRequest(), debtAccount.getExternalId(),
                    mbwayPaymentRequest.getExternalId()), model, redirectAttributes);

        } catch (final TreasuryDomainException e) {
            addErrorMessage(e.getLocalizedMessage(), model);

            return _create(debtAccount, bean, model);
        }
    }

    private static final String _SHOW_MBWAY_PAYMENT_REQUEST_URI = "/showmbwaypaymentrequest";
    public static final String SHOW_MBWAY_PAYMENT_REQUEST_URL = CONTROLLER_URL + _SHOW_MBWAY_PAYMENT_REQUEST_URI;

    @RequestMapping(value = _SHOW_MBWAY_PAYMENT_REQUEST_URI + "/{debtAccountId}/{mbwayPaymentRequestId}")
    public String showmbwaypaymentrequest(@PathVariable("debtAccountId") final DebtAccount debtAccount,

            @PathVariable("mbwayPaymentRequestId") final MbwayRequest mbwayPaymentRequest, final Model model) {

        model.addAttribute("debtAccount", debtAccount);
        model.addAttribute("mbwayPaymentRequest", mbwayPaymentRequest);
        model.addAttribute("debtAccountUrl", readDebtAccountUrl(debtAccount));
        model.addAttribute("createUrl", getCreateUrl());
        model.addAttribute("createPostbackUrl", getCreatePostbackUrl());

        return jspPage(_SHOW_MBWAY_PAYMENT_REQUEST_URI);
    }

    protected String readDebtAccountUrl(final DebtAccount debtAccount) {
        return String.format("%s/%s", DebtAccountController.READ_URL, debtAccount.getExternalId());
    }

    protected String getCreateUrl() {
        return CREATE_URL;
    }

    protected String getCreatePostbackUrl() {
        return CREATEPOSTBACK_URL;
    }

    protected String getShowMbwayPaymentRequest() {
        return SHOW_MBWAY_PAYMENT_REQUEST_URL;
    }

    private String jspPage(final String page) {
        return JSP_PATH + "/" + page;
    }

}
