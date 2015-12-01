package com.sensu.android.zimaogou.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;
import com.sensu.android.zimaogou.R;

/**
 * Created by zhangwentao on 2015/12/1.
 */
public class CountryListAdapter extends SimpleBaseAdapter {

    private CountryListAdapter2 mCountryListAdapter2;

    public CountryListAdapter(Context context) {
        super(context);
        mCountryListAdapter2 = new CountryListAdapter2(context);
    }

    @Override
    public int getCount() {
        return 5;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder viewHolder;
        if (view == null) {
            view = LayoutInflater.from(mContext).inflate(R.layout.country_select_item, null);
            viewHolder = new ViewHolder();
            viewHolder.mAreaText = (TextView) view.findViewById(R.id.area_name);
            viewHolder.mCountryList = (ListView) view.findViewById(R.id.country_list2);
            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }
        viewHolder.mCountryList.setAdapter(mCountryListAdapter2);
        return view;
    }

    private class ViewHolder{
        TextView mAreaText;
        ListView mCountryList;
    }
}
