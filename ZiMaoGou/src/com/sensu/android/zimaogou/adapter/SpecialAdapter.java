package com.sensu.android.zimaogou.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.sensu.android.zimaogou.R;
import com.sensu.android.zimaogou.ReqResponse.ThemeListResponse;
import com.sensu.android.zimaogou.utils.ImageUtils;

/**
 * Created by zhangwentao on 2015/11/18.
 */
public class SpecialAdapter extends SimpleBaseAdapter {

    private ThemeListResponse mThemeListResponse;

    public SpecialAdapter(Context context) {
        super(context);
    }

    public void setThemeListData(ThemeListResponse themeListResponse) {
        mThemeListResponse = themeListResponse;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return mThemeListResponse == null ? 0 : mThemeListResponse.data.size();
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder viewHolder;
        if (view == null) {
            view = LayoutInflater.from(mContext).inflate(R.layout.special_list_item, null);
            viewHolder = new ViewHolder();
            viewHolder.mSpecialPic = (ImageView) view.findViewById(R.id.special_pic);
            viewHolder.mName = (TextView) view.findViewById(R.id.name);
            viewHolder.mPrice = (TextView) view.findViewById(R.id.price);
            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }

        ThemeListResponse.ThemeListData themeListData = mThemeListResponse.data.get(i);

        ImageUtils.displayImage(themeListData.media, viewHolder.mSpecialPic);
        viewHolder.mName.setText(themeListData.name);
        viewHolder.mPrice.setText(themeListData.price);

        return view;
    }

    public class ViewHolder {
        ImageView mSpecialPic;
        TextView mPrice;
        TextView mName;
    }
}
