package com.example.foodrandomizer;

import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import java.util.Arrays;
import java.util.List;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class MainActivity extends AppCompatActivity {

    private List<String> foodList = Arrays.asList("Pizza", "Sushi", "Burger", "Salad", "Pasta", "Tacos");
    private Map<String, Integer> foodImages = new HashMap<String, Integer>() {{
        put("Pizza", R.drawable.pizza);
        put("Sushi", R.drawable.sushi);
        put("Burger", R.drawable.burger);
        put("Salad", R.drawable.salad);
        put("Pasta", R.drawable.pasta);
        put("Tacos", R.drawable.tacos);
    }};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        TextView foodTextView = findViewById(R.id.foodName);
        ImageView foodImageView = findViewById(R.id.foodImageView);
        Button randomizeButton = findViewById(R.id.randomizeButton);

        randomizeButton.setOnClickListener(v -> {
            String randomFood = foodList.get(new Random().nextInt(foodList.size()));
            foodTextView.setText(randomFood);

            Integer imageResId = foodImages.get(randomFood);
            if (imageResId != null) {
                foodImageView.setImageResource(imageResId);
            }
        });
    }
}
