package com.example.myapplication.ui.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.R;
import com.example.myapplication.data.model.Ingredient;
import com.example.myapplication.ui.activity.AddDetailActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * 재료 검색 결과를 표시하고 선택 시 상세 추가 화면으로 이동시키는 어댑터.
 */
public class IngredientSearchAdapter extends RecyclerView.Adapter<IngredientSearchAdapter.ViewHolder> {

    private final List<Ingredient> originalIngredients;
    private final List<Ingredient> filteredIngredients;
    private final Context context;

    public IngredientSearchAdapter(List<Ingredient> ingredients, Context context) {
        this.originalIngredients = new ArrayList<>(ingredients);
        this.filteredIngredients = new ArrayList<>(ingredients);
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_search_result, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Ingredient ingredient = filteredIngredients.get(position);
        holder.nameTextView.setText(ingredient.getName());
        holder.iconImageView.setImageResource(ingredient.getImageResId());

        holder.itemView.setOnClickListener(v -> openAddDetailScreen(ingredient));
    }

    @Override
    public int getItemCount() {
        return filteredIngredients.size();
    }

    public void filter(String query) {
        String lowerQuery = query == null ? "" : query.trim().toLowerCase();
        filteredIngredients.clear();

        if (lowerQuery.isEmpty()) {
            filteredIngredients.addAll(originalIngredients);
        } else {
            for (Ingredient ingredient : originalIngredients) {
                if (ingredient.getName().toLowerCase().contains(lowerQuery)) {
                    filteredIngredients.add(ingredient);
                }
            }
        }

        notifyDataSetChanged();
    }

    private void openAddDetailScreen(Ingredient ingredient) {
        Intent intent = new Intent(context, AddDetailActivity.class);
        intent.putExtra("ingredient", ingredient);
        context.startActivity(intent);
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        final TextView nameTextView;
        final ImageView iconImageView;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            nameTextView = itemView.findViewById(R.id.tv_search_ingredient_name);
            iconImageView = itemView.findViewById(R.id.iv_search_ingredient_icon);
        }
    }
}
