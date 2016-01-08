package com.sensu.android.zimaogou.activity.fragment;

import android.app.Activity;
import android.app.Dialog;
import android.app.Fragment;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import com.sensu.android.zimaogou.R;

/**
 * Created by Administrator on 2015/11/11.
 */
public abstract class BaseFragment extends Fragment {

    protected Activity mParentActivity;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mParentActivity = getActivity();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        initView();
    }
    Dialog loadingDialog;
    ImageView infoOperatingIV;
    boolean isLoading = false;
    public void showLoading(){
        loadingDialog = new Dialog(mParentActivity, R.style.dialog);
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
        infoOperatingIV.clearAnimation();
        loadingDialog.dismiss();
    }
    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    protected abstract void initView();
}
