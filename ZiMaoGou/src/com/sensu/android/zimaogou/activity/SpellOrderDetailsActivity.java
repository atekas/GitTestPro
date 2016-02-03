package com.sensu.android.zimaogou.activity;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.*;
import android.webkit.WebView;
import android.widget.*;
import com.alibaba.fastjson.JSON;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.sensu.android.zimaogou.IConstants;
import com.sensu.android.zimaogou.Mode.SelectProductModel;
import com.sensu.android.zimaogou.R;
import com.sensu.android.zimaogou.ReqResponse.GroupDetailsResponse;
import com.sensu.android.zimaogou.ReqResponse.GroupMemberResponse;
import com.sensu.android.zimaogou.activity.login.LoginActivity;
import com.sensu.android.zimaogou.external.greendao.helper.GDUserInfoHelper;
import com.sensu.android.zimaogou.external.greendao.model.UserInfo;
import com.sensu.android.zimaogou.utils.*;
import com.sensu.android.zimaogou.utils.TextUtils;
import com.sensu.android.zimaogou.widget.RoundImageView;
import com.umeng.socialize.ShareAction;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.media.UMImage;
import org.apache.http.Header;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhangwentao on 2015/11/20.
 */
public class SpellOrderDetailsActivity extends BaseActivity implements View.OnClickListener {

    private GroupDetailsResponse mGroupDetailsResponse;
    private String mTbId;
    private String mUid;
    private String mToken;
    private String mButtonStatue;
    private String mRightButtonStatue;

    private LinearLayout mUserHeadContainer;
    private UserInfo mUserInfo;
    private TimeCount mTimeCount;

    private TextView mOldPriceText;

    private TextView mHaveCodeView;
    private TextView mJoinGroupView;

