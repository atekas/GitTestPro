package com.sensu.android.zimaogou.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.sensu.android.zimaogou.R;
import com.sensu.android.zimaogou.ReqResponse.GroupBuyListResponse;
import com.sensu.android.zimaogou.utils.ImageUtils;
import com.sensu.android.zimaogou.utils.StringUtils;
import com.sensu.android.zimaogou.utils.TextUtils;

/**
 * Created by zhangwentao on 2015/11/19.
 */
public class SpellOrderAdapter extends SimpleBaseAdapter {

    private GroupBuyListResponse mGroupBuyListResponse;

    public SpellOrderAdapter(Context context) {
        super(context);
    }

    public void setGroupBuyList(GroupBuyListResponse groupBuyListResponse) {
        mGroupBuyListResponse = groupBuyListResponse;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return mGroupBuyListResponse == null ? 0 : mGroupBuyListResponse.data.size();
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder viewHolder;
        if (view == null) {
            view = LayoutInflater.from(mContext).inflate(R.layout.spell_order_list_item, null);
            viewHolder = new ViewHolder();
            viewHolder.mImageView = (ImageView) view.findViewById(R.id.group_pic);
            viewHolder.mProductName = (TextView) view.findViewById(R.id.product_name);
            viewHolder.mProductDescribe = (TextView) view.findViewById(R.id.product_describe);
            viewHolder.mGroupPrice = (TextView) view.findViewById(R.id.group_buy_price);
            viewHolder.mOldPrice = (TextView) view.findViewById(R.id.old_price);
            TextUtils.addLineCenter(viewHolder.mOldPrice);
            viewHolder.mGroupPersonSize = (TextView) view.findViewById(R.id.group_person_size);
            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }

        GroupBuyListResponse.GroupBuyListData groupBuyListData = mGroupBuyListResponse.data.get(i);
        viewHolder.mProductName.setText(groupBuyListData.name);
        viewHolder.mProductDescribe.setText(groupBuyListData.content);
        viewHolder.mGroupPrice.setText(StringUtils.deleteZero(groupBuyListData.price));
        viewHolder.mOldPrice.setText("¥" + StringUtils.deleteZero(groupBuyListData.price_market));
        viewHolder.mGroupPersonSize.setText(groupBuyListData.min_num + "人成团");
        ImageUtils.displayImage(groupBuyListData.media, viewHolder.mImageView);

        return view;
    }

    private class ViewHolder {
        ImageView mImageView;
        TextView mProductName;
        TextView mProductDescribe;
        TextView mGroupPrice;
        TextView mOldPrice;
        TextView mGroupPersonSize;
    }
}
