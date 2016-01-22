package com.sensu.android.zimaogou.activity.mycenter;

import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import com.alibaba.fastjson.JSON;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.sensu.android.zimaogou.IConstants;
import com.sensu.android.zimaogou.Mode.MessageMode;
import com.sensu.android.zimaogou.R;
import com.sensu.android.zimaogou.ReqResponse.MessageResponse;
import com.sensu.android.zimaogou.activity.BaseActivity;
import com.sensu.android.zimaogou.adapter.MessageAdapter;
import com.sensu.android.zimaogou.external.greendao.helper.GDUserInfoHelper;
import com.sensu.android.zimaogou.external.greendao.model.UserInfo;
import com.sensu.android.zimaogou.utils.HttpUtil;
import com.sensu.android.zimaogou.utils.LogUtils;
import com.sensu.android.zimaogou.utils.PromptUtils;
import com.sensu.android.zimaogou.widget.OnRefreshListener;
import com.sensu.android.zimaogou.widget.RefreshListView;
import org.apache.http.Header;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by qi.yang on 2015/12/16.
 */
public class MessageActivity extends BaseActivity {
    RefreshListView mMessageListView;
    MessageAdapter messageAdapter;

    private UserInfo mUserInfo;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.message_activity);
        initView();
    }

    private void initView(){
        mUserInfo = GDUserInfoHelper.getInstance(this).getUserInfo();
        mMessageListView = (RefreshListView) findViewById(R.id.lv_message);
        mMessageListView.setDivider(null);
        mMessageListView.setOnRefreshListener(mOnRefreshListener);

        findViewById(R.id.back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

    }


    @Override
    protected void onResume() {
        super.onResume();
        getMyMessage();
    }

    private Handler mHandler = new Handler();
    private OnRefreshListener mOnRefreshListener = new OnRefreshListener() {
        @Override
        public void onDownPullRefresh() {
            //下拉刷新接口
            mHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    mMessageListView.hideHeaderView();
                }
            }, 2000);
        }

        @Override
        public void onLoadingMore() {
            //上拉加载接口
            mHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    mMessageListView.hideFooterView();
                }
            }, 2000);
        }
    };

    private void getMyMessage() {
        RequestParams requestParams = new RequestParams();
        requestParams.put("uid", mUserInfo.getUid());
        HttpUtil.getWithSign(mUserInfo.getToken(), IConstants.sMyMessage, requestParams, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                LogUtils.d("我的消息返回",response.toString());
                if(response.optInt("ret")<0){
                    PromptUtils.showToast(response.optString("msg"));
                }else{
                    MessageResponse messageResponse = JSON.parseObject(response.toString(),MessageResponse.class);
                    messageAdapter = new MessageAdapter(MessageActivity.this,messageResponse.data);
                    mMessageListView.setAdapter(messageAdapter);
                }

            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);
            }
        });
    }
}
