package com.mvula.axis.order.service;

import com.mvula.axis.common.exception.ResourceNotFoundException;
import com.mvula.axis.order.dto.OrderRequest;
import com.mvula.axis.order.entity.Order;
import com.mvula.axis.order.entity.OrderItem;
import com.mvula.axis.order.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import com.mvula.axis.order.dto.OrderItemCreateRequest;
import com.mvula.axis.order.dto.OrderItemPatchRequest;
import com.mvula.axis.order.repository.OrderItemRepository;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderService {

  private final OrderRepository orderRepository;
  private final OrderItemRepository orderItemRepository;

  public Order addItemToOrder(Long orderId, OrderItemCreateRequest itemRequest) {
    Order order = orderRepository.findById(orderId)
        .orElseThrow(() -> new ResourceNotFoundException("order not found"));

    OrderItem item = new OrderItem();
    item.setProductName(itemRequest.getProductName());
    item.setQuantity(itemRequest.getQuantity());
    item.setUnitPrice(itemRequest.getUnitPrice());
    item.setOrder(order);

    order.getItems().add(item);
    order.setTotalAmount(calculateTotal(order.getItems()));

    return orderRepository.save(order);
  }
  public Order patchOrderItem(Long orderId, Long itemId, OrderItemPatchRequest itemRequest) {
    Order order = orderRepository.findById(orderId)
        .orElseThrow(() -> new ResourceNotFoundException("order not found"));

    OrderItem item = orderItemRepository.findById(itemId)
        .orElseThrow(() -> new ResourceNotFoundException("order item not found"));

    if (!item.getOrder().getId().equals(orderId)) {
      throw new ResourceNotFoundException("order item not found in this order");
    }

    if (itemRequest.getProductName() != null) {
      item.setProductName(itemRequest.getProductName());
    }

    if (itemRequest.getQuantity() != null) {
      item.setQuantity(itemRequest.getQuantity());
    }

    if (itemRequest.getUnitPrice() != null) {
      item.setUnitPrice(itemRequest.getUnitPrice());
    }

    order.setTotalAmount(calculateTotal(order.getItems()));

    return orderRepository.save(order);
  }
  public Order deleteOrderItem(Long orderId, Long itemId) {
    Order order = orderRepository.findById(orderId)
        .orElseThrow(() -> new ResourceNotFoundException("order not found"));

    OrderItem item = orderItemRepository.findById(itemId)
        .orElseThrow(() -> new ResourceNotFoundException("order item not found"));

    if (!item.getOrder().getId().equals(orderId)) {
      throw new ResourceNotFoundException("order item not found in this order");
    }

    order.getItems().remove(item);
    order.setTotalAmount(calculateTotal(order.getItems()));

    return orderRepository.save(order);
  }
  public List<Order> getAllOrders() {
    return orderRepository.findAll();
  }

  public Order getOrderById(Long id) {
    return orderRepository.findById(id)
        .orElseThrow(() -> new ResourceNotFoundException("order not found"));
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

    existingOrder.setTotalAmount(calculateTotal(existingOrder.getItems()));

    return orderRepository.save(existingOrder);
  }
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

    order.setTotalAmount(calculateTotal(order.getItems()));

    return orderRepository.save(order);
  }
  public void deleteOrder(Long id) {
    if (!orderRepository.existsById(id)) {
      throw new ResourceNotFoundException("order not found");
    }
    orderRepository.deleteById(id);
  }

  private BigDecimal calculateTotal(List<OrderItem> items) {
    if (items == null || items.isEmpty()) {
      return BigDecimal.ZERO;
    }

    return items.stream()
        .map(item -> item.getUnitPrice().multiply(BigDecimal.valueOf(item.getQuantity())))
        .reduce(BigDecimal.ZERO, BigDecimal::add);
  }

  public Order patchOrder(Long id, OrderRequest orderRequest) {
    Order existingOrder = orderRepository.findById(id)
        .orElseThrow(() -> new ResourceNotFoundException("order not found"));

    if (orderRequest.getVendor() != null) {
      existingOrder.setVendor(orderRequest.getVendor());
    }

    if (orderRequest.getDescription() != null) {
      existingOrder.setDescription(orderRequest.getDescription());
    }

    if (orderRequest.getStatus() != null) {
      existingOrder.setStatus(orderRequest.getStatus());
    }

    if (orderRequest.getUpdatedBy() != null) {
      existingOrder.setUpdatedBy(orderRequest.getUpdatedBy());
    }

    return orderRepository.save(existingOrder);
  }
}