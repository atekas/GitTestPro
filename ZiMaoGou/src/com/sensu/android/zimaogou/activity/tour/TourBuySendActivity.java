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
import android.widget.*;
import com.alibaba.fastjson.JSON;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.sensu.android.zimaogou.BaseApplication;
import com.sensu.android.zimaogou.IConstants;
import com.sensu.android.zimaogou.Mode.LandMode;
import com.sensu.android.zimaogou.Mode.TravelTagMode;
import com.sensu.android.zimaogou.R;
import com.sensu.android.zimaogou.ReqResponse.TravelSendResponse;
import com.sensu.android.zimaogou.activity.BaseActivity;
import com.sensu.android.zimaogou.activity.LocalPhotoActivity;
import com.sensu.android.zimaogou.activity.fragment.TourBuyFragment;
import com.sensu.android.zimaogou.adapter.SimpleBaseAdapter;
import com.sensu.android.zimaogou.external.greendao.helper.GDUserInfoHelper;
import com.sensu.android.zimaogou.external.greendao.model.UserInfo;
import com.sensu.android.zimaogou.photoalbum.PhotoInfo;
import com.sensu.android.zimaogou.popup.SelectCountryPopup;
import com.sensu.android.zimaogou.utils.*;
import com.sensu.android.zimaogou.widget.MyTagListView;
import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhangwentao on 2015/11/13.
 */
public class TourBuySendActivity extends BaseActivity implements View.OnClickListener {

    public static final String IS_VIDEO = "is_video";
    public static final String VIDEO_PATH = "video_path";

    String path;
    String mServiceVideoPath = "";
    private Object mAdd;
    private List<Object> mObjectList = new ArrayList<Object>();

    private boolean mIsVideo;
    private GridView mGridView;
    private TourPicAdapter mTourPicAdapter;

    private List<PhotoInfo> mPhotoList = TourSendData.picDataList;
    private UserInfo userInfo;


    private ImageView mLocationSwitch;
    private TextView mCountryNameTextView, mLocationTextView;
    private EditText mContentEditText;

    private boolean mIsPosition = true;

    private int mPicSize;
    private String mVideoPath;
    private MyTagListView mTagListView;
    private String mPhotoPath = Environment.getExternalStorageDirectory() + "/zimaogou/mobile/";
//    private ArrayList<String> mServiceImages = new ArrayList<String>();

    ArrayList<LandMode> landModes = new ArrayList<LandMode>();
    ArrayList<TravelTagMode> travelTagModes = new ArrayList<TravelTagMode>();
    int mSendSuccess = 0;
    boolean mIsUpload = true;
    String location = "";
    String coverUrl = "";

    private String mUrlString[];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tour_buy_send_activity);
        userInfo = GDUserInfoHelper.getInstance(this).getUserInfo();
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
            mVideoPath = getIntent().getStringExtra(VIDEO_PATH);
            MediaMetadataRetriever mediaMetadataRetriever = new MediaMetadataRetriever();
            mediaMetadataRetriever.setDataSource(mVideoPath);
            Bitmap bitmap = mediaMetadataRetriever.getFrameAtTime(1);
//            PromptUtils.showToast("接收video地址:"+mVideoPath);
            try {
                coverUrl = BitmapUtils.saveImg(bitmap, "cover");
            } catch (Exception e) {
                e.printStackTrace();
            }
