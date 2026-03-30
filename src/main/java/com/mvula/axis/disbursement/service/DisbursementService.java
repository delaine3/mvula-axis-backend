package com.mvula.axis.disbursement.service;

import com.mvula.axis.disbursement.dto.CreateDisbursementPaymentRequest;
import com.mvula.axis.disbursement.dto.CreateDisbursementRequest;
import com.mvula.axis.disbursement.dto.DisbursementResponse;
import java.util.List;

public interface DisbursementService {

  DisbursementResponse createDisbursement(CreateDisbursementRequest request);

  List<DisbursementResponse> getAllDisbursements();

  DisbursementResponse getDisbursementById(Long id);

  DisbursementResponse addPayment(Long disbursementId, CreateDisbursementPaymentRequest request);
}
