package com.mvula.axis.disbursement.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.mvula.axis.disbursement.entity.DisbursementStatus;
import com.mvula.axis.disbursement.entity.PayeeType;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import lombok.Data;

@Data
public class DisbursementResponse {

  private Long id;
  private String payeeName;
  private PayeeType payeeType;
  private String serviceDescription;
  private BigDecimal totalCharged;
  private BigDecimal totalPaid;
  private BigDecimal balanceOutstanding;
  private String currency;

  @JsonFormat(pattern = "yyyy-MM-dd")
  private LocalDate dueDate;

  private DisbursementStatus status;
  private Boolean isInstallment;
  private Integer installmentCount;
  private String notes;
  private String createdBy;
  private String updatedBy;

  @JsonFormat(pattern = "dd-MM-yyyy HH:mm")
  private LocalDateTime createdAt;

  @JsonFormat(pattern = "dd-MM-yyyy HH:mm")
  private LocalDateTime updatedAt;

  private List<DisbursementPaymentResponse> payments;
}
