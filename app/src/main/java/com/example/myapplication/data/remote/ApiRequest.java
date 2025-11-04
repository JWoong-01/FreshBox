package com.example.myapplication.data.remote;

import android.content.Context;
import android.net.Uri;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.myapplication.data.model.Ingredient;
import com.example.myapplication.data.model.IngredientData;
import com.example.myapplication.data.model.Recipe;
import com.example.myapplication.data.model.ShoppingItem;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class ApiRequest {

    private static final String TAG = "ApiRequest";
    private static final String INGREDIENTS_URL = ServerConfig.BASE_URL + "/api/ingredients";
    private static final String SHOPPING_ITEMS_URL = ServerConfig.BASE_URL + "/api/shopping-items";
    private static final String RECIPES_URL = ServerConfig.BASE_URL + "/api/recipes";

    private final Context context;
    private final RequestQueue requestQueue;

    public ApiRequest(Context context) {
        this.context = context.getApplicationContext();
        this.requestQueue = Volley.newRequestQueue(this.context);
    }

    public void updateShoppingItem(long id, String name, int quantity, String unit, final ApiCallback callback) {
        JSONObject body = new JSONObject();
        try {
            body.put("name", name);
            body.put("quantity", quantity);
            body.put("unit", unit);
        } catch (JSONException e) {
            Log.e(TAG, "장바구니 수정 JSON 구성 실패", e);
            if (callback != null) callback.onError(new VolleyError(e));
            return;
        }

        String url = SHOPPING_ITEMS_URL + "/" + id;
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.PUT, url, body,
                response -> {
                    if (callback != null) callback.onSuccess(response.toString());
                },
                error -> {
                    if (callback != null) callback.onError(error);
                });

        requestQueue.add(request);
    }

    public void moveItemsToFridge(List<ShoppingItem> items, String storageLocation, String expirationDate,
                                  final ApiCallback callback) {
        JSONArray itemIds = new JSONArray();
        for (ShoppingItem item : items) {
            itemIds.put(item.getId());
        }

        JSONObject body = new JSONObject();
        try {
            String today = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());
            body.put("itemIds", itemIds);
            body.put("storageLocation", storageLocation);
            body.put("intakeDate", today);
            if (expirationDate != null) {
                body.put("expirationDate", expirationDate);
            }
        } catch (JSONException e) {
            Log.e(TAG, "장바구니 이동 JSON 구성 실패", e);
            if (callback != null) callback.onError(new VolleyError(e));
            return;
        }

        String url = SHOPPING_ITEMS_URL + "/move-to-fridge";
        StringRequest request = new StringRequest(Request.Method.POST, url,
                response -> {
                    if (callback != null) callback.onSuccess(response);
                },
                error -> {
                    if (callback != null) callback.onError(error);
                }) {
            @Override
            public byte[] getBody() {
                return body.toString().getBytes(StandardCharsets.UTF_8);
            }

            @Override
            public String getBodyContentType() {
                return "application/json; charset=UTF-8";
            }
        };

        requestQueue.add(request);
    }

    /* ===================== 장바구니 ===================== */

    public void addShoppingItem(String name, int quantity, String unit, final ApiCallback callback) {
        JSONObject body = new JSONObject();
        try {
            body.put("name", name);
            body.put("quantity", quantity);
            body.put("unit", unit);
        } catch (JSONException e) {
            Log.e(TAG, "장바구니 추가 JSON 구성 실패", e);
            if (callback != null) callback.onError(new VolleyError(e));
            return;
        }

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, SHOPPING_ITEMS_URL, body,
                response -> {
                    Toast.makeText(context, "장바구니에 추가되었습니다.", Toast.LENGTH_SHORT).show();
                    if (callback != null) {
                        callback.onSuccess(response.toString());
                    }
                },
                error -> {
                    Log.e(TAG, "장바구니 추가 오류", error);
                    Toast.makeText(context, "장바구니 추가 중 오류가 발생했습니다.", Toast.LENGTH_SHORT).show();
                    if (callback != null) {
                        callback.onError(error);
                    }
                });

        requestQueue.add(request);
    }

    public void fetchShoppingItems(final ShoppingFetchListener listener) {
        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, SHOPPING_ITEMS_URL, null,
                response -> listener.onFetchSuccess(parseShoppingItems(response)),
                error -> {
                    Log.e(TAG, "장바구니 불러오기 오류", error);
                    listener.onFetchError(error);
                });

        requestQueue.add(request);
    }

    public void deleteShoppingItem(long id, final ApiCallback callback) {
        String url = SHOPPING_ITEMS_URL + "/" + id;
        StringRequest request = new StringRequest(Request.Method.DELETE, url,
                response -> {
                    Toast.makeText(context, "장바구니에서 삭제되었습니다.", Toast.LENGTH_SHORT).show();
                    if (callback != null) callback.onSuccess(response);
                },
                error -> {
                    Log.e(TAG, "장바구니 삭제 오류", error);
                    Toast.makeText(context, "장바구니 삭제 중 오류가 발생했습니다.", Toast.LENGTH_SHORT).show();
                    if (callback != null) callback.onError(error);
                });

        requestQueue.add(request);
    }

    private List<ShoppingItem> parseShoppingItems(JSONArray jsonArray) {
        List<ShoppingItem> shoppingItems = new ArrayList<>();
        try {
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                int id = jsonObject.optInt("id");
                String name = jsonObject.optString("name", "");
                int quantity = jsonObject.optInt("quantity", 0);
                String unit = jsonObject.optString("unit", "개");
                shoppingItems.add(new ShoppingItem(id, name, quantity, unit));
            }
        } catch (JSONException e) {
            Log.e(TAG, "장바구니 파싱 오류", e);
        }
        return shoppingItems;
    }

    /* ===================== 재료 ===================== */

    public void addIngredient(String itemName, int quantity, String unit, String intakeDate,
                              String expirationDate, String storageLocation, int image) {
        JSONObject body = new JSONObject();
        try {
            body.put("name", itemName);
            body.put("quantity", quantity);
            body.put("unit", unit);
            body.put("intakeDate", intakeDate);
            body.put("expirationDate", expirationDate);
            body.put("storageLocation", storageLocation);
            body.put("image", image);
        } catch (JSONException e) {
            Log.e(TAG, "재료 추가 JSON 구성 실패", e);
            Toast.makeText(context, "요청 생성 중 오류가 발생했습니다.", Toast.LENGTH_SHORT).show();
            return;
        }

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, INGREDIENTS_URL, body,
                response -> Toast.makeText(context, "재료가 추가되었습니다.", Toast.LENGTH_SHORT).show(),
                error -> {
                    Log.e(TAG, "재료 추가 오류", error);
                    Toast.makeText(context, "오류 발생: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                });

        requestQueue.add(request);
    }

    public void fetchIngredients(final IngredientFetchListener listener) {
        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, INGREDIENTS_URL, null,
                response -> listener.onFetchSuccess(parseIngredients(response)),
                error -> {
                    Log.e(TAG, "재료 조회 오류", error);
                    listener.onFetchError(error);
                });

        requestQueue.add(request);
    }

    private List<Ingredient> parseIngredients(JSONArray jsonArray) {
        List<Ingredient> ingredients = new ArrayList<>();
        try {
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                Long id = jsonObject.has("id") ? jsonObject.getLong("id") : null;
                String name = jsonObject.optString("name", "");
                int quantity = jsonObject.optInt("quantity", 0);
                String unit = jsonObject.optString("unit", "개");
                String intakeDate = jsonObject.optString("intakeDate", "");
                String expirationDateString = jsonObject.optString("expirationDate", "");
                String storageLocation = jsonObject.optString("storageLocation", "냉장");
                int image = jsonObject.optInt("image", IngredientData.getImageResource(name));

                Calendar expirationDate = Calendar.getInstance();
                if (!expirationDateString.isEmpty()) {
                    try {
                        String[] dateParts = expirationDateString.split("-");
                        int year = Integer.parseInt(dateParts[0]);
                        int month = Integer.parseInt(dateParts[1]) - 1;
                        int day = Integer.parseInt(dateParts[2]);
                        expirationDate.set(year, month, day);
                    } catch (Exception e) {
                        Log.w(TAG, "유통기한 파싱 실패: " + expirationDateString, e);
                    }
                }

                Ingredient ingredient = new Ingredient(id, name, quantity, unit, intakeDate, expirationDate, storageLocation, image);
                ingredients.add(ingredient);
            }
        } catch (JSONException e) {
            Log.e(TAG, "재료 파싱 오류", e);
        }
        return ingredients;
    }

    public void deleteIngredientByName(String name, final ApiDeleteListener listener) {
        try {
            String url = INGREDIENTS_URL + "?name=" + URLEncoder.encode(name, StandardCharsets.UTF_8.name());
            StringRequest request = new StringRequest(Request.Method.DELETE, url,
                    response -> {
                        if (listener != null) listener.onDeleteSuccess();
                    },
                    error -> {
                        Log.e(TAG, "재료 삭제 오류", error);
                        if (listener != null) listener.onDeleteError();
                    });
            requestQueue.add(request);
        } catch (Exception e) {
            Log.e(TAG, "재료 삭제 URL 구성 실패", e);
            if (listener != null) listener.onDeleteError();
        }
    }

    public void updateIngredient(Long id, String name, int quantity, String unit, String intakeDate,
                                 String expirationDate, String storageLocation, int imageResId,
                                 final ApiUpdateListener listener) {
        if (id == null) {
            Log.e(TAG, "재료 ID가 없어 수정할 수 없습니다.");
            if (listener != null) listener.onUpdateError();
            return;
        }

        JSONObject body = new JSONObject();
        try {
            body.put("name", name);
            body.put("quantity", quantity);
            body.put("unit", unit);
            body.put("intakeDate", intakeDate);
            body.put("expirationDate", expirationDate);
            body.put("storageLocation", storageLocation);
            body.put("image", imageResId);
        } catch (JSONException e) {
            Log.e(TAG, "재료 수정 JSON 구성 실패", e);
            if (listener != null) listener.onUpdateError();
            return;
        }

        String url = INGREDIENTS_URL + "/" + id;
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.PUT, url, body,
                response -> {
                    if (listener != null) listener.onUpdateSuccess();
                },
                error -> {
                    Log.e(TAG, "재료 수정 오류", error);
                    if (listener != null) listener.onUpdateError();
                });

        requestQueue.add(request);
    }

    /* ===================== 레시피 ===================== */

    public void fetchRecipesByIngredients(List<String> ingredients, final RecipeFetchListener listener) {
        // Spring 서버에서는 keyword 기반 검색을 제공하므로 재료를 키워드로 묶어서 조회한다.
        StringBuilder keywordBuilder = new StringBuilder();
        for (int i = 0; i < ingredients.size(); i++) {
            keywordBuilder.append(ingredients.get(i));
            if (i < ingredients.size() - 1) {
                keywordBuilder.append(",");
            }
        }
        String keyword = keywordBuilder.toString();
        String url = RECIPES_URL;
        if (!keyword.isEmpty()) {
            url += "?keyword=" + Uri.encode(keyword);
        }

        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, url, null,
                response -> listener.onFetchSuccess(parseRecipeList(response)),
                error -> listener.onFetchError(error));

        requestQueue.add(request);
    }

    public void fetchAllRecipes(final RecipeFetchListener listener) {
        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, RECIPES_URL, null,
                response -> listener.onFetchSuccess(parseRecipeList(response)),
                error -> listener.onFetchError(error));

        requestQueue.add(request);
    }

    private List<Recipe> parseRecipeList(JSONArray array) {
        List<Recipe> recipes = new ArrayList<>();
        for (int i = 0; i < array.length(); i++) {
            try {
                JSONObject recipeObject = array.getJSONObject(i);
                String name = recipeObject.optString("name", "");
                String imageUrl = recipeObject.optString("imageUrl", "");
                String ingredients = recipeObject.optString("ingredients", "");
                String instructions = recipeObject.optString("instructions", "");
                recipes.add(new Recipe(name, imageUrl, ingredients, instructions));
            } catch (JSONException e) {
                Log.e(TAG, "레시피 파싱 오류", e);
            }
        }
        return recipes;
    }

    /* 기존 공공데이터 API 호출 로직은 유지 (필요 시 사용) */
    public void fetchRecipesFromXMLAPI(final RecipeFetchListener listener) {
        String url = "http://openapi.foodsafetykorea.go.kr/api/cf37f4688166446c8e5e/COOKRCP01/xml/1/100";

        StringRequest request = new StringRequest(Request.Method.GET, url,
                response -> {
                    try {
                        List<Recipe> recipes = parseXML(response);
                        listener.onFetchSuccess(recipes);
                    } catch (Exception e) {
                        Log.e(TAG, "XML 파싱 실패", e);
                        listener.onFetchError(null);
                    }
                },
                error -> {
                    Log.e(TAG, "공공 API(XML) 요청 실패", error);
                    listener.onFetchError(error);
                });

        requestQueue.add(request);
    }

    private List<Recipe> parseXML(String xml) throws Exception {
        List<Recipe> recipes = new ArrayList<>();
        Recipe currentRecipe = null;

        XmlPullParser parser = XmlPullParserFactory.newInstance().newPullParser();
        parser.setInput(new StringReader(xml));
        int eventType = parser.getEventType();

        while (eventType != XmlPullParser.END_DOCUMENT) {
            String tag = parser.getName();

            switch (eventType) {
                case XmlPullParser.START_TAG:
                    if ("row".equals(tag)) {
                        currentRecipe = new Recipe();
                    } else if (currentRecipe != null) {
                        if ("RCP_NM".equals(tag)) {
                            currentRecipe.setName(parser.nextText());
                        } else if ("ATT_FILE_NO_MAIN".equals(tag)) {
                            currentRecipe.setImageUrl(parser.nextText());
                        } else if ("RCP_PARTS_DTLS".equals(tag)) {
                            currentRecipe.setIngredients(parser.nextText());
                        } else if (tag.startsWith("MANUAL")) {
                            String step = parser.nextText();
                            if (!step.trim().isEmpty()) {
                                String inst = currentRecipe.getInstructions() != null ? currentRecipe.getInstructions() : "";
                                currentRecipe.setInstructions(inst + step + "\n");
                            }
                        }
                    }
                    break;

                case XmlPullParser.END_TAG:
                    if ("row".equals(tag) && currentRecipe != null) {
                        recipes.add(currentRecipe);
                        currentRecipe = null;
                    }
                    break;
            }

            eventType = parser.next();
        }

        return recipes;
    }

    /* ===================== 콜백 인터페이스 ===================== */

    public interface ApiCallback {
        void onSuccess(String response);
        void onError(VolleyError error);
    }

    public interface ShoppingFetchListener {
        void onFetchSuccess(List<ShoppingItem> shoppingItems);
        void onFetchError(VolleyError error);
    }

    public interface IngredientFetchListener {
        void onFetchSuccess(List<Ingredient> ingredients);
        void onFetchError(VolleyError error);
    }

    public interface ApiDeleteListener {
        void onDeleteSuccess();
        void onDeleteError();
    }

    public interface ApiUpdateListener {
        void onUpdateSuccess();
        void onUpdateError();
    }

    public interface RecipeFetchListener {
        void onFetchSuccess(List<Recipe> recipes);
        void onFetchError(VolleyError error);
    }
}
