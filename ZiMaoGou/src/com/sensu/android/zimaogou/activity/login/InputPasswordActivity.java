package com.sensu.android.zimaogou.activity.login;

import android.os.Bundle;
import android.view.View;
import com.sensu.android.zimaogou.R;
import com.sensu.android.zimaogou.activity.BaseActivity;

/**
 * Created by zhangwentao on 2015/11/13.
 */
public class InputPasswordActivity extends BaseActivity implements View.OnClickListener {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.input_password_activity);

        initViews();
    }

    private void initViews() {
        findViewById(R.id.finish).setOnClickListener(this);
        findViewById(R.id.register_ok).setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.finish:
                finish();
                break;
            case R.id.register_ok:

                break;
        }
    }
}
