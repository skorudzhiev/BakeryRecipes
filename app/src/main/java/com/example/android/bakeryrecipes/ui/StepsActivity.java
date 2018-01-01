package com.example.android.bakeryrecipes.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.example.android.bakeryrecipes.R;
import com.example.android.bakeryrecipes.StepAdapter;
import com.example.android.bakeryrecipes.data.Recipe;
import com.example.android.bakeryrecipes.data.Step;

import java.util.ArrayList;
import java.util.List;

import static com.example.android.bakeryrecipes.ui.MainActivity.SELECTED_INDEX;
import static com.example.android.bakeryrecipes.ui.MainActivity.SELECTED_RECIPES;
import static com.example.android.bakeryrecipes.ui.MainActivity.SELECTED_STEPS;
import static com.example.android.bakeryrecipes.ui.MainActivity.STACK_RECIPE_DETAIL;
import static com.example.android.bakeryrecipes.ui.MainActivity.STACK_RECIPE_STEP_DETAIL;
import static com.example.android.bakeryrecipes.ui.MainActivity.TITLE;

public class StepsActivity extends AppCompatActivity implements StepAdapter.ItemClickListener,
        FragmentDetails.ListItemListener {

    private ArrayList<Recipe> recipe;
    String recipeName;
    boolean isTablet;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe);

        isTablet = getResources().getBoolean(R.bool.isTablet);

        if (savedInstanceState == null) {

            Bundle selectedRecipeBundle = getIntent().getExtras();

            recipe = new ArrayList<>();
            recipe = selectedRecipeBundle.getParcelableArrayList(SELECTED_RECIPES);
            recipeName = recipe.get(0).getName();

            final FragmentSteps fragmentSteps = new FragmentSteps();
            fragmentSteps.setArguments(selectedRecipeBundle);
            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager
                    .beginTransaction()
                    .replace(R.id.fragment_container, fragmentSteps)
                    .addToBackStack(STACK_RECIPE_DETAIL)
                    .commit();

            if (isTablet) {

                final FragmentDetails fragmentDetails = new FragmentDetails();
                fragmentDetails.setArguments(selectedRecipeBundle);
                fragmentManager
                        .beginTransaction()
                        .replace(R.id.fragment_container_2, fragmentDetails)
                        .addToBackStack(STACK_RECIPE_STEP_DETAIL)
                        .commit();
            }

        } else {
            recipeName = savedInstanceState.getString(TITLE);
        }

        Toolbar myToolbar = findViewById(R.id.toolbar);
        setSupportActionBar(myToolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(recipeName);

        myToolbar.setNavigationOnClickListener(v -> {
            FragmentManager fragmentManager = getSupportFragmentManager();
            if (findViewById(R.id.fragment_container_2) == null) {
                if (fragmentManager.getBackStackEntryCount() > 1) {
                    fragmentManager.popBackStack(STACK_RECIPE_DETAIL, 0);
                } else if (fragmentManager.getBackStackEntryCount() > 0) {
                    finish();
                }
            } else {
                finish();
            }
        });

    }

    @Override
    public void onListItemClick(List<Step> stepsOut, int clickedItemIndex, String recipeName) {

        final FragmentDetails fragmentDetails = new FragmentDetails();
        FragmentManager fragmentManager = getSupportFragmentManager();

        getSupportActionBar().setTitle(recipeName);

        Bundle bundle = new Bundle();
        bundle.putParcelableArrayList(SELECTED_STEPS, (ArrayList<Step>) stepsOut);
        bundle.putInt(SELECTED_INDEX, clickedItemIndex);
        bundle.putString("Title", recipeName);
        fragmentDetails.setArguments(bundle);

        if (isTablet) {
            fragmentManager
                    .beginTransaction()
                    .replace(R.id.fragment_container_2, fragmentDetails)
                    .addToBackStack(STACK_RECIPE_STEP_DETAIL)
                    .commit();

        } else {
            fragmentManager
                    .beginTransaction()
                    .replace(R.id.fragment_container, fragmentDetails)
                    .addToBackStack(STACK_RECIPE_STEP_DETAIL)
                    .commit();
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("Title", recipeName);
    }
}
