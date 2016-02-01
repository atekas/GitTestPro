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
import android.text.TextUtils;
import android.util.Log;
import android.view.*;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.alibaba.fastjson.JSON;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.sensu.android.zimaogou.*;
import com.sensu.android.zimaogou.Mode.TravelMode;
import com.sensu.android.zimaogou.ReqResponse.TravelResponse;
import com.sensu.android.zimaogou.activity.LocalPhotoActivity;
import com.sensu.android.zimaogou.activity.login.LoginActivity;
import com.sensu.android.zimaogou.activity.tour.TourBuyDetailsActivity;
import com.sensu.android.zimaogou.activity.tour.TourBuySendActivity;
import com.sensu.android.zimaogou.activity.tour.TourSendData;
import com.sensu.android.zimaogou.activity.video.CameraActivity;
import com.sensu.android.zimaogou.adapter.TourBuyAdapter;
import com.sensu.android.zimaogou.demo.ui.record.MediaRecorderActivity;
import com.sensu.android.zimaogou.external.greendao.helper.GDUserInfoHelper;
import com.sensu.android.zimaogou.external.greendao.model.UserInfo;
import com.sensu.android.zimaogou.photoalbum.PhotoInfo;
import com.sensu.android.zimaogou.utils.AppConfig;
import com.sensu.android.zimaogou.utils.HttpUtil;
import com.sensu.android.zimaogou.utils.NetworkTypeUtils;
import com.sensu.android.zimaogou.utils.PromptUtils;
import com.sensu.android.zimaogou.widget.OnRefreshListener;
import com.sensu.android.zimaogou.widget.RefreshListView;
import org.apache.http.Header;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhangwentao on 2015/11/10.
 */
public class TourBuyFragment extends BaseFragment implements View.OnClickListener, AdapterView.OnItemClickListener {

    public static final int TAKE_PHOTO_CODE = 1;
    public static final int CHOOSE_PHOTO_CODE = 2;
    private RefreshListView mTourBuyListView;
    private TourBuyAdapter mTourBuyAdapter;
    String path;
    TravelResponse travelModes = new TravelResponse();
    private Handler mHandler = new Handler();

    private View mNoNetView;

    private String mPostId = "0";

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
        mNoNetView = mParentActivity.findViewById(R.id.tour_no_net);
        mParentActivity.findViewById(R.id.tour_buy_send).setOnClickListener(this);
        mTourBuyListView = (RefreshListView) mParentActivity.findViewById(R.id.tour_list);
        mTourBuyAdapter = new TourBuyAdapter(mParentActivity);
        mTourBuyListView.setAdapter(mTourBuyAdapter);

        mTourBuyListView.setOnRefreshListener(mOnRefreshListener);
        mTourBuyListView.setOnItemClickListener(this);
        mNoNetView.findViewById(R.id.bt_reload).setOnClickListener(this);

