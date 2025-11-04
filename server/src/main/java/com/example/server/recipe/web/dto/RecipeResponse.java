package com.example.server.recipe.web.dto;

import com.example.server.recipe.domain.Recipe;

public class RecipeResponse {

    private final Long id;
    private final String name;
    private final String imageUrl;
    private final String ingredients;
    private final String instructions;

    private RecipeResponse(Long id, String name, String imageUrl, String ingredients, String instructions) {
        this.id = id;
        this.name = name;
        this.imageUrl = imageUrl;
        this.ingredients = ingredients;
        this.instructions = instructions;
    }

    public static RecipeResponse from(Recipe recipe) {
        return new RecipeResponse(
                recipe.getId(),
                recipe.getName(),
                recipe.getImageUrl(),
                recipe.getIngredients(),
                recipe.getInstructions()
        );
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public String getIngredients() {
        return ingredients;
    }

    public String getInstructions() {
        return instructions;
    }
}
