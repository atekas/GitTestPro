package com.sensu.android.zimaogou.activity.mycenter;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import com.alibaba.fastjson.JSON;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.sensu.android.zimaogou.IConstants;
import com.sensu.android.zimaogou.Mode.ProvinceMode;
import com.sensu.android.zimaogou.R;
import com.sensu.android.zimaogou.ReqResponse.AddressResponse;
import com.sensu.android.zimaogou.ReqResponse.ReceiverAddressResponse;
import com.sensu.android.zimaogou.activity.BaseActivity;
import com.sensu.android.zimaogou.adapter.ReceiverListAdapter;
import com.sensu.android.zimaogou.external.greendao.helper.GDUserInfoHelper;
import com.sensu.android.zimaogou.external.greendao.model.UserInfo;
import com.sensu.android.zimaogou.utils.HttpUtil;
import com.sensu.android.zimaogou.utils.LogUtils;
import org.apache.http.Header;
import org.json.JSONObject;

/**
 * Created by qi.yang on 2015/11/20.
 */
public class ReceiverAddressActivity extends BaseActivity {
    public static final String IS_NO_EDIT = "is_no_edit";

    ImageView mBackImageView;
    ListView mReceiverAddressListView;
    ProvinceMode mResultAddress;
    private boolean mIsNoEdit;
    ReceiverAddressResponse receiverAddressResponse = new ReceiverAddressResponse();
    UserInfo userInfo ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.receiver_address_activity);
        mResultAddress = new ProvinceMode();
        initView();
        userInfo = GDUserInfoHelper.getInstance(this).getUserInfo();
    }

    private void initView(){
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

    }

    @Override
    protected void onResume() {
        super.onResume();
        getReceiverAddress();
    }

    public void AddAddressClick(View v){
        startActivity(new Intent(this,ReceiverAddressEditActivity.class).putExtra("title","新增收货地址"));
    }

    /**
     * 获取收货地址
     */
    private void getReceiverAddress(){
        RequestParams requestParams = new RequestParams();
        requestParams.put("uid",userInfo.getUid());

        HttpUtil.getWithSign(userInfo.getToken(),IConstants.sGetReceiverAddress,requestParams,new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                LogUtils.d("获取收货地址：",response.toString());
                receiverAddressResponse = JSON.parseObject(response.toString(),ReceiverAddressResponse.class);
                mReceiverAddressListView.setAdapter(new ReceiverListAdapter(ReceiverAddressActivity.this, mIsNoEdit,receiverAddressResponse.data));
            }
        });
    }


}
