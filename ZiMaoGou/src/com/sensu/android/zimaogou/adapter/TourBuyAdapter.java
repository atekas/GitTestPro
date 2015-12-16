package com.sensu.android.zimaogou.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.sensu.android.zimaogou.Mode.TravelMode;
import com.sensu.android.zimaogou.R;
import com.sensu.android.zimaogou.utils.DateUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

/**
 * Created by zhangwentao on 2015/11/12.
 */
public class TourBuyAdapter extends SimpleBaseAdapter {
    ArrayList<TravelMode> travelModes = new ArrayList<TravelMode>();

    public TourBuyAdapter(Context context) {
        super(context);
    }
    public void flush(ArrayList<TravelMode> travelModes){
        this.travelModes = travelModes;
        notifyDataSetChanged();
    }
    @Override
    public int getCount() {
        return travelModes.size();
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder viewHolder;
        if (view == null) {
            view = LayoutInflater.from(mContext).inflate(R.layout.tour_buy_list_item, null);
            viewHolder = new ViewHolder();
            viewHolder.img_contentPic = (ImageView) view.findViewById(R.id.content_pic);
            viewHolder.img_userHead = (ImageView) view.findViewById(R.id.user_head_pic);
            viewHolder.img_videoPic = (ImageView) view.findViewById(R.id.video_icon);
            viewHolder.tv_content = (TextView) view.findViewById(R.id.content_text);
            viewHolder.tv_location = (TextView) view.findViewById(R.id.tv_location);
            viewHolder.tv_marks = (TextView) view.findViewById(R.id.marks);
            viewHolder.tv_praiseCount = (TextView) view.findViewById(R.id.praise_count);
            viewHolder.tv_reviewCount = (TextView) view.findViewById(R.id.review_count);
            viewHolder.tv_sendTime = (TextView) view.findViewById(R.id.send_time);
            viewHolder.tv_userName = (TextView) view.findViewById(R.id.user_name);


            //TODO 查找控件
            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }
        viewHolder.tv_userName.setText(travelModes.get(i).getCreatorname());
        viewHolder.tv_sendTime.setText(DateUtils.getTimeAgo(travelModes.get(i).getCreated_at()));
        viewHolder.tv_marks.setText(travelModes.get(i).getCountry()+" "+travelModes.get(i).getTag());
        viewHolder.tv_praiseCount.setText(travelModes.get(i).getLike_num());
        viewHolder.tv_reviewCount.setText(travelModes.get(i).getComment_num());
        return view;
    }

    private static class ViewHolder {
        ImageView img_userHead,img_contentPic,img_videoPic;
        TextView tv_userName,tv_sendTime,tv_location,tv_content,tv_marks,tv_reviewCount,tv_praiseCount;
    }
}
