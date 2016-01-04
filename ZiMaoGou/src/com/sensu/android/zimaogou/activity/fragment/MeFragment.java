package com.sensu.android.zimaogou.activity.fragment;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.sensu.android.zimaogou.R;
import com.sensu.android.zimaogou.activity.OnlineServiceActivity;
import com.sensu.android.zimaogou.activity.login.LoginActivity;
import com.sensu.android.zimaogou.activity.mycenter.*;
import com.sensu.android.zimaogou.external.greendao.helper.GDUserInfoHelper;
import com.sensu.android.zimaogou.external.greendao.model.UserInfo;
import com.sensu.android.zimaogou.utils.ImageUtils;
import com.sensu.android.zimaogou.utils.PromptUtils;
import com.sensu.android.zimaogou.utils.TextUtils;
import com.umeng.socialize.ShareAction;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.media.UMImage;

/**
 * Created by zhangwentao on 2015/11/10.
 */
public class MeFragment extends BaseFragment implements View.OnClickListener {

    private ImageView mHeadPicImageView,mNumOfMessage;
    private TextView mNicknameTextView,mLoginTextView,mNumOfOrder1,mNumOfOrder2,mNumOfOrder3;

//    String shareUrl = URL.BASEURL+"/download";
//    String ImageUrl = URL.BASEURL+"/images/img_app_1.png";
    String shareUrl = "http://www.baidu.com";
    String ImageUrl = "http://www.baidu.com/images/img_app_1.png";
    String shareContent = "自贸购";
    private UserInfo userInfo;
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
        mNumOfOrder1 = (TextView) mParentActivity.findViewById(R.id.tv_numOfOrder1);
        mNumOfOrder2 = (TextView) mParentActivity.findViewById(R.id.tv_numOfOrder2);
        mNumOfOrder3 = (TextView) mParentActivity.findViewById(R.id.tv_numOfOrder3);
        mNumOfMessage = (ImageView) mParentActivity.findViewById(R.id.tv_numOfMessage);

        mHeadPicImageView.setOnClickListener(this);
        mLoginTextView = (TextView) mParentActivity.findViewById(R.id.login_register);
        mNicknameTextView = (TextView) mParentActivity.findViewById(R.id.tv_nickname);
        mParentActivity.findViewById(R.id.login_register).setOnClickListener(this);
        mParentActivity.findViewById(R.id.wait_pay).setOnClickListener(this);
        mParentActivity.findViewById(R.id.wait_receive).setOnClickListener(this);
        mParentActivity.findViewById(R.id.sales_return).setOnClickListener(this);
        mParentActivity.findViewById(R.id.all_orders).setOnClickListener(this);

        mParentActivity.findViewById(R.id.my_coupon).setOnClickListener(this);
        mParentActivity.findViewById(R.id.my_collection).setOnClickListener(this);
        mParentActivity.findViewById(R.id.my_travel).setOnClickListener(this);
        mParentActivity.findViewById(R.id.take_goods_address).setOnClickListener(this);

