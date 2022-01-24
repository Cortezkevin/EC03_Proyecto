package com.example.proyectoec03.Application;

import android.app.Application;
import android.content.Context;

import androidx.appcompat.app.AppCompatDelegate;

public class ProyectoEC03Application extends Application {

    private static ProyectoEC03Application instance;
    private static Context appContext;

    public static ProyectoEC03Application getInstance(){
        return instance;
    }

    public static Context getAppContext(){
       return appContext;
    }

    public void setAppContext(Context mAppContext){
        this.appContext = mAppContext;
    }

    @Override
    public void onCreate(){
        super.onCreate();

        instance = this;

        this.setAppContext(getApplicationContext());
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
    }
}
