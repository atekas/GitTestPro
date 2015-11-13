package com.sensu.android.zimaogou.photoalbum;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.os.Handler;
import android.provider.MediaStore;
import com.sensu.android.zimaogou.BaseApplication;
import com.sensu.android.zimaogou.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by k on 2015/6/19.
 *
 * @author vinson
 */
public class LocalAlbumAsyncRequest implements Runnable {

    private ContentResolver mContentResolver;
    private LocationPhotoListener mLocationPhotoListener;

    private List<AlbumInfo> mAlbumInfoList = new ArrayList<AlbumInfo>();
    private Handler mHandler;

    public LocalAlbumAsyncRequest(Context context) {
        mContentResolver = context.getContentResolver();
        mHandler = new Handler();
    }

    public void setLocationPhotoListener(LocationPhotoListener locationPhotoListener) {
        if (locationPhotoListener != null) {
            mLocationPhotoListener = locationPhotoListener;
        }
    }

    public void execute() {
        new Thread(this).start();
    }

    @Override
    public void run() {
        //获取原图
        Cursor cursor = mContentResolver.query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, null, null, null, "date_modified DESC");
        if (cursor != null) {
            String _path = "_data";
            String _album = "bucket_display_name";
            HashMap<String, AlbumInfo> photoAlbumHash = new HashMap<String, AlbumInfo>();
            while (cursor.moveToNext()) {
                String path = cursor.getString(cursor.getColumnIndex(_path));
                String album = cursor.getString(cursor.getColumnIndex(_album));
                PhotoInfo photoInfo = new PhotoInfo();
                photoInfo.setPathPath("file://" + path);
                AlbumInfo albumInfo = photoAlbumHash.get(album);
                if (albumInfo == null) {
                    albumInfo = new AlbumInfo();
                    albumInfo.setAlbumName(album);
                    List<PhotoInfo> photoInfoList = new ArrayList<PhotoInfo>();
                    photoInfoList.add(photoInfo);
                    albumInfo.setList(photoInfoList);
                    photoAlbumHash.put(album, albumInfo);
                    mAlbumInfoList.add(albumInfo);
                } else {
                    albumInfo.getList().add(photoInfo);
                }
            }
            cursor.close();
        }

        //从每一个相册中获取到所有的图片
        List<PhotoInfo> allPhotoList = new ArrayList<PhotoInfo>();
        for (AlbumInfo albumInfo : mAlbumInfoList) {
            allPhotoList.addAll(albumInfo.getList());
        }

        if (allPhotoList.size() > 0) {
            AlbumInfo albumInfo = new AlbumInfo();
            albumInfo.setList(allPhotoList);
            albumInfo.setAlbumName(BaseApplication.getStr(R.string.all_photos));
            mAlbumInfoList.add(0, albumInfo);
        }

        mHandler.post(new Runnable() {
            @Override
            public void run() {
                if (mLocationPhotoListener != null) {
                    mLocationPhotoListener.onGetLocationAlbumFinished(mAlbumInfoList);
                }
            }
        });
    }
}
