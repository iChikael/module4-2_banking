package com.demo9.service.transfer;

import com.demo9.model.Customer;
import com.demo9.model.Transfer;
import com.demo9.model.Withdraw;
import com.demo9.repository.TransferRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import javax.transaction.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;


@Service
@Transactional
public class TransferServiceImpl implements ITransferService {

    @Autowired
    private TransferRepository transferRepository;

    @Override
    public List<Transfer> findAll() {
        return transferRepository.findAll();
    }

    @Override
    public List<Transfer> findAllBySender(Customer sender) {
        return transferRepository.findAllBySender(sender);
    }

    @Override
    public BigDecimal getAllFeesAmount() {
        return transferRepository.getAllFeesAmount();
    }

    @Override
    public Optional<Transfer> findOne(Long id) {
        return Optional.empty();
    }

    @Override
    public List<Transfer> findALl() {
        return transferRepository.findAll();
    }

    @Override
    public Optional<Transfer> findById(Long id) {
        return Optional.empty();
    }

    @Override
    public Transfer save(Transfer transfer) {
        return null;
    }

    @Override
    public void deleteById(Long id) {

    }

    @Override
    public Customer delete(Transfer transfer) {
        return null;
    }

    @Override
    public Withdraw withdraw(Withdraw withdraw) {
        return null;
    }
}
