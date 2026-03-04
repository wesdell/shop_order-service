package com.wesdell.shop.order.service;

import com.wesdell.shop.order.DTO.OrderRequest;
import com.wesdell.shop.order.client.InventoryClient;
import com.wesdell.shop.order.model.Order;
import com.wesdell.shop.order.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final InventoryClient inventoryClient;

    public void createOrder(OrderRequest newOrder) {
        boolean isInStock = inventoryClient.isItemInStock(newOrder.skuCode(), newOrder.quantity());
        if (!isInStock) {
            throw new RuntimeException("Product: "  + newOrder.skuCode() + " not exists or there are not enough items in stock");
        }

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
