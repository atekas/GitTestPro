package com.sensu.android.zimaogou.activity.login;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import com.sensu.android.zimaogou.R;
import com.sensu.android.zimaogou.activity.BaseActivity;

/**
 * Created by zhangwentao on 2015/11/13.
 */
public class LoginActivity extends BaseActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.login_activity);

        initViews();
    }

    private void initViews() {
        findViewById(R.id.finish).setOnClickListener(this);
        findViewById(R.id.register).setOnClickListener(this);
        findViewById(R.id.login).setOnClickListener(this);
        findViewById(R.id.forget_password).setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.finish:
                finish();
                break;
            case R.id.register:
                startActivity(new Intent(this, RegisterActivity.class));
                break;
            case R.id.forget_password:
                startActivity(new Intent(this, ForgetPasswordActivity.class));
                break;
            case R.id.login:
                //TODO 调用登陆接口
                break;
        }
    }
}
