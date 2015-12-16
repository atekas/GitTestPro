package com.sensu.android.zimaogou.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.TextView;
import com.alibaba.fastjson.JSON;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.sensu.android.zimaogou.IConstants;
import com.sensu.android.zimaogou.R;
import com.sensu.android.zimaogou.ReqResponse.ProductListResponse;
import com.sensu.android.zimaogou.adapter.ProductsDetailsAdapter;
import com.sensu.android.zimaogou.utils.HttpUtil;
import com.sensu.android.zimaogou.utils.PromptUtils;
import org.apache.http.Header;
import org.json.JSONObject;


/**
 * Created by zhangwentao on 2015/11/17.
 */
public class ProductListActivity extends BaseActivity implements View.OnClickListener, AdapterView.OnItemClickListener {

    public static final String IS_NO_TITLE = "is_no_title";
    private GridView mGridView;
    private ProductsDetailsAdapter mProductsDetailsAdapter;
    private boolean mIsNoTitle;

    private String mKeyword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.product_list_activity);
        intiViews();
    }

    private void intiViews() {

        mIsNoTitle = getIntent().getBooleanExtra(IS_NO_TITLE, false);
        mKeyword = getIntent().getStringExtra(SearchActivity.KEYWORD);
        if (mIsNoTitle) {
            findViewById(R.id.sort_rules_layout).setVisibility(View.GONE);
        } else {
            findViewById(R.id.sort_rules_layout).setVisibility(View.VISIBLE);
        }

        findViewById(R.id.newest).setOnClickListener(this);
        findViewById(R.id.moods).setOnClickListener(this);
        findViewById(R.id.back).setOnClickListener(this);
        findViewById(R.id.moods_text).setSelected(true);

        mGridView = (GridView) findViewById(R.id.product_list);
        mProductsDetailsAdapter = new ProductsDetailsAdapter(this);
        mGridView.setAdapter(mProductsDetailsAdapter);

        ((TextView) findViewById(R.id.product_name)).setText(mKeyword);

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
                break;
            case R.id.moods:
                findViewById(R.id.moods_text).setSelected(true);
                findViewById(R.id.newest_text).setSelected(false);
                break;
        }
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        startActivity(new Intent(this, ProductDetailsActivity.class));
    }

    private void getProductList() {
        RequestParams requestParams = new RequestParams();
//        requestParams.put("tag", "");
        requestParams.put("q", mKeyword);
//        requestParams.put("order_by", "");
//        requestParams.put("page_num", "");
//        requestParams.put("limit", "");
        HttpUtil.get(IConstants.sGoodList, requestParams, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                ProductListResponse productListResponse = JSON.parseObject(response.toString(), ProductListResponse.class);
                mProductsDetailsAdapter.setProductList(productListResponse);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);
            }
        });
    }
}
