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
import com.sensu.android.zimaogou.utils.StringUtils;
import com.sensu.android.zimaogou.utils.TextUtils;

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
            TextUtils.addLineCenter(viewHolder.mOldPrice);
            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }

        ThemeDetailResponse.ThemeDetailData themeDetailData = mThemeDetailResponse.data.get(i);
        if (themeDetailData.media.type.equals("12")) {
            viewHolder.mVideoIcon.setVisibility(View.VISIBLE);
        } else {
            viewHolder.mVideoIcon.setVisibility(View.GONE);
        }
        ImageUtils.displayImage(themeDetailData.media.image.get(0), viewHolder.mPic);
        viewHolder.mName.setText(themeDetailData.name);
        viewHolder.mNewPrice.setText("¥" + StringUtils.deleteZero(themeDetailData.price));
        viewHolder.mContent.setText(themeDetailData.sale_title);
        viewHolder.mOldPrice.setText("¥" + StringUtils.deleteZero(themeDetailData.price_market));

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
