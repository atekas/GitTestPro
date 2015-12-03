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
import com.sensu.android.zimaogou.photoalbum.PhotoInfo;
import com.sensu.android.zimaogou.utils.ImageUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhangwentao on 2015/11/13.
 */
public class TourBuySendAdapter extends SimpleBaseAdapter {

    private List mList = new ArrayList();
    private Object mAdd;

    public TourBuySendAdapter(Context context) {
        super(context);
    }

    public void setList(List list) {
        if (mList != list) {
            for (int i = 0; i < list.size(); i++) {
                mList.add(list.get(i));
            }
            if (list.size() < 5) {
                mList.add(mAdd);
            }
            notifyDataSetChanged();
        }
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
            ImageUtils.displayImage(((PhotoInfo) object).getPicPath(), viewHolder.mImageView);
        }

        viewHolder.mImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (object == mAdd) {
                    Intent intent = new Intent(mContext, LocalPhotoActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                    mContext.startActivity(intent);
                }
            }
        });


        return view;
    }

    private class ViewHolder {
        ImageView mImageView;
    }
}
