package com.mvula.axis.order.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import lombok.Data;

@Data
public class OrderResponse {

  private Long id;
  private Long vendorId;
  private String vendorName;
  private Boolean isPaid;
  private String description;
  private String status;
  private BigDecimal totalAmount;
  private String createdBy;
  private String updatedBy;

  @JsonFormat(pattern = "dd-MM-yyyy")
  private LocalDateTime createdAt;

  @JsonFormat(pattern = "dd-MM-yyyy")
  private LocalDateTime updatedAt;

  private List<OrderItemResponse> items;
}
