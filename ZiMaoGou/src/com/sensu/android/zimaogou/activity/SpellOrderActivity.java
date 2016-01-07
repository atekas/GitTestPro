package com.sensu.android.zimaogou.activity;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ListView;
import com.alibaba.fastjson.JSON;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.sensu.android.zimaogou.IConstants;
import com.sensu.android.zimaogou.R;
import com.sensu.android.zimaogou.ReqResponse.GroupBuyListResponse;
import com.sensu.android.zimaogou.activity.login.LoginActivity;
import com.sensu.android.zimaogou.adapter.SpellOrderAdapter;
import com.sensu.android.zimaogou.external.greendao.helper.GDUserInfoHelper;
import com.sensu.android.zimaogou.external.greendao.model.UserInfo;
import com.sensu.android.zimaogou.utils.HttpUtil;
import com.sensu.android.zimaogou.utils.PromptUtils;
import org.apache.http.Header;
import org.json.JSONObject;

/**
 * Created by zhangwentao on 2015/11/19.
 */
public class SpellOrderActivity extends BaseActivity implements View.OnClickListener, AdapterView.OnItemClickListener {
    public static final String TB_ID = "tb_id";

    public String mIsMyTbList = "0";

    private ListView mListView;
    private SpellOrderAdapter mSpellOrderAdapter;
    private GroupBuyListResponse mGroupBuyListResponse;
    private UserInfo mUserInfo;
    FrameLayout content_view;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.spell_order_activity);

        initViews();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    private void initViews() {
        mUserInfo = GDUserInfoHelper.getInstance(this).getUserInfo();
        findViewById(R.id.back).setOnClickListener(this);
        findViewById(R.id.group_buy_selection).setOnClickListener(this);
        findViewById(R.id.my_group_buy).setOnClickListener(this);
        findViewById(R.id.group_buy_selection_text).setSelected(true);
        content_view = (FrameLayout) findViewById(R.id.content_view);
        mListView = (ListView) findViewById(R.id.group_buy_list);
        mSpellOrderAdapter = new SpellOrderAdapter(this);
        mListView.setAdapter(mSpellOrderAdapter);
        mListView.setOnItemClickListener(this);
        findViewById(R.id.input_word).setOnClickListener(this);

        getTbList();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.back:
                finish();
                break;
            case R.id.group_buy_selection:
                findViewById(R.id.group_buy_selection_text).setSelected(true);
                findViewById(R.id.my_group_buy_text).setSelected(false);
                findViewById(R.id.bottom_layout).setVisibility(View.VISIBLE);

                if (mIsMyTbList.equals("1")) {
                    getTbList();
                }
                mIsMyTbList = "0";
                break;
            case R.id.my_group_buy:
                findViewById(R.id.group_buy_selection_text).setSelected(false);
                findViewById(R.id.my_group_buy_text).setSelected(true);
                findViewById(R.id.bottom_layout).setVisibility(View.GONE);

                if (mIsMyTbList.equals("0")) {
                    getMyTbList();
                }
                mIsMyTbList = "1";

                break;
            case R.id.input_word:
                if (mUserInfo == null) {
                    startActivity(new Intent(this, LoginActivity.class));
                    return;
                }
                commandInput(findViewById(R.id.input_word));
                break;
            case R.id.submit:
                joinGroup();
                break;
        }
    }

    /**
     * 输入口令
     */
    Dialog mCommandInputDialog;

    public void commandInput(View v) {
        mCommandInputDialog = new Dialog(this, R.style.dialog);
        mCommandInputDialog.setCancelable(true);
        mCommandInputDialog.setContentView(R.layout.command_dialog);
        mCommandInputDialog.show();
        mCommandInputDialog.findViewById(R.id.submit).setOnClickListener(this);
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        GroupBuyListResponse.GroupBuyListData groupBuyListData = mGroupBuyListResponse.data.get(i);
        Intent intent = new Intent(this, SpellOrderDetailsActivity.class);
        intent.putExtra(TB_ID, groupBuyListData.id);
        startActivity(intent);
    }

    //获取团购列表
    private void getTbList() {
        String uid;
        if (mUserInfo == null) {
            uid = "0";
        } else {
            uid = mUserInfo.getUid();
        }
        RequestParams requestParams = new RequestParams();
        requestParams.put("uid", uid);
        HttpUtil.get(IConstants.sTb_list, requestParams, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                GroupBuyListResponse groupBuyListResponse = JSON.parseObject(response.toString(), GroupBuyListResponse.class);
                if (groupBuyListResponse.getRet().equals("-1")) {
                    PromptUtils.showToast(groupBuyListResponse.getMsg());
                    return;
                }
                mGroupBuyListResponse = groupBuyListResponse;
                mSpellOrderAdapter.setGroupBuyList(groupBuyListResponse);
                if(groupBuyListResponse.data.size() == 0){
                    exceptionLinearLayout.setException(IConstants.EXCEPTION_MY_GROUP_IS_NULL);
                    content_view.addView(ExceptionView);
                }else{
                    content_view.removeView(ExceptionView);
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);
            }
        });
    }

    private void getMyTbList() {
        RequestParams requestParams = new RequestParams();
        requestParams.put("uid", mUserInfo.getUid());

        HttpUtil.getWithSign(mUserInfo.getToken(), IConstants.sMyTb_list, requestParams, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);

                GroupBuyListResponse groupBuyListResponse = JSON.parseObject(response.toString(), GroupBuyListResponse.class);
                if (groupBuyListResponse.getRet().equals("-1")) {
                    PromptUtils.showToast(groupBuyListResponse.getMsg());
                    return;
                }
                mGroupBuyListResponse = groupBuyListResponse;
                mSpellOrderAdapter.setGroupBuyList(groupBuyListResponse);
                if(groupBuyListResponse.data.size() == 0){
                    exceptionLinearLayout.setException(IConstants.EXCEPTION_MY_GROUP_IS_NULL);
                    content_view.addView(ExceptionView);
                }else{
                    content_view.removeView(ExceptionView);
                }
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
        if (TextUtils.isEmpty(code)) {
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

                Intent intent = new Intent(SpellOrderActivity.this, SpellOrderDetailsActivity.class);
                intent.putExtra(TB_ID, tbId);
                startActivity(intent);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);
            }
        });
    }
}
