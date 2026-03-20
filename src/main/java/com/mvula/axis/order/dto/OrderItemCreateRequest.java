package com.mvula.axis.order.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class OrderItemCreateRequest {

  @NotBlank
  private String productName;

  @PositiveOrZero
  private Integer quantity;

  @DecimalMin(value = "0.0", inclusive = true)
  private BigDecimal unitPrice;
}