package com.sensu.android.zimaogou.activity;

import android.os.Bundle;
import android.widget.ListView;
import com.sensu.android.zimaogou.R;
import com.sensu.android.zimaogou.adapter.SpecialAdapter;

/**
 * Created by zhangwentao on 2015/11/18.
 *
 * 专题页面
 */
public class SpecialActivity extends BaseActivity {

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
    }
}
