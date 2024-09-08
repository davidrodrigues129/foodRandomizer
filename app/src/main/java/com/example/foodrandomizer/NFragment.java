package com.example.foodrandomizer;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
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

import com.example.foodrandomizer.databinding.FragmentNBinding;


import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;


public class NFragment extends Fragment {

    private FragmentNBinding binding;

    private List<String> foodList = Arrays.asList(
            "Arroz de atum",
            "Strogonoff",
            "Bolonhesa de carne",
            "Bacalhau à Brás",
            "Bacalhau à Gomes de Sá",
            "Bifana",
            "Bifes",
            "Bifes de Perú",
            "Massa com fiambre, atum, salsicha e ovo",
            "Pizza",
            "Hambúrguer",
            "Panados",
            "Filetes de pescada",
            "Peixe cozido",
            "Feijoada",
            "Cozido à portuguesa",
            "Rancho",
            "Vitela estufada",
            "Massa de frango",
            "Arroz de carne",
            "Frango frito",
            "Francesinha",
            "Omelete",
            "Nova receita"
    );
    private Map<String, Integer> foodImages = new HashMap<String, Integer>() {{
        put("Arroz de atum", R.drawable.arroz_de_atum);
        put("Strogonoff", R.drawable.strogonoff);
        put("Bolonhesa de carne", R.drawable.bolonhesa_de_carne);
        put("Bacalhau à Brás", R.drawable.bacalhau_a_bras);
        put("Nova receita", R.drawable.nova_receita);
        put("Bifes", R.drawable.bife);
        put("Bifes de Perú", R.drawable.bife_de_peru);
        put("Massa com fiambre, atum, salsicha e ovo", R.drawable.massa_com_salsicha_atum_salsicha_ovo);
        put("Pizza", R.drawable.pizza);
        put("Hambúrguer", R.drawable.burger);
        put("Panados", R.drawable.panados);
        put("Bacalhau à Gomes de Sá", R.drawable.bacalhau_a_gomes_de_sa);
        put("Bifana", R.drawable.bifana);
        put("Filetes de pescada", R.drawable.filetes_de_pescada);
        put("Peixe cozido", R.drawable.peixe_cozido);
        put("Feijoada", R.drawable.feijoada);
        put("Cozido à portuguesa", R.drawable.cozido_a_portuguesa);
        put("Rancho", R.drawable.rancho);
        put("Vitela estufada", R.drawable.vitela_estufada);
        put("Massa de frango", R.drawable.massa_de_frango);
        put("Arroz de carne", R.drawable.arroz_de_carne);
        put("Frango frito", R.drawable.frango_frito);
        put("Francesinha", R.drawable.francesinha);
        put("Omelete", R.drawable.omelete);

    }};

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
        }

        return super.onOptionsItemSelected(item);
    }


    private void showRecipeList() {
        String[] recipes = {
                "Arroz de atum",
                "Arroz de carne",
                "Bacalhau à Brás",
                "Bacalhau à Gomes de Sá",
                "Bifana",
                "Bifes",
                "Bifes de Perú",
                "Bolonhesa de carne",
                "Cozido à portuguesa",
                "Feijoada",
                "Filetes de pescada",
                "Francesinha",
                "Frango frito",
                "Hambúrguer",
                "Massa com fiambre, atum, salsicha e ovo",
                "Massa de frango",
                "Omelete",
                "Panados",
                "Peixe cozido",
                "Pizza",
                "Rancho",
                "Strogonoff",
                "Vitela estufada"
        };

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
        binding = FragmentNBinding.inflate(inflater, container, false);
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