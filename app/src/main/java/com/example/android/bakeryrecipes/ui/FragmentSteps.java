package com.example.android.bakeryrecipes.ui;


import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.android.bakeryrecipes.R;
import com.example.android.bakeryrecipes.StepAdapter;
import com.example.android.bakeryrecipes.data.Ingredient;
import com.example.android.bakeryrecipes.data.Recipe;
import com.example.android.bakeryrecipes.ui.widget.UpdateService;

import java.util.ArrayList;
import java.util.List;

import static com.example.android.bakeryrecipes.ui.MainActivity.SELECTED_RECIPES;

public class FragmentSteps extends Fragment {

    ArrayList<Recipe> recipe;
    String recipeName;

    public FragmentSteps() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        RecyclerView recyclerView;
        TextView textView;

        recipe = new ArrayList<>();

        if (savedInstanceState != null) {
            recipe = savedInstanceState.getParcelableArrayList(SELECTED_RECIPES);
        } else {
            recipe = getArguments().getParcelableArrayList(SELECTED_RECIPES);
        }

        List<Ingredient> ingredients = recipe.get(0).getIngredients();
        recipeName = recipe.get(0).getName();

        View rootview = inflater.inflate(R.layout.fragment_steps, container, false);
        textView = rootview.findViewById(R.id.ingredients_view);

        ArrayList<String> ingredientsForWidget = new ArrayList<>();

        ingredients.forEach((a) ->
        {
            textView.append("\u2022 " + a.getIngredient()+"\n");
            textView.append("\t\t\t Quantity: " + a.getQuantity().toString() + "\n");
            textView.append("\t\t\t Measure: " + a.getMeasure() + "\n\n");

            ingredientsForWidget.add(a.getIngredient()+"\n"+
                    "Quantity: "+a.getQuantity().toString()+"\n"+
                    "Measure: "+a.getMeasure()+"\n");
        });

        recyclerView = rootview.findViewById(R.id.steps_recycler);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);

        StepAdapter stepAdapter = new StepAdapter((StepsActivity)getActivity());
        recyclerView.setAdapter(stepAdapter);
        stepAdapter.setStepsData(recipe,getContext());

        UpdateService.StartService(getContext(),ingredientsForWidget);

        return rootview;
    }

    @Override
    public void onSaveInstanceState(Bundle currentState) {
        super.onSaveInstanceState(currentState);
        currentState.putParcelableArrayList(SELECTED_RECIPES, recipe);
        currentState.putString("Title",recipeName);
    }
}
