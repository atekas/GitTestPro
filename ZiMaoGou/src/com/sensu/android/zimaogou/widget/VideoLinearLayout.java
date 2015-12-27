package com.sensu.android.zimaogou.widget;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import cn.com.video.venvy.param.*;
import com.sensu.android.zimaogou.R;
import com.sensu.android.zimaogou.utils.ImageUtils;
import com.sensu.android.zimaogou.utils.PromptUtils;
import com.sensu.android.zimaogou.utils.TextUtils;

/**
 * 视频播放
 * Created by qi.yang on 2015/12/3.
 */
public class VideoLinearLayout extends LinearLayout {
    private JjVideoView mVideoView;//
    private View mLoadBufferView;// //
    private TextView mLoadBufferTextView;// //
    private View mLoadView;// /
    private TextView mLoadText;// /
    private ImageView mCoverImageView,mPlayImageView;
    private String mCoverUrl = "";
    private String mPlayUrl = "";
    public VideoLinearLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }
    boolean isFirst = true;
    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();

        mVideoView = (JjVideoView) findViewById(R.id.video);
        mLoadView = findViewById(R.id.sdk_ijk_progress_bar_layout);
        mLoadText = (TextView) findViewById(R.id.sdk_ijk_progress_bar_text);
        mLoadBufferView = findViewById(R.id.sdk_load_layout);
        mLoadBufferTextView = (TextView) findViewById(R.id.sdk_sdk_ijk_load_buffer_text);
        mCoverImageView = (ImageView) findViewById(R.id.img_cover);
        mPlayImageView = (ImageView) findViewById(R.id.img_play);
        mPlayImageView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if(TextUtils.isEmpty(mPlayUrl)){
                    PromptUtils.showToast("视频地址为空");
                    return;
                }
                playing();
            }
        });
        // mVideoView.setMediaController(new
        // UsetMediaContoller(this));//用户自定义控制器

//        mVideoView.setMediaController(new VideoJjMediaContoller(getContext(), true));// 内部写好的控制器
//        mVideoView.setMediaController(new UsetMediaContoller(getContext()));
        // 参数必须true
        mLoadBufferTextView.setTextColor(Color.RED);
        /***
         * 用户自定义的外链 可 获取外链点击时间
         */
        mVideoView.setOnJjOutsideLinkClick(new OnJjOutsideLinkClickListener() {

            @Override
            public void onJjOutsideLinkClick(String arg0) {
                // TODO Auto-generated method stub
                Log.e("Video++", arg0);
            }
        });
        /***
         * 设置缓冲
         */
        mVideoView.setMediaBufferingView(mLoadBufferView);
        /***
         * 视频开始加载数据
         */
        mVideoView.setOnJjOpenStart(new OnJjOpenStartListener() {

            @Override
            public void onJjOpenStart(String arg0) {
                mLoadText.setText(arg0);
            }
        });
        /***
         * 视频开始播放
         */
        mVideoView.setOnJjOpenSuccess(new OnJjOpenSuccessListener() {

            @Override
            public void onJjOpenSuccess() {
                mLoadView.setVisibility(View.GONE);
            }
        });
        // 缓冲开始
        mVideoView.setOnJjBufferStart(new OnJjBufferStartListener() {

            @Override
            public void onJjBufferStartListener(int arg0) {

            }
        });
        // mVideoView
        // .setOnJjBufferingUpdateListener(new OnJjBufferingUpdateListener() {
        //
        // @Override
        // public void onJjBufferingUpdate(VideoViewBase arg0,int arg1) {
        // // TODO Auto-generated method stub
        // if (mLoadBufferView.getVisibility() == View.VISIBLE) {
        // mLoadBufferTextView.setText(String.valueOf(arg1)
        // + "%");
        // }
        // }
        // });
        // 缓冲完成
        mVideoView.setOnJjBufferComplete(new OnJjBufferCompleteListener() {

            @Override
            public void onJjBufferCompleteListener(int arg0) {
                // TODO Auto-generated method stub

            }
        });
        /***
         * 注意VideoView 要调用下面方法 配置你用户信息
         */
        mVideoView.setVideoJjAppKey("N1-sEJF4l");
        mVideoView.setVideoJjPageName("com.sensu.android.zimaogou");
        mVideoView.setMediaCodecEnabled(true);// 是否开启 硬解 硬解对一些手机有限制
        // 判断是否源 0 代表 8大视频网站url 1代表自己服务器的视频源 2代表直播地址 3代表本地视频(手机上的视频源),4特殊需求
        mVideoView.setVideoJjType(1);
        /***
         * 视频标签显示的时间 默认显示5000毫秒 可设置 传入值 long类型 毫秒
         */
        // 参数代表是否记录视频播放位置 默认false不记录 true代表第二次或多次进入，直接跳转到上次退出的时间点开始播放
        // mVideoView.setVideoJjSaveExitTime(false);
        /***
         * 指定时间开始播放 毫秒
         */
        mVideoView.setVideoJjSeekToTime(Long.valueOf(20000));
        mVideoView.setVideoJjTitle("测试视频");
        mVideoView.setOnJjCompletionListener(new OnJjCompletionListener() {
            @Override
            public void onJjCompletion() {
                isFirst = true;
                mPlayImageView.setImageResource(R.drawable.video);
                mCoverImageView.setVisibility(View.VISIBLE);
                mPlayImageView.setVisibility(View.VISIBLE);
            }
        });
//        mVideoView.setResourceVideo("http://wsv.videojj.com/haoyigou_ctrd.mp4");
    }

    public void setURL(String coverImageUrl,String videoUrl){
        mCoverUrl = coverImageUrl;
        mPlayUrl = videoUrl;
        ImageUtils.displayImage(coverImageUrl,mCoverImageView);
    }

    public void playing(){
        AlphaAnimation outAnimation = new AlphaAnimation(0.0f,1.0f);
        outAnimation.setDuration(1000);
        outAnimation.setStartOffset(500);
        AlphaAnimation inAnimation = new AlphaAnimation(1.0f,0.0f);
        outAnimation.setDuration(1000);
        outAnimation.setStartOffset(500);
        outAnimation.setAnimationListener(new RemoveAnimationListener());
        inAnimation.setAnimationListener(new RemoveAnimationListener());
        if(mVideoView.isPlaying()){
            mVideoView.pause();
            mPlayImageView.setImageResource(R.drawable.video);
            mPlayImageView.setAlpha(1.0f);
            mPlayImageView.startAnimation(outAnimation);
        }else{
            mVideoView.start();
            mPlayImageView.setImageResource(R.drawable.video_stop);
            mPlayImageView.startAnimation(inAnimation);
        }

        if(isFirst) {
            mVideoView.setResourceVideo(mPlayUrl);
            mPlayImageView.setImageResource(R.drawable.video_stop);
            mPlayImageView.startAnimation(outAnimation);
            mCoverImageView.setVisibility(View.GONE);
            isFirst = false;
        }

    }

    private class RemoveAnimationListener implements Animation.AnimationListener {
        // 动画效果执行完时remove
        public void onAnimationEnd(Animation animation) {
            System.out.println("onAnimationEnd");
            if(mVideoView.isPlaying()){
                mPlayImageView.setAlpha(0.0f);
            }else{
                mPlayImageView.setAlpha(1.0f);
            }
        }

        public void onAnimationRepeat(Animation animation) {
            System.out.println("onAnimationRepeat");
        }

        public void onAnimationStart(Animation animation) {
            System.out.println("onAnimationStart");
        }
    }
}