        mParentActivity.findViewById(R.id.recommend_friends).setOnClickListener(this);
        mParentActivity.findViewById(R.id.online_service).setOnClickListener(this);
        mParentActivity.findViewById(R.id.service_phone).setOnClickListener(this);
        mParentActivity.findViewById(R.id.setting).setOnClickListener(this);
        mParentActivity.findViewById(R.id.rl_message).setOnClickListener(this);
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (hidden) {

        } else {}
    }

    @Override
    public void onResume() {
        super.onResume();
        userInfo = GDUserInfoHelper.getInstance(mParentActivity).getUserInfo();
        flushUi();
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
//                GDUserInfoHelper.getInstance(mParentActivity).insertUserInfo(userInfo);
                startActivity(new Intent(mParentActivity, MyInformationActivity.class));
                break;
            case R.id.login_register:
//                UserInfo userInfo1 = GDUserInfoHelper.getInstance(mParentActivity).getUserInfo();
                mParentActivity.startActivity(new Intent(mParentActivity, LoginActivity.class));

                break;
            case R.id.rl_message:
                if(checkLogin()) {
                    mParentActivity.startActivity(new Intent(mParentActivity, MessageActivity.class));
                }else{
                    startActivity(new Intent(mParentActivity,LoginActivity.class));
                }
                break;
            case R.id.wait_pay:
                //TODO 进入待付款页面
                if(checkLogin()) {
                    startActivity(new Intent(mParentActivity, OrderActivity.class).putExtra("type", 1));
                }else{
                    startActivity(new Intent(mParentActivity,LoginActivity.class));
                }
                break;
            case R.id.wait_receive:
                //TODO 进入待收货界面
                if(checkLogin()) {
                    startActivity(new Intent(mParentActivity, OrderActivity.class).putExtra("type",2));
                }else{
                    startActivity(new Intent(mParentActivity,LoginActivity.class));
                }

                break;
            case R.id.sales_return:
                //TODO 退货单
                if(checkLogin()) {
                    startActivity(new Intent(mParentActivity, OrderActivity.class).putExtra("type",3));
                }else{
                    startActivity(new Intent(mParentActivity,LoginActivity.class));
                }

                break;
            case R.id.all_orders:
                //TODO 所有订单
                if(checkLogin()) {
                    startActivity(new Intent(mParentActivity, OrderActivity.class));
                }else{
                    startActivity(new Intent(mParentActivity,LoginActivity.class));
                }

                break;
            case R.id.my_coupon:
                //TODO 我的优惠券
                if(checkLogin()) {
                    startActivity(new Intent(mParentActivity, CouponActivity.class).putExtra("type","isMy"));
                }else{
                    startActivity(new Intent(mParentActivity,LoginActivity.class));
                }

                break;
            case R.id.my_collection:
                //TODO 我的收藏
                if(checkLogin()) {
                    startActivity(new Intent(mParentActivity, CollectActivity.class));
                }else{
                    startActivity(new Intent(mParentActivity,LoginActivity.class));
                }
                break;
            case R.id.my_travel:
                if(checkLogin()) {
                    startActivity(new Intent(mParentActivity, MyTravelActivity.class));
                }else{
                    startActivity(new Intent(mParentActivity,LoginActivity.class));
                }
                break;
            case R.id.take_goods_address:
                //TODO 收货地址
                if(checkLogin()) {
                    startActivity(new Intent(mParentActivity, ReceiverAddressActivity.class));
                }else{
                    startActivity(new Intent(mParentActivity,LoginActivity.class));
                }
                break;
            case R.id.recommend_friends:
                //TODO 推荐给好友
                PromptUtils.showToast("推荐给好友");
                ShareDialog() ;
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
    private UMShareListener umShareListener = new UMShareListener() {
        @Override
        public void onResult(SHARE_MEDIA platform) {
            Toast.makeText(mParentActivity, platform + " 分享成功啦", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onError(SHARE_MEDIA platform, Throwable t) {
            Toast.makeText(mParentActivity,platform + " 分享失败啦", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onCancel(SHARE_MEDIA platform) {
            Toast.makeText(mParentActivity,platform + " 分享取消了", Toast.LENGTH_SHORT).show();
        }
    };





    private void flushUi(){
        if(userInfo == null){
            mNumOfMessage.setVisibility(View.INVISIBLE);
            mNumOfOrder1.setVisibility(View.INVISIBLE);
            mNumOfOrder2.setVisibility(View.INVISIBLE);
            mNumOfOrder3.setVisibility(View.INVISIBLE);
            mNicknameTextView.setVisibility(View.GONE);
            mLoginTextView.setVisibility(View.VISIBLE);
            mHeadPicImageView.setImageResource(R.drawable.head_pic_default);

        }else{
            mNumOfMessage.setVisibility(View.VISIBLE);
            mNumOfOrder1.setVisibility(View.VISIBLE);
            mNumOfOrder2.setVisibility(View.VISIBLE);
            mNumOfOrder3.setVisibility(View.VISIBLE);


            mNicknameTextView.setVisibility(View.VISIBLE);
            mLoginTextView.setVisibility(View.GONE);
            if(TextUtils.isEmpty(userInfo.getName())){
                mNicknameTextView.setText(userInfo.getMobile());
            }else{
                mNicknameTextView.setText(userInfo.getName());
            }
            ImageUtils.displayImage(userInfo.getAvatar(), mHeadPicImageView,ImageUtils.mHeadDefaultOptions);
        }
    }

    private boolean checkLogin(){
        if(userInfo == null){
            PromptUtils.showToast("请先登录");
            return false;
        }
        return true;
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        /** attention to this below ,must add this**/
        UMShareAPI.get(mParentActivity).onActivityResult(requestCode, resultCode, data);

    }

    /**
     * 分享
     */
    Dialog mShareDialog;

    private void ShareDialog() {
        mShareDialog = new Dialog(mParentActivity, R.style.dialog);
        mShareDialog.setCancelable(true);
        mShareDialog.setContentView(R.layout.share_dialog);
        LinearLayout ll_wx = (LinearLayout) mShareDialog.findViewById(R.id.ll_wx);
        LinearLayout ll_friends = (LinearLayout) mShareDialog.findViewById(R.id.ll_friends);
        LinearLayout ll_sina = (LinearLayout) mShareDialog.findViewById(R.id.ll_sina);
        ll_sina.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                UMImage image = new UMImage(mParentActivity, "http://www.umeng.com/images/pic/social/integrated_3.png");
                new ShareAction(mParentActivity).setPlatform(SHARE_MEDIA.SINA).setCallback(umShareListener)
                        .withText("hello umeng video")
                        .withTargetUrl("http://www.baidu.com")
                        .withMedia(image)
                        .share();
                mShareDialog.dismiss();
            }
        });
        ll_wx.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new ShareAction(mParentActivity).setPlatform(SHARE_MEDIA.WEIXIN).setCallback(umShareListener)
                        .withText("hello wx")
                        .share();
                mShareDialog.dismiss();
            }
        });
        ll_friends.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new ShareAction(mParentActivity).setPlatform(SHARE_MEDIA.WEIXIN_CIRCLE).setCallback(umShareListener)
                        .withText("hello umeng")
                        .share();
                mShareDialog.dismiss();
            }
        });

        mShareDialog.show();
    }
}
