package com.sensu.android.zimaogou.activity.mycenter;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import com.alibaba.fastjson.JSON;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.sensu.android.zimaogou.BaseApplication;
import com.sensu.android.zimaogou.IConstants;
import com.sensu.android.zimaogou.Mode.ProvinceMode;
import com.sensu.android.zimaogou.R;
import com.sensu.android.zimaogou.ReqResponse.AddressResponse;
import com.sensu.android.zimaogou.activity.BaseActivity;
import com.sensu.android.zimaogou.utils.HttpUtil;
import com.sensu.android.zimaogou.utils.LogUtils;
import com.sensu.android.zimaogou.utils.TextUtils;
import org.apache.http.Header;
import org.json.JSONObject;
import org.w3c.dom.Text;

/**
 * Created by qi.yang on 2015/11/20.
 */
public class ReceiverAddressEditActivity extends BaseActivity {
    ImageView mBackImageView;
    TextView mTitleTextView,mProvinceTextView;
    ProvinceMode mResultAddress = BaseApplication.mChooseAddress;
    AddressResponse addressResponse = new AddressResponse();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.receiver_address_edit_activity);
        initView();
    }

    private void initView(){
        mBackImageView = (ImageView) findViewById(R.id.back);
        mTitleTextView = (TextView) findViewById(R.id.tv_title);
        if(getIntent().getExtras() != null){
            mTitleTextView.setText(getIntent().getExtras().getString("title"));
        }
        mProvinceTextView = (TextView) findViewById(R.id.tv_province);
        mBackImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        getAddress();
    }
    private void getAddress(){
        HttpUtil.get(IConstants.sGetProvinceAndCity, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                LogUtils.d("获取城市返回：",response.toString());
                addressResponse = JSON.parseObject(response.toString(), AddressResponse.class);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(mResultAddress.data!= null&& mResultAddress.data.get(0) != null && mResultAddress.data.get(0).data.get(0) != null){
            mProvinceTextView.setText(mResultAddress.getName()+" "+mResultAddress.data.get(0).getName()+" "+mResultAddress.data.get(0).data.get(0).getName());
        }else{
            mProvinceTextView.setText("");
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
}
