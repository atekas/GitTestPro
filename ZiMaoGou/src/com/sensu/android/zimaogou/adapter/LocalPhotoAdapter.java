package com.sensu.android.zimaogou.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import com.sensu.android.zimaogou.R;
import com.sensu.android.zimaogou.photoalbum.PhotoInfo;
import com.sensu.android.zimaogou.utils.ImageUtils;

import java.util.List;

/**
 * Created by zhangwentao on 2015/11/13.
 */
public class LocalPhotoAdapter extends SimpleBaseAdapter {

    private List<PhotoInfo> mLocalPhotoList;
    private OnPhotoSelectChanged mOnPhotoSelectChanged;

    public LocalPhotoAdapter(Context context) {
        super(context);
    }

    public void setLocalPhotoList(List<PhotoInfo> photoInfoList) {
        mLocalPhotoList = photoInfoList;
        notifyDataSetChanged();
    }

    public void setOnPhotoSelectChanged(OnPhotoSelectChanged onPhotoSelectChanged) {
        if (mOnPhotoSelectChanged == null) {
            mOnPhotoSelectChanged = onPhotoSelectChanged;
        }
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

            viewHolder.mLeftChoose = (ImageView) view.findViewById(R.id.left_select);
            viewHolder.mCenterChoose = (ImageView) view.findViewById(R.id.center_select);
            viewHolder.mRightChoose = (ImageView) view.findViewById(R.id.right_select);

            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }

        final int leftIndex = position * 3 + 2;
        setPhotoInfo(leftIndex, viewHolder.mLeftImageView);
        setPhotoSelect(leftIndex, viewHolder.mLeftChoose);

        final int centerIndex = position * 3 + 3;
        if (centerIndex < (mLocalPhotoList.size())) {
            viewHolder.mCenterImageView.setVisibility(View.VISIBLE);
            viewHolder.mCenterChoose.setVisibility(View.VISIBLE);
            setPhotoInfo(centerIndex, viewHolder.mCenterImageView);
            setPhotoSelect(centerIndex, viewHolder.mCenterChoose);
        } else {
            viewHolder.mCenterImageView.setVisibility(View.GONE);
            viewHolder.mCenterChoose.setVisibility(View.GONE);
        }

        final int rightIndex = position * 3 + 4;
        if (rightIndex < (mLocalPhotoList.size())) {
            viewHolder.mRightImageView.setVisibility(View.VISIBLE);
            viewHolder.mRightChoose.setVisibility(View.VISIBLE);
            setPhotoInfo(rightIndex, viewHolder.mRightImageView);
            setPhotoSelect(rightIndex, viewHolder.mRightChoose);
        } else {
            viewHolder.mRightImageView.setVisibility(View.GONE);
            viewHolder.mRightChoose.setVisibility(View.GONE);
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

    private void setPhotoSelect(final int index, final ImageView photoSelect) {
        if (mLocalPhotoList.get(index).isChoose()) {
            photoSelect.setSelected(true);
        } else {
            photoSelect.setSelected(false);
        }

        photoSelect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean isAdd;
                if (mLocalPhotoList.get(index).isChoose()) {
                    isAdd = false;
                } else {
                    isAdd = true;
                }
                mOnPhotoSelectChanged.getSelectPhotos(mLocalPhotoList.get(index), isAdd);
                mLocalPhotoList.get(index).setChoose(isAdd);
                photoSelect.setSelected(isAdd);
            }
        });
    }

    public interface OnPhotoSelectChanged {
        public void getSelectPhotos(PhotoInfo selectPhoto, boolean isAdd);
    }

    private class ViewHolder {
        public ImageView mLeftImageView;
        public ImageView mCenterImageView;
        public ImageView mRightImageView;

        ImageView mLeftChoose;
        ImageView mCenterChoose;
        ImageView mRightChoose;
    }
}
