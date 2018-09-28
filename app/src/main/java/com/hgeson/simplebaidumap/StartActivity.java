package com.hgeson.simplebaidumap;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import com.hgeson.simplebaidumap.utils.PermissionUtils;

/**
 * @Describe：
 * @Date：2018/9/28
 * @Author：hgeson
 */

public class StartActivity extends Activity implements View.OnClickListener {
    private TextView btnLocation;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN); //隐藏状态栏
        super.onCreate(savedInstanceState);
        if (getActionBar() != null){
            getActionBar().hide();
        }
        setContentView(R.layout.activity_start);
        btnLocation = (TextView) findViewById(R.id.btn_location);
        btnLocation.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (PermissionUtils.initPermissions(this
                , new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION,}
                , new String[]{"定位","获取位置"})) {
            startActivity(new Intent(this,MainActivity.class));
        }
    }
}
