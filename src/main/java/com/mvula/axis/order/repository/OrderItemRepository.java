package com.mvula.axis.order.repository;

import com.mvula.axis.order.entity.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {
  Optional<OrderItem> findByIdAndOrderId(Long itemId, Long orderId);
}