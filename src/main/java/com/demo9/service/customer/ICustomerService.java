package com.demo9.service.customer;

import com.demo9.model.Customer;

import com.demo9.model.Deposit;
import com.demo9.model.Transfer;
import com.demo9.service.IGeneralService;

import java.math.BigDecimal;
import java.util.List;

public interface ICustomerService extends IGeneralService<Customer> {

    List<Customer> findAllByDeletedIsFalse();

    List<Customer> findAllByIdNot(Long id);

    List<Customer> findAllByIdNotAndDeletedIsFalse(Long id);

    void incrementBalance(Long customerId, BigDecimal transactionAmount);

    Deposit deposit(Deposit deposit);

    Transfer transfer(Transfer transfer);
    Customer save(Customer customer);

    Customer delete(Customer customer);

}
