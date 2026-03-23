package com.mvula.axis.vendor.entity;

import jakarta.persistence.Embeddable;
import lombok.Data;

@Data
@Embeddable
public class Address {

  private String addressLine1;
  private String addressLine2;
  private String city;
  private String stateOrProvince;
  private String postalCode;
  private String country;
}
