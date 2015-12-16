package com.sensu.android.zimaogou.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.sensu.android.zimaogou.R;

/**
 * Created by qi.yang on 2015/11/19.
 */
public class MessageAdapter extends SimpleBaseAdapter {
    public MessageAdapter(Context context) {
        super(context);
    }

    @Override
    public int getCount() {
        return 2;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        view = LayoutInflater.from(mContext).inflate(R.layout.message_list_item,null);

        return view;
    }
}
