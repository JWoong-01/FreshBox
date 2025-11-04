package com.example.server.cart.web.dto;

import jakarta.validation.constraints.NotEmpty;

import java.time.LocalDate;
import java.util.List;

public class MoveToFridgeRequest {

    @NotEmpty
    private List<Long> itemIds;

    private String storageLocation = "냉장";

    private LocalDate intakeDate = LocalDate.now();

    private LocalDate expirationDate;

    private Integer image;

    private String unitOverride;

    public List<Long> getItemIds() {
        return itemIds;
    }

    public String getStorageLocation() {
        return storageLocation;
    }

    public LocalDate getIntakeDate() {
        return intakeDate;
    }

    public LocalDate getExpirationDate() {
        return expirationDate;
    }

    public Integer getImage() {
        return image;
    }

    public String getUnitOverride() {
        return unitOverride;
    }
}
