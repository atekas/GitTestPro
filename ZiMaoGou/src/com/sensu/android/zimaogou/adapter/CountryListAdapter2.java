package com.sensu.android.zimaogou.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.sensu.android.zimaogou.R;

/**
 * Created by zhangwentao on 2015/12/1.
 */
public class CountryListAdapter2 extends SimpleBaseAdapter {

    public CountryListAdapter2(Context context) {
        super(context);
    }

    @Override
    public int getCount() {
        return 4;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {

        if (view == null) {
            view = LayoutInflater.from(mContext).inflate(R.layout.country_select_item2, null);
        }

        return view;
    }
}
