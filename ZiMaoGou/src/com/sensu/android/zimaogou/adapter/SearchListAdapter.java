package com.sensu.android.zimaogou.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.sensu.android.zimaogou.R;

/**
 * Created by zhangwentao on 2015/11/18.
 */
public class SearchListAdapter extends SimpleBaseAdapter {


    public SearchListAdapter(Context context) {
        super(context);
    }

    @Override
    public int getCount() {
        return 6;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        if (view == null) {
            view = LayoutInflater.from(mContext).inflate(R.layout.search_listview_item, null);
        } else {
        }

        //todo 塞数据

        return view;
    }


}
