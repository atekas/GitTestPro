package com.sensu.android.zimaogou.activity.tour;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ListView;
import com.sensu.android.zimaogou.R;
import com.sensu.android.zimaogou.activity.BaseActivity;

/**
 * Created by zhangwentao on 2015/11/16.
 */
public class TourBuyDetailsActivity extends BaseActivity implements View.OnClickListener {

    private ListView mTourDetailsListView;
    private TourBuyDetailsAdapter mTourBuyDetailsAdapter;

    private View mHeaderView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.tour_buy_details_activity);

        initViews();
    }

    private void initViews() {
        mTourDetailsListView = (ListView) findViewById(R.id.review_details);
        mHeaderView = LayoutInflater.from(this).inflate(R.layout.tour_details_header, null);
        mTourDetailsListView.addHeaderView(mHeaderView);
        mTourBuyDetailsAdapter = new TourBuyDetailsAdapter(this);
        mTourDetailsListView.setAdapter(mTourBuyDetailsAdapter);

        findViewById(R.id.back).setOnClickListener(this);

        initHeader();
    }

    private void initHeader() {

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
