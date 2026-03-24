package com.mvula.axis.order.dto;

import com.mvula.axis.vendor.dto.VendorRequest;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import java.util.List;
import lombok.Data;

@Data
public class OrderRequest {

  private Long vendorId;

  @Valid private VendorRequest newVendor;

  private String description;

  private Boolean isPaid;

  @NotBlank private String status;

  private String createdBy;

  private String updatedBy;

  @Valid private List<OrderItemRequest> items;
}
