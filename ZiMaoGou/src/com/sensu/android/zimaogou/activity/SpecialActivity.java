package com.sensu.android.zimaogou.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import com.sensu.android.zimaogou.R;
import com.sensu.android.zimaogou.adapter.SpecialAdapter;

/**
 * Created by zhangwentao on 2015/11/18.
 *
 * 专题页面
 */
public class SpecialActivity extends BaseActivity implements AdapterView.OnItemClickListener, View.OnClickListener {

    private ListView mSpecialList;
    private SpecialAdapter mSpecialAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.special_activity);

        initViews();
    }

    private void initViews() {
        mSpecialList = (ListView) findViewById(R.id.special_list);
        mSpecialAdapter = new SpecialAdapter(this);
        mSpecialList.setAdapter(mSpecialAdapter);

        mSpecialList.setOnItemClickListener(this);

        findViewById(R.id.back).setOnClickListener(this);
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        startActivity(new Intent(this, SpecialDetailsActivity.class));
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
