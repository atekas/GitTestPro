package com.sensu.android.zimaogou.activity.mycenter;

import android.app.Dialog;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.sensu.android.zimaogou.IConstants;
import com.sensu.android.zimaogou.R;
import com.sensu.android.zimaogou.activity.BaseActivity;
import com.sensu.android.zimaogou.external.greendao.helper.GDUserInfoHelper;
import com.sensu.android.zimaogou.external.greendao.model.UserInfo;
import com.sensu.android.zimaogou.utils.*;
import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.File;

/**
 * Created by qi.yang on 2015/11/26.
 */
public class MyInformationActivity extends BaseActivity {
    ImageView mHeadImageView;
    EditText mNicknameEditText;
    TextView mSexTextView, mPhoneTextView;
    private int IMAGE_REQUEST_CODE = 6;
    private UserInfo userInfo;
    private String avatar="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.my_infor_activity);

        initView();
    }

    private void initView() {
        mHeadImageView = (ImageView) findViewById(R.id.img_head);
        mNicknameEditText = (EditText) findViewById(R.id.et_nickname);
        mSexTextView = (TextView) findViewById(R.id.tv_sex);
        mPhoneTextView = (TextView) findViewById(R.id.tv_phone);
        findViewById(R.id.back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });


    }

    @Override
    protected void onResume() {
        super.onResume();
        userInfo = GDUserInfoHelper.getInstance(this).getUserInfo();
        if (TextUtils.isEmpty(userInfo.getName())) {

            mNicknameEditText.setText("");
        } else {
            mNicknameEditText.setText(userInfo.getName());
        }
        if (TextUtils.isEmpty(userInfo.getSex())) {
            mSexTextView.setText("男");
        } else {
            mSexTextView.setText(userInfo.getSex());
        }
        mPhoneTextView.setText(userInfo.getMobile());
        if(TextUtils.isEmpty(userInfo.getMobile())){
            bindPhone = false;

        }else{
            bindPhone = true;
        }
        ImageUtils.displayImage(userInfo.getAvatar(), mHeadImageView,ImageUtils.mHeadDefaultOptions);
    }

    /**
     * 头像
     */
    public void HeadClick(View v) {
        getPhotoFromAlum();
    }

    /**
     * 性别
     */
    public void SexClick(View v) {
        chooseDialog();
    }

    /**
     * 手机
     * @param v
     */
    boolean bindPhone = true;
    public void PhoneClick(View v){
//        if(TextUtils.isEmpty(userInfo.getMobile())){
//            startActivity(new Intent(this,BindPhoneActivity.class));
//
//        }else{
//            startActivity(new Intent(this,UpdatePasswordActivity.class));
//        }
        if(bindPhone){
            startActivity(new Intent(this,UpdateBindPhoneActivity.class));


        }else{
            startActivity(new Intent(this,BindPhoneActivity.class));

        }

    }
    /**
     * 选择对话框
     */
    Dialog mSexChooseDialog;

    public void chooseDialog() {
        mSexChooseDialog = new Dialog(this, R.style.notParentDialog);
        mSexChooseDialog.setCancelable(true);
        mSexChooseDialog.setContentView(R.layout.sex_choose_dialog);
        TextView tv_cancel = (TextView) mSexChooseDialog.findViewById(R.id.tv_cancel);
        TextView tv_man = (TextView) mSexChooseDialog.findViewById(R.id.tv_man);
        TextView tv_woman = (TextView) mSexChooseDialog.findViewById(R.id.tv_woman);
        tv_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mSexChooseDialog.dismiss();
            }
        });
        tv_man.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mSexTextView.setText("男");
                mSexChooseDialog.dismiss();
            }
        });
        tv_woman.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mSexTextView.setText("女");
                mSexChooseDialog.dismiss();
            }
        });
        WindowManager m = this.getWindowManager();

        Window dialogWindow = mSexChooseDialog.getWindow();

