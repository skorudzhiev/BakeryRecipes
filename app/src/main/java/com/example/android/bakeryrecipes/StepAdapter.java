package com.example.android.bakeryrecipes;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.android.bakeryrecipes.data.Recipe;
import com.example.android.bakeryrecipes.data.Step;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class StepAdapter extends RecyclerView.Adapter<StepAdapter.StepViewHolder> {

    List<Step> steps;
    private String recipeName;
    final private ItemClickListener onClickListener;

    public interface ItemClickListener {
        void onListItemClick(List<Step> stepsOut, int clickedItemIndex, String recipeName);
    }

    public StepAdapter(ItemClickListener onClickListener) {
        this.onClickListener = onClickListener;
    }

    public void setStepsData(List<Recipe> recipes, Context context) {
        steps = recipes.get(0).getSteps();
        recipeName = recipes.get(0).getName();
        notifyDataSetChanged();
    }

    class StepViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        @BindView(R.id.step) LinearLayout step;
        @BindView(R.id.step_short_description) TextView shortDescription;
        @BindView(R.id.connector_top) View connectorTop;
        @BindView(R.id.connector_bottom) View connectorBottom;

        StepViewHolder (View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);

            shortDescription.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int clickedPosition = getAdapterPosition();
            onClickListener.onListItemClick(steps, clickedPosition, recipeName);
        }
    }

    @Override
    public StepViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.steps_layout, parent, false);

        return new StepAdapter.StepViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(StepViewHolder holder, int position) {
        Step item = steps.get(position);
        if (position == 0) {
            holder.connectorTop.setVisibility(View.VISIBLE);
            holder.connectorBottom.setVisibility(View.VISIBLE);
        } else if (position < getItemCount() - 1) {
            holder.connectorTop.setVisibility(View.VISIBLE);
            holder.connectorBottom.setVisibility(View.VISIBLE);
        } else {
            holder.connectorTop.setVisibility(View.VISIBLE);
            holder.connectorBottom.setVisibility(View.VISIBLE);
        }
        holder.shortDescription.setText(steps.get(position).getId()
                + ". " + item.getShortDescription());
    }

    @Override
    public int getItemCount() {
        return steps != null ? steps.size():0;
    }
}
