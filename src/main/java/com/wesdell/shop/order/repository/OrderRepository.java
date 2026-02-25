package com.wesdell.shop.order.repository;

import com.wesdell.shop.order.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, Long> {
}
