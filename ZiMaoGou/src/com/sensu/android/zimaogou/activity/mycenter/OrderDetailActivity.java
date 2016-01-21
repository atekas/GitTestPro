package com.sensu.android.zimaogou.activity.mycenter;

import android.app.Activity;
import android.app.Dialog;
import android.content.ComponentName;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.*;
import com.alibaba.fastjson.JSON;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.sensu.android.zimaogou.IConstants;
import com.sensu.android.zimaogou.Mode.MyOrderGoodsMode;
import com.sensu.android.zimaogou.Mode.MyOrderMode;
import com.sensu.android.zimaogou.Mode.OrderMode;
import com.sensu.android.zimaogou.Mode.ProductMode;
import com.sensu.android.zimaogou.R;
import com.sensu.android.zimaogou.ReqResponse.MyOrderResponse;
import com.sensu.android.zimaogou.activity.BaseActivity;
import com.sensu.android.zimaogou.activity.ProductCommentActivity;
import com.sensu.android.zimaogou.adapter.OrderDetailListAdapter;
import com.sensu.android.zimaogou.adapter.OrderListAdapter;
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
public class OrderDetailActivity extends BaseActivity {
    ImageView mBackImageView;
    ListView mOrderListView;
    ScrollView mOrderDetailScrollView;
    ArrayList<MyOrderMode> mOrders = new ArrayList<MyOrderMode>();
    String EXTRA_CHARGE = "com.pingplusplus.android.PaymentActivity.CHARGE";
    int REQUEST_CODE_PAYMENT = 1001;
    Button mPayButton;//用来防止重复支付点击的Button
    String CANCEL_ORDER = "1";//取消订单
    String SURE_ORDER = "2";//确认收货


    TextView mTitleTextView, tv_orderNo, tv_orderState, tv_nameAndMobile, tv_receiverAddress;
    Button bt_cancel, bt_submit;
    String id = "";
    UserInfo userInfo;
    int state = 0;
    OrderDetailListAdapter adapter;

