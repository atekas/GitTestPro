package com.sensu.android.zimaogou.activity.mycenter;

import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ImageView;
import com.sensu.android.zimaogou.Mode.OrderMode;
import com.sensu.android.zimaogou.Mode.ProductMode;
import com.sensu.android.zimaogou.Mode.Warehouse;
import com.sensu.android.zimaogou.R;
import com.sensu.android.zimaogou.activity.BaseActivity;
import com.sensu.android.zimaogou.adapter.OrderListAdapter;
import com.sensu.android.zimaogou.external.greendao.helper.GDBaseHelper;
import com.sensu.android.zimaogou.widget.OnRefreshListener;
import com.sensu.android.zimaogou.widget.RefreshListView;

import java.util.ArrayList;

/**
 * 订单页面
 * Created by qi.yang on 2015/11/18.
 */
public class OrderActivity extends BaseActivity {
    ImageView mBackImageView;
    RefreshListView mOrderListView;
    ArrayList<OrderMode> mOrders = new ArrayList<OrderMode>();
    int sUnpaid = 1;//待付款
    int sUnreceived = 2;//待收货
    int sRefund = 3;//退款单
    int sAllOrder = 0;//全部订单

    int type = 0;
    OrderListAdapter adapter = new OrderListAdapter(this,mOrders);
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.order_activity);
        initView();
        setData();
        if(getIntent().getExtras() != null){
            type = getIntent().getExtras().getInt("type");
        }
        if(type != 0) {
            changeType();
        }
        adapter.flush(mOrders);
    }

    private void initView(){
        mBackImageView = (ImageView) findViewById(R.id.back);
        mOrderListView = (RefreshListView) findViewById(R.id.lv_orders);
        mOrderListView.setDivider(null);
        mOrderListView.setOnRefreshListener(mOnRefreshListener);
        mOrderListView.setAdapter(adapter);
        mBackImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
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

    private void setData(){
        ArrayList<ProductMode> pros1 = new ArrayList<ProductMode>();
        ArrayList<ProductMode> pros2 = new ArrayList<ProductMode>();
        ArrayList<ProductMode> pros3 = new ArrayList<ProductMode>();
        ArrayList<ProductMode> pros4 = new ArrayList<ProductMode>();
        ArrayList<ProductMode> pros5 = new ArrayList<ProductMode>();
        ProductMode mode1 = new ProductMode();
        mode1.setTestImg(R.drawable.product3);
        mode1.setTestTitle("懒人支架，给你最好的体验");
        mode1.setPrice(100);
        mode1.setNum(1);
        pros1.add(mode1);
        ProductMode mode2 = new ProductMode();
        mode2.setTestImg(R.drawable.product3);
        mode2.setPrice(101);
        mode2.setNum(1);
        mode2.setTestTitle("抹茶手工皂精油");
        pros2.add(mode2);

        ProductMode mode3 = new ProductMode();
        mode3.setTestImg(R.drawable.product3);
        mode3.setPrice(103);
        mode3.setNum(1);
        mode3.setTestTitle("YEASALAND——your best partner");
        pros3.add(mode3);
        ProductMode mode4 = new ProductMode();
        mode4.setTestImg(R.drawable.product3);
        mode4.setPrice(104);
        mode4.setNum(1);
        mode4.setTestTitle("可伤过的痛过的我，向谁述说");
        pros4.add(mode4);
        ProductMode mode5 = new ProductMode();
        mode5.setTestImg(R.drawable.product3);
        mode5.setPrice(105);
        mode5.setNum(1);
        mode5.setTestTitle("还是放开了说好不分的手");
        pros5.add(mode5);
        pros1.add(mode2);
        OrderMode orderMode1 = new OrderMode();
        orderMode1.setType(1);
        orderMode1.setPros(pros1);
        OrderMode orderMode2 = new OrderMode();
        orderMode2.setType(2);
        orderMode2.setPros(pros2);
        OrderMode orderMode3 = new OrderMode();
        orderMode3.setType(3);
        orderMode3.setPros(pros3);
        OrderMode orderMode4 = new OrderMode();
        orderMode4.setType(5);
        orderMode4.setPros(pros4);
        OrderMode orderMode5 = new OrderMode();
        orderMode5.setType(2);
        orderMode5.setPros(pros1);

        mOrders.add(orderMode1);
        mOrders.add(orderMode2);
        mOrders.add(orderMode3);
        mOrders.add(orderMode4);
        mOrders.add(orderMode5);

    }
    /**
     *
     * 测试用修改订单状态
     *
     */
    public void changeType(){
        for(int i = 0; i < mOrders.size(); i++){
            mOrders.get(i).setType(type);
        }
    }
}
