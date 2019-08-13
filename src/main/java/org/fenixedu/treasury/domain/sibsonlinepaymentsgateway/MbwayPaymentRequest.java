package org.fenixedu.treasury.domain.sibsonlinepaymentsgateway;

import java.math.BigDecimal;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.fenixedu.onlinepaymentsgateway.api.MbWayCheckoutResultBean;
import org.fenixedu.onlinepaymentsgateway.exceptions.OnlinePaymentsGatewayCommunicationException;
import org.fenixedu.treasury.domain.debt.DebtAccount;
import org.fenixedu.treasury.domain.document.InvoiceEntry;
import org.fenixedu.treasury.domain.exceptions.TreasuryDomainException;
import org.fenixedu.treasury.util.TreasuryConstants;
import org.joda.time.DateTime;

import pt.ist.fenixframework.Atomic;
import pt.ist.fenixframework.Atomic.TxMode;
import pt.ist.fenixframework.FenixFramework;

public class MbwayPaymentRequest extends MbwayPaymentRequest_Base {

    public MbwayPaymentRequest() {
        super();

        setCreationDate(new DateTime());
        setDomainRoot(FenixFramework.getDomainRoot());
    }

    protected MbwayPaymentRequest(final SibsOnlinePaymentsGateway sibsOnlinePaymentsGateway, final DebtAccount debtAccount,
            final Set<InvoiceEntry> invoiceEntries, final BigDecimal payableAmount, final String phoneNumber,
            final String sibsMerchantTransactionId, final String sibsReferenceId) {
        this();

        setSibsOnlinePaymentsGateway(sibsOnlinePaymentsGateway);
        setDebtAccount(debtAccount);
        getInvoiceEntriesSet().addAll(invoiceEntries);
        setPayableAmount(payableAmount);
        setPhoneNumber(phoneNumber);
        setSibsMerchantTransactionId(sibsMerchantTransactionId);
        setSibsReferenceId(sibsReferenceId);

        checkRules();
    }

    private void checkRules() {
        
        if(getDomainRoot() == null) {
            throw new TreasuryDomainException("error.MbwayPaymentRequest.domainRoot.required");
        }
        
        if(getCreationDate() == null) {
            throw new TreasuryDomainException("error.MbwayPaymentRequest.creationDate.required");
        }
        
        if(getSibsOnlinePaymentsGateway() == null) {
            throw new TreasuryDomainException("error.MbwayPaymentRequest.sibsOnlinePaymentsGateway.required");
        }
        
        if(getDebtAccount() == null) {
            throw new TreasuryDomainException("error.MbwayPaymentRequest.debtAccount.required");
        }
        
        if(getInvoiceEntriesSet().isEmpty()) {
            throw new TreasuryDomainException("error.MbwayPaymentRequest.invoiceEntriesSet.required");
        }
        
        if(getPayableAmount() == null || !TreasuryConstants.isPositive(getPayableAmount())) {
            throw new TreasuryDomainException("error.MbwayPaymentRequest.payableAmount.required");
        }
        
        if(StringUtils.isEmpty(getPhoneNumber())) {
            throw new TreasuryDomainException("error.MbwayPaymentRequest.phoneNumber.required");
        }
        
        if(StringUtils.isEmpty(getSibsMerchantTransactionId())) {
            throw new TreasuryDomainException("error.MbwayPaymentRequest.sibsMerchantTransaction.required");
        }

        if(StringUtils.isEmpty(getSibsReferenceId())) {
            throw new TreasuryDomainException("error.MbwayPaymentRequest.sibsReferenceId.required");
        }
        
    }

    /* ************ */
    /* * SERVICES * */
    /* ************ */

