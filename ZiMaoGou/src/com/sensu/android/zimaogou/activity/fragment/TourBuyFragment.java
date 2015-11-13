package com.sensu.android.zimaogou.activity.fragment;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import com.sensu.android.zimaogou.R;
import com.sensu.android.zimaogou.activity.tour.TourBuySendActivity;
import com.sensu.android.zimaogou.adapter.TourBuyAdapter;

/**
 * Created by zhangwentao on 2015/11/10.
 */
public class TourBuyFragment extends BaseFragment implements View.OnClickListener {

    private ListView mTourBuyListView;
    private TourBuyAdapter mTourBuyAdapter;

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
        mTourBuyListView = (ListView) mParentActivity.findViewById(R.id.tour_list);
        mTourBuyAdapter = new TourBuyAdapter(mParentActivity);
        mTourBuyListView.setAdapter(mTourBuyAdapter);
    }

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
