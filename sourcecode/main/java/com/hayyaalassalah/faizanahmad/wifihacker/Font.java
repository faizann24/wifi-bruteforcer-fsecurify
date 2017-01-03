package com.hayyaalassalah.faizanahmad.wifihacker;

import android.app.Application;

import uk.co.chrisjenx.calligraphy.CalligraphyConfig;

/**
 * Created by Faizan Ahmad on 1/1/2017.
 */
public class Font extends Application {
    @Override
    public void onCreate() {
        super.onCreate();

        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                        .setDefaultFontPath("Lato-Light.ttf")
                        .setFontAttrId(R.attr.fontPath)
                        .build()
        );
    }
}