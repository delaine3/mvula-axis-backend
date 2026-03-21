package com.mvula.axis.order.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.util.List;

@Data
public class OrderRequest {

  @NotBlank
  private String vendor;

  private String description;

  @NotBlank
  private String status;

  private String createdBy;

  private String updatedBy;

  @Valid
  private List<OrderItemRequest> items;
}