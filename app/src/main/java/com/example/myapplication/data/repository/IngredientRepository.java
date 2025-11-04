package com.example.myapplication.data.repository;

import com.example.myapplication.data.model.Ingredient;
import com.example.myapplication.data.model.IngredientData;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

/**
 * 정적 재료 데이터를 캡슐화해 검색 기능 등에서 재사용하기 위한 저장소 클래스.
 * 재료명은 {@link IngredientData} 에 정의된 배열을 기반으로 한글 표기를 우선 사용한다.
 */
public final class IngredientRepository {

    private IngredientRepository() {
        // instantiation not allowed
    }

    public static List<Ingredient> getAllIngredients() {
        Set<String> ingredientNames = new LinkedHashSet<>();

        collectKoreanNames(ingredientNames, IngredientData.FRUITS);
        collectKoreanNames(ingredientNames, IngredientData.VEGETABLES);
        collectKoreanNames(ingredientNames, IngredientData.DAIRY);
        collectKoreanNames(ingredientNames, IngredientData.MEAT);
        collectKoreanNames(ingredientNames, IngredientData.SEAFOOD);
        collectKoreanNames(ingredientNames, IngredientData.CONDIMENTS);
        collectKoreanNames(ingredientNames, IngredientData.ETC);

        List<Ingredient> ingredients = new ArrayList<>();
        for (String name : ingredientNames) {
            String trimmedName = name.trim();
            Calendar calendar = Calendar.getInstance();
            int imageResId = IngredientData.getImageResource(trimmedName);

            ingredients.add(new Ingredient(
                    trimmedName,
                    0,
                    "",
                    "",
                    calendar,
                    "냉장",
                    imageResId
            ));
        }

        return ingredients;
    }

    private static void collectKoreanNames(Set<String> destination, String[] dataPairs) {
        for (int i = 0; i < dataPairs.length; i += 2) {
            destination.add(dataPairs[i]);
        }
    }
}
