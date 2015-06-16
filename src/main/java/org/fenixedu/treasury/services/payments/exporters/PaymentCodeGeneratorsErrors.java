package org.fenixedu.treasury.services.payments.exporters;

import java.util.List;

import org.apache.commons.lang.exception.ExceptionUtils;

import com.google.common.collect.Lists;

public class PaymentCodeGeneratorsErrors {

    protected List<String> errors = Lists.newArrayList();

    public PaymentCodeGeneratorsErrors() {

    }

    public List<String> getErrors() {
        return errors;
    }

    public synchronized void recordError(final String error) {
        errors.add(error);
    }

    public synchronized void recordError(final Throwable error) {
        errors.add(ExceptionUtils.getFullStackTrace(error));
    }

    @Override
    public String toString() {
        StringBuilder output = new StringBuilder();
        for (String error : getErrors()) {
            output.append(error).append("\n");
        }
        return output.toString();
    }
}
