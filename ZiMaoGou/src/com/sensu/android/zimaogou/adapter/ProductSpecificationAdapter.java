package com.sensu.android.zimaogou.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.sensu.android.zimaogou.R;
import com.sensu.android.zimaogou.ReqResponse.ProductDetailsResponse;

/**
 * Created by qi.yang on 2015/12/3.
 */
public class ProductSpecificationAdapter extends SimpleBaseAdapter {

    private ProductDetailsResponse.ProductDetailData mProductDetailData;

    public ProductSpecificationAdapter(Context context) {
        super(context);
    }

    public void setProductDetailData(ProductDetailsResponse.ProductDetailData productDetailData) {
        mProductDetailData = productDetailData;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return mProductDetailData == null ? 0 : mProductDetailData.normal_spec.size();
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        if(view == null){
            view = LayoutInflater.from(mContext).inflate(R.layout.product_specification_item,null);
        }

        ProductDetailsResponse.NormalSpec normalSpec = mProductDetailData.normal_spec.get(i);
        ((TextView) view.findViewById(R.id.name)).setText(normalSpec.name);
        ((TextView) view.findViewById(R.id.value)).setText(normalSpec.value);
        return view;
    }
}
