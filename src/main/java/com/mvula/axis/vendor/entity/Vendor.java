package com.mvula.axis.vendor.entity;

import jakarta.persistence.*;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Table(name = "vendors")
@Data
public class Vendor {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "vendor_id")
  private Long id;

  @NotBlank
  @Column(nullable = false)
  private String name;

  private String category;

  @Column(nullable = false)
  private Boolean isActive = true;

  private String website;

  private String taxNumber;

  @Embedded @Valid private Address address;

  @Column(nullable = false)
  private Boolean offersDelivery = false;

  private String contactPerson;

  private String contactNumber;

  @Email private String email;

  @Column(length = 2000)
  private String notes;

  private String paymentTerms;

  private String preferredCurrency;

  private LocalDateTime createdAt;

  private LocalDateTime updatedAt;

  @PrePersist
  public void prePersist() {
    this.createdAt = LocalDateTime.now();
    this.updatedAt = LocalDateTime.now();
  }

  @PreUpdate
  public void preUpdate() {
    this.updatedAt = LocalDateTime.now();
  }
}
