package com.example.android.bakeryrecipes.ui;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.android.bakeryrecipes.R;
import com.example.android.bakeryrecipes.RecipesAdapter;
import com.example.android.bakeryrecipes.data.Recipe;
import com.example.android.bakeryrecipes.retrofit.IRecipe;
import com.example.android.bakeryrecipes.retrofit.RetrofitBuilder;
import com.example.android.bakeryrecipes.ui.utils.SpacesItemDecoration;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.example.android.bakeryrecipes.ui.MainActivity.ALL_RECIPES;

public class FragmentMain extends Fragment {

    public FragmentMain() {
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        RecyclerView recyclerView;

        View rootView = inflater.inflate(R.layout.fragment_main, container, false);

        recyclerView = rootView.findViewById(R.id.recycler_view);
        final RecipesAdapter recipesAdapter = new RecipesAdapter((MainActivity)getActivity());
        recyclerView.setAdapter(recipesAdapter);

        if (rootView.getTag() != null && rootView.getTag().equals("phone-land")) {
            StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
            recyclerView.setLayoutManager(layoutManager);
            int spacingInPixels = getResources().getDimensionPixelSize(R.dimen.spacing);
            recyclerView.addItemDecoration(new SpacesItemDecoration(spacingInPixels));
        }
        else {
            LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
            recyclerView.setLayoutManager(layoutManager);
        }

        IRecipe iRecipe = RetrofitBuilder.Retrieve();
        Call<ArrayList<Recipe>> recipe = iRecipe.getRecipe();

        SimpleIdlingResource idlingResource = (SimpleIdlingResource)((MainActivity)getActivity()).getIdlingResource();

        if (idlingResource != null) {
            idlingResource.setIdleState(true);
        }

        recipe.enqueue(new Callback<ArrayList<Recipe>>() {
            @Override
            public void onResponse(Call<ArrayList<Recipe>> call, Response<ArrayList<Recipe>> response) {
                Integer statusCode = response.code();
                Log.v("status code: ", statusCode.toString());

                ArrayList<Recipe> recipes = response.body();

                Bundle bundle = new Bundle();
                bundle.putParcelableArrayList(ALL_RECIPES, recipes);

                recipesAdapter.setRecipesData(recipes,getContext());
            }

            @Override
            public void onFailure(Call<ArrayList<Recipe>> call, Throwable t) {
                Log.v("http fail: ", t.getMessage());
            }
        });

        return rootView;
    }


}
