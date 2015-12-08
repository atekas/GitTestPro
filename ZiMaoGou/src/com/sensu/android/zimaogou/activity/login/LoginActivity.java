package com.sensu.android.zimaogou.activity.login;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import com.alibaba.fastjson.JSON;
import com.loopj.android.http.AsyncHttpResponseHandler;
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
import org.json.JSONException;
import org.json.JSONObject;

import java.security.NoSuchAlgorithmException;

/**
 * Created by zhangwentao on 2015/11/13.
 */
public class LoginActivity extends BaseActivity implements View.OnClickListener {
    private EditText mMobileEditText,mPasswordEditText;
    String password;
    String mobile;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.login_activity);

        initViews();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(GDUserInfoHelper.getInstance(this).getUserInfo()!=null && GDUserInfoHelper.getInstance(this).getUserInfo().getIsLogin().equals("true")){//如果本地已存自己的信息则退出
            finish();
        }

    }

    private void initViews() {
        mMobileEditText = (EditText) findViewById(R.id.input_phone);
        mPasswordEditText = (EditText) findViewById(R.id.input_password);
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
                login();
                break;
        }
    }

    private void login() {
        password = mPasswordEditText.getText().toString();
        mobile = mMobileEditText.getText().toString();
        if (TextUtils.isEmpty(mobile)) {
            PromptUtils.showToast("手机号码不能为空");
            return;
        }
        if (TextUtils.isEmpty(password)) {
            PromptUtils.showToast("密码不能为空");
            return;
        }
        JSONObject requestParams = new JSONObject();

        try {
            requestParams.put("mobile", mobile);
            requestParams.put("password", MD5Utils.md5(password));
            HttpUtil.post(this, IConstants.sLogin, requestParams, new AsyncHttpResponseHandler() {
                @Override
                public void onSuccess(String content) {
                    super.onSuccess(content);
                    System.out.print(content);
                    UserInfoResponse userInfoResponse = JSON.parseObject(content, UserInfoResponse.class);
                    userInfoResponse.data.setIsLogin("true");
                    GDUserInfoHelper.getInstance(LoginActivity.this).insertUserInfo(userInfoResponse.data);
                    if (userInfoResponse.data != null) {
                        PromptUtils.showToast("登录成功！");
                        finish();
                    }
                }

                @Override
                public void onFailure(Throwable error, String content) {
                    super.onFailure(error, content);
                }
            });
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
    }
}
