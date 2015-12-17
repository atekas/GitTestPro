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

    private GroupDetailsResponse mGroupDetailsResponse;
    private String mTbId;
    private String mUid;
    private String mToken;

    private LinearLayout mUserHeadContainer;
    private UserInfo mUserInfo;
    private TimeCount mTimeCount;
    private boolean mIsGroupFinish;

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
        mTimeCount = new TimeCount(getTimeDifference(mGroupDetailsResponse.data.end_time), 1000);
        mTimeCount.start();
        ((TextView) findViewById(R.id.spell_order_name)).setText(mGroupDetailsResponse.data.name);
        ImageUtils.displayImage(mGroupDetailsResponse.data.media, ((ImageView) findViewById(R.id.group_pic)));
        ((TextView) findViewById(R.id.product_name)).setText(mGroupDetailsResponse.data.name);
        ((TextView) findViewById(R.id.product_describe)).setText(mGroupDetailsResponse.data.content);
        ((TextView) findViewById(R.id.group_min_size)).setText(mGroupDetailsResponse.data.min_num + "人成团");
        ((TextView) findViewById(R.id.group_buy_price)).setText("¥" + mGroupDetailsResponse.data.price);
        ((TextView) findViewById(R.id.price_market)).setText("自贸购特价¥" + mGroupDetailsResponse.data.price_goods);
        mOldPriceText.setText("¥" + mGroupDetailsResponse.data.price_market);
        int saveMoney = Integer.parseInt(mGroupDetailsResponse.data.price_market) - Integer.parseInt(mGroupDetailsResponse.data.price);
        ((TextView) findViewById(R.id.save_money)).setText("立省¥" + String.valueOf(saveMoney));
        ((WebView) findViewById(R.id.web_view)).loadUrl(mGroupDetailsResponse.data.description);

        isJoinGroup();
    }

    private void isJoinGroup() {
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
                    startActivity(new Intent(this, LoginActivity.class));
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
        requestParams.put("uid", mUserInfo);
        HttpUtil.getWithSign(mToken, IConstants.sTb_detail + id, requestParams, new JsonHttpResponseHandler() {
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
                String msg = response.optString("msg");
                String ret = response.optString("ret");
                if (ret.equals("-1")) {
                    PromptUtils.showToast(msg);
                    return;
                }

                String tbId = response.optJSONObject("data").optString("tb_id");
                getGroupDetail(tbId);
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
            findViewById(R.id.show_time_count).setVisibility(View.GONE);
            mHaveCodeView.setText("去别的团逛逛");
            mJoinGroupView.setVisibility(View.GONE);

            mNoCodeView.setText("已结束");
            mWantGroupView.setVisibility(View.GONE);

            mIsGroupFinish = true;
        }
    }
}
