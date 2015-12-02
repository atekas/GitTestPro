package com.sensu.android.zimaogou.activity.fragment;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.sensu.android.zimaogou.R;
import com.sensu.android.zimaogou.activity.OnlineServiceActivity;
import com.sensu.android.zimaogou.activity.login.LoginActivity;
import com.sensu.android.zimaogou.activity.mycenter.*;
import com.sensu.android.zimaogou.external.greendao.helper.GDUserInfoHelper;
import com.sensu.android.zimaogou.external.greendao.model.UserInfo;
import com.sensu.android.zimaogou.utils.PromptUtils;
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
 * Created by zhangwentao on 2015/11/10.
 */
public class MeFragment extends BaseFragment implements View.OnClickListener {

    private ImageView mHeadPicImageView;

    public static final String DESCRIPTOR = "com.umeng.share";
    private final UMSocialService mController = UMServiceFactory
            .getUMSocialService(DESCRIPTOR);
    private SHARE_MEDIA mPlatform = SHARE_MEDIA.SINA;
//    String shareUrl = URL.BASEURL+"/download";
//    String ImageUrl = URL.BASEURL+"/images/img_app_1.png";
    String shareUrl = "http://www.baidu.com";
    String ImageUrl = "http://www.baidu.com/images/img_app_1.png";
    String shareContent = "自贸购";
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.me_fragment, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        initView();
    }

    @Override
    protected void initView() {

        mHeadPicImageView = (ImageView) mParentActivity.findViewById(R.id.head_pic);

        mHeadPicImageView.setOnClickListener(this);
        mParentActivity.findViewById(R.id.login_register).setOnClickListener(this);
        mParentActivity.findViewById(R.id.wait_pay).setOnClickListener(this);
        mParentActivity.findViewById(R.id.wait_receive).setOnClickListener(this);
        mParentActivity.findViewById(R.id.sales_return).setOnClickListener(this);
        mParentActivity.findViewById(R.id.all_orders).setOnClickListener(this);

        mParentActivity.findViewById(R.id.my_coupon).setOnClickListener(this);
        mParentActivity.findViewById(R.id.my_collection).setOnClickListener(this);
        mParentActivity.findViewById(R.id.take_goods_address).setOnClickListener(this);

        mParentActivity.findViewById(R.id.recommend_friends).setOnClickListener(this);
        mParentActivity.findViewById(R.id.online_service).setOnClickListener(this);
        mParentActivity.findViewById(R.id.service_phone).setOnClickListener(this);
        mParentActivity.findViewById(R.id.setting).setOnClickListener(this);
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (hidden) {

        } else {}
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
            case R.id.head_pic:
                //TODO 登陆或者更换头像  判断是否登陆  登陆进入用户信息页面，未登陆进入登陆页面
                PromptUtils.showToast("登陆或者更换头像");
//                UserInfo userInfo = new UserInfo();
//                userInfo.setName("张三");
//                userInfo.setSex("男");
//                userInfo.setPhoneNum("138888888");
//                GDUserInfoHelper.getInstance(mParentActivity).insertUserInfo(userInfo);
                startActivity(new Intent(mParentActivity, MyInformationActivity.class));
                break;
            case R.id.login_register:
//                UserInfo userInfo1 = GDUserInfoHelper.getInstance(mParentActivity).getUserInfo();
//                PromptUtils.showToast(userInfo1.getName() + userInfo1.getSex() + userInfo1.getPhoneNum());
                mParentActivity.startActivity(new Intent(mParentActivity, LoginActivity.class));

                break;
            case R.id.wait_pay:
                //TODO 进入待付款页面
                PromptUtils.showToast("进入待付款页面");
                startActivity(new Intent(mParentActivity, OrderActivity.class).putExtra("type",1));
                break;
            case R.id.wait_receive:
                //TODO 进入待收货界面
                PromptUtils.showToast("进入待收货界面");
                startActivity(new Intent(mParentActivity, OrderActivity.class).putExtra("type",2));
                break;
            case R.id.sales_return:
                //TODO 退货单
                PromptUtils.showToast("退货单");
                startActivity(new Intent(mParentActivity, OrderActivity.class).putExtra("type",3));
                break;
            case R.id.all_orders:
                //TODO 所有订单
                PromptUtils.showToast("所有订单");
                startActivity(new Intent(mParentActivity, OrderActivity.class));
                break;
            case R.id.my_coupon:
                //TODO 我的优惠券
                PromptUtils.showToast("我的优惠券");
                startActivity(new Intent(mParentActivity, CouponActivity.class));
                break;
            case R.id.my_collection:
                //TODO 我的收藏
                PromptUtils.showToast("我的收藏");
                startActivity(new Intent(mParentActivity, CollectActivity.class));
                break;
            case R.id.take_goods_address:
                //TODO 收货地址
                PromptUtils.showToast("收货地址");
                startActivity(new Intent(mParentActivity, ReceiverAddressActivity.class));
                break;
            case R.id.recommend_friends:
                //TODO 推荐给好友
                PromptUtils.showToast("推荐给好友");
                configPlatforms();
                // 设置分享的内容
                setShareContent();
                mController.getConfig().setPlatforms(SHARE_MEDIA.WEIXIN,
                        SHARE_MEDIA.WEIXIN_CIRCLE, SHARE_MEDIA.SINA);
                mController.openShare(mParentActivity, false);
                break;
            case R.id.online_service:
                //TODO 在线客服
                PromptUtils.showToast("在线客服");
                startActivity(new Intent(mParentActivity, OnlineServiceActivity.class));
                break;
            case R.id.service_phone:
                //TODO 客服电话
                PromptUtils.showToast("客服电话");
                String tel = ((TextView) mParentActivity.findViewById(R.id.service_phone_num)).getText().toString().trim();
                Uri uri = Uri.parse("tel:" + tel);
                //跳入到拨号界面  ACTION_DIAL     直接拨号 ACTION_CALL
                Intent intent = new Intent(Intent.ACTION_DIAL, uri);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                mParentActivity.startActivity(intent);
                break;
            case R.id.setting:
                //TODO 设置
                PromptUtils.showToast("设置");
                startActivity(new Intent(mParentActivity, SettingActivity.class));
                break;
        }
    }

    /**
     * 配置分享平台参数</br>
     */
    private void configPlatforms() {
        // 添加新浪SSO授权
        mController.getConfig().setSsoHandler(new SinaSsoHandler());

        // 添加微信、微信朋友圈平台
        addWXPlatform();
    }

    /**
     * @功能描述 : 添加微信平台分享
     * @return
     */
    private void addWXPlatform() {
        // 注意：在微信授权的时候，必须传递appSecret
        // wx967daebe835fbeac是你在微信开发平台注册应用的AppID, 这里需要替换成你注册的AppID

        String appId = "wx22e5d0e3792db66a";
        String appSecret = "dfbe36c15fe8317f34ad8a17170c7a4c";

        // 添加微信平台
        UMWXHandler wxHandler = new UMWXHandler(mParentActivity,
                appId, appSecret);
        wxHandler.addToSocialSDK();
        // 支持微信朋友圈
        UMWXHandler wxCircleHandler = new UMWXHandler(
                mParentActivity, appId, appSecret);
        wxCircleHandler.setToCircle(true);
        wxCircleHandler.addToSocialSDK();

    }


    /**
     * 根据不同的平台设置不同的分享内容</br>
     */
    private void setShareContent() {

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

        UMImage localImage = new UMImage(mParentActivity,
                R.drawable.index_14);
        UMImage urlImage = new UMImage(mParentActivity,
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
        sinaContent.setShareContent(shareContent+"查看详情："+shareUrl);
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
}
