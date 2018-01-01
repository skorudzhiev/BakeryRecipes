package com.example.android.bakeryrecipes.retrofit;

import com.example.android.bakeryrecipes.data.Recipe;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.http.GET;

public interface IRecipe {

    @GET("baking.json")
    Call<ArrayList<Recipe>> getRecipe();
}
