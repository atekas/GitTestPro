package com.sensu.android.zimaogou.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.sensu.android.zimaogou.R;

/**
 * Created by qi.yang on 2015/12/3.
 */
public class ProductSpecificationAdapter extends SimpleBaseAdapter {

    public ProductSpecificationAdapter(Context context) {
        super(context);
    }

    @Override
    public int getCount() {
        return 9;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        if(view == null){
            view = LayoutInflater.from(mContext).inflate(R.layout.product_specification_item,null);
        }
        return view;
    }
}
