package com.sensu.android.zimaogou.activity.mycenter;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.sensu.android.zimaogou.IConstants;
import com.sensu.android.zimaogou.R;
import com.sensu.android.zimaogou.activity.BaseActivity;
import com.sensu.android.zimaogou.encrypt.MD5Utils;
import com.sensu.android.zimaogou.external.greendao.helper.GDUserInfoHelper;
import com.sensu.android.zimaogou.external.greendao.model.UserInfo;
import com.sensu.android.zimaogou.utils.HttpUtil;
import com.sensu.android.zimaogou.utils.LogUtils;
import com.sensu.android.zimaogou.utils.PromptUtils;
import com.sensu.android.zimaogou.utils.TextUtils;
import org.apache.http.Header;
import org.json.JSONObject;

import java.security.NoSuchAlgorithmException;

/**
 * Created by qi.yang on 2015/12/1.
 */
public class UpdatePasswordActivity extends BaseActivity {
    private ImageView mBackImageView;
    EditText et_oldPass,et_newPass,et_surePass;
    TextView tv_submit;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.update_password_activity);
        initView();
    }

    private void initView(){
        mBackImageView = (ImageView) findViewById(R.id.back);
        mBackImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        et_oldPass = (EditText) findViewById(R.id.et_oldPass);
        et_newPass = (EditText) findViewById(R.id.et_newPass);
        et_surePass = (EditText) findViewById(R.id.et_surePass);
        tv_submit = (TextView) findViewById(R.id.tv_submit);
        tv_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updatePass();
            }
        });
    }

    private void updatePass(){
        UserInfo userInfo = GDUserInfoHelper.getInstance(this).getUserInfo();
        String oldPass = et_oldPass.getText().toString().trim();
        String newPass = et_newPass.getText().toString().trim();
        String surePass = et_surePass.getText().toString().trim();
        if(TextUtils.isEmpty(oldPass)){
            PromptUtils.showToast("请输入旧密码");
            return;
        }
        if(TextUtils.isEmpty(newPass)){
            PromptUtils.showToast("请输入新密码");
            return;
        }
        if(TextUtils.isEmpty(surePass)){
            PromptUtils.showToast("请再输入一次新密码");
            return;
        }
        RequestParams requestParams = new RequestParams();
        requestParams.put("uid",userInfo.getUid());
        try {
            requestParams.put("old_password", MD5Utils.md5(oldPass));
            requestParams.put("new_password",MD5Utils.md5(newPass));
            requestParams.put("confirm_password",MD5Utils.md5(surePass));
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        HttpUtil.postWithSign(userInfo.getToken(), IConstants.sUpdateUserInfo+"/"+userInfo.getUid()+"/update_password",requestParams,new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                LogUtils.d("修改用户密码返回：",response.toString());
                if(response.optInt("ret")>=0){
                    PromptUtils.showToast("修改密码成功！");
                    finish();
                }else{
                    PromptUtils.showToast(response.optString("msg"));
                }
            }
        });

    }
}
