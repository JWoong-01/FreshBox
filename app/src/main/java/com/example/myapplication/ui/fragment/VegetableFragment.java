package com.example.myapplication.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.myapplication.R;
import com.example.myapplication.ui.activity.AddDetailActivity;

public class VegetableFragment extends Fragment {

    public VegetableFragment() {
        // 기본 생성자
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // 카테고리 Fragment 레이아웃을 반환
        return inflater.inflate(R.layout.add_vegetable, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // 이미지버튼 배열
        int[] buttonIds = {
                R.id.iv_carrot, R.id.iv_paprika, R.id.iv_eggplant, R.id.iv_onion,
                R.id.iv_potato, R.id.iv_swpotato, R.id.iv_broccoli, R.id.iv_greenonion,
                R.id.iv_garlic, R.id.iv_zucchini, R.id.iv_pepper, R.id.iv_cabbage,
                R.id.iv_bokchoy, R.id.iv_radish,  R.id.iv_cucumber, R.id.iv_lettuce,
                R.id.iv_mushroom
        };

        // 재료 이름 배열
        String[] itemNames = {
                "당근", "피망", "가지", "양파", "감자", "고구마", "브로콜리", "대파",
                "마늘", "애호박", "고추", "양배추", "청경채", "무", "오이", "상추", "버섯"
        };

        // 재료 이미지 리소스 배열
        int[] itemImages = {
                R.drawable.it_carrot, R.drawable.it_paprika, R.drawable.it_eggplant,
                R.drawable.it_onion, R.drawable.it_potato, R.drawable.it_sweetpotato,
                R.drawable.it_broccoli, R.drawable.it_greenonion, R.drawable.it_garlic,
                R.drawable.it_zucchini, R.drawable.it_pepper, R.drawable.it_cabbage,
                R.drawable.it_bokchoy, R.drawable.it_radish, R.drawable.it_cucumber,
                R.drawable.it_lettuce, R.drawable.it_mushroom
        };


        // 각 버튼에 클릭 리스너 추가
        for (int i = 0; i < buttonIds.length; i++) {
            final int index = i;  // 클릭 리스너 안에서 사용하기 위한 인덱스
            View button = view.findViewById(buttonIds[i]);

            // 이미지버튼이 null인지 확인
            if (button == null) {
                Log.e("VegetableFragment", "Button with ID " + buttonIds[i] + " not found!");
                continue;  // 버튼을 찾을 수 없으면 다음 버튼으로 넘어감
            }

            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // AddDetailActivity로 이동
                    Intent intent = new Intent(getActivity(), AddDetailActivity.class);
                    intent.putExtra("itemName", itemNames[index]); // 재료 이름
                    intent.putExtra("itemImage", itemImages[index]); // 재료 이미지 리소스 ID
                    startActivity(intent);
                }
            });
        }
    }
}
