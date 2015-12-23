package com.sensu.android.zimaogou.widget;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.ViewConfiguration;

/**
 * Created by k on 14-9-25.
 *
 * @author vinson
 */
public class ProductDetailViewPager extends ViewPager {

    private float mXDown;// 记录手指按下时的横坐标。
    private float mYDown;// 记录手指按下时的纵坐标。
    private boolean mHorizontalScroll = false;// 当前是否是viewpager滑动
    private boolean mVerticalScroll = false;
    private int mTouchSlop;

    protected boolean mFullScreen;

    public ProductDetailViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
        mTouchSlop = ViewConfiguration.get(context).getScaledTouchSlop();
    }

    private boolean isChecked() {
        return mHorizontalScroll || mVerticalScroll;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, mFullScreen ? heightMeasureSpec : widthMeasureSpec);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        getParent().requestDisallowInterceptTouchEvent(true);
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                getParent().requestDisallowInterceptTouchEvent(true);
                mXDown = ev.getX();
                mYDown = ev.getY();
                mHorizontalScroll = false;
                mVerticalScroll = false;
                break;
            case MotionEvent.ACTION_MOVE:
                if (!isChecked()) {
                    float x = ev.getX();
                    float y = ev.getY();
                    if (Math.abs(x - mXDown) > mTouchSlop) {
                        if (Math.abs(y - mYDown) > Math.abs(x - mXDown)) {
                            getParent().requestDisallowInterceptTouchEvent(false);
                        }
                        mHorizontalScroll = true;
                    } else if (Math.abs(y - mYDown) > mTouchSlop) {
                        if (Math.abs(y - mYDown) > Math.abs(x - mXDown)) {
                            getParent().requestDisallowInterceptTouchEvent(false);
                        }
                        mVerticalScroll = true;
                    }
                }
                if (mHorizontalScroll) {
                    getParent().requestDisallowInterceptTouchEvent(true);
                }
                break;
            default:
                getParent().requestDisallowInterceptTouchEvent(false);
                break;
        }
        return super.dispatchTouchEvent(ev);
    }
}
