package com.example.wardrobe;

import android.app.Application;
import android.content.Context;

public class WardrobeApplication extends Application {
    public static Context context;

    @Override
    public void onCreate() {
        super.onCreate();
        context = getApplicationContext();
    }
}