    @Atomic(mode = TxMode.READ)
    public static MbwayPaymentRequest create(final SibsOnlinePaymentsGateway sibsOnlinePaymentsGateway,
            final DebtAccount debtAccount, final Set<InvoiceEntry> invoiceEntries, final String phoneNumber) {

        if (!Boolean.TRUE.equals(sibsOnlinePaymentsGateway.getForwardPaymentConfiguration().isActive())) {
            throw new TreasuryDomainException("error.MbwayPaymentRequest.forwardPaymentConfiguration.not.active");
        }

        final BigDecimal payableAmount = invoiceEntries.stream().map(e -> e.getOpenAmountWithInterests())
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        final String merchantTransactionId = sibsOnlinePaymentsGateway.generateNewMerchantTransactionId();
        final SibsOnlinePaymentsGatewayLog log = createLog(sibsOnlinePaymentsGateway, debtAccount);

        try {
            FenixFramework.atomic(() -> {
                log.saveMerchantTransactionId(merchantTransactionId);
                log.logRequestSendDate();
            });

            final MbWayCheckoutResultBean checkoutResultBean =
                    sibsOnlinePaymentsGateway.generateMbwayReference(payableAmount, merchantTransactionId, phoneNumber);

            final String sibsReferenceId = checkoutResultBean.getTransactionId();
            FenixFramework.atomic(() -> {
                log.logRequestReceiveDateAndData(checkoutResultBean.getTransactionId(), checkoutResultBean.isOperationSuccess(),
                        false, checkoutResultBean.getPaymentGatewayResultCode(),
                        checkoutResultBean.getOperationResultDescription());
                log.saveRequestAndResponsePayload(checkoutResultBean.getRequestLog(), checkoutResultBean.getResponseLog());
            });

            if (!checkoutResultBean.isOperationSuccess()) {
                throw new TreasuryDomainException(
                        "error.SibsOnlinePaymentsGatewayPaymentCodeGenerator.generateNewCodeFor.request.not.successful");
            }

            return createMbwayPaymentRequest(sibsOnlinePaymentsGateway, debtAccount, invoiceEntries, phoneNumber, payableAmount,
                    merchantTransactionId, sibsReferenceId);

        } catch (Exception e) {
            final boolean isOnlinePaymentsGatewayException = e instanceof OnlinePaymentsGatewayCommunicationException;

            FenixFramework.atomic(() -> {

                log.logRequestReceiveDateAndData(null, false, false, null, null);
                log.markExceptionOccuredAndSaveLog(e);

                if (isOnlinePaymentsGatewayException) {
                    log.saveRequestAndResponsePayload(((OnlinePaymentsGatewayCommunicationException) e).getRequestLog(),
                            ((OnlinePaymentsGatewayCommunicationException) e).getResponseLog());
                }
            });

            if (e instanceof TreasuryDomainException) {
                throw (TreasuryDomainException) e;
            } else {
                final String message = "error.SibsOnlinePaymentsGatewayPaymentCodeGenerator.generateNewCodeFor."
                        + (isOnlinePaymentsGatewayException ? "gateway.communication" : "unknown");

                throw new TreasuryDomainException(e, message);
            }
        }
    }

    @Atomic(mode = TxMode.WRITE)
    private static MbwayPaymentRequest createMbwayPaymentRequest(final SibsOnlinePaymentsGateway sibsOnlinePaymentsGateway,
            final DebtAccount debtAccount, final Set<InvoiceEntry> invoiceEntries, final String phoneNumber,
            final BigDecimal payableAmount, final String merchantTransactionId, final String sibsReferenceId) {
        return new MbwayPaymentRequest(sibsOnlinePaymentsGateway, debtAccount, invoiceEntries, payableAmount, phoneNumber,
                merchantTransactionId, sibsReferenceId);
    }

    @Atomic(mode = TxMode.WRITE)
    private static SibsOnlinePaymentsGatewayLog createLog(final SibsOnlinePaymentsGateway sibsGateway,
            final DebtAccount debtAccount) {
        return SibsOnlinePaymentsGatewayLog.createLogForRequestPaymentCode(sibsGateway, debtAccount);
    }

}
