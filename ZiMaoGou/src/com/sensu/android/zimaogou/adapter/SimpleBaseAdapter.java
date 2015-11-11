package com.sensu.android.zimaogou.adapter;

import android.content.Context;
import android.widget.BaseAdapter;

/**
 * Created by Administrator on 2015/11/11.
 *
 * 提供一个默认的行为，防止大家都继承BaseAdapter，产生重复代码的警告
 */
public abstract class SimpleBaseAdapter extends BaseAdapter {

    protected Context mContext;

    protected SimpleBaseAdapter() { }

    protected SimpleBaseAdapter(Context context) {
        mContext = context;
    }

    @Override
    public Object getItem(int i) {
        return i;
    }

    @Override
    public long getItemId(int i) {
        return i;
    }
}
