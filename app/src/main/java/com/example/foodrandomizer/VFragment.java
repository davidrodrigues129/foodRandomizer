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

import com.example.foodrandomizer.databinding.FragmentVBinding;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
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

    private Map<String, String> customImageUris = new HashMap<>();

    private Uri selectedImageUri;
    private View dialogView;

    private Map<String, String> recipeSteps = new HashMap<>();


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


        recipeSteps.put("Arroz de atum", "1. Cook rice.\n2. Add tuna.\n3. Mix and serve.");
        recipeSteps.put("Bacalhau à Assis", "1. Prepare codfish.\n2. Add ingredients.\n3. Cook until done.");
        recipeSteps.put("Penne de atum com manjericão", "1. Boil penne.\n2. Add tuna.\n3. Mix with basil and serve.");
        //Add more here



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

                                saveRecipes();

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
                        // Default image
                        binding.foodImageView.setImageResource(R.drawable.nova_receita);
                    }
                })
                .setPositiveButton("OK", null)
                .show();
    }

    private void showRecipeSteps(String recipeName) {
        String steps = recipeSteps.get(recipeName);
        if (steps == null) {
            steps = "No steps available for this recipe.";
        }

        new AlertDialog.Builder(getContext())
                .setTitle(recipeName + " - Steps")
                .setMessage(steps)
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

        initializeData();
        loadRecipes();

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

        foodImageView.setOnClickListener(v -> {
            String currentRecipe = foodTextView.getText().toString();
            if (!currentRecipe.isEmpty()) {
                showRecipeSteps(currentRecipe);
            } else {
                Toast.makeText(getContext(), "No recipe selected", Toast.LENGTH_SHORT).show();
            }
        });
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

                        saveRecipes();

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

    private void saveRecipes() {
        SharedPreferences sharedPreferences = getContext().getSharedPreferences(PREFS_NAME, getContext().MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        StringBuilder recipeListBuilder = new StringBuilder();
        for (String recipe : foodList) {
            recipeListBuilder.append(recipe).append(",");
        }
        if (recipeListBuilder.length() > 0) {
            recipeListBuilder.setLength(recipeListBuilder.length() - 1);
        }
        editor.putString(RECIPE_LIST_KEY, recipeListBuilder.toString());

        for (Map.Entry<String, String> entry : customImageUris.entrySet()) {
            editor.putString(RECIPE_IMAGE_KEY_PREFIX + entry.getKey(), entry.getValue());
        }

        editor.apply();
    }

    private void loadRecipes() {
        SharedPreferences sharedPreferences = getContext().getSharedPreferences(PREFS_NAME, getContext().MODE_PRIVATE);

        String recipeListString = sharedPreferences.getString(RECIPE_LIST_KEY, "");
        if (!recipeListString.isEmpty()) {
            String[] recipes = recipeListString.split(",");
            foodList.clear();
            foodList.addAll(Arrays.asList(recipes));
        }

        for (String recipe : foodList) {
            String imageUriString = sharedPreferences.getString(RECIPE_IMAGE_KEY_PREFIX + recipe, "");
            if (!imageUriString.isEmpty()) {
                customImageUris.put(recipe, imageUriString);
            }
        }
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == Activity.RESULT_OK && data != null) {
            selectedImageUri = data.getData();
            if (selectedImageUri != null && dialogView != null) {
                ImageView selectedImageView = dialogView.findViewById(R.id.selectedImageView);
                selectedImageView.setImageURI(selectedImageUri);

                requireContext().getContentResolver().takePersistableUriPermission(
                        selectedImageUri,
                        Intent.FLAG_GRANT_READ_URI_PERMISSION
                );
            }
        }
}


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
