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
    private OnCountrySelect mOnCountrySelect;

    public CountryListAdapter(Context context, OnCountrySelect onCountrySelect) {
        super(context);
        mCountryListAdapter2 = new CountryListAdapter2(context);
        mOnCountrySelect = onCountrySelect;
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

    class CountryListAdapter2 extends SimpleBaseAdapter {

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

            view.findViewById(R.id.country_select).setSelected(false);
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    view.findViewById(R.id.country_select).setSelected(true);
                    mOnCountrySelect.onCountrySelect();
                }
            });

            return view;
        }
    }

    public interface OnCountrySelect {
        public void onCountrySelect();
    }
}
