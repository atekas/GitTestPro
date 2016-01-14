package com.sensu.android.zimaogou.activity.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.alibaba.fastjson.JSON;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.sensu.android.zimaogou.BaseApplication;
import com.sensu.android.zimaogou.IConstants;
import com.sensu.android.zimaogou.Mode.BannerMode;
import com.sensu.android.zimaogou.R;
import com.sensu.android.zimaogou.ReqResponse.AddressResponse;
import com.sensu.android.zimaogou.ReqResponse.BannerResponse;
import com.sensu.android.zimaogou.ReqResponse.ThemeDetailResponse;
import com.sensu.android.zimaogou.ReqResponse.ThemeListResponse;
import com.sensu.android.zimaogou.activity.*;
import com.sensu.android.zimaogou.activity.mycenter.WebViewActivity;
import com.sensu.android.zimaogou.activity.tour.TourBuyDetailsActivity;
import com.sensu.android.zimaogou.activity_home.HomeHorizontalLinearLayout;
import com.sensu.android.zimaogou.activity_home.HomeVerticalLinearLayout;
import com.sensu.android.zimaogou.pullrefresh.PullToRefreshLayout;
import com.sensu.android.zimaogou.utils.HttpUtil;
import com.sensu.android.zimaogou.utils.LogUtils;
import com.sensu.android.zimaogou.utils.NetworkTypeUtils;
import com.sensu.android.zimaogou.utils.PromptUtils;
import com.sensu.android.zimaogou.widget.ImageRollView;
import org.apache.http.Header;
import org.json.JSONObject;

import java.util.ArrayList;

