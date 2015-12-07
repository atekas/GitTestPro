package com.sensu.android.zimaogou.activity.login;

import android.os.Bundle;
import android.view.View;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.sensu.android.zimaogou.IConstants;
import com.sensu.android.zimaogou.R;
import com.sensu.android.zimaogou.activity.BaseActivity;
import com.sensu.android.zimaogou.utils.HttpUtil;

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

    private void register() {
        RequestParams requestParams = new RequestParams();
        requestParams.put("mobile", "15229024392");
        requestParams.put("recode", "123456");
        requestParams.put("password", "88888888");
        HttpUtil.post(IConstants.sRegister, requestParams, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(String content) {
                super.onSuccess(content);
            }

            @Override
            public void onFailure(Throwable error, String content) {
                super.onFailure(error, content);
            }
        });
    }
}
