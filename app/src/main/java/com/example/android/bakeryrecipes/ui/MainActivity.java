package com.example.android.bakeryrecipes.ui;

import android.content.Intent;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.VisibleForTesting;
import android.support.design.widget.Snackbar;
import android.support.test.espresso.IdlingResource;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.example.android.bakeryrecipes.R;
import com.example.android.bakeryrecipes.RecipesAdapter;
import com.example.android.bakeryrecipes.data.Recipe;
import com.example.android.bakeryrecipes.ui.utils.ConnectivityReceiver;
import com.example.android.bakeryrecipes.ui.utils.MyApplication;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements RecipesAdapter.ItemClickListener,
        ConnectivityReceiver.ConnectivityReceiverListener {

    public static final String ALL_RECIPES = "All_Recipes";
    public static final String SELECTED_RECIPES = "Selected_Recipes";
    public static final String STACK_RECIPE_DETAIL = "Stack_Recipe_Detail";
    public static final String STACK_RECIPE_STEP_DETAIL = "Stack_Recipe_Step_Detail";
    public static final String SELECTED_STEPS = "Selected_Steps";
    public static final String SELECTED_INDEX = "Selected_Index";
    public static final String EXTRA_PLAYER_POSITION = "Extra_Player_Position";
    public static final String EXTRA_INGREDIENTS_LIST = "Extra_Ingredient_List";
    public static final String TITLE = "Title";

    @Nullable
    private SimpleIdlingResource idlingResource;

    @VisibleForTesting
    @NonNull
    public IdlingResource getIdlingResource() {
        if (idlingResource == null) {
            idlingResource = new SimpleIdlingResource();
        }
        return idlingResource;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        getSupportActionBar().setTitle(R.string.app_name);

        getIdlingResource();

        checkConnection();
    }

    // Method to manually check connection status
    private void checkConnection() {
        boolean isConnected = ConnectivityReceiver.isConnected();
        showSnack(isConnected);
    }

    // Showing the status in Snackbar
    private void showSnack(boolean isConnected) {
        String message;
        int color;
        if (isConnected) {
            message = "Good! Connected to Internet";
            color = Color.WHITE;
        } else {
            message = "Sorry! Not connected to internet";
            color = Color.RED;
        }

        Snackbar snackbar = Snackbar
                .make(findViewById(R.id.fab), message, Snackbar.LENGTH_LONG);

        View sbView = snackbar.getView();
        TextView textView = sbView.findViewById(android.support.design.R.id.snackbar_text);
        textView.setTextColor(color);
        snackbar.show();
    }

    @Override
    protected void onResume() {
        super.onResume();

        MyApplication.getInstance().setConnectivityListener(this);
    }

    /**
     * Callback will be triggered when there is change in
     * network connection
     */
    @Override
    public void onNetworkConnectionChanged(boolean isConnected) {
        showSnack(isConnected);
    }

    @Override
    public void onItemClick(Recipe selectedItemIndex) {
        Bundle selectedRecipeBundle = new Bundle();
        ArrayList<Recipe> selectedRecipe = new ArrayList<>();
        selectedRecipe.add(selectedItemIndex);
        selectedRecipeBundle.putParcelableArrayList(SELECTED_RECIPES, selectedRecipe);

        final Intent intent = new Intent(this, StepsActivity.class);
        intent.putExtras(selectedRecipeBundle);
        startActivity(intent);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }
}
