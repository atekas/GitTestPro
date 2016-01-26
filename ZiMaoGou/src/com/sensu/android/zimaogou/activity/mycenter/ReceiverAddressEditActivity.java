package com.sensu.android.zimaogou.activity.mycenter;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import com.alibaba.fastjson.JSON;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.sensu.android.zimaogou.BaseApplication;
import com.sensu.android.zimaogou.IConstants;
import com.sensu.android.zimaogou.Mode.ProvinceMode;
import com.sensu.android.zimaogou.Mode.ReceiverAddressMode;
import com.sensu.android.zimaogou.R;
import com.sensu.android.zimaogou.ReqResponse.AddressResponse;
import com.sensu.android.zimaogou.activity.BaseActivity;
import com.sensu.android.zimaogou.external.greendao.helper.GDUserInfoHelper;
import com.sensu.android.zimaogou.external.greendao.model.UserInfo;
import com.sensu.android.zimaogou.utils.HttpUtil;
import com.sensu.android.zimaogou.utils.LogUtils;
import com.sensu.android.zimaogou.utils.PromptUtils;
import com.sensu.android.zimaogou.utils.TextUtils;
import org.apache.http.Header;
import org.json.JSONObject;

/**
 * 新增和编辑收货地址
 * Created by qi.yang on 2015/11/20.
 */
public class ReceiverAddressEditActivity extends BaseActivity {
    ImageView mBackImageView,mSwitchImageView;
    TextView mTitleTextView,mProvinceTextView,mSubmitTextView;
    ProvinceMode mResultAddress = BaseApplication.mChooseAddress;
    AddressResponse addressResponse =null;

    EditText mReceiverNameEditText,mReceiverPhoneEditText,mReceiverIDEditText,mReceiverStreetEditText;
    boolean isDefault = false;
    UserInfo userInfo ;
    ReceiverAddressMode reveiverAddressMode;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.receiver_address_edit_activity);
        initView();
        if(getIntent().getExtras() != null){
            String title = getIntent().getExtras().getString("title");
            mTitleTextView.setText(title);
            if(title.indexOf("编辑")>=0){

                reveiverAddressMode = (ReceiverAddressMode) getIntent().getExtras().get("data");
            }
        }
        addressResponse =  BaseApplication.getAddressResponse();
        setData();
    }

    private void initView(){
        mBackImageView = (ImageView) findViewById(R.id.back);
        mTitleTextView = (TextView) findViewById(R.id.tv_title);
        mReceiverNameEditText = (EditText) findViewById(R.id.et_receiverName);
        mReceiverPhoneEditText = (EditText) findViewById(R.id.et_receiverPhone);
        mReceiverIDEditText = (EditText) findViewById(R.id.et_receiverID);
        mReceiverStreetEditText = (EditText) findViewById(R.id.et_receiverStreet);
        mSwitchImageView = (ImageView) findViewById(R.id.img_switch);
        mSubmitTextView = (TextView) findViewById(R.id.tv_function);
        mSubmitTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                submitAddAddress();
            }
        });
        mSwitchImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                    isDefault = !isDefault;
                    mSwitchImageView.setSelected(isDefault);
            }
        });

        mProvinceTextView = (TextView) findViewById(R.id.tv_province);
        mBackImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }


    @Override
    protected void onResume() {
        super.onResume();
        userInfo = GDUserInfoHelper.getInstance(this).getUserInfo();
        if(mResultAddress.data!= null&& mResultAddress.data.get(0) != null && mResultAddress.data.get(0).data.get(0) != null){
            mProvinceTextView.setText(mResultAddress.getName()+" "+mResultAddress.data.get(0).getName()+" "+mResultAddress.data.get(0).data.get(0).getName());
        }else{
            if(reveiverAddressMode == null) {//如是编辑收货地址 省市不需置空
                mProvinceTextView.setText("");
            }
        }



    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        mResultAddress.data = null;
    }

    /**
     * 选择省市区
     * @param v
     */
    public void ChooseAddressClick(View v){
        Intent intent = new Intent(this,ChooseProvinceActivity.class);
        intent.putExtra("address",addressResponse);
        startActivity(intent);
    }

    private void setData(){
        if(reveiverAddressMode != null){
            mReceiverNameEditText.setText(reveiverAddressMode.getName());
            mReceiverPhoneEditText.setText(reveiverAddressMode.getMobile());
            mReceiverIDEditText.setText(reveiverAddressMode.getId_card());
            mProvinceTextView.setText(reveiverAddressMode.getProvince()+" "+reveiverAddressMode.getCity()+" "+reveiverAddressMode.getDistrict());
            mReceiverStreetEditText.setText(reveiverAddressMode.getAddress());
            if(reveiverAddressMode.getIs_default().equals("1")){
                isDefault = true;

            }else{
                isDefault = false;
            }
            mSwitchImageView.setSelected(isDefault);
        }
    }

    /**
     * 提交
     */
    private void submitAddAddress(){
        if(TextUtils.isEmpty(mReceiverNameEditText.getText().toString())){
            PromptUtils.showToast("请填写收货人姓名");
            return;
        }
        if(TextUtils.isEmpty(mReceiverPhoneEditText.getText().toString())){
            PromptUtils.showToast("请填写收货人联系电话");
            return;
        }

        if(TextUtils.isEmpty(mReceiverIDEditText.getText().toString())){
            PromptUtils.showToast("请填写收货人身份证号码");
            return;
        }
        if(TextUtils.isEmpty(mProvinceTextView.getText().toString())){
            PromptUtils.showToast("请选择省、市、区");
            return;
        }
        if(TextUtils.isEmpty(mReceiverStreetEditText.getText().toString())){
            PromptUtils.showToast("请填写详细收货地址");
            return;
        }
        String url=IConstants.sAddAddress;
        RequestParams requestParams = new RequestParams();
        requestParams.put("uid",userInfo.getUid());
        requestParams.put("name",mReceiverNameEditText.getText().toString());
        requestParams.put("mobile",mReceiverPhoneEditText.getText().toString());
        requestParams.put("id_card",mReceiverIDEditText.getText().toString());

        requestParams.put("address",mReceiverStreetEditText.getText().toString());
        requestParams.put("is_default",isDefault?"1":"0");
        if(reveiverAddressMode != null){
            requestParams.put("id",reveiverAddressMode.getId());
            url += "/"+reveiverAddressMode.getId();
            requestParams.put("province", reveiverAddressMode.getProvince_id());
            requestParams.put("city",reveiverAddressMode.getCity_id());
            requestParams.put("district",reveiverAddressMode.getDistrict_id());
        }
        if(mResultAddress.data !=null){
            requestParams.put("province",mResultAddress.getId());
            requestParams.put("city",mResultAddress.data.get(0).getId());
            requestParams.put("district",mResultAddress.data.get(0).data.get(0).getId());
        }


        HttpUtil.postWithSign(userInfo.getToken(),url,requestParams,new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                if(reveiverAddressMode == null) {
                    LogUtils.d("添加收货地址返回：", response.toString());
                }else{
                    LogUtils.d("编辑收货地址返回：", response.toString());
                }
                onBackPressed();
            }
        });



    }

}
