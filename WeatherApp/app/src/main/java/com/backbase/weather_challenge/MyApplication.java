package com.backbase.weather_challenge;

import android.app.Application;
import android.content.Context;
import android.support.multidex.MultiDex;

/**
 * Created by JGomez on 6/1/17.
 */

public class MyApplication extends Application {
    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }
}
