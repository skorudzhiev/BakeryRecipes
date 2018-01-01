package com.example.android.bakeryrecipes.ui.widget;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;

import java.util.ArrayList;
import static com.example.android.bakeryrecipes.ui.MainActivity.EXTRA_INGREDIENTS_LIST;

public class UpdateService extends IntentService {

    public UpdateService() {super("UpdateService");}

    public static void StartService (Context context, ArrayList<String> ingredientsList) {
        Intent intent = new Intent(context, UpdateService.class);
        intent.putExtra(EXTRA_INGREDIENTS_LIST, ingredientsList);
        context.startService(intent);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
            ArrayList<String> ingredientsList = intent.getExtras().getStringArrayList(EXTRA_INGREDIENTS_LIST);
            handleActionUpdate(ingredientsList);
        }
    }

    private void handleActionUpdate (ArrayList<String> ingredientsList) {
        Intent intent = new Intent("android.appwidget.action.APPWIDGET_UPDATE2");
        intent.setAction("android.appwidget.action.APPWIDGET_UPDATE2");
        intent.putExtra(EXTRA_INGREDIENTS_LIST, ingredientsList);
        sendBroadcast(intent);
    }
}
