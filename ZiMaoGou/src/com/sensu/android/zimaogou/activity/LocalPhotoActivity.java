package com.sensu.android.zimaogou.activity;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import com.sensu.android.zimaogou.R;
import com.sensu.android.zimaogou.adapter.LocalPhotoAdapter;
import com.sensu.android.zimaogou.photoalbum.AlbumInfo;
import com.sensu.android.zimaogou.photoalbum.LocalAlbumAsyncRequest;
import com.sensu.android.zimaogou.photoalbum.LocationPhotoListener;
import com.sensu.android.zimaogou.photoalbum.PhotoInfo;
import com.sensu.android.zimaogou.utils.ImageUtils;
import com.sensu.android.zimaogou.utils.PromptUtils;
import com.sensu.android.zimaogou.widget.CameraPreviewView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhangwentao on 2015/11/13.
 */
public class LocalPhotoActivity extends BaseActivity implements LocationPhotoListener {

    private LocalPhotoAdapter mLocalPhotoAdapter;
    private List<PhotoInfo> mPhotoInfoList = new ArrayList<PhotoInfo>();
    private List<AlbumInfo> mAlbumInfoList;
    private AlbumInfo mCurrentAlbum;
    private View mHeadView;
    private int mPicSize;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.local_photo_activity);

        initViews();
    }

    private void initViews() {

        ListView listView = (ListView) findViewById(R.id.local_photo_grid);
        mHeadView = LayoutInflater.from(this).inflate(R.layout.local_photo_head_item, null);
        listView.addHeaderView(mHeadView);

        mLocalPhotoAdapter = new LocalPhotoAdapter(this);
        listView.setAdapter(mLocalPhotoAdapter);

        PromptUtils.showProgressDialog(this, "正在查找图片，请稍后", true, true);
        LocalAlbumAsyncRequest mLocalAlbumAsyncRequest = new LocalAlbumAsyncRequest(this);
        mLocalAlbumAsyncRequest.setLocationPhotoListener(this);
        mLocalAlbumAsyncRequest.execute();
    }

    private void setHeadData() {
        if (mPhotoInfoList.size() == 0) {
            mHeadView.findViewById(R.id.center_img).setVisibility(View.GONE);
            mHeadView.findViewById(R.id.right_img).setVisibility(View.GONE);
        } else if (mPhotoInfoList.size() >= 2) {
            mHeadView.findViewById(R.id.center_img).setVisibility(View.VISIBLE);
            mHeadView.findViewById(R.id.right_img).setVisibility(View.VISIBLE);

            ImageUtils.displayImage(mPhotoInfoList.get(0).getPicPath(), (ImageView) mHeadView.findViewById(R.id.center_img));

            ImageUtils.displayImage(mPhotoInfoList.get(1).getPicPath(), (ImageView) mHeadView.findViewById(R.id.right_img));

            mLocalPhotoAdapter.setLocalPhotoList(mPhotoInfoList);
        } else if (mPhotoInfoList.size() == 1) {
            mHeadView.findViewById(R.id.right_img).setVisibility(View.GONE);

            ImageUtils.displayImage(mPhotoInfoList.get(0).getPicPath(), (ImageView) mHeadView.findViewById(R.id.center_img));
        }
    }

    @Override
    public void onGetLocationAlbumFinished(List<AlbumInfo> albumInfoList) {
        PromptUtils.dismissProgressDialog();
        if (albumInfoList == null || albumInfoList.size() == 0) {
            setHeadData();
            return;
        }
        mPhotoInfoList = albumInfoList.get(0).getList();
        setHeadData();
    }

    @Override
    protected void onResume() {
        super.onResume();
        ((CameraPreviewView)mHeadView.findViewById(R.id.camera_view)).onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        ((CameraPreviewView)mHeadView.findViewById(R.id.camera_view)).onPause();
    }
}
