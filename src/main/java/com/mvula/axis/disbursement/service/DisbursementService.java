package com.mvula.axis.disbursement.service;

import com.mvula.axis.disbursement.dto.CreateDisbursementPaymentRequest;
import com.mvula.axis.disbursement.dto.CreateDisbursementRequest;
import com.mvula.axis.disbursement.dto.DisbursementResponse;
import com.mvula.axis.disbursement.entity.DisbursementStatus;
import com.mvula.axis.disbursement.entity.PayeeType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface DisbursementService {

  DisbursementResponse createDisbursement(CreateDisbursementRequest request);

  Page<DisbursementResponse> getAllDisbursements(
      String search,
      String payeeName,
      PayeeType payeeType,
      DisbursementStatus status,
      String serviceDescription,
      Pageable pageable);

  DisbursementResponse getDisbursementById(Long id);

  DisbursementResponse addPayment(Long disbursementId, CreateDisbursementPaymentRequest request);

  DisbursementResponse updateDisbursement(Long id, CreateDisbursementRequest request);

  DisbursementResponse cancelDisbursement(Long id);

  void deleteDisbursement(Long id);
}
