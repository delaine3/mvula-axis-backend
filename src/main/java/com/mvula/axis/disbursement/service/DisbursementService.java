package com.mvula.axis.disbursement.service;

import com.mvula.axis.disbursement.dto.CreateDisbursementPaymentRequest;
import com.mvula.axis.disbursement.dto.CreateDisbursementRequest;
import com.mvula.axis.disbursement.dto.DisbursementResponse;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface DisbursementService {

  DisbursementResponse createDisbursement(CreateDisbursementRequest request);

  Page<DisbursementResponse> getAllDisbursements(Pageable pageable);

  DisbursementResponse getDisbursementById(Long id);

  DisbursementResponse addPayment(Long disbursementId, CreateDisbursementPaymentRequest request);
}
