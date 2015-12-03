package com.sensu.android.zimaogou.activity.tour;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.*;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.sensu.android.zimaogou.R;
import com.sensu.android.zimaogou.activity.BaseActivity;
import com.sensu.android.zimaogou.activity.LocalPhotoActivity;
import com.sensu.android.zimaogou.activity.fragment.TourBuyFragment;
import com.sensu.android.zimaogou.adapter.SimpleBaseAdapter;
import com.sensu.android.zimaogou.photoalbum.PhotoInfo;
import com.sensu.android.zimaogou.popup.SelectCountryPopup;
import com.sensu.android.zimaogou.utils.DisplayUtils;
import com.sensu.android.zimaogou.utils.ImageUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhangwentao on 2015/11/13.
 */
public class TourBuySendActivity extends BaseActivity implements View.OnClickListener {

    public static final String IS_VIDEO = "is_video";
    public static final String VIDEO_PATH = "video_path";

    String path;

    private Object mAdd;
    private List<Object> mObjectList = new ArrayList<Object>();

    private boolean mIsVideo;
    private GridView mGridView;
    private TourPicAdapter mTourPicAdapter;

    private List<PhotoInfo> mPhotoList = TourSendData.picDataList;

    private boolean mIsSelectFood;
    private boolean mIsSelectBuy;
    private boolean mIsSelectSightSpot;

    private LinearLayout mFoodLayout;
    private LinearLayout mBuyLayout;
    private LinearLayout mSightSpotLayout;

    private ImageView mLocationSwitch;
    private boolean mIsPosition = true;

    private int mPicSize;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tour_buy_send_activity);

        initViews();
    }

    private void initViews() {

        mPicSize = (DisplayUtils.getDisplayWidth() - DisplayUtils.dp2px(55)) / 4;

        mIsVideo = getIntent().getBooleanExtra(IS_VIDEO, false);

        mGridView = (GridView) findViewById(R.id.grid_view);
        mTourPicAdapter = new TourPicAdapter();
        mGridView.setAdapter(mTourPicAdapter);

        if (mIsVideo) {
            //视频
            mGridView.setVisibility(View.GONE);
            findViewById(R.id.video_layout).setVisibility(View.VISIBLE);
            String videoPath = getIntent().getStringExtra(VIDEO_PATH);
            MediaMetadataRetriever mediaMetadataRetriever = new MediaMetadataRetriever();
            mediaMetadataRetriever.setDataSource(videoPath);
            Bitmap bitmap = mediaMetadataRetriever.getFrameAtTime();
            ((ImageView) findViewById(R.id.video_cover)).setImageBitmap(bitmap);
        } else {
            //图片
            mGridView.setVisibility(View.VISIBLE);
            findViewById(R.id.video_layout).setVisibility(View.GONE);
        }

        mFoodLayout = (LinearLayout) findViewById(R.id.food_layout);
        mBuyLayout = (LinearLayout) findViewById(R.id.buy_layout);
        mSightSpotLayout = (LinearLayout) findViewById(R.id.sight_spot_layout);
        mLocationSwitch = (ImageView) findViewById(R.id.location_switch);
        mLocationSwitch.setSelected(mIsPosition);

        mFoodLayout.setOnClickListener(this);
        mBuyLayout.setOnClickListener(this);
        mSightSpotLayout.setOnClickListener(this);
        mLocationSwitch.setOnClickListener(this);
        findViewById(R.id.cancel).setOnClickListener(this);
        findViewById(R.id.release).setOnClickListener(this);
        findViewById(R.id.choose_country).setOnClickListener(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mTourPicAdapter.notifyDataSetChanged();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        TourSendData.picDataList.clear();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.cancel:
                finish();
                break;
            case R.id.release:
                //TODO 发布按钮
                break;
            case R.id.choose_country:
                //TODO 选择国家 弹出对话框
                SelectCountryPopup selectCountryPopup = new SelectCountryPopup(this);
                selectCountryPopup.showAtLocation(getWindow().getDecorView(), Gravity.BOTTOM, 0, 0);
                break;
            case R.id.location_switch:
                //todo 定位开关
                if (mIsPosition) {
                    //关闭定位
                    mIsPosition = false;
                } else {
                    //开启定位并进行定位
                    mIsPosition = true;
                }
                mLocationSwitch.setSelected(mIsPosition);
                break;
            case R.id.food_layout:
                if (mIsSelectFood) {
                    mFoodLayout.setSelected(false);
                    findViewById(R.id.food_text).setSelected(false);
                    findViewById(R.id.food_select).setVisibility(View.GONE);
                    mIsSelectFood = false;
                } else {
                    mFoodLayout.setSelected(true);
                    findViewById(R.id.food_text).setSelected(true);
                    findViewById(R.id.food_select).setVisibility(View.VISIBLE);
                    mIsSelectFood = true;
                }
                break;
            case R.id.buy_layout:
                if (mIsSelectBuy) {
                    mBuyLayout.setSelected(false);
                    findViewById(R.id.buy_text).setSelected(false);
                    findViewById(R.id.buy_select).setVisibility(View.GONE);
                    mIsSelectBuy = false;
                } else {
                    mBuyLayout.setSelected(true);
                    findViewById(R.id.buy_text).setSelected(true);
                    findViewById(R.id.buy_select).setVisibility(View.VISIBLE);
                    mIsSelectBuy = true;
                }
                break;
            case R.id.sight_spot_layout:
                if (mIsSelectSightSpot) {
                    mSightSpotLayout.setSelected(false);
                    findViewById(R.id.sight_spot_text).setSelected(false);
                    findViewById(R.id.sight_spot_select).setVisibility(View.GONE);
                    mIsSelectSightSpot = false;
                } else {
                    mSightSpotLayout.setSelected(true);
                    findViewById(R.id.sight_spot_text).setSelected(true);
                    findViewById(R.id.sight_spot_select).setVisibility(View.VISIBLE);
                    mIsSelectSightSpot = true;
                }
                break;
            case R.id.take_photo:
                mObjectList.clear();
                Intent intent1 = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                path = Environment.getExternalStorageDirectory() + File.separator + "im/" +System.currentTimeMillis() +".jpg";
                File mTempCapturePicFile = new File(path);
                intent1.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(mTempCapturePicFile));
                startActivityForResult(intent1, TourBuyFragment.TAKE_PHOTO_CODE);
                mTourBuyChooseDialog.dismiss();
                break;
            case R.id.choose_from_photo_album:
                mObjectList.clear();
                Intent intent = new Intent(this, LocalPhotoActivity.class);
                startActivityForResult(intent, TourBuyFragment.CHOOSE_PHOTO_CODE);
                mTourBuyChooseDialog.dismiss();
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == TourBuyFragment.TAKE_PHOTO_CODE) {
                PhotoInfo photoInfo = new PhotoInfo();
                photoInfo.setPathPath("file://" + path);
                TourSendData.picDataList.add(photoInfo);

//                mTourPicAdapter.notifyDataSetChanged();
            } else if (requestCode == TourBuyFragment.CHOOSE_PHOTO_CODE) {
//                mTourPicAdapter.notifyDataSetChanged();
            }
        }
    }

    class TourPicAdapter extends SimpleBaseAdapter {

        @Override
        public void notifyDataSetChanged() {
            for (PhotoInfo photoInfo : mPhotoList) {
                mObjectList.add(photoInfo);
            }
            if (mPhotoList.size() < 5) {
                mObjectList.add(mAdd);
            }
            super.notifyDataSetChanged();
        }

        @Override
        public int getCount() {
            return mObjectList == null ? 0 : mObjectList.size();
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            ViewHolder viewHolder;
            if (view == null) {
                view = LayoutInflater.from(TourBuySendActivity.this).inflate(R.layout.tour_send_grid_item, null);
                viewHolder = new ViewHolder();
                viewHolder.mImageView = (ImageView) view.findViewById(R.id.image);
                viewHolder.mImageView.setLayoutParams(new LinearLayout.LayoutParams(mPicSize, mPicSize));
                view.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) view.getTag();
            }

            final Object object = mObjectList.get(i);

            if (object == mAdd) {
                viewHolder.mImageView.setImageResource(R.drawable.add_photos);
            } else {
                ImageUtils.displayImage(((PhotoInfo) object).getPicPath(), viewHolder.mImageView);
            }

            viewHolder.mImageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (object == mAdd) {
                        chooseDialog();
                    }
                }
            });
            return view;
        }
    }

    private class ViewHolder {
        ImageView mImageView;
    }

    /**
     * 选择对话框
     */
    Dialog mTourBuyChooseDialog;
    public void chooseDialog(){
        mTourBuyChooseDialog = new Dialog(this,R.style.notParentDialog);
        mTourBuyChooseDialog.setCancelable(true);
        mTourBuyChooseDialog.setContentView(R.layout.tour_buy_choose_dialog);
        TextView tv_cancel = (TextView) mTourBuyChooseDialog.findViewById(R.id.tv_cancel);
        tv_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mTourBuyChooseDialog.dismiss();
            }
        });

        mTourBuyChooseDialog.findViewById(R.id.take_video).setVisibility(View.GONE);
        mTourBuyChooseDialog.findViewById(R.id.take_photo).setOnClickListener(this);
        mTourBuyChooseDialog.findViewById(R.id.choose_from_photo_album).setOnClickListener(this);

        WindowManager m = getWindowManager();

        Window dialogWindow = mTourBuyChooseDialog.getWindow();

//        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
//        dialogWindow.setGravity(Gravity.TOP);
//        lp.y = DisplayUtils.dp2px(50);
//        dialogWindow.setAttributes(lp);

        Display d = m.getDefaultDisplay(); // 获取屏幕宽、高用
        WindowManager.LayoutParams p = dialogWindow.getAttributes(); // 获取对话框当前的参数值
//        p.height = (int) d.getHeight() ; // 高度设置为屏幕
        p.width = (int) d.getWidth() ; // 宽度设置为屏幕
        dialogWindow.setAttributes(p);
        mTourBuyChooseDialog.show();
    }
}
