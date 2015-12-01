package com.sensu.android.zimaogou.external.umeng.share;

import android.content.Context;
import com.sensu.android.zimaogou.R;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.controller.UMServiceFactory;
import com.umeng.socialize.controller.UMSocialService;
import com.umeng.socialize.media.SinaShareContent;
import com.umeng.socialize.media.UMImage;
import com.umeng.socialize.sso.SinaSsoHandler;
import com.umeng.socialize.weixin.controller.UMWXHandler;
import com.umeng.socialize.weixin.media.CircleShareContent;
import com.umeng.socialize.weixin.media.WeiXinShareContent;

/**
 * Created by zhangwentao on 2015/12/1.
 */
public class UmengShare {

    private static UmengShare sUmengShare;
    private static Context mContext;
    public static final String DESCRIPTOR = "com.umeng.share";
    public final UMSocialService mController = UMServiceFactory
            .getUMSocialService(DESCRIPTOR);
    private SHARE_MEDIA mPlatform = SHARE_MEDIA.SINA;
    //    String shareUrl = URL.BASEURL+"/download";
//    String ImageUrl = URL.BASEURL+"/images/img_app_1.png";
    String shareUrl = "http://www.baidu.com";
    String ImageUrl = "http://www.baidu.com/images/img_app_1.png";
    String shareContent = "自贸购";

    public static UmengShare getInstance(Context context) {
        if (sUmengShare == null) {
            sUmengShare = new UmengShare();
        }
        mContext = context;
        return sUmengShare;
    }

    /**
     * 配置分享平台参数</br>
     */
    public void configPlatforms() {
        // 添加新浪SSO授权
        mController.getConfig().setSsoHandler(new SinaSsoHandler());

        // 添加微信、微信朋友圈平台
        addWXPlatform();
    }


    /**
     * 根据不同的平台设置不同的分享内容</br>
     */
    public void setShareContent() {

        // 配置SSO
        mController.getConfig().setSsoHandler(new SinaSsoHandler());

        // APP ID：201874, API
        // * KEY：28401c0964f04a72a14c812d6132fcef, Secret
        // * Key：3bf66e42db1e4fa9829b955cc300b737.
        // RenrenSsoHandler renrenSsoHandler = new
        // RenrenSsoHandler(SwimmingDetailActivity.this,
        // "201874", "28401c0964f04a72a14c812d6132fcef",
        // "3bf66e42db1e4fa9829b955cc300b737");
        // mController.getConfig().setSsoHandler(renrenSsoHandler);

        UMImage localImage = new UMImage(mContext,
                R.drawable.index_14);
        UMImage urlImage = new UMImage(mContext,
                ImageUrl);
        // UMImage resImage = new UMImage(SwimmingDetailActivity.this,
        // R.drawable.icon);

        // // 视频分享
        // UMVideo video = new UMVideo(
        // "http://v.youku.com/v_show/id_XNTc0ODM4OTM2.html");
        // //
        // vedio.setThumb("http://www.umeng.com/images/pic/home/social/img-1.png");
        // video.setTitle("友盟社会化组件视频");
        // video.setThumb(urlImage);
        //
        // UMusic uMusic = new UMusic(
        // "http://music.huoxing.com/upload/20130330/1364651263157_1085.mp3");
        // uMusic.setAuthor("umeng");
        // uMusic.setTitle("天籁之音");
        // uMusic.setThumb(urlImage);
        // //
        // uMusic.setThumb("http://www.umeng.com/images/pic/social/chart_1.png");

        WeiXinShareContent weixinContent = new WeiXinShareContent();
        weixinContent.setShareContent(shareContent);
        weixinContent.setTitle("自贸购");
        weixinContent.setTargetUrl(shareUrl);
        weixinContent.setShareMedia(urlImage);
        mController.setShareMedia(weixinContent);

        // 设置朋友圈分享的内容
        // CircleShareContent circleMedia = new CircleShareContent();
        // weixinContent.setShareContent("微信分享-肌肤管家内容");
        // weixinContent.setTitle("肌肤管家");
        // circleMedia.setShareImage(urlImage);
        // // circleMedia.setShareMedia(uMusic);
        // // circleMedia.setShareMedia(video);
        // circleMedia.setTargetUrl(shareUrl);
        // circleMedia.setShareMedia(urlImage);
        // mController.setShareMedia(circleMedia);

        // 视频分享
        // UMVideo umVideo = new UMVideo(
        // "http://v.youku.com/v_show/id_XNTc0ODM4OTM2.html");
        // umVideo.setThumb("http://www.umeng.com/images/pic/home/social/img-1.png");
        // umVideo.setTitle("友盟社会化组件视频");

        SinaShareContent sinaContent = new SinaShareContent(urlImage);
        sinaContent.setShareContent(shareContent + "查看详情：" + shareUrl);
        sinaContent.setTitle("自贸购");
        mController.setShareMedia(sinaContent);

        // 设置朋友圈分享的内容
        CircleShareContent circleMedia = new CircleShareContent();
        circleMedia.setShareContent(shareContent);
        circleMedia.setTitle("自贸购");
        circleMedia.setShareImage(urlImage);
        // circleMedia.setShareMedia(uMusic);
        // circleMedia.setShareMedia(video);
        circleMedia.setTargetUrl(shareUrl);
        mController.setShareMedia(circleMedia);
        // TwitterShareContent twitterShareContent = new TwitterShareContent();
        // twitterShareContent
        // .setShareContent("来自友盟社会化组件（SDK）让移动应用快速整合社交分享功能，TWITTER");
        // twitterShareContent.setShareMedia(localImage);
        // mController.setShareMedia(twitterShareContent);
        //
        // GooglePlusShareContent googlePlusShareContent = new
        // GooglePlusShareContent();
        // googlePlusShareContent
        // .setShareContent("来自友盟社会化组件（SDK）让移动应用快速整合社交分享功能，G+");
        // googlePlusShareContent.setShareMedia(localImage);
        // mController.setShareMedia(googlePlusShareContent);

    }

    /**
     * @return
     * @功能描述 : 添加微信平台分享
     */
    private void addWXPlatform() {
        // 注意：在微信授权的时候，必须传递appSecret
        // wx967daebe835fbeac是你在微信开发平台注册应用的AppID, 这里需要替换成你注册的AppID

        String appId = "wx22e5d0e3792db66a";
        String appSecret = "dfbe36c15fe8317f34ad8a17170c7a4c";

        // 添加微信平台
        UMWXHandler wxHandler = new UMWXHandler(mContext,
                appId, appSecret);
        wxHandler.addToSocialSDK();
        // 支持微信朋友圈
        UMWXHandler wxCircleHandler = new UMWXHandler(
                mContext, appId, appSecret);
        wxCircleHandler.setToCircle(true);
        wxCircleHandler.addToSocialSDK();

    }

}
