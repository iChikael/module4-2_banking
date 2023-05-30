package com.demo9.service.withdraw;

import com.demo9.model.Customer;
import com.demo9.model.Withdraw;
import com.demo9.repository.WithdrawRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class WithdrawServiceImpl implements IWithdrawService {

    @Autowired
    private WithdrawRepository withdrawRepository;


    @Override
    public List<Withdraw> findALl() {
        return withdrawRepository.findAll();
    }

    @Override
    public Optional<Withdraw> findById(Long id) {
        return Optional.empty();
    }

    @Override
    public Withdraw save(Withdraw withdraw) {
        return null;
    }

    @Override
    public void deleteById(Long id) {

    }

    @Override
    public Customer delete(Withdraw withdraw) {

        return null;
    }

    @Override
    public Withdraw withdraw(Withdraw withdraw) {
        return null;
    }

    @Override
    public List<Withdraw> findAll() {
        return withdrawRepository.findAll();
    }
}
