package com.mvula.axis.order.controller;

import com.mvula.axis.order.dto.OrderRequest;
import com.mvula.axis.order.dto.OrderResponse;
import com.mvula.axis.order.service.OrderService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import com.mvula.axis.order.dto.OrderItemCreateRequest;
import com.mvula.axis.order.dto.OrderItemPatchRequest;
import java.util.List;
@RestController
@RequestMapping("/orders")
@RequiredArgsConstructor
public class OrderController {

  private final OrderService orderService;

  @PostMapping("/{orderId}/items")
  public OrderResponse addItemToOrder(@PathVariable Long orderId,
      @Valid @RequestBody OrderItemCreateRequest itemRequest) {
    return orderService.addItemToOrder(orderId, itemRequest);
  }

  @PatchMapping("/{orderId}/items/{itemId}")
  public OrderResponse patchOrderItem(@PathVariable Long orderId,
      @PathVariable Long itemId,
      @RequestBody OrderItemPatchRequest itemRequest) {
    return orderService.patchOrderItem(orderId, itemId, itemRequest);
  }

  @DeleteMapping("/{orderId}/items/{itemId}")
  public OrderResponse deleteOrderItem(@PathVariable Long orderId,
      @PathVariable Long itemId) {
    return orderService.deleteOrderItem(orderId, itemId);
  }

  @PostMapping
  public OrderResponse createOrder(@Valid @RequestBody OrderRequest orderRequest) {
    return orderService.createOrder(orderRequest);
  }

  @GetMapping
  public List<OrderResponse> getAllOrders() {
    return orderService.getAllOrders();
  }

  @GetMapping("/{id}")
  public OrderResponse getOrderById(@PathVariable Long id) {
    return orderService.getOrderById(id);
  }

  @PutMapping("/{id}")
  public OrderResponse updateOrder(@PathVariable Long id,
      @Valid @RequestBody OrderRequest orderRequest) {
    return orderService.updateOrder(id, orderRequest);
  }

  @DeleteMapping("/{id}")
  public void deleteOrder(@PathVariable Long id) {
    orderService.deleteOrder(id);
  }

  @PatchMapping("/{id}")
  public OrderResponse patchOrder(@PathVariable Long id,
      @RequestBody OrderRequest orderRequest) {
    return orderService.patchOrder(id, orderRequest);
  }
}