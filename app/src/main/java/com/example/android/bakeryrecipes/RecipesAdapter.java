package com.example.android.bakeryrecipes;

import android.content.Context;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.android.bakeryrecipes.data.Recipe;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RecipesAdapter extends RecyclerView.Adapter<RecipesAdapter.RecipeAdapterViewHolder> {

    ArrayList<Recipe> recipes = new ArrayList<>();
    Context context;
    final private ItemClickListener onClickListener;

    public interface ItemClickListener {
        void onNetworkConnectionChanged(boolean isConnected);

        void onItemClick(Recipe clickedItemIndex);
    }

    public RecipesAdapter(ItemClickListener listener) {
        onClickListener = listener;
    }

    public void setRecipesData(ArrayList<Recipe> recipesData, Context conText) {
        recipes = recipesData;
        context = conText;
        notifyDataSetChanged();
    }

    public class RecipeAdapterViewHolder extends RecyclerView.ViewHolder
            implements View.OnClickListener {

        @BindView(R.id.name) TextView recipeName;
        @BindView(R.id.servings) TextView servings;
        @BindView(R.id.image) ImageView imageView;

        public RecipeAdapterViewHolder(View itemView){
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            int clickedPosition = getAdapterPosition();
            onClickListener.onItemClick(recipes.get(clickedPosition));
        }
    }

    @Override
    public RecipeAdapterViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        Context context = viewGroup.getContext();
        int layout = R.layout.recipe_layout;

        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(layout, viewGroup, false);
        RecipeAdapterViewHolder viewHolder = new RecipeAdapterViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecipeAdapterViewHolder holder, int position) {
        holder.recipeName.setText(recipes.get(position).getName());
        holder.servings.setText(String.valueOf(recipes.get(position).getServings()));

        String imageUrl = recipes.get(position).getImage();
        if (imageUrl !=""){
            Uri builtUri = Uri.parse(imageUrl).buildUpon().build();
            Picasso
                    .with(context)
                    .load(builtUri)
                    .into(holder.imageView);
        }
    }

    @Override
    public int getItemCount() {
        return recipes.size();
    }
}
