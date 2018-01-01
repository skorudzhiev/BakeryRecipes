package com.example.android.bakeryrecipes.ui.widget;

import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.example.android.bakeryrecipes.R;

import java.util.List;

import static com.example.android.bakeryrecipes.ui.widget.WidgetProvider.ingredientList;

public class WidgetService extends RemoteViewsService {

    List<String> remoteIngredientsList;

    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        return new RemoteViewsFactoryClass(this.getApplicationContext(), intent);
    }

    class RemoteViewsFactoryClass implements RemoteViewsService.RemoteViewsFactory {

        Context context = null;

        public RemoteViewsFactoryClass(Context context, Intent intent) {
            this.context = context;
        }

        @Override
        public void onCreate() {

        }

        @Override
        public void onDataSetChanged() {
            remoteIngredientsList = ingredientList;
        }

        @Override
        public void onDestroy() {

        }

        @Override
        public int getCount() {
            return remoteIngredientsList.size();
        }

        @Override
        public RemoteViews getViewAt(int position) {

            RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.widget_item);

            views.setTextViewText(R.id.widget_item, remoteIngredientsList.get(position));

            Intent intent = new Intent();
            views.setOnClickFillInIntent(R.id.widget_item, intent);

            return views;
        }

        @Override
        public RemoteViews getLoadingView() {
            return null;
        }

        @Override
        public int getViewTypeCount() {
            return 1;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public boolean hasStableIds() {
            return true;
        }
    }
}
