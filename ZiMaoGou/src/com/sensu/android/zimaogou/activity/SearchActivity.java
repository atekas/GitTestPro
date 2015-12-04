package com.sensu.android.zimaogou.activity;

import android.os.Bundle;
import android.view.View;
import com.sensu.android.zimaogou.R;
import com.sensu.android.zimaogou.adapter.SearchGirdAdapter;
import com.sensu.android.zimaogou.adapter.SearchListAdapter;
import com.sensu.android.zimaogou.widget.NoScrollGridView;
import com.sensu.android.zimaogou.widget.NoScrollListView;

/**
 * Created by zhangwentao on 2015/11/26.
 */
public class SearchActivity extends BaseActivity implements View.OnClickListener {
    private NoScrollGridView mSearchGirdView;
    private NoScrollListView mSearchListView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search_activity);

        initViews();
    }

    private void initViews() {
        findViewById(R.id.back).setOnClickListener(this);
        mSearchGirdView = (NoScrollGridView) findViewById(R.id.gv_hot_search);
        mSearchListView = (NoScrollListView) findViewById(R.id.lv_search);

        mSearchGirdView.setAdapter(new SearchGirdAdapter(this));
        mSearchListView.setAdapter(new SearchListAdapter(this));
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.back:
                finish();
                break;
        }
    }
}
