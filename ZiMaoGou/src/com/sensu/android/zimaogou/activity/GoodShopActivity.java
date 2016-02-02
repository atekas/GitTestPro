package com.sensu.android.zimaogou.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import com.alibaba.fastjson.JSON;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.sensu.android.zimaogou.BaseApplication;
import com.sensu.android.zimaogou.IConstants;
import com.sensu.android.zimaogou.Mode.TravelMode;
import com.sensu.android.zimaogou.R;
import com.sensu.android.zimaogou.ReqResponse.TravelResponse;
import com.sensu.android.zimaogou.activity.tour.TourBuyDetailsActivity;
import com.sensu.android.zimaogou.activity_home.StoreHorizontalListViewAdapter;
import com.sensu.android.zimaogou.adapter.TourBuyAdapter;
import com.sensu.android.zimaogou.utils.HttpUtil;
import com.sensu.android.zimaogou.utils.LogUtils;
import com.sensu.android.zimaogou.widget.OnRefreshListener;
import com.sensu.android.zimaogou.widget.RefreshListView;
import org.apache.http.Header;
import org.json.JSONObject;

/**
 * Created by zhangwentao on 2015/11/25.
 * <p/>
 * 好店铺列表页
 */
public class GoodShopActivity extends BaseActivity implements AdapterView.OnItemClickListener {

    private RefreshListView mListView;
    private TourBuyAdapter mGoodShopAdapter;

    private Handler mHandler = new Handler();
    TravelResponse travelModes = new TravelResponse();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.good_shop_activity);

        initViews();
        getTravelData();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(BaseApplication.tempTravel !=null){
            TravelMode travelMode = BaseApplication.tempTravel;
            for(int i = 0;i<travelModes.data.size();i++){
                if(travelMode.getId().equals(travelModes.data.get(i).getId())){
                    travelModes.data.get(i).setLike_num(travelMode.getLike_num());
                    travelModes.data.get(i).setComment_num(travelMode.getComment_num());
                    travelModes.data.get(i).setBrowser_num(travelMode.getBrowser_num());
                    travelModes.data.get(i).setIs_like(travelMode.getIs_like());
                }

            }
            mGoodShopAdapter.reFlush(travelModes.data);
            BaseApplication.tempTravel = null;
        }
    }

    private void initViews() {
        mListView = (RefreshListView) findViewById(R.id.good_shop_list);
        mGoodShopAdapter = new TourBuyAdapter(this);
        mListView.setAdapter(mGoodShopAdapter);

        mListView.setOnRefreshListener(mOnRefreshListener);
        mListView.setOnItemClickListener(this);
        findViewById(R.id.back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }

    private OnRefreshListener mOnRefreshListener = new OnRefreshListener() {
        @Override
        public void onDownPullRefresh() {
            //下拉刷新接口
            mHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    mListView.hideHeaderView();
                }
            }, 2000);
        }

        @Override
        public void onLoadingMore() {
            //上拉加载接口
            mHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    mListView.hideFooterView();
                }
            }, 2000);
        }
    };

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        if(i > 0) {
            startActivity(new Intent(this, TourBuyDetailsActivity.class).putExtra("travel", travelModes.data.get(i-1)));
        }
    }


    /**
     *
     * 获取游购数据
     */
    private void getTravelData(){

        RequestParams requestParams = new RequestParams();
        requestParams.put("is_shop","1");
        HttpUtil.get(IConstants.sGetTravelList,requestParams,new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                LogUtils.d("发现好店铺返回：", response.toString());
                travelModes = JSON.parseObject(response.toString(), TravelResponse.class);
                mGoodShopAdapter.reFlush(travelModes.data);
            }
        });
    }
}
