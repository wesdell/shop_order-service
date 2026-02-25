CREATE TABLE `orders`
(
       `id`           bigint(20) NOT NULL AUTO_INCREMENT,
       `order_number` varchar(255) DEFAULT NULL,
       `sku_code`     varchar(255),
       `quantity`     int(11),
       `price`        decimal(19, 2),
       PRIMARY KEY (`id`)
);