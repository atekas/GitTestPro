package com.sensu.android.zimaogou.activity;

import android.os.Bundle;
import android.text.TextUtils;
import com.sensu.android.zimaogou.R;
import com.sensu.android.zimaogou.utils.ImageUtils;
import com.sensu.android.zimaogou.widget.CropImageView;

/**
 * Created by zhangwentao on 2015/11/26.
 *
 * 展示图片可进行放大查看
 */
public class ShowImageActivity extends BaseActivity {

    public static final String IMAGE_URL = "image_url";
    private CropImageView mCropImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.show_image_activity);

        String url = getIntent().getStringExtra(IMAGE_URL);

        mCropImageView = (CropImageView) findViewById(R.id.scalable_watch_pic);

        if (!TextUtils.isEmpty(url)) {
            ImageUtils.displayImage(url, mCropImageView);
        }
    }
}
