package com.sensu.android.zimaogou.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.sensu.android.zimaogou.R;

/**
 * Created by zhangwentao on 2015/11/19.
 */
public class SpellOrderAdapter extends SimpleBaseAdapter {

    public SpellOrderAdapter(Context context) {
        super(context);
    }

    @Override
    public int getCount() {
        return 5;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder viewHolder;
        if (view == null) {
            view = LayoutInflater.from(mContext).inflate(R.layout.spell_order_list_item, null);
            viewHolder = new ViewHolder();
            viewHolder.mImageView = (ImageView) view.findViewById(R.id.group_pic);
            viewHolder.mProductName = (TextView) view.findViewById(R.id.product_name);
            viewHolder.mProductDescribe = (TextView) view.findViewById(R.id.product_describe);
            viewHolder.mGroupPrice = (TextView) view.findViewById(R.id.group_buy_price);
            viewHolder.mOldPrice = (TextView) view.findViewById(R.id.old_price);
            viewHolder.mGroupPersonSize = (TextView) view.findViewById(R.id.group_person_size);
            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }
        return view;
    }

    private class ViewHolder {
        ImageView mImageView;
        TextView mProductName;
        TextView mProductDescribe;
        TextView mGroupPrice;
        TextView mOldPrice;
        TextView mGroupPersonSize;
    }
}
