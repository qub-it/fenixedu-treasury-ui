package org.fenixedu.treasury.ui.document.forwardpayments;

import static java.lang.String.format;
import static org.fenixedu.treasury.util.TreasuryConstants.treasuryBundle;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.fenixedu.bennu.spring.portal.SpringFunctionality;
import org.fenixedu.treasury.domain.forwardpayments.ForwardPaymentRequest;
import org.fenixedu.treasury.domain.forwardpayments.ForwardPaymentStateType;
import org.fenixedu.treasury.domain.forwardpayments.implementations.IForwardPaymentPlatformService;
import org.fenixedu.treasury.domain.payments.PaymentRequestLog;
import org.fenixedu.treasury.dto.forwardpayments.ForwardPaymentStatusBean;
import org.fenixedu.treasury.services.accesscontrol.TreasuryAccessControlAPI;
import org.fenixedu.treasury.services.integration.TreasuryPlataformDependentServicesFactory;
import org.fenixedu.treasury.ui.TreasuryBaseController;
import org.fenixedu.treasury.ui.TreasuryController;
import org.fenixedu.treasury.util.TreasuryConstants;
import org.joda.time.Days;
import org.joda.time.LocalDate;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.google.common.base.Strings;
import com.google.common.collect.Lists;

@SpringFunctionality(app = TreasuryController.class, title = "label.ManageForwardPayments.functionality", accessGroup = "treasuryBackOffice")
@RequestMapping(ManageForwardPaymentsController.CONTROLLER_URL)
public class ManageForwardPaymentsController extends TreasuryBaseController {

    private static final int MAX_SEARCH_SIZE = 1000;
    public static final String CONTROLLER_URL = "/treasury/document/forwardpayments/management";
    private static final String JSP_PATH = "/treasury/document/forwardpayments/management";

    @RequestMapping
    public String home() {
        return "redirect:" + SEARCH_URL;
    }

    public static final String SEARCH_URI = "/search";
    public static final String SEARCH_URL = CONTROLLER_URL + SEARCH_URI;

