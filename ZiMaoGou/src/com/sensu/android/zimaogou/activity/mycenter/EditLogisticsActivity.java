package com.sensu.android.zimaogou.activity.mycenter;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import com.alibaba.fastjson.JSON;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.sensu.android.zimaogou.IConstants;
import com.sensu.android.zimaogou.Mode.MyOrderMode;
import com.sensu.android.zimaogou.Mode.RefundReasonMode;
import com.sensu.android.zimaogou.R;
import com.sensu.android.zimaogou.ReqResponse.LogisticsCompanyResponse;
import com.sensu.android.zimaogou.activity.BaseActivity;
import com.sensu.android.zimaogou.external.greendao.helper.GDUserInfoHelper;
import com.sensu.android.zimaogou.external.greendao.model.UserInfo;
import com.sensu.android.zimaogou.utils.HttpUtil;
import com.sensu.android.zimaogou.utils.LogUtils;
import com.sensu.android.zimaogou.utils.PromptUtils;
import com.sensu.android.zimaogou.utils.TextUtils;
import org.apache.http.Header;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * 退款订单中 填写物流
 * Created by qi.yang on 2016/1/15.
 */
public class EditLogisticsActivity extends BaseActivity {
    TextView mReceiverAddress,mCompany,mSubmit;
    EditText mLogisticsNo;
    MyOrderMode myOrderMode = new MyOrderMode();
    UserInfo userInfo;
    ArrayList<RefundReasonMode> mCompanies =new ArrayList<RefundReasonMode>();
    RefundReasonMode mChooseCompany = new RefundReasonMode();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.refund_edit_logistics_activity);
        if(getIntent().getExtras() != null){
            myOrderMode = (MyOrderMode) getIntent().getExtras().get("data");
        }
        initView();
        setReason();
    }
    private void initView(){
        userInfo = GDUserInfoHelper.getInstance(this).getUserInfo();
        mReceiverAddress = (TextView) findViewById(R.id.tv_receiverAddress);
        mCompany = (TextView) findViewById(R.id.tv_company);
        mLogisticsNo = (EditText) findViewById(R.id.et_logisticsNo);
        mSubmit = (TextView) findViewById(R.id.submit);

        mReceiverAddress.setText(myOrderMode.getReceiver_address());
        findViewById(R.id.back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        mSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                submitLogistics();
            }
        });

    }

    public void CompanyClick(View v){
        startActivityForResult(new Intent(this, OnlyListActivity.class).putExtra("list",mCompanies )
                .putExtra("data", mChooseCompany)
                .putExtra("type","company"), 1001);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == 1001){
            mChooseCompany = (RefundReasonMode) data.getExtras().get("data");
            mCompany.setText(mChooseCompany.getName());
        }
    }

    private void submitLogistics(){
        String companyName = mCompany.getText().toString().trim();
        String logisticsNo = mLogisticsNo.getText().toString().trim();
        if(TextUtils.isEmpty(companyName)){
            PromptUtils.showToast("请选择物流公司");
            return;
        }
        if(TextUtils.isEmpty(logisticsNo)){
            PromptUtils.showToast("请输入快递单号");
            return;
        }
        RequestParams requestParams = new RequestParams();
        requestParams.put("uid",userInfo.getUid());
        requestParams.put("id",myOrderMode.getId());
        requestParams.put("express_name",companyName);
        requestParams.put("express_sn",logisticsNo);
        HttpUtil.postWithSign(userInfo.getToken(), IConstants.sOrder+"/return"+"/"+myOrderMode.getId(),requestParams,new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                LogUtils.d("填写物流修改退单返回：",response.toString());
                if(response.optInt("ret")>= 0){
                    PromptUtils.showToast("提交成功");
                    finish();
                }else{
                    PromptUtils.showToast(response.optString("msg"));
                }
            }
        });
    }
    /**
     * 填充 物流公司数据
     */
    private void setReason() {
        HttpUtil.get(IConstants.sGetLogisticsCompany,new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                LogisticsCompanyResponse logisticsCompanyResponse = JSON.parseObject(response.toString(),LogisticsCompanyResponse.class);
                mCompanies = logisticsCompanyResponse.data;
            }
        });

    }


}
