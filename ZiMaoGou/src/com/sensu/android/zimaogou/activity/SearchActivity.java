package com.sensu.android.zimaogou.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.TextView;
import com.alibaba.fastjson.JSON;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.sensu.android.zimaogou.IConstants;
import com.sensu.android.zimaogou.R;
import com.sensu.android.zimaogou.ReqResponse.HotKeywordsResponse;
import com.sensu.android.zimaogou.adapter.SearchGirdAdapter;
import com.sensu.android.zimaogou.adapter.SearchListAdapter;
import com.sensu.android.zimaogou.external.greendao.helper.GDSearchKeywordHelper;
import com.sensu.android.zimaogou.external.greendao.model.SearchKeyword;
import com.sensu.android.zimaogou.utils.HttpUtil;
import com.sensu.android.zimaogou.widget.HotKeywordsListView;
import com.sensu.android.zimaogou.widget.NoScrollGridView;
import com.sensu.android.zimaogou.widget.NoScrollListView;
import org.apache.http.Header;
import org.json.JSONObject;

import java.util.List;

/**
 * Created by zhangwentao on 2015/11/26.
 */
public class SearchActivity extends BaseActivity implements View.OnClickListener, AdapterView.OnItemClickListener {

//    private NoScrollGridView mSearchGirdView;
    private NoScrollListView mSearchListView;

    private SearchListAdapter mSearchListAdapter;

    private List<SearchKeyword> mSearchKeywordList;

    private EditText mSearchEdit;
    private TextView mClearHistoryText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search_activity);

        initViews();
    }

    private void initViews() {

        getHotKeyword();

        mSearchListView = (NoScrollListView) findViewById(R.id.lv_search);
        mSearchListAdapter = new SearchListAdapter(this);

        mSearchEdit = (EditText) findViewById(R.id.search_edit);
        //屏蔽回车
        mSearchEdit.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                return (keyEvent.getKeyCode() == KeyEvent.KEYCODE_ENTER);
            }
        });

        mClearHistoryText = (TextView) findViewById(R.id.clear_history);

        findViewById(R.id.back).setOnClickListener(this);
        findViewById(R.id.search).setOnClickListener(this);
        mClearHistoryText.setOnClickListener(this);

        mSearchListView.setAdapter(mSearchListAdapter);
        mSearchListView.setOnItemClickListener(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        updateLayout();
    }

    private void updateLayout() {
        mSearchKeywordList = GDSearchKeywordHelper.getInstance(this).getSearchKeyword();
        if (mSearchKeywordList == null || mSearchKeywordList.size() == 0) {
            findViewById(R.id.search_recently).setVisibility(View.GONE);
            mClearHistoryText.setVisibility(View.GONE);
        } else {
            mSearchListAdapter.setSearchKeywordList(mSearchKeywordList);
            findViewById(R.id.search_recently).setVisibility(View.VISIBLE);
            mClearHistoryText.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.back:
                finish();
                break;
            case R.id.search:
                String keyword = mSearchEdit.getText().toString().trim();
                if (!TextUtils.isEmpty(keyword)) {
                    Intent intent = new Intent(this, ProductListActivity.class);
                    intent.putExtra(ProductListActivity.IS_NO_TITLE, true);
                    intent.putExtra(ProductListActivity.PRODUCT_LIST_KEYWORD, keyword);
                    intent.putExtra(ProductListActivity.PRODUCT_LIST_TITLE, keyword);
                    startActivity(intent);
                    SearchKeyword searchKeyword = new SearchKeyword();
                    searchKeyword.setKeyword(keyword);
                    GDSearchKeywordHelper.getInstance(this).insertKeyword(searchKeyword);
                }
                break;
            case R.id.clear_history:
                GDSearchKeywordHelper.getInstance(this).deleteKeyword();
                findViewById(R.id.search_recently).setVisibility(View.GONE);
                mClearHistoryText.setVisibility(View.GONE);
                mSearchListView.setVisibility(View.GONE);
                break;
        }
    }

    private void getHotKeyword() {
        HttpUtil.get(IConstants.sHotKeywords, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                HotKeywordsResponse hotKeywordsResponse = JSON.parseObject(response.toString(), HotKeywordsResponse.class);
                ((HotKeywordsListView) findViewById(R.id.gv_hot_search)).setHotKeywords(hotKeywordsResponse);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);
            }
        });
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        if (mSearchKeywordList == null) {
            return;
        }
        String keyword = mSearchKeywordList.get(i).getKeyword();
        Intent intent = new Intent(this, ProductListActivity.class);
        intent.putExtra(ProductListActivity.IS_NO_TITLE, true);
        intent.putExtra(ProductListActivity.PRODUCT_LIST_KEYWORD, keyword);
        intent.putExtra(ProductListActivity.PRODUCT_LIST_TITLE, keyword);
        startActivity(intent);
    }
}
