package com.sensu.android.zimaogou.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import com.sensu.android.zimaogou.BaseApplication;
import com.sensu.android.zimaogou.R;
import com.sensu.android.zimaogou.activity.tour.TourBuySendActivity;
import com.sensu.android.zimaogou.activity.tour.TourSendData;
import com.sensu.android.zimaogou.adapter.LocalPhotoAdapter;
import com.sensu.android.zimaogou.photoalbum.AlbumInfo;
import com.sensu.android.zimaogou.photoalbum.LocalAlbumAsyncRequest;
import com.sensu.android.zimaogou.photoalbum.LocationPhotoListener;
import com.sensu.android.zimaogou.photoalbum.PhotoInfo;
import com.sensu.android.zimaogou.utils.ImageUtils;
import com.sensu.android.zimaogou.utils.PromptUtils;
import com.sensu.android.zimaogou.widget.CameraPreviewView;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhangwentao on 2015/11/13.
 */
public class LocalPhotoActivity extends BaseActivity implements LocationPhotoListener, View.OnClickListener, LocalPhotoAdapter.OnPhotoSelectChanged {

    public static final String SELECT_PHOTOS = "select_photos";
    public static final int TAKE_PHOTO_CODE = 1;

    private LocalPhotoAdapter mLocalPhotoAdapter;
    private List<PhotoInfo> mPhotoInfoList = new ArrayList<PhotoInfo>();
    private List<PhotoInfo> mSelectPhotoList = new ArrayList<PhotoInfo>();
    private View mHeadView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.local_photo_activity);

        initViews();
    }

    private void initViews() {

        findViewById(R.id.back).setOnClickListener(this);
        ListView listView = (ListView) findViewById(R.id.local_photo_grid);
        mHeadView = LayoutInflater.from(this).inflate(R.layout.local_photo_head_item, null);
//        listView.addHeaderView(mHeadView);
        mHeadView.findViewById(R.id.right_select).setOnClickListener(this);
        mHeadView.findViewById(R.id.center_select).setOnClickListener(this);
        mHeadView.findViewById(R.id.frame_left).setOnClickListener(this);
        findViewById(R.id.sure).setOnClickListener(this);

        mLocalPhotoAdapter = new LocalPhotoAdapter(this);
        listView.setAdapter(mLocalPhotoAdapter);
        mLocalPhotoAdapter.setOnPhotoSelectChanged(this);

        PromptUtils.showProgressDialog(this, "正在查找图片，请稍后", true, true);
        LocalAlbumAsyncRequest mLocalAlbumAsyncRequest = new LocalAlbumAsyncRequest(this);
        mLocalAlbumAsyncRequest.setLocationPhotoListener(this);
        mLocalAlbumAsyncRequest.execute();
    }

    private void setHeadData() {
        if (mPhotoInfoList.size() == 0) {
            mHeadView.findViewById(R.id.center_img).setVisibility(View.GONE);
            mHeadView.findViewById(R.id.right_img).setVisibility(View.GONE);
            mHeadView.findViewById(R.id.center_select).setVisibility(View.GONE);
            mHeadView.findViewById(R.id.right_select).setVisibility(View.GONE);
        } else if (mPhotoInfoList.size() >= 2) {
            mHeadView.findViewById(R.id.center_img).setVisibility(View.VISIBLE);
            mHeadView.findViewById(R.id.right_img).setVisibility(View.VISIBLE);
            mHeadView.findViewById(R.id.center_select).setVisibility(View.VISIBLE);
            mHeadView.findViewById(R.id.right_select).setVisibility(View.VISIBLE);

            ImageUtils.displayImage(mPhotoInfoList.get(0).getPicPath(), (ImageView) mHeadView.findViewById(R.id.center_img));

            ImageUtils.displayImage(mPhotoInfoList.get(1).getPicPath(), (ImageView) mHeadView.findViewById(R.id.right_img));

            mLocalPhotoAdapter.setLocalPhotoList(mPhotoInfoList);
        } else if (mPhotoInfoList.size() == 1) {
            mHeadView.findViewById(R.id.right_img).setVisibility(View.GONE);
            mHeadView.findViewById(R.id.right_select).setVisibility(View.GONE);

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
        ((TextView) findViewById(R.id.photo_count)).setText("共" + mPhotoInfoList.size() + "张图片");
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

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.back:
                finish();
                break;
            case R.id.center_select:
                if (mPhotoInfoList.get(0).isChoose()) {
                    mHeadView.findViewById(R.id.center_select).setSelected(false);
                    mPhotoInfoList.get(0).setChoose(false);
                    mSelectPhotoList.remove(mPhotoInfoList.get(0));
                } else {
                    mHeadView.findViewById(R.id.center_select).setSelected(true);
                    mPhotoInfoList.get(0).setChoose(true);
                    mSelectPhotoList.add(mPhotoInfoList.get(0));
                }
                setText();
                break;
            case R.id.right_select:
                if (mPhotoInfoList.get(1).isChoose()) {
                    mHeadView.findViewById(R.id.right_select).setSelected(false);
                    mPhotoInfoList.get(1).setChoose(false);
                    mSelectPhotoList.remove(mPhotoInfoList.get(1));
                } else {
                    mHeadView.findViewById(R.id.right_select).setSelected(true);
                    mPhotoInfoList.get(1).setChoose(true);
                    mSelectPhotoList.add(mPhotoInfoList.get(1));
                }
                setText();
                break;
            case R.id.sure:
                if (mSelectPhotoList.size() == 0) {
                    PromptUtils.showToast("请选择照片");
                }

                for (PhotoInfo photoInfo : mSelectPhotoList) {
                    TourSendData.picDataList.add(photoInfo);
                }

                Intent intent = new Intent(this, TourBuySendActivity.class);
                intent.putExtra(TourBuySendActivity.IS_VIDEO, false);
                intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(intent);
                finish();
                break;
            case R.id.frame_left:
                Intent intent1 = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(intent1, TAKE_PHOTO_CODE);
                break;
        }
    }

    @Override
    public void getSelectPhotos(PhotoInfo selectPhoto, boolean isAdd) {
        if (isAdd) {
            mSelectPhotoList.add(selectPhoto);
        } else {
            mSelectPhotoList.remove(selectPhoto);
        }
        setText();
    }

    private void setText() {
        if (mSelectPhotoList.size() == 0) {
            ((TextView) findViewById(R.id.sure)).setText(BaseApplication.getStr(R.string.sure));
        } else {
            ((TextView) findViewById(R.id.sure)).setText(BaseApplication.getStr(R.string.sure) + "(" + mSelectPhotoList.size() + ")");
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == TAKE_PHOTO_CODE) {
                Bundle bundle = data.getExtras();
                Bitmap bitmap = (Bitmap) bundle.get("data");
            }
        }
    }
}
