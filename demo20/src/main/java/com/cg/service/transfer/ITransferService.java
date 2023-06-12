package com.cg.service.transfer;

import com.cg.model.Transfer;
import com.cg.model.dto.TransferViewDTO;
import com.cg.service.IGeneralService;

import java.math.BigDecimal;
import java.util.List;

public interface ITransferService extends IGeneralService<Transfer> {
     List<Transfer> findALl();

     List<TransferViewDTO> findAllTransferViewDTO();

     BigDecimal getSumFeesAmount();


}
