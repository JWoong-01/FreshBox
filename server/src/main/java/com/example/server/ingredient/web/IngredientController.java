package com.example.server.ingredient.web;

import com.example.server.ingredient.domain.Ingredient;
import com.example.server.ingredient.service.IngredientService;
import com.example.server.ingredient.web.dto.IngredientRequest;
import com.example.server.ingredient.web.dto.IngredientResponse;
import jakarta.validation.Valid;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/ingredients")
public class IngredientController {

    private final IngredientService ingredientService;

    public IngredientController(IngredientService ingredientService) {
        this.ingredientService = ingredientService;
    }

    @GetMapping
    public ResponseEntity<List<IngredientResponse>> getIngredients() {
        List<IngredientResponse> responses = ingredientService.findAll().stream()
                .map(IngredientResponse::from)
                .toList();
        return ResponseEntity.ok(responses);
    }

    @PostMapping
    public ResponseEntity<IngredientResponse> addIngredient(@Valid @RequestBody IngredientRequest request) {
        Ingredient ingredient = ingredientService.create(
                request.getName(),
                request.getQuantity(),
                request.getUnit(),
                request.getIntakeDate(),
                request.getExpirationDate(),
                request.getStorageLocation(),
                request.getImage()
        );
        return ResponseEntity.ok(IngredientResponse.from(ingredient));
    }

    @PutMapping("/{id}")
    public ResponseEntity<IngredientResponse> updateIngredient(@PathVariable Long id,
                                                               @Valid @RequestBody IngredientRequest request) {
        Ingredient ingredient = ingredientService.update(
                id,
                request.getQuantity(),
                request.getUnit(),
                request.getIntakeDate(),
                request.getExpirationDate(),
                request.getStorageLocation(),
                request.getImage()
        );
        return ResponseEntity.ok(IngredientResponse.from(ingredient));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteIngredient(@PathVariable Long id) {
        ingredientService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping
    public ResponseEntity<Void> deleteByName(@RequestParam String name) {
        ingredientService.deleteByName(name);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/bulk")
    public ResponseEntity<List<IngredientResponse>> addBulk(@RequestParam List<String> names,
                                                             @RequestParam Integer quantity,
                                                             @RequestParam String unit,
                                                             @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate expirationDate,
                                                             @RequestParam(defaultValue = "냉장") String storageLocation,
                                                             @RequestParam(required = false) Integer image) {
        List<IngredientResponse> responses = names.stream()
                .map(name -> ingredientService.create(name, quantity, unit, LocalDate.now(), expirationDate, storageLocation, image))
                .map(IngredientResponse::from)
                .toList();
        return ResponseEntity.ok(responses);
    }
}
