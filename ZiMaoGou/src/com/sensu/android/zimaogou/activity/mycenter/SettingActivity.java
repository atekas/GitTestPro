package com.sensu.android.zimaogou.activity.mycenter;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import com.sensu.android.zimaogou.R;
import com.sensu.android.zimaogou.activity.BaseActivity;

/**
 * Created by qi.yang on 2015/11/26.
 */
public class SettingActivity extends BaseActivity {
    ImageView mBackImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.setting_activity);
        mBackImageView = (ImageView) findViewById(R.id.back);
        mBackImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }
    /**
     *
     * 修改密码
     *
     */
    public void UpdatePassClick(View v){

    }
    /**
     *
     * 清除缓存
     *
     */
    public void CacheClick(View v){

    }
    /**
     *
     * 用户协议
     *
     */
    public void ServiceClick(View v){

    }
    /**
     *
     * 用户去评分
     *
     */
    public void RatingClick(View v){

    }
    /**
     *
     * 关于我们
     *
     */
    public void AboutUsClick(View v){

    }
    /**
     *
     * 退出账号
     *
     */
    public void LoginOutClick(View v){

    }

}
