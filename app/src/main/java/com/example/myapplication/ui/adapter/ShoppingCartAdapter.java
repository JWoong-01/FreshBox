package com.example.myapplication.ui.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.VolleyError;
import com.example.myapplication.R;
import com.example.myapplication.data.model.ShoppingItem;
import com.example.myapplication.data.remote.ApiRequest;

import java.util.List;

public class ShoppingCartAdapter extends RecyclerView.Adapter<ShoppingCartAdapter.ViewHolder> {

    private List<ShoppingItem> itemList;
    private final String[] unitArray = {"개", "봉지", "팩", "g", "ml"};
    private final Context context;
    private final ApiRequest apiRequest;
    private boolean isSelectionMode = false; // 선택 모드 상태

    public ShoppingCartAdapter(Context context, List<ShoppingItem> itemList, ApiRequest apiRequest) {
        this.context = context;
        this.itemList = itemList;
        this.apiRequest = apiRequest;
    }

    @NonNull
    @Override
    public ShoppingCartAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_shopping_cart, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ShoppingCartAdapter.ViewHolder holder, int position) {
        ShoppingItem item = itemList.get(position);
        holder.tvName.setText(item.getName());
        holder.tvQuantity.setText(item.getQuantity() + " " + item.getUnit());


        // 선택 모드 상태에 따라 체크박스 보이기
        holder.checkBox.setVisibility(isSelectionMode ? View.VISIBLE : View.GONE);
        holder.checkBox.setChecked(item.isChecked());

        holder.checkBox.setOnCheckedChangeListener((buttonView, isChecked) -> {
            item.setChecked(isChecked);
        });

        // 항목 클릭 시 (선택 모드 아닐 때만 수정 다이얼로그 열기)
        holder.itemView.setOnClickListener(v -> {
            if (!isSelectionMode) {
                showEditDialog(item, position);
            } else {
                // 선택 모드에서는 클릭 시 체크 상태 토글
                holder.checkBox.setChecked(!holder.checkBox.isChecked());
            }
        });

        // 삭제 버튼
        holder.btnDelete.setOnClickListener(v -> deleteItemFromDatabase(item.getId(), position));
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvName, tvQuantity;
        ImageButton btnDelete;
        CheckBox checkBox;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tv_item_name);
            tvQuantity = itemView.findViewById(R.id.tv_item_quantity);
            btnDelete = itemView.findViewById(R.id.btn_delete);
            checkBox = new CheckBox(itemView.getContext());
            // 체크박스를 LinearLayout의 맨 앞에 동적으로 추가
            ((LinearLayout) itemView).addView(checkBox, 0);
        }
    }

    private void showEditDialog(ShoppingItem item, int position) {
        View dialogView = LayoutInflater.from(context).inflate(R.layout.dialog_add_item, null);

        AutoCompleteTextView etName = dialogView.findViewById(R.id.et_item_name);
        Button btnDecrease = dialogView.findViewById(R.id.btn_decrease);
        Button btnIncrease = dialogView.findViewById(R.id.btn_increase);
        TextView tvQuantity = dialogView.findViewById(R.id.tv_quantity);
        Spinner spinnerUnit = dialogView.findViewById(R.id.spinner_unit);

        etName.setText(item.getName());
        tvQuantity.setText(String.valueOf(item.getQuantity()));

        ArrayAdapter<String> adapter = new ArrayAdapter<>(context, android.R.layout.simple_spinner_item, unitArray);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerUnit.setAdapter(adapter);

        int unitPosition = 0;
        for (int i = 0; i < unitArray.length; i++) {
            if (unitArray[i].equals(item.getUnit())) {
                unitPosition = i;
                break;
            }
        }
        spinnerUnit.setSelection(unitPosition);

        btnDecrease.setOnClickListener(v -> {
            int q = Integer.parseInt(tvQuantity.getText().toString());
            if (q > 1) tvQuantity.setText(String.valueOf(q - 1));
        });

        btnIncrease.setOnClickListener(v -> {
            int q = Integer.parseInt(tvQuantity.getText().toString());
            tvQuantity.setText(String.valueOf(q + 1));
        });

        new AlertDialog.Builder(context)
                .setTitle("품목 수정")
                .setView(dialogView)
                .setPositiveButton("저장", (dialog, which) -> {
                    String name = etName.getText().toString().trim();
                    String unit = spinnerUnit.getSelectedItem().toString();
                    int quantity = Integer.parseInt(tvQuantity.getText().toString());

                    if (!name.isEmpty()) {
                        item.setName(name);
                        item.setQuantity(quantity);
                        item.setUnit(unit);
                        notifyItemChanged(position);
                        updateItemInDatabase(item);
                    }
                })
                .setNegativeButton("취소", null)
                .show();
    }

    private void updateItemInDatabase(ShoppingItem item) {
        apiRequest.updateShoppingItem(item.getId(), item.getName(), item.getQuantity(), item.getUnit(), new ApiRequest.ApiCallback() {
            @Override
            public void onSuccess(String response) {
                Toast.makeText(context, "수정되었습니다.", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(VolleyError error) {
                Toast.makeText(context, "수정 실패", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void deleteItemFromDatabase(long id, int position) {
        apiRequest.deleteShoppingItem(id, new ApiRequest.ApiCallback() {
            @Override
            public void onSuccess(String response) {
                itemList.remove(position);
                notifyItemRemoved(position);
                notifyItemRangeChanged(position, itemList.size());
                Toast.makeText(context, "삭제되었습니다.", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(VolleyError error) {
                Toast.makeText(context, "서버 통신 오류", Toast.LENGTH_SHORT).show();
            }
        });
    }

    // 선택 모드 상태 토글
    public void setSelectionMode(boolean enabled) {
        isSelectionMode = enabled;
        if (!enabled) {
            // 선택 해제
            for (ShoppingItem item : itemList) {
                item.setChecked(false);
            }
        }
        notifyDataSetChanged();
    }

    // 선택된 항목 리스트 반환
    public List<ShoppingItem> getSelectedItems() {
        return itemList.stream().filter(ShoppingItem::isChecked).toList();
    }
}
