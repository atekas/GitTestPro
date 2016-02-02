package com.sensu.android.zimaogou.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.alibaba.fastjson.JSON;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.sensu.android.zimaogou.IConstants;
import com.sensu.android.zimaogou.R;
import com.sensu.android.zimaogou.ReqResponse.ProductListResponse;
import com.sensu.android.zimaogou.adapter.ProductsDetailsAdapter;
import com.sensu.android.zimaogou.pullrefresh.PullToRefreshLayout;
import com.sensu.android.zimaogou.utils.HttpUtil;
import com.sensu.android.zimaogou.widget.ExceptionLinearLayout;
import org.apache.http.Header;
import org.json.JSONObject;


/**
 * Created by zhangwentao on 2015/11/17.
 */
public class ProductListActivity extends BaseActivity implements View.OnClickListener, AdapterView.OnItemClickListener
        , PullToRefreshLayout.OnRefreshListener {

    public static final String IS_NO_TITLE = "is_no_title";
    public static final String PRODUCT_LIST_KEYWORD = "product_list_keyword";
    public static final String PRODUCT_LIST_TAG = "product_list_tag";
    public static final String PRODUCT_LIST_CATEGORY = "product_list_category";
    public static final String PRODUCT_LIST_ORDER_BY = "product_list_order_by";
    public static final String PRODUCT_LIST_TITLE = "product_list_title";

    private GridView mGridView;
    private ProductsDetailsAdapter mProductsDetailsAdapter;
    private boolean mIsNoTitle;
    private ProductListResponse mProductListResponse;

    private PullToRefreshLayout mPullToRefreshLayout;
    private LinearLayout mNoProductView;

    private String mTitle;

    private String mKeyword;
    private String mTag;
    private String mCategory;
    private String mOrderBy;
    private int mPageNum = 0;
    private String mLimit = "20";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.product_list_activity);
        intiViews();
    }

    private void intiViews() {

        mIsNoTitle = getIntent().getBooleanExtra(IS_NO_TITLE, false);
        mKeyword = getIntent().getStringExtra(PRODUCT_LIST_KEYWORD);
        mTag = getIntent().getStringExtra(PRODUCT_LIST_TAG);
        mCategory = getIntent().getStringExtra(PRODUCT_LIST_CATEGORY);
        mOrderBy = getIntent().getStringExtra(PRODUCT_LIST_ORDER_BY);
        mTitle = getIntent().getStringExtra(PRODUCT_LIST_TITLE);
        mTitle = mTitle == null ? "" : mTitle;
        if (mIsNoTitle) {
            findViewById(R.id.sort_rules_layout).setVisibility(View.GONE);
        } else {
            findViewById(R.id.sort_rules_layout).setVisibility(View.VISIBLE);
            mOrderBy = "2";
        }

        mNoProductView = (LinearLayout) findViewById(R.id.no_product);
        mPullToRefreshLayout = (PullToRefreshLayout) findViewById(R.id.refresh_view);
        mPullToRefreshLayout.setOnRefreshListener(this);
        findViewById(R.id.newest).setOnClickListener(this);
        findViewById(R.id.moods).setOnClickListener(this);
        findViewById(R.id.back).setOnClickListener(this);
        findViewById(R.id.newest).setSelected(true);

        mGridView = (GridView) findViewById(R.id.product_list);
        mProductsDetailsAdapter = new ProductsDetailsAdapter(this);
        mGridView.setAdapter(mProductsDetailsAdapter);

        ((TextView) findViewById(R.id.product_name)).setText(mTitle);

        getProductList();

        mGridView.setOnItemClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.back:
                finish();
                break;
            case R.id.newest:
                findViewById(R.id.moods_text).setSelected(false);
                findViewById(R.id.newest_text).setSelected(true);
                if (!mOrderBy.equals("2")) {
                    mPageNum = 0;
                    mOrderBy = "2";
                    getProductList();
                }
                break;
            case R.id.moods:
                findViewById(R.id.moods_text).setSelected(true);
                findViewById(R.id.newest_text).setSelected(false);
                if (!mOrderBy.equals("11")) {
                    mPageNum = 0;
                    mOrderBy = "11";
                    getProductList();
                }
                break;
        }
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        ProductListResponse.ProductListData productListData = mProductsDetailsAdapter.getData().data.get(i);
        Intent intent = new Intent(this, ProductDetailsActivity.class);
        intent.putExtra(ProductDetailsActivity.PRODUCT_ID, productListData.id);
        intent.putExtra(ProductDetailsActivity.FROM_SOURCE, "0");
        startActivity(intent);
    }

    private void getProductList() {
        RequestParams requestParams = new RequestParams();
        if (null != mTag) {
            requestParams.put("tag", mTag);
        }

        if (null != mCategory) {
            requestParams.put("category", mCategory);
        }

        if (null != mKeyword) {
            requestParams.put("q", mKeyword);
        }

        if (null != mOrderBy) {
            requestParams.put("order_by", mOrderBy);
        }
        requestParams.put("page_num", mPageNum);
        requestParams.put("limit", mLimit);
        HttpUtil.get(IConstants.sGoodList, requestParams, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                ProductListResponse productListResponse = JSON.parseObject(response.toString(), ProductListResponse.class);
                mProductListResponse = productListResponse;
                if (mPageNum == 0) {
                    mProductsDetailsAdapter.clearData();
                    mPullToRefreshLayout.refreshFinish(PullToRefreshLayout.SUCCEED);
                } else {
                    mPullToRefreshLayout.loadmoreFinish(PullToRefreshLayout.SUCCEED);
                }

                mProductsDetailsAdapter.setProductList(productListResponse);
                if (mPageNum == 0) {
                    if (productListResponse.data.size() == 0) {
                        ((ExceptionLinearLayout)mNoProductView.findViewById(R.id.ll_exception)).setException(IConstants.EXCEPTION_GOODS_IS_NULL);
                        mNoProductView.setVisibility(View.VISIBLE);
                        mPullToRefreshLayout.setVisibility(View.GONE);
                    } else {
                        mNoProductView.setVisibility(View.GONE);
                        mPullToRefreshLayout.setVisibility(View.VISIBLE);
                    }
                }

                mPageNum++;
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);
            }
        });
    }

    @Override
    public void onRefresh(PullToRefreshLayout pullToRefreshLayout) {
        mPageNum = 0;
        getProductList();
    }

    @Override
    public void onLoadMore(PullToRefreshLayout pullToRefreshLayout) {
        getProductList();
    }
}
