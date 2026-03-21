package com.mvula.axis.order.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class OrderResponse {

  private Long id;
  private String vendor;
  private String description;
  private String status;
  private BigDecimal totalAmount;
  private String createdBy;
  private String updatedBy;
  private LocalDateTime createdAt;
  private LocalDateTime updatedAt;
  private List<OrderItemResponse> items;
}