package com.sensu.android.zimaogou.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import com.nostra13.universalimageloader.cache.memory.impl.WeakMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.nostra13.universalimageloader.core.listener.ImageLoadingProgressListener;
import com.sensu.android.zimaogou.BaseApplication;
import com.sensu.android.zimaogou.R;
import com.sensu.android.zimaogou.activity.BaseActivity;

import java.io.IOException;

/**
 * Created by zhangwentao on 2015/11/4.
 *
 * 显示图片工具类
 */
public class ImageUtils {

    private static DisplayImageOptions mDefaultOptions = new DisplayImageOptions.Builder()
            .considerExifParams(true)
            .imageScaleType(ImageScaleType.EXACTLY)
            .bitmapConfig(Bitmap.Config.RGB_565)
            .resetViewBeforeLoading(true)
//            .showImageOnLoading(R.drawable.bg_movie_add_shoot)
//            .showImageOnFail(R.drawable.bg_movie_add_shoot)
//            .showImageForEmptyUri(R.drawable.bg_movie_add_shoot)
            .cacheInMemory(true)
            .resetViewBeforeLoading(true)
            .cacheOnDisk(true)
            .build();
    public static DisplayImageOptions mItemTopOptions = new DisplayImageOptions.Builder()
            .considerExifParams(true)
            .imageScaleType(ImageScaleType.EXACTLY)
            .bitmapConfig(Bitmap.Config.RGB_565)
            .resetViewBeforeLoading(true)
            .showImageOnLoading(R.drawable.home_item_top_default)
            .showImageOnFail(R.drawable.home_item_top_default)
            .showImageForEmptyUri(R.drawable.home_item_top_default)
            .cacheInMemory(true)
            .resetViewBeforeLoading(true)
            .cacheOnDisk(true)
            .build();
    public static DisplayImageOptions mGroupListOptions = new DisplayImageOptions.Builder()
            .considerExifParams(true)
            .imageScaleType(ImageScaleType.EXACTLY)
            .bitmapConfig(Bitmap.Config.RGB_565)
            .resetViewBeforeLoading(true)
            .showImageOnLoading(R.drawable.group_list_default)
            .showImageOnFail(R.drawable.group_list_default)
            .showImageForEmptyUri(R.drawable.group_list_default)
            .cacheInMemory(true)
            .resetViewBeforeLoading(true)
            .cacheOnDisk(true)
            .build();
    public static DisplayImageOptions mHeadDefaultOptions = new DisplayImageOptions.Builder()
            .considerExifParams(true)
            .imageScaleType(ImageScaleType.EXACTLY)
            .bitmapConfig(Bitmap.Config.RGB_565)
            .resetViewBeforeLoading(true)
//            .showImageOnLoading(R.drawable.bg_movie_add_shoot)
            .showImageOnFail(R.drawable.head_pic_default)
            .showImageForEmptyUri(R.drawable.head_pic_default)
            .cacheInMemory(true)
            .resetViewBeforeLoading(true)
            .cacheOnDisk(true)
            .build();
    //图片工具类初始化
    public static void init(Context context) {

        ImageLoaderConfiguration configuration = new ImageLoaderConfiguration
                .Builder(context)
                .memoryCache(new WeakMemoryCache())
                .threadPoolSize(5)
                .build();

        ImageLoader.getInstance().init(configuration);
    }

    /**
     * 通过URL获取到图片的bitmap
     * @param url 图片URL
     * @return bitmap
     */

    public static Bitmap getBitmapFromUrl(String url) {
        return ImageLoader.getInstance().loadImageSync(url, mDefaultOptions);
    }

    /**
     * 显示图片  加载默认配置图片
     * @param uri 图片的地址
     * uri 类型
     * String imageUri = "http://site.com/image.png"; // from Web
     * String imageUri = "file:///mnt/sdcard/image.png"; // from SD card
     * String imageUri = "content://media/external/audio/albumart/13"; // from content provider
     * String imageUri = "assets://image.png"; // from assets
     * String imageUri = "drawable://" + R.drawable.image; // from drawables (only images, non-9patch)
     * @param imageView 承载图的imageView
     */
    public static void displayImage(String uri, ImageView imageView) {
        if(com.sensu.android.zimaogou.utils.TextUtils.isEmpty(uri)){
            imageView.setImageResource(R.drawable.head_pic_default);
            return;
        }
        displayImage(uri, imageView, null);
    }

    /**
     * 加载自定义配置的一个图片的
     * @param uri 图片url地址
     * @param imageView 承载图片的iamgeView
     * @param displayImageOptions DisplayImageOptions配置信息
     */
    public static void displayImage(String uri, ImageView imageView, DisplayImageOptions displayImageOptions) {
        if (displayImageOptions != null) {
            displayImage(uri, imageView, displayImageOptions, null);
        } else {
            displayImage(uri, imageView, mDefaultOptions, null);
        }
    }

    /**
     * 图片加载带监听
     * @param uri 图片地址
     * @param imageView 承载图片的imageView
     * @param displayImageOptions 图片配置信息
     * @param imageLoadingListener 下载监听
     */
    public static void displayImage(String uri, ImageView imageView,
                                    DisplayImageOptions displayImageOptions,
                                    ImageLoadingListener imageLoadingListener) {
        displayImage(uri, imageView, displayImageOptions, imageLoadingListener, null);
    }

    /**
     * 图片加载监听带进度条
     * @param uri 图片地址
     * @param imageView 承载图片的imageView
     * @param displayImageOptions 图片配置信息
     * @param imageLoadingListener 图片下载监听
     * @param imageLoadingProgressListener 图片进度信息
     */
    public static void displayImage(String uri, ImageView imageView,
                                    DisplayImageOptions displayImageOptions,
                                    ImageLoadingListener imageLoadingListener,
                                    ImageLoadingProgressListener imageLoadingProgressListener) {
        if (TextUtils.isEmpty(uri) || imageView == null) {
            LogUtils.d("ImageUtil", "参数为空");
            return;
        }
        ImageLoader.getInstance().displayImage(uri, imageView, displayImageOptions, imageLoadingListener, imageLoadingProgressListener);
    }
    /**
     * 图片旋转
     * @param tmpBitmap
     * @param degrees
     * @return
     */
    public static Bitmap rotateToDegrees(Bitmap tmpBitmap, float degrees) {
        Matrix matrix = new Matrix();
        matrix.reset();
        matrix.setRotate(degrees);
        return tmpBitmap =
                Bitmap.createBitmap(tmpBitmap, 0, 0, tmpBitmap.getWidth(), tmpBitmap.getHeight(), matrix, true);
    }

    /**
     * 读取照片exif信息中的旋转角度
     * @param path 照片路径
     * @return角度
     */
    public static int readPictureDegree(String path) {
        int degree = 0;
        try {
            ExifInterface exifInterface = new ExifInterface(path);
            int orientation =
                    exifInterface.getAttributeInt(ExifInterface.TAG_ORIENTATION,
                            ExifInterface.ORIENTATION_NORMAL);
            switch (orientation) {
                case ExifInterface.ORIENTATION_ROTATE_90:
                    degree = 90;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_180:
                    degree = 180;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_270:
                    degree = 270;
                    break;
            }
        }  catch (IOException e) {
            e.printStackTrace();
        }
        return degree;
    }

}
