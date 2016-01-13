package com.sensu.android.zimaogou.service;

import android.app.Dialog;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.sensu.android.zimaogou.IConstants;
import com.sensu.android.zimaogou.R;
import com.sensu.android.zimaogou.utils.AppInfoUtils;
import com.sensu.android.zimaogou.utils.HttpUtil;
import com.sensu.android.zimaogou.utils.PromptUtils;
import org.apache.http.Header;
import org.json.JSONObject;

/**
 * Created by zhangwentao on 2016/1/13.
 */
public class UpdateAppService extends Service {

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        RequestParams requestParams = new RequestParams();
        requestParams.put("uid", "0");
        requestParams.put("version", AppInfoUtils.getVersionName());

        HttpUtil.get(IConstants.sVersion, requestParams, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                if (response.optString("ret").equals("-1")) {
                    return;
                }
                String version = response.optJSONObject("data").optString("version_number");
                if (AppInfoUtils.getVersionName().equals(version)) {
                    //版本号一致
                } else {
                    PromptUtils.showToast("有新版本");
                    if (response.optJSONObject("data").optString("is_force_update").equals("1")) {
                        //需要强制升级
                    } else {
                        //用户选择升级
                    }
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);
            }
        });


        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
