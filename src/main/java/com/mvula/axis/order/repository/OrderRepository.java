package com.mvula.axis.order.repository;

import com.mvula.axis.order.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface OrderRepository
    extends JpaRepository<Order, Long>, JpaSpecificationExecutor<Order> {}
