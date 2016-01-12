package com.sensu.android.zimaogou.activity.mycenter;

import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.*;
import com.alibaba.fastjson.JSON;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.sensu.android.zimaogou.IConstants;
import com.sensu.android.zimaogou.Mode.MyOrderMode;
import com.sensu.android.zimaogou.Mode.OrderMode;
import com.sensu.android.zimaogou.Mode.ProductMode;
import com.sensu.android.zimaogou.R;
import com.sensu.android.zimaogou.ReqResponse.MyOrderResponse;
import com.sensu.android.zimaogou.activity.BaseActivity;
import com.sensu.android.zimaogou.adapter.OrderDetailListAdapter;
import com.sensu.android.zimaogou.adapter.OrderListAdapter;
import com.sensu.android.zimaogou.external.greendao.helper.GDUserInfoHelper;
import com.sensu.android.zimaogou.external.greendao.model.UserInfo;
import com.sensu.android.zimaogou.utils.HttpUtil;
import com.sensu.android.zimaogou.utils.LogUtils;
import com.sensu.android.zimaogou.utils.UiUtils;
import com.sensu.android.zimaogou.widget.OnRefreshListener;
import com.sensu.android.zimaogou.widget.RefreshListView;
import org.apache.http.Header;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * 订单页面
 * Created by qi.yang on 2015/11/18.
 */
public class OrderDetailActivity extends BaseActivity {
    ImageView mBackImageView;
    ListView mOrderListView;
    ScrollView mOrderDetailScrollView;
    ArrayList<MyOrderMode> mOrders = new ArrayList<MyOrderMode>();

    TextView mTitleTextView,tv_orderNo,tv_orderState,tv_nameAndMobile,tv_receiverAddress;
    Button bt_cancel,bt_submit;
    String id = "";
    UserInfo userInfo;
    int state = 0;
    OrderDetailListAdapter adapter ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.order_detail_activity);


        if(getIntent().getExtras() != null){
            id = getIntent().getExtras().getString("id");
            state = getIntent().getExtras().getInt("state");
        }
        initView();
        getOrderDetail();
    }

    private void initView(){
        userInfo = GDUserInfoHelper.getInstance(this).getUserInfo();
        mBackImageView = (ImageView) findViewById(R.id.back);
        mOrderListView = (ListView) findViewById(R.id.lv_orders);
        mTitleTextView = (TextView) findViewById(R.id.tv_title);
        mOrderDetailScrollView = (ScrollView) findViewById(R.id.sv_orderDetail);
        tv_orderNo = (TextView) findViewById(R.id.tv_orderNo);
        tv_orderState = (TextView) findViewById(R.id.tv_orderState);
        tv_nameAndMobile = (TextView) findViewById(R.id.tv_nameAndMobile);
        tv_receiverAddress = (TextView) findViewById(R.id.tv_receiverAddress);
        bt_cancel = (Button) findViewById(R.id.bt_cancel);
        bt_submit = (Button) findViewById(R.id.bt_submit);

        mOrderListView.setDivider(null);
        adapter = new OrderDetailListAdapter(this,mOrders);
        mOrderListView.setAdapter(adapter);

        mBackImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }




    private void getOrderDetail(){
        RequestParams requestParams = new RequestParams();
        requestParams.put("uid",userInfo.getUid());
        requestParams.put("id",id);
        HttpUtil.getWithSign(userInfo.getToken(), IConstants.sOrder+"/"+id,requestParams,new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                LogUtils.d("订单详情返回：",response.toString());
                MyOrderMode myOrderMode = JSON.parseObject(response.optJSONObject("data").toString(), MyOrderMode.class);
                tv_orderNo.setText(myOrderMode.getOrder_no());
                switch (state){
                    case IConstants.sUnpaid:
                        tv_orderState.setText("待付款");
                        break;
                    case IConstants.sUnreceived:
                        tv_orderState.setText("待收货");
                        break;
                    case IConstants.sReceived:
                        tv_orderState.setText("交易成功");
                        break;
                }
                tv_nameAndMobile.setText(myOrderMode.getReceiver_info().getName()+" "+myOrderMode.getReceiver_info().getMobile());
                tv_receiverAddress.setText(myOrderMode.getReceiver_info().getAddress());
                mOrders.add(myOrderMode);
                adapter.flush(mOrders);
                UiUtils.setListViewHeightBasedOnChilds(mOrderListView);
                mOrderDetailScrollView.smoothScrollTo(0,0);

            }
        });

    }
}
