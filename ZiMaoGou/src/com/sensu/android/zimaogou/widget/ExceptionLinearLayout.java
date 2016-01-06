package com.sensu.android.zimaogou.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.sensu.android.zimaogou.IConstants;
import com.sensu.android.zimaogou.R;

import java.util.ArrayList;

/**
 * Created by qi.yang on 2016/1/6.
 */
public class ExceptionLinearLayout extends LinearLayout {
    public ExceptionLinearLayout(Context context) {
        super(context);
    }

    public ExceptionLinearLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }
    View left_line,right_line;
    ImageView mExceptionImageView;
    TextView mExceptionTextView;
    Button bt_reload;
    String exceptionMessage = "";
    int exceptionImageResource = R.drawable.exception_bag;
    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        mExceptionImageView = (ImageView) findViewById(R.id.img_exception);
        mExceptionTextView = (TextView) findViewById(R.id.tv_exception);
        left_line = findViewById(R.id.left_line);
        right_line = findViewById(R.id.right_line);
        bt_reload = (Button) findViewById(R.id.bt_reload);
    }

    /**
     * 无数据Exception
     * @param exceptionCode
     */
    public void setException(int exceptionCode){

        left_line.setVisibility(View.VISIBLE);
        right_line.setVisibility(View.VISIBLE);
        bt_reload.setVisibility(View.GONE);


        switch (exceptionCode){
            case IConstants.EXCEPTION_NO_INTERNET:
                exceptionMessage = "您的网络开了小差哦";
                exceptionImageResource = R.drawable.exception_internet;
                break;
            case IConstants.EXCEPTION_SHOP_IS_NULL:
                exceptionMessage = "您的购物车还没有商品";
                exceptionImageResource = R.drawable.exception_bag;
                break;
            case IConstants.EXCEPTION_ORDER_IS_NULL:
                exceptionMessage = "您还没有订单";
                exceptionImageResource = R.drawable.exception_order;
                break;
            case IConstants.EXCEPTION_COUPON_IS_NULL:
                exceptionMessage = "您还没有优惠券";
                exceptionImageResource = R.drawable.exception_order;
                break;
            case IConstants.EXCEPTION_ADDRESS_IS_NULL:
                exceptionMessage = "您还没有设置收货地址";
                exceptionImageResource = R.drawable.exception_order;
                break;
            case IConstants.EXCEPTION_GOODS_IS_NULL:
                exceptionMessage = "您还没有收藏的商品";
                exceptionImageResource = R.drawable.exception_order;
                break;
            case IConstants.EXCEPTION_TIPS_IS_NULL:
                exceptionMessage = "您还没有收藏的帖子";
                exceptionImageResource = R.drawable.exception_order;
                break;

        }
        mExceptionImageView.setImageResource(exceptionImageResource);
        mExceptionTextView.setText(exceptionMessage);
    }

    /**
     * 无网络Exception
     * @param listener
     */
    public void setNoInternet(View.OnClickListener listener){
        left_line.setVisibility(View.GONE);
        right_line.setVisibility(View.GONE);
        bt_reload.setVisibility(View.VISIBLE);
        bt_reload.setOnClickListener(listener);
        exceptionMessage = "您的网络开了小差哦";
        exceptionImageResource = R.drawable.exception_internet;
        mExceptionImageView.setImageResource(exceptionImageResource);
        mExceptionTextView.setText(exceptionMessage);
    }
}
