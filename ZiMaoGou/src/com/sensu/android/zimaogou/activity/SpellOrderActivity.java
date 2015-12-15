package com.sensu.android.zimaogou.activity;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import com.alibaba.fastjson.JSON;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.sensu.android.zimaogou.IConstants;
import com.sensu.android.zimaogou.R;
import com.sensu.android.zimaogou.ReqResponse.GroupBuyListResponse;
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
    public static final String GROUP_BUY_DATA = "group_buy_data";

    private ListView mListView;
    private SpellOrderAdapter mSpellOrderAdapter;
    private GroupBuyListResponse mGroupBuyListResponse;
    private UserInfo mUserInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.spell_order_activity);
    }

    @Override
    protected void onResume() {
        super.onResume();
        initViews();
    }

    private void initViews() {
        mUserInfo = GDUserInfoHelper.getInstance(this).getUserInfo();
        findViewById(R.id.back).setOnClickListener(this);
        findViewById(R.id.group_buy_selection).setOnClickListener(this);
        findViewById(R.id.my_group_buy).setOnClickListener(this);
        findViewById(R.id.group_buy_selection_text).setSelected(true);

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
                break;
            case R.id.my_group_buy:
                findViewById(R.id.group_buy_selection_text).setSelected(false);
                findViewById(R.id.my_group_buy_text).setSelected(true);
                findViewById(R.id.bottom_layout).setVisibility(View.GONE);
                break;
            case R.id.input_word:
                commandInput(findViewById(R.id.input_word));
                break;
            case R.id.submit:
                PromptUtils.showToast("组团");
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
        GroupBuyListResponse.GroupBuyListData groupBuyListData = mGroupBuyListResponse.data.get(i - 1);
        Intent intent = new Intent(this, SpellOrderDetailsActivity.class);
        intent.putExtra(GROUP_BUY_DATA, groupBuyListData);
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
                mGroupBuyListResponse = groupBuyListResponse;
                mSpellOrderAdapter.setGroupBuyList(groupBuyListResponse);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);
            }
        });
    }

    //入团
    private void joinGroup() {
        UserInfo userInfo = GDUserInfoHelper.getInstance(this).getUserInfo();
        if (userInfo == null) {
            PromptUtils.showToast("请先登录");
            return;
        }
        String code = ((EditText) mCommandInputDialog.findViewById(R.id.code_edit)).getText().toString().trim();
        RequestParams requestParams = new RequestParams();
        requestParams.put("uid", userInfo.getUid());
        requestParams.put("code", code);
        HttpUtil.postWithSign(userInfo.getToken(), IConstants.sJoin_group, requestParams, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);
            }
        });
    }
}
