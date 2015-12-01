package com.sensu.android.zimaogou.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import com.sensu.android.zimaogou.R;
import com.sensu.android.zimaogou.adapter.SpecialDetailsAdapter;

/**
 * Created by zhangwentao on 2015/11/18.
 *
 * 专题详情页
 */
public class SpecialDetailsActivity extends BaseActivity implements View.OnClickListener, AdapterView.OnItemClickListener {

    private ListView mListView;
    private SpecialDetailsAdapter mSpecialDetailsAdapter;
    private View mHeadView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.special_details_activity);

        initViews();
    }

    private void initViews() {
        mListView = (ListView) findViewById(R.id.special_details_list);
        mHeadView = LayoutInflater.from(this).inflate(R.layout.special_details_header, null);
        mListView.addHeaderView(mHeadView);

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

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        if (i > 1) {
            startActivity(new Intent(this, ProductDetailsActivity.class));
        }
    }
}
