package com.mvula.axis.order.specification;

import com.mvula.axis.order.entity.Order;
import org.springframework.data.jpa.domain.Specification;

public class OrderSpecification {

  public static Specification<Order> containsText(String search) {
    return (root, query, criteriaBuilder) -> {
      if (search == null || search.isBlank()) {
        return criteriaBuilder.conjunction();
      }

      String likeSearch = "%" + search.toLowerCase() + "%";

      return criteriaBuilder.or(
          criteriaBuilder.like(criteriaBuilder.lower(root.get("vendor")), likeSearch),
          criteriaBuilder.like(criteriaBuilder.lower(root.get("description")), likeSearch),
          criteriaBuilder.like(criteriaBuilder.lower(root.get("status")), likeSearch));
    };
  }
}
