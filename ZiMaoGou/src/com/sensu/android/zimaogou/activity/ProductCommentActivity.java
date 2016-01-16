package com.sensu.android.zimaogou.activity;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.*;
import android.widget.*;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.sensu.android.zimaogou.IConstants;
import com.sensu.android.zimaogou.Mode.MyOrderGoodsMode;
import com.sensu.android.zimaogou.Mode.MyOrderMode;
import com.sensu.android.zimaogou.Mode.ProductCommentMode;
import com.sensu.android.zimaogou.Mode.RefundReasonMode;
import com.sensu.android.zimaogou.R;
import com.sensu.android.zimaogou.activity.fragment.TourBuyFragment;
import com.sensu.android.zimaogou.activity.tour.TourBuySendAdapter;
import com.sensu.android.zimaogou.external.greendao.helper.GDUserInfoHelper;
import com.sensu.android.zimaogou.external.greendao.model.UserInfo;
import com.sensu.android.zimaogou.photoalbum.PhotoInfo;
import com.sensu.android.zimaogou.utils.*;
import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhangwentao on 2015/11/27.
 */
public class ProductCommentActivity extends BaseActivity implements View.OnClickListener {

    private TextView mSubmit;
    private GridView mGridView;
    private TourBuySendAdapter mTourBuySendAdapter;

    private ArrayList<String> mServiceImages = new ArrayList<String>();
    LinearLayout mImagesLinearLayout;
    private List<PhotoInfo> mPhotoList = new ArrayList<PhotoInfo>();
    int mSendSuccess = 0;
    private Object mAdd;
    private List<Object> mObjectList = new ArrayList<Object>();

    private ImageView mImageStar1;
    private ImageView mImageStar2;
    private ImageView mImageStar3;
    private ImageView mImageStar4;
    private ImageView mImageStar5;

    private ImageView mImageShowName;

