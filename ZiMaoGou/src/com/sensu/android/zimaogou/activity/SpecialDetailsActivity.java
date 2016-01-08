package com.sensu.android.zimaogou.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import com.alibaba.fastjson.JSON;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.sensu.android.zimaogou.IConstants;
import com.sensu.android.zimaogou.R;
import com.sensu.android.zimaogou.ReqResponse.ThemeDetailResponse;
import com.sensu.android.zimaogou.ReqResponse.ThemeListResponse;
import com.sensu.android.zimaogou.adapter.SpecialDetailsAdapter;
import com.sensu.android.zimaogou.utils.HttpUtil;
import com.sensu.android.zimaogou.utils.ImageUtils;
import org.apache.http.Header;
import org.json.JSONObject;

/**
 * Created by zhangwentao on 2015/11/18.
 * <p/>
 * 专题详情页
 */
public class SpecialDetailsActivity extends BaseActivity implements View.OnClickListener, AdapterView.OnItemClickListener {

    private ListView mListView;
    private SpecialDetailsAdapter mSpecialDetailsAdapter;
    private View mHeadView;

    private ThemeListResponse.ThemeListData mThemeListData;

    private ThemeDetailResponse mThemeDetailResponse;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.special_details_activity);

        initViews();
    }

    private void initViews() {

        mThemeListData = (ThemeListResponse.ThemeListData) getIntent().getSerializableExtra(SpecialActivity.THEME_TITLE);

        mListView = (ListView) findViewById(R.id.special_details_list);
        mHeadView = LayoutInflater.from(this).inflate(R.layout.special_details_header, null);
        mListView.addHeaderView(mHeadView);
        layoutTitle();

        mSpecialDetailsAdapter = new SpecialDetailsAdapter(this);
        mListView.setAdapter(mSpecialDetailsAdapter);

        mListView.setOnItemClickListener(this);
        findViewById(R.id.back).setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.back:
                finish();
                break;
        }
    }

    private void layoutTitle() {
        if (mThemeListData != null) {
            ImageUtils.displayImage(mThemeListData.media.cover, (ImageView) mHeadView.findViewById(R.id.special_introduce_pic),ImageUtils.mGroupListOptions);
            ((TextView) findViewById(R.id.special_introduce_title)).setText(mThemeListData.name);
            ((TextView) findViewById(R.id.special_introduce_content)).setText(mThemeListData.content);

            getThemeDetail(mThemeListData.id);
        }else{
            ImageUtils.displayImage(getIntent().getExtras().getString("image"), (ImageView) mHeadView.findViewById(R.id.special_introduce_pic),ImageUtils.mGroupListOptions);
            ((TextView) findViewById(R.id.special_introduce_title)).setText(getIntent().getExtras().getString("name"));
            ((TextView) findViewById(R.id.special_introduce_content)).setText(getIntent().getExtras().getString("content"));

            getThemeDetail(getIntent().getExtras().getString("id"));
        }
    }

    private void getThemeDetail(String id) {
        String url = IConstants.sGoodTheme + "/" + id + "/goods";
        HttpUtil.get(url, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                ThemeDetailResponse themeDetailResponse = JSON.parseObject(response.toString(), ThemeDetailResponse.class);
                mThemeDetailResponse = themeDetailResponse;
                mSpecialDetailsAdapter.setThemeDetailData(themeDetailResponse);
            }
        });
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        if (i > 0) {
            ThemeDetailResponse.ThemeDetailData themeDetailData = mThemeDetailResponse.data.get(i-1);
            Intent intent = new Intent(this, ProductDetailsActivity.class);
            intent.putExtra(ProductDetailsActivity.PRODUCT_ID, themeDetailData.id);
            intent.putExtra(ProductDetailsActivity.FROM_SOURCE, themeDetailData.source);
            startActivity(intent);
        }
    }
}
