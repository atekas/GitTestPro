package com.sensu.android.zimaogou.activity.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import com.sensu.android.zimaogou.R;
import com.sensu.android.zimaogou.activity_home.DailyRecLinearLayout;
import com.sensu.android.zimaogou.utils.PromptUtils;

/**
 * Created by zhangwentao on 2015/11/10.
 */
public class HomePageFragment extends BaseFragment {

    DailyRecLinearLayout mDailyLinearLayout;
    //how many images for the screen
    int sDailyCommendSize = 3;
    int sGroupSpecialSize = 2;
    //the value for the type
    int sDailyCommendType = 1;
    int sGroupSpecialType = 2;

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
        mDailyLinearLayout = (DailyRecLinearLayout) mParentActivity.findViewById(R.id.ll_dailyRecommend);
        mDailyLinearLayout.setData(sDailyCommendSize, sDailyCommendType);
    }


}