    private EditText mCommentEdit;
    MyOrderMode myOrderMode = new MyOrderMode();
    MyOrderGoodsMode myOrderGoodsMode = new MyOrderGoodsMode();
    UserInfo userInfo;
    boolean isAnonymous = false;//是否匿名
    int score = 5;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.product_comment_activity);
        if(getIntent().getExtras() != null){
            myOrderMode = (MyOrderMode) getIntent().getExtras().get("order");
            myOrderGoodsMode = (MyOrderGoodsMode) getIntent().getExtras().get("goods");
        }
        findViewById(R.id.back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        userInfo = GDUserInfoHelper.getInstance(this).getUserInfo();
        initViews();
    }

    private void initViews() {
        mSubmit = (TextView) findViewById(R.id.submit);
        mGridView = (GridView) findViewById(R.id.grid_view);
        mSubmit.setEnabled(false);
        mSubmit.setOnClickListener(this);
        mImageStar1 = (ImageView) findViewById(R.id.star_1);
        mImageStar2 = (ImageView) findViewById(R.id.star_2);
        mImageStar3 = (ImageView) findViewById(R.id.star_3);
        mImageStar4 = (ImageView) findViewById(R.id.star_4);
        mImageStar5 = (ImageView) findViewById(R.id.star_5);
        mImageStar1.setSelected(true);
        mImageStar2.setSelected(true);
        mImageStar3.setSelected(true);
        mImageStar4.setSelected(true);
        mImageStar5.setSelected(true);
        mImageShowName = (ImageView) findViewById(R.id.is_show_name);
        mCommentEdit = (EditText) findViewById(R.id.edit_comment);
        mImagesLinearLayout = (LinearLayout) findViewById(R.id.ll_img);

//        mCommentEdit.addTextChangedListener(new TextWatcher() {
//            @Override
//            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
//
//            }
//
//            @Override
//            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
//                //todo 判断是否有星星    如果没有星星不能提交   有星星有文字才可以点击
//                if (TextUtils.isEmpty(charSequence.toString())) {
//                    mSubmit.setEnabled(false);
//                } else {
//                    mSubmit.setEnabled(true);
//                }
//            }
//
//            @Override
//            public void afterTextChanged(Editable editable) {
//
//            }
//        });

        mImageStar1.setOnClickListener(this);
        mImageStar2.setOnClickListener(this);
        mImageStar3.setOnClickListener(this);
        mImageStar4.setOnClickListener(this);
        mImageStar5.setOnClickListener(this);
        mImageShowName.setOnClickListener(this);

        mTourBuySendAdapter = new TourBuySendAdapter(this);
        mGridView.setAdapter(mTourBuySendAdapter);
        mTourBuySendAdapter.setList(new ArrayList());
        flushImages();
    }

    private void flushImages() {
        mImagesLinearLayout.removeAllViews();

        mObjectList.clear();
        for (PhotoInfo photoInfo : mPhotoList) {

            mObjectList.add(photoInfo);

        }
        if (mPhotoList.size() < 3 ) {
            mObjectList.add(mAdd);
        }
        for (int i = 0; i < mObjectList.size(); i++) {
            View view = LayoutInflater.from(ProductCommentActivity.this).inflate(R.layout.image_grid_item, null);
            view.setPadding(0,0, DisplayUtils.dp2px(6),0);
            ImageView mImageView = (ImageView) view.findViewById(R.id.image);
            int mPicSize = (DisplayUtils.getDisplayWidth() - DisplayUtils.dp2px(55)) / 4;
            mImageView.setLayoutParams(new LinearLayout.LayoutParams(mPicSize, mPicSize));

            final Object object = mObjectList.get(i);

            if (object == mAdd) {
                mImageView.setImageResource(R.drawable.add_photos);
            } else {
                ImageUtils.displayImage(((PhotoInfo) object).getPicPath(), mImageView);
            }

            mImageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (object == mAdd) {
                        chooseDialog();
                    }
                }
            });
            mImagesLinearLayout.addView(view);
        }

    }
    /**
     * 上传图片
     *
     * @param url
     */
    private void uploadImage(String url) {
        HttpUtil.postImage(userInfo.getUid(), userInfo.getToken(), url, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                LogUtils.d("上传图片返回：", response.toString());
                mSendSuccess++;
                String photoUrl = "";
                try {
                    photoUrl = response.getJSONObject("data").getString("url");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                mServiceImages.add(photoUrl);
                if (mSendSuccess != mPhotoList.size()) {
                    return;
                }
                createComment();

            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);
                PromptUtils.showToast("上传图片出错");
            }
        });
    }
    private void createComment(){
        RequestParams requestParams = new RequestParams();
        requestParams.put("uid",userInfo.getUid());
        requestParams.put("id",myOrderGoodsMode.getGid());
        requestParams.put("is_anonymous",isAnonymous?"1":"0");
        requestParams.put("score",""+score);
        requestParams.put("order_id",myOrderMode.getId());
        requestParams.put("content",mCommentEdit.getText().toString().trim());
        requestParams.put("images", mServiceImages.size() == 0 ? "" : changeTOStringOfImages());
        HttpUtil.postWithSign(userInfo.getToken(), IConstants.sProduct_detail+""+myOrderGoodsMode.getGid()+"/comment",requestParams,new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                cancelLoading();
                LogUtils.d("提交商品评论返回：",response.toString());
                if(response.optInt("ret")>=0){
                    PromptUtils.showToast("评论成功");
                    finish();
                }else{
                    PromptUtils.showToast(response.optString("msg"));
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);
                cancelLoading();
            }
        });
    }
    private JSONArray changeTOStringOfImages() {
        JSONArray jsonArray1 = new JSONArray();
        for (int j = 0; j < mServiceImages.size(); j++) {
            jsonArray1.put(mServiceImages.get(j));
        }
        return jsonArray1;
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
     * 从相册选择
     */
    public String IMAGE_UNSPECIFIED = "image/*";
    private int IMAGE_REQUEST_CODE = 6;

    private void getPhotoFromAlum() {
        Intent intent2 = new Intent(Intent.ACTION_PICK, null);
        intent2.setDataAndType(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                IMAGE_UNSPECIFIED);
        startActivityForResult(intent2, IMAGE_REQUEST_CODE);
    }
    public String getRealPathFromURI(Uri contentUri) {
        String[] proj = {MediaStore.Images.Media.DATA};
        Cursor cursor = managedQuery(contentUri, proj, null, null, null);

        if (cursor == null)
            return null;

        int column_index = cursor
                .getColumnIndexOrThrow(MediaStore.Images.Media.DATA);

        cursor.moveToFirst();
        return cursor.getString(column_index);
    }
    private Uri uri = null;
    String path ;
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == TourBuyFragment.TAKE_PHOTO_CODE) {
                PhotoInfo photoInfo = new PhotoInfo();
                photoInfo.setPathPath("file://" + path);
                photoInfo.setmUploadPath(path);
                mPhotoList.add(photoInfo);
//                mTourPicAdapter.flush();
                flushImages();

            } else if (requestCode == 6) {
//                mTourPicAdapter.notifyDataSetChanged();

                if (data == null) {
                    return;
                }
                uri = data.getData();
//                    getBitmap(uri, IMAGE_REQUEST_CODE);
                String path = "";
                path = getRealPathFromURI(uri); // from Gallery
                if (path == null) {
                    path = uri.getPath(); // from File Manager
                }
                String avatar = path;
                int degree = ImageUtils.readPictureDegree(path);
                Bitmap bmpOk = ImageUtils.rotateToDegrees(BitmapUtils.getSampleBitmap(path, 800, 800).getBitmap(), degree);
//                        mHeadImageView.setImageBitmap(bmpOk);
                try {
                    avatar = BitmapUtils.saveImg(bmpOk, "head");
                } catch (Exception e) {
                    e.printStackTrace();
                }
                PhotoInfo photoInfo = new PhotoInfo();
                photoInfo.setmPathPath("file://" + avatar);
                photoInfo.setmUploadPath(avatar);
                mPhotoList.add(photoInfo);
//                        mTourPicAdapter.flush();
                flushImages();
            }
        }
    }
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.take_photo:
                Intent intent1 = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                path = Environment.getExternalStorageDirectory() + File.separator + "im/" + System.currentTimeMillis() + ".jpg";
                File mTempCapturePicFile = new File(path);
                intent1.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(mTempCapturePicFile));
                startActivityForResult(intent1, TourBuyFragment.TAKE_PHOTO_CODE);
                mTourBuyChooseDialog.dismiss();
                break;
            case R.id.choose_from_photo_album:
                getPhotoFromAlum();
                mTourBuyChooseDialog.dismiss();
                break;
            case R.id.submit:
                if(com.sensu.android.zimaogou.utils.TextUtils.isEmpty(mCommentEdit.getText().toString().trim())){
                    PromptUtils.showToast("请输入评论内容");
                    return;
                }
                mServiceImages.clear();
                mSendSuccess = 0;
                showLoading();
                if (mPhotoList.size() == 0) {
                    createComment();
                }
                for (int i = 0; i < mPhotoList.size(); i++) {
                    uploadImage(mPhotoList.get(i).getmUploadPath());
                }
                break;
            case R.id.star_1:
                starBlack();
                score = 1;
                mImageStar1.setSelected(true);
                break;
            case R.id.star_2:
                starBlack();
                score = 2;
                mImageStar1.setSelected(true);
                mImageStar2.setSelected(true);
                break;
            case R.id.star_3:
                starBlack();
                score = 3;
                mImageStar1.setSelected(true);
                mImageStar2.setSelected(true);
                mImageStar3.setSelected(true);
                break;
            case R.id.star_4:
                starBlack();
                score = 4;
                mImageStar1.setSelected(true);
                mImageStar2.setSelected(true);
                mImageStar3.setSelected(true);
                mImageStar4.setSelected(true);
                break;
            case R.id.star_5:
                starBlack();
                score = 5;
                mImageStar1.setSelected(true);
                mImageStar2.setSelected(true);
                mImageStar3.setSelected(true);
                mImageStar4.setSelected(true);
                mImageStar5.setSelected(true);
                break;
            case R.id.is_show_name:
                isAnonymous = !isAnonymous;
                mImageShowName.setSelected(isAnonymous);
                break;
        }
    }

    private void starBlack() {
        mImageStar1.setSelected(false);
        mImageStar2.setSelected(false);
        mImageStar3.setSelected(false);
        mImageStar4.setSelected(false);
        mImageStar5.setSelected(false);
    }
}
