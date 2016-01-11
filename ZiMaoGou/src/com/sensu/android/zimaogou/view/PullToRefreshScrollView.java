package com.sensu.android.zimaogou.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ScrollView;
import com.sensu.android.zimaogou.pullrefresh.Pullable;

/**
 * Created by zhangwentao on 2016/1/11.
 */
public class PullToRefreshScrollView extends ScrollView implements Pullable {

    public PullToRefreshScrollView(Context context) {
        super(context);
    }

    public PullToRefreshScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public PullToRefreshScrollView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    public boolean canPullDown() {
        if (getScrollY() == 0)
            return true;
        else
            return false;
    }

    @Override
    public boolean canPullUp() {
        if (getScrollY() >= (getChildAt(0).getHeight() - getMeasuredHeight()))
            return true;
        else
            return false;
    }
}
