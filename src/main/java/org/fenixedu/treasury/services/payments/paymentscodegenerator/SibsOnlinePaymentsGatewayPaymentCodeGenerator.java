package org.fenixedu.treasury.services.payments.paymentscodegenerator;

import java.math.BigDecimal;

import org.apache.commons.lang.exception.ExceptionUtils;
import org.fenixedu.onlinepaymentsgateway.api.MbCheckoutResultBean;
import org.fenixedu.onlinepaymentsgateway.exceptions.OnlinePaymentsGatewayCommunicationException;
import org.fenixedu.treasury.domain.debt.DebtAccount;
import org.fenixedu.treasury.domain.exceptions.TreasuryDomainException;
import org.fenixedu.treasury.domain.paymentcodes.PaymentReferenceCode;
import org.fenixedu.treasury.domain.paymentcodes.PaymentReferenceCodeStateType;
import org.fenixedu.treasury.domain.paymentcodes.pool.PaymentCodePool;
import org.fenixedu.treasury.domain.sibsonlinepaymentsgateway.SibsOnlinePaymentsGateway;
import org.fenixedu.treasury.domain.sibsonlinepaymentsgateway.SibsOnlinePaymentsGatewayLog;
import org.fenixedu.treasury.dto.document.managepayments.PaymentReferenceCodeBean;
import org.joda.time.LocalDate;

import com.google.common.base.Strings;
import com.google.common.collect.Sets;

import pt.ist.fenixframework.Atomic;
import pt.ist.fenixframework.Atomic.TxMode;
import pt.ist.fenixframework.FenixFramework;

public class SibsOnlinePaymentsGatewayPaymentCodeGenerator implements IPaymentCodeGenerator {

    private PaymentCodePool paymentCodePool;

    public SibsOnlinePaymentsGatewayPaymentCodeGenerator(final PaymentCodePool paymentCodePool) {
        this.paymentCodePool = paymentCodePool;
    }

    @Override
    @Atomic(mode = TxMode.READ)
    public PaymentReferenceCode createPaymentReferenceCode(final DebtAccount debtAccount, final PaymentReferenceCodeBean bean) {

        final BigDecimal paymentAmount = bean.getPaymentAmount();

        final PaymentReferenceCode paymentReferenceCode = generateNewCodeFor(debtAccount, paymentAmount, new LocalDate(),
                new LocalDate().plusMonths(1) /* bean.getEndDate()*/);

        FenixFramework.atomic(() -> {
            paymentReferenceCode.createPaymentTargetTo(Sets.newHashSet(bean.getSelectedDebitEntries()), paymentAmount);
        });

        return paymentReferenceCode;
    }

    private PaymentReferenceCode generateNewCodeFor(final DebtAccount debtAccount, final BigDecimal amount, LocalDate validFrom,
            LocalDate validTo) {
        final SibsOnlinePaymentsGateway sibsGateway = this.paymentCodePool.getSibsOnlinePaymentsGateway();
        final String merchantTransactionId = sibsGateway.generateNewMerchantTransactionId();

        final SibsOnlinePaymentsGatewayLog log = createLog(sibsGateway, debtAccount);

        try {

            FenixFramework.atomic(() -> {
                log.saveMerchantTransactionId(merchantTransactionId);
                log.logRequestSendDate();
            });

            final MbCheckoutResultBean checkoutResultBean =
                    sibsGateway.generateMBPaymentReference(amount, validFrom.toDateTimeAtStartOfDay(),
                            validTo.toDateTimeAtStartOfDay().plusDays(1).minusSeconds(1), merchantTransactionId);

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

            final String paymentCode = checkoutResultBean.getPaymentReference();

            if (Strings.isNullOrEmpty(paymentCode)) {
                throw new TreasuryDomainException(
                        "error.SibsOnlinePaymentsGatewayPaymentCodeGenerator.generateNewCodeFor.reference.not.empty");
            }

            return createPaymentReferenceCode(amount, validFrom, validTo, log, paymentCode, merchantTransactionId,
                    sibsReferenceId);
        } catch (final Exception e) {
            final boolean isOnlinePaymentsGatewayException = e instanceof OnlinePaymentsGatewayCommunicationException;

            FenixFramework.atomic(() -> {

                log.logRequestReceiveDateAndData(null, false, false, null, null);
                log.markExceptionOccuredAndSaveLog(e);

                if (isOnlinePaymentsGatewayException) {
                    log.saveRequestAndResponsePayload(((OnlinePaymentsGatewayCommunicationException) e).getRequestLog(),
                            ((OnlinePaymentsGatewayCommunicationException) e).getResponseLog());
                }
            });

            throw new TreasuryDomainException(e,
                    isOnlinePaymentsGatewayException ? "error.SibsOnlinePaymentsGatewayPaymentCodeGenerator.generateNewCodeFor.gateway.communication" : "error.SibsOnlinePaymentsGatewayPaymentCodeGenerator.generateNewCodeFor.unknown");
        }
    }

    @Atomic(mode = TxMode.WRITE)
    private PaymentReferenceCode createPaymentReferenceCode(final BigDecimal amount, final LocalDate validFrom,
            final LocalDate validTo, final SibsOnlinePaymentsGatewayLog log, final String paymentCode,
            final String sibsMerchantTransactionId, final String sibsReferenceId) throws Exception {
        log.savePaymentCode(paymentCode);

        return PaymentReferenceCode.createForSibsOnlinePaymentGateway(paymentCode, validFrom, validTo,
                PaymentReferenceCodeStateType.USED, this.paymentCodePool, amount, sibsMerchantTransactionId, sibsReferenceId);
    }

    @Atomic(mode = TxMode.WRITE)
    private SibsOnlinePaymentsGatewayLog createLog(final SibsOnlinePaymentsGateway sibsGateway, final DebtAccount debtAccount) {
        return SibsOnlinePaymentsGatewayLog.createLogForRequestPaymentCode(sibsGateway, debtAccount);
    }

    @Override
    public PaymentCodePool getReferenceCodePool() {
        return this.paymentCodePool;
    }

    @Override
    public boolean isSibsMerchantTransactionAndReferenceIdRequired() {
        return true;
    }

}
