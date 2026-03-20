package com.mvula.axis.order.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Data;

@Entity
@Table(name = "order_items")
@Data
public class OrderItem {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "order_item_id")
  private Long id;

  @NotBlank
  @Column(nullable = false)
  private String productName;

  @PositiveOrZero
  @Column(nullable = false)
  private Integer quantity;

  @PositiveOrZero
  @Column(nullable = false)
  private Double unitPrice;

  @ManyToOne
  @JoinColumn(name = "order_id", nullable = false)
  @JsonBackReference
  private Order order;
}