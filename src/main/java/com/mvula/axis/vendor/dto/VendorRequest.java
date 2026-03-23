package com.mvula.axis.vendor.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class VendorRequest {

  @NotBlank private String name;

  private String category;

  private Boolean isActive = true;

  private String website;

  private String taxNumber;

  @Valid private AddressRequest address;

  private Boolean offersDelivery = false;

  private String contactPerson;

  private String contactNumber;

  @Email private String email;

  private String notes;

  private String paymentTerms;

  private String preferredCurrency;
}
