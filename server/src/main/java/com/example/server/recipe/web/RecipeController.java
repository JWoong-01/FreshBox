package com.example.server.recipe.web;

import com.example.server.recipe.domain.Recipe;
import com.example.server.recipe.service.RecipeService;
import com.example.server.recipe.web.dto.RecipeRequest;
import com.example.server.recipe.web.dto.RecipeResponse;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/recipes")
public class RecipeController {

    private final RecipeService recipeService;

    public RecipeController(RecipeService recipeService) {
        this.recipeService = recipeService;
    }

    @GetMapping
    public ResponseEntity<List<RecipeResponse>> getRecipes(@RequestParam(required = false) String keyword) {
        List<Recipe> recipes = recipeService.search(keyword);
        List<RecipeResponse> responses = recipes.stream()
                .map(RecipeResponse::from)
                .toList();
        return ResponseEntity.ok(responses);
    }

    @PostMapping
    public ResponseEntity<RecipeResponse> createRecipe(@Valid @RequestBody RecipeRequest request) {
        Recipe recipe = recipeService.create(request.getName(), request.getImageUrl(), request.getIngredients(), request.getInstructions());
        return ResponseEntity.ok(RecipeResponse.from(recipe));
    }
}
