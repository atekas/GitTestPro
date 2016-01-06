package com.sensu.android.zimaogou.activity_home;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.sensu.android.zimaogou.R;
import com.sensu.android.zimaogou.ReqResponse.CommendProductResponse;
import com.sensu.android.zimaogou.adapter.SimpleBaseAdapter;
import com.sensu.android.zimaogou.utils.ImageUtils;
import com.sensu.android.zimaogou.utils.StringUtils;

import java.util.ArrayList;

/**
 * 推荐单品
 * Created by zhangwentao on 2015/11/24.
 */
public class RecommendItemAdapter extends SimpleBaseAdapter {

    ArrayList<CommendProductResponse.CommendProductMode> commendProductModes = new ArrayList<CommendProductResponse.CommendProductMode>();
    public RecommendItemAdapter(Context context,ArrayList<CommendProductResponse.CommendProductMode> commendProductModes) {
        super(context);
        this.commendProductModes = commendProductModes;
    }

    @Override
    public int getCount() {
        return commendProductModes.size();
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder viewHolder;
        if (view == null) {
            view = LayoutInflater.from(mContext).inflate(R.layout.recommend_item_item, null);
            viewHolder = new ViewHolder();

            viewHolder.mImageView = (ImageView) view.findViewById(R.id.first_frame);
            viewHolder.mCountryIcon = (ImageView) view.findViewById(R.id.country);
            viewHolder.mProductName = (TextView) view.findViewById(R.id.name);
            viewHolder.mPrice = (TextView) view.findViewById(R.id.price);
            viewHolder.mDescribe = (TextView) view.findViewById(R.id.describe);
            viewHolder.mVideoImage = (ImageView) view.findViewById(R.id.video_player);
            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }
        if(commendProductModes.get(i).media.type.equals("1")){
            viewHolder.mVideoImage.setVisibility(View.GONE);

        }else{
            viewHolder.mVideoImage.setVisibility(View.VISIBLE);
        }
        ImageUtils.displayImage(commendProductModes.get(i).broad_image,viewHolder.mImageView,ImageUtils.mGroupListOptions);
        viewHolder.mProductName.setText(commendProductModes.get(i).name);
        viewHolder.mPrice.setText("￥"+ StringUtils.deleteZero(commendProductModes.get(i).price));
        viewHolder.mDescribe.setText(commendProductModes.get(i).sale_title);
        return view;
    }

    private class ViewHolder {
        ImageView mImageView;
        ImageView mCountryIcon;
        ImageView mVideoImage;
        TextView mProductName;
        TextView mPrice;
        TextView mDescribe;
    }
}
