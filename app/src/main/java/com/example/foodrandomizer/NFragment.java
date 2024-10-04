package com.example.foodrandomizer;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.foodrandomizer.databinding.FragmentNBinding;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class NFragment extends Fragment {

    private FragmentNBinding binding;

    private static final String NEW_PREFS_NAME = "NewRecipePrefs";
    private static final String NEW_RECIPE_LIST_KEY = "newRecipeList";
    private static final String NEW_RECIPE_IMAGE_KEY_PREFIX = "newRecipeImage_";

    private List<String> foodList = new ArrayList<>();
    private Map<String, Integer> foodImages = new HashMap<>();
    private Map<String, String> customImageUris = new HashMap<>();
    private Uri selectedImageUri;
    private View dialogView;

    private void initializeData() {
        foodList.add("Arroz de atum");
        foodList.add("Arroz de carne");
        foodList.add("Bacalhau à Brás");
        foodList.add("Bacalhau à Gomes de Sá");
        foodList.add("Bifana");
        foodList.add("Bifes");
        foodList.add("Bifes de Perú");
        foodList.add("Bolonhesa de carne");
        foodList.add("Cozido à portuguesa");
        foodList.add("Feijoada");
        foodList.add("Filetes de pescada");
        foodList.add("Francesinha");
        foodList.add("Frango frito");
        foodList.add("Hambúrguer");
        foodList.add("Massa com fiambre e ovo");
        foodList.add("Massa de frango");
        foodList.add("Omelete");
        foodList.add("Panados");
        foodList.add("Peixe cozido");
        foodList.add("Pizza");
        foodList.add("Rancho");
        foodList.add("Strogonoff");
        foodList.add("Vitela estufada");

        foodImages.put("Arroz de atum", R.drawable.arroz_de_atum);
        foodImages.put("Arroz de carne", R.drawable.arroz_de_carne);
        foodImages.put("Bacalhau à Brás", R.drawable.bacalhau_a_bras);
        foodImages.put("Bacalhau à Gomes de Sá", R.drawable.bacalhau_a_gomes_de_sa);
        foodImages.put("Bifana", R.drawable.bifana);
        foodImages.put("Bifes", R.drawable.bife);
        foodImages.put("Bifes de Perú", R.drawable.bife_de_peru);
        foodImages.put("Bolonhesa de carne", R.drawable.bolonhesa_de_carne);
        foodImages.put("Cozido à portuguesa", R.drawable.cozido_a_portuguesa);
        foodImages.put("Feijoada", R.drawable.feijoada);
        foodImages.put("Filetes de pescada", R.drawable.filetes_de_pescada);
        foodImages.put("Francesinha", R.drawable.francesinha);
        foodImages.put("Frango frito", R.drawable.frango_frito);
        foodImages.put("Hambúrguer", R.drawable.burger);
        foodImages.put("Massa com fiambre e ovo", R.drawable.massa_com_salsicha_atum_salsicha_ovo);
        foodImages.put("Massa de frango", R.drawable.massa_de_frango);
        foodImages.put("Omelete", R.drawable.omelete);
        foodImages.put("Panados", R.drawable.panados);
        foodImages.put("Peixe cozido", R.drawable.peixe_cozido);
        foodImages.put("Pizza", R.drawable.pizza);
        foodImages.put("Rancho", R.drawable.rancho);
        foodImages.put("Strogonoff", R.drawable.strogonoff);
        foodImages.put("Vitela estufada", R.drawable.vitela_estufada);

        foodList.add("Nova receita");
        foodImages.put("Nova receita", R.drawable.nova_receita);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
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
        } else if (id == R.id.action_remove) {
            showRemoveRecipeDialog();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void showRemoveRecipeDialog() {
        String[] recipes = foodList.toArray(new String[0]);

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Remove Recipe")
                .setItems(recipes, (dialog, which) -> {
                    String selectedRecipe = recipes[which];

                    new AlertDialog.Builder(getContext())
                            .setTitle("Delete Recipe")
                            .setMessage("Are you sure you want to delete this recipe?")
                            .setPositiveButton("Yes", (confirmDialog, confirmWhich) -> {
                                foodList.remove(selectedRecipe);
                                foodImages.remove(selectedRecipe);
                                customImageUris.remove(selectedRecipe);

                                saveNewRecipes();

                                String displayedRecipe = binding.foodName.getText().toString();
                                if (selectedRecipe.equals(displayedRecipe)) {
                                    binding.foodName.setText("");
                                    binding.foodImageView.setImageDrawable(null);
                                }

                                Toast.makeText(getContext(), "Recipe removed successfully", Toast.LENGTH_SHORT).show();
                            })
                            .setNegativeButton("No", null)
                            .show();
                })
                .setPositiveButton("OK", null)
                .show();
    }

    private void showRecipeList() {
        List<String> filteredRecipes = new ArrayList<>(foodList);
        filteredRecipes.remove("Nova receita");

        String[] recipes = filteredRecipes.toArray(new String[0]);

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Recipe List")
                .setItems(recipes, (dialog, which) -> {
                    String selectedRecipe = recipes[which];
                    binding.foodName.setText(selectedRecipe);

                    if (foodImages.containsKey(selectedRecipe)) {
                        binding.foodImageView.setImageResource(foodImages.get(selectedRecipe));
                    } else if (customImageUris.containsKey(selectedRecipe)) {
                        Uri imageUri = Uri.parse(customImageUris.get(selectedRecipe));
                        binding.foodImageView.setImageURI(imageUri);
                    } else {
                        binding.foodImageView.setImageResource(R.drawable.nova_receita);
                    }
                })
                .setPositiveButton("OK", null)
                .show();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentNBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initializeData();
        loadNewRecipes();

        TextView foodTextView = binding.foodName;
        ImageView foodImageView = binding.foodImageView;
        ImageButton randomizeButton = binding.randomizeButton;
        ImageButton addRecipeButton = binding.addRecipeButton;

        randomizeButton.setOnClickListener(v -> {
            String randomFood = foodList.get(new Random().nextInt(foodList.size()));
            foodTextView.setText(randomFood);

            if (foodImages.containsKey(randomFood)) {
                foodImageView.setImageResource(foodImages.get(randomFood));
            } else if (customImageUris.containsKey(randomFood)) {
                foodImageView.setImageURI(Uri.parse(customImageUris.get(randomFood)));
            } else {
                foodImageView.setImageResource(R.drawable.nova_receita);
            }
        });

        addRecipeButton.setOnClickListener(v -> showAddRecipeDialog());
    }

    private void showAddRecipeDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        LayoutInflater inflater = getLayoutInflater();
        dialogView = inflater.inflate(R.layout.dialog_add_recipe, null);
        builder.setView(dialogView);

        EditText recipeNameInput = dialogView.findViewById(R.id.recipeNameInput);
        Button selectImageButton = dialogView.findViewById(R.id.selectImageButton);
        ImageView selectedImageView = dialogView.findViewById(R.id.selectedImageView);

        selectImageButton.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
            intent.setType("image/*");
            intent.addCategory(Intent.CATEGORY_OPENABLE);
            intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, false);
            intent.addFlags(Intent.FLAG_GRANT_PERSISTABLE_URI_PERMISSION);
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            startActivityForResult(intent, 1);
        });

        builder.setTitle("Add a New Recipe")
                .setPositiveButton("Add", (dialog, which) -> {
                    String recipeName = recipeNameInput.getText().toString().trim();
                    boolean recipeExists = foodList.stream().anyMatch(r -> r.equalsIgnoreCase(recipeName));

                    if (recipeExists) {
                        AlertDialog.Builder errorBuilder = new AlertDialog.Builder(getContext());
                        errorBuilder.setTitle("Duplicate Recipe")
                                .setMessage("This recipe already exists. Please add a different recipe.")
                                .setPositiveButton("OK", null)
                                .show();
                    } else if (!recipeName.isEmpty()) {
                        foodList.add(recipeName);

                        Collections.sort(foodList, String::compareToIgnoreCase);

                        if (selectedImageUri != null) {
                            customImageUris.put(recipeName, selectedImageUri.toString());
                        } else {
                            foodImages.put(recipeName, R.drawable.nova_receita);
                        }

                        saveNewRecipes();

                        binding.foodName.setText(recipeName);
                        if (selectedImageUri != null) {
                            binding.foodImageView.setImageURI(selectedImageUri);
                        }
                    }
                })
                .setNegativeButton("Cancel", (dialog, which) -> dialog.dismiss())
                .create()
                .show();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1 && resultCode == Activity.RESULT_OK && data != null) {
            selectedImageUri = data.getData();
            ImageView selectedImageView = dialogView.findViewById(R.id.selectedImageView);
            selectedImageView.setImageURI(selectedImageUri);
        }
    }

    private void saveNewRecipes() {
        SharedPreferences sharedPreferences = getContext().getSharedPreferences(NEW_PREFS_NAME, getContext().MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        StringBuilder recipeListBuilder = new StringBuilder();
        for (String recipe : foodList) {
            recipeListBuilder.append(recipe).append(",");
        }
        if (recipeListBuilder.length() > 0) {
            recipeListBuilder.setLength(recipeListBuilder.length() - 1);
        }
        editor.putString(NEW_RECIPE_LIST_KEY, recipeListBuilder.toString());

        for (Map.Entry<String, String> entry : customImageUris.entrySet()) {
            editor.putString(NEW_RECIPE_IMAGE_KEY_PREFIX + entry.getKey(), entry.getValue());
        }

        editor.apply();
    }

    private void loadNewRecipes() {
        SharedPreferences sharedPreferences = getContext().getSharedPreferences(NEW_PREFS_NAME, getContext().MODE_PRIVATE);

        String recipeListString = sharedPreferences.getString(NEW_RECIPE_LIST_KEY, "");
        if (!recipeListString.isEmpty()) {
            String[] recipes = recipeListString.split(",");
            foodList.clear();
            foodList.addAll(Arrays.asList(recipes));
        }

        for (String recipe : foodList) {
            String imageUriString = sharedPreferences.getString(NEW_RECIPE_IMAGE_KEY_PREFIX + recipe, "");
            if (!imageUriString.isEmpty()) {
                customImageUris.put(recipe, imageUriString);
            }
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