//        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
//        dialogWindow.setGravity(Gravity.TOP);
//        lp.y = DisplayUtils.dp2px(50);
//        dialogWindow.setAttributes(lp);

        Display d = m.getDefaultDisplay(); // 获取屏幕宽、高用
        WindowManager.LayoutParams p = dialogWindow.getAttributes(); // 获取对话框当前的参数值
//        p.height = (int) d.getHeight() ; // 高度设置为屏幕
        p.width = (int) d.getWidth(); // 宽度设置为屏幕
        dialogWindow.setAttributes(p);
        mSexChooseDialog.show();
    }


    /**
     * 从相册选择
     */
    public String IMAGE_UNSPECIFIED = "image/*";

    private void getPhotoFromAlum() {
        Intent intent2 = new Intent(Intent.ACTION_PICK, null);
        intent2.setDataAndType(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                IMAGE_UNSPECIFIED);
        startActivityForResult(intent2, IMAGE_REQUEST_CODE);
    }

    private Uri uri = null;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode != RESULT_CANCELED) {
            String state = Environment.getExternalStorageState();
            switch (requestCode) {
                case 6:
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
                    avatar = path;
                    int degree = ImageUtils.readPictureDegree(path);
                    Bitmap bmpOk = ImageUtils.rotateToDegrees(BitmapUtils.getSampleBitmap(path, 800, 800).getBitmap(), degree);
                    mHeadImageView.setImageBitmap(bmpOk);
                    try {
                        avatar = BitmapUtils.saveImg(bmpOk,"head");
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    break;
//                case CAMERA_REQUEST_CODE:
//                    if (state.equals(Environment.MEDIA_MOUNTED)) {
//                        File tempFile = new File(SwimmingpoolAppApplication.getPhotographpath());
//                        startPhotoZoom(Uri.fromFile(tempFile));
//                    } else {
//                        //ToastUtil.toastInfo(MainActivity.this, "未找到存储卡，无法存储照片！");
//                    }
//                    break;
//
//                case RESULT_REQUEST_CODE:
//                    if (data != null) {
//                        if (state.equals(Environment.MEDIA_MOUNTED)) {
//                            getImageToView(data);
//                        } else {
//                            // ToastUtil.toastInfo(PerfectPersonDateActivity.this,
//                            // "未找到存储卡，无法存储照片！");
//                        }
//                    }
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

    /**
     * 保存
     *
     * @param v
     */
    public void SaveClick(View v) {
        if(TextUtils.isEmpty(avatar)) {
            postInfo();
        }else{
            showLoading();
            HttpUtil.postImage(userInfo.getUid(),userInfo.getToken(),avatar,new JsonHttpResponseHandler(){
                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                    super.onSuccess(statusCode, headers, response);
                    LogUtils.d("头像上传返回：",response.toString());
                    try {
                        avatar = response.getJSONObject("data").getString("url");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    postInfo();
                }
            });
        }
    }
    private void postInfo(){
        RequestParams requestParams = new RequestParams();
        requestParams.put("name", mNicknameEditText.getText().toString());
        requestParams.put("uid", userInfo.getUid());
        requestParams.put("token", userInfo.getToken());
        requestParams.put("sex", mSexTextView.getText().toString());
        if(!TextUtils.isEmpty(avatar)){
            requestParams.put("avatar", avatar);
        }

        HttpUtil.postWithSign(userInfo.getToken(), IConstants.sUpdateUserInfo, requestParams, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject responseString) {
                super.onSuccess(statusCode, headers, responseString);

                Log.d("返回值：", responseString.toString());
                if (responseString.optString("ret").equals("0")) {
                    cancelLoading();
                    PromptUtils.showToast("保存成功");

                    userInfo.setSex(mSexTextView.getText().toString());
                    userInfo.setName(mNicknameEditText.getText().toString());
                    if(!TextUtils.isEmpty(avatar)){
                        userInfo.setAvatar(avatar);
                    }
                    GDUserInfoHelper.getInstance(MyInformationActivity.this).updateUserInfo(userInfo);
                }
            }
        });
    }
}
