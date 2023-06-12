package com.cg.service.customer;

import com.cg.model.Customer;
import com.cg.model.Deposit;
import com.cg.model.Transfer;
import com.cg.model.Withdraw;
import com.cg.model.dto.CustomerDTO;
import com.cg.service.IGeneralService;

import java.util.List;
import java.util.Optional;

public interface ICustomerService extends IGeneralService<Customer> {
    List<CustomerDTO> findAllCustomerDTO();

    List<Customer> findAllByIdNotAndDeletedIsFalse(Long id);

    List<CustomerDTO> findAllCustomerDTOByDeletedIsFalse();

    Optional<CustomerDTO> findCustomerDTOByIdAndDeletedIsFalse(Long id);
    List<CustomerDTO> findAllByDeletedIsFalse();

    Boolean existsByEmailEquals(String email);


    Deposit deposit(Deposit deposit);

    Withdraw withdraw(Withdraw withdraw);

    Transfer transfer(Transfer transfer);

}
