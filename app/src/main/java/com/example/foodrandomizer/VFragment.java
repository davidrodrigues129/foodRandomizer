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

import com.example.foodrandomizer.databinding.FragmentVBinding;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class VFragment extends Fragment {

    private FragmentVBinding binding;

    private List<String> foodList = Arrays.asList(
            "Arroz de atum",
            "Bacalhau à Assis",
            "Bifinhos Rápidos",
            "Caril de Frango com arroz",
            "Penne com frango e rúcula",
            "Massinha de camarão e amêijoas",
            "Peixe ao Sal",
            "Strogonoff",
            "Chili com carne",
            "Tirinhas de porco salteadas",
            "Saltear frango em tiras",
            "Penne de atum com manjericão",
            "Esparguete com atum e molho arrabiata",
            "Strogonoff de frango com castanhas",
            "Massada de peixe",
            "Bolonhesa de carne",
            "Bacalhau à Brás",
            "Nova receita"
    );
    private Map<String, Integer> foodImages = new HashMap<String, Integer>() {{
        put("Arroz de atum", R.drawable.arroz_de_atum);
        put("Bacalhau à Assis", R.drawable.bacalhau_a_assis);
        put("Bifinhos Rápidos", R.drawable.bifinhos_rapidos);
        put("Caril de Frango com arroz", R.drawable.caril_de_frango_com_arroz);
        put("Penne com frango e rúcula", R.drawable.penne_com_frango_e_rucula);
        put("Massinha de camarão e amêijoas", R.drawable.massinha_de_camarao_e_ameijoas);
        put("Peixe ao Sal", R.drawable.peixe_ao_sal);
        put("Strogonoff", R.drawable.strogonoff);
        put("Chili com carne", R.drawable.chili_com_carne);
        put("Tirinhas de porco salteadas", R.drawable.tirinhas_de_porco_salteadas);
        put("Saltear frango em tiras", R.drawable.saltear_frango_em_tiras);
        put("Penne de atum com manjericão", R.drawable.penne_de_atum_com_manjericao);
        put("Esparguete com atum e molho arrabiata", R.drawable.esparguete_com_atum_e_molho_arrabiata);
        put("Strogonoff de frango com castanhas", R.drawable.strogonoff_de_frango_com_castanhas);
        put("Massada de peixe", R.drawable.massada_de_peixe);
        put("Bolonhesa de carne", R.drawable.bolonhesa_de_carne);
        put("Bacalhau à Brás", R.drawable.bacalhau_a_bras);
        put("Nova receita", R.drawable.nova_receita);

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
                "Bacalhau à Assis",
                "Bacalhau à Brás",
                "Bifinhos Rápidos",
                "Bolonhesa de carne",
                "Caril de Frango com arroz",
                "Chili com carne",
                "Esparguete com atum e molho arrabiata",
                "Massada de peixe",
                "Massinha de camarão e amêijoas",
                "Penne com frango e rúcula",
                "Penne de atum com manjericão",
                "Peixe ao Sal",
                "Saltear frango em tiras",
                "Strogonoff",
                "Strogonoff de frango com castanhas",
                "Tirinhas de porco salteadas"
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
