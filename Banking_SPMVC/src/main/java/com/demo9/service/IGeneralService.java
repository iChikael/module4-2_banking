package com.demo9.service;

import com.demo9.model.Customer;
import com.demo9.model.Withdraw;


import java.util.List;
import java.util.Optional;

public interface IGeneralService<T> {

    List<T> findALl();

    Optional<T> findById(Long id);

    T save(T t);

    void deleteById(Long id);

    Customer delete(T t);

    Withdraw withdraw(Withdraw withdraw);

}