    RelativeLayout rl_button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.order_detail_activity);


        if (getIntent().getExtras() != null) {
            id = getIntent().getExtras().getString("id");
            state = getIntent().getExtras().getInt("state");
        }
        initView();

    }

    private void initView() {
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
        rl_button = (RelativeLayout) findViewById(R.id.rl_button);
        if (state == IConstants.sCancel) {
            rl_button.setVisibility(View.GONE);
        } else {
            rl_button.setVisibility(View.VISIBLE);
        }


        mOrderListView.setDivider(null);
        adapter = new OrderDetailListAdapter(this, mOrders, state);
        mOrderListView.setAdapter(adapter);

        mBackImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }

    /**
     * 跳转物流信息
     *
     * @param v
     */
    public void LogisticsClick(View v) {
        startActivity(new Intent(this, LogisticsMessageActivity.class));
    }

    @Override
    protected void onResume() {
        super.onResume();
        getOrderDetail();
    }

    private void getOrderDetail() {
        mOrders.clear();
        showLoading();
        RequestParams requestParams = new RequestParams();
        requestParams.put("uid", userInfo.getUid());
        requestParams.put("id", id);
        HttpUtil.getWithSign(userInfo.getToken(), IConstants.sOrder + "/" + id, requestParams, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                LogUtils.d("订单详情返回：", response.toString());
                cancelLoading();
                final MyOrderMode myOrderMode = JSON.parseObject(response.optJSONObject("data").toString(), MyOrderMode.class);
                tv_orderNo.setText(myOrderMode.getOrder_no());
                state = myOrderMode.getState();
                state = divisiveState(state);
                final int orderState = state;
                switch (state) {
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
                if (state == IConstants.sUnpaid) {
                    bt_cancel.setVisibility(View.VISIBLE);
                    bt_cancel.setText("取消订单");
                    bt_submit.setText("去支付");
                    bt_cancel.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            updateOrderDialog(orderState, myOrderMode);
                        }
                    });
                    bt_submit.setOnClickListener(new PayClickListener());
                } else if (state == IConstants.sUnreceived) {
                    bt_cancel.setVisibility(View.GONE);
                    bt_cancel.setText("查看物流");
                    bt_submit.setText("确认收货");
                    bt_cancel.setOnClickListener(null);
                    bt_submit.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            updateOrderDialog(orderState, myOrderMode);
                        }
                    });
                } else {
                    bt_cancel.setVisibility(View.GONE);
                    if (checkCommentState(myOrderMode)) {
                        rl_button.setVisibility(View.VISIBLE);
                        bt_submit.setText("评价");
                    } else {
                        rl_button.setVisibility(View.GONE);
                    }
                    bt_submit.setOnClickListener(new View.OnClickListener() {

                        @Override
                        public void onClick(View view) {
                            if (myOrderMode.getGoods().size() > 1) {
                                startActivity(new Intent(OrderDetailActivity.this, RefundOrCommentActivity.class).putExtra("type", 1)
                                        .putExtra("data", myOrderMode));
                            } else {
                                startActivity(new Intent(OrderDetailActivity.this, ProductCommentActivity.class).putExtra("order", myOrderMode)
                                        .putExtra("goods", myOrderMode.getGoods().get(0)));
                            }
                        }
                    });
                }

                tv_nameAndMobile.setText(myOrderMode.getReceiver_info().getName() + " " + myOrderMode.getReceiver_info().getMobile());
                tv_receiverAddress.setText(myOrderMode.getReceiver_info().getAddress());
                mOrders.add(myOrderMode);
                adapter.flush(mOrders, state);
                UiUtils.setListViewHeightBasedOnChilds(mOrderListView);
                mOrderDetailScrollView.smoothScrollTo(0, 0);

            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);
                cancelLoading();
            }
        });


    }

    /**
     * 去付款接口
     */
    private void goPay() {

        RequestParams requestParams = new RequestParams();
        requestParams.put("uid", userInfo.getUid());
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

    private void updateOrder(String update) {
        final String state = update;
        RequestParams requestParams = new RequestParams();
        requestParams.put("uid", userInfo.getUid());
        requestParams.put("id", id);
        requestParams.put("state", update);
        HttpUtil.postWithSign(userInfo.getToken(), IConstants.sOrder + "/" + id, requestParams, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                LogUtils.d("修改订单状态返回：", response.toString());
                if (response.optInt("ret") >= 0) {
                    if (state.equals(CANCEL_ORDER)) {
                        PromptUtils.showToast("取消订单成功");

                    } else if (state.equals(SURE_ORDER)) {
                        PromptUtils.showToast("确认收货成功");

                    }
                    getOrderDetail();

                } else {
                    if (state.equals(CANCEL_ORDER)) {
                        PromptUtils.showToast("取消订单失败");
                    } else if (state.equals(SURE_ORDER)) {
                        PromptUtils.showToast("确认收货失败");
                    }
                }
            }
        });

    }

    private class PayClickListener implements View.OnClickListener {


        @Override
        public void onClick(View view) {
            mPayButton = (Button) view;
            mPayButton.setOnClickListener(null);//防止重复点击
            goPay();
        }
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
                PromptUtils.showToast("支付返回：" + result + "  " + errorMsg + "  " + extraMsg);
                mPayButton.setOnClickListener(new PayClickListener());
            }
        }
    }

    /**
     * 根据后台返回stateCode区分状态
     *
     * @param state
     */
    private int divisiveState(int state) {
        int code = 0;
        if (state == 0) {
            code = IConstants.sUnpaid;
        } else if (state >= 1 && state <= 3) {
            code = IConstants.sUnreceived;
        } else if (state == 5 || state == 6) {
            code = IConstants.sReceived;
        } else {
            code = IConstants.sCancel;
        }
        return code;
    }

    /**
     * 计算一个订单的商品数量
     *
     * @param myOrderMode
     * @return
     */
    private int getProductsNum(MyOrderMode myOrderMode) {
        int num = 0;
        for (MyOrderGoodsMode myOrderGoodsMode : myOrderMode.getGoods()) {
            num += Integer.parseInt(myOrderGoodsMode.getNum());
        }
        return num;
    }

    /**
     * 该订单下的商品是否可以评论
     *
     * @param myOrderMode
     * @return
     */
    private boolean checkCommentState(MyOrderMode myOrderMode) {
        boolean canComment = false;
        for (MyOrderGoodsMode myOrderGoodsMode : myOrderMode.getGoods()) {
            if (myOrderGoodsMode.getIs_commented().equals("0")) {
                canComment = true;
                break;
            }
        }
        return canComment;
    }

    /**
     * 修改订单状态dialog
     */
    Dialog mUpdateOrderDialog;

    private void updateOrderDialog(int state, final MyOrderMode myOrderMode) {
        mUpdateOrderDialog = new Dialog(this, R.style.dialog);
        mUpdateOrderDialog.setCancelable(true);
        mUpdateOrderDialog.setContentView(R.layout.delete_address_dialog);

        TextView tv_tip = (TextView) mUpdateOrderDialog.findViewById(R.id.tv_tip);
        Button bt_sure = (Button) mUpdateOrderDialog.findViewById(R.id.bt_sure);
        Button bt_cancel = (Button) mUpdateOrderDialog.findViewById(R.id.bt_cancel);
        String stateString = "";
        if (state == IConstants.sUnpaid) {
            tv_tip.setText("确认取消订单");
            stateString = CANCEL_ORDER;
        } else if (state == IConstants.sUnreceived) {
            tv_tip.setText("确认收货");
            stateString = SURE_ORDER;
        }
        final String finalStateString = stateString;
        bt_sure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                updateOrder(finalStateString);
                mUpdateOrderDialog.dismiss();
            }
        });
        bt_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mUpdateOrderDialog.dismiss();
            }
        });
        mUpdateOrderDialog.show();
    }

}
