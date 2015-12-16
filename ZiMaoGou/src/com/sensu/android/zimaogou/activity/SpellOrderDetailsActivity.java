package com.sensu.android.zimaogou.activity;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.*;
import android.webkit.WebView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.alibaba.fastjson.JSON;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.sensu.android.zimaogou.IConstants;
import com.sensu.android.zimaogou.R;
import com.sensu.android.zimaogou.ReqResponse.GroupBuyListResponse;
import com.sensu.android.zimaogou.ReqResponse.GroupDetailsResponse;
import com.sensu.android.zimaogou.ReqResponse.GroupMemberResponse;
import com.sensu.android.zimaogou.activity.login.LoginActivity;
import com.sensu.android.zimaogou.external.greendao.helper.GDUserInfoHelper;
import com.sensu.android.zimaogou.external.greendao.model.UserInfo;
import com.sensu.android.zimaogou.utils.*;
import com.sensu.android.zimaogou.utils.TextUtils;
import com.sensu.android.zimaogou.widget.RoundImageView;
import org.apache.http.Header;
import org.json.JSONObject;

import java.util.List;

/**
 * Created by zhangwentao on 2015/11/20.
 */
public class SpellOrderDetailsActivity extends BaseActivity implements View.OnClickListener {
    public static final int LOGIN_REQUEST_CODE = 0;

    private GroupDetailsResponse mGroupDetailsResponse;

    private LinearLayout mUserHeadContainer;
    private String mTbId;
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
        GroupBuyListResponse.GroupBuyListData groupBuyListData = (GroupBuyListResponse.GroupBuyListData) getIntent().getSerializableExtra(SpellOrderActivity.GROUP_BUY_DATA);
        String id = getIntent().getStringExtra(SpellOrderActivity.TB_ID);

        if (id == null) {
            mTbId = groupBuyListData.id;
        } else {
            mTbId = id;
        }

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