import static com.sensu.android.zimaogou.ReqResponse.ThemeListResponse.*;

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
    ArrayList<String> list = new ArrayList<String>();
    BannerResponse bannerResponse = new BannerResponse();
    ArrayList<BannerMode> bannerModes = new ArrayList<BannerMode>();

    private PullToRefreshLayout mPullToRefreshLayout;
    private View mNoNetView;

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
//            PromptUtils.showToast("home隐藏");
        } else {
//            PromptUtils.showToast("home显示");
        }
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    protected void initView() {
        mNoNetView = mParentActivity.findViewById(R.id.no_net);
        mPullToRefreshLayout = (PullToRefreshLayout) mParentActivity.findViewById(R.id.refresh_view);

        if (NetworkTypeUtils.isNetWorkAvailable()) {
            mPullToRefreshLayout.setVisibility(View.VISIBLE);
            mNoNetView.setVisibility(View.GONE);
        } else {
            mPullToRefreshLayout.setVisibility(View.GONE);
            mNoNetView.setVisibility(View.VISIBLE);
            ((ImageView) mNoNetView.findViewById(R.id.img_exception)).setImageResource(R.drawable.exception_internet);
            ((TextView) mNoNetView.findViewById(R.id.tv_exception)).setText("您的网络开了小差哦");
        }

        mPullToRefreshLayout.setOnRefreshListener(new PullToRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh(final PullToRefreshLayout pullToRefreshLayout) {
                initView();
                new Handler() {
                    @Override
                    public void handleMessage(Message msg) {
                        pullToRefreshLayout.refreshFinish(PullToRefreshLayout.SUCCEED);
                    }
                }.sendEmptyMessageDelayed(0, 2000);
            }

            @Override
            public void onLoadMore(final PullToRefreshLayout pullToRefreshLayout) {
                new Handler() {
                    @Override
                    public void handleMessage(Message msg) {
                        pullToRefreshLayout.loadmoreFinish(PullToRefreshLayout.SUCCEED);
                    }
                }.sendEmptyMessageDelayed(0, 2000);
            }
        });

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


        mImageRollView.setOnImageRollViewClickListener(new ImageRollView.OnImageRollViewClickListener() {
            @Override
            public void onClick(View view, int position) {
                if (bannerModes.get(position).getType().equals("1")) {
                    Bundle bundle = new Bundle();
                    bundle.putString("id",bannerModes.get(position).getValue());
                    bundle.putString("image",bannerModes.get(position).getThemeimage());
                    bundle.putString("name",bannerModes.get(position).getThemename());
                    bundle.putString("content",bannerModes.get(position).getThemecontent());

                    Intent intent = new Intent(mParentActivity, SpecialDetailsActivity.class);
                    intent.putExtras(bundle);
                    startActivity(intent);
                    return;
                } else if (bannerModes.get(position).getType().equals("2")) {
                    Intent intent = new Intent(mParentActivity, ProductDetailsActivity.class);
                    intent.putExtra(ProductDetailsActivity.PRODUCT_ID, bannerModes.get(position).getValue());
                    intent.putExtra(ProductDetailsActivity.FROM_SOURCE, "0");
                    startActivity(intent);
                } else {
                    startActivity(new Intent(mParentActivity, WebViewActivity.class)
                            .putExtra("title", bannerModes.get(position).getName())
                            .putExtra("url", bannerModes.get(position).getValue()));
                }
            }
        });

        mParentActivity.findViewById(R.id.ll_buyRead).setOnClickListener(this);
        mParentActivity.findViewById(R.id.ll_hotGoods).setOnClickListener(this);
        mParentActivity.findViewById(R.id.ll_latest).setOnClickListener(this);
        mParentActivity.findViewById(R.id.ll_featureVideos).setOnClickListener(this);
        mNoNetView.findViewById(R.id.bt_reload).setOnClickListener(this);
        getAddress();
        getBanner();
    }

    @Override
    public void onClick(View view) {
        Intent intent;
        switch (view.getId()) {
            case R.id.ll_buyRead:
                startActivity(new Intent(mParentActivity, WebViewActivity.class).putExtra("title", "诞生秘密"));
                break;
            case R.id.ll_hotGoods:
                intent = new Intent(mParentActivity, ProductListActivity.class);
                intent.putExtra(ProductListActivity.IS_NO_TITLE, true);
                intent.putExtra(ProductListActivity.PRODUCT_LIST_TAG, "1");
                intent.putExtra(ProductListActivity.PRODUCT_LIST_TITLE, "不得不爱");
                startActivity(intent);
                break;
            case R.id.ll_latest:
                intent = new Intent(mParentActivity, ProductListActivity.class);
                intent.putExtra(ProductListActivity.IS_NO_TITLE, true);
                intent.putExtra(ProductListActivity.PRODUCT_LIST_TAG, "3");
                intent.putExtra(ProductListActivity.PRODUCT_LIST_TITLE, "限时优惠");
                startActivity(intent);
                break;
            case R.id.ll_featureVideos:
                intent = new Intent(mParentActivity, ProductListActivity.class);
                intent.putExtra(ProductListActivity.IS_NO_TITLE, true);
                intent.putExtra(ProductListActivity.PRODUCT_LIST_TAG, "2");
                intent.putExtra(ProductListActivity.PRODUCT_LIST_TITLE, "星梦奇缘");
                startActivity(intent);
                break;
            case R.id.bt_reload:
                initView();
                break;
        }
    }

    private void getAddress() {
        HttpUtil.get(IConstants.sGetProvinceAndCity, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                LogUtils.d("获取城市返回：", response.toString());
                BaseApplication.setAddressResponse(JSON.parseObject(response.toString(), AddressResponse.class));
            }
        });
    }

    private void getBanner() {
        HttpUtil.get(IConstants.sGetBanner, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                bannerResponse = JSON.parseObject(response.toString(), BannerResponse.class);
                bannerModes = bannerResponse.data;
                list.clear();
                for (BannerMode bannerMode : bannerModes) {
                    list.add(bannerMode.getImage());
                }
                mImageRollView.initImageRollView(list, true);
            }
        });
    }
}
