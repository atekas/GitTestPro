package com.sensu.android.zimaogou.activity.mycenter;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import com.sensu.android.zimaogou.R;
import com.sensu.android.zimaogou.activity.BaseActivity;

/**
 * Created by qi.yang on 2015/12/1.
 */
public class UpdatePasswordActivity extends BaseActivity {
    private ImageView mBackImageView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.update_password_activity);
        initView();
    }

    private void initView(){
        mBackImageView = (ImageView) findViewById(R.id.back);
        mBackImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }
}
