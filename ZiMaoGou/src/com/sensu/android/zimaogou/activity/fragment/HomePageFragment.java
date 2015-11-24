package com.sensu.android.zimaogou.activity.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.sensu.android.zimaogou.R;
import com.sensu.android.zimaogou.activity_home.HomeHorizontalLinearLayout;
import com.sensu.android.zimaogou.activity_home.HomeVerticalLinearLayout;
import com.sensu.android.zimaogou.utils.PromptUtils;

/**
 * Created by zhangwentao on 2015/11/10.
 */
public class HomePageFragment extends BaseFragment {

    //the value for the type
    public static int DAILY_COMMEND_TYPE = 1;
    public static int GROUP_SPECIAL_TYPE = 2;
    public static int FIND_STORE_TYPE = 3;
    public static int LIVELY_STORE_TYPE = 4;
    //推荐单品
    public static int RECOMMEND_ITEM_TYPE = 5;

    HomeHorizontalLinearLayout mDailyLinearLayout,mGroupLinearLayout,mFindStoreLinearLayout;
    HomeVerticalLinearLayout mLivelyLinearLayout, mRecommendItemLayout;

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
    }


}
