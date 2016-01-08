package com.sensu.android.zimaogou.activity;

import android.app.Activity;
import android.app.Dialog;
import android.graphics.drawable.AnimationDrawable;
import android.os.Build;
import android.os.Bundle;
import android.view.*;
import android.widget.ImageView;
import android.widget.Toast;
import com.sensu.android.zimaogou.R;
import com.sensu.android.zimaogou.utils.NetworkTypeUtils;
import com.sensu.android.zimaogou.widget.ExceptionLinearLayout;

/**
 * Created by winter on 2015/9/23.
 *
 * @author winter
 */
public class BaseActivity extends Activity {
    public View ExceptionView;
    public ExceptionLinearLayout exceptionLinearLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //去掉头
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        /**
         * 设置通知栏与app同色  4.4以上版本有效  在xml布局添加下面两属性
         * android:clipToPadding="false"
         * android:fitsSystemWindows="true"
         */

//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
//            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
//            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
//        }
        ExceptionView = View.inflate(this, R.layout.exception_layout,null);
        exceptionLinearLayout = (ExceptionLinearLayout) ExceptionView.findViewById(R.id.ll_exception);
        if(!NetworkTypeUtils.isNetWorkAvailable()){
            Toast toast = Toast.makeText(this,"您的网络开了小差哦！",Toast.LENGTH_LONG);
            toast.setGravity(Gravity.CENTER,0,0);
            toast.show();
        }
    }
    Dialog loadingDialog;
    ImageView infoOperatingIV;
    boolean isLoading = false;
    public void showLoading(){
        loadingDialog = new Dialog(this,R.style.dialog);
        loadingDialog.setCancelable(false);
        loadingDialog.setContentView(R.layout.show_progress_lay_more);
        infoOperatingIV = (ImageView) loadingDialog
                .findViewById(R.id.img_progress);
        // 单张图片旋转动画
        // Animation operatingAnim = AnimationUtils.loadAnimation(this,
        // R.anim.progress);
        // LinearInterpolator lin = new LinearInterpolator();
        // operatingAnim.setInterpolator(lin);
        // if (operatingAnim != null) {
        // infoOperatingIV.startAnimation(operatingAnim);
        // }

        // 多张图片动画
        final AnimationDrawable animDance = (AnimationDrawable) infoOperatingIV
                .getBackground();
        infoOperatingIV.getViewTreeObserver().addOnPreDrawListener(
                new ViewTreeObserver.OnPreDrawListener() {
                    @Override
                    public boolean onPreDraw() {
                        animDance.start();
                        return true;
                    }
                });
        isLoading = true;
        loadingDialog.show();
    }
    public void cancelLoading(){
        if(!isLoading){
            return;
        }
        isLoading = false;
        infoOperatingIV.clearAnimation();
        loadingDialog.dismiss();
    }
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (keyCode == KeyEvent.KEYCODE_BACK
                && event.getRepeatCount() == 0) {
            if(isLoading || loadingDialog != null){
                loadingDialog.dismiss();
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
