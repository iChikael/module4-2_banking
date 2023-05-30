package com.demo9.repository;

import com.demo9.model.Transfer;
import com.demo9.model.Withdraw;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WithdrawRepository extends JpaRepository<Withdraw, Long>{
}