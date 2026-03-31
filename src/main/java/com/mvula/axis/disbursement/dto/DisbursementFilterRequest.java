package com.mvula.axis.disbursement.dto;

import com.mvula.axis.disbursement.entity.DisbursementStatus;
import com.mvula.axis.disbursement.entity.PayeeType;
import lombok.Data;

@Data
public class DisbursementFilterRequest {
  private String payeeName;
  private PayeeType payeeType;
  private DisbursementStatus status;
  private String serviceDescription;
}
