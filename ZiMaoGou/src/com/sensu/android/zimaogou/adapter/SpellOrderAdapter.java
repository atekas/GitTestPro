package com.sensu.android.zimaogou.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
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

    private DisplayImageOptions mGroupListOptions = new DisplayImageOptions.Builder()
            .considerExifParams(true)
            .imageScaleType(ImageScaleType.EXACTLY)
            .bitmapConfig(Bitmap.Config.RGB_565)
            .resetViewBeforeLoading(true)
            .showImageOnLoading(R.drawable.group_list_default)
            .showImageOnFail(R.drawable.group_list_default)
            .showImageForEmptyUri(R.drawable.group_list_default)
            .resetViewBeforeLoading(true)
            .build();

    public SpellOrderAdapter(Context context) {
        super(context);
    }

    public void setGroupBuyList(GroupBuyListResponse groupBuyListResponse) {
        if (mGroupBuyListResponse == null) {
            mGroupBuyListResponse = groupBuyListResponse;
        } else {
            mGroupBuyListResponse.data.addAll(groupBuyListResponse.data);
        }
        notifyDataSetChanged();
    }

    public void clearData() {
        if (mGroupBuyListResponse != null) {
            mGroupBuyListResponse.data.clear();
        }
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
            viewHolder.mGroupLinear = (LinearLayout) view.findViewById(R.id.group);
            viewHolder.mStateImage = (ImageView) view.findViewById(R.id.state);
            viewHolder.mGoGroup = (TextView) view.findViewById(R.id.go_group);
            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }

        GroupBuyListResponse.GroupBuyListData groupBuyListData = mGroupBuyListResponse.data.get(i);
        viewHolder.mProductName.setText(groupBuyListData.name);
        viewHolder.mProductDescribe.setText(groupBuyListData.content);
        viewHolder.mGroupPrice.setText(StringUtils.deleteZero(groupBuyListData.price));
        viewHolder.mOldPrice.setText("¥" + StringUtils.deleteZero(groupBuyListData.price_goods));
        viewHolder.mGroupPersonSize.setText(groupBuyListData.min_num + "人成团");
        ImageUtils.displayImage(groupBuyListData.media, viewHolder.mImageView, mGroupListOptions);
        if (groupBuyListData.state.equals("1")) {
            viewHolder.mGroupLinear.setSelected(false);
            viewHolder.mGroupPersonSize.setSelected(false);
            viewHolder.mStateImage.setVisibility(View.GONE);
        } else {
            //2  结束   3  抢光
            viewHolder.mStateImage.setVisibility(View.VISIBLE);
            if (groupBuyListData.state.equals("2")) {
                viewHolder.mStateImage.setImageResource(R.drawable.group_close);
            } else if (groupBuyListData.state.equals("3")) {
                viewHolder.mStateImage.setImageResource(R.drawable.group_finish);
            }
            viewHolder.mGroupLinear.setSelected(true);
            viewHolder.mGroupPersonSize.setSelected(true);
        }

        if (groupBuyListData.is_join.equals("1")) {
            viewHolder.mGoGroup.setText("去购买");
        } else if (groupBuyListData.is_join.equals("0")) {
            viewHolder.mGoGroup.setText("去组团");
        }

        return view;
    }

    private class ViewHolder {
        ImageView mImageView;
        TextView mProductName;
        TextView mProductDescribe;
        TextView mGroupPrice;
        TextView mOldPrice;
        TextView mGroupPersonSize;
        LinearLayout mGroupLinear;
        ImageView mStateImage;
        TextView mGoGroup;
    }
}
