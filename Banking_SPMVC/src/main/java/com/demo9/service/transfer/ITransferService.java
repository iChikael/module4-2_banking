package com.demo9.service.transfer;

import com.demo9.model.Customer;
import com.demo9.model.Transfer;
import com.demo9.service.IGeneralService;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
//@Service
public interface ITransferService extends IGeneralService<Transfer> {
    List<Transfer> findAll();

    List<Transfer> findAllBySender(Customer sender);

    BigDecimal getAllFeesAmount();

    Optional<Transfer> findOne(Long id);

    Transfer save(Transfer transfer);
}
