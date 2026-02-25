package com.wesdell.shop.order.service;

import com.wesdell.shop.order.DTO.OrderRequest;
import com.wesdell.shop.order.model.Order;
import com.wesdell.shop.order.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;

    public void createOrder(OrderRequest newOrder) {
        Order order = orderRequestToOrder(newOrder);
        order.setOrderNumber(UUID.randomUUID().toString());
        orderRepository.save(order);
    }

    private Order orderRequestToOrder(OrderRequest newOrder) {
        return new Order(
                newOrder.id(),
                newOrder.orderNumber(),
                newOrder.skuCode(),
                newOrder.quantity(),
                newOrder.price()
        );
    }

}
