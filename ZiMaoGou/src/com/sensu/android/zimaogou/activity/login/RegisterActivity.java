package com.sensu.android.zimaogou.activity.login;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import com.alibaba.fastjson.JSON;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.sensu.android.zimaogou.BaseApplication;
import com.sensu.android.zimaogou.IConstants;
import com.sensu.android.zimaogou.R;
import com.sensu.android.zimaogou.ReqResponse.BaseReqResponse;
import com.sensu.android.zimaogou.ReqResponse.CheckCodeResponse;
import com.sensu.android.zimaogou.activity.BaseActivity;
import com.sensu.android.zimaogou.handler.UpdateTimeHandler;
import com.sensu.android.zimaogou.utils.HttpUtil;
import com.sensu.android.zimaogou.utils.PromptUtils;

/**
 * Created by zhangwentao on 2015/11/13.
 */
public class RegisterActivity extends BaseActivity implements View.OnClickListener {

    private TextView mGetAuthCode;
    private UpdateTimeHandler mUpdateTimeHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.register_activity);

        initViews();
    }

    public void initViews() {
        mGetAuthCode = (TextView) findViewById(R.id.get_auth_code);
        mGetAuthCode.setEnabled(true);

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
                mGetAuthCode.setEnabled(false);
                mUpdateTimeHandler.sendEmptyMessage(UpdateTimeHandler.UPDATE_TIME_CODE);
                break;
        }
    }

    private void checkCode() {
        RequestParams requestParams = new RequestParams();
        requestParams.put("mobile", "15229024392");
        requestParams.put("recode", "123456");
        HttpUtil.get(IConstants.sCheck, requestParams, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(String content) {
                super.onSuccess(content);
                CheckCodeResponse checkCodeResponse = JSON.parseObject(content, CheckCodeResponse.class);
                PromptUtils.showToast(checkCodeResponse.getMsg() + checkCodeResponse.data.is_pass);
            }

            @Override
            public void onFailure(Throwable error, String content) {
                super.onFailure(error, content);
            }
        });
    }
}
