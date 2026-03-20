package com.mvula.axis.order.controller;

import com.mvula.axis.order.entity.Order;
import com.mvula.axis.order.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/orders")
@RequiredArgsConstructor
public class OrderController {

  private final OrderService orderService;

  @PostMapping
  public Order createOrder(@RequestBody Order order) {
    return orderService.createOrder(order);
  }

  @GetMapping
  public List<Order> getAllOrders() {
    return orderService.getAllOrders();
  }
}