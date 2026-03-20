package com.wesdell.shop.order.service;

import com.wesdell.shop.order.DTO.OrderRequest;
import com.wesdell.shop.order.client.InventoryClient;
import com.wesdell.shop.order.event.OrderEvent;
import com.wesdell.shop.order.model.Order;
import com.wesdell.shop.order.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final InventoryClient inventoryClient;
    private final KafkaTemplate<String, OrderEvent> kafkaTemplate;

    public void createOrder(OrderRequest newOrder) {
        boolean isInStock = inventoryClient.isItemInStock(newOrder.skuCode(), newOrder.quantity());
        if (!isInStock) {
            throw new RuntimeException("Product: "  + newOrder.skuCode() + " not exists or there are not enough items in stock");
        }

        Order order = orderRequestToOrder(newOrder);
        order.setOrderNumber(UUID.randomUUID().toString());
        orderRepository.save(order);

        OrderEvent orderEvent = new OrderEvent(order.getOrderNumber(), newOrder.userDetails().email());
        kafkaTemplate.send("order-created", orderEvent);
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
