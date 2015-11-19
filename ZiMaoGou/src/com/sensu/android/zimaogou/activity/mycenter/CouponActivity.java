package com.sensu.android.zimaogou.activity.mycenter;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import com.sensu.android.zimaogou.R;
import com.sensu.android.zimaogou.activity.BaseActivity;
import com.sensu.android.zimaogou.adapter.CouponInvalidListAdapter;
import com.sensu.android.zimaogou.adapter.CouponValidListAdapter;
import com.sensu.android.zimaogou.utils.UiUtils;

/**
 * 优惠券
 * Created by qi.yang on 2015/11/19.
 */
public class CouponActivity extends BaseActivity{

    ListView mValidListView,mInvalidListView;
    ImageView mBackImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.coupon_activity);
        initView();
    }

    private void initView(){
        mValidListView = (ListView) findViewById(R.id.lv_valid);
        mInvalidListView = (ListView) findViewById(R.id.lv_invalid);
        mBackImageView = (ImageView) findViewById(R.id.back);
        mValidListView.setDivider(null);
        mValidListView.setAdapter(new CouponValidListAdapter(this));
        mInvalidListView.setDivider(null);
        mInvalidListView.setAdapter(new CouponInvalidListAdapter(this));
        UiUtils.setListViewHeightBasedOnChilds(mValidListView);
        UiUtils.setListViewHeightBasedOnChilds(mInvalidListView);
        mBackImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }
}
