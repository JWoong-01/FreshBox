package com.example.server.cart.web.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class ShoppingItemRequest {

    @NotBlank
    private String name;

    @NotNull
    @Min(1)
    private Integer quantity;

    @NotBlank
    private String unit;

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
