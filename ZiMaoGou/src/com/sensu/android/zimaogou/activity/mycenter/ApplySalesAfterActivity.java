package com.sensu.android.zimaogou.activity.mycenter;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.*;
import android.widget.*;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.sensu.android.zimaogou.IConstants;
import com.sensu.android.zimaogou.Mode.MyOrderGoodsMode;
import com.sensu.android.zimaogou.Mode.MyOrderMode;
import com.sensu.android.zimaogou.Mode.RefundReasonMode;
import com.sensu.android.zimaogou.R;
import com.sensu.android.zimaogou.activity.BaseActivity;
import com.sensu.android.zimaogou.activity.fragment.TourBuyFragment;
import com.sensu.android.zimaogou.adapter.SimpleBaseAdapter;
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
 * 申请退款
 * Created by qi.yang on 2015/11/27.
 */
public class ApplySalesAfterActivity extends BaseActivity implements View.OnClickListener {

    private GridView mGridView;



    UserInfo userInfo;

    ArrayList<RefundReasonMode> mRefundMoneyReasons = new ArrayList<RefundReasonMode>();
    ArrayList<RefundReasonMode> mRefundGoodsReasons = new ArrayList<RefundReasonMode>();
    boolean isJustRefundMoney = true;
    ImageView mRefundMoneyImageView, mRefundGoodsImageView;
    EditText mRefundMoneyEditText, mRefundInstructionsEditText;
    TextView mRefundReasonTextView, mSubmitTextView;
    RefundReasonMode mChooseRefundReason = new RefundReasonMode();//被选择退款原因

    private ArrayList<String> mServiceImages = new ArrayList<String>();
    LinearLayout mImagesLinearLayout;
    private List<PhotoInfo> mPhotoList = new ArrayList<PhotoInfo>();
    int mSendSuccess = 0;
    private Object mAdd;
    private List<Object> mObjectList = new ArrayList<Object>();

    TourPicAdapter mTourPicAdapter;
    MyOrderMode orderMode = new MyOrderMode();
    MyOrderGoodsMode goodsMode = new MyOrderGoodsMode();
    double maxRefundMoney =0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.apply_after_sales_activity);
        userInfo = GDUserInfoHelper.getInstance(this).getUserInfo();
        if (getIntent().getExtras() != null) {
            orderMode = (MyOrderMode) getIntent().getExtras().get("order");
            goodsMode = (MyOrderGoodsMode) getIntent().getExtras().get("goods");
            maxRefundMoney = Double.parseDouble(goodsMode.getPrice())*Integer.parseInt(goodsMode.getNum());
        }
        findViewById(R.id.back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        setReason();
        initView();

    }

    private void initView() {

        mGridView = (GridView) findViewById(R.id.grid_view);
        mTourPicAdapter = new TourPicAdapter();
        mGridView.setAdapter(mTourPicAdapter);

        mRefundMoneyImageView = (ImageView) findViewById(R.id.refund_money);
        mRefundGoodsImageView = (ImageView) findViewById(R.id.refund_goods);
        mRefundMoneyEditText = (EditText) findViewById(R.id.input_refundMoney);
        mRefundInstructionsEditText = (EditText) findViewById(R.id.input_instructions);
        mRefundReasonTextView = (TextView) findViewById(R.id.tv_refundReason);
        mImagesLinearLayout = (LinearLayout) findViewById(R.id.ll_img);

        mRefundMoneyImageView.setSelected(isJustRefundMoney);
        mRefundGoodsImageView.setSelected(!isJustRefundMoney);
        mSubmitTextView = (TextView) findViewById(R.id.submit);
        mSubmitTextView.setOnClickListener(this);
        mRefundMoneyEditText.setHint("退款金额不能大于"+maxRefundMoney+"元");
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
            View view = LayoutInflater.from(ApplySalesAfterActivity.this).inflate(R.layout.image_grid_item, null);
            view.setPadding(0,0,DisplayUtils.dp2px(6),0);
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

    private Uri uri = null;

    /**
     * 我要退款
     *
     * @param v
     */
    public void refundMoneyClick(View v) {
        isJustRefundMoney = true;
        mRefundMoneyImageView.setSelected(isJustRefundMoney);
        mRefundGoodsImageView.setSelected(!isJustRefundMoney);
        mRefundReasonTextView.setText("");
    }

    /**
     * 我要退货
     *
     * @param v
     */
    public void refundGoodsClick(View v) {
        isJustRefundMoney = false;
        mRefundMoneyImageView.setSelected(isJustRefundMoney);
        mRefundGoodsImageView.setSelected(!isJustRefundMoney);
        mRefundReasonTextView.setText("");

    }

    /**
     * 选择退款原因
     *
     * @param v
     */
    public void RefundReasonClick(View v) {
        startActivityForResult(new Intent(this, OnlyListActivity.class).putExtra("list", isJustRefundMoney ? mRefundMoneyReasons : mRefundGoodsReasons)
                .putExtra("data", mChooseRefundReason), 1001);
    }

    PopupWindow mRefundReasonPopupWindow;

    @TargetApi(Build.VERSION_CODES.KITKAT)
    private void showPopupWindow() {
        View view = LayoutInflater.from(this)
                .inflate(R.layout.refund_reason_list, null);
        mRefundReasonPopupWindow = new PopupWindow(view, DisplayUtils.dp2px(120),
                LinearLayout.LayoutParams.WRAP_CONTENT);

        ListView listView = (ListView) view.findViewById(R.id.lv_refund_reason);
        if (isJustRefundMoney) {
            listView.setAdapter(new RefundReasonAdapter(mRefundMoneyReasons));
        } else {
            listView.setAdapter(new RefundReasonAdapter(mRefundGoodsReasons));
        }
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                mChooseRefundReason = isJustRefundMoney ? mRefundMoneyReasons.get(i) : mRefundGoodsReasons.get(i);
                mRefundReasonTextView.setText(mChooseRefundReason.getName());
                mRefundReasonPopupWindow.dismiss();
            }
        });
        mRefundReasonPopupWindow.setFocusable(true);
        mRefundReasonPopupWindow.setOutsideTouchable(true);//不能在没有焦点的时候使用
        ColorDrawable color = new ColorDrawable(1);
        mRefundReasonPopupWindow.setBackgroundDrawable(color);//
        mRefundReasonPopupWindow.showAsDropDown(findViewById(R.id.tv_popupIndex));
