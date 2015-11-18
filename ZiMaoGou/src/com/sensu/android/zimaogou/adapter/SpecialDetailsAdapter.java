package com.sensu.android.zimaogou.adapter;

import android.content.Context;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.sensu.android.zimaogou.R;

/**
 * Created by zhangwentao on 2015/11/18.
 */
public class SpecialDetailsAdapter extends SimpleBaseAdapter {

    public SpecialDetailsAdapter(Context context) {
        super(context);
    }

    @Override
    public int getCount() {
        return 10;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder viewHolder;
        if (view == null) {
            view = LayoutInflater.from(mContext).inflate(R.layout.special_details_list_item, null);
            viewHolder = new ViewHolder();
            viewHolder.mPic = (ImageView) view.findViewById(R.id.pic);
            viewHolder.mVideoIcon = (ImageView) view.findViewById(R.id.video_icon);
            viewHolder.mName = (TextView) view.findViewById(R.id.name);
            viewHolder.mNewPrice = (TextView) view.findViewById(R.id.new_price);
            viewHolder.mOldPrice = (TextView) view.findViewById(R.id.old_price);
            viewHolder.mOldPrice.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }

        return view;
    }

    private class ViewHolder {
        ImageView mPic;
        ImageView mVideoIcon;
        TextView mName;
        TextView mNewPrice;
        TextView mOldPrice;
    }
}
