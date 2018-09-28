package com.hgeson.simplebaidumap;

import android.app.Application;

import com.baidu.mapapi.SDKInitializer;

/**
 * @Describe：
 * @Date：2018/9/28
 * @Author：hgeson
 */

public class BaseApplaction extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        SDKInitializer.initialize(getApplicationContext());
    }
}
