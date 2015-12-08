package com.sensu.android.zimaogou.activity.login;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import com.alibaba.fastjson.JSON;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.sensu.android.zimaogou.IConstants;
import com.sensu.android.zimaogou.R;
import com.sensu.android.zimaogou.ReqResponse.AuthCodeResponse;
import com.sensu.android.zimaogou.ReqResponse.CheckCodeResponse;
import com.sensu.android.zimaogou.activity.BaseActivity;
import com.sensu.android.zimaogou.handler.UpdateTimeHandler;
import com.sensu.android.zimaogou.utils.HttpUtil;
import com.sensu.android.zimaogou.utils.PromptUtils;
import com.sensu.android.zimaogou.utils.TextUtils;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by zhangwentao on 2015/11/13.
 */
public class RegisterActivity extends BaseActivity implements View.OnClickListener {

    private TextView mGetAuthCode;
    private UpdateTimeHandler mUpdateTimeHandler;
    private EditText mPhoneEditText,mAuthCodeEditText;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.register_activity);

        initViews();
    }

    public void initViews() {
        mGetAuthCode = (TextView) findViewById(R.id.get_auth_code);
        mGetAuthCode.setEnabled(true);
        mPhoneEditText = (EditText) findViewById(R.id.input_phone);
        mAuthCodeEditText = (EditText) findViewById(R.id.input_authCode);
        mUpdateTimeHandler = new UpdateTimeHandler(this);

        findViewById(R.id.finish).setOnClickListener(this);
        findViewById(R.id.next).setOnClickListener(this);
        mGetAuthCode.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.finish:
                finish();
                break;
            case R.id.next:
                checkCode();
                break;
            case R.id.get_auth_code:
                //TODO 调用获取验证码接口，成功后下面两方法在返回成功里面调用
                getCode();
                mGetAuthCode.setEnabled(false);
                mUpdateTimeHandler.sendEmptyMessage(UpdateTimeHandler.UPDATE_TIME_CODE);

                break;
        }
    }


    private void getCode(){
        String phoneNum = mPhoneEditText.getText().toString().trim();
        if(TextUtils.isEmpty(phoneNum)){
            PromptUtils.showToast("手机号码不能为空");
            return ;
        }
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("mobile", phoneNum);

            HttpUtil.post(this, IConstants.sGetAuthCode, jsonObject, new AsyncHttpResponseHandler() {
                @Override
                public void onSuccess(String content) {
                    super.onSuccess(content);
                    AuthCodeResponse authCodeResponse = JSON.parseObject(content, AuthCodeResponse.class);
                    PromptUtils.showToast(authCodeResponse.data.recode);
                }
            });

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void checkCode() {
        if(TextUtils.isEmpty(mPhoneEditText.getText().toString())){
            PromptUtils.showToast("手机号码不能为空");
            return ;
        }
        if(TextUtils.isEmpty(mAuthCodeEditText.getText().toString())){
            PromptUtils.showToast("验证码不能为空");
            return ;
        }
        RequestParams requestParams = new RequestParams();
        requestParams.put("mobile", mPhoneEditText.getText().toString());
        requestParams.put("recode", mAuthCodeEditText.getText().toString());
        HttpUtil.get(IConstants.sCheck, requestParams, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(String content) {
                super.onSuccess(content);
                CheckCodeResponse checkCodeResponse = JSON.parseObject(content, CheckCodeResponse.class);
                PromptUtils.showToast(checkCodeResponse.getMsg() + checkCodeResponse.data.is_pass);
                if((checkCodeResponse.data.is_pass).equals("1")){
                    startActivity(new Intent(RegisterActivity.this,InputPasswordActivity.class));
                }else{
                    PromptUtils.showToast("验证码不正确");
                }
            }

            @Override
            public void onFailure(Throwable error, String content) {
                super.onFailure(error, content);
            }
        });
    }
}
