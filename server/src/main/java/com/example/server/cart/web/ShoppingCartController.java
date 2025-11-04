package com.example.server.cart.web;

import com.example.server.cart.domain.ShoppingItem;
import com.example.server.cart.service.ShoppingCartService;
import com.example.server.cart.web.dto.MoveToFridgeRequest;
import com.example.server.cart.web.dto.ShoppingItemRequest;
import com.example.server.cart.web.dto.ShoppingItemResponse;
import com.example.server.ingredient.service.IngredientService;
import com.example.server.ingredient.web.dto.IngredientResponse;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/shopping-items")
public class ShoppingCartController {

    private final ShoppingCartService shoppingCartService;
    private final IngredientService ingredientService;

    public ShoppingCartController(ShoppingCartService shoppingCartService, IngredientService ingredientService) {
        this.shoppingCartService = shoppingCartService;
        this.ingredientService = ingredientService;
    }

    @GetMapping
    public ResponseEntity<List<ShoppingItemResponse>> getItems() {
        List<ShoppingItemResponse> responses = shoppingCartService.findAll().stream()
                .map(ShoppingItemResponse::from)
                .toList();
        return ResponseEntity.ok(responses);
    }

    @PostMapping
    public ResponseEntity<ShoppingItemResponse> addItem(@Valid @RequestBody ShoppingItemRequest request) {
        ShoppingItem item = shoppingCartService.addItem(request.getName(), request.getQuantity(), request.getUnit());
        return ResponseEntity.ok(ShoppingItemResponse.from(item));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ShoppingItemResponse> updateItem(@PathVariable Long id,
                                                            @Valid @RequestBody ShoppingItemRequest request) {
        ShoppingItem item = shoppingCartService.updateItem(id, request.getName(), request.getQuantity(), request.getUnit());
        return ResponseEntity.ok(ShoppingItemResponse.from(item));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteItem(@PathVariable Long id) {
        shoppingCartService.deleteItem(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/move-to-fridge")
    public ResponseEntity<List<IngredientResponse>> moveToFridge(@Valid @RequestBody MoveToFridgeRequest request) {
        List<ShoppingItem> items = shoppingCartService.findAllById(request.getItemIds());
        if (items.isEmpty()) {
            throw new IllegalArgumentException("선택된 장바구니 항목이 없습니다.");
        }

        LocalDate intakeDate = request.getIntakeDate() != null ? request.getIntakeDate() : LocalDate.now();
        LocalDate effectiveExpirationDate = request.getExpirationDate() != null
                ? request.getExpirationDate()
                : intakeDate.plusDays(7);
        String effectiveUnitOverride = request.getUnitOverride();

        List<IngredientResponse> responses = items.stream()
                .map(item -> ingredientService.create(
                        item.getName(),
                        item.getQuantity(),
                        effectiveUnitOverride != null ? effectiveUnitOverride : item.getUnit(),
                        intakeDate,
                        effectiveExpirationDate,
                        request.getStorageLocation(),
                        request.getImage()
                ))
                .map(IngredientResponse::from)
                .collect(Collectors.toList());

        request.getItemIds().forEach(shoppingCartService::deleteItem);
        return ResponseEntity.ok(responses);
    }
}
