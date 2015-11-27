package com.sensu.android.zimaogou.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.sensu.android.zimaogou.R;

/**
 * Created by zhangwentao on 2015/11/26.
 *
 * 商品评论的adapter
 */
public class ProductEvaluateAdapter extends SimpleBaseAdapter {

    public ProductEvaluateAdapter(Context context) {
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
            view = LayoutInflater.from(mContext).inflate(R.layout.product_evaluate_list_item, null);
            viewHolder = new ViewHolder();
            viewHolder.mFrameLayout1 = (FrameLayout) view.findViewById(R.id.frame_1);
            viewHolder.mFrameLayout2 = (FrameLayout) view.findViewById(R.id.frame_2);
            viewHolder.mFrameLayout3 = (FrameLayout) view.findViewById(R.id.frame_3);
            viewHolder.mFrameLayout4 = (FrameLayout) view.findViewById(R.id.frame_4);

            viewHolder.mImageView1 = (ImageView) view.findViewById(R.id.photo_1);
            viewHolder.mImageView2 = (ImageView) view.findViewById(R.id.photo_2);
            viewHolder.mImageView3 = (ImageView) view.findViewById(R.id.photo_3);
            viewHolder.mImageView4 = (ImageView) view.findViewById(R.id.photo_4);

            viewHolder.mLinearLayout = (LinearLayout) view.findViewById(R.id.photos_layout);

            viewHolder.mUserPhoto = (ImageView) view.findViewById(R.id.user_photo);
            viewHolder.mUserName = (TextView) view.findViewById(R.id.user_name);
            viewHolder.mEvaluateContent = (TextView) view.findViewById(R.id.evaluate_content);

            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }

        return view;
    }

    private class ViewHolder {
        FrameLayout mFrameLayout1;
        FrameLayout mFrameLayout2;
        FrameLayout mFrameLayout3;
        FrameLayout mFrameLayout4;

        ImageView mImageView1;
        ImageView mImageView2;
        ImageView mImageView3;
        ImageView mImageView4;

        LinearLayout mLinearLayout;

        ImageView mUserPhoto;

        TextView mUserName;
        TextView mEvaluateContent;

    }
}
