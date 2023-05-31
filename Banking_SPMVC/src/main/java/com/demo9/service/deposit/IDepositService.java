package com.demo9.service.deposit;

import com.demo9.model.Customer;
import com.demo9.model.Deposit;
import com.demo9.model.Transfer;
import com.demo9.service.IGeneralService;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

public interface IDepositService extends IGeneralService<Deposit> {
    List<Deposit> findAll();
}
