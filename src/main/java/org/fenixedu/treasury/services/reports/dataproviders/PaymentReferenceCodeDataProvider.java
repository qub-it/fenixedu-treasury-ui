package org.fenixedu.treasury.services.reports.dataproviders;

import java.math.BigDecimal;

import org.fenixedu.treasury.domain.paymentcodes.FinantialDocumentPaymentCode;
import org.fenixedu.treasury.domain.paymentcodes.PaymentReferenceCode;
import org.joda.time.LocalDate;

import com.qubit.terra.docs.util.IDocumentFieldsData;
import com.qubit.terra.docs.util.IFieldsExporter;
import com.qubit.terra.docs.util.IReportDataProvider;

public class PaymentReferenceCodeDataProvider extends AbstractDataProvider implements IReportDataProvider {

    protected static final String PAYMENT_CODE_KEY = "paymentCode";

    private PaymentReferenceCode paymentCode;

    public PaymentReferenceCodeDataProvider(final PaymentReferenceCode paymentCode) {
        this.setPaymentCode(paymentCode);
        registerKey(PAYMENT_CODE_KEY, PaymentReferenceCodeDataProvider::handlePaymentCodeKey);
    }

    private static Object handlePaymentCodeKey(IReportDataProvider provider) {
        PaymentReferenceCodeDataProvider regisProvider = (PaymentReferenceCodeDataProvider) provider;
        return regisProvider.getPaymentCode();
    }

    @Override
    public void registerFieldsAndImages(IDocumentFieldsData arg0) {
        // TODO Auto-generated method stub
    }

    @Override
    public void registerFieldsMetadata(IFieldsExporter arg0) {
        // TODO Auto-generated method stub

    }

    public PaymentReferenceCode getPaymentCode() {
        return paymentCode;
    }

    public void setPaymentCode(PaymentReferenceCode paymentCode) {
        this.paymentCode = paymentCode;
    }

    public String getDescription() {
        return paymentCode.getTargetPayment().getDescription();
    }

    public BigDecimal getAmount() {
        return paymentCode.getPayableAmount();
    }

    public LocalDate getDueDate() {
        if (paymentCode.getPaymentCodePool().getIsVariableTimeWindow()) {
            return paymentCode.getEndDate();
        } else {
            if (paymentCode.getTargetPayment() instanceof FinantialDocumentPaymentCode) {
                ((FinantialDocumentPaymentCode) paymentCode.getTargetPayment()).getFinantialDocument().getDocumentDueDate();
            }
        }

        //default get the Payment EndDate
        return paymentCode.getEndDate();

    }

}
