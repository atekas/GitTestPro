package com.sensu.android.zimaogou.activity.tour;

import android.os.Bundle;
import android.view.View;
import android.widget.GridView;
import com.sensu.android.zimaogou.R;
import com.sensu.android.zimaogou.activity.BaseActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhangwentao on 2015/11/13.
 */
public class TourBuySendActivity extends BaseActivity implements View.OnClickListener {

    private GridView mGridView;
    private TourBuySendAdapter mTourBuySendAdapter;

    private List mPhotoList = new ArrayList();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tour_buy_send_activity);

        initViews();
    }

    private void initViews() {
        mGridView = (GridView) findViewById(R.id.grid_view);
        mTourBuySendAdapter = new TourBuySendAdapter(this);
        mGridView.setAdapter(mTourBuySendAdapter);
        mTourBuySendAdapter.setList(mPhotoList);

        findViewById(R.id.cancel).setOnClickListener(this);
        findViewById(R.id.release).setOnClickListener(this);
        findViewById(R.id.choose_country).setOnClickListener(this);
        findViewById(R.id.location_switch).setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.cancel:
                finish();
                break;
            case R.id.release:
                //TODO 发布按钮
                break;
            case R.id.choose_country:
                //TODO 选择国家 弹出对话框
                break;
            case R.id.location_switch:
                //todo 定位开关
                break;
        }
    }
}
