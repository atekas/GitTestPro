package com.sensu.android.zimaogou.handler;


import android.os.Handler;
import android.os.Message;

import java.lang.ref.SoftReference;

/**
 * Created by zhangwentao on 2015/11/6.
 */
public abstract class SoftReferenceActivityHandler<T> extends Handler {
    private SoftReference<T> mSoftReference;

    public SoftReferenceActivityHandler(T weakReference) {
        mSoftReference = new SoftReference<T>(weakReference);
    }

    @Override
    public void handleMessage(Message msg) {
        if (mSoftReference.get() == null) {
            return;
        }
        handlerMessage(mSoftReference.get(), msg);
    }

    protected abstract void handlerMessage(T softReference, Message msg);
}
