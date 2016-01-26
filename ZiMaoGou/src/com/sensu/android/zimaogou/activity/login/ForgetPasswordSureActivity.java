package com.sensu.android.zimaogou.activity.login;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import com.alibaba.fastjson.JSON;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.sensu.android.zimaogou.IConstants;
import com.sensu.android.zimaogou.R;
import com.sensu.android.zimaogou.ReqResponse.AuthCodeResponse;
import com.sensu.android.zimaogou.activity.BaseActivity;
import com.sensu.android.zimaogou.encrypt.MD5Utils;
import com.sensu.android.zimaogou.handler.UpdateTimeHandler;
import com.sensu.android.zimaogou.utils.HttpUtil;
import com.sensu.android.zimaogou.utils.PromptUtils;
import com.sensu.android.zimaogou.utils.TextUtils;
import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import java.security.NoSuchAlgorithmException;

/**
 * Created by zhangwentao on 2015/11/13.
 */
public class ForgetPasswordSureActivity extends BaseActivity implements View.OnClickListener {
    EditText mCodeEditText, mPasswordEditText, mPasswordSureEditText;
    String phoneNum = "";
    private TextView mGetAuthCode;
    private UpdateTimeHandler mUpdateTimeHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.forget_password_sure_activity);
        if (getIntent().getExtras() != null) {
            phoneNum = getIntent().getExtras().getString("phone");
        }

        initView();
    }

    private void initView() {
        mCodeEditText = (EditText) findViewById(R.id.input_authCode);
        mPasswordEditText = (EditText) findViewById(R.id.input_password);
        mPasswordSureEditText = (EditText) findViewById(R.id.input_password_sure);
        mGetAuthCode = (TextView) findViewById(R.id.get_auth_code);
        mGetAuthCode.setEnabled(false);

        mUpdateTimeHandler = new UpdateTimeHandler(ForgetPasswordSureActivity.this);
        mUpdateTimeHandler.sendEmptyMessage(UpdateTimeHandler.UPDATE_TIME_CODE);
        findViewById(R.id.next).setOnClickListener(this);
        mGetAuthCode.setOnClickListener(this);
        findViewById(R.id.back).setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.back:
                onBackPressed();
                break;
            case R.id.next:
                if (TextUtils.isEmpty(mCodeEditText.getText().toString())) {
                    PromptUtils.showToast("验证码不能为空");
                    return;
                }
                if (TextUtils.isEmpty(mPasswordEditText.getText().toString())) {
                    PromptUtils.showToast("密码不能为空");
                    return;
                }
                if (TextUtils.isEmpty(mPasswordSureEditText.getText().toString())) {
                    PromptUtils.showToast("请再次输入密码");
                    return;
                }
                if (!mPasswordEditText.getText().toString().equals(mPasswordSureEditText.getText().toString())) {
                    PromptUtils.showToast("两次输入的密码不一致");
                    return;
                }
                passwordModify();
                break;
            case R.id.get_auth_code:
                getCode();
                break;
        }
    }


    private void passwordModify() {

        RequestParams jsonObject = new RequestParams();
        try {
            jsonObject.put("mobile", phoneNum);
            jsonObject.put("recode", mCodeEditText.getText().toString());
            jsonObject.put("password", MD5Utils.md5(mPasswordSureEditText.getText().toString()));
            HttpUtil.post(IConstants.sForgetPass, jsonObject, new JsonHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                    super.onSuccess(statusCode, headers, response);
                    Log.d("忘记密码返回值：", response.toString());
                    if (response.optString("ret").equals("0")) {
                        PromptUtils.showToast("重设密码成功");
                        finish();
                    } else {
                        PromptUtils.showToast(response.optString("msg"));
                    }
                }
            });
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
    }

    private void getCode() {

        RequestParams requestParams = new RequestParams();
        requestParams.put("mobile", phoneNum);

        HttpUtil.post(IConstants.sGetForgetPassCode, requestParams, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                AuthCodeResponse authCodeResponse = JSON.parseObject(response.toString(), AuthCodeResponse.class);
            }
        });
    }
}
