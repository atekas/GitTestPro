package com.sensu.android.zimaogou.handler;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;

import java.lang.ref.SoftReference;

/**
 * Created zhangwentao
 * 会对回调接口使用软引用
 */
public class SoftReferenceHandler extends Handler {

    public interface MsgCallback {
        void handleMessage(Message msg);
    }

    private SoftReference<MsgCallback> mCallbackRef;

    public SoftReferenceHandler() {}

    public SoftReferenceHandler(MsgCallback callback) {
        mCallbackRef = new SoftReference<MsgCallback>(callback);
    }

    public SoftReferenceHandler(MsgCallback callback, Looper looper) {
        super(looper);
        mCallbackRef = new SoftReference<MsgCallback>(callback);
    }

    @Override
    public final void handleMessage(Message msg) {
        MsgCallback callback = mCallbackRef != null ? mCallbackRef.get() : null;
        if (callback != null) {
            callback.handleMessage(msg);
        }
    }

    public final boolean postSoftReferenceDelayed(Runnable r, long delayMillis) {
        return postDelayed(new SoftReferenceRunnable(r), delayMillis);
    }

    private class SoftReferenceRunnable implements Runnable {

        private SoftReference<Runnable> mRunnableRef;

        SoftReferenceRunnable(Runnable runnable) {
            mRunnableRef = new SoftReference<Runnable>(runnable);
        }

        @Override
        public void run() {
            Runnable runnable = mRunnableRef.get();
            if (runnable != null) {
                runnable.run();
            }
        }
    }
}
