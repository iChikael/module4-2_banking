package com.demo9.service.withdraw;

import com.demo9.model.Deposit;
import com.demo9.model.Withdraw;
import com.demo9.service.IGeneralService;

import java.util.List;

public interface IWithdrawService extends IGeneralService<Withdraw> {
    List<Withdraw> findAll();

}
