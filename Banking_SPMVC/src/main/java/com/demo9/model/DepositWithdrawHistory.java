package com.demo9.model;

import java.math.BigDecimal;

public class DepositWithdrawHistory {

    private String fullName;
    private BigDecimal transactionAmount;
    private String transactionType;

    public DepositWithdrawHistory() {
    }

    public DepositWithdrawHistory(String fullName, BigDecimal transactionAmount, String transactionType) {
        this.fullName = fullName;
        this.transactionAmount = transactionAmount;
        this.transactionType = transactionType;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public BigDecimal getTransactionAmount() {
        return transactionAmount;
    }

    public void setTransactionAmount(BigDecimal transactionAmount) {
        this.transactionAmount = transactionAmount;
    }

    public String getTransactionType() {
        return transactionType;
    }

    public void setTransactionType(String transactionType) {
        this.transactionType = transactionType;
    }
}
