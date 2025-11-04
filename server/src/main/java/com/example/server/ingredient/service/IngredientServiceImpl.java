package com.example.server.ingredient.service;

import com.example.server.ingredient.domain.Ingredient;
import com.example.server.ingredient.repository.IngredientRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
@Transactional
public class IngredientServiceImpl implements IngredientService {

    private final IngredientRepository ingredientRepository;

    public IngredientServiceImpl(IngredientRepository ingredientRepository) {
        this.ingredientRepository = ingredientRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public List<Ingredient> findAll() {
        return ingredientRepository.findAll();
    }

    @Override
    public Ingredient create(String name, Integer quantity, String unit, LocalDate intakeDate,
                              LocalDate expirationDate, String storageLocation, Integer image) {
        Ingredient ingredient = Ingredient.create(name, quantity, unit, intakeDate, expirationDate, storageLocation, image);
        return ingredientRepository.save(ingredient);
    }

    @Override
    public Ingredient update(Long id, Integer quantity, String unit, LocalDate intakeDate,
                              LocalDate expirationDate, String storageLocation, Integer image) {
        Ingredient ingredient = ingredientRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("재료를 찾을 수 없습니다."));
        ingredient.update(quantity, unit, intakeDate, expirationDate, storageLocation, image);
        return ingredient;
    }

    @Override
    public void deleteById(Long id) {
        ingredientRepository.deleteById(id);
    }

    @Override
    public void deleteByName(String name) {
        ingredientRepository.deleteByName(name);
    }
}
