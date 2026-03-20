package com.mvula.axis.order.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Data;

@Data
public class OrderItemRequest {

  @NotBlank
  private String productName;

  @PositiveOrZero
  private Integer quantity;

  @PositiveOrZero
  private Double unitPrice;
}