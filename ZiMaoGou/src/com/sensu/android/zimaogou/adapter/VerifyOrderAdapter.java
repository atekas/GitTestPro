package com.sensu.android.zimaogou.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.sensu.android.zimaogou.R;

/**
 * Created by zhangwentao on 2015/11/19.
 */
public class VerifyOrderAdapter extends SimpleBaseAdapter {

    public VerifyOrderAdapter(Context context) {
        super(context);
    }

    @Override
    public int getCount() {
        return 3;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder viewHolder;
        if (view == null) {
            view = LayoutInflater.from(mContext).inflate(R.layout.verify_order_list_item, null);
            viewHolder = new ViewHolder();
            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }
        return view;
    }

    private class ViewHolder {

    }
}
