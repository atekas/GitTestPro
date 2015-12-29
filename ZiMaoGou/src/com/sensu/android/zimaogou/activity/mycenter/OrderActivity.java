package com.sensu.android.zimaogou.activity.mycenter;

import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import com.alibaba.fastjson.JSON;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.sensu.android.zimaogou.IConstants;
import com.sensu.android.zimaogou.Mode.OrderMode;
import com.sensu.android.zimaogou.Mode.ProductMode;
import com.sensu.android.zimaogou.Mode.Warehouse;
import com.sensu.android.zimaogou.R;
import com.sensu.android.zimaogou.ReqResponse.MyOrderResponse;
import com.sensu.android.zimaogou.activity.BaseActivity;
import com.sensu.android.zimaogou.adapter.OrderListAdapter;
import com.sensu.android.zimaogou.external.greendao.helper.GDBaseHelper;
import com.sensu.android.zimaogou.external.greendao.helper.GDUserInfoHelper;
import com.sensu.android.zimaogou.external.greendao.model.UserInfo;
import com.sensu.android.zimaogou.utils.HttpUtil;
import com.sensu.android.zimaogou.utils.LogUtils;
import com.sensu.android.zimaogou.widget.OnRefreshListener;
import com.sensu.android.zimaogou.widget.RefreshListView;
import org.apache.http.Header;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.util.ArrayList;

/**
 * 订单页面
 * Created by qi.yang on 2015/11/18.
 */
public class OrderActivity extends BaseActivity {
    ImageView mBackImageView;
    RefreshListView mOrderListView;
    int sUnpaid = 1;//待付款
    int sUnreceived = 2;//待收货
    int sRefund = 3;//退款单
    int sAllOrder = 0;//全部订单
    TextView mTitleTextView;
    int type = 0;
    OrderListAdapter adapter ;
    MyOrderResponse myOrderResponse = new MyOrderResponse();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.order_activity);


        if(getIntent().getExtras() != null){
            type = getIntent().getExtras().getInt("type");
        }
        initView();
        getOrder();
    }

    private void initView(){
        mBackImageView = (ImageView) findViewById(R.id.back);
        mOrderListView = (RefreshListView) findViewById(R.id.lv_orders);
        mTitleTextView = (TextView) findViewById(R.id.tv_title);
        mOrderListView.setDivider(null);
        mOrderListView.setOnRefreshListener(mOnRefreshListener);

        mBackImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        switch (type){
            case 1:
                mTitleTextView.setText("待付款");
                break;
            case 2:
                mTitleTextView.setText("待收货");
                break;
            case 3:
                mTitleTextView.setText("退货单");
                break;

        }
    }
    private Handler mHandler = new Handler();
    private OnRefreshListener mOnRefreshListener = new OnRefreshListener() {
        @Override
        public void onDownPullRefresh() {
            //下拉刷新接口
            mHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    mOrderListView.hideHeaderView();
                }
            }, 2000);
        }

        @Override
        public void onLoadingMore() {
            //上拉加载接口
            mHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    mOrderListView.hideFooterView();
                }
            }, 2000);
        }
    };

    private void getOrder(){
        UserInfo userInfo = GDUserInfoHelper.getInstance(this).getUserInfo();
        RequestParams requestParams = new RequestParams();
        requestParams.put("uid","23");
        requestParams.put("state",type+"");
        HttpUtil.getWithSign(userInfo.getToken(), IConstants.sGetMyOrder,requestParams,new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                LogUtils.d("获取我的订单返回：",response.toString());
                myOrderResponse = JSON.parseObject(response.toString(),MyOrderResponse.class);
                adapter = new OrderListAdapter(OrderActivity.this,myOrderResponse.data);
                mOrderListView.setAdapter(adapter);

            }
        });

    }
}
