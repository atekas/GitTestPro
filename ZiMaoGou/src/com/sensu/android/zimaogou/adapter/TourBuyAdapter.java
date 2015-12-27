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
import com.sensu.android.zimaogou.utils.ImageUtils;

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

        ImageUtils.displayImage(travelModes.get(i).getAvatar(),viewHolder.img_userHead,ImageUtils.mHeadDefaultOptions);
        viewHolder.tv_userName.setText(travelModes.get(i).getName());
        viewHolder.tv_sendTime.setText(DateUtils.getTimeAgo(travelModes.get(i).getCreated_at()));
        String tag = "";
        for(int j = 0; j <travelModes.get(i).getTag().size(); j++){
            tag += " "+travelModes.get(i).getTag().get(j);
        }
        if(travelModes.get(i).getCategory().equals("1")){
            viewHolder.img_videoPic.setVisibility(View.GONE);
            if(travelModes.get(i).getMedia().image != null&&travelModes.get(i).getMedia().image.size()>0) {
                ImageUtils.displayImage(travelModes.get(i).getMedia().image.get(0), viewHolder.img_contentPic);
            }
        }else{
            viewHolder.img_videoPic.setVisibility(View.VISIBLE);
            ImageUtils.displayImage(travelModes.get(i).getMedia().cover,viewHolder.img_contentPic);
        }
        viewHolder.tv_marks.setText(travelModes.get(i).getCountry()+tag);
        viewHolder.tv_praiseCount.setText(travelModes.get(i).getLike_num());
        viewHolder.tv_reviewCount.setText(travelModes.get(i).getComment_num());
        viewHolder.tv_location.setText(travelModes.get(i).getLocation());
        viewHolder.tv_content.setText(travelModes.get(i).getContent());
        return view;
    }

    private static class ViewHolder {
        ImageView img_userHead,img_contentPic,img_videoPic;
        TextView tv_userName,tv_sendTime,tv_location,tv_content,tv_marks,tv_reviewCount,tv_praiseCount;
    }
}
