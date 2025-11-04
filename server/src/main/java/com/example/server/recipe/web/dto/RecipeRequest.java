package com.example.server.recipe.web.dto;

import jakarta.validation.constraints.NotBlank;

public class RecipeRequest {

    @NotBlank
    private String name;

    private String imageUrl;

    @NotBlank
    private String ingredients;

    @NotBlank
    private String instructions;

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
