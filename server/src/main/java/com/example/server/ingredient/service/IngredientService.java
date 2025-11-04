package com.example.server.ingredient.service;

import com.example.server.ingredient.domain.Ingredient;

import java.time.LocalDate;
import java.util.List;

public interface IngredientService {
    List<Ingredient> findAll();
    Ingredient create(String name, Integer quantity, String unit, LocalDate intakeDate,
                      LocalDate expirationDate, String storageLocation, Integer image);
    Ingredient update(Long id, Integer quantity, String unit, LocalDate intakeDate,
                      LocalDate expirationDate, String storageLocation, Integer image);
    void deleteById(Long id);
    void deleteByName(String name);
}
