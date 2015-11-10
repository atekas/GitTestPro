package com.sensu.android.zimaogou.utils;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Handler;
import android.widget.Toast;


/**
 * Created by winter on 2015/9/22.
 *
 * @author winter
 */
public class PromptUtils {

    private static Context mContext;
    private static Toast mToast;
    private static ProgressDialog mProgressDialog;
    private static Handler mHandler;
    private static long mLastTime;

    private static boolean isTest = true;

    public static void init(Context context) {
        mContext = context;
        mToast = Toast.makeText(context, "", Toast.LENGTH_SHORT);
        mHandler = new Handler();
    }

    public static void showToast(final String info, final int duration) {
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                mToast.setText(info);
                mToast.setDuration(duration);
                mToast.show();
                mLastTime = System.currentTimeMillis();
            }
        });
    }

    public static boolean isJustNowShowToast() {
        return (System.currentTimeMillis() - mLastTime) < 300;
    }

    public static void showToast(String info) {
        showToast(info, Toast.LENGTH_SHORT);
    }

    public static void showToast(int resId, int duration) {
        showToast(mContext.getString(resId), duration);
    }

    public static void showToast(int resId) {
        showToast(resId, Toast.LENGTH_SHORT);
    }

    public static void showTestToast(String info) {
        if (ConfigUtils.isShowTestToast()) {
            showToast(info, Toast.LENGTH_SHORT);
        }
    }

    public static void showTestToast(int resId) {
        if (ConfigUtils.isShowTestToast()) {
            showToast(resId, Toast.LENGTH_SHORT);
        }
    }

    public static void showProgressDialog(Context context, String info, boolean cancelable, boolean canceledOnTouchOutside) {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.dismiss();
        }
        mProgressDialog = new ProgressDialog(context);
        mProgressDialog.setMessage(info);
        mProgressDialog.setCancelable(cancelable);
        mProgressDialog.setCanceledOnTouchOutside(canceledOnTouchOutside);
        mProgressDialog.show();
    }

    public static void showProgressDialog(Context context, int resId, boolean cancelable, boolean canceledOnTouchOutside) {
        showProgressDialog(context, resId, cancelable, canceledOnTouchOutside);
    }

    public static void showProgressDialog(Context context, int resId, int max, boolean cancelable, boolean canceledOnTouchOutside) {
        showProgressDialog(context, context.getString(resId), max, cancelable, canceledOnTouchOutside);
    }

    public static void showProgressDialog(Context context, String info, int max, boolean cancelable, boolean canceledOnTouchOutside) {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.dismiss();
        }
        mProgressDialog = new ProgressDialog(context);
        mProgressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        mProgressDialog.setMax(max);
        mProgressDialog.setMessage(info);
        mProgressDialog.setCancelable(cancelable);
        mProgressDialog.setCanceledOnTouchOutside(canceledOnTouchOutside);
        mProgressDialog.show();
    }

    public static void setProgress(int progress) {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.setProgress(progress);
        }
    }

    public static void dismissProgressDialog() {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            try {
                mProgressDialog.dismiss();
            } catch (Exception e) { }
        }
        mProgressDialog = null;
    }
}
