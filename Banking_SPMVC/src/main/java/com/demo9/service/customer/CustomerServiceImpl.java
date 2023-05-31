package com.demo9.service.customer;

import com.demo9.model.*;
import com.demo9.repository.CustomerRepository;
import com.demo9.repository.DepositRepository;
import com.demo9.repository.TransferRepository;
import com.demo9.repository.WithdrawRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Optional;


@Service
@Transactional
public class CustomerServiceImpl implements ICustomerService {

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private DepositRepository depositRepository;

    @Autowired
    private TransferRepository transferRepository;

    @Autowired
    private WithdrawRepository withdrawRepository;

    @Override
    public List<Customer> findALl() {
        return customerRepository.findAll();
    }

    @Override
    public List<IDepositWithdrawHistory> getALlDepositWithdrawHistory() {
        return customerRepository.getALlDepositWithdrawHistory();
    }

    @Override
    public List<Customer> findAllByDeletedIsFalse() {
        return customerRepository.findAllByDeletedIsFalse();
    }

    @Override
    public List<Customer> findAllByIdNot(Long id) {
        return customerRepository.findAllByIdNot(id);
    }

    @Override
    public List<Customer> findAllByIdNotAndDeletedIsFalse(Long id) {
        return customerRepository.findAllByIdNotAndDeletedIsFalse(id);
    }

    @Override
    public Optional<Customer> findById(Long id) {
        return customerRepository.findById(id);
    }

    @Override
    public Customer save(Customer customer) {
        return customerRepository.save(customer);
    }

//    @Override
//    public Customer delete(Customer customer) {
//        customerRepository.delete(customer);
//        return customer;
//    }

    @Override
    public void incrementBalance(Long customerId, BigDecimal transactionAmount) {
        customerRepository.incrementBalance(customerId, transactionAmount);
    }

    @Override
    public Deposit deposit(Deposit deposit) {
        Customer customer = deposit.getCustomer();
        BigDecimal transactionAmount = deposit.getTransactionAmount();
        customerRepository.incrementBalance(customer.getId(), transactionAmount);

        deposit.setId(null);
        deposit.setCreatedAt(new Date());
        depositRepository.save(deposit);

        customer = customerRepository.findById(customer.getId()).get();
        deposit.setCustomer(customer);

        return deposit;
    }

    @Override
    public Transfer transfer(Transfer transfer) {

        BigDecimal transferAmount = transfer.getTransferAmount();
        BigDecimal transactionAmount = transfer.getTransactionAmount();

        customerRepository.decrementBalance(transfer.getSender().getId(), transactionAmount);

        customerRepository.incrementBalance(transfer.getRecipient().getId(), transferAmount);

        transferRepository.save(transfer);

        Customer sender = customerRepository.findById(transfer.getSender().getId()).get();
        transfer.setSender(sender);

        return transfer;
    }

    @Override
    public void deleteById(Long id) {

    }

    @Override
    public Withdraw withdraw(Withdraw withdraw) {
        Customer customer = withdraw.getCustomer();
        BigDecimal transactionAmount = withdraw.getTransactionAmount();
        BigDecimal balance = customer.getBalance();
        customerRepository.decrementBalance(customer.getId(), transactionAmount);

        withdraw.setId(null);
        withdraw.setCreatedAt(new Date());
        withdrawRepository.save(withdraw);

        customer = customerRepository.findById(customer.getId()).get();
        withdraw.setCustomer(customer);

        return withdraw;
    }


    public Customer delete(Customer customer) {
        customerRepository.delete(customer);
        return customer;
    }
}
