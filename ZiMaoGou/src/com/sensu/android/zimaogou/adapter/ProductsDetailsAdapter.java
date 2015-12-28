package com.sensu.android.zimaogou.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.sensu.android.zimaogou.R;
import com.sensu.android.zimaogou.ReqResponse.ProductListResponse;
import com.sensu.android.zimaogou.utils.DisplayUtils;
import com.sensu.android.zimaogou.utils.ImageUtils;

/**
 * Created by zhangwentao on 2015/11/18.
 */
public class ProductsDetailsAdapter extends SimpleBaseAdapter {

    private ProductListResponse mProductListResponse;
    private int mPicSize;

    public ProductsDetailsAdapter(Context context) {
        super(context);
        mPicSize = (DisplayUtils.getDisplayWidth()) / 2;
    }

    public void setProductList(ProductListResponse productListResponse) {
        if (productListResponse != mProductListResponse) {
            mProductListResponse = productListResponse;
        }
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return mProductListResponse == null ? 0 : mProductListResponse.data.size();
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder viewHolder;
        if (view == null) {
            view = LayoutInflater.from(mContext).inflate(R.layout.products_details_grid_item, null);
            viewHolder = new ViewHolder();
            viewHolder.mProductPic = (ImageView) view.findViewById(R.id.product_pic);
            viewHolder.mProductPic.setLayoutParams(new LinearLayout.LayoutParams(mPicSize, mPicSize));

            viewHolder.mProductName = (TextView) view.findViewById(R.id.product_detail_name);
            viewHolder.mPrice = (TextView) view.findViewById(R.id.product_price);
            viewHolder.mCountryIcon = (ImageView) view.findViewById(R.id.country_icon);
            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }
        //todo 塞数据
        ProductListResponse.ProductListData productListData = mProductListResponse.data.get(i);
        viewHolder.mProductName.setText(productListData.name);
        viewHolder.mPrice.setText(productListData.price);
        if (productListData.media.type.equals("1")) {
            ImageUtils.displayImage(productListData.media.image.get(0), viewHolder.mProductPic);
        } else {
            ImageUtils.displayImage(productListData.media.cover, viewHolder.mProductPic);
        }

        return view;
    }

    public static class ViewHolder {
        ImageView mProductPic;
        TextView mProductName;
        TextView mPrice;
        ImageView mCountryIcon;
    }
}
