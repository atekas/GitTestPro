package com.sensu.android.zimaogou.activity;

import android.os.Bundle;
import android.view.View;
import com.sensu.android.zimaogou.R;

/**
 * Created by zhangwentao on 2015/11/26.
 */
public class SearchActivity extends BaseActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search_activity);

        initViews();
    }

    private void initViews() {
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
}