    private TextView mNoCodeView;
    private TextView mWantGroupView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.spell_order_details_activity);

        initViews();
    }

    private void initViews() {
        mTbId = getIntent().getStringExtra(SpellOrderActivity.TB_ID);

        mOldPriceText = (TextView) findViewById(R.id.old_price);
        TextUtils.addLineCenter(mOldPriceText);

        mHaveCodeView = (TextView) findViewById(R.id.have_code);
        mJoinGroupView = (TextView) findViewById(R.id.join_group);
        mNoCodeView = (TextView) findViewById(R.id.no_code);
        mWantGroupView = (TextView) findViewById(R.id.want_group);

        findViewById(R.id.back).setOnClickListener(this);
        findViewById(R.id.buy_directly).setOnClickListener(this);
        findViewById(R.id.create_group).setOnClickListener(this);
        findViewById(R.id.command_input).setOnClickListener(this);

        mUserHeadContainer = (LinearLayout) findViewById(R.id.user_photo_container);
    }

    private void initDetailData() {
        ImageUtils.displayImage(mGroupDetailsResponse.data.media, ((ImageView) findViewById(R.id.group_pic)));
        ((TextView) findViewById(R.id.product_name)).setText(mGroupDetailsResponse.data.name);
        ((TextView) findViewById(R.id.product_describe)).setText(mGroupDetailsResponse.data.content);
        ((TextView) findViewById(R.id.group_min_size)).setText(mGroupDetailsResponse.data.min_num + "人成团");
        ((TextView) findViewById(R.id.group_buy_price)).setText("¥" + StringUtils.deleteZero(mGroupDetailsResponse.data.price));
        ((TextView) findViewById(R.id.price_market)).setText("自贸购特价¥" + StringUtils.deleteZero(mGroupDetailsResponse.data.price_goods));
        mOldPriceText.setText("¥" + StringUtils.deleteZero(mGroupDetailsResponse.data.price_goods));
        double saveMoney = Double.parseDouble(mGroupDetailsResponse.data.price_goods) - Double.parseDouble(mGroupDetailsResponse.data.price);
        ((TextView) findViewById(R.id.save_money)).setText("立省¥" + StringUtils.deleteZero(String.valueOf(StringUtils.getDoubleWithTwo(saveMoney))));
        ((WebView) findViewById(R.id.web_view)).loadUrl(mGroupDetailsResponse.data.description);

        if (mCommandGroupDialog != null) {
            ((TextView) mCommandGroupDialog.findViewById(R.id.group_code)).setText(mGroupDetailsResponse.data.code);
        }

        isJoinGroup();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        UMShareAPI.get(this).onActivityResult(requestCode, resultCode, data);
    }

    private void isJoinGroup() {
        if (mGroupDetailsResponse.data.state.equals("1")) {
            mTimeCount = new TimeCount(getTimeDifference(mGroupDetailsResponse.data.end_time), 1000);
            mTimeCount.start();
            //todo 正常
            mJoinGroupView.setVisibility(View.VISIBLE);
            mWantGroupView.setVisibility(View.VISIBLE);
            if (mGroupDetailsResponse.data.is_join.equals("0")) {
                ((TextView) findViewById(R.id.group_info)).setText("已有" + mGroupDetailsResponse.data.member_num + "人参团");
                mButtonStatue = "0";
                mRightButtonStatue = "1";
            } else if (mGroupDetailsResponse.data.is_join.equals("1")) {
                getMember(mGroupDetailsResponse.data.code);
                int minNum = Integer.parseInt(mGroupDetailsResponse.data.min_num);
                int memberNum = Integer.parseInt(mGroupDetailsResponse.data.member_num);
                if (memberNum >= minNum) {
                    mButtonStatue = "1";
                    mHaveCodeView.setText("组团成功");
                    mJoinGroupView.setText("立即购买");
                } else {
                    mButtonStatue = "2";
                    mHaveCodeView.setText("换个口令");
                    mJoinGroupView.setText("退出此团");
                }

                mRightButtonStatue = "2";
                mNoCodeView.setText("本团口令:" + mGroupDetailsResponse.data.code);
                mWantGroupView.setText("邀请更多人");
            }

        } else if (mGroupDetailsResponse.data.state.equals("2")) {
            ((TextView) findViewById(R.id.group_info)).setText("已有" + mGroupDetailsResponse.data.member_num + "人参团");
            //todo 已结束
            findViewById(R.id.show_time_count).setVisibility(View.GONE);
            mHaveCodeView.setText("逛逛其他团");
            mJoinGroupView.setVisibility(View.GONE);

            mNoCodeView.setText("已结束");
            mWantGroupView.setVisibility(View.GONE);

            mButtonStatue = "3";
            mRightButtonStatue = "3";
        } else if (mGroupDetailsResponse.data.state.equals("3")) {
            ((TextView) findViewById(R.id.group_info)).setText("已有" + mGroupDetailsResponse.data.member_num + "人参团");
            //todo 已抢光
            findViewById(R.id.show_time_count).setVisibility(View.GONE);
            mHaveCodeView.setText("逛逛其他团");
            mJoinGroupView.setVisibility(View.GONE);

            mNoCodeView.setText("已抢光");
            mWantGroupView.setVisibility(View.GONE);

            mButtonStatue = "3";
            mRightButtonStatue = "3";
        } else if (mGroupDetailsResponse.data.state.equals("4")) {
            // 已经购买
            findViewById(R.id.show_time_count).setVisibility(View.GONE);
            mHaveCodeView.setText("逛逛其他团");
            mJoinGroupView.setVisibility(View.GONE);

            mNoCodeView.setText("已购买");
            mWantGroupView.setVisibility(View.GONE);

            mButtonStatue = "3";
            mRightButtonStatue = "3";
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        mUserInfo = GDUserInfoHelper.getInstance(this).getUserInfo();
        if (mUserInfo == null) {
            mUid = "0";
            mToken = "";
        } else {
            mUid = mUserInfo.getUid();
            mToken = mUserInfo.getToken();
        }

        if (mTbId != null) {
            getGroupDetail(mTbId);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mTimeCount != null) {
            mTimeCount.cancel();
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.back:
                finish();
                break;
            case R.id.buy_directly:
                Intent intent = new Intent(this, ProductDetailsActivity.class);
                intent.putExtra(ProductDetailsActivity.PRODUCT_ID, mGroupDetailsResponse.data.goods_id);
                intent.putExtra(ProductDetailsActivity.FROM_SOURCE, "0");
                startActivity(intent);
                break;
            case R.id.create_group:
                if (mUserInfo == null) {
                    startActivity(new Intent(this, LoginActivity.class));
                    return;
                }

                if (mRightButtonStatue.equals("1")) {
                    createGroup();
                } else if (mRightButtonStatue.equals("2")) {
                    shareDialog();
                } else if (mRightButtonStatue.equals("3")) {

                }
                break;
            case R.id.command_input:
                if (mUserInfo == null) {
                    startActivity(new Intent(this, LoginActivity.class));
                    return;
                }

                if (mButtonStatue.equals("0")) {
                    commandInput();
                } else if (mButtonStatue.equals("1")) {
                    //todo 去付款
                    SelectProductModel selectProductModel = new SelectProductModel();
                    selectProductModel.setDeliverAddress(mGroupDetailsResponse.data.deliver_address);
                    selectProductModel.setTotalMoney(Double.parseDouble(mGroupDetailsResponse.data.price));
                    selectProductModel.setIsUseCoupon(false);

                    List<SelectProductModel.GoodsInfo> goodsInfoList = new ArrayList<SelectProductModel.GoodsInfo>();
                    SelectProductModel.GoodsInfo goodsInfo = new SelectProductModel.GoodsInfo();
                    goodsInfo.setGoodsId(mGroupDetailsResponse.data.goods_id);
                    goodsInfo.setSpecId(mGroupDetailsResponse.data.spec_id);
                    goodsInfo.setNum("1");
                    goodsInfo.setPrice(mGroupDetailsResponse.data.price);
                    goodsInfo.setSource("2");
                    goodsInfo.setName(mGroupDetailsResponse.data.name);
                    goodsInfo.setSpec(mGroupDetailsResponse.data.spec);
                    goodsInfo.setImage(mGroupDetailsResponse.data.goods_image);
                    goodsInfo.setRate(mGroupDetailsResponse.data.rate);
                    goodsInfoList.add(goodsInfo);

                    selectProductModel.setGoodsInfo(goodsInfoList);
                    Intent intent1 = new Intent(this, VerifyOrderActivity.class);
                    intent1.putExtra(VerifyOrderActivity.PRODUCT_FOR_PAY, selectProductModel);
                    startActivity(intent1);
                } else if (mButtonStatue.equals("2")) {
                    commandInput();
                } else if (mButtonStatue.equals("3")) {
                    Intent intent1 = new Intent(this, SpellOrderActivity.class);
                    intent1.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                    startActivity(intent1);
                }
                break;
            case R.id.submit:
                joinGroup();
                break;
            case R.id.share_friend_circle:
                new ShareAction(this).setPlatform(SHARE_MEDIA.WEIXIN_CIRCLE).setCallback(umShareListener)
                        .withTitle(mGroupDetailsResponse.data.name)
                        .withText(mGroupDetailsResponse.data.description)
                        .withTargetUrl("http://139.196.108.137:80/v1/share/tb/" + mTbId)
                        .withMedia(new UMImage(SpellOrderDetailsActivity.this, mGroupDetailsResponse.data.media))
                        .share();
                break;
            case R.id.share_friends:
                new ShareAction(this).setPlatform(SHARE_MEDIA.WEIXIN).setCallback(umShareListener)
                    .withTitle(mGroupDetailsResponse.data.name)
                    .withText(mGroupDetailsResponse.data.description)
                    .withTargetUrl("http://139.196.108.137:80/v1/share/tb/" + mTbId)
                    .withMedia(new UMImage(SpellOrderDetailsActivity.this, mGroupDetailsResponse.data.media))
                    .share();
                break;
        }
    }

    /**
     * 分享
     */
    Dialog mShareDialog;

    private void shareDialog() {
        mShareDialog = new Dialog(this, R.style.dialog);
        mShareDialog.setCancelable(true);
        mShareDialog.setContentView(R.layout.share_dialog);
        LinearLayout ll_wx = (LinearLayout) mShareDialog.findViewById(R.id.ll_wx);
        LinearLayout ll_friends = (LinearLayout) mShareDialog.findViewById(R.id.ll_friends);
        LinearLayout ll_sina = (LinearLayout) mShareDialog.findViewById(R.id.ll_sina);
        ll_sina.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new ShareAction(SpellOrderDetailsActivity.this).setPlatform(SHARE_MEDIA.SINA).setCallback(umShareListener)
                        .withTitle(mGroupDetailsResponse.data.name)
                        .withText(mGroupDetailsResponse.data.description)
                        .withTargetUrl("http://m.ftzgo365.com/v1/share/tb/" + mTbId)
                        .withMedia(new UMImage(SpellOrderDetailsActivity.this, mGroupDetailsResponse.data.media))
                        .share();
                mShareDialog.dismiss();
            }
        });
        ll_wx.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new ShareAction(SpellOrderDetailsActivity.this).setPlatform(SHARE_MEDIA.WEIXIN).setCallback(umShareListener)
                        .withTitle(mGroupDetailsResponse.data.name)
                        .withText(mGroupDetailsResponse.data.description)
                        .withTargetUrl("http://m.ftzgo365.com/v1/share/tb/" + mTbId)
                        .withMedia(new UMImage(SpellOrderDetailsActivity.this, mGroupDetailsResponse.data.media))
                        .share();
                mShareDialog.dismiss();
            }
        });
        ll_friends.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new ShareAction(SpellOrderDetailsActivity.this).setPlatform(SHARE_MEDIA.WEIXIN_CIRCLE).setCallback(umShareListener)
                        .withTitle(mGroupDetailsResponse.data.name)
                        .withText(mGroupDetailsResponse.data.description)
                        .withTargetUrl("http://m.ftzgo365.com/v1/share/tb/" + mTbId)
                        .withMedia(new UMImage(SpellOrderDetailsActivity.this, mGroupDetailsResponse.data.media))
                        .share();
                mShareDialog.dismiss();
            }
        });

        mShareDialog.show();
    }


    private UMShareListener umShareListener = new UMShareListener() {
        @Override
        public void onResult(SHARE_MEDIA platform) {
            Toast.makeText(SpellOrderDetailsActivity.this, platform + " 分享成功啦", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onError(SHARE_MEDIA platform, Throwable t) {
            Toast.makeText(SpellOrderDetailsActivity.this,platform + " 分享失败啦", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onCancel(SHARE_MEDIA platform) {
            Toast.makeText(SpellOrderDetailsActivity.this,platform + " 分享取消了", Toast.LENGTH_SHORT).show();
        }
    };

    private DisplayImageOptions mHeadDefaultOptions = new DisplayImageOptions.Builder()
            .considerExifParams(true)
            .imageScaleType(ImageScaleType.EXACTLY)
            .bitmapConfig(Bitmap.Config.RGB_565)
            .resetViewBeforeLoading(true)
            .showImageOnLoading(R.drawable.head_pic_default)
            .showImageOnFail(R.drawable.head_pic_default)
            .showImageForEmptyUri(R.drawable.head_pic_default)
            .cacheInMemory(true)
            .resetViewBeforeLoading(true)
            .cacheOnDisk(true)
            .build();

    private void addUserPhoto(List<GroupMemberResponse.MemberInfo> memberInfoList) {
        mUserHeadContainer.removeAllViews();
        for (int i = 0; i < memberInfoList.size(); i++) {
            RoundImageView roundImageView = new RoundImageView(this);
            roundImageView.setPadding(5, 5, 5, 5);
            int size = DisplayUtils.dp2px(50);
            roundImageView.setLayoutParams(new ViewGroup.LayoutParams(size, size));
            roundImageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            ImageUtils.displayImage(memberInfoList.get(i).avatar, roundImageView, mHeadDefaultOptions);
            mUserHeadContainer.addView(roundImageView);
        }
    }

    /**
     * 输入口令
     */
    Dialog mCommandInputDialog;

    public void commandInput() {
        mCommandInputDialog = new Dialog(this, R.style.dialog);
        mCommandInputDialog.setCancelable(true);
        mCommandInputDialog.setContentView(R.layout.command_dialog);
        mCommandInputDialog.show();
        mCommandInputDialog.findViewById(R.id.submit).setOnClickListener(this);
    }

    /**
     * 组团
     */
    Dialog mCommandGroupDialog;

    public void commandGroup() {
        mCommandGroupDialog = new Dialog(this, R.style.dialog);
        mCommandGroupDialog.setCancelable(true);
        mCommandGroupDialog.setContentView(R.layout.command_group_dialog);
        LinearLayout ll_top = (LinearLayout) mCommandGroupDialog.findViewById(R.id.ll_top);
        LinearLayout ll_bottom = (LinearLayout) mCommandGroupDialog.findViewById(R.id.ll_bottom);
        ll_top.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mCommandGroupDialog.dismiss();
            }
        });
        ll_bottom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mCommandGroupDialog.dismiss();
            }
        });
        WindowManager m = getWindowManager();

        Window dialogWindow = mCommandGroupDialog.getWindow();

        Display d = m.getDefaultDisplay(); // 获取屏幕宽、高用
        WindowManager.LayoutParams p = dialogWindow.getAttributes(); // 获取对话框当前的参数值
        p.height = d.getHeight(); // 高度设置为屏幕
        p.width = d.getWidth(); // 宽度设置为屏幕
        dialogWindow.setAttributes(p);
        mCommandGroupDialog.show();
        mCommandGroupDialog.findViewById(R.id.share_friend_circle).setOnClickListener(this);
        mCommandGroupDialog.findViewById(R.id.share_friends).setOnClickListener(this);
        if (!android.text.TextUtils.isEmpty(mGroupDetailsResponse.data.code)) {
            ((TextView) mCommandGroupDialog.findViewById(R.id.group_code)).setText(mGroupDetailsResponse.data.code);
        }
    }

    private void getGroupDetail(String id) {
        RequestParams requestParams = new RequestParams();
        requestParams.put("uid", mUid);
        requestParams.put("id", id);
        HttpUtil.get(IConstants.sTb_detail + id, requestParams, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                GroupDetailsResponse groupDetailsResponse = JSON.parseObject(response.toString(), GroupDetailsResponse.class);
                if (groupDetailsResponse.getRet().equals("-1")) {
                    PromptUtils.showToast(groupDetailsResponse.getMsg());
                }
                mGroupDetailsResponse = groupDetailsResponse;
                initDetailData();
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);
            }
        });
    }

    private void createGroup() {
        RequestParams requestParams = new RequestParams();
        requestParams.put("uid", mUserInfo.getUid());
        requestParams.put("tb_id", mTbId);
        HttpUtil.postWithSign(mUserInfo.getToken(), IConstants.sGroup_create, requestParams, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                if (response.optString("ret").equals("-1")) {
                    PromptUtils.showToast(response.optString("msg"));
                    return;
                }
                commandGroup();
                if (mCommandGroupDialog != null) {
                    ((TextView) mCommandGroupDialog.findViewById(R.id.group_code)).setText(response.optJSONObject("data").optString("code"));
                }
                onResume();
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);
            }
        });
    }

    private void getMember(String code) {

        RequestParams requestParams = new RequestParams();
        requestParams.put("uid", mUserInfo.getUid());
        requestParams.put("code", code);
        HttpUtil.getWithSign(mUserInfo.getToken(), IConstants.sTb_member, requestParams, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                GroupMemberResponse groupMemberResponse = JSON.parseObject(response.toString(), GroupMemberResponse.class);
                if (groupMemberResponse.getRet().equals("-1")) {
                    PromptUtils.showToast(groupMemberResponse.getMsg());
                    return;
                }
                addUserPhoto(groupMemberResponse.data.list);
                ((TextView) findViewById(R.id.group_info)).setText("已有" + groupMemberResponse.data.list.size() + "人参团 上限" + mGroupDetailsResponse.data.max_num + "人");
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);
            }
        });
    }

    //入团
    private void joinGroup() {
        String code = ((EditText) mCommandInputDialog.findViewById(R.id.code_edit)).getText().toString().trim();
        if (android.text.TextUtils.isEmpty(code)) {
            PromptUtils.showToast("请输入口令");
            return;
        }
        RequestParams requestParams = new RequestParams();
        requestParams.put("uid", mUserInfo.getUid());
        requestParams.put("code", code);
        HttpUtil.postWithSign(mUserInfo.getToken(), IConstants.sJoin_group, requestParams, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                String msg = response.optString("msg");
                String ret = response.optString("ret");
                if (ret.equals("-1")) {
                    PromptUtils.showToast(msg);
                    return;
                }

                String tbId = response.optJSONObject("data").optString("tb_id");
                getGroupDetail(tbId);

                if (mCommandInputDialog != null) {
                    mCommandInputDialog.dismiss();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);
            }
        });
    }

    //获取时间差
    private long getTimeDifference(String endTime) {
        long timeDifference = DateFormatUtils.getMillsByStringDateTime(endTime) - System.currentTimeMillis();
        return timeDifference;
    }

    private void showTimeCount(long time) {
        long day;
        long hour;
        long minute;
        long second;

        day = (int) (time / ConstantUtils.SECOND_PER_DAY);
        long day1 = time % ConstantUtils.SECOND_PER_DAY;
        if (day1 != 0) {
            hour = (day1 / ConstantUtils.SECOND_PER_HOUR);
            long hour1 = day1 % ConstantUtils.SECOND_PER_HOUR;
            if (hour1 != 0) {
                minute = (hour1 / ConstantUtils.SECONDS_PER_MINUTE);
                long minute1 = hour1 % ConstantUtils.SECONDS_PER_MINUTE;
                if (minute1 != 0) {
                    second = minute1;
                } else {
                    second = 0;
                }
            } else {
                minute = 0;
                second = 0;
            }
        } else {
            hour = 0;
            minute = 0;
            second = 0;
        }
        ((TextView) findViewById(R.id.show_time_count))
                .setText("距离结束还有" + day + "天" + hour + "小时" + minute + "分钟" + second + "秒");
    }

    class TimeCount extends CountDownTimer {

        public TimeCount(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }

        @Override
        public void onTick(long l) {
            showTimeCount(l / 1000);
        }

        @Override
        public void onFinish() {
            //TODO 倒计时结束
            if (mTimeCount != null) {
                mTimeCount.cancel();
            }
            findViewById(R.id.show_time_count).setVisibility(View.GONE);
            mHaveCodeView.setText("逛逛其他团");
            mJoinGroupView.setVisibility(View.GONE);

            mNoCodeView.setText("已结束");
            mWantGroupView.setVisibility(View.GONE);

            mButtonStatue = "3";
            mRightButtonStatue = "3";
        }
    }
}
