package com.sensu.android.zimaogou.activity.tour;

import android.os.Bundle;
import android.view.View;
import android.widget.GridView;
import android.widget.LinearLayout;
import com.sensu.android.zimaogou.R;
import com.sensu.android.zimaogou.activity.BaseActivity;
import com.sensu.android.zimaogou.activity.LocalPhotoActivity;
import com.sensu.android.zimaogou.photoalbum.PhotoInfo;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhangwentao on 2015/11/13.
 */
public class TourBuySendActivity extends BaseActivity implements View.OnClickListener {

    private GridView mGridView;
    private TourBuySendAdapter mTourBuySendAdapter;

    private List<PhotoInfo> mPhotoList;

    private boolean mIsSelectFood;
    private boolean mIsSelectBuy;
    private boolean mIsSelectSightSpot;

    private LinearLayout mFoodLayout;
    private LinearLayout mBuyLayout;
    private LinearLayout mSightSpotLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tour_buy_send_activity);

        initViews();
    }

    private void initViews() {

        mPhotoList = (List<PhotoInfo>) getIntent().getSerializableExtra(LocalPhotoActivity.SELECT_PHOTOS);

        if (mPhotoList == null) {
            mPhotoList = new ArrayList<PhotoInfo>();
        }

        mGridView = (GridView) findViewById(R.id.grid_view);
        mTourBuySendAdapter = new TourBuySendAdapter(this);
        mGridView.setAdapter(mTourBuySendAdapter);
        mTourBuySendAdapter.setList(mPhotoList);

        mFoodLayout = (LinearLayout) findViewById(R.id.food_layout);
        mBuyLayout = (LinearLayout) findViewById(R.id.buy_layout);
        mSightSpotLayout = (LinearLayout) findViewById(R.id.sight_spot_layout);

        mFoodLayout.setOnClickListener(this);
        mBuyLayout.setOnClickListener(this);
        mSightSpotLayout.setOnClickListener(this);
        findViewById(R.id.cancel).setOnClickListener(this);
        findViewById(R.id.release).setOnClickListener(this);
        findViewById(R.id.choose_country).setOnClickListener(this);
        findViewById(R.id.location_switch).setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.cancel:
                finish();
                break;
            case R.id.release:
                //TODO 发布按钮
                break;
            case R.id.choose_country:
                //TODO 选择国家 弹出对话框
                break;
            case R.id.location_switch:
                //todo 定位开关
                break;
            case R.id.food_layout:
                if (mIsSelectFood) {
                    mFoodLayout.setSelected(false);
                    findViewById(R.id.food_text).setSelected(false);
                    findViewById(R.id.food_select).setVisibility(View.GONE);
                    mIsSelectFood = false;
                } else {
                    mFoodLayout.setSelected(true);
                    findViewById(R.id.food_text).setSelected(true);
                    findViewById(R.id.food_select).setVisibility(View.VISIBLE);
                    mIsSelectFood = true;
                }
                break;
            case R.id.buy_layout:
                if (mIsSelectBuy) {
                    mBuyLayout.setSelected(false);
                    findViewById(R.id.buy_text).setSelected(false);
                    findViewById(R.id.buy_select).setVisibility(View.GONE);
                    mIsSelectBuy = false;
                } else {
                    mBuyLayout.setSelected(true);
                    findViewById(R.id.buy_text).setSelected(true);
                    findViewById(R.id.buy_select).setVisibility(View.VISIBLE);
                    mIsSelectBuy = true;
                }
                break;
            case R.id.sight_spot_layout:
                if (mIsSelectSightSpot) {
                    mSightSpotLayout.setSelected(false);
                    findViewById(R.id.sight_spot_text).setSelected(false);
                    findViewById(R.id.sight_spot_select).setVisibility(View.GONE);
                    mIsSelectSightSpot = false;
                } else {
                    mSightSpotLayout.setSelected(true);
                    findViewById(R.id.sight_spot_text).setSelected(true);
                    findViewById(R.id.sight_spot_select).setVisibility(View.VISIBLE);
                    mIsSelectSightSpot = true;
                }
                break;
        }
    }
}
