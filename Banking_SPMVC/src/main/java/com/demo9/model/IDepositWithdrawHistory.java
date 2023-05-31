package com.demo9.model;

import java.math.BigDecimal;

public interface IDepositWithdrawHistory {

    String getFullName();
    BigDecimal getTransactionAmount();
    String getTransactionType();
}
