package com.mvula.axis.disbursement.dto;

import com.mvula.axis.disbursement.entity.PayeeType;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDate;
import lombok.Data;

@Data
public class CreateDisbursementRequest {

  @NotBlank(message = "Payee name is required")
  private String payeeName;

  @NotNull(message = "Payee type is required")
  private PayeeType payeeType;

  @NotBlank(message = "Service description is required")
  private String serviceDescription;

  @NotNull(message = "Total charged is required")
  @DecimalMin(value = "0.01", message = "Total charged must be greater than 0")
  private BigDecimal totalCharged;

  private String currency;

  private LocalDate dueDate;

  @NotNull(message = "Installment flag is required")
  private Boolean isInstallment;

  private Integer installmentCount;

  private String notes;
}
