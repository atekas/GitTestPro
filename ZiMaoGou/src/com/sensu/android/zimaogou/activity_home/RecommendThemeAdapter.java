package com.sensu.android.zimaogou.activity_home;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import com.sensu.android.zimaogou.R;
import com.sensu.android.zimaogou.ReqResponse.ThemeListResponse;
import com.sensu.android.zimaogou.adapter.SimpleBaseAdapter;
import com.sensu.android.zimaogou.utils.ImageUtils;
import com.sensu.android.zimaogou.utils.StringUtils;


public class RecommendThemeAdapter extends SimpleBaseAdapter {

    private ThemeListResponse mThemeListResponse;

    public RecommendThemeAdapter(Context context) {
        super(context);
    }

    public void setThemeListResponse(ThemeListResponse themeListResponse) {
        mThemeListResponse = themeListResponse;
    }

    @Override
    public int getCount() {
        return mThemeListResponse == null ? 0 : mThemeListResponse.data.size();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder viewHolder;
        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = LayoutInflater.from(mContext).inflate(R.layout.lively_list_item, null);

            viewHolder.mImage = (ImageView) convertView.findViewById(R.id.theme_pic);
            viewHolder.mName = (TextView) convertView.findViewById(R.id.theme_name);
            viewHolder.mLine = convertView.findViewById(R.id.line_bottom);
            viewHolder.mPrice = (TextView) convertView.findViewById(R.id.price);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        ThemeListResponse.ThemeListData themeListData = mThemeListResponse.data.get(position);

        ImageUtils.displayImage(themeListData.media.cover, viewHolder.mImage,ImageUtils.mGroupListOptions);

//        viewHolder.mPrice.setText(StringUtils.deleteZero(themeListData.price) + "元起");
        viewHolder.mName.setText(themeListData.name);

        if (position == mThemeListResponse.data.size() - 1) {
            viewHolder.mLine.setVisibility(View.GONE);
        } else {
            viewHolder.mLine.setVisibility(View.VISIBLE);
        }

        return convertView;
    }


    private class ViewHolder {
        private TextView mName, mPrice;
        private ImageView mImage;
        private View mLine;
    }

}