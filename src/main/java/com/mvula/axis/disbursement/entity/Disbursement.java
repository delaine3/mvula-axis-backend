package com.mvula.axis.disbursement.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import lombok.Data;

@Entity
@Table(name = "disbursements")
@Data
public class Disbursement {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "disbursement_id")
  private Long id;

  @Column(name = "payee_name", nullable = false, length = 150)
  private String payeeName;

  @Enumerated(EnumType.STRING)
  @Column(name = "payee_type", nullable = false, length = 50)
  private PayeeType payeeType;

  @Column(name = "service_description", nullable = false, length = 255)
  private String serviceDescription;

  @Column(name = "total_charged", nullable = false, precision = 12, scale = 2)
  private BigDecimal totalCharged;

  @Column(name = "currency", nullable = false, length = 10)
  private String currency = "SZL";

  @Column(name = "due_date")
  private LocalDate dueDate;

  @Enumerated(EnumType.STRING)
  @Column(name = "status", nullable = false, length = 30)
  private DisbursementStatus status = DisbursementStatus.UNPAID;

  @Column(name = "notes", columnDefinition = "TEXT")
  private String notes;

  @Column(name = "created_by")
  private String createdBy;

  @Column(name = "updated_by")
  private String updatedBy;

  @Column(name = "created_at", nullable = false)
  private LocalDateTime createdAt;

  @Column(name = "updated_at", nullable = false)
  private LocalDateTime updatedAt;

  @OneToMany(mappedBy = "disbursement", cascade = CascadeType.ALL, orphanRemoval = true)
  private List<DisbursementPayment> payments = new ArrayList<>();

  @PrePersist
  public void prePersist() {
    createdAt = LocalDateTime.now();
    updatedAt = LocalDateTime.now();
  }

  @PreUpdate
  public void preUpdate() {
    updatedAt = LocalDateTime.now();
  }
}