        initData(groupBuyListData);
    }

    private void initData(GroupBuyListResponse.GroupBuyListData groupBuyListData) {
        if (groupBuyListData != null) {
            mTimeCount = new TimeCount(getTimeDifference(groupBuyListData.end_time), 1000);
            mTimeCount.start();
            ((TextView) findViewById(R.id.spell_order_name)).setText(groupBuyListData.name);
            ImageUtils.displayImage(groupBuyListData.media, ((ImageView) findViewById(R.id.group_pic)));
            ((TextView) findViewById(R.id.product_name)).setText(groupBuyListData.name);
            ((TextView) findViewById(R.id.product_describe)).setText(groupBuyListData.content);
            ((TextView) findViewById(R.id.group_min_size)).setText(groupBuyListData.min_num + "人成团");
            ((TextView) findViewById(R.id.group_buy_price)).setText("¥" + groupBuyListData.price);
            ((TextView) findViewById(R.id.price_market)).setText("自贸购特价¥" + groupBuyListData.price_goods);
            mOldPriceText.setText("¥" + groupBuyListData.price_market);
            int saveMoney = Integer.parseInt(groupBuyListData.price_market) - Integer.parseInt(groupBuyListData.price);
            ((TextView) findViewById(R.id.save_money)).setText("立省¥" + String.valueOf(saveMoney));
            ((WebView) findViewById(R.id.web_view)).loadUrl(groupBuyListData.description);
            if (groupBuyListData.is_join.equals("0")) {
                ((TextView) findViewById(R.id.group_info)).setText("已有" + groupBuyListData.member_num + "人参团");
            } else if (groupBuyListData.is_join.equals("1")) {
                getMember(groupBuyListData.code);
                ((TextView) findViewById(R.id.group_info)).setText("已有" + groupBuyListData.member_num + "人参团 上限" + groupBuyListData.max_num + "人");

                mHaveCodeView.setText("换个口令");
                mJoinGroupView.setText("退出此团");

                mNoCodeView.setText("本团口令:" + groupBuyListData.code);
                mWantGroupView.setText("邀请更多人");
            }
        }
    }

    private void initDetailData() {
        if (mGroupDetailsResponse.data.is_join.equals("0")) {
            ((TextView) findViewById(R.id.group_info)).setText("已有" + mGroupDetailsResponse.data.member_num + "人参团");
        } else if (mGroupDetailsResponse.data.is_join.equals("1")) {
            getMember(mGroupDetailsResponse.data.code);
            ((TextView) findViewById(R.id.group_info)).setText("已有" + mGroupDetailsResponse.data.member_num + "人参团 上限" + mGroupDetailsResponse.data.max_num + "人");

            mHaveCodeView.setText("换个口令");
            mJoinGroupView.setText("退出此团");

            mNoCodeView.setText("本团口令:" + mGroupDetailsResponse.data.code);
            mWantGroupView.setText("邀请更多人");
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        mUserInfo = GDUserInfoHelper.getInstance(this).getUserInfo();
        if (mUserInfo != null) {
            if (mTbId != null) {
                getGroupDetail(mTbId);
            }
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
                startActivity(new Intent(this, ProductDetailsActivity.class));
                break;
            case R.id.create_group:
                if (mUserInfo == null) {
                    startActivity(new Intent(this, LoginActivity.class));
                    return;
                }
                createGroup();
                break;
            case R.id.command_input:
                if (mUserInfo == null) {
                    startActivityForResult(new Intent(this, LoginActivity.class), LOGIN_REQUEST_CODE);
                    return;
                }
                commandInput();
                break;
            case R.id.submit:
                joinGroup();
                break;
        }
    }

    private void addUserPhoto(List<GroupMemberResponse.MemberInfo> memberInfoList) {
        for (int i = 0; i < memberInfoList.size(); i++) {
            RoundImageView roundImageView = new RoundImageView(this);
            roundImageView.setPadding(5, 5, 5, 5);
            int size = DisplayUtils.dp2px(50);
            roundImageView.setLayoutParams(new ViewGroup.LayoutParams(size, size));
            roundImageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            ImageUtils.displayImage(memberInfoList.get(i).avatar, roundImageView);
            mUserHeadContainer.addView(roundImageView);
        }
    }
    /**
     * 输入口令
     */
    Dialog mCommandInputDialog;
    public void commandInput(){
        mCommandInputDialog = new Dialog(this,R.style.dialog);
        mCommandInputDialog.setCancelable(true);
        mCommandInputDialog.setContentView(R.layout.command_dialog);
        mCommandInputDialog.show();
        mCommandInputDialog.findViewById(R.id.submit).setOnClickListener(this);
    }
    /**
     * 组团
     */
    Dialog mCommandGroupDialog;
    public void commandGroup(){
        mCommandGroupDialog = new Dialog(this,R.style.dialog);
        mCommandGroupDialog.setCancelable(true);
        mCommandGroupDialog.setContentView(R.layout.command_group_dialog);

        WindowManager m = getWindowManager();

        Window dialogWindow = mCommandGroupDialog.getWindow();

        Display d = m.getDefaultDisplay(); // 获取屏幕宽、高用
        WindowManager.LayoutParams p = dialogWindow.getAttributes(); // 获取对话框当前的参数值
        p.height = d.getHeight() ; // 高度设置为屏幕
        p.width = d.getWidth() ; // 宽度设置为屏幕
        dialogWindow.setAttributes(p);
        mCommandGroupDialog.show();
    }

    private void getGroupDetail(String id) {
        RequestParams requestParams = new RequestParams();
        requestParams.put("uid", mUserInfo.getUid());
        HttpUtil.getWithSign(mUserInfo.getToken(), IConstants.sTb_detail + id, requestParams, new JsonHttpResponseHandler() {
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
                commandGroup();
                onResume();
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);
            }
        });
    }

    private void getMember(String code) {
        if (mUserInfo == null) {
            PromptUtils.showToast("请先登录");
            //TODO
            return;
        }

        RequestParams requestParams = new RequestParams();
        requestParams.put("uid", mUserInfo.getUid());
        requestParams.put("code", code);
        HttpUtil.getWithSign(mUserInfo.getToken(), IConstants.sTb_member, requestParams, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                GroupMemberResponse groupMemberResponse = JSON.parseObject(response.toString(), GroupMemberResponse.class);
                addUserPhoto(groupMemberResponse.data.list);
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
                //todo 进入详情页
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
            if (hour1 !=0) {
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
        }
    }
}