        if (NetworkTypeUtils.isNetWorkAvailable()) {
            mTourBuyListView.setVisibility(View.VISIBLE);
            mNoNetView.setVisibility(View.GONE);
        } else {
            mTourBuyListView.setVisibility(View.GONE);
            mNoNetView.setVisibility(View.VISIBLE);
            ((ImageView) mNoNetView.findViewById(R.id.img_exception)).setImageResource(R.drawable.exception_internet);
            ((TextView) mNoNetView.findViewById(R.id.tv_exception)).setText("您的网络开了小差哦");
        }
    }

    private OnRefreshListener mOnRefreshListener = new OnRefreshListener() {
        @Override
        public void onDownPullRefresh() {
            //下拉刷新接口
//            mHandler.postDelayed(new Runnable() {
//                @Override
//                public void run() {
//                    mTourBuyListView.hideHeaderView();
//                }
//            }, 2000);
            mPostId = "0";
            getTravelData();
        }

        @Override
        public void onLoadingMore() {
            //上拉加载接口
//            mHandler.postDelayed(new Runnable() {
//                @Override
//                public void run() {
//                    mTourBuyListView.hideFooterView();
//                }
//            }, 2000);
            getTravelData();
        }
    };

    /**
     *
     * 获取游购数据
     */
    private void getTravelData(){
        showLoading();
        UserInfo userInfo = GDUserInfoHelper.getInstance(mParentActivity).getUserInfo();
        RequestParams requestParams = new RequestParams();
        requestParams.put("post_id", mPostId);
        requestParams.put("limit", "10");
        requestParams.put("login_id",userInfo==null?"0":userInfo.getUid());
        HttpUtil.get(IConstants.sGetTravelList, requestParams, new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                Log.d("游购列表返回：",response.toString());
                cancelLoading();
                if(travelModes.data != null && travelModes.data.size()>0) {
                    travelModes.data.clear();
                }
                travelModes = JSON.parseObject(response.toString(),TravelResponse.class);
                if (mPostId.equals("0")) {
                    mTourBuyAdapter.clearData();
                }
                if (travelModes.data.size() > 0) {
                    mPostId = travelModes.data.get(travelModes.data.size() - 1).getId();
                }
                mTourBuyListView.hideHeaderView();
                mTourBuyListView.hideFooterView();
                mTourBuyAdapter.flush(travelModes.data);
            }
        });

    }
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
    public void onResume() {
        super.onResume();
        mPostId = "0";
        getTravelData();

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tour_buy_send:
                if(GDUserInfoHelper.getInstance(mParentActivity).getUserInfo() == null){
                    PromptUtils.showToast("请先登录");
                    startActivity(new Intent(mParentActivity, LoginActivity.class));
                }else {
                    chooseDialog();
                }
                break;
            case R.id.take_video:

                mParentActivity.startActivity(new Intent(mParentActivity, MediaRecorderActivity.class));
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
            case R.id.bt_reload:
                if (NetworkTypeUtils.isNetWorkAvailable()) {
                    mTourBuyListView.setVisibility(View.VISIBLE);
                    mNoNetView.setVisibility(View.GONE);
                    getTravelData();
                } else {
                    mTourBuyListView.setVisibility(View.GONE);
                    mNoNetView.setVisibility(View.VISIBLE);
                    ((ImageView) mNoNetView.findViewById(R.id.img_exception)).setImageResource(R.drawable.exception_internet);
                    ((TextView) mNoNetView.findViewById(R.id.tv_exception)).setText("您的网络开了小差哦");
                }
                break;
        }
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

        List<TravelMode> travelModeList = mTourBuyAdapter.getData();

        if (i > 0 ) {
            startActivity(new Intent(mParentActivity, TourBuyDetailsActivity.class).putExtra("travel",travelModeList.get(i - 1)));
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
        LinearLayout ll_top = (LinearLayout) mTourBuyChooseDialog.findViewById(R.id.ll_top);
        ll_top.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mTourBuyChooseDialog.dismiss();
            }
        });
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
                photoInfo.setmUploadPath(path);
                TourSendData.picDataList.add(photoInfo);

                Intent intent = new Intent(mParentActivity, TourBuySendActivity.class);
                intent.putExtra(TourBuySendActivity.IS_VIDEO, false);
                startActivity(intent);
                return;
            } else if (requestCode == CHOOSE_PHOTO_CODE) {
                Intent intent = new Intent(mParentActivity, TourBuySendActivity.class);
                intent.putExtra(TourBuySendActivity.IS_VIDEO, false);
                startActivity(intent);
                return;
            }
