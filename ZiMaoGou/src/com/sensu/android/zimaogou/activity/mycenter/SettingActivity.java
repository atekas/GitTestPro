package com.sensu.android.zimaogou.activity.mycenter;

import android.app.Dialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.sensu.android.zimaogou.IConstants;
import com.sensu.android.zimaogou.R;
import com.sensu.android.zimaogou.activity.BaseActivity;
import com.sensu.android.zimaogou.activity.MainActivity;
import com.sensu.android.zimaogou.external.greendao.helper.GDUserInfoHelper;
import com.sensu.android.zimaogou.external.greendao.model.UserInfo;
import com.sensu.android.zimaogou.utils.DataCleanManager;
import com.sensu.android.zimaogou.utils.HttpUtil;
import com.sensu.android.zimaogou.utils.PromptUtils;
import org.apache.http.Header;
import org.json.JSONObject;


/**
 * Created by qi.yang on 2015/11/26.
 */
public class SettingActivity extends BaseActivity {
    ImageView mBackImageView;
    RelativeLayout mUpdatePassRelativeLayout, mLoginOutRelativeLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.setting_activity);
        mBackImageView = (ImageView) findViewById(R.id.back);
        mUpdatePassRelativeLayout = (RelativeLayout) findViewById(R.id.rl_updatePass);
        mLoginOutRelativeLayout = (RelativeLayout) findViewById(R.id.rl_loginOut);
        mBackImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        try {
            String size = DataCleanManager.getTotalCacheSize(this);
            ((TextView) findViewById(R.id.cache_size)).setText(size);
        } catch (Exception e) {
            e.printStackTrace();
        }


        flushUi();
    }

    /**
     * 修改密码
     */
    public void UpdatePassClick(View v) {
        startActivity(new Intent(this, UpdatePasswordActivity.class));
    }

    /**
     * 清除缓存
     */
    public void CacheClick(View v) {
        DataCleanManager.clearAllCache(this);
        String size = null;
        try {
            size = DataCleanManager.getTotalCacheSize(this);
            ((TextView) findViewById(R.id.cache_size)).setText(size);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 用户协议
     */
    public void ServiceClick(View v) {
        startActivity(new Intent(this, WebViewActivity.class).putExtra("title", "用户协议"));
    }

    /**
     * 用户去评分
     */
    public void RatingClick(View v) {
        Uri uri = Uri.parse("market://details?id="+getPackageName());
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    /**
     * 关于我们
     */
    public void AboutUsClick(View v) {
        AboutUs();
    }

    /**
     * 退出账号
     */
    public void LoginOutClick(View v) {
        LoginOutDialog();
    }

    Dialog mLoginOutDialog;

    private void LoginOutDialog() {
        mLoginOutDialog = new Dialog(this, R.style.dialog);
        mLoginOutDialog.setCancelable(true);
        mLoginOutDialog.setContentView(R.layout.loginout_dialog);

        Button bt_sure = (Button) mLoginOutDialog.findViewById(R.id.bt_sure);
        Button bt_cancel = (Button) mLoginOutDialog.findViewById(R.id.bt_cancel);

        bt_sure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                loginOut();
                mLoginOutDialog.dismiss();
            }
        });
        bt_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mLoginOutDialog.dismiss();
            }
        });
        mLoginOutDialog.show();
    }

    /**
     * 退出登录请求
     */
    private void loginOut() {
        UserInfo userInfo = GDUserInfoHelper.getInstance(this).getUserInfo();

        if (userInfo == null) {
            return;
        }

        RequestParams requestParams1 = new RequestParams();
        requestParams1.put("uid", userInfo.getUid());

        HttpUtil.postWithSign(userInfo.getToken(), IConstants.sLoginOut, requestParams1, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                Log.d("退出登录返回值：", response.toString());
                if (response.optString("ret").equals("0")) {
                    PromptUtils.showToast("退出登录");
                    GDUserInfoHelper.getInstance(SettingActivity.this).deleteData();
                    Intent intent = new Intent(SettingActivity.this, MainActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                    startActivity(intent);
                }

            }
        });
    }

    private void AboutUs() {
        Intent intent = new Intent(SettingActivity.this, WebViewActivity.class);
        intent.putExtra("title", "关于我们");
        intent.putExtra("url",IConstants.sAboutUs);
        startActivity(intent);
//        HttpUtil.get(IConstants.sAboutUs, new JsonHttpResponseHandler() {
//            @Override
//            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
//                super.onSuccess(statusCode, headers, response);
//                JSONObject data = response.optJSONObject("data");
//                Intent intent = new Intent(SettingActivity.this, WebViewActivity.class);
//                intent.putExtra("title", data.optString("title"));
//                intent.putExtra("url",data.optString("url"));
//                startActivity(intent);
//
//            }
//        });
    }

    private void flushUi() {
        UserInfo userInfo = GDUserInfoHelper.getInstance(this).getUserInfo();
        if (userInfo == null) {
            mLoginOutRelativeLayout.setVisibility(View.GONE);
            mUpdatePassRelativeLayout.setVisibility(View.GONE);
        } else {
            mLoginOutRelativeLayout.setVisibility(View.VISIBLE);
            mUpdatePassRelativeLayout.setVisibility(View.VISIBLE);
        }
    }
}
