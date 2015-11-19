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
import com.sensu.android.zimaogou.activity.login.LoginActivity;
import com.sensu.android.zimaogou.activity.mycenter.CouponActivity;
import com.sensu.android.zimaogou.activity.mycenter.OrderActivity;
import com.sensu.android.zimaogou.external.greendao.helper.GDUserInfoHelper;
import com.sensu.android.zimaogou.external.greendao.model.UserInfo;
import com.sensu.android.zimaogou.utils.PromptUtils;

/**
 * Created by zhangwentao on 2015/11/10.
 */
public class MeFragment extends BaseFragment implements View.OnClickListener {

    private ImageView mHeadPicImageView;

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
                startActivity(new Intent(mParentActivity, OrderActivity.class).putExtra("type",4));
                break;
            case R.id.my_coupon:
                //TODO 我的优惠券
                PromptUtils.showToast("我的优惠券");
                startActivity(new Intent(mParentActivity, CouponActivity.class));
                break;
            case R.id.my_collection:
                //TODO 我的收藏
                PromptUtils.showToast("我的收藏");
                break;
            case R.id.take_goods_address:
                //TODO 收货地址
                PromptUtils.showToast("收货地址");
                break;
            case R.id.recommend_friends:
                //TODO 推荐给好友
                PromptUtils.showToast("推荐给好友");
                break;
            case R.id.online_service:
                //TODO 在线客服
                PromptUtils.showToast("在线客服");
                break;
            case R.id.service_phone:
                //TODO 客服电话
                PromptUtils.showToast("客服电话");
//                String tel = ((TextView) mActivity.findViewById(R.id.service_phone_num)).getText().toString().trim();
//                Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + tel));
//                mActivity.startActivity(intent);
                break;
            case R.id.setting:
                //TODO 设置
                PromptUtils.showToast("设置");
                break;
        }
    }
}
