package com.sensu.android.zimaogou.activity.login;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import com.alibaba.fastjson.JSON;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.sensu.android.zimaogou.IConstants;
import com.sensu.android.zimaogou.R;
import com.sensu.android.zimaogou.ReqResponse.AuthCodeResponse;
import com.sensu.android.zimaogou.activity.BaseActivity;
import com.sensu.android.zimaogou.utils.HttpUtil;
import com.sensu.android.zimaogou.utils.PromptUtils;
import com.sensu.android.zimaogou.utils.TextUtils;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by zhangwentao on 2015/11/13.
 */
public class ForgetPasswordActivity extends BaseActivity implements View.OnClickListener{
    EditText mPhoneEditText;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.forget_password_activity);
        initView();
    }

    private void initView(){
        mPhoneEditText = (EditText) findViewById(R.id.input_phone);
        findViewById(R.id.next).setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.back:
                onBackPressed();
                break;
            case R.id.next:
                if(TextUtils.isEmpty(mPhoneEditText.getText().toString())){
                    PromptUtils.showToast("手机号码不能为空");
                    return;
                }
                getCode();
                break;
        }
    }


    private void getCode(){
        final String phoneNum = mPhoneEditText.getText().toString().trim();

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("mobile", phoneNum);

            HttpUtil.post(this, IConstants.sGetForgetPassCode, jsonObject, new AsyncHttpResponseHandler() {
                @Override
                public void onSuccess(String content) {
                    super.onSuccess(content);
                    AuthCodeResponse authCodeResponse = JSON.parseObject(content, AuthCodeResponse.class);
                    PromptUtils.showToast(authCodeResponse.data.recode);
                    startActivity(new Intent(ForgetPasswordActivity.this,ForgetPasswordSureActivity.class).putExtra("phone",phoneNum));
                    finish();
                }
            });

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
