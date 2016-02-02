package com.sensu.android.zimaogou.view;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.sensu.android.zimaogou.R;
import com.sensu.android.zimaogou.activity.ShowImageActivity;
import com.sensu.android.zimaogou.utils.DisplayUtils;
import com.sensu.android.zimaogou.utils.ImageUtils;
import com.sensu.android.zimaogou.widget.VideoLinearLayout;

/**
 * Created by zhangwentao on 2015/12/23.
 */
public class PhotoView extends FrameLayout implements View.OnClickListener {

    private String mImageUrl;

    public PhotoView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public DisplayImageOptions mProductDetailOptions = new DisplayImageOptions.Builder()
            .considerExifParams(true)
            .imageScaleType(ImageScaleType.EXACTLY)
            .bitmapConfig(Bitmap.Config.RGB_565)
            .resetViewBeforeLoading(true)
            .showImageOnLoading(R.drawable.product_details_default)
            .showImageOnFail(R.drawable.product_details_default)
            .showImageForEmptyUri(R.drawable.product_details_default)
            .cacheInMemory(true)
            .resetViewBeforeLoading(true)
            .cacheOnDisk(true)
            .build();

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        findViewById(R.id.photo).setOnClickListener(this);
    }

    public void setPhotoData(String imagePath, boolean isVideo, String videoPath) {
        mImageUrl = imagePath;

        //todo 有数据时进行逻辑处理
        if (isVideo) {
            findViewById(R.id.video_layout).setVisibility(VISIBLE);
            findViewById(R.id.photo).setVisibility(GONE);
            ((VideoLinearLayout) findViewById(R.id.video_layout)).setURL(imagePath, videoPath);
        } else {
            findViewById(R.id.photo).setVisibility(VISIBLE);
            findViewById(R.id.video_layout).setVisibility(GONE);
            ImageUtils.displayImage(imagePath, ((ImageView) findViewById(R.id.photo)), mProductDetailOptions);
        }
    }

    public void playVideo() {
        ((VideoLinearLayout) findViewById(R.id.video_layout)).playing();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
//            case R.id.video_play:
//                //TODO 播放视频处理
//                break;
            case R.id.photo:
                Intent intent = new Intent(getContext(), ShowImageActivity.class);
                intent.putExtra(ShowImageActivity.IMAGE_URL, mImageUrl);
                getContext().startActivity(intent);
                break;
        }
    }
}
