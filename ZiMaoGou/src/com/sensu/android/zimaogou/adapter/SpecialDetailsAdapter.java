package com.sensu.android.zimaogou.adapter;

import android.content.Context;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.sensu.android.zimaogou.R;
import com.sensu.android.zimaogou.ReqResponse.ThemeDetailResponse;
import com.sensu.android.zimaogou.utils.ImageUtils;

/**
 * Created by zhangwentao on 2015/11/18.
 */
public class SpecialDetailsAdapter extends SimpleBaseAdapter {

    private ThemeDetailResponse mThemeDetailResponse;

    public SpecialDetailsAdapter(Context context) {
        super(context);
    }

    public void setThemeDetailData(ThemeDetailResponse themeDetailResponse) {
        mThemeDetailResponse = themeDetailResponse;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return mThemeDetailResponse == null ? 0 : mThemeDetailResponse.data.size();
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
            viewHolder.mContent = (TextView) view.findViewById(R.id.content);
            viewHolder.mOldPrice.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }

        ThemeDetailResponse.ThemeDetailData themeDetailData = mThemeDetailResponse.data.get(i);
        ImageUtils.displayImage(themeDetailData.media.image.get(0), viewHolder.mPic);
        viewHolder.mName.setText(themeDetailData.name);
        viewHolder.mNewPrice.setText(themeDetailData.price);
        viewHolder.mContent.setText(themeDetailData.sale_title);

        return view;
    }

    private class ViewHolder {
        ImageView mPic;
        ImageView mVideoIcon;
        TextView mName;
        TextView mNewPrice;
        TextView mOldPrice;
        TextView mContent;
    }
}
