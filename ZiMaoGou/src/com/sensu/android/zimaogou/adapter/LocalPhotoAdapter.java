package com.sensu.android.zimaogou.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import com.sensu.android.zimaogou.R;
import com.sensu.android.zimaogou.photoalbum.PhotoInfo;
import com.sensu.android.zimaogou.utils.DisplayUtils;
import com.sensu.android.zimaogou.utils.ImageUtils;

import java.util.List;

/**
 * Created by zhangwentao on 2015/11/13.
 */
public class LocalPhotoAdapter extends SimpleBaseAdapter {

    List<PhotoInfo> mLocalPhotoList;

    public LocalPhotoAdapter(Context context) {
        super(context);
    }

    public void setLocalPhotoList(List<PhotoInfo> photoInfoList) {
        mLocalPhotoList = photoInfoList;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        int count = 0;
        if (mLocalPhotoList != null && mLocalPhotoList.size() > 2) {
            count = (mLocalPhotoList.size() - 2) / 3;
            if ((mLocalPhotoList.size() - 2) % 3 != 0) {
                count++;
            }
        }
        return count;
    }

    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {
        ViewHolder viewHolder;
        if (view == null) {
            viewHolder = new ViewHolder();
            view = LayoutInflater.from(mContext).inflate(R.layout.local_photo_item, null);
            viewHolder.mLeftImageView = (ImageView) view.findViewById(R.id.left_img);
            viewHolder.mCenterImageView = (ImageView) view.findViewById(R.id.center_img);
            viewHolder.mRightImageView = (ImageView) view.findViewById(R.id.right_img);
            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }

        setPhotoInfo(position * 3 + 2, viewHolder.mLeftImageView);

        final int centerIndex = position * 3 + 3;
        if (centerIndex < (mLocalPhotoList.size())) {
            viewHolder.mCenterImageView.setVisibility(View.VISIBLE);
            setPhotoInfo(centerIndex, viewHolder.mCenterImageView);
        } else {
            viewHolder.mCenterImageView.setVisibility(View.GONE);
        }

        final int rightIndex = position * 3 + 4;
        if (rightIndex < (mLocalPhotoList.size())) {
            viewHolder.mRightImageView.setVisibility(View.VISIBLE);
            setPhotoInfo(rightIndex, viewHolder.mRightImageView);
        } else {
            viewHolder.mRightImageView.setVisibility(View.GONE);
        }
        return view;
    }

    private void setPhotoInfo(final int index, ImageView photoView) {

        ImageUtils.displayImage(mLocalPhotoList.get(index).getPicPath(), photoView);

        photoView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    private class ViewHolder {
        public ImageView mLeftImageView;
        public ImageView mCenterImageView;
        public ImageView mRightImageView;
    }
}
