package com.mvula.axis.vendor.dto;

import java.time.LocalDateTime;
import lombok.Data;

@Data
public class VendorResponse {

  private Long id;
  private String name;
  private String category;
  private Boolean isActive;
  private String website;
  private String taxNumber;
  private AddressResponse address;
  private Boolean offersDelivery;
  private String contactPerson;
  private String contactNumber;
  private String email;
  private String notes;
  private String paymentTerms;
  private String preferredCurrency;
  private LocalDateTime createdAt;
  private LocalDateTime updatedAt;
}