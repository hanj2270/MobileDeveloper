package com.example.nagato.mobiledeveloper.global;

import android.app.Application;
import android.content.Context;
import android.os.Handler;

import io.realm.Realm;
import io.realm.RealmConfiguration;

/**
 * Created by nagato on 2016/9/9.
 */
public class MyApplacation extends Application {
    private static Context context;
    private static Handler handler;
    private static int mainThreadId;


    @Override
    public void onCreate() {
        super.onCreate();
        context = getApplicationContext();
        handler = new Handler();
        mainThreadId = android.os.Process.myTid();

        Realm.setDefaultConfiguration(new RealmConfiguration.Builder(this)
                .name("MobileDeveloper.realm")
                .deleteRealmIfMigrationNeeded()
                .build());
    }

    public static Context getContext() {
        return context;
    }

    public static Handler getHandler() {
        return handler;
    }

    public static int getMainThreadId() {
        return mainThreadId;
    }
}

//
