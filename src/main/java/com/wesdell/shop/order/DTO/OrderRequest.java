package com.wesdell.shop.order.DTO;

import java.math.BigDecimal;

public record OrderRequest(
        Long id,
        String orderNumber,
        String skuCode,
        Integer quantity,
        BigDecimal price,
        UserDetails userDetails
) {
}
