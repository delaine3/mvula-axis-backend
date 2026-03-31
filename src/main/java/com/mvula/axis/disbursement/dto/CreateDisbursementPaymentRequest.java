package com.mvula.axis.disbursement.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDate;
import lombok.Data;

@Data
public class CreateDisbursementPaymentRequest {

  @NotNull(message = "Date paid is required")
  private LocalDate datePaid;

  @NotNull(message = "Amount paid is required")
  @DecimalMin(value = "0.01", message = "Amount paid must be greater than 0")
  private BigDecimal amountPaid;

  private String paymentMethod;

  private String notes;
}