//        mRefundReasonPopupWindow.showAtLocation(mRefundReasonTextView, Gravity.BOTTOM, DisplayUtils.dp2px(200),0);

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

    String path;

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.submit:

                String reason = mRefundReasonTextView.getText().toString().trim();
                String content = mRefundInstructionsEditText.getText().toString().trim();
                String money = mRefundMoneyEditText.getText().toString().trim();

                if (TextUtils.isEmpty(reason)) {
                    PromptUtils.showToast("请选择退货原因");
                    return;
                }

                if (TextUtils.isEmpty(money)) {
                    PromptUtils.showToast("请输入退款金额");
                    return;
                }
                if(Double.parseDouble(money)>maxRefundMoney){
                    PromptUtils.showToast("退款金额不能大于商品总金额");
                    return;
                }

                mServiceImages.clear();
                mSendSuccess = 0;
                showLoading();
                if (mPhotoList.size() == 0) {
                    createReturnOrder();
                }
                for (int i = 0; i < mPhotoList.size(); i++) {
                    uploadImage(mPhotoList.get(i).getmUploadPath());
                }
                break;

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
                createReturnOrder();

            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);
                PromptUtils.showToast("上传图片出错");
            }
        });
    }

    private void createReturnOrder() {
        String reason = mRefundReasonTextView.getText().toString().trim();
        String content = mRefundInstructionsEditText.getText().toString().trim();
        String money = mRefundMoneyEditText.getText().toString().trim();
        if (TextUtils.isEmpty(reason)) {
            PromptUtils.showToast("请选择退货原因");
            return;
        }

        if (TextUtils.isEmpty(money)) {
            PromptUtils.showToast("请输入退款金额");
            return;
        }
        RequestParams requestParams = new RequestParams();
        requestParams.put("uid", userInfo.getUid());
        requestParams.put("gid", goodsMode.getGid());
        requestParams.put("spec_id", goodsMode.getSpec_id());
        requestParams.put("order_no", orderMode.getOrder_no());
        requestParams.put("return_type", isJustRefundMoney ? "1" : "2");
        requestParams.put("return_reason", mChooseRefundReason.getName());
        requestParams.put("return_content", TextUtils.isEmpty(content) ? "" : content);
        requestParams.put("amount", money);
        requestParams.put("images", mServiceImages.size() == 0 ? "" : changeTOStringOfImages());
        HttpUtil.postWithSign(userInfo.getToken(), IConstants.sOrder + "/return", requestParams, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                LogUtils.d("生成退单返回：", response.toString());
                if (response.optInt("ret") >= 0) {
                    PromptUtils.showToast("提交申请成功");
                    cancelLoading();
                    finish();
                } else {
                    PromptUtils.showToast(response.optString("msg"));
                    cancelLoading();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);
                LogUtils.d("生成退单失败返回：", responseString);
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
        } else if (resultCode == 1001) {
            mChooseRefundReason = (RefundReasonMode) data.getExtras().get("data");
            if (!TextUtils.isEmpty(mChooseRefundReason.getName())) {
                mRefundReasonTextView.setText(mChooseRefundReason.getName());
            }
        }
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

    class RefundReasonAdapter extends BaseAdapter {
        ArrayList<RefundReasonMode> refundReasonModes = new ArrayList<RefundReasonMode>();


        public RefundReasonAdapter(ArrayList<RefundReasonMode> refundReasonModes) {
            this.refundReasonModes = refundReasonModes;
        }

        public void flush(ArrayList<RefundReasonMode> refundReasonModes) {
            this.refundReasonModes = refundReasonModes;
            this.notifyDataSetChanged();
        }

        @Override
        public int getCount() {
            return refundReasonModes.size();
        }

        @Override
        public Object getItem(int i) {
            return null;
        }

        @Override
        public long getItemId(int i) {
            return 0;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            ViewHolder viewHolder;
            if (view == null) {
                viewHolder = new ViewHolder();
                view = View.inflate(ApplySalesAfterActivity.this, R.layout.refund_reason_item_list, null);
                viewHolder.tv_reason = (TextView) view.findViewById(R.id.tv_reason);
                view.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) view.getTag();
            }
            viewHolder.tv_reason.setText(refundReasonModes.get(i).getName());
            return view;
        }

        class ViewHolder {
            TextView tv_reason;
        }
    }

    class TourPicAdapter extends SimpleBaseAdapter {
        private int size = 0;

        public void flush() {
            if (size == mPhotoList.size()) {
                return;
            }
            mObjectList.clear();
            for (PhotoInfo photoInfo : mPhotoList) {

                mObjectList.add(photoInfo);

            }
            if (mPhotoList.size() < 3 || mObjectList.size() == 0) {
                mObjectList.add(mAdd);
            }
            size = mPhotoList.size();
            this.notifyDataSetChanged();
        }


        @Override
        public int getCount() {
            return mObjectList.size();
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            ViewHolder viewHolder;
            if (view == null) {
                view = LayoutInflater.from(ApplySalesAfterActivity.this).inflate(R.layout.tour_send_grid_item, null);
                viewHolder = new ViewHolder();
                viewHolder.mImageView = (ImageView) view.findViewById(R.id.image);
                int mPicSize = (DisplayUtils.getDisplayWidth() - DisplayUtils.dp2px(55)) / 4;
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
     * 本地填充退货原因数据
     */
    private void setReason() {
        RefundReasonMode refundReasonMode = new RefundReasonMode();
        refundReasonMode.setId("1");
        refundReasonMode.setName("未收到货");
        RefundReasonMode refundReasonMode1 = new RefundReasonMode();
        refundReasonMode1.setId("2");
        refundReasonMode1.setName("商品缺货");
        RefundReasonMode refundReasonMode2 = new RefundReasonMode();
        refundReasonMode2.setId("3");
        refundReasonMode2.setName("协商一致退款");
        RefundReasonMode refundReasonMode3 = new RefundReasonMode();
        refundReasonMode3.setId("4");
        refundReasonMode3.setName("重复下单");
        RefundReasonMode refundReasonMode4 = new RefundReasonMode();
        refundReasonMode4.setId("4");
        refundReasonMode4.setName("收货人信息错误");
        RefundReasonMode refundReasonMode5 = new RefundReasonMode();
        refundReasonMode5.setId("5");
        refundReasonMode5.setName("其他原因");
        RefundReasonMode refundReasonMode6 = new RefundReasonMode();
        refundReasonMode6.setId("11");
        refundReasonMode6.setName("七天无理由退货");
        RefundReasonMode refundReasonMode7 = new RefundReasonMode();
        refundReasonMode7.setId("12");
        refundReasonMode7.setName("收到商品破损");
        RefundReasonMode refundReasonMode8 = new RefundReasonMode();
        refundReasonMode8.setId("13");
        refundReasonMode8.setName("商品错发/漏发");
        RefundReasonMode refundReasonMode9 = new RefundReasonMode();
        refundReasonMode9.setId("14");
        refundReasonMode9.setName("收到商品与描述不符");
        RefundReasonMode refundReasonMode10 = new RefundReasonMode();
        refundReasonMode10.setId("15");
        refundReasonMode10.setName("商品质量问题");

        mRefundMoneyReasons.add(refundReasonMode);
        mRefundMoneyReasons.add(refundReasonMode1);
        mRefundMoneyReasons.add(refundReasonMode2);
        mRefundMoneyReasons.add(refundReasonMode3);
        mRefundMoneyReasons.add(refundReasonMode4);
        mRefundMoneyReasons.add(refundReasonMode5);

        mRefundGoodsReasons.add(refundReasonMode6);
        mRefundGoodsReasons.add(refundReasonMode7);
        mRefundGoodsReasons.add(refundReasonMode8);
        mRefundGoodsReasons.add(refundReasonMode9);
        mRefundGoodsReasons.add(refundReasonMode10);
        mRefundGoodsReasons.add(refundReasonMode5);
    }
}
