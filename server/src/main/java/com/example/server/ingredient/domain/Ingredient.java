package com.example.server.ingredient.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.time.LocalDate;

@Entity
@Table(name = "ingredients")
public class Ingredient {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private Integer quantity;

    @Column(nullable = false)
    private String unit;

    @Column(name = "intake_date")
    private LocalDate intakeDate;

    @Column(name = "expiration_date")
    private LocalDate expirationDate;

    @Column(name = "storage_location")
    private String storageLocation;

    @Column(name = "image_id")
    private Integer image;

    protected Ingredient() {
    }

    private Ingredient(String name, Integer quantity, String unit, LocalDate intakeDate,
                       LocalDate expirationDate, String storageLocation, Integer image) {
        this.name = name;
        this.quantity = quantity;
        this.unit = unit;
        this.intakeDate = intakeDate;
        this.expirationDate = expirationDate;
        this.storageLocation = storageLocation;
        this.image = image;
    }

    public static Ingredient create(String name, Integer quantity, String unit, LocalDate intakeDate,
                                    LocalDate expirationDate, String storageLocation, Integer image) {
        return new Ingredient(name, quantity, unit, intakeDate, expirationDate, storageLocation, image);
    }

    public void update(Integer quantity, String unit, LocalDate intakeDate,
                       LocalDate expirationDate, String storageLocation, Integer image) {
        this.quantity = quantity;
        this.unit = unit;
        this.intakeDate = intakeDate;
        this.expirationDate = expirationDate;
        this.storageLocation = storageLocation;
        this.image = image;
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
