package com.sensu.android.zimaogou.activity.tour;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.sensu.android.zimaogou.Mode.CommentMode;
import com.sensu.android.zimaogou.R;
import com.sensu.android.zimaogou.adapter.SimpleBaseAdapter;
import com.sensu.android.zimaogou.utils.DateUtils;
import com.sensu.android.zimaogou.utils.ImageUtils;
import com.sensu.android.zimaogou.utils.TextUtils;

import java.util.ArrayList;

import static com.sensu.android.zimaogou.R.id.review_time;

/**
 * Created by zhangwentao on 2015/11/16.
 */
public class TourBuyDetailsAdapter extends SimpleBaseAdapter {
    private ArrayList<CommentMode> commentModes = new ArrayList<CommentMode>();
    public TourBuyDetailsAdapter (Context context) {
        super(context);
    }
    public void flush(ArrayList<CommentMode> commentModes){
        this.commentModes = commentModes;
        notifyDataSetChanged();
    }
    @Override
    public int getCount() {
        return commentModes.size();
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder viewHolder;
        if (view == null) {
            view = LayoutInflater.from(mContext).inflate(R.layout.tour_details_item, null);
            viewHolder = new ViewHolder();
            viewHolder.mImageView = (ImageView) view.findViewById(R.id.review_user_head_pic);
            viewHolder.mUserName = (TextView) view.findViewById(R.id.review_user_name);
            viewHolder.mTime = (TextView) view.findViewById(review_time);
            viewHolder.mReview = (TextView) view.findViewById(R.id.review_content);
            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }
        ImageUtils.displayImage(commentModes.get(i).getAvatar(), viewHolder.mImageView,ImageUtils.mHeadDefaultOptions);

        if(!TextUtils.isEmpty(commentModes.get(i).getName())) {
            viewHolder.mUserName.setText(commentModes.get(i).getName());
        }else{
            viewHolder.mUserName.setText(commentModes.get(i).getMobile());
        }
        viewHolder.mTime.setText(DateUtils.getTimeAgo(commentModes.get(i).getCreated_at()));
        viewHolder.mReview.setText(commentModes.get(i).getContent());
        return view;
    }

    private class ViewHolder {
        ImageView mImageView;
        TextView mUserName;
        TextView mTime;
        TextView mReview;
    }
}
