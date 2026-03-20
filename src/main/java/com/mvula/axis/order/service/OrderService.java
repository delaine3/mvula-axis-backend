package com.mvula.axis.order.service;

import com.mvula.axis.common.exception.ResourceNotFoundException;
import com.mvula.axis.order.entity.Order;
import com.mvula.axis.order.entity.OrderItem;
import com.mvula.axis.order.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import com.mvula.axis.order.dto.OrderItemRequest;
import com.mvula.axis.order.dto.OrderRequest;

import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderService {

  private final OrderRepository orderRepository;

  public Order createOrder(OrderRequest orderRequest) {
    Order order = new Order();
    order.setVendor(orderRequest.getVendor());
    order.setDescription(orderRequest.getDescription());
    order.setStatus(orderRequest.getStatus());
    order.setCreatedBy(orderRequest.getCreatedBy());
    order.setUpdatedBy(orderRequest.getUpdatedBy());

    if (orderRequest.getItems() != null) {
      orderRequest.getItems().forEach(itemRequest -> {
        OrderItem item = new OrderItem();
        item.setProductName(itemRequest.getProductName());
        item.setQuantity(itemRequest.getQuantity());
        item.setUnitPrice(itemRequest.getUnitPrice());
        item.setOrder(order);
        order.getItems().add(item);
      });
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

  public Order updateOrder(Long id, OrderRequest orderRequest) {
    Order existingOrder = orderRepository.findById(id)
        .orElseThrow(() -> new ResourceNotFoundException("order not found"));

    existingOrder.setVendor(orderRequest.getVendor());
    existingOrder.setDescription(orderRequest.getDescription());
    existingOrder.setStatus(orderRequest.getStatus());
    existingOrder.setUpdatedBy(orderRequest.getUpdatedBy());

    existingOrder.getItems().clear();

    if (orderRequest.getItems() != null) {
      orderRequest.getItems().forEach(itemRequest -> {
        OrderItem item = new OrderItem();
        item.setProductName(itemRequest.getProductName());
        item.setQuantity(itemRequest.getQuantity());
        item.setUnitPrice(itemRequest.getUnitPrice());
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
