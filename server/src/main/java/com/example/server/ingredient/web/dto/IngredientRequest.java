package com.example.server.ingredient.web.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public class IngredientRequest {

    @NotBlank
    private String name;

    @NotNull
    @Min(1)
    private Integer quantity;

    @NotBlank
    private String unit;

    private LocalDate intakeDate;

    @NotNull
    private LocalDate expirationDate;

    @NotBlank
    private String storageLocation;

    private Integer image;

    public String getName() {
        return name;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public String getUnit() {
        return unit;
    }

    public LocalDate getIntakeDate() {
        return intakeDate;
    }

    public LocalDate getExpirationDate() {
        return expirationDate;
    }

    public String getStorageLocation() {
        return storageLocation;
    }

    public Integer getImage() {
        return image;
    }
}
