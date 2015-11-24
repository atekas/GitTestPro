package com.sensu.android.zimaogou.activity;

import android.os.Bundle;
import android.view.View;
import com.sensu.android.zimaogou.R;
import com.sensu.android.zimaogou.utils.PromptUtils;
import com.sensu.android.zimaogou.widget.ScrollViewContainer;

/**
 * Created by zhangwentao on 2015/11/20.
 */
public class ProductDetailsActivity extends BaseActivity implements View.OnClickListener {

    private ScrollViewContainer mScrollViewContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.product_details_activity);

        initViews();
    }

    private void initViews() {
        mScrollViewContainer = (ScrollViewContainer) findViewById(R.id.scroll_view_container);
        mScrollViewContainer.setOnSlideFinish(new ScrollViewContainer.OnSlideFinish() {
            @Override
            public void slideFinish(boolean isBottomView) {
                if (isBottomView) {
                    PromptUtils.showToast("显示头");
                    findViewById(R.id.scroll_view_title).setVisibility(View.VISIBLE);

                } else {
                    PromptUtils.showToast("隐藏头");
                    findViewById(R.id.scroll_view_title).setVisibility(View.GONE);
                }
            }
        });

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
