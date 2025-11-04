package com.example.myapplication.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import com.example.myapplication.R;
import com.example.myapplication.data.model.Ingredient;
import com.example.myapplication.data.repository.IngredientRepository;
import com.example.myapplication.ui.adapter.CategoryPagerAdapter;
import com.example.myapplication.ui.adapter.IngredientSearchAdapter;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import java.util.List;

public class AddIngredientActivity extends AppCompatActivity {
    private Button btn_fredge, btn_input;
    private TabLayout tabLayout;
    private ViewPager2 viewPager;
    private EditText searchBar;
    private RecyclerView searchResultRecycler;
    private IngredientSearchAdapter searchAdapter;
    private List<Ingredient> allIngredients;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_ingredient);

        tabLayout = findViewById(R.id.category_tab);
        viewPager = findViewById(R.id.ingredient_view);


        // 어댑터 설정
        CategoryPagerAdapter adapter = new CategoryPagerAdapter(this);
        viewPager.setAdapter(adapter);

        // TabLayout과 ViewPager2 연결
        new TabLayoutMediator(tabLayout, viewPager,
                (tab, position) -> {
                    switch (position) {
                        case 0:
                            tab.setText("과일");
                            break;
                        case 1:
                            tab.setText("채소");
                            break;
                        case 2:
                            tab.setText("유제품");
                            break;
                        case 3:
                            tab.setText("고기");
                            break;
                        case 4:
                            tab.setText("수산물");
                            break;
                        case 5:
                            tab.setText("양념");
                            break;
                        case 6:
                            tab.setText("기타");
                            break;
                        default:
                            tab.setText("");
                            break;
                    }
                }).attach();

        allIngredients = IngredientRepository.getAllIngredients();
        searchAdapter = new IngredientSearchAdapter(allIngredients, this);

        searchBar = findViewById(R.id.search_bar);
        searchResultRecycler = findViewById(R.id.search_result_recycler);
        searchResultRecycler.setLayoutManager(new LinearLayoutManager(this));
        searchResultRecycler.setAdapter(searchAdapter);
        searchResultRecycler.setVisibility(View.GONE);

        searchBar.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.toString().isEmpty()) {
                    tabLayout.setVisibility(View.VISIBLE);
                    viewPager.setVisibility(View.VISIBLE);
                    searchResultRecycler.setVisibility(View.GONE);
                    searchAdapter.filter(null);
                } else {
                    tabLayout.setVisibility(View.GONE);
                    viewPager.setVisibility(View.GONE);
                    searchResultRecycler.setVisibility(View.VISIBLE);
                    searchAdapter.filter(s.toString());
                }
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        btn_fredge = findViewById(R.id.btn_fredge);
        btn_fredge.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AddIngredientActivity.this, HomeActivity.class);
                startActivity(intent);
            }
        });

        btn_input = findViewById(R.id.btn_input);
        btn_input.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AddIngredientActivity.this, UserAddIngredientActivity.class);
                startActivity(intent);
            }
        });
    }
}
