package com.demo9.repository;

import com.demo9.model.Customer;
import com.demo9.model.Transfer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

@Repository
public interface TransferRepository extends JpaRepository<Transfer, Long> {
    @Query("SELECT SUM(tr.feesAmount) FROM Transfer AS tr")
    BigDecimal getAllFeesAmount();

    List<Transfer> findAllBySender(Customer sender);
}

