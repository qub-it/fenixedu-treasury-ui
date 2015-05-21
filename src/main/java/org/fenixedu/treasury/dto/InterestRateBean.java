package org.fenixedu.treasury.dto;

import java.io.Serializable;
import java.math.BigDecimal;

import org.fenixedu.bennu.IBean;
import org.fenixedu.treasury.domain.tariff.InterestType;

public class InterestRateBean implements IBean, Serializable {

    private static final long serialVersionUID = 1L;

    private InterestType interestType;

    private int numberOfDays;

    private int numberOfMonths;

    private BigDecimal diaryRate;

    private BigDecimal monthlyRate;

    private BigDecimal amount;

    public InterestRateBean() {
        diaryRate = BigDecimal.ZERO;
        monthlyRate = BigDecimal.ZERO;
        amount = BigDecimal.ZERO;
    }

    public InterestRateBean(InterestType interestType, BigDecimal amount) {
        this.interestType = interestType;
        this.amount = amount;
    }

    public void initDiaryInterestRate(InterestType interestType, int numberOfDays, BigDecimal diaryRate, BigDecimal amount) {
        this.interestType = interestType;
        this.numberOfDays = numberOfDays;
        this.diaryRate = diaryRate;
        this.amount = amount;
    }

    public void initMonthlyInterestRate(InterestType interestType, int numberOfMonths, BigDecimal monthlyRate, BigDecimal amount) {
        this.interestType = interestType;
        this.numberOfMonths = numberOfMonths;
        this.monthlyRate = monthlyRate;
        this.amount = amount;
    }

    public InterestType getInterestType() {
        return interestType;
    }

    public void setInterestType(InterestType interestType) {
        this.interestType = interestType;
    }

    public int getNumberOfDays() {
        return numberOfDays;
    }

    public void setNumberOfDays(int numberOfDays) {
        this.numberOfDays = numberOfDays;
    }

    public int getNumberOfMonths() {
        return numberOfMonths;
    }

    public void setNumberOfMonths(int numberOfMonths) {
        this.numberOfMonths = numberOfMonths;
    }

    public BigDecimal getDiaryRate() {
        return diaryRate;
    }

    public void setDiaryRate(BigDecimal diaryRate) {
        this.diaryRate = diaryRate;
    }

    public BigDecimal getMonthRate() {
        return monthlyRate;
    }

    public void setMonthRate(BigDecimal monthlyRate) {
        this.monthlyRate = monthlyRate;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }
}
