package com.sensu.android.zimaogou.activity.login;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
import com.alibaba.fastjson.JSON;
import com.loopj.android.http.AsyncHttpResponseHandler;
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
import com.umeng.socialize.UMAuthListener;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.bean.SHARE_MEDIA;
import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import java.security.NoSuchAlgorithmException;
import java.util.Map;

/**
 * Created by zhangwentao on 2015/11/13.
 */
public class LoginActivity extends BaseActivity implements View.OnClickListener {
    private EditText mMobileEditText,mPasswordEditText;
    String password;
    String mobile;
    private UMShareAPI mShareAPI = null;
    SHARE_MEDIA platform = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.login_activity);
        mShareAPI = UMShareAPI.get( this );
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
        findViewById(R.id.bt_wx).setOnClickListener(this);
        findViewById(R.id.bt_qq).setOnClickListener(this);
        findViewById(R.id.bt_wb).setOnClickListener(this);
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
            case R.id.bt_wx:
                platform = SHARE_MEDIA.WEIXIN;
                mShareAPI.doOauthVerify(this, platform, umAuthListener);
                break;
            case R.id.bt_qq:
                platform = SHARE_MEDIA.QQ;
                mShareAPI.doOauthVerify(this, platform, umAuthListener);
                break;
            case R.id.bt_wb:
                platform = SHARE_MEDIA.SINA;
                mShareAPI.doOauthVerify(this, platform, umAuthListener);
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
        RequestParams requestParams = new RequestParams();

        try {
            requestParams.put("mobile", mobile);
            requestParams.put("password", MD5Utils.md5(password));
            HttpUtil.post(IConstants.sLogin, requestParams, new JsonHttpResponseHandler() {

                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                    super.onSuccess(statusCode, headers, response);
                    if (response.optString("ret").equals("-1")) {
                        PromptUtils.showToast(response.optString("msg"));
                        return;
                    }

                    UserInfoResponse userInfoResponse = JSON.parseObject(response.toString(), UserInfoResponse.class);
                    userInfoResponse.data.setIsLogin("true");
                    GDUserInfoHelper.getInstance(LoginActivity.this).insertUserInfo(userInfoResponse.data);
                    PromptUtils.showToast("登录成功");
                    finish();
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                    super.onFailure(statusCode, headers, responseString, throwable);
                }
            });
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
    }

    /** auth callback interface 获取授权**/
    private UMAuthListener umAuthListener = new UMAuthListener() {
        @Override
        public void onComplete(SHARE_MEDIA platform, int action, Map<String, String> data) {
            mShareAPI.getPlatformInfo(LoginActivity.this, platform, umLoginListener);//获取用户授权登录后的信息
        }

        @Override
        public void onError(SHARE_MEDIA platform, int action, Throwable t) {
            Toast.makeText( getApplicationContext(), "Authorize fail", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onCancel(SHARE_MEDIA platform, int action) {
            Toast.makeText( getApplicationContext(), "Authorize cancel", Toast.LENGTH_SHORT).show();
        }
    };
    private UMAuthListener umLoginListener = new UMAuthListener() {
        @Override
        public void onComplete(SHARE_MEDIA platform, int action, Map<String, String> data) {
            if (data!=null){
                Toast.makeText(getApplicationContext(), data.toString(), Toast.LENGTH_LONG).show();
            }

        }

        @Override
        public void onError(SHARE_MEDIA platform, int action, Throwable t) {
            Toast.makeText( getApplicationContext(), "get fail", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onCancel(SHARE_MEDIA platform, int action) {
            Toast.makeText( getApplicationContext(), "get cancel", Toast.LENGTH_SHORT).show();
        }
    };
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        mShareAPI.onActivityResult(requestCode, resultCode, data);
    }
}
