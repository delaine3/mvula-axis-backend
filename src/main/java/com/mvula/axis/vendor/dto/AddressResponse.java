package com.mvula.axis.vendor.dto;

import lombok.Data;

@Data
public class AddressResponse {

  private String addressLine1;
  private String addressLine2;
  private String city;
  private String stateOrProvince;
  private String postalCode;
  private String country;
}
