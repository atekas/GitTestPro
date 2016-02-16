package com.sensu.android.zimaogou.activity.mycenter;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import com.alibaba.fastjson.JSON;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.sensu.android.zimaogou.IConstants;
import com.sensu.android.zimaogou.Mode.ProvinceMode;
import com.sensu.android.zimaogou.Mode.ReceiverAddressMode;
import com.sensu.android.zimaogou.R;
import com.sensu.android.zimaogou.ReqResponse.ReceiverAddressResponse;
import com.sensu.android.zimaogou.activity.BaseActivity;
import com.sensu.android.zimaogou.adapter.ReceiverListAdapter;
import com.sensu.android.zimaogou.external.greendao.helper.GDAddressDefaultHelper;
import com.sensu.android.zimaogou.external.greendao.helper.GDUserInfoHelper;
import com.sensu.android.zimaogou.external.greendao.model.AddressDefault;
import com.sensu.android.zimaogou.external.greendao.model.UserInfo;
import com.sensu.android.zimaogou.utils.HttpUtil;
import com.sensu.android.zimaogou.utils.LogUtils;
import com.sensu.android.zimaogou.utils.PromptUtils;
import org.apache.http.Header;
import org.json.JSONObject;

/**
 * Created by qi.yang on 2015/11/20.
 */
public class ReceiverAddressActivity extends BaseActivity implements AdapterView.OnItemClickListener {
    public static final String IS_NO_EDIT = "is_no_edit";

    ImageView mBackImageView;
    ListView mReceiverAddressListView;
    ProvinceMode mResultAddress;
    private boolean mIsNoEdit;
    ReceiverAddressResponse receiverAddressResponse = new ReceiverAddressResponse();
    UserInfo userInfo;
    RelativeLayout rl_content;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.receiver_address_activity);
        mResultAddress = new ProvinceMode();
        initView();
        userInfo = GDUserInfoHelper.getInstance(this).getUserInfo();
    }

    private void initView() {
        mIsNoEdit = getIntent().getBooleanExtra(IS_NO_EDIT, false);

        mBackImageView = (ImageView) findViewById(R.id.back);
        mReceiverAddressListView = (ListView) findViewById(R.id.lv_receiverAddress);
        mBackImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        mReceiverAddressListView.setDivider(null);
        mReceiverAddressListView.setOnItemClickListener(this);
        rl_content = (RelativeLayout) findViewById(R.id.rl_content);
    }

    @Override
    protected void onResume() {
        super.onResume();
        getReceiverAddress();
    }

    public void AddAddressClick(View v) {
        startActivity(new Intent(this, ReceiverAddressEditActivity.class).putExtra("title", "新增收货地址"));
    }

    /**
     * 获取收货地址
     */
    private void getReceiverAddress() {
        RequestParams requestParams = new RequestParams();
        requestParams.put("uid", userInfo.getUid());

        HttpUtil.getWithSign(userInfo.getToken(), IConstants.sGetReceiverAddress, requestParams, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                LogUtils.d("获取收货地址：", response.toString());
                if (response.optString("ret").equals("-1")) {
                    PromptUtils.showToast(response.optString("msg"));
                    return;
                }
                receiverAddressResponse = JSON.parseObject(response.toString(), ReceiverAddressResponse.class);
                saveAddress();
                mReceiverAddressListView.setAdapter(new ReceiverListAdapter(ReceiverAddressActivity.this, mIsNoEdit, receiverAddressResponse.data));
                if(receiverAddressResponse.data.size() == 0){
                    exceptionLinearLayout.setException(IConstants.EXCEPTION_ADDRESS_IS_NULL);
                    rl_content.addView(ExceptionView);
                }else{
                    rl_content.removeView(ExceptionView);
                }
            }
        });
    }

    private void saveAddress() {
        boolean isHaveDefault = false;
        if (receiverAddressResponse.data.size() != 0) {
            for (ReceiverAddressMode receiverAddressMode : receiverAddressResponse.data) {
                if (receiverAddressMode.getIs_default().equals("1")) {
                    isHaveDefault = true;
                    AddressDefault addressDefault = new AddressDefault();
                    addressDefault.setId(receiverAddressMode.getId());
                    addressDefault.setName(receiverAddressMode.getName());
                    addressDefault.setAddress(receiverAddressMode.getAddress());
                    addressDefault.setCity(receiverAddressMode.getCity());
                    addressDefault.setCity_id(receiverAddressMode.getCity_id());
                    addressDefault.setDistrict(receiverAddressMode.getDistrict());
                    addressDefault.setDistrict_id(receiverAddressMode.getDistrict_id());
                    addressDefault.setId_card(receiverAddressMode.getId_card());
                    addressDefault.setMobile(receiverAddressMode.getMobile());
                    addressDefault.setProvince(receiverAddressMode.getProvince());
                    addressDefault.setProvince_id(receiverAddressMode.getProvince_id());
                    GDAddressDefaultHelper.getInstance(this).insertAddress(addressDefault);
                    break;
                }
            }

            if (!isHaveDefault) {
                ReceiverAddressMode receiverAddressMode = receiverAddressResponse.data.get(0);
                AddressDefault addressDefault = new AddressDefault();
                addressDefault.setId(receiverAddressMode.getId());
                addressDefault.setName(receiverAddressMode.getName());
                addressDefault.setAddress(receiverAddressMode.getAddress());
                addressDefault.setCity(receiverAddressMode.getCity());
                addressDefault.setCity_id(receiverAddressMode.getCity_id());
                addressDefault.setDistrict(receiverAddressMode.getDistrict());
                addressDefault.setDistrict_id(receiverAddressMode.getDistrict_id());
                addressDefault.setId_card(receiverAddressMode.getId_card());
                addressDefault.setMobile(receiverAddressMode.getMobile());
                addressDefault.setProvince(receiverAddressMode.getProvince());
                addressDefault.setProvince_id(receiverAddressMode.getProvince_id());
                GDAddressDefaultHelper.getInstance(this).insertAddress(addressDefault);
            }
        }
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        if (mIsNoEdit) {
            ReceiverAddressMode receiverAddressMode = receiverAddressResponse.data.get(i);
            Intent intent = new Intent();
            intent.putExtra("address", receiverAddressMode);
            setResult(RESULT_OK, intent);
            finish();
        }
    }
}