    @RequestMapping(value = SEARCH_URI, method = RequestMethod.GET)
    public String search(
            @RequestParam(value = "beginDate", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate beginDate,
            @RequestParam(value = "endDate", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate endDate,
            @RequestParam(value = "customerName", required = false) String customerName,
            @RequestParam(value = "customerBusinessId", required = false) String customerBusinessId,
            @RequestParam(value = "orderNumber", required = false) String orderNumber,
            @RequestParam(value = "withPendingPlatformPayment", required = false) boolean withPendingPlatformPayment,
            @RequestParam(value = "forwardPaymentStateType", required = false) ForwardPaymentStateType stateType,
            Model model) {

        Stream<ForwardPaymentRequest> stream = ForwardPaymentRequest.findAll();
        
        
        if (!Strings.isNullOrEmpty(orderNumber)) {
            stream = stream.filter(p -> orderNumber.trim().equals(String.valueOf(p.getOrderNumber())));
        } else {
            if (beginDate != null) {
                stream = stream.filter(p -> !p.getRequestDate().isBefore(beginDate.toDateTimeAtStartOfDay()));
            }

            if (endDate != null) {
                stream = stream
                        .filter(p -> !p.getRequestDate().isAfter(endDate.toDateTimeAtStartOfDay().plusDays(1).minusSeconds(1)));
            }

            if (!Strings.isNullOrEmpty(customerName)) {
                stream = stream.filter(p -> TreasuryConstants.stringNormalizedContains(p.getDebtAccount().getCustomer().getName(),
                        customerName));
            }

            if (!Strings.isNullOrEmpty(customerBusinessId)) {
                stream = stream
                        .filter(p -> p.getDebtAccount().getCustomer().getBusinessIdentification().equals(customerBusinessId));
            }

            if (stateType != null) {
                stream = stream.filter(p -> p.getState() == stateType);
            }
        }

        if (withPendingPlatformPayment) {
            final List<ForwardPaymentRequest> resultList = stream.collect(Collectors.toList());
            stream = resultList.stream();

            int MAX_PENDING_POSSIBLE_PAYMENTS = 500;
            if (resultList.stream().filter(p -> p.isInRequestedState()).count() > MAX_PENDING_POSSIBLE_PAYMENTS) {
                addErrorMessage(
                        treasuryBundle("error.ManageForwardPayments.search.withPendingPlatformPayment.limited.narrow.search"),
                        model);
            } else {
                stream = stream.filter(p -> p.isInRequestedState());
                stream = stream.filter(p -> p.getDigitalPaymentPlatform().castToForwardPaymentPlatformService().paymentStatus(p)
                        .isAbleToRegisterPostPayment(p));
            }
        }

        String username = TreasuryPlataformDependentServicesFactory.implementation().getLoggedUsername();
        stream = stream.filter(p -> TreasuryAccessControlAPI.isBackOfficeMember(username, p.getDigitalPaymentPlatform().getFinantialInstitution()));
        
        List<ForwardPaymentRequest> forwardPayments =
                stream.sorted(Collections.reverseOrder(Comparator.comparing(ForwardPaymentRequest::getRequestDate)))
                        .collect(Collectors.toList());

        boolean canLimitResults = !(beginDate != null && endDate != null && Days.daysBetween(beginDate, endDate).getDays() <= 1);

        if (canLimitResults && forwardPayments.size() > MAX_SEARCH_SIZE) {
            model.addAttribute("limitResults", true);
            model.addAttribute("limit", MAX_SEARCH_SIZE);
            model.addAttribute("total", forwardPayments.size());

            forwardPayments = forwardPayments.subList(0, MAX_SEARCH_SIZE);
        }

        model.addAttribute("forwardPaymentStateTypes", Lists.newArrayList(ForwardPaymentStateType.values()).stream()
                .sorted(ForwardPaymentStateType.COMPARE_BY_LOCALIZED_NAME).collect(Collectors.toList()));
        model.addAttribute("forwardPayments", forwardPayments);

        return jspPage(SEARCH_URI);
    }

    public static final String VIEW_URI = "/view";
    public static final String VIEW_URL = CONTROLLER_URL + VIEW_URI;

    @RequestMapping(VIEW_URI + "/{forwardPaymentId}")
    public String view(@PathVariable("forwardPaymentId") ForwardPaymentRequest forwardPayment, Model model) {
        model.addAttribute("forwardPayment", forwardPayment);

        return jspPage(VIEW_URI);
    }

    private static final String VERIFY_FORWARD_PAYMENT_URI = "/verifyforwardpayment";
    public static final String VERIFY_FORWARD_PAYMENT_URL = CONTROLLER_URL + VERIFY_FORWARD_PAYMENT_URI;

    @RequestMapping(VERIFY_FORWARD_PAYMENT_URI + "/{forwardPaymentId}")
    public String verifyforwardpayment(@PathVariable("forwardPaymentId") ForwardPaymentRequest forwardPayment, Model model,
            RedirectAttributes redirectAttributes) {

        try {
            List<ForwardPaymentStatusBean> paymentStatusBeanList = forwardPayment.getDigitalPaymentPlatform()
                    .castToForwardPaymentPlatformService().verifyPaymentStatus(forwardPayment);

            model.addAttribute("forwardPayment", forwardPayment);
            model.addAttribute("paymentStatusBeanList", paymentStatusBeanList);

            return jspPage(VERIFY_FORWARD_PAYMENT_URI);
        } catch (final Exception e) {
            addErrorMessage(e.getLocalizedMessage(), model);
            return redirect(VIEW_URL + "/" + forwardPayment.getExternalId(), model, redirectAttributes);
        }
    }

    private static final String REGISTER_PAYMENT_URI = "/registerpayment";
    public static final String REGISTER_PAYMENT_URL = CONTROLLER_URL + REGISTER_PAYMENT_URI;

    @RequestMapping(value = REGISTER_PAYMENT_URI + "/{forwardPaymentId}", method = RequestMethod.POST)
    public String registerPayment(@PathVariable("forwardPaymentId") ForwardPaymentRequest forwardPayment,
            @RequestParam("justification") String justification, @RequestParam("transactionId") String transactionId, Model model,
            RedirectAttributes redirectAttributes) {
        try {
            ForwardPaymentStatusBean paymentStatusBean =
                    forwardPayment.getDigitalPaymentPlatform().castToForwardPaymentPlatformService().paymentStatus(forwardPayment);

            if (!forwardPayment.isInStateToPostProcessPayment() || !paymentStatusBean.isInPayedState()) {
                addErrorMessage(treasuryBundle("label.ManageForwardPayments.forwardPayment.not.created.nor.payed.in.platform"),
                        model);
                return String.format("redirect:%s/%s", VERIFY_FORWARD_PAYMENT_URL, forwardPayment.getExternalId());
            }

            final IForwardPaymentPlatformService implementation =
                    forwardPayment.getDigitalPaymentPlatform().castToForwardPaymentPlatformService();

            Optional<String> optionalTransactionId = Optional.empty();
            if (StringUtils.isNotEmpty(transactionId)) {
                optionalTransactionId = Optional.of(transactionId);
            }

            implementation.postProcessPayment(forwardPayment, justification, optionalTransactionId);

            return String.format("redirect:%s/%s", VIEW_URL, forwardPayment.getExternalId());
        } catch (final Exception e) {
            e.printStackTrace();
            addErrorMessage(e.getLocalizedMessage(), model);
            return redirect(format("%s/%s", VERIFY_FORWARD_PAYMENT_URL, forwardPayment.getExternalId()), model,
                    redirectAttributes);
        }
    }

    private static final String DOWNLOAD_EXCEPTION_LOG_URI = "/downloadexceptionlog";
    public static final String DOWNLOAD_EXCEPTION_LOG_URL = CONTROLLER_URL + DOWNLOAD_EXCEPTION_LOG_URI;

    @RequestMapping(value = DOWNLOAD_EXCEPTION_LOG_URI + "/{forwardPaymentLogId}", method = RequestMethod.GET,
            produces = "application/octet-stream")
    @ResponseBody
    public byte[] downloadexceptionlog(@PathVariable("forwardPaymentLogId") PaymentRequestLog forwardPaymentLog,
            final Model model, final HttpServletResponse response) {
        assertUserIsManager(model);

        response.setHeader("Content-Disposition",
                "attachment; filename=" + String.format("exceptionLog-%s.txt", forwardPaymentLog.getExternalId()));
        if (forwardPaymentLog.getExceptionLogFile() != null) {
            return TreasuryPlataformDependentServicesFactory.implementation()
                    .getFileContent(forwardPaymentLog.getExceptionLogFile());
        }

        return null;
    }

    public static final String EXPORT_URI = "/export";
    public static final String EXPORT_URL = CONTROLLER_URL + EXPORT_URI;

    @RequestMapping(value = EXPORT_URI, method = RequestMethod.GET)
    @ResponseBody
    public void export() {
    }

    private String jspPage(final String mapping) {
        return JSP_PATH + mapping;
    }

}
