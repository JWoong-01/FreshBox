package com.example.server.recipe.service;

import com.example.server.recipe.domain.Recipe;

import java.util.List;

public interface RecipeService {
    List<Recipe> findAll();
    List<Recipe> search(String keyword);
    Recipe create(String name, String imageUrl, String ingredients, String instructions);
}
