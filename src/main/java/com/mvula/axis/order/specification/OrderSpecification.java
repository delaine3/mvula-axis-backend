package com.mvula.axis.order.specification;

import com.mvula.axis.order.entity.Order;
import com.mvula.axis.vendor.entity.Vendor;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import org.springframework.data.jpa.domain.Specification;

public class OrderSpecification {

  private OrderSpecification() {}

  public static Specification<Order> containsText(String search) {
    return (root, query, criteriaBuilder) -> {
      if (search == null || search.isBlank()) {
        return criteriaBuilder.conjunction();
      }

      String likeSearch = "%" + search.toLowerCase() + "%";

      Join<Order, Vendor> vendorJoin = root.join("vendor", JoinType.LEFT);

      return criteriaBuilder.or(
          criteriaBuilder.like(criteriaBuilder.lower(vendorJoin.get("name")), likeSearch),
          criteriaBuilder.like(criteriaBuilder.lower(root.get("description")), likeSearch),
          criteriaBuilder.like(criteriaBuilder.lower(root.get("status")), likeSearch));
    };
  }
}
