package com.sensu.android.zimaogou.activity.mycenter;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import com.alibaba.fastjson.JSON;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.sensu.android.zimaogou.IConstants;
import com.sensu.android.zimaogou.Mode.MyOrderMode;
import com.sensu.android.zimaogou.R;
import com.sensu.android.zimaogou.ReqResponse.MyOrderResponse;
import com.sensu.android.zimaogou.activity.BaseActivity;
import com.sensu.android.zimaogou.adapter.OrderChildListAdapter;
import com.sensu.android.zimaogou.adapter.SimpleBaseAdapter;
import com.sensu.android.zimaogou.external.greendao.helper.GDUserInfoHelper;
import com.sensu.android.zimaogou.external.greendao.model.UserInfo;
import com.sensu.android.zimaogou.utils.HttpUtil;
import com.sensu.android.zimaogou.utils.LogUtils;
import com.sensu.android.zimaogou.utils.PromptUtils;
import com.sensu.android.zimaogou.utils.UiUtils;
import com.sensu.android.zimaogou.widget.OnRefreshListener;
import com.sensu.android.zimaogou.widget.RefreshListView;
import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

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
    OrderListAdapter1 adapter;
    String EXTRA_CHARGE = "com.pingplusplus.android.PaymentActivity.CHARGE";
    int REQUEST_CODE_PAYMENT = 1001;
    MyOrderResponse myOrderResponse = new MyOrderResponse();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.order_activity);


        if (getIntent().getExtras() != null) {
            type = getIntent().getExtras().getInt("type");
        }
        initView();
        getOrder();
    }

    private void initView() {
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
        switch (type) {
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

    private void getOrder() {
        UserInfo userInfo = GDUserInfoHelper.getInstance(this).getUserInfo();
        RequestParams requestParams = new RequestParams();
        requestParams.put("uid", "23");
        requestParams.put("state", type + "");
        HttpUtil.getWithSign(userInfo.getToken(), IConstants.sGetMyOrder, requestParams, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                LogUtils.d("获取我的订单返回：", response.toString());
                myOrderResponse = JSON.parseObject(response.toString(), MyOrderResponse.class);
                adapter = new OrderListAdapter1(OrderActivity.this, myOrderResponse.data);
                mOrderListView.setAdapter(adapter);

            }
        });

    }


    public class OrderListAdapter1 extends SimpleBaseAdapter {
        ArrayList<MyOrderMode> mOrders;


        public void flush(ArrayList<MyOrderMode> orders) {
            this.mOrders = orders;
            this.notifyDataSetChanged();
        }

        public OrderListAdapter1(Context context, ArrayList<MyOrderMode> mOrders) {
            super(context);
            this.mOrders = mOrders;
        }

        @Override
        public int getCount() {
            return mOrders.size();
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            ViewHolder viewHolder;
            if (view == null) {
                viewHolder = new ViewHolder();
                view = LayoutInflater.from(mContext).inflate(R.layout.order_list_item, null);
                viewHolder.tv_amount = (TextView) view.findViewById(R.id.tv_amount);
                viewHolder.tv_freight = (TextView) view.findViewById(R.id.tv_freight);
                viewHolder.tv_orderNo = (TextView) view.findViewById(R.id.tv_orderNo);
                viewHolder.tv_orderType = (TextView) view.findViewById(R.id.tv_type);
                viewHolder.tv_showNum = (TextView) view.findViewById(R.id.tv_showNum);
                viewHolder.lv_products = (ListView) view.findViewById(R.id.product_child);
                viewHolder.rl_amount = (RelativeLayout) view.findViewById(R.id.rl_amount);
                viewHolder.rl_button = (RelativeLayout) view.findViewById(R.id.rl_button);
                viewHolder.bt_cancel = (Button) view.findViewById(R.id.bt_cancel);
                viewHolder.bt_submit = (Button) view.findViewById(R.id.bt_submit);
                viewHolder.bt_comment = (Button) view.findViewById(R.id.bt_comment);
                viewHolder.rl_top = (RelativeLayout) view.findViewById(R.id.rl_top);
                view.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) view.getTag();
            }
            viewHolder.rl_top.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mContext.startActivity(new Intent(mContext, OrderDetailActivity.class));
                }
            });
            viewHolder.rl_amount.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mContext.startActivity(new Intent(mContext, OrderDetailActivity.class));
                }
            });
            switch (mOrders.get(i).getState()) {
                case 0:
                    viewHolder.rl_button.setVisibility(View.VISIBLE);
                    viewHolder.rl_amount.setVisibility(View.VISIBLE);
                    viewHolder.bt_cancel.setVisibility(View.GONE);
                    viewHolder.bt_comment.setVisibility(View.GONE);
                    viewHolder.bt_submit.setText("付款");
                    viewHolder.tv_orderType.setText("待付款");
                    viewHolder.bt_submit.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            goPay();
                        }
                    });
                    break;
                case 1:
                    viewHolder.rl_button.setVisibility(View.VISIBLE);
                    viewHolder.rl_amount.setVisibility(View.VISIBLE);
                    viewHolder.bt_cancel.setVisibility(View.VISIBLE);
                    viewHolder.bt_comment.setVisibility(View.GONE);
                    viewHolder.bt_cancel.setText("查看物流");
                    viewHolder.bt_submit.setText("确认收货");
                    viewHolder.tv_orderType.setText("待收货");
                    break;
                case 2:
                    viewHolder.rl_button.setVisibility(View.VISIBLE);
                    viewHolder.rl_amount.setVisibility(View.GONE);
                    viewHolder.bt_cancel.setVisibility(View.VISIBLE);
                    viewHolder.bt_comment.setVisibility(View.GONE);
                    viewHolder.bt_cancel.setText("取消订单");
                    viewHolder.bt_submit.setText("撤销退款");
                    viewHolder.tv_orderType.setText("待退款");
                    break;
                case 5:
                    viewHolder.rl_button.setVisibility(View.VISIBLE);
                    viewHolder.rl_amount.setVisibility(View.VISIBLE);
                    viewHolder.bt_cancel.setVisibility(View.VISIBLE);
                    viewHolder.bt_comment.setVisibility(View.VISIBLE);
                    viewHolder.bt_cancel.setText("查看物流");
                    viewHolder.bt_submit.setText("申请售后");
                    viewHolder.tv_orderType.setText("交易成功");
                    viewHolder.bt_submit.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            mContext.startActivity(new Intent(mContext, RefundOrCommentActivity.class).putExtra("type", 0));
                        }
                    });
                    viewHolder.bt_comment.setOnClickListener(new View.OnClickListener() {

                        @Override
                        public void onClick(View view) {
                            mContext.startActivity(new Intent(mContext, RefundOrCommentActivity.class).putExtra("type", 1));
                        }
                    });
                    break;
            }


            viewHolder.tv_showNum.setText("共" + mOrders.size() + "件商品，合计:");
            viewHolder.tv_amount.setText(mOrders.get(i).getAmount_total());
            viewHolder.tv_orderNo.setText(mOrders.get(i).getOrder_no());
            viewHolder.tv_freight.setText("(含运费：￥" + mOrders.get(i).getAmount_express() + ")");

            OrderChildListAdapter adapter = new OrderChildListAdapter(mContext, mOrders.get(i).getGoods(), mOrders.get(i).getState());
            viewHolder.lv_products.setDivider(null);
            viewHolder.lv_products.setAdapter(adapter);
            adapter.flush(mOrders.get(i).getGoods());
            UiUtils.setListViewHeightBasedOnChilds(viewHolder.lv_products);
            return view;
        }
    }

    private class ViewHolder {
        public TextView tv_orderNo, tv_orderType, tv_showNum, tv_amount, tv_freight;
        public ListView lv_products;
        public RelativeLayout rl_amount, rl_button, rl_top;
        public Button bt_cancel, bt_submit, bt_comment;
    }

    private void goPay() {
        RequestParams requestParams = new RequestParams();
        requestParams.put("uid", "21");
        HttpUtil.postWithSign(GDUserInfoHelper.getInstance(this).getUserInfo().getToken(), IConstants.sGetPayCharge, requestParams, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                LogUtils.d("获取支付charge：", response.toString());
                try {
                    String charge = response.getJSONObject("data").getJSONObject("pay_info").toString();
                    Intent intent = new Intent();
                    String packageName = getPackageName();
                    ComponentName componentName = new ComponentName(packageName, packageName + ".wxapi.WXPayEntryActivity");
                    intent.setComponent(componentName);
                    intent.putExtra(EXTRA_CHARGE, charge);
                    startActivityForResult(intent, REQUEST_CODE_PAYMENT);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_PAYMENT) {
            if (resultCode == Activity.RESULT_OK) {
                String result = data.getExtras().getString("pay_result");
                /* 处理返回值
                 * "success" - payment succeed
                 * "fail"    - payment failed
                 * "cancel"  - user canceld
                 * "invalid" - payment plugin not installed
                 */
                String errorMsg = data.getExtras().getString("error_msg"); // 错误信息
                String extraMsg = data.getExtras().getString("extra_msg"); // 错误信息
                PromptUtils.showToast("支付返回："+result+"  "+errorMsg+"  "+extraMsg);
            }
        }
    }
}
