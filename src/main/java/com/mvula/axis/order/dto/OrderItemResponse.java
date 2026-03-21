package com.mvula.axis.order.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class OrderItemResponse {

  private Long id;
  private String productName;
  private Integer quantity;
  private BigDecimal unitPrice;
  private BigDecimal lineTotal;
}