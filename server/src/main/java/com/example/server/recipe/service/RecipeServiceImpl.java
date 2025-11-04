package com.example.server.recipe.service;

import com.example.server.recipe.domain.Recipe;
import com.example.server.recipe.repository.RecipeRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class RecipeServiceImpl implements RecipeService {

    private final RecipeRepository recipeRepository;

    public RecipeServiceImpl(RecipeRepository recipeRepository) {
        this.recipeRepository = recipeRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public List<Recipe> findAll() {
        return recipeRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public List<Recipe> search(String keyword) {
        if (keyword == null || keyword.isBlank()) {
            return findAll();
        }
        return recipeRepository.findByNameContainingIgnoreCase(keyword);
    }

    @Override
    public Recipe create(String name, String imageUrl, String ingredients, String instructions) {
        Recipe recipe = Recipe.create(name, imageUrl, ingredients, instructions);
        return recipeRepository.save(recipe);
    }
}
