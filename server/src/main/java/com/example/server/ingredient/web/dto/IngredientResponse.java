package com.example.server.ingredient.web.dto;

import com.example.server.ingredient.domain.Ingredient;

import java.time.LocalDate;

public class IngredientResponse {

    private final Long id;
    private final String name;
    private final Integer quantity;
    private final String unit;
    private final LocalDate intakeDate;
    private final LocalDate expirationDate;
    private final String storageLocation;
    private final Integer image;

    private IngredientResponse(Long id, String name, Integer quantity, String unit, LocalDate intakeDate,
                               LocalDate expirationDate, String storageLocation, Integer image) {
        this.id = id;
        this.name = name;
        this.quantity = quantity;
        this.unit = unit;
        this.intakeDate = intakeDate;
        this.expirationDate = expirationDate;
        this.storageLocation = storageLocation;
        this.image = image;
    }

    public static IngredientResponse from(Ingredient ingredient) {
        return new IngredientResponse(
                ingredient.getId(),
                ingredient.getName(),
                ingredient.getQuantity(),
                ingredient.getUnit(),
                ingredient.getIntakeDate(),
                ingredient.getExpirationDate(),
                ingredient.getStorageLocation(),
                ingredient.getImage()
        );
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
