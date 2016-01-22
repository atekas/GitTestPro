package com.sensu.android.zimaogou.activity.mycenter;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.sensu.android.zimaogou.IConstants;
import com.sensu.android.zimaogou.R;
import com.sensu.android.zimaogou.activity.BaseActivity;
import com.sensu.android.zimaogou.external.greendao.helper.GDBaseHelper;
import com.sensu.android.zimaogou.external.greendao.helper.GDUserInfoHelper;
import com.sensu.android.zimaogou.external.greendao.model.UserInfo;
import com.sensu.android.zimaogou.handler.UpdateTimeHandler;
import com.sensu.android.zimaogou.utils.HttpUtil;
import com.sensu.android.zimaogou.utils.LogUtils;
import com.sensu.android.zimaogou.utils.PromptUtils;
import com.sensu.android.zimaogou.utils.TextUtils;
import org.apache.http.Header;
import org.json.JSONObject;
import org.w3c.dom.Text;

/**
 * Created by qi.yang on 2015/12/25.
 */
public class UpdateBindPhoneActivity extends BaseActivity {
    private TextView mPhoneTextView,mSubmitTextView;
    private TextView mGetAuthCode;
    private UpdateTimeHandler mUpdateTimeHandler;
    private EditText mPhoneEditText,mAuthCodeEditText;
    private UserInfo userInfo;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.update_bindphone_activity);
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
        mPhoneTextView = (TextView) findViewById(R.id.tv_phone);
        mPhoneTextView.setText(GDUserInfoHelper.getInstance(this).getUserInfo().getMobile());
        mGetAuthCode = (TextView) findViewById(R.id.get_auth_code);
        mPhoneEditText = (EditText) findViewById(R.id.input_phone);
        mAuthCodeEditText = (EditText) findViewById(R.id.input_authCode);
        mSubmitTextView = (TextView) findViewById(R.id.submit);
        mSubmitTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updatePhone();
            }
        });

        mUpdateTimeHandler = new UpdateTimeHandler(this);
        mGetAuthCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mGetAuthCode.setClickable(false);
                mUpdateTimeHandler.sendEmptyMessage(UpdateTimeHandler.UPDATE_TIME_CODE);
                getCode();
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
                LogUtils.d("获取验证码返回：", response.toString());
                if (response.optInt("ret") < 0) {
                    PromptUtils.showToast(response.optString("msg"));
                } else {
                    PromptUtils.showToast("验证码已发送，请注意查收");
                }

            }
        });

    }

    private void updatePhone(){
        final String phone = mPhoneEditText.getText().toString().trim();
        String authCode = mAuthCodeEditText.getText().toString().trim();
        if(TextUtils.isEmpty(phone)){
            PromptUtils.showToast("请输入手机号");
            return;
        }
        if(TextUtils.isEmpty(authCode)){
            PromptUtils.showToast("请输入验证码");
            return;
        }
        RequestParams requestParams = new RequestParams();
        requestParams.put("uid",userInfo.getUid());
        requestParams.put("mobile",phone);
        requestParams.put("recode",authCode);
        HttpUtil.postWithSign(userInfo.getToken(),IConstants.sUpdateUserInfo+"/"+userInfo.getUid()+"/update_mobile",requestParams,new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                if(response.optInt("ret")<0){
                    PromptUtils.showToast(response.optString("msg"));

                }else{
                    PromptUtils.showToast("保存成功！");
                    userInfo.setMobile(phone);
                    GDUserInfoHelper.getInstance(UpdateBindPhoneActivity.this).updateUserInfo(userInfo);
                    finish();
                }
            }
        });
    }
}
