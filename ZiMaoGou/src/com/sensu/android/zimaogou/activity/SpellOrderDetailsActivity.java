package com.sensu.android.zimaogou.activity;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.*;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.sensu.android.zimaogou.IConstants;
import com.sensu.android.zimaogou.R;
import com.sensu.android.zimaogou.ReqResponse.GroupBuyListResponse;
import com.sensu.android.zimaogou.external.greendao.helper.GDUserInfoHelper;
import com.sensu.android.zimaogou.external.greendao.model.UserInfo;
import com.sensu.android.zimaogou.utils.DisplayUtils;
import com.sensu.android.zimaogou.utils.HttpUtil;
import com.sensu.android.zimaogou.utils.ImageUtils;
import com.sensu.android.zimaogou.utils.TextUtils;
import com.sensu.android.zimaogou.widget.RoundImageView;
import org.apache.http.Header;
import org.json.JSONObject;

/**
 * Created by zhangwentao on 2015/11/20.
 */
public class SpellOrderDetailsActivity extends BaseActivity implements View.OnClickListener {

    private LinearLayout mUserHeadContainer;
    private TextView mOldPriceText;
    private GroupBuyListResponse.GroupBuyListData mGroupBuyListData;

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

        mGroupBuyListData = (GroupBuyListResponse.GroupBuyListData) getIntent().getSerializableExtra(SpellOrderActivity.GROUP_BUY_DATA);

        mOldPriceText = (TextView) findViewById(R.id.old_price);

        mHaveCodeView = (TextView) findViewById(R.id.have_code);
        mJoinGroupView = (TextView) findViewById(R.id.join_group);
        mNoCodeView = (TextView) findViewById(R.id.no_code);
        mWantGroupView = (TextView) findViewById(R.id.want_group);

        if (mGroupBuyListData != null) {
            ((TextView) findViewById(R.id.spell_order_name)).setText(mGroupBuyListData.name);
            ImageUtils.displayImage(mGroupBuyListData.media, ((ImageView) findViewById(R.id.group_pic)));
            ((TextView) findViewById(R.id.product_name)).setText(mGroupBuyListData.name);
            ((TextView) findViewById(R.id.product_describe)).setText(mGroupBuyListData.content);
            ((TextView) findViewById(R.id.group_min_size)).setText(mGroupBuyListData.min_num + "人成团");
            ((TextView) findViewById(R.id.group_buy_price)).setText("¥" + mGroupBuyListData.price);
            mOldPriceText.setText("¥" + mGroupBuyListData.price_market);
            int saveMoney = Integer.parseInt(mGroupBuyListData.price_market) - Integer.parseInt(mGroupBuyListData.price);
            ((TextView) findViewById(R.id.save_money)).setText("立省¥" + String.valueOf(saveMoney));
            if (mGroupBuyListData.is_join.equals("0")) {
                ((TextView) findViewById(R.id.group_info)).setText("已有" + mGroupBuyListData.member_num + "人参团");
            } else if (mGroupBuyListData.is_join.equals("1")) {

                ((TextView) findViewById(R.id.group_info)).setText("已有" + mGroupBuyListData.member_num + "人参团 上限" + mGroupBuyListData.max_num + "人");

                mHaveCodeView.setText("换个口令");
                mJoinGroupView.setText("退出此团");

                mNoCodeView.setText("本团口令:" + mGroupBuyListData.code);
                mWantGroupView.setText("邀请更多人");
            }
        }

        findViewById(R.id.back).setOnClickListener(this);
        findViewById(R.id.buy_directly).setOnClickListener(this);
        findViewById(R.id.create_group).setOnClickListener(this);

        TextUtils.addLineCenter(mOldPriceText);
        mUserHeadContainer = (LinearLayout) findViewById(R.id.user_photo_container);
        addUserPhoto();
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
                createGroup();
                break;
        }
    }

    private void addUserPhoto() {
        for (int i = 0; i < 3; i++) {
            RoundImageView roundImageView = new RoundImageView(this);
            roundImageView.setPadding(5, 5, 5, 5);
            int size = DisplayUtils.dp2px(50);
            roundImageView.setLayoutParams(new ViewGroup.LayoutParams(size, size));
            roundImageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            roundImageView.setImageResource(R.drawable.head_photo_02);
            mUserHeadContainer.addView(roundImageView);
        }
    }
    /**
     *
     * 输入口令
     *
     */
    Dialog mCommandInputDialog;
    public void CommandInput(View v){
        mCommandInputDialog = new Dialog(this,R.style.dialog);
        mCommandInputDialog.setCancelable(true);
        mCommandInputDialog.setContentView(R.layout.command_dialog);
        mCommandInputDialog.show();
    }
    /**
     *
     * 组团
     *
     */
    Dialog mCommandGroupDialog;
    public void commandGroup(){
        mCommandGroupDialog = new Dialog(this,R.style.dialog);
        mCommandGroupDialog.setCancelable(true);
        mCommandGroupDialog.setContentView(R.layout.command_group_dialog);

        WindowManager m = getWindowManager();

        Window dialogWindow = mCommandGroupDialog.getWindow();

//        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
//        dialogWindow.setGravity(Gravity.TOP);
//        lp.y = DisplayUtils.dp2px(50);
//        dialogWindow.setAttributes(lp);

        Display d = m.getDefaultDisplay(); // 获取屏幕宽、高用
        WindowManager.LayoutParams p = dialogWindow.getAttributes(); // 获取对话框当前的参数值
        p.height = (int) d.getHeight() ; // 高度设置为屏幕
        p.width = (int) d.getWidth() ; // 宽度设置为屏幕
        dialogWindow.setAttributes(p);
        mCommandGroupDialog.show();
    }

    private void createGroup() {
        UserInfo userInfo = GDUserInfoHelper.getInstance(this).getUserInfo();
        if (userInfo == null) {
            return;
        }
        RequestParams requestParams = new RequestParams();
        requestParams.put("uid", userInfo.getUid());
        requestParams.put("tb_id", mGroupBuyListData.id);
        HttpUtil.postWithSign(userInfo.getToken(), IConstants.sGroup_create, requestParams, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                commandGroup();
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);
            }
        });
    }
}
