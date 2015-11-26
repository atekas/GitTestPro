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
import com.nineoldandroids.animation.ObjectAnimator;
import com.nineoldandroids.view.ViewHelper;
import com.sensu.android.zimaogou.R;

/**
 *
 *
 */
public class PullPushScrollView extends ScrollView {

    public PullPushScrollView(Context context) {
        super(context);
    }

    public PullPushScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public PullPushScrollView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();

    }


}
