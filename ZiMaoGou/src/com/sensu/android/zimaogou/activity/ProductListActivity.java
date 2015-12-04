package com.sensu.android.zimaogou.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import com.sensu.android.zimaogou.R;
import com.sensu.android.zimaogou.adapter.ProductsDetailsAdapter;

/**
 * Created by zhangwentao on 2015/11/17.
 */
public class ProductListActivity extends BaseActivity implements View.OnClickListener, AdapterView.OnItemClickListener {

    public static final String IS_NO_TITLE = "is_no_title";
    private GridView mGridView;
    private ProductsDetailsAdapter mProductsDetailsAdapter;
    private boolean mIsNoTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.product_list_activity);
        intiViews();
    }

    private void intiViews() {

        mIsNoTitle = getIntent().getBooleanExtra(IS_NO_TITLE, false);
        if (mIsNoTitle) {
            findViewById(R.id.sort_rules_layout).setVisibility(View.GONE);
        } else {
            findViewById(R.id.sort_rules_layout).setVisibility(View.VISIBLE);
        }

        findViewById(R.id.newest).setOnClickListener(this);
        findViewById(R.id.moods).setOnClickListener(this);
        findViewById(R.id.back).setOnClickListener(this);
        findViewById(R.id.moods_text).setSelected(true);

        mGridView = (GridView) findViewById(R.id.product_list);
        mProductsDetailsAdapter = new ProductsDetailsAdapter(this);
        mGridView.setAdapter(mProductsDetailsAdapter);

        mGridView.setOnItemClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.back:
                finish();
                break;
            case R.id.newest:
                findViewById(R.id.moods_text).setSelected(false);
                findViewById(R.id.newest_text).setSelected(true);
                break;
            case R.id.moods:
                findViewById(R.id.moods_text).setSelected(true);
                findViewById(R.id.newest_text).setSelected(false);
                break;
        }
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        startActivity(new Intent(this, ProductDetailsActivity.class));
    }
}
