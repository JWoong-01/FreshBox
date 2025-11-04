package com.example.server.cart.web.dto;

import com.example.server.cart.domain.ShoppingItem;

public class ShoppingItemResponse {

    private final Long id;
    private final String name;
    private final Integer quantity;
    private final String unit;

    private ShoppingItemResponse(Long id, String name, Integer quantity, String unit) {
        this.id = id;
        this.name = name;
        this.quantity = quantity;
        this.unit = unit;
    }

    public static ShoppingItemResponse from(ShoppingItem item) {
        return new ShoppingItemResponse(item.getId(), item.getName(), item.getQuantity(), item.getUnit());
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public String getUnit() {
        return unit;
    }
}
