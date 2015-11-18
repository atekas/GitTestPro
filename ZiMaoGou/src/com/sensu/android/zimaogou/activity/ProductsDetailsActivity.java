package com.sensu.android.zimaogou.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.GridView;
import com.sensu.android.zimaogou.R;
import com.sensu.android.zimaogou.adapter.ProductsDetailsAdapter;
import com.sensu.android.zimaogou.utils.DisplayUtils;

/**
 * Created by zhangwentao on 2015/11/17.
 */
public class ProductsDetailsActivity extends BaseActivity implements View.OnClickListener {

    private GridView mGridView;
    private ProductsDetailsAdapter mProductsDetailsAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.products_details_activity);
        intiViews();
    }

    private void intiViews() {
        findViewById(R.id.newest).setOnClickListener(this);
        findViewById(R.id.moods).setOnClickListener(this);
        findViewById(R.id.back).setOnClickListener(this);
        findViewById(R.id.moods_text).setSelected(true);

        mGridView = (GridView) findViewById(R.id.product_list);
        mProductsDetailsAdapter = new ProductsDetailsAdapter(this);
        mGridView.setColumnWidth(DisplayUtils.getDisplayWidth() / 2);
        mGridView.setAdapter(mProductsDetailsAdapter);
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
}
