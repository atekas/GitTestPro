package com.sensu.android.zimaogou.activity.mycenter;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import com.alibaba.fastjson.JSON;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.sensu.android.zimaogou.IConstants;
import com.sensu.android.zimaogou.R;
import com.sensu.android.zimaogou.ReqResponse.AuthCodeResponse;
import com.sensu.android.zimaogou.activity.BaseActivity;
import com.sensu.android.zimaogou.activity.login.ForgetPasswordSureActivity;
import com.sensu.android.zimaogou.encrypt.MD5Utils;
import com.sensu.android.zimaogou.external.greendao.helper.GDUserInfoHelper;
import com.sensu.android.zimaogou.external.greendao.model.UserInfo;
import com.sensu.android.zimaogou.handler.UpdateTimeHandler;
import com.sensu.android.zimaogou.utils.HttpUtil;
import com.sensu.android.zimaogou.utils.LogUtils;
import com.sensu.android.zimaogou.utils.PromptUtils;
import com.sensu.android.zimaogou.utils.TextUtils;
import org.apache.http.Header;
import org.json.JSONObject;

import java.security.NoSuchAlgorithmException;

/**
 * 绑定手机
 * Created by qi.yang on 2015/12/25.
 */
public class BindPhoneActivity extends BaseActivity {
    private TextView mGetAuthCode,mSubmit;
    private UpdateTimeHandler mUpdateTimeHandler;
    private EditText mPhoneEditText,mAuthCodeEditText,mPasswordEditText;
    UserInfo userInfo;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bindphone_activity);
        findViewById(R.id.back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        initView();
        userInfo = GDUserInfoHelper.getInstance(this).getUserInfo();
    }

   private void initView(){
       mGetAuthCode = (TextView) findViewById(R.id.get_auth_code);
       mPhoneEditText = (EditText) findViewById(R.id.input_phone);
       mAuthCodeEditText = (EditText) findViewById(R.id.input_authCode);
       mPasswordEditText = (EditText) findViewById(R.id.input_password);
       mSubmit = (TextView) findViewById(R.id.submit);
       mUpdateTimeHandler = new UpdateTimeHandler(this);
       mGetAuthCode.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
                mGetAuthCode.setClickable(false);
               mUpdateTimeHandler.sendEmptyMessage(UpdateTimeHandler.UPDATE_TIME_CODE);
               getCode();
           }
       });
       mSubmit.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               bindMobile();
           }
       });
   }


    /**
     * 获取验证码
     */
    private void getCode() {
        final String phoneNum = mPhoneEditText.getText().toString().trim();

        RequestParams requestParams = new RequestParams();
        requestParams.put("mobile", phoneNum);

        HttpUtil.post(IConstants.sGetBindPhoneCode, requestParams, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                LogUtils.d("获取验证码返回：",response.toString());
                if(response.optInt("ret")<0){
                    PromptUtils.showToast(response.optString("msg"));
                }else{
                    PromptUtils.showToast("验证码已发送，请注意查收");
                }

            }
        });

    }


    private void bindMobile(){
        final String phone = mPhoneEditText.getText().toString().trim();
        String recode = mAuthCodeEditText.getText().toString().trim();
        String password = mPasswordEditText.getText().toString().trim();
        if(TextUtils.isEmpty(phone)){
            PromptUtils.showToast("请输入手机号");
            return;
        }
        if(TextUtils.isEmpty(recode)){
            PromptUtils.showToast("请输入验证码");
            return;
        }
        if(TextUtils.isEmpty(password)){
            PromptUtils.showToast("请输入登录密码");
            return;
        }

        RequestParams requestParams = new RequestParams();
        requestParams.put("uid",userInfo.getUid());
        requestParams.put("id",userInfo.getUid());
        requestParams.put("mobile",phone);
        requestParams.put("recode",recode);
        try {
            requestParams.put("password", MD5Utils.md5(password));
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        HttpUtil.postWithSign(userInfo.getToken(),IConstants.sUpdateUserInfo+"/"+userInfo.getUid()+"/"+"bind_mobile",requestParams,new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                if(response.optInt("ret")<0){
                    PromptUtils.showToast(response.optString("msg"));
                }else{
                    PromptUtils.showToast("保存成功！");
                    userInfo.setMobile(phone);
                    GDUserInfoHelper.getInstance(BindPhoneActivity.this).updateUserInfo(userInfo);
                    finish();
                }
            }
        });
    }
}
