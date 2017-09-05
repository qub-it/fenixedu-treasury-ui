package org.fenixedu.treasury.services.forwardpayments;

import java.util.List;
import java.util.concurrent.Callable;
import java.util.stream.Stream;

import org.apache.commons.lang.exception.ExceptionUtils;
import org.apache.poi.ss.usermodel.Row;
import org.fenixedu.bennu.scheduler.CronTask;
import org.fenixedu.bennu.scheduler.annotation.Task;
import org.fenixedu.treasury.domain.exceptions.TreasuryDomainException;
import org.fenixedu.treasury.domain.forwardpayments.ForwardPayment;
import org.fenixedu.treasury.domain.forwardpayments.ForwardPaymentStateType;
import org.fenixedu.treasury.domain.forwardpayments.PostForwardPaymentsReportFile;
import org.fenixedu.treasury.domain.forwardpayments.implementations.IForwardPaymentImplementation;
import org.fenixedu.treasury.domain.forwardpayments.implementations.PostProcessPaymentStatusBean;
import org.fenixedu.treasury.util.Constants;
import org.fenixedu.treasury.util.streaming.spreadsheet.ExcelSheet;
import org.fenixedu.treasury.util.streaming.spreadsheet.IErrorsLog;
import org.fenixedu.treasury.util.streaming.spreadsheet.Spreadsheet;
import org.fenixedu.treasury.util.streaming.spreadsheet.SpreadsheetRow;
import org.joda.time.DateTime;

import com.google.common.collect.Lists;

import pt.ist.fenixframework.FenixFramework;

@Task(englishTitle = "Post forward payments registration", readOnly = true)
public class PostForwardPaymentsTask extends CronTask {

    @Override
    public void runTask() throws Exception {
        DateTime now = new DateTime();
        ForwardPayment.postForwardPaymentProcessService(now.minusDays(3), now, getTaskLogWriter());
    }


}
