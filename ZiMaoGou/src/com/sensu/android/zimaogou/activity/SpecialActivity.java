package com.sensu.android.zimaogou.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import com.alibaba.fastjson.JSON;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.sensu.android.zimaogou.IConstants;
import com.sensu.android.zimaogou.R;
import com.sensu.android.zimaogou.ReqResponse.ThemeListResponse;
import com.sensu.android.zimaogou.adapter.SpecialAdapter;
import com.sensu.android.zimaogou.utils.HttpUtil;
import org.apache.http.Header;
import org.json.JSONObject;

/**
 * Created by zhangwentao on 2015/11/18.
 *
 * 专题页面
 */
public class SpecialActivity extends BaseActivity implements AdapterView.OnItemClickListener, View.OnClickListener {

    private ListView mSpecialList;
    private SpecialAdapter mSpecialAdapter;

    private ThemeListResponse mThemeListResponse;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.special_activity);

        initViews();
    }

    private void initViews() {
        getSpecialList();
        mSpecialList = (ListView) findViewById(R.id.special_list);
        mSpecialAdapter = new SpecialAdapter(this);
        mSpecialList.setAdapter(mSpecialAdapter);

        mSpecialList.setOnItemClickListener(this);

        findViewById(R.id.back).setOnClickListener(this);
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        ThemeListResponse.ThemeListData themeListData = mThemeListResponse.data.get(i + 1);
        Intent intent = new Intent(this, SpecialDetailsActivity.class);
        intent.putExtra("000", themeListData);
        startActivity(intent);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.back:
                finish();
                break;
        }
    }

    private void getSpecialList() {
        HttpUtil.get(IConstants.sGoodTheme, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                ThemeListResponse themeListResponse = JSON.parseObject(response.toString(), ThemeListResponse.class);
                mThemeListResponse = themeListResponse;
                mSpecialAdapter.setThemeListData(themeListResponse);
            }
        });
    }
}
