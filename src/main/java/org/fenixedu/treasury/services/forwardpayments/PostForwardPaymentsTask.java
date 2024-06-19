package org.fenixedu.treasury.services.forwardpayments;

import static com.qubit.terra.framework.tools.excel.ExcelUtil.createCellWithValue;
import static org.fenixedu.treasury.util.TreasuryConstants.treasuryBundle;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.apache.commons.lang.exception.ExceptionUtils;
import org.apache.poi.ss.usermodel.Row;
import org.fenixedu.bennu.scheduler.CronTask;
import org.fenixedu.bennu.scheduler.annotation.Task;
import org.fenixedu.treasury.domain.document.DebitEntry;
import org.fenixedu.treasury.domain.document.SettlementEntry;
import org.fenixedu.treasury.domain.document.SettlementNote;
import org.fenixedu.treasury.domain.exceptions.TreasuryDomainException;
import org.fenixedu.treasury.domain.forwardpayments.ForwardPaymentRequest;
import org.fenixedu.treasury.domain.forwardpayments.ForwardPaymentStateType;
import org.fenixedu.treasury.domain.forwardpayments.PostForwardPaymentsReportFile;
import org.fenixedu.treasury.domain.forwardpayments.implementations.IForwardPaymentPlatformService;
import org.fenixedu.treasury.domain.forwardpayments.implementations.PostProcessPaymentStatusBean;
import org.fenixedu.treasury.domain.payments.PaymentTransaction;
import org.fenixedu.treasury.util.TreasuryConstants;
import org.fenixedu.treasury.util.streaming.spreadsheet.ExcelSheet;
import org.fenixedu.treasury.util.streaming.spreadsheet.IErrorsLog;
import org.fenixedu.treasury.util.streaming.spreadsheet.Spreadsheet;
import org.fenixedu.treasury.util.streaming.spreadsheet.SpreadsheetRow;
import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import org.slf4j.Logger;

import com.google.common.collect.Lists;

import pt.ist.fenixframework.Atomic;
import pt.ist.fenixframework.Atomic.TxMode;
import pt.ist.fenixframework.FenixFramework;

@Deprecated
@Task(englishTitle = "Post forward payments registration", readOnly = true)
public class PostForwardPaymentsTask extends CronTask {

    @Override
    public void runTask() throws Exception {
        DateTime now = new DateTime();
        postForwardPaymentProcessService(now.minusDays(3), now, getLogger());
    }

    // @formatter: off
    /*********************************
     * POST FORWARD PAYMENTS SERVICE *
     *********************************/
    // @formatter: on

