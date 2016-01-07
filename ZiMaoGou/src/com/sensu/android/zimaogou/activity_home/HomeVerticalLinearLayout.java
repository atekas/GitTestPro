package com.sensu.android.zimaogou.activity_home;

import android.content.Context;
import android.content.Intent;
import android.util.AttributeSet;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import com.alibaba.fastjson.JSON;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.sensu.android.zimaogou.IConstants;
import com.sensu.android.zimaogou.R;
import com.sensu.android.zimaogou.ReqResponse.CommendProductResponse;
import com.sensu.android.zimaogou.ReqResponse.ThemeListResponse;
import com.sensu.android.zimaogou.activity.ProductDetailsActivity;
import com.sensu.android.zimaogou.activity.ProductListActivity;
import com.sensu.android.zimaogou.activity.SpecialActivity;
import com.sensu.android.zimaogou.activity.SpecialDetailsActivity;
import com.sensu.android.zimaogou.activity.mycenter.WebViewActivity;
import com.sensu.android.zimaogou.utils.*;
import org.apache.http.Header;
import org.json.JSONObject;

/**
 * Created by qi.yang on 2015/11/13.
 */
public class HomeVerticalLinearLayout extends LinearLayout implements AdapterView.OnItemClickListener, View.OnClickListener {

    private int mType;
    TextView mTitle;
    ListView mListView;

    private ThemeListResponse mThemeListResponse;

    private RecommendThemeAdapter mRecommendThemeAdapter;
    private CommendProductResponse mCommendProduct = new CommendProductResponse();

    public HomeVerticalLinearLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        mTitle = (TextView) findViewById(R.id.tv_title);
        mListView = (ListView) findViewById(R.id.listView);
        mListView.setDivider(null);

        mRecommendThemeAdapter = new RecommendThemeAdapter(getContext());

        mListView.setOnItemClickListener(this);
        findViewById(R.id.vertical_more).setOnClickListener(this);
    }

    public void setData(int Type) {
        mType = Type;
        switch (Type) {
            case 4:
                getHomeTheme();
                mTitle.setText(getResources().getText(R.string.lively));
                mListView.setAdapter(mRecommendThemeAdapter);
                break;
            case 5:
                mTitle.setText("甜蜜心跳");
                getCommendProducts();
                break;
        }
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        switch (mType) {
            case 4:
                if (mThemeListResponse == null) {
                    return;
                }
                ThemeListResponse.ThemeListData themeListData = mThemeListResponse.data.get(i);
                if (TextUtils.isEmpty(themeListData.url)) {
                    Intent intent = new Intent(getContext(), SpecialDetailsActivity.class);
                    intent.putExtra(SpecialActivity.THEME_TITLE, themeListData);
                    getContext().startActivity(intent);
                } else {
                    getContext().startActivity(new Intent(getContext(), WebViewActivity.class)
                            .putExtra("title", "专题详情")
                            .putExtra("url", themeListData.url));
                }
                break;
            case 5:
                if (mCommendProduct == null) {
                    return;
                }
                CommendProductResponse.CommendProductMode commendProductMode = mCommendProduct.data.get(i);
                Intent intent1 = new Intent(getContext(), ProductDetailsActivity.class);
                intent1.putExtra(ProductDetailsActivity.PRODUCT_ID, commendProductMode.id);
                intent1.putExtra(ProductDetailsActivity.FROM_SOURCE, "0");
                getContext().startActivity(intent1);
                break;
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.vertical_more:
                if (mType == 4) {
                    //todo 进入专题列表
                    getContext().startActivity(new Intent(getContext(), SpecialActivity.class));
                } else if (mType == 5) {
                    Intent intent = new Intent(getContext(), ProductListActivity.class);
                    intent.putExtra(ProductListActivity.IS_NO_TITLE, true);
                    intent.putExtra(ProductListActivity.PRODUCT_LIST_TAG, "5");
                    intent.putExtra(ProductListActivity.PRODUCT_LIST_TITLE, "甜蜜心跳");
                    getContext().startActivity(intent);
                }
                break;
        }
    }

    private void getHomeTheme() {
        RequestParams requestParams = new RequestParams();
        requestParams.put("is_index_page", "1");
        HttpUtil.get(IConstants.sGoodTheme, requestParams, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                ThemeListResponse themeListResponse = JSON.parseObject(response.toString(), ThemeListResponse.class);
                mThemeListResponse = themeListResponse;
                mRecommendThemeAdapter.setThemeListResponse(themeListResponse);
                UiUtils.setListViewHeightBasedOnChilds(mListView);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);
            }
        });
    }

    private void getCommendProducts() {
        final RequestParams requestParams = new RequestParams();
        requestParams.put("tag", "5");
        HttpUtil.get(IConstants.sGoodList, requestParams, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                LogUtils.d("推荐单品返回：", response.toString());
                mCommendProduct = JSON.parseObject(response.toString(), CommendProductResponse.class);

                mListView.setAdapter(new RecommendItemAdapter(getContext(), mCommendProduct.data));
                UiUtils.setListViewHeightBasedOnChilds(mListView);
            }
        });
    }
}
