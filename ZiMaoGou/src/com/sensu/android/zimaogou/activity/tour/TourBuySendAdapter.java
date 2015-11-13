package com.sensu.android.zimaogou.activity.tour;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import com.sensu.android.zimaogou.R;
import com.sensu.android.zimaogou.activity.LocalPhotoActivity;
import com.sensu.android.zimaogou.adapter.SimpleBaseAdapter;
import com.sensu.android.zimaogou.utils.ImageUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhangwentao on 2015/11/13.
 */
public class TourBuySendAdapter extends SimpleBaseAdapter {

    private List mList = new ArrayList();
    private Object mAdd;

    public TourBuySendAdapter(Context context){
        super(context);
    }

    public void setList(List list) {
        for (Object object : list) {
            mList.add(object);
        }
        mList.add(mAdd);
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return mList == null ? 0 : mList.size();
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder viewHolder;
        if (view == null) {
            view = LayoutInflater.from(mContext).inflate(R.layout.tour_send_grid_item, null);
            viewHolder = new ViewHolder();
            viewHolder.mImageView = (ImageView) view.findViewById(R.id.image);
            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }

        final Object object = mList.get(i);

        if (object == mAdd) {
            viewHolder.mImageView.setImageResource(R.drawable.add_photos);
        } else {
        }

        viewHolder.mImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (object == mAdd) {
                    mContext.startActivity(new Intent(mContext, LocalPhotoActivity.class));
                }
            }
        });


        return view;
    }

    private class ViewHolder {
        ImageView mImageView;
    }
}
