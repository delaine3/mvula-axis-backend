package com.mvula.axis.disbursement.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import lombok.Data;

@Entity
@Table(name = "disbursement_payments")
@Data
public class DisbursementPayment {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "payment_id")
  private Long id;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "disbursement_id", nullable = false)
  private Disbursement disbursement;

  @Column(name = "date_paid", nullable = false)
  private LocalDate datePaid;

  @Column(name = "amount_paid", nullable = false, precision = 12, scale = 2)
  private BigDecimal amountPaid;

  @Column(name = "payment_method", length = 50)
  private String paymentMethod;

  @Column(name = "reference_number", length = 100)
  private String referenceNumber;

  @Column(name = "notes", columnDefinition = "TEXT")
  private String notes;

  @Column(name = "created_at", nullable = false)
  private LocalDateTime createdAt;

  @PrePersist
  public void prePersist() {
    createdAt = LocalDateTime.now();
  }
}