    @Atomic(mode = TxMode.READ)
    public static void postForwardPaymentProcessService(DateTime beginDate, DateTime endDate, Logger logger) throws IOException {
        if (beginDate == null || endDate == null) {
            throw new TreasuryDomainException("error.ForwardPayment.postForwardPaymentProcessService.dates.required");
        }

        final DateTime postForwardPaymentsExecutionDate = new DateTime();

        final List<PostForwardPaymentReportBean> result = Lists.newArrayList();

        ForwardPaymentRequest.findAllByStateType(ForwardPaymentStateType.CREATED, ForwardPaymentStateType.REQUESTED)
                .filter(f -> f.getRequestDate().compareTo(beginDate) >= 0 && f.getRequestDate().compareTo(endDate) < 0)
                .forEach(f -> {
                    try {
                        List<PostForwardPaymentReportBean> reportBeansList = updateForwardPayment(f, logger);
                        for (PostForwardPaymentReportBean bean : reportBeansList) {
                // @formatter:off
                            logger.info(String.format(
                                    "C\tPAYMENT REQUEST\t%s\t%s\t%s\t%s\t%s\t%s\t%s\t%s\t%s\t%s\t%s\t%s\t%s\t%s\t%s\t%s\t%s\n",
                                    bean.executionDate,
                                    bean.forwardPaymentExternalId,
                                    bean.forwardPaymentOrderNumber,
                                    bean.customerCode,
                                    bean.customerName,
                                    bean.previousStateDescription,
                                    bean.nextStateDescription,
                                    bean.paymentRegisteredWithSuccess,
                                    bean.settlementNote,
                                    bean.advancedPaymentCreditNote,
                                    bean.paymentDate,
                                    bean.paidAmount,
                                    bean.advancedCreditAmount != null ? bean.advancedCreditAmount.toString() : "",
                                    bean.transactionId,
                                    bean.statusCode,
                                    bean.statusMessage,
                                    bean.remarks
                                    ));

                            result.add(bean);
                            // @formatter:on
                        }
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                });

        writeExcel(result, postForwardPaymentsExecutionDate, beginDate, endDate);
    }

    private static void writeExcel(List<PostForwardPaymentReportBean> reportBeans, DateTime postForwardPaymentsExecutionDate,
            DateTime beginDate, DateTime endDate) {

        final byte[] content = Spreadsheet.buildSpreadsheetContent(() -> new ExcelSheet[] { new ExcelSheet() {

            @Override
            public String getName() {
                return treasuryBundle("label.PostForwardPaymentReportBean.sheet.name");
            }

            @Override
            public String[] getHeaders() {
                return new String[] { treasuryBundle("label.PostForwardPaymentReportBean.cell.executionDate"),
                        treasuryBundle("label.PostForwardPaymentReportBean.cell.forwardPaymentExternalId"),
                        treasuryBundle("label.PostForwardPaymentReportBean.cell.forwardPaymentOrderNumber"),
                        treasuryBundle("label.PostForwardPaymentReportBean.cell.forwardPaymentWhenOccured"),
                        treasuryBundle("label.PostForwardPaymentReportBean.cell.customerCode"),
                        treasuryBundle("label.PostForwardPaymentReportBean.cell.customerName"),
                        treasuryBundle("label.PostForwardPaymentReportBean.cell.previousStateDescription"),
                        treasuryBundle("label.PostForwardPaymentReportBean.cell.nextStateDescription"),
                        treasuryBundle("label.PostForwardPaymentReportBean.cell.paymentRegisteredWithSuccess"),
                        treasuryBundle("label.PostForwardPaymentReportBean.cell.settlementNote"),
                        treasuryBundle("label.PostForwardPaymentReportBean.cell.advancedCreditSettlementNote"),
                        treasuryBundle("label.PostForwardPaymentReportBean.cell.paymentDate"),
                        treasuryBundle("label.PostForwardPaymentReportBean.cell.paidAmount"),
                        treasuryBundle("label.PostForwardPaymentReportBean.cell.advancedCreditAmount"),
                        treasuryBundle("label.PostForwardPaymentReportBean.cell.transactionId"),
                        treasuryBundle("label.PostForwardPaymentReportBean.cell.statusCode"),
                        treasuryBundle("label.PostForwardPaymentReportBean.cell.statusMessage"),
                        treasuryBundle("label.PostForwardPaymentReportBean.cell.remarks") };
            }

            @Override
            public Stream<? extends SpreadsheetRow> getRows() {
                return reportBeans.stream();
            }

        } }, null);

        final String filename = treasuryBundle("label.PostForwardPaymentsReportFile.filename",
                postForwardPaymentsExecutionDate.toString("yyyy_MM_dd_HH_mm_ss"));

        PostForwardPaymentsReportFile.create(postForwardPaymentsExecutionDate, beginDate, endDate, filename, content);
    }

    private static List<PostForwardPaymentReportBean> updateForwardPayment(ForwardPaymentRequest forwardPayment,
            final Logger logger) throws IOException {
        final List<PostForwardPaymentReportBean> result = new ArrayList<>();

        try {
            FenixFramework.atomic(() -> {

                final IForwardPaymentPlatformService service =
                        forwardPayment.getDigitalPaymentPlatform().castToForwardPaymentPlatformService();
                final String justification = treasuryBundle("error.PostForwardPaymentsTask.post.payment.justification");

                final PostProcessPaymentStatusBean postProcessPaymentStatusBean =
                        service.postProcessPayment(forwardPayment, justification, Optional.empty());

                if (forwardPayment.getPaymentTransactionsSet().isEmpty()) {
                    result.add(new PostForwardPaymentReportBean(forwardPayment, postProcessPaymentStatusBean));
                }

                for (PaymentTransaction paymentTransaction : forwardPayment.getPaymentTransactionsSet()) {
                    for (SettlementNote settlementNote : paymentTransaction.getSettlementNotesSet()) {
                        result.add(new PostForwardPaymentReportBean(paymentTransaction, settlementNote,
                                postProcessPaymentStatusBean));
                    }
                }
            });

        } catch (Exception e) {
            final String message = e.getMessage();
            final String stackTrace = ExceptionUtils.getStackTrace(e);

            String exceptionOutput = String.format("E\tERROR ON\t%s\t%s\n", forwardPayment.getExternalId(), message);
            logger.error(exceptionOutput);
            logger.error(stackTrace + "\n");
        }

        return result;
    }

    private static class PostForwardPaymentReportBean implements SpreadsheetRow {

        private String executionDate;
        private String forwardPaymentExternalId;
        private String forwardPaymentOrderNumber;
        private String forwardPaymentWhenOccured;
        private String customerCode;
        private String customerName;
        private String previousStateDescription;
        private String nextStateDescription;
        private boolean paymentRegisteredWithSuccess;
        private String settlementNote = "";
        private String advancedPaymentCreditNote = "";
        private String paymentDate = "";
        private String paidAmount = "";
        private BigDecimal advancedCreditAmount;
        private String transactionId = "";
        private String statusCode;
        private String statusMessage;
        private String remarks = "";

        private PostForwardPaymentReportBean(ForwardPaymentRequest forwardPayment,
                PostProcessPaymentStatusBean postProcessPaymentStatusBean) {

            this.executionDate = new DateTime().toString(TreasuryConstants.DATE_TIME_FORMAT_YYYY_MM_DD);
            this.forwardPaymentExternalId = forwardPayment.getExternalId();
            this.forwardPaymentOrderNumber = "" + forwardPayment.getOrderNumber();
            this.forwardPaymentWhenOccured =
                    forwardPayment.getRequestDate().toString(TreasuryConstants.DATE_TIME_FORMAT_YYYY_MM_DD);
            this.customerCode = forwardPayment.getDebtAccount().getCustomer().getBusinessIdentification();
            this.customerName = forwardPayment.getDebtAccount().getCustomer().getName();
            this.previousStateDescription = postProcessPaymentStatusBean.getPreviousState().getLocalizedName().getContent();
            this.nextStateDescription =
                    postProcessPaymentStatusBean.getForwardPaymentStatusBean().getStateType().getLocalizedName().getContent();
            this.paymentRegisteredWithSuccess = postProcessPaymentStatusBean.isSuccess();

        }

        private PostForwardPaymentReportBean(PaymentTransaction paymentTransaction, SettlementNote settlementNote,
                PostProcessPaymentStatusBean postProcessPaymentStatusBean) {
            ForwardPaymentRequest forwardPayment = (ForwardPaymentRequest) paymentTransaction.getPaymentRequest();

            this.executionDate = new DateTime().toString(TreasuryConstants.DATE_TIME_FORMAT_YYYY_MM_DD);
            this.forwardPaymentExternalId = forwardPayment.getExternalId();
            this.forwardPaymentOrderNumber = "" + forwardPayment.getOrderNumber();
            this.forwardPaymentWhenOccured =
                    forwardPayment.getRequestDate().toString(TreasuryConstants.DATE_TIME_FORMAT_YYYY_MM_DD);
            this.customerCode = forwardPayment.getDebtAccount().getCustomer().getBusinessIdentification();
            this.customerName = forwardPayment.getDebtAccount().getCustomer().getName();
            this.previousStateDescription = postProcessPaymentStatusBean.getPreviousState().getLocalizedName().getContent();
            this.nextStateDescription =
                    postProcessPaymentStatusBean.getForwardPaymentStatusBean().getStateType().getLocalizedName().getContent();
            this.paymentRegisteredWithSuccess = postProcessPaymentStatusBean.isSuccess();

            this.settlementNote = settlementNote.getUiDocumentNumber();
            this.paymentDate = settlementNote.getPaymentDate().toString(TreasuryConstants.DATE_TIME_FORMAT_YYYY_MM_DD);
            this.paidAmount = settlementNote.getTotalPayedAmount().toString();
            this.transactionId = paymentTransaction.getTransactionId();

            if (settlementNote.getAdvancedPaymentCreditNote() != null) {
                this.advancedPaymentCreditNote = settlementNote.getAdvancedPaymentCreditNote().getUiDocumentNumber();
                this.advancedCreditAmount = settlementNote.getAdvancedPaymentCreditNote().getTotalAmount();
            }

            if (hasSettlementNotesOnSameDayForSameDebts(forwardPayment, settlementNote)) {
                remarks = treasuryBundle("warn.PostForwardPaymentsTask.settlement.notes.on.same.day.for.same.debts");
            }

            this.statusCode = postProcessPaymentStatusBean.getForwardPaymentStatusBean().getStatusCode();
            this.statusMessage = postProcessPaymentStatusBean.getForwardPaymentStatusBean().getStatusMessage();
        }

        private boolean hasSettlementNotesOnSameDayForSameDebts(final ForwardPaymentRequest forwardPayment,
                SettlementNote settlementNote) {
            final LocalDate paymentDate = settlementNote.getPaymentDate().toLocalDate();

            final Set<DebitEntry> forwardPaymentDebitEntriesSet = forwardPayment.getDebitEntriesSet();

            for (SettlementNote s : SettlementNote.findByDebtAccount(forwardPayment.getDebtAccount())
                    .collect(Collectors.toSet())) {
                if (s == settlementNote) {
                    continue;
                }

                if (s.isAnnulled()) {
                    continue;
                }

                if (!s.getPaymentDate().toLocalDate().isEqual(paymentDate)) {
                    continue;
                }

                for (final SettlementEntry settlementEntry : s.getSettlemetEntriesSet()) {
                    if (forwardPaymentDebitEntriesSet.contains(settlementEntry.getInvoiceEntry())) {
                        return true;
                    }
                }
            }

            return false;
        }

        @Override
        public void writeCellValues(final Row row, final IErrorsLog errorsLog) {
            int i = 0;

            createCellWithValue(row, i++, executionDate);
            createCellWithValue(row, i++, forwardPaymentExternalId);
            createCellWithValue(row, i++, forwardPaymentOrderNumber);
            createCellWithValue(row, i++, forwardPaymentWhenOccured);
            createCellWithValue(row, i++, customerCode);
            createCellWithValue(row, i++, customerName);
            createCellWithValue(row, i++, previousStateDescription);
            createCellWithValue(row, i++, nextStateDescription);
            createCellWithValue(row, i++, treasuryBundle("label." + paymentRegisteredWithSuccess));
            createCellWithValue(row, i++, settlementNote);
            createCellWithValue(row, i++, advancedPaymentCreditNote);
            createCellWithValue(row, i++, paymentDate);
            createCellWithValue(row, i++, paidAmount);
            createCellWithValue(row, i++, advancedCreditAmount != null ? advancedCreditAmount.toString() : "");
            createCellWithValue(row, i++, transactionId);
            createCellWithValue(row, i++, statusCode);
            createCellWithValue(row, i++, statusMessage);
            createCellWithValue(row, i++, remarks);
        }

    }

}
