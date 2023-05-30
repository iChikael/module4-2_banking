package com.demo9.repository;

import com.demo9.model.Deposit;
import com.demo9.model.Transfer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DepositRepository extends JpaRepository<Deposit, Long>{
}
