package com.sensu.android.zimaogou.utils;

import android.content.Context;
import android.util.DisplayMetrics;

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
}
