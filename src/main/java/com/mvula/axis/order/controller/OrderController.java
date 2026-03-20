package com.mvula.axis.order.controller;

import com.mvula.axis.order.entity.Order;
import com.mvula.axis.order.service.OrderService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/orders")
@RequiredArgsConstructor
public class OrderController {

  private final OrderService orderService;

  @PostMapping
  public Order createOrder(@Valid @RequestBody Order order) {
    return orderService.createOrder(order);
  }

  @GetMapping
  public List<Order> getAllOrders() {
    return orderService.getAllOrders();
  }

  @GetMapping("/{id}")
  public Order getOrderById(@PathVariable Long id) {
    return orderService.getOrderById(id);
  }

  @DeleteMapping("/{id}")
  public void deleteOrder(@PathVariable Long id) {
    orderService.deleteOrder(id);
  }

  @PutMapping("/{id}")
  public Order updateOrder(@PathVariable Long id, @Valid @RequestBody Order order) {
    return orderService.updateOrder(id, order);
  }
}