package com.sensu.android.zimaogou.widget;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.widget.ScrollView;
import android.widget.TextView;
import com.nineoldandroids.animation.ObjectAnimator;
import com.nineoldandroids.view.ViewHelper;
import com.sensu.android.zimaogou.R;
import com.sensu.android.zimaogou.ReqResponse.ProductDetailsResponse;

/**
 *
 *
 */
public class PullPushScrollView extends ScrollView implements View.OnClickListener {

    private ProductDetailsResponse.ProductDetailData mProductDetailData;

    public PullPushScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        findViewById(R.id.is_collect).setOnClickListener(this);
    }

    public void setProductDetailsResponse(ProductDetailsResponse productDetailsResponse) {
        ProductDetailsResponse.ProductDetailData productDetailData = productDetailsResponse.data;
        mProductDetailData = productDetailData;
        ((TextView) findViewById(R.id.product_name)).setText(productDetailData.name);
        ((TextView) findViewById(R.id.now_price)).setText(productDetailData.price);
        ((TextView) findViewById(R.id.price_market)).setText(productDetailData.price_market);
        if (TextUtils.isEmpty(productDetailData.sale_title)) {
            findViewById(R.id.sale_title).setVisibility(GONE);
        } else {
            findViewById(R.id.sale_title).setVisibility(VISIBLE);
            ((TextView) findViewById(R.id.sale_title)).setText(productDetailData.sale_title);
        }
        if (productDetailData.is_favorite.equals("0")) {
            findViewById(R.id.is_collect).setSelected(false);
        } else if (productDetailData.is_favorite.equals("1")) {
            findViewById(R.id.is_collect).setSelected(true);
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.is_collect:
                if (mProductDetailData.is_favorite.equals("0")) {
                    //TODO 进行收藏
                } else if (mProductDetailData.is_favorite.equals("1")) {
                    //todo 取消收藏
                }
                break;
        }
    }
}
