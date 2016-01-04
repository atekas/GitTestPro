package com.sensu.android.zimaogou.activity.mycenter;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.util.Log;
import android.view.*;
import android.widget.AdapterView;
import android.widget.TextView;
import com.alibaba.fastjson.JSON;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.sensu.android.zimaogou.IConstants;
import com.sensu.android.zimaogou.R;
import com.sensu.android.zimaogou.ReqResponse.MyTravelResponse;
import com.sensu.android.zimaogou.ReqResponse.TravelResponse;
import com.sensu.android.zimaogou.activity.BaseActivity;
import com.sensu.android.zimaogou.activity.LocalPhotoActivity;
import com.sensu.android.zimaogou.activity.fragment.BaseFragment;
import com.sensu.android.zimaogou.activity.tour.TourBuyDetailsActivity;
import com.sensu.android.zimaogou.activity.tour.TourBuySendActivity;
import com.sensu.android.zimaogou.activity.tour.TourSendData;
import com.sensu.android.zimaogou.activity.video.CameraActivity;
import com.sensu.android.zimaogou.adapter.TourBuyAdapter;
import com.sensu.android.zimaogou.external.greendao.helper.GDUserInfoHelper;
import com.sensu.android.zimaogou.external.greendao.model.UserInfo;
import com.sensu.android.zimaogou.photoalbum.PhotoInfo;
import com.sensu.android.zimaogou.utils.HttpUtil;
import com.sensu.android.zimaogou.widget.OnRefreshListener;
import com.sensu.android.zimaogou.widget.RefreshListView;
import org.apache.http.Header;
import org.json.JSONObject;

import java.io.File;

/**
 * Created by zhangwentao on 2015/11/10.
 */
public class MyTravelActivity extends BaseActivity {

    public static final int TAKE_PHOTO_CODE = 1;
    public static final int CHOOSE_PHOTO_CODE = 2;
    private RefreshListView mTourBuyListView;
    private TourBuyAdapter mTourBuyAdapter;
    String path;
    MyTravelResponse travelModes = new MyTravelResponse();
    private Handler mHandler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mytravel_activity);
        initView();
    }

    protected void initView() {
        findViewById(R.id.back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        mTourBuyListView = (RefreshListView) findViewById(R.id.tour_list);
        mTourBuyAdapter = new TourBuyAdapter(this,true);
        mTourBuyListView.setAdapter(mTourBuyAdapter);

        mTourBuyListView.setOnRefreshListener(mOnRefreshListener);
        mTourBuyListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if(i > 0){
                    startActivity(new Intent(MyTravelActivity.this,TourBuyDetailsActivity.class).putExtra("travel",travelModes.data.travel.get(i -1)));
                }
            }
        });
        getTravelData();
    }

    private OnRefreshListener mOnRefreshListener = new OnRefreshListener() {
        @Override
        public void onDownPullRefresh() {
            //下拉刷新接口
            mHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    mTourBuyListView.hideHeaderView();
                }
            }, 2000);
        }

        @Override
        public void onLoadingMore() {
            //上拉加载接口
            mHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    mTourBuyListView.hideFooterView();
                }
            }, 2000);
        }
    };

    /**
     *
     * 获取游购数据
     */
    private void getTravelData(){
        UserInfo userInfo = GDUserInfoHelper.getInstance(this).getUserInfo();
        RequestParams requestParams = new RequestParams();
        requestParams.put("uid", userInfo.getUid());
        HttpUtil.getWithSign(userInfo.getToken(), IConstants.sGetMyTravel, requestParams, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                Log.d("我的游购列表返回：", response.toString());

                travelModes = JSON.parseObject(response.toString(), MyTravelResponse.class);
                mTourBuyAdapter.flush(travelModes.data.travel);
            }
        });

    }




    @Override
    public void onDestroy() {
        super.onDestroy();
    }



}
