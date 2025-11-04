package com.example.myapplication.ui.activity;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.*;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.VolleyError;
import com.example.myapplication.R;
import com.example.myapplication.data.model.ShoppingItem;
import com.example.myapplication.data.remote.ApiRequest;
import com.example.myapplication.ui.adapter.ShoppingCartAdapter;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class ShoppingCartActivity extends AppCompatActivity {

    private ArrayList<ShoppingItem> shoppingList;
    private ShoppingCartAdapter adapter;
    private final String[] unitArray = {"개", "봉지", "팩", "g", "ml"};

    private Button btnAddItem;
    private Button btnMoveToFridge;
    private boolean isSelectionMode = false;
    private ApiRequest apiRequest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shopping_cart);

        Button btnBack = findViewById(R.id.btn_back);
        btnBack.setOnClickListener(v -> finish());

        shoppingList = new ArrayList<>();
        apiRequest = new ApiRequest(this);
        adapter = new ShoppingCartAdapter(this, shoppingList, apiRequest);

        RecyclerView recyclerView = findViewById(R.id.recyclerViewShoppingCart);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        btnAddItem = findViewById(R.id.btn_add_item);
        btnAddItem.setOnClickListener(v -> showAddItemDialog());

        btnMoveToFridge = findViewById(R.id.btn_move_to_fridge);
        btnMoveToFridge.setOnClickListener(v -> {
            if (!isSelectionMode) {
                // 선택 모드 시작
                isSelectionMode = true;
                adapter.setSelectionMode(true);
                btnAddItem.setVisibility(View.GONE);
                btnMoveToFridge.setText("선택 항목 추가");
            } else {
                // 선택된 항목 서버에 전송
                List<ShoppingItem> selectedItems = adapter.getSelectedItems();
                if (selectedItems.isEmpty()) {
                    Toast.makeText(this, "선택된 항목이 없습니다.", Toast.LENGTH_SHORT).show();
                    return;
                }

                new AlertDialog.Builder(this)
                        .setTitle("내 냉장고에 추가")
                        .setMessage(selectedItems.size() + "개의 항목을 추가하시겠습니까?")
                        .setPositiveButton("추가", (dialog, which) -> addToFridge(selectedItems))
                        .setNegativeButton("취소", null)
                        .show();
            }
        });

        isSelectionMode = false;
        adapter.setSelectionMode(false);
    }

    @Override
    protected void onResume() {
        super.onResume();
        fetchShoppingCartItems();
    }

    private void fetchShoppingCartItems() {
        apiRequest.fetchShoppingItems(new ApiRequest.ShoppingFetchListener() {
            @Override
            public void onFetchSuccess(List<ShoppingItem> shoppingItems) {
                shoppingList.clear();
                shoppingList.addAll(shoppingItems);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onFetchError(VolleyError error) {
                Toast.makeText(ShoppingCartActivity.this, "장바구니를 불러오지 못했습니다.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void showAddItemDialog() {
        View dialogView = getLayoutInflater().inflate(R.layout.dialog_add_item, null);

        AutoCompleteTextView etName = dialogView.findViewById(R.id.et_item_name);
        ImageButton btnDecrease = dialogView.findViewById(R.id.btn_decrease);
        ImageButton  btnIncrease = dialogView.findViewById(R.id.btn_increase);
        TextView tvQuantity = dialogView.findViewById(R.id.tv_quantity);
        Spinner spinnerUnit = dialogView.findViewById(R.id.spinner_unit);

        String[] itemSuggestions = {"우유", "계란", "감자", "사과", "양파", "당근"};
        ArrayAdapter<String> nameAdapter = new ArrayAdapter<>(this, android.R.layout.simple_dropdown_item_1line, itemSuggestions);
        etName.setAdapter(nameAdapter);

        ArrayAdapter<String> unitAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, unitArray);
        unitAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerUnit.setAdapter(unitAdapter);

        final int[] quantity = {1};
        tvQuantity.setText(String.valueOf(quantity[0]));

        btnDecrease.setOnClickListener(v -> {
            if (quantity[0] > 1) {
                quantity[0]--;
                tvQuantity.setText(String.valueOf(quantity[0]));
            }
        });

        btnIncrease.setOnClickListener(v -> {
            quantity[0]++;
            tvQuantity.setText(String.valueOf(quantity[0]));
        });

        new AlertDialog.Builder(this)
                .setTitle("장바구니 항목 추가")
                .setView(dialogView)
                .setPositiveButton("추가", (dialog, which) -> {
                    String name = etName.getText().toString().trim();
                    String unit = spinnerUnit.getSelectedItem().toString();

                    if (!name.isEmpty()) {
                        apiRequest.addShoppingItem(name, quantity[0], unit, new ApiRequest.ApiCallback() {
                            @Override
                            public void onSuccess(String response) {
                                fetchShoppingCartItems();
                            }

                            @Override
                            public void onError(VolleyError error) {
                                Toast.makeText(ShoppingCartActivity.this, "장바구니 추가에 실패했습니다.", Toast.LENGTH_SHORT).show();
                            }
                        });
                    } else {
                        Toast.makeText(this, "품목명을 입력하세요.", Toast.LENGTH_SHORT).show();
                    }
                })
                .setNegativeButton("취소", null)
                .show();
    }

    private void addToFridge(List<ShoppingItem> selectedItems) {
        if (selectedItems.isEmpty()) {
            Toast.makeText(this, "선택된 항목이 없습니다.", Toast.LENGTH_SHORT).show();
            return;
        }

        String expirationDate = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
                .format(new Date(System.currentTimeMillis() + (7L * 24 * 60 * 60 * 1000)));

        apiRequest.moveItemsToFridge(selectedItems, "냉장", expirationDate, new ApiRequest.ApiCallback() {
            @Override
            public void onSuccess(String response) {
                Toast.makeText(ShoppingCartActivity.this, "선택된 항목을 냉장고에 추가했습니다!", Toast.LENGTH_SHORT).show();
                isSelectionMode = false;
                adapter.setSelectionMode(false);
                btnAddItem.setVisibility(View.VISIBLE);
                btnMoveToFridge.setText("내 냉장고에 추가");
                fetchShoppingCartItems();
            }

            @Override
            public void onError(VolleyError error) {
                Toast.makeText(ShoppingCartActivity.this, "서버 연결 오류", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
