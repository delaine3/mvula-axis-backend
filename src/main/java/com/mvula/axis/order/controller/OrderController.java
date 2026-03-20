package com.mvula.axis.order.controller;

import com.mvula.axis.order.dto.OrderRequest;
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
  public Order createOrder(@Valid @RequestBody OrderRequest orderRequest) {
    return orderService.createOrder(orderRequest);
  }

  @GetMapping
  public List<Order> getAllOrders() {
    return orderService.getAllOrders();
  }

  @GetMapping("/{id}")
  public Order getOrderById(@PathVariable Long id) {
    return orderService.getOrderById(id);
  }

  @PutMapping("/{id}")
  public Order updateOrder(@PathVariable Long id, @Valid @RequestBody OrderRequest orderRequest) {
    return orderService.updateOrder(id, orderRequest);
  }

  @DeleteMapping("/{id}")
  public void deleteOrder(@PathVariable Long id) {
    orderService.deleteOrder(id);
  }

  @PatchMapping("/{id}")
  public Order patchOrder(@PathVariable Long id, @RequestBody OrderRequest orderRequest) {
    return orderService.patchOrder(id, orderRequest);
  }
}