package com.sensu.android.zimaogou.activity.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.sensu.android.zimaogou.R;
import com.sensu.android.zimaogou.activity_home.HomeHorizontalLinearLayout;
import com.sensu.android.zimaogou.activity_home.HomeVerticalLinearLayout;
import com.sensu.android.zimaogou.utils.PromptUtils;
import com.sensu.android.zimaogou.widget.ImageRollView;

import java.util.ArrayList;

/**
 * Created by zhangwentao on 2015/11/10.
 */
public class HomePageFragment extends BaseFragment implements View.OnClickListener {

    //the value for the type
    public static int DAILY_COMMEND_TYPE = 1;
    public static int GROUP_SPECIAL_TYPE = 2;
    public static int FIND_STORE_TYPE = 3;
    public static int LIVELY_STORE_TYPE = 4;
    //推荐单品
    public static int RECOMMEND_ITEM_TYPE = 5;

    HomeHorizontalLinearLayout mDailyLinearLayout, mGroupLinearLayout, mFindStoreLinearLayout;
    HomeVerticalLinearLayout mLivelyLinearLayout, mRecommendItemLayout;

    private ImageRollView mImageRollView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.home_page_fragment, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (hidden) {
            PromptUtils.showToast("home隐藏");
        } else {
            PromptUtils.showToast("home显示");
        }
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    protected void initView() {
        mDailyLinearLayout = (HomeHorizontalLinearLayout) mParentActivity.findViewById(R.id.ll_dailyRecommend);
        mGroupLinearLayout = (HomeHorizontalLinearLayout) mParentActivity.findViewById(R.id.ll_groupSpecial);
        mFindStoreLinearLayout = (HomeHorizontalLinearLayout) mParentActivity.findViewById(R.id.ll_findStore);
        mLivelyLinearLayout = (HomeVerticalLinearLayout) mParentActivity.findViewById(R.id.ll_lively);
        mRecommendItemLayout = (HomeVerticalLinearLayout) mParentActivity.findViewById(R.id.recommend_item);
        mDailyLinearLayout.setData(DAILY_COMMEND_TYPE);
        mGroupLinearLayout.setData(GROUP_SPECIAL_TYPE);
        mFindStoreLinearLayout.setData(FIND_STORE_TYPE);
        mLivelyLinearLayout.setData(LIVELY_STORE_TYPE);
        mRecommendItemLayout.setData(RECOMMEND_ITEM_TYPE);

        mImageRollView = (ImageRollView) mParentActivity.findViewById(R.id.banner);

        ArrayList<String> list = new ArrayList<String>();
        list.add("http://img.taodiantong.cn/v55183/infoimg/2013-07/130720115322ky.jpg");
        list.add("http://pic30.nipic.com/20130626/8174275_085522448172_2.jpg");
        list.add("http://pic18.nipic.com/20111215/577405_080531548148_2.jpg");
        list.add("http://pic15.nipic.com/20110722/2912365_092519919000_2.jpg");
        list.add("http://pic.58pic.com/58pic/12/64/27/55U58PICrdX.jpg");

        mImageRollView.initImageRollView(list, true);

        mParentActivity.findViewById(R.id.ll_buyRead).setOnClickListener(this);
        mParentActivity.findViewById(R.id.ll_hotGoods).setOnClickListener(this);
        mParentActivity.findViewById(R.id.ll_latest).setOnClickListener(this);
        mParentActivity.findViewById(R.id.ll_featureVideos).setOnClickListener(this);
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ll_buyRead:
                PromptUtils.showToast("购物须知");
                break;
            case R.id.ll_hotGoods:
                PromptUtils.showToast("人气商品");
                break;
            case R.id.ll_latest:
                PromptUtils.showToast("最新上架");
                break;
            case R.id.ll_featureVideos:
                PromptUtils.showToast("");
                break;
        }
    }
}
