package com.example.foodrandomizer;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.foodrandomizer.databinding.FragmentVBinding;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;


public class NFragment extends Fragment {

    private FragmentVBinding binding;

    private List<String> foodList = Arrays.asList("Pizza", "Sushi", "Burger", "Salad", "Pasta", "Tacos");
    private Map<String, Integer> foodImages = new HashMap<String, Integer>() {{
        put("Pizza", R.drawable.pizza);
        put("Sushi", R.drawable.sushi);
        put("Burger", R.drawable.burger);
        put("Salad", R.drawable.salad);
        put("Pasta", R.drawable.pasta);
        put("Tacos", R.drawable.tacos);
    }};

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentVBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        TextView foodTextView = binding.foodName;
        ImageView foodImageView = binding.foodImageView;
        Button randomizeButton = binding.randomizeButton;

        randomizeButton.setOnClickListener(v -> {
            String randomFood = foodList.get(new Random().nextInt(foodList.size()));
            foodTextView.setText(randomFood);

            Integer imageResId = foodImages.get(randomFood);
            if (imageResId != null) {
                foodImageView.setImageResource(imageResId);
            }
        });
    }

    private void replaceFragment(Fragment fragment) {
        FragmentManager fragmentManager = getParentFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frameLayout, fragment);
        fragmentTransaction.commit();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}