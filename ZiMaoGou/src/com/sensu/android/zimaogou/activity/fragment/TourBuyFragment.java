package com.sensu.android.zimaogou.activity.fragment;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.view.*;
import android.widget.AdapterView;
import android.widget.TextView;
import com.sensu.android.zimaogou.R;
import com.sensu.android.zimaogou.activity.LocalPhotoActivity;
import com.sensu.android.zimaogou.activity.tour.TourBuyDetailsActivity;
import com.sensu.android.zimaogou.activity.tour.TourBuySendActivity;
import com.sensu.android.zimaogou.activity.tour.TourSendData;
import com.sensu.android.zimaogou.activity.video.CameraActivity;
import com.sensu.android.zimaogou.adapter.TourBuyAdapter;
import com.sensu.android.zimaogou.photoalbum.PhotoInfo;
import com.sensu.android.zimaogou.widget.OnRefreshListener;
import com.sensu.android.zimaogou.widget.RefreshListView;

import java.io.File;

/**
 * Created by zhangwentao on 2015/11/10.
 */
public class TourBuyFragment extends BaseFragment implements View.OnClickListener, AdapterView.OnItemClickListener {

    public static final int TAKE_PHOTO_CODE = 1;
    public static final int CHOOSE_PHOTO_CODE = 2;
    private RefreshListView mTourBuyListView;
    private TourBuyAdapter mTourBuyAdapter;
    String path;

    private Handler mHandler = new Handler();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.tour_buy_fragment, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    protected void initView() {
        mParentActivity.findViewById(R.id.tour_buy_send).setOnClickListener(this);
        mTourBuyListView = (RefreshListView) mParentActivity.findViewById(R.id.tour_list);
        mTourBuyAdapter = new TourBuyAdapter(mParentActivity);
        mTourBuyListView.setAdapter(mTourBuyAdapter);

        mTourBuyListView.setOnRefreshListener(mOnRefreshListener);
        mTourBuyListView.setOnItemClickListener(this);
    }

    private OnRefreshListener mOnRefreshListener = new OnRefreshListener() {
        @Override
        public void onDownPullRefresh() {
            //下拉刷新接口
            mHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    mTourBuyListView.hideHeaderView();
                }
            }, 2000);
        }

        @Override
        public void onLoadingMore() {
            //上拉加载接口
            mHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    mTourBuyListView.hideFooterView();
                }
            }, 2000);
        }
    };

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (hidden) {

        } else {

        }
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tour_buy_send:
                chooseDialog();
                break;
            case R.id.take_video:
                mParentActivity.startActivity(new Intent(mParentActivity, CameraActivity.class));
                mTourBuyChooseDialog.dismiss();
                break;
            case R.id.take_photo:
                Intent intent1 = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                path = Environment.getExternalStorageDirectory() + File.separator + "im/" + System.currentTimeMillis() + ".jpg";
                File mTempCapturePicFile = new File(path);
                intent1.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(mTempCapturePicFile));
                TourBuyFragment.this.startActivityForResult(intent1, TAKE_PHOTO_CODE);
                mTourBuyChooseDialog.dismiss();
                break;
            case R.id.choose_from_photo_album:
                Intent intent = new Intent(mParentActivity, LocalPhotoActivity.class);
                TourBuyFragment.this.startActivityForResult(intent, CHOOSE_PHOTO_CODE);
                mTourBuyChooseDialog.dismiss();
                break;
        }
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        if (i == 1) {
            startActivity(new Intent(mParentActivity, TourBuyDetailsActivity.class));
        }
    }

    /**
     * 选择对话框
     */
    Dialog mTourBuyChooseDialog;
    public void chooseDialog(){
        mTourBuyChooseDialog = new Dialog(mParentActivity,R.style.notParentDialog);
        mTourBuyChooseDialog.setCancelable(true);
        mTourBuyChooseDialog.setContentView(R.layout.tour_buy_choose_dialog);
        TextView tv_cancel = (TextView) mTourBuyChooseDialog.findViewById(R.id.tv_cancel);
        tv_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mTourBuyChooseDialog.dismiss();
            }
        });

        mTourBuyChooseDialog.findViewById(R.id.take_video).setOnClickListener(this);
        mTourBuyChooseDialog.findViewById(R.id.take_photo).setOnClickListener(this);
        mTourBuyChooseDialog.findViewById(R.id.choose_from_photo_album).setOnClickListener(this);

        WindowManager m = mParentActivity.getWindowManager();

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

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == TAKE_PHOTO_CODE) {
                PhotoInfo photoInfo = new PhotoInfo();
                photoInfo.setPathPath("file://" + path);
                TourSendData.picDataList.add(photoInfo);

                Intent intent = new Intent(mParentActivity, TourBuySendActivity.class);
                intent.putExtra(TourBuySendActivity.IS_VIDEO, false);
                startActivity(intent);
            } else if (requestCode == CHOOSE_PHOTO_CODE) {
                Intent intent = new Intent(mParentActivity, TourBuySendActivity.class);
                intent.putExtra(TourBuySendActivity.IS_VIDEO, false);
                startActivity(intent);
            }
        }
    }
}
