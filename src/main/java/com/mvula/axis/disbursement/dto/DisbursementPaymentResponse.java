package com.mvula.axis.disbursement.dto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import lombok.Data;

@Data
public class DisbursementPaymentResponse {

  private Long id;
  private LocalDate datePaid;
  private BigDecimal amountPaid;
  private String paymentMethod;
  private String referenceNumber;
  private String notes;
  private LocalDateTime createdAt;
}
