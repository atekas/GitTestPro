package com.sensu.android.zimaogou.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.sensu.android.zimaogou.R;

/**
 * Created by zhangwentao on 2015/11/17.
 */
public class ClassificationGridAdapter extends SimpleBaseAdapter {

    public ClassificationGridAdapter(Context context) {
        super(context);
    }

    @Override
    public int getCount() {
        return 20;
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
        return view;
    }

    public static class ViewHolder {
        ImageView mImageView;
        TextView mTextView;
    }
}
