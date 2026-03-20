package com.mvula.axis.order.service;

import com.mvula.axis.common.exception.ResourceNotFoundException;
import com.mvula.axis.order.entity.Order;
import com.mvula.axis.order.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderService {

  private final OrderRepository orderRepository;

  public Order createOrder(Order order) {
    if (order.getItems() != null) {
      order.getItems().forEach(item -> item.setOrder(order));
    }

    double total = order.getItems().stream()
        .mapToDouble(item -> item.getQuantity() * item.getUnitPrice())
        .sum();

    order.setTotalAmount(total);

    return orderRepository.save(order);
  }

  public List<Order> getAllOrders() {
    return orderRepository.findAll();
  }

  public Order getOrderById(Long id) {
    return orderRepository.findById(id)
        .orElseThrow(() -> new RuntimeException("order not found"));
  }

  public void deleteOrder(Long id) {
    if (!orderRepository.existsById(id)) {
      throw new RuntimeException("order not found");
    }
    orderRepository.deleteById(id);
  }

  public Order updateOrder(Long id, Order updatedOrder) {
    Order existingOrder = orderRepository.findById(id)
        .orElseThrow(() -> new ResourceNotFoundException("order not found"));

    existingOrder.setVendor(updatedOrder.getVendor());
    existingOrder.setDescription(updatedOrder.getDescription());
    existingOrder.setStatus(updatedOrder.getStatus());
    existingOrder.setUpdatedBy(updatedOrder.getUpdatedBy());

    existingOrder.getItems().clear();

    if (updatedOrder.getItems() != null) {
      updatedOrder.getItems().forEach(item -> {
        item.setOrder(existingOrder);
        existingOrder.getItems().add(item);
      });
    }

    double total = existingOrder.getItems().stream()
        .mapToDouble(item -> item.getQuantity() * item.getUnitPrice())
        .sum();

    existingOrder.setTotalAmount(total);

    return orderRepository.save(existingOrder);
  }
}