//            RecordResult result =new RecordResult(data);
//            //得到视频地址，和缩略图地址的数组，返回十张缩略图
//            String videoPath = result.getPath();
//            String [] thum = result.getThumbnail();
//            Intent intent = new Intent(mParentActivity, TourBuySendActivity.class);
//            intent.putExtra(TourBuySendActivity.IS_VIDEO, true);
//            intent.putExtra(TourBuySendActivity.VIDEO_PATH, videoPath);
//            intent.putExtra("cover",thum[0]);
//            startActivity(intent);
////            tv_result.setText("视频路径:" + videoPath + "图片路径:" + thum[0]);
//
//            try{
//                Files.move(new File(videoPath), new File(IConstants.VIDEOPATH));
//                Files.move(new File(thum[0]), new File(IConstants.THUMBPATH));
//            }catch (IOException e){
//                Toast.makeText(mParentActivity,"拷贝失败",Toast.LENGTH_LONG).show();
//                e.printStackTrace();
//            }

            /**
             * 清除草稿,草稿文件将会删除。所以在这之前我们执行拷贝move操作。
             * 上面的拷贝操作请自行实现，第一版本的copyVideoFile接口不再使用
             */
//            QupaiService qupaiService = AlibabaSDK
//                    .getService(QupaiService.class);
//            qupaiService.deleteDraft(getApplicationContext(),data);
        }else {
//            if (resultCode == RESULT_CANCELED) {
//                Toast.makeText(MainActivity.this, "RESULT_CANCELED", Toast.LENGTH_LONG).show();
//            }
        }


    }

    /*class MyVideoClickListener implements View.OnClickListener{

        @Override
        public void onClick(View view) {
            QupaiService qupaiService = AlibabaSDK
                    .getService(QupaiService.class);

            if (qupaiService == null) {
                Toast.makeText(mParentActivity, "插件没有初始化，无法获取 QupaiService",
                        Toast.LENGTH_LONG).show();
                return;
            }

            //视频时长

            int mDurationLimit = IConstants.DEFAULT_DURATION_LIMIT;


            //视频码率

            int  mVideoBitrate = IConstants.DEFAULT_BITRATE;


            //是否需要水印
//            mWaterMark = Integer.valueOf(TextUtils.isEmpty(edit_watermark.getText().toString()) ? "1" : edit_watermark.getText().toString());
            //水印存储的目录
            String waterMarkPath = IConstants.WATER_MARK_PATH ;

            VideoSessionCreateInfo info =new VideoSessionCreateInfo.Builder()
                    .setOutputDurationLimit((float) mDurationLimit)
                    .setOutputVideoBitrate(mVideoBitrate)
                    .setHasImporter(true)//开启导入
                    .setWaterMarkPath(waterMarkPath)
                    .setWaterMarkPosition(1)
                    .setHasEditorPage(false)//是否开启编辑页面
                    .build();



            //是否需要更多音乐页面--如果不需要更多音乐可以干掉
//            Intent moreMusic = new Intent();
//            if (st_more_music.isChecked()) {
//                moreMusic.setClass(MainActivity.this, MoreMusicActivity.class);
//            } else {
//                moreMusic = null;
//            }
            //初始化，建议在application里面做初始化，这里做是为了方便开发者认识参数的意义
//            qupaiService.initRecord(info);
//            qupaiService.hasMroeMusic(moreMusic);

            //引导，只显示一次，这里用SharedPreferences记录
            final AppGlobalSetting sp = new AppGlobalSetting(mParentActivity.getApplicationContext());
            Boolean isGuideShow = sp.getBooleanGlobalItem(
                    AppConfig.PREF_VIDEO_EXIST_USER, true);

            *//**
             * 建议上面的initRecord只在application里面调用一次。这里为了能够开发者直观看到改变所以可以调用多次
             *//*
            qupaiService.showRecordPage(mParentActivity, RequestCode.RECORDE_SHOW, isGuideShow,
                    new FailureCallback() {
                        @Override
                        public void onFailure(int i, String s) {
                            Toast.makeText(mParentActivity, "onFailure:"+ s + "CODE"+ i, Toast.LENGTH_LONG).show();
                        }
                    });

            sp.saveGlobalConfigItem(
                    AppConfig.PREF_VIDEO_EXIST_USER, false);
        }
    }*/
}
