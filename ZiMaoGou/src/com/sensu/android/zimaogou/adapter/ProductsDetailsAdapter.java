package com.sensu.android.zimaogou.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.sensu.android.zimaogou.R;

/**
 * Created by zhangwentao on 2015/11/18.
 */
public class ProductsDetailsAdapter extends SimpleBaseAdapter {

    public ProductsDetailsAdapter(Context context) {
        super(context);
    }

    @Override
    public int getCount() {
        return 30;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder viewHolder;
        if (view == null) {
            view = LayoutInflater.from(mContext).inflate(R.layout.products_details_grid_item, null);
            viewHolder = new ViewHolder();
            viewHolder.mProductPic = (ImageView) view.findViewById(R.id.product_pic);
            viewHolder.mProductName = (TextView) view.findViewById(R.id.product_detail_name);
            viewHolder.mPrice = (TextView) view.findViewById(R.id.product_price);
            viewHolder.mCountryIcon = (ImageView) view.findViewById(R.id.country_icon);
            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }

        //todo 塞数据

        return view;
    }

    public static class ViewHolder {
        ImageView mProductPic;
        TextView mProductName;
        TextView mPrice;
        ImageView mCountryIcon;
    }
}