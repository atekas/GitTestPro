package com.sensu.android.zimaogou.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.AdapterView;
import com.sensu.android.zimaogou.R;
import com.sensu.android.zimaogou.activity.tour.TourBuyDetailsActivity;
import com.sensu.android.zimaogou.adapter.TourBuyAdapter;
import com.sensu.android.zimaogou.widget.OnRefreshListener;
import com.sensu.android.zimaogou.widget.RefreshListView;

/**
 * Created by zhangwentao on 2015/11/25.
 * <p/>
 * 好店铺列表页
 */
public class GoodShopActivity extends BaseActivity implements AdapterView.OnItemClickListener {

    private RefreshListView mListView;
    private TourBuyAdapter mGoodShopAdapter;

    private Handler mHandler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.good_shop_activity);

        initViews();
    }

    private void initViews() {
        mListView = (RefreshListView) findViewById(R.id.good_shop_list);
        mGoodShopAdapter = new TourBuyAdapter(this);
        mListView.setAdapter(mGoodShopAdapter);

        mListView.setOnRefreshListener(mOnRefreshListener);
        mListView.setOnItemClickListener(this);
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
        startActivity(new Intent(this, TourBuyDetailsActivity.class));
    }
}
