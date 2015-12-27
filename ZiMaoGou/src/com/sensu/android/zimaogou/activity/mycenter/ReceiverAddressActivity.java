package com.sensu.android.zimaogou.activity.mycenter;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import com.alibaba.fastjson.JSON;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.sensu.android.zimaogou.IConstants;
import com.sensu.android.zimaogou.Mode.ProvinceMode;
import com.sensu.android.zimaogou.R;
import com.sensu.android.zimaogou.ReqResponse.AddressResponse;
import com.sensu.android.zimaogou.activity.BaseActivity;
import com.sensu.android.zimaogou.adapter.ReceiverListAdapter;
import com.sensu.android.zimaogou.utils.HttpUtil;
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
    AddressResponse addressResponse = new AddressResponse();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.receiver_address_activity);
        mResultAddress = new ProvinceMode();
        initView();
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
        mReceiverAddressListView.setAdapter(new ReceiverListAdapter(this, mIsNoEdit));
    }

    public void AddAddressClick(View v){
        startActivity(new Intent(this,ReceiverAddressEditActivity.class).putExtra("title","新增收货地址"));
    }



}
