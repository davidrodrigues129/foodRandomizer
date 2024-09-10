package com.example.foodrandomizer;

import android.app.AlertDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.foodrandomizer.databinding.FragmentVBinding;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class VFragment extends Fragment {

    private FragmentVBinding binding;

    private static final String PREFS_NAME = "RecipePrefs";
    private static final String RECIPE_LIST_KEY = "recipeList";
    private static final String RECIPE_IMAGE_KEY_PREFIX = "recipeImage_";

    private List<String> foodList = new ArrayList<>();
    private Map<String, Integer> foodImages = new HashMap<>();

    // Sample food list and images (initial data)
    private void initializeData() {
        foodList.add("Arroz de atum");
        foodList.add("Bacalhau à Assis");
        foodList.add("Bacalhau à Brás");
        foodList.add("Bifinhos Rápidos");
        foodList.add("Bolonhesa de carne");
        foodList.add("Caril de Frango com arroz");
        foodList.add("Chili com carne");
        foodList.add("Esparguete com atum e molho arrabiata");
        foodList.add("Massada de peixe");
        foodList.add("Massinha de camarão e amêijoas");
        foodList.add("Penne com frango e rúcula");
        foodList.add("Penne de atum com manjericão");
        foodList.add("Peixe ao Sal");
        foodList.add("Saltear frango em tiras");
        foodList.add("Strogonoff");
        foodList.add("Strogonoff de frango com castanhas");
        foodList.add("Tirinhas de porco salteadas");

        foodImages.put("Arroz de atum", R.drawable.arroz_de_atum);
        foodImages.put("Bacalhau à Assis", R.drawable.bacalhau_a_assis);
        foodImages.put("Bacalhau à Brás", R.drawable.bacalhau_a_bras);
        foodImages.put("Bifinhos Rápidos", R.drawable.bifinhos_rapidos);
        foodImages.put("Bolonhesa de carne", R.drawable.bolonhesa_de_carne);
        foodImages.put("Caril de Frango com arroz", R.drawable.caril_de_frango_com_arroz);
        foodImages.put("Chili com carne", R.drawable.chili_com_carne);
        foodImages.put("Esparguete com atum e molho arrabiata", R.drawable.esparguete_com_atum_e_molho_arrabiata);
        foodImages.put("Massada de peixe", R.drawable.massada_de_peixe);
        foodImages.put("Massinha de camarão e amêijoas", R.drawable.massinha_de_camarao_e_ameijoas);
        foodImages.put("Penne com frango e rúcula", R.drawable.penne_com_frango_e_rucula);
        foodImages.put("Penne de atum com manjericão", R.drawable.penne_de_atum_com_manjericao);
        foodImages.put("Peixe ao Sal", R.drawable.peixe_ao_sal);
        foodImages.put("Saltear frango em tiras", R.drawable.saltear_frango_em_tiras);
        foodImages.put("Strogonoff", R.drawable.strogonoff);
        foodImages.put("Strogonoff de frango com castanhas", R.drawable.strogonoff_de_frango_com_castanhas);
        foodImages.put("Tirinhas de porco salteadas", R.drawable.tirinhas_de_porco_salteadas);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        //SharedPreferences sharedPreferences = getContext().getSharedPreferences(PREFS_NAME, getContext().MODE_PRIVATE);
        //sharedPreferences.edit().clear().apply();
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.menu_main, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_list) {
            showRecipeList();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void showRecipeList() {
        String[] recipes = foodList.toArray(new String[0]);

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Recipe List")
                .setItems(recipes, (dialog, which) -> {
                    String selectedRecipe = recipes[which];
                    binding.foodName.setText(selectedRecipe);

                    Integer imageResId = foodImages.get(selectedRecipe);
                    if (imageResId != null) {
                        binding.foodImageView.setImageResource(imageResId);
                    }
                })
                .setPositiveButton("OK", null)
                .show();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentVBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initializeData();  // Initialize food list and images
        loadRecipes(); // Load recipes from SharedPreferences <------------------- Error here

        TextView foodTextView = binding.foodName;
        ImageView foodImageView = binding.foodImageView;
        Button randomizeButton = binding.randomizeButton;
        Button addRecipeButton = binding.addRecipeButton;

        randomizeButton.setOnClickListener(v -> {
            String randomFood = foodList.get(new Random().nextInt(foodList.size()));
            String normalizedRecipe = randomFood.toLowerCase().trim(); // Normalize key
            foodTextView.setText(randomFood);

            Integer imageResId = foodImages.get(randomFood);
            if (imageResId != null) {
                foodImageView.setImageResource(imageResId);
                Log.d("Randomizer", "Set image for " + randomFood + " with resource ID: " + imageResId);
            } else {
                Log.d("Randomizer", "No image found for " + randomFood);
            }
        });

        // Add recipe button functionality
        addRecipeButton.setOnClickListener(v -> showAddRecipeDialog());
    }

    // Function to show the add recipe dialog
    private void showAddRecipeDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_add_recipe, null);
        builder.setView(dialogView);

        // Get dialog fields
        EditText recipeNameInput = dialogView.findViewById(R.id.recipeNameInput);
        Button selectImageButton = dialogView.findViewById(R.id.selectImageButton);
        ImageView selectedImageView = dialogView.findViewById(R.id.selectedImageView);

        // Handle image selection (sample image for now)
        selectImageButton.setOnClickListener(v -> {
            selectedImageView.setImageResource(R.drawable.nova_receita);
        });

        builder.setTitle("Add a New Recipe")
                .setPositiveButton("Add", (dialog, which) -> {
                    String recipeName = recipeNameInput.getText().toString().trim();

                    // Check if the recipe name already exists (case-insensitive comparison)
                    boolean recipeExists = false;
                    for (String recipe : foodList) {
                        if (recipe.equalsIgnoreCase(recipeName)) {
                            recipeExists = true;
                            break;
                        }
                    }

                    if (recipeExists) {
                        // Show a message if the recipe already exists
                        AlertDialog.Builder errorBuilder = new AlertDialog.Builder(getContext());
                        errorBuilder.setTitle("Duplicate Recipe")
                                .setMessage("This recipe already exists. Please add a different recipe.")
                                .setPositiveButton("OK", null)
                                .show();
                    } else if (!recipeName.isEmpty()) {
                        // Add the new recipe to the list and map if it does not exist
                        foodList.add(recipeName);
                        foodImages.put(recipeName, R.drawable.nova_receita);

                        // Save the updated recipes to SharedPreferences
                        saveRecipes();

                        // Update the UI with the new recipe
                        binding.foodName.setText(recipeName);
                        binding.foodImageView.setImageResource(R.drawable.nova_receita);
                    }
                })
                .setNegativeButton("Cancel", (dialog, which) -> dialog.dismiss())
                .create()
                .show();
    }




    // Save recipes to SharedPreferences
    private void saveRecipes() {
        SharedPreferences sharedPreferences = getContext().getSharedPreferences(PREFS_NAME, getContext().MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        // Convert foodList to a single string (comma-separated)
        StringBuilder recipeListBuilder = new StringBuilder();
        for (String recipe : foodList) {
            recipeListBuilder.append(recipe).append(",");
        }

        // Remove trailing comma and save
        if (recipeListBuilder.length() > 0) {
            recipeListBuilder.setLength(recipeListBuilder.length() - 1);
        }
        editor.putString(RECIPE_LIST_KEY, recipeListBuilder.toString());

        // Save the associated images for each recipe with case normalization (toLowerCase and trim)
        for (Map.Entry<String, Integer> entry : foodImages.entrySet()) {
            String normalizedRecipe = entry.getKey().toLowerCase().trim();
            int imageResId = entry.getValue();

            editor.putInt(RECIPE_IMAGE_KEY_PREFIX + normalizedRecipe, imageResId);
            Log.d("SaveRecipes", "Saving image for " + entry.getKey() + " with ID: " + imageResId);
        }

        editor.apply();
    }


    // Load recipes from SharedPreferences
    private void loadRecipes() {
        SharedPreferences sharedPreferences = getContext().getSharedPreferences(PREFS_NAME, getContext().MODE_PRIVATE);

        // Get saved recipe list as a single string and split it back to the list
        String savedRecipes = sharedPreferences.getString(RECIPE_LIST_KEY, "");
        if (!savedRecipes.isEmpty()) {
            foodList.clear();
            foodList.addAll(Arrays.asList(savedRecipes.split(",")));
        }

        // Load the associated images for each recipe with case normalization (toLowerCase and trim)
        for (String recipe : foodList) {
            String normalizedRecipe = recipe.toLowerCase().trim();
            int imageResId = sharedPreferences.getInt(RECIPE_IMAGE_KEY_PREFIX + recipe, R.drawable.nova_receita);

            foodImages.put(normalizedRecipe, imageResId);
            Log.d("LoadRecipes", "Loaded image for " + recipe + " with ID: " + imageResId);
        }

        sharedPreferences.getAll();
    }




    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
