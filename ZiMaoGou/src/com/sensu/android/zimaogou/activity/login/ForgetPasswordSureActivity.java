package com.sensu.android.zimaogou.activity.login;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import com.alibaba.fastjson.JSON;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.sensu.android.zimaogou.IConstants;
import com.sensu.android.zimaogou.R;
import com.sensu.android.zimaogou.ReqResponse.AuthCodeResponse;
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
public class ForgetPasswordSureActivity extends BaseActivity implements View.OnClickListener{
    EditText mCodeEditText,mPasswordEditText,mPasswordSureEditText;
    String phoneNum = "";
    private TextView mGetAuthCode;
    private UpdateTimeHandler mUpdateTimeHandler;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.forget_password_sure_activity);
        if(getIntent().getExtras() != null){
            phoneNum = getIntent().getExtras().getString("phone");
        }

        initView();
    }

    private void initView(){
        mCodeEditText = (EditText) findViewById(R.id.input_authCode);
        mPasswordEditText = (EditText) findViewById(R.id.input_password);
        mPasswordSureEditText = (EditText) findViewById(R.id.input_password_sure);
        mGetAuthCode = (TextView) findViewById(R.id.get_auth_code);
        mGetAuthCode.setEnabled(false);
        mUpdateTimeHandler.sendEmptyMessage(UpdateTimeHandler.UPDATE_TIME_CODE);
        findViewById(R.id.next).setOnClickListener(this);
        mGetAuthCode.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.back:
                onBackPressed();
                break;
            case R.id.next:
                if(TextUtils.isEmpty(mCodeEditText.getText().toString())){
                    PromptUtils.showToast("验证码不能为空");
                    return;
                }
                if(TextUtils.isEmpty(mPasswordEditText.getText().toString())){
                    PromptUtils.showToast("密码不能为空");
                    return;
                }
                if(TextUtils.isEmpty(mPasswordSureEditText.getText().toString())){
                    PromptUtils.showToast("请再次输入密码");
                    return;
                }
                if(!mPasswordEditText.getText().toString().equals(mPasswordSureEditText.getText().toString())){
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


    private void passwordModify(){

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("mobile", phoneNum);
            jsonObject.put("recode", mCodeEditText.getText().toString());
            jsonObject.put("password", mPasswordSureEditText.getText().toString());
            HttpUtil.post(this, IConstants.sGetForgetPassCode, jsonObject, new AsyncHttpResponseHandler() {
                @Override
                public void onSuccess(String content) {
                    super.onSuccess(content);
                    try {
                        JSONObject jsonObject1 = new JSONObject(content);
                        if(jsonObject1.optString("ret").equals("0")){
                            PromptUtils.showToast("重设密码成功");
                            finish();
                        }else{
                            PromptUtils.showToast(jsonObject1.optString("msg"));
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
            });

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void getCode(){

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("mobile", phoneNum);

            HttpUtil.post(this, IConstants.sGetForgetPassCode, jsonObject, new AsyncHttpResponseHandler() {
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
}
