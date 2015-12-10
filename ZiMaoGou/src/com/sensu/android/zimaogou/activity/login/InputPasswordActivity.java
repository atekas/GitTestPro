package com.sensu.android.zimaogou.activity.login;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import com.alibaba.fastjson.JSON;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.sensu.android.zimaogou.IConstants;
import com.sensu.android.zimaogou.R;
import com.sensu.android.zimaogou.ReqResponse.UserInfoResponse;
import com.sensu.android.zimaogou.activity.BaseActivity;
import com.sensu.android.zimaogou.encrypt.MD5Utils;
import com.sensu.android.zimaogou.external.greendao.helper.GDUserInfoHelper;
import com.sensu.android.zimaogou.utils.HttpUtil;
import com.sensu.android.zimaogou.utils.PromptUtils;
import com.sensu.android.zimaogou.utils.TextUtils;
import org.apache.http.Header;
import org.json.JSONObject;

import java.security.NoSuchAlgorithmException;

/**
 * Created by zhangwentao on 2015/11/13.
 */
public class InputPasswordActivity extends BaseActivity implements View.OnClickListener {
    EditText mPasswordEditText;
    String recode = "";
    String mobile = "";
    String password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.input_password_activity);
        if (getIntent().getExtras() != null) {
            recode = getIntent().getExtras().getString("recode");
            mobile = getIntent().getExtras().getString("mobile");
        }
        initViews();
    }

    private void initViews() {
        findViewById(R.id.finish).setOnClickListener(this);
        findViewById(R.id.register_ok).setOnClickListener(this);
        mPasswordEditText = (EditText) findViewById(R.id.input_password);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.finish:
                finish();
                break;
            case R.id.register_ok:
                register();
                break;
        }
    }

    private void register() {
        password = mPasswordEditText.getText().toString();
        if (TextUtils.isEmpty(password)) {
            PromptUtils.showToast("密码不能为空");
            return;
        }
        RequestParams requestParams = new RequestParams();

        try {
            requestParams.put("recode", recode);
            requestParams.put("mobile", mobile);
            requestParams.put("password", MD5Utils.md5(password));
            HttpUtil.post(IConstants.sRegister, requestParams, new JsonHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                    super.onSuccess(statusCode, headers, response);
                    UserInfoResponse userInfoResponse = JSON.parseObject(response.toString(), UserInfoResponse.class);
                    userInfoResponse.data.setIsLogin("true");
                    GDUserInfoHelper.getInstance(InputPasswordActivity.this).updateUserInfo(userInfoResponse.data);
                    if (userInfoResponse.data != null) {
                        PromptUtils.showToast("注册成功！");
                        finish();
                    }
                }
            });
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
    }
}