//            coverUrl = getIntent().getExtras().getString("cover");
            ImageUtils.displayImage("file://" + coverUrl, (ImageView) findViewById(R.id.video_cover));
        } else {
            //图片
            mGridView.setVisibility(View.VISIBLE);
            findViewById(R.id.video_layout).setVisibility(View.GONE);
        }

        mLocationSwitch = (ImageView) findViewById(R.id.location_switch);
        mLocationSwitch.setSelected(mIsPosition);
        mCountryNameTextView = (TextView) findViewById(R.id.tv_countryName);
        mLocationTextView = (TextView) findViewById(R.id.location);
        mContentEditText = (EditText) findViewById(R.id.et_content);
        mTagListView = (MyTagListView) findViewById(R.id.tagview);

        mLocationSwitch.setOnClickListener(this);
        findViewById(R.id.cancel).setOnClickListener(this);
        findViewById(R.id.release).setOnClickListener(this);
        findViewById(R.id.choose_country).setOnClickListener(this);

        getTravelSendData();
        getLocation();
    }

    private void getLocation() {
        new LocationService(this, new LocationService.OnLocationFinish() {
            @Override
            public void getLocationInfo(List<String> stringList) {
                location = stringList.get(0);
                mLocationTextView.setText(location);
            }
        });
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

                if (!checkInputData()) {//检查参数
                    return;
                }
                mUrlString = null;
                mSendSuccess = 0;
                showLoading();
                if (mIsVideo) {
                    mUrlString = new String[1];
                    uploadImage(coverUrl, 0);
                } else {
                    if (mPhotoList.size() == 0) {
                        return;
                    }
                    mUrlString = new String[mPhotoList.size()];
                    for (int i = 0; i < mPhotoList.size(); i++) {
                        uploadImage(mPhotoList.get(i).getmUploadPath(), i);
                    }
                }

                break;
            case R.id.choose_country:
                //TODO 选择国家 弹出对话框
                SelectCountryPopup selectCountryPopup = new SelectCountryPopup(this, mCountryNameTextView, landModes);
                selectCountryPopup.showAtLocation(getWindow().getDecorView(), Gravity.BOTTOM, 0, 0);
                break;
            case R.id.location_switch:
                //todo 定位开关
                if (mIsPosition) {
                    //关闭定位
                    mIsPosition = false;
                    mLocationTextView.setText("");
                } else {
                    //开启定位并进行定位
                    mIsPosition = true;
                    mLocationTextView.setText(location);
                }
                mLocationSwitch.setSelected(mIsPosition);
                break;
            case R.id.take_photo:
                mObjectList.clear();
                Intent intent1 = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                path = Environment.getExternalStorageDirectory() + File.separator + "im/" + System.currentTimeMillis() + ".jpg";
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
                photoInfo.setmUploadPath(path);
                TourSendData.picDataList.add(photoInfo);

            } else if (requestCode == TourBuyFragment.CHOOSE_PHOTO_CODE) {
//                mTourPicAdapter.notifyDataSetChanged();
            }
        }
    }

    class TourPicAdapter extends SimpleBaseAdapter {
        private int size = 0;

        @Override
        public void notifyDataSetChanged() {
            if (size == mPhotoList.size()) {
                return;
            }
            for (PhotoInfo photoInfo : mPhotoList) {

                mObjectList.add(photoInfo);

            }
            if (mPhotoList.size() < 5) {
                mObjectList.add(mAdd);
            }
            size = mPhotoList.size();
            super.notifyDataSetChanged();
        }

        @Override
        public int getCount() {
            return mObjectList == null ? 0 : mObjectList.size();
        }

        @Override
        public View getView(final int i, View view, ViewGroup viewGroup) {
            ViewHolder viewHolder;
            if (view == null) {
                view = LayoutInflater.from(TourBuySendActivity.this).inflate(R.layout.tour_send_grid_item, null);
                viewHolder = new ViewHolder();
                viewHolder.mImageView = (ImageView) view.findViewById(R.id.image);
                viewHolder.mImageView.setLayoutParams(new FrameLayout.LayoutParams(mPicSize, mPicSize));
                viewHolder.mPicDelete = (ImageView) view.findViewById(R.id.pic_delete);
                view.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) view.getTag();
            }

            final Object object = mObjectList.get(i);

            if (object == mAdd) {
                viewHolder.mImageView.setImageResource(R.drawable.add_photos);
                viewHolder.mPicDelete.setVisibility(View.GONE);
            } else {
                ImageUtils.displayImage(((PhotoInfo) object).getPicPath(), viewHolder.mImageView);
                viewHolder.mPicDelete.setVisibility(View.VISIBLE);
            }

            viewHolder.mImageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (object == mAdd) {
                        chooseDialog();
                    }
                }
            });

            viewHolder.mPicDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mPhotoList.remove(i);
                    mObjectList.clear();
                    mTourPicAdapter.notifyDataSetChanged();
                }
            });
            return view;
        }
    }

    private class ViewHolder {
        ImageView mImageView;
        ImageView mPicDelete;
    }

    /**
     * 选择对话框
     */
    Dialog mTourBuyChooseDialog;

    public void chooseDialog() {
        mTourBuyChooseDialog = new Dialog(this, R.style.notParentDialog);
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
        p.width = (int) d.getWidth(); // 宽度设置为屏幕
        dialogWindow.setAttributes(p);
        mTourBuyChooseDialog.show();
    }

    /**
     * 获取国家,游购标签
     */
    private void getTravelSendData() {

        HttpUtil.get(IConstants.sGetComm, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                LogUtils.d("获取游购国家，标签", response.toString());
                TravelSendResponse travelSendResponse = new TravelSendResponse();
                travelSendResponse = JSON.parseObject(response.toString(), TravelSendResponse.class);
                landModes = travelSendResponse.data.country;


            }
        });
        HttpUtil.get(IConstants.sGetTravelTags, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                LogUtils.d("获取游购国家，标签", response.toString());
                try {
                    JSONArray data = response.getJSONArray("data");
                    JSONObject item = null;
                    for (int i = 0; i < data.length(); i++) {
                        item = (JSONObject) data.get(i);
                        TravelTagMode tagMode = new TravelTagMode();
                        tagMode = JSON.parseObject(item.toString(), TravelTagMode.class);
                        travelTagModes.add(tagMode);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

                mTagListView.setTravelTagModes(travelTagModes);
            }
        });
    }

    private void setRequestData(RequestParams requestParams) {
        JSONObject jsonObject = new JSONObject();


        requestParams.put("location", mLocationTextView.getText().toString());
        requestParams.put("country", mCountryNameTextView.getText().toString());
        requestParams.put("content", mContentEditText.getText().toString());
        JSONArray jsonArray = new JSONArray();
        JSONArray jsonArray1 = new JSONArray();
        for (int i = 0; i < travelTagModes.size(); i++) {

            if (travelTagModes.get(i).isCheck()) {
                jsonArray.put(travelTagModes.get(i).getId());
            }
        }
        requestParams.put("tags", jsonArray);

        for (int j = 0; j < mUrlString.length; j++) {
            jsonArray1.put(mUrlString[j]);
        }
        if (mIsVideo) {
            requestParams.put("category", "2");
            requestParams.put("images", "");
            requestParams.put("cover", mUrlString[0]);
            requestParams.put("video", mServiceVideoPath);
        } else {
            requestParams.put("category", "1");
            requestParams.put("images", jsonArray1);
        }

    }


    /**
     * 检查必填参数
     *
     * @return
     */
    private boolean checkInputData() {
        if (TextUtils.isEmpty(mContentEditText.getText().toString())) {
            PromptUtils.showToast("发表游购内容不能为空");
            return false;
        }
//        if (TextUtils.isEmpty(mCountryNameTextView.getText().toString())) {
//            PromptUtils.showToast("发表游购国家不能为空");
//            return false;
//        }
        boolean haveTag = false;
        for (TravelTagMode tagMode : travelTagModes) {
            if (tagMode.isCheck()) {
                haveTag = true;
                break;
            }
        }
        if (!haveTag) {
            PromptUtils.showToast("请选择游购标签");
            return false;
        }
        return true;
    }

    private void uploadImage(String url, final int i) {
        if (TextUtils.isEmpty(url)) {
            PromptUtils.showToast("封面图保存失败");
        }
        HttpUtil.postImage(userInfo.getUid(), userInfo.getToken(), url, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                LogUtils.d("上传图片返回：", response.toString());
                mSendSuccess++;
                String photoUrl = "";
                try {
                    photoUrl = response.getJSONObject("data").getString("url");

                    if (mUrlString != null) {
                        mUrlString[i] = photoUrl;
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
//                mServiceImages.add(photoUrl);
                if (!mIsVideo) {
                    if (mSendSuccess != mPhotoList.size()) {
                        return;
                    }
                    uploadTravel();
                } else {
                    uploadVideo();
                }

            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);
                mIsUpload = false;
                PromptUtils.showToast("上传图片出错");
            }
        });
    }


    private void uploadVideo() {
        RequestParams requestParams = new RequestParams();
        requestParams.put("uid", userInfo.getUid());
        try {

            requestParams.put("body", new File(mVideoPath), "application/mp4");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        HttpUtil.postWithSign(userInfo.getToken(), IConstants.sVideoUpload, requestParams, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                LogUtils.d("上传视频返回：", response.toString());
                cancelLoading();
                try {
                    mServiceVideoPath = response.getJSONObject("data").getString("url");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                uploadTravel();
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);
                LogUtils.d("上传视频失败", responseString.toString());
                cancelLoading();
            }
        });
    }

    private void uploadTravel() {
        final RequestParams requestParams = new RequestParams();
        requestParams.put("uid", userInfo.getUid());
        setRequestData(requestParams);
        HttpUtil.postWithSign(userInfo.getToken(), IConstants.sSendTravel, requestParams, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                LogUtils.d("发布游购返回：", response.toString());
                cancelLoading();
                try {
                    if (response.getString("ret").equals("0")) {
                        PromptUtils.showToast(response.getString("msg"));
                        BaseApplication.isSendTravel = true;

                        finish();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);
                PromptUtils.showToast("发布游购失败");
                cancelLoading();
                LogUtils.d("发布游购返回：", "ErrorCode:" + statusCode + "Html:" + responseString);
            }
        });
    }
}
