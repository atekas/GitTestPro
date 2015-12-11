package com.sensu.android.zimaogou.widget;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
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
public class PullPushScrollView extends ScrollView {

    public PullPushScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
    }

    public void setProductDetailsResponse(ProductDetailsResponse productDetailsResponse) {
        ProductDetailsResponse.ProductDetailData productDetailData = productDetailsResponse.data;
        ((TextView) findViewById(R.id.product_name)).setText(productDetailData.name);
        ((TextView) findViewById(R.id.now_price)).setText(productDetailData.price);
        ((TextView) findViewById(R.id.price_market)).setText(productDetailData.price_market);
    }

}
