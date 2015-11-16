package com.sensu.android.zimaogou.activity.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.sensu.android.zimaogou.R;
import com.sensu.android.zimaogou.activity.tour.TourBuySendActivity;
import com.sensu.android.zimaogou.adapter.TourBuyAdapter;
import com.sensu.android.zimaogou.widget.OnRefreshListener;
import com.sensu.android.zimaogou.widget.RefreshListView;

/**
 * Created by zhangwentao on 2015/11/10.
 */
public class TourBuyFragment extends BaseFragment implements View.OnClickListener {

    private RefreshListView mTourBuyListView;
    private TourBuyAdapter mTourBuyAdapter;

    private Handler mHandler = new Handler();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.tour_buy_fragment, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    protected void initView() {
        mParentActivity.findViewById(R.id.tour_buy_send).setOnClickListener(this);
        mTourBuyListView = (RefreshListView) mParentActivity.findViewById(R.id.tour_list);
        mTourBuyAdapter = new TourBuyAdapter(mParentActivity);
        mTourBuyListView.setAdapter(mTourBuyAdapter);

        mTourBuyListView.setOnRefreshListener(mOnRefreshListener);
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

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (hidden) {

        } else {

        }
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tour_buy_send:
                //TODO 进入发布界面
                mParentActivity.startActivity(new Intent(mParentActivity, TourBuySendActivity.class));
                break;
        }
    }
}
