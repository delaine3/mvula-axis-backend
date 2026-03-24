package com.mvula.axis.order.service;

import com.mvula.axis.common.dto.PagedResponse;
import com.mvula.axis.common.exception.ResourceNotFoundException;
import com.mvula.axis.order.dto.OrderItemCreateRequest;
import com.mvula.axis.order.dto.OrderItemPatchRequest;
import com.mvula.axis.order.dto.OrderItemResponse;
import com.mvula.axis.order.dto.OrderRequest;
import com.mvula.axis.order.dto.OrderResponse;
import com.mvula.axis.order.entity.Order;
import com.mvula.axis.order.entity.OrderItem;
import com.mvula.axis.order.repository.OrderItemRepository;
import com.mvula.axis.order.repository.OrderRepository;
import com.mvula.axis.order.specification.OrderSpecification;
import com.mvula.axis.vendor.dto.VendorRequest;
import com.mvula.axis.vendor.entity.Address;
import com.mvula.axis.vendor.entity.Vendor;
import com.mvula.axis.vendor.repository.VendorRepository;
import java.math.BigDecimal;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OrderService {
  private final VendorRepository vendorRepository;
  private final OrderRepository orderRepository;
  private final OrderItemRepository orderItemRepository;

  private Vendor resolveVendor(OrderRequest orderRequest) {
    if (orderRequest.getVendorId() != null) {
      return vendorRepository
          .findById(orderRequest.getVendorId())
          .orElseThrow(() -> new ResourceNotFoundException("vendor not found"));
    }

    if (orderRequest.getNewVendor() != null) {
      VendorRequest request = orderRequest.getNewVendor();

      Vendor vendor = new Vendor();
      vendor.setName(request.getName());
      vendor.setCategory(request.getCategory());
      vendor.setIsActive(request.getIsActive());
      vendor.setWebsite(request.getWebsite());
      vendor.setTaxNumber(request.getTaxNumber());
      vendor.setOffersDelivery(request.getOffersDelivery());
      vendor.setContactPerson(request.getContactPerson());
      vendor.setContactNumber(request.getContactNumber());
      vendor.setEmail(request.getEmail());
      vendor.setNotes(request.getNotes());
      vendor.setPaymentTerms(request.getPaymentTerms());
      vendor.setPreferredCurrency(request.getPreferredCurrency());

      if (request.getAddress() != null) {
        Address address = new Address();
        address.setAddressLine1(request.getAddress().getAddressLine1());
        address.setAddressLine2(request.getAddress().getAddressLine2());
        address.setCity(request.getAddress().getCity());
        address.setStateOrProvince(request.getAddress().getStateOrProvince());
        address.setPostalCode(request.getAddress().getPostalCode());
        address.setCountry(request.getAddress().getCountry());
        vendor.setAddress(address);
      }

      return vendorRepository.save(vendor);
    }

    throw new IllegalArgumentException("either vendorId or newVendor must be provided");
  }

  public OrderResponse addItemToOrder(Long orderId, OrderItemCreateRequest itemRequest) {
    Order order =
        orderRepository
            .findById(orderId)
            .orElseThrow(() -> new ResourceNotFoundException("order not found"));

    OrderItem item = new OrderItem();
    item.setProductName(itemRequest.getProductName());
    item.setQuantity(itemRequest.getQuantity());
    item.setUnitPrice(itemRequest.getUnitPrice());
    item.setOrder(order);

    order.getItems().add(item);
    order.setTotalAmount(calculateTotal(order.getItems()));
    return mapToResponse(orderRepository.save(order));
  }

  public OrderResponse patchOrderItem(
      Long orderId, Long itemId, OrderItemPatchRequest itemRequest) {
    Order order =
        orderRepository
            .findById(orderId)
            .orElseThrow(() -> new ResourceNotFoundException("order not found"));

    OrderItem item =
        orderItemRepository
            .findById(itemId)
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
    return mapToResponse(orderRepository.save(order));
  }

  private OrderResponse mapToResponse(Order order) {
    OrderResponse response = new OrderResponse();

    response.setId(order.getId());
    response.setVendorId(order.getVendor().getId());
    response.setVendorName(order.getVendor().getName());
    response.setDescription(order.getDescription());
    response.setStatus(order.getStatus());
    response.setTotalAmount(order.getTotalAmount());
    response.setCreatedBy(order.getCreatedBy());
    response.setUpdatedBy(order.getUpdatedBy());
    response.setCreatedAt(order.getCreatedAt());
    response.setUpdatedAt(order.getUpdatedAt());
    response.setIsPaid(order.getIsPaid());

    List<OrderItemResponse> itemResponses =
        order.getItems().stream()
            .map(
                item -> {
                  OrderItemResponse itemResponse = new OrderItemResponse();
                  itemResponse.setId(item.getId());
                  itemResponse.setProductName(item.getProductName());
                  itemResponse.setQuantity(item.getQuantity());
                  itemResponse.setUnitPrice(item.getUnitPrice());
                  itemResponse.setLineTotal(
                      item.getUnitPrice()
                          .multiply(java.math.BigDecimal.valueOf(item.getQuantity())));
                  return itemResponse;
                })
            .toList();

    response.setItems(itemResponses);

    return response;
  }

  public OrderResponse deleteOrderItem(Long orderId, Long itemId) {
    Order order =
        orderRepository
            .findById(orderId)
            .orElseThrow(() -> new ResourceNotFoundException("order not found"));

    OrderItem item =
        orderItemRepository
            .findById(itemId)
            .orElseThrow(() -> new ResourceNotFoundException("order item not found"));

    if (!item.getOrder().getId().equals(orderId)) {
      throw new ResourceNotFoundException("order item not found in this order");
    }

    order.getItems().remove(item);
    order.setTotalAmount(calculateTotal(order.getItems()));
    return mapToResponse(orderRepository.save(order));
  }

  public PagedResponse<OrderResponse> getAllOrders(
      int page, int size, String sortBy, String sortDir, String search) {
    Sort sort =
        sortDir.equalsIgnoreCase("asc")
            ? Sort.by(sortBy).ascending()
            : Sort.by(sortBy).descending();

    Pageable pageable = PageRequest.of(page, size, sort);

    Page<Order> orderPage =
        orderRepository.findAll(OrderSpecification.containsText(search), pageable);

    PagedResponse<OrderResponse> response = new PagedResponse<>();
    response.setItems(orderPage.getContent().stream().map(this::mapToResponse).toList());
    response.setPage(orderPage.getNumber());
    response.setSize(orderPage.getSize());
    response.setTotalItems(orderPage.getTotalElements());
    response.setTotalPages(orderPage.getTotalPages());

    return response;
  }

  public OrderResponse getOrderById(Long id) {
    Order order =
        orderRepository
            .findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("order not found"));

    return mapToResponse(order);
  }

  public OrderResponse updateOrder(Long id, OrderRequest orderRequest) {
    Order existingOrder =
        orderRepository
            .findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("order not found"));
    existingOrder.setVendor(resolveVendor(orderRequest));
    existingOrder.setDescription(orderRequest.getDescription());
    existingOrder.setStatus(orderRequest.getStatus());
    existingOrder.setUpdatedBy(orderRequest.getUpdatedBy());
    existingOrder.setIsPaid(orderRequest.getIsPaid());

    existingOrder.getItems().clear();

    if (orderRequest.getItems() != null) {
      orderRequest
          .getItems()
          .forEach(
              itemRequest -> {
                OrderItem item = new OrderItem();
                item.setProductName(itemRequest.getProductName());
                item.setQuantity(itemRequest.getQuantity());
                item.setUnitPrice(itemRequest.getUnitPrice());
                item.setOrder(existingOrder);
                existingOrder.getItems().add(item);
              });
    }

    existingOrder.setTotalAmount(calculateTotal(existingOrder.getItems()));
    return mapToResponse(orderRepository.save(existingOrder));
  }

  public OrderResponse createOrder(OrderRequest orderRequest) {
    Order order = new Order();
    order.setVendor(resolveVendor(orderRequest));
    order.setDescription(orderRequest.getDescription());
    order.setStatus(orderRequest.getStatus());
    order.setCreatedBy(orderRequest.getCreatedBy());
    order.setUpdatedBy(orderRequest.getUpdatedBy());
    order.setIsPaid(orderRequest.getIsPaid() != null ? orderRequest.getIsPaid() : false);

    if (orderRequest.getItems() != null) {
      orderRequest
          .getItems()
          .forEach(
              itemRequest -> {
                OrderItem item = new OrderItem();
                item.setProductName(itemRequest.getProductName());
                item.setQuantity(itemRequest.getQuantity());
                item.setUnitPrice(itemRequest.getUnitPrice());
                item.setOrder(order);
                order.getItems().add(item);
              });
    }

    order.setTotalAmount(calculateTotal(order.getItems()));

    Order savedOrder = orderRepository.save(order);
    return mapToResponse(savedOrder);
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

  public OrderResponse patchOrder(Long id, OrderRequest orderRequest) {
    Order existingOrder =
        orderRepository
            .findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("order not found"));

    if (orderRequest.getVendorId() != null || orderRequest.getNewVendor() != null) {
      existingOrder.setVendor(resolveVendor(orderRequest));
    }
    if (orderRequest.getDescription() != null) {
      existingOrder.setDescription(orderRequest.getDescription());
    }
    if (orderRequest.getIsPaid() != null) {
      existingOrder.setIsPaid(orderRequest.getIsPaid());
    }
    if (orderRequest.getStatus() != null) {
      existingOrder.setStatus(orderRequest.getStatus());
    }

    if (orderRequest.getUpdatedBy() != null) {
      existingOrder.setUpdatedBy(orderRequest.getUpdatedBy());
    }
    return mapToResponse(orderRepository.save(existingOrder));
  }
}
