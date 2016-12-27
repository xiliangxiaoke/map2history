package com.haonight.www.map2history.app;

import android.app.Application;

import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.map.MapView;

/**
 * Created by SHANGLIKUN on 16/12/27.
 */

public class MyApplication extends Application {

    MapView mapView;

    @Override
    public void onCreate() {
        super.onCreate();

        //map init
        SDKInitializer.initialize(getApplicationContext());


    }
}
