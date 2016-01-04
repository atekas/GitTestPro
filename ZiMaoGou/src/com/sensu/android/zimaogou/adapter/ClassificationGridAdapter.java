package com.sensu.android.zimaogou.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.sensu.android.zimaogou.R;
import com.sensu.android.zimaogou.ReqResponse.ProductClassificationResponse;
import com.sensu.android.zimaogou.utils.ImageUtils;

/**
 * Created by zhangwentao on 2015/11/17.
 */
public class ClassificationGridAdapter extends SimpleBaseAdapter {

    private ProductClassificationResponse.ProductCategory mProductCategory;

    private DisplayImageOptions mCategoryOptions = new DisplayImageOptions.Builder()
            .considerExifParams(true)
            .imageScaleType(ImageScaleType.EXACTLY)
            .bitmapConfig(Bitmap.Config.RGB_565)
            .resetViewBeforeLoading(true)
            .showImageOnLoading(R.drawable.category_default)
            .showImageOnFail(R.drawable.category_default)
            .showImageForEmptyUri(R.drawable.category_default)
            .resetViewBeforeLoading(true)
            .build();

    public ClassificationGridAdapter(Context context) {
        super(context);
    }

    public void setCategoryList(ProductClassificationResponse.ProductCategory categoryList) {
        if (categoryList != mProductCategory) {
            mProductCategory = categoryList;
            notifyDataSetChanged();
        }
    }


    @Override
    public int getCount() {
        return mProductCategory == null ? 0 : mProductCategory.sub.size();
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder viewHolder;
        if (view == null) {
            view = LayoutInflater.from(mContext).inflate(R.layout.classification_grid_item, null);
            viewHolder = new ViewHolder();
            viewHolder.mImageView = (ImageView) view.findViewById(R.id.classification_image);
            viewHolder.mTextView = (TextView) view.findViewById(R.id.second_name);
            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }
        ProductClassificationResponse.CategoryList categoryList = mProductCategory.sub.get(i);
        ImageUtils.displayImage(categoryList.image, viewHolder.mImageView, mCategoryOptions);
        viewHolder.mTextView.setText(categoryList.name);

        return view;
    }

    private class ViewHolder {
        ImageView mImageView;
        TextView mTextView;
    }
}
