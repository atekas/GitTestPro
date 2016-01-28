package com.sensu.android.zimaogou.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.sensu.android.zimaogou.R;
import com.sensu.android.zimaogou.ReqResponse.ProductListResponse;
import com.sensu.android.zimaogou.utils.DisplayUtils;
import com.sensu.android.zimaogou.utils.ImageUtils;
import com.sensu.android.zimaogou.utils.StringUtils;
import com.sensu.android.zimaogou.utils.TextUtils;

/**
 * Created by zhangwentao on 2015/11/18.
 */
public class ProductsDetailsAdapter extends SimpleBaseAdapter {

    private ProductListResponse mProductListResponse;
    private int mPicSize;

    private DisplayImageOptions mProductOptions = new DisplayImageOptions.Builder()
            .considerExifParams(true)
            .imageScaleType(ImageScaleType.EXACTLY)
            .bitmapConfig(Bitmap.Config.RGB_565)
            .resetViewBeforeLoading(true)
            .showImageOnLoading(R.drawable.category_default)
            .showImageOnFail(R.drawable.category_default)
            .showImageForEmptyUri(R.drawable.category_default)
            .resetViewBeforeLoading(true)
            .cacheInMemory(true)
            .build();

    public ProductsDetailsAdapter(Context context) {
        super(context);
        mPicSize = (DisplayUtils.getDisplayWidth()) / 2;
    }

    public void setProductList(ProductListResponse productListResponse) {
        if (mProductListResponse == null) {
            mProductListResponse = productListResponse;
        } else {
            if (productListResponse != null) {
                mProductListResponse.data.addAll(productListResponse.data);
            }
        }
        notifyDataSetChanged();
    }
    public void reFlushProductList(ProductListResponse productListResponse) {
        if(productListResponse == null) {
            mProductListResponse.data.clear();
            return;
        }else {
            mProductListResponse = productListResponse;
        }
        notifyDataSetChanged();
    }
    public void clearData() {
        if (mProductListResponse != null) {
            mProductListResponse.data.clear();
        }
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
            viewHolder.mProductPic.setLayoutParams(new FrameLayout.LayoutParams(mPicSize, mPicSize));
            viewHolder.mSellout = (ImageView) view.findViewById(R.id.sellout);
            viewHolder.mProductName = (TextView) view.findViewById(R.id.product_detail_name);
            viewHolder.mPrice = (TextView) view.findViewById(R.id.product_price);
            viewHolder.mCountryIcon = (ImageView) view.findViewById(R.id.country_icon);
            viewHolder.mVideoIcon = (ImageView) view.findViewById(R.id.video_icon);
            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }
        //todo 塞数据
        ProductListResponse.ProductListData productListData = mProductListResponse.data.get(i);
        viewHolder.mProductName.setText(productListData.name);
        viewHolder.mPrice.setText(StringUtils.deleteZero(productListData.price));
        ImageUtils.displayImage(productListData.country_icon, viewHolder.mCountryIcon);
        if (productListData.media.type.equals("2")) {
            viewHolder.mVideoIcon.setVisibility(View.VISIBLE);
            ImageUtils.displayImage(productListData.media.cover, viewHolder.mProductPic, mProductOptions);
        } else if (productListData.media.type.equals("12")){
            viewHolder.mVideoIcon.setVisibility(View.VISIBLE);
            ImageUtils.displayImage(productListData.media.image.get(0), viewHolder.mProductPic, mProductOptions);
        } else {
            viewHolder.mVideoIcon.setVisibility(View.GONE);
            if(productListData.media.image!=null && productListData.media.image.size()>0){
                ImageUtils.displayImage(productListData.media.image.get(0), viewHolder.mProductPic, mProductOptions);
            }

        }
        if(productListData.num>0){
            viewHolder.mSellout.setVisibility(View.GONE);
        }else{
            viewHolder.mSellout.setVisibility(View.VISIBLE);
        }
        return view;
    }

    public static class ViewHolder {
        ImageView mProductPic;
        TextView mProductName;
        TextView mPrice;
        ImageView mCountryIcon;
        ImageView mVideoIcon,mSellout;

    }
}
