package com.sensu.android.zimaogou.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import com.sensu.android.zimaogou.R;
import com.sensu.android.zimaogou.utils.ImageUtils;
import com.sensu.android.zimaogou.widget.VideoLinearLayout;

/**
 * Created by zhangwentao on 2015/12/23.
 */
public class PhotoView extends FrameLayout implements View.OnClickListener {

    public PhotoView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        findViewById(R.id.video_play).setOnClickListener(this);
    }

    public void setPhotoData(String imagePath, boolean isVideo, String videoPath) {
        ImageUtils.displayImage(imagePath, ((ImageView) findViewById(R.id.photo)));
        //todo 有数据时进行逻辑处理
        if (isVideo) {
            findViewById(R.id.video_play).setVisibility(VISIBLE);
            findViewById(R.id.video_view).setVisibility(VISIBLE);

            ((VideoLinearLayout) findViewById(R.id.video_view)).setURL(imagePath, videoPath);

        } else {
            findViewById(R.id.video_play).setVisibility(GONE);
            findViewById(R.id.video_view).setVisibility(GONE);
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.video_play:
                //TODO 播放视频处理
                break;
        }
    }
}
