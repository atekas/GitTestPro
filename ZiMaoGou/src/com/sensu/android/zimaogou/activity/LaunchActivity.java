package com.sensu.android.zimaogou.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.WindowManager;
import com.sensu.android.zimaogou.R;

/**
 * Created by zhangwentao on 2016/1/11.
 */
public class LaunchActivity extends BaseActivity {

    public static final int GO_HOME = 100001;
    public static final int GO_GUIDE = 100002;

    private String mName = "zimaogou_preferences";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.launch_activity);

        if (isFirst()) {
            mHandler.sendEmptyMessageDelayed(GO_GUIDE, 2000);
        } else {
            mHandler.sendEmptyMessageDelayed(GO_HOME, 2000);
        }
    }

    private boolean isFirst() {
        SharedPreferences preferences = getSharedPreferences(mName, Activity.MODE_PRIVATE);
        String isFirst = preferences.getString("isFirst", "true");
        if (isFirst.equals("true")) {
            return true;
        } else {
            return false;
        }
    }

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case GO_GUIDE:
                    //进入引导页
                    Intent intent = new Intent(LaunchActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                    break;
                case GO_HOME:
                    Intent intent1 = new Intent(LaunchActivity.this, MainActivity.class);
                    startActivity(intent1);
                    finish();
                    break;
            }
        }
    };
}
