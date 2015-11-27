package com.sensu.android.zimaogou.activity.home;

import android.content.Context;
import android.content.Intent;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;
import com.sensu.android.zimaogou.R;
import com.sensu.android.zimaogou.activity.ProductCommentActivity;
import com.sensu.android.zimaogou.activity.ProductDetailsActivity;

/**
 * Created by zhangwentao on 2015/11/24.
 */
public class HomeGrid extends LinearLayout implements View.OnClickListener {

    public HomeGrid(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();

        findViewById(R.id.product_1).setOnClickListener(this);
        findViewById(R.id.product_2).setOnClickListener(this);
        findViewById(R.id.product_3).setOnClickListener(this);
        findViewById(R.id.product_4).setOnClickListener(this);
        findViewById(R.id.product_5).setOnClickListener(this);
        findViewById(R.id.product_6).setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.product_1:
                getContext().startActivity(new Intent(getContext(), ProductDetailsActivity.class));
                break;
            case R.id.product_2:
                getContext().startActivity(new Intent(getContext(), ProductCommentActivity.class));
                break;
            case R.id.product_3:
                getContext().startActivity(new Intent(getContext(), ProductDetailsActivity.class));
                break;
            case R.id.product_4:
                getContext().startActivity(new Intent(getContext(), ProductDetailsActivity.class));
                break;
            case R.id.product_5:
                getContext().startActivity(new Intent(getContext(), ProductDetailsActivity.class));
                break;
            case R.id.product_6:
                getContext().startActivity(new Intent(getContext(), ProductDetailsActivity.class));
                break;
        }
    }
}
