package com.cg.repository;

import com.cg.model.Transfer;
import com.cg.model.dto.TransferViewDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

@Repository
public interface TransferRepository extends JpaRepository<Transfer, Long> {
    @Query("SELECT SUM(t.feesAmount) FROM Transfer AS t")
    BigDecimal getSumFeesAmount();



//    @Query(value = "SELECT NEW com.cg.model.dto.TransferViewDTO (" +
//            "transf.id, " +
//            "transf.sender.id, " +
//            "transf.sender.fullName, " +
//            "transf.recipient.id, " +
//            "transf.recipient.fullName, " +
//            "transf.transferAmount, " +
//            "transf.feesAmount, " +
//            "transf.transactionAmount," +
//            "transf.createdAt" +
//            ") " +
//            "FROM Transfer AS transf "
//    )
//    List<TransferViewDTO> findAllTransferViewDTO();
}
