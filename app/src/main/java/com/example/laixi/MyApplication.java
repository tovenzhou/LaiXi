package com.example.laixi;

import android.app.Application;
import android.content.Context;
import android.content.Intent;

public class MyApplication extends Application {
    private static Context mContext;

    @Override
    public void onCreate() {
        super.onCreate();
        startService(new Intent(this, ProgressService.class));
        mContext = getApplicationContext();
    }

    public static Context getInstance() {
        return mContext;
    }
}