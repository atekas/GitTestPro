package com.sensu.android.zimaogou.activity_home;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.sensu.android.zimaogou.R;
import com.sensu.android.zimaogou.adapter.SimpleBaseAdapter;

/**
 * Created by zhangwentao on 2015/11/24.
 */
public class RecommendItemAdapter extends SimpleBaseAdapter {

    public RecommendItemAdapter(Context context) {
        super(context);
    }

    @Override
    public int getCount() {
        return 4;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder viewHolder;
        if (view == null) {
            view = LayoutInflater.from(mContext).inflate(R.layout.recommend_item_item, null);
            viewHolder = new ViewHolder();

            viewHolder.mImageView = (ImageView) view.findViewById(R.id.first_frame);
            viewHolder.mCountryIcon = (ImageView) view.findViewById(R.id.country);
            viewHolder.mProductName = (TextView) view.findViewById(R.id.name);
            viewHolder.mPrice = (TextView) view.findViewById(R.id.price);
            viewHolder.mDescribe = (TextView) view.findViewById(R.id.describe);

            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }



        return view;
    }

    private class ViewHolder {
        ImageView mImageView;
        ImageView mCountryIcon;
        TextView mProductName;
        TextView mPrice;
        TextView mDescribe;
    }
}
