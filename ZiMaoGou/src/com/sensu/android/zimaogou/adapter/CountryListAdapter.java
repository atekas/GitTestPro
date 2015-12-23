package com.sensu.android.zimaogou.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;
import com.sensu.android.zimaogou.Mode.CountryMode;
import com.sensu.android.zimaogou.Mode.LandMode;
import com.sensu.android.zimaogou.R;

import java.util.ArrayList;

/**
 * Created by zhangwentao on 2015/12/1.
 */
public class CountryListAdapter extends SimpleBaseAdapter {

    private OnCountrySelect mOnCountrySelect;
    ArrayList<LandMode> landModes = new ArrayList<LandMode>();
    TextView tv_country ;
    public CountryListAdapter(Context context, OnCountrySelect onCountrySelect,TextView tv_country,ArrayList<LandMode> landModes) {
        super(context);
        this.landModes = landModes;
        this.tv_country = tv_country;
        mOnCountrySelect = onCountrySelect;

    }

    @Override
    public int getCount() {
        return landModes.size();
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
        viewHolder.mAreaText.setText(landModes.get(i).getName());
        viewHolder.mCountryList.setAdapter(new CountryListAdapter2(mContext,landModes.get(i).getSub()));
        return view;
    }

    private class ViewHolder{
        TextView mAreaText;
        ListView mCountryList;
    }

    class CountryListAdapter2 extends SimpleBaseAdapter {
        ArrayList<CountryMode> countryModes = new ArrayList<CountryMode>();
        public CountryListAdapter2(Context context,ArrayList<CountryMode> countryModes) {
            super(context);
            this.countryModes = countryModes;
        }

        @Override
        public int getCount() {
            return countryModes.size();
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            final CountryViewHolder holder;
            if (view == null) {
                holder = new CountryViewHolder();
                view = LayoutInflater.from(mContext).inflate(R.layout.country_select_item2, null);
                holder.area_name = (TextView) view.findViewById(R.id.area_name);
                holder.bottom_line = view.findViewById(R.id.bottom_line);
                view.setTag(holder);
            }else{
                holder = (CountryViewHolder) view.getTag();
            }
            holder.area_name.setText(countryModes.get(i).getName());
            if (i == getCount() - 1) {
                holder.bottom_line.setVisibility(View.GONE);
            } else {
                holder.bottom_line.setVisibility(View.VISIBLE);
            }

            view.findViewById(R.id.country_select).setSelected(false);
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    view.findViewById(R.id.country_select).setSelected(true);
                    mOnCountrySelect.onCountrySelect();
                    tv_country.setText(holder.area_name.getText().toString());
                }
            });

            return view;
        }
    }

    private class CountryViewHolder{
        public TextView area_name;
        public View bottom_line;
    }
    public interface OnCountrySelect {
        public void onCountrySelect();
    }
}
