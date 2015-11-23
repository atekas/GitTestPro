package com.sensu.android.zimaogou.utils;

import android.app.Activity;
import android.content.Context;
import android.graphics.Rect;
import android.util.DisplayMetrics;
import android.view.View;

/**
 * Created by winter on 2015/9/22.
 *
 * @author winter
 */
public class DisplayUtils {
    private static DisplayMetrics mDisplayMetrics;
    private static int mThumbnailSize;

    public static void init(Context context) {
        mDisplayMetrics = context.getResources().getDisplayMetrics();
    }

    public static int getThumbnailSize() {
        return mThumbnailSize;
    }

    public static int getDisplayWidth() {
        return mDisplayMetrics.widthPixels;
    }

    public static int getDisplayHeight() {
        return mDisplayMetrics.heightPixels;
    }

    public static float getDisplayDensity() {
        return mDisplayMetrics.density;
    }

    public static int getDpi() {
        return mDisplayMetrics.densityDpi;
    }

    public static int dp2px(float dp) {
        return (int) (mDisplayMetrics.density * dp + 0.5f);
    }

    public static int px2dip(int px) {
        return (int) (px / mDisplayMetrics.density + 0.5f);
    }

    public static int getWindowVisibleDisplayHeight(View view) {
        Rect r = new Rect();
        view.getWindowVisibleDisplayFrame(r);
        return (r.bottom - r.top);
    }

    private static int mNotificationHeight;

    private static int mAppWidth;
    private static int mAppHeight;
    private static float mAppRatio;

    /**
     * 必要条件：1.显示通知栏，2.没有显示输入法
     */
    public static void setActivity(Activity activity) {
        if (mNotificationHeight == 0) {
            View view = activity.getWindow().getDecorView();
            mAppWidth = view.getWidth();
            mAppHeight = view.getHeight();
            mAppRatio = (float)mAppWidth / mAppHeight;
            mNotificationHeight = mAppHeight - getWindowVisibleDisplayHeight(view);
        }
    }

    public static int getNotificationHeight() {
        return mNotificationHeight;
    }
}
