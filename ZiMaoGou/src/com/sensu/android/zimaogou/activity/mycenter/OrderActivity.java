package com.sensu.android.zimaogou.activity.mycenter;

import android.app.Activity;
import android.app.Dialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.*;
import android.widget.*;
import com.alibaba.fastjson.JSON;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.sensu.android.zimaogou.IConstants;
import com.sensu.android.zimaogou.Mode.MyOrderGoodsMode;
import com.sensu.android.zimaogou.Mode.MyOrderMode;
import com.sensu.android.zimaogou.R;
import com.sensu.android.zimaogou.ReqResponse.MyOrderResponse;
import com.sensu.android.zimaogou.activity.BaseActivity;
import com.sensu.android.zimaogou.activity.ProductCommentActivity;
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

    public int sAllOrder = 0;//全部订单
    TextView mTitleTextView;
    int type = 0;
    OrderListAdapter1 adapter;
    UserInfo userInfo;
    ArrayList<MyOrderMode> mOrders = new ArrayList<MyOrderMode>();
    String EXTRA_CHARGE = "com.pingplusplus.android.PaymentActivity.CHARGE";
    int REQUEST_CODE_PAYMENT = 1001;
    MyOrderResponse myOrderResponse = new MyOrderResponse();
    Button mPayButton;//用来防止重复支付点击的Button
    LinearLayout ll_content;
    String CANCEL_ORDER = "1";//取消订单
    String SURE_ORDER = "2";//确认收货

    MyOrderMode chooseOrderMode = new MyOrderMode();//付款的订单
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.order_activity);


        if (getIntent().getExtras() != null) {
            type = getIntent().getExtras().getInt("type");
        }
        initView();

    }

    @Override
    protected void onResume() {
        super.onResume();
        getOrder();
    }

    private void initView() {
        userInfo = GDUserInfoHelper.getInstance(this).getUserInfo();
        mBackImageView = (ImageView) findViewById(R.id.back);
        mOrderListView = (RefreshListView) findViewById(R.id.lv_orders);
        mTitleTextView = (TextView) findViewById(R.id.tv_title);
        mOrderListView.setDivider(null);
        mOrderListView.setOnRefreshListener(mOnRefreshListener);
        ll_content = (LinearLayout) findViewById(R.id.ll_content);
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
        mOrders.clear();
        UserInfo userInfo = GDUserInfoHelper.getInstance(this).getUserInfo();
        RequestParams requestParams = new RequestParams();
        requestParams.put("uid", userInfo.getUid());
        requestParams.put("state", type + "");
        HttpUtil.getWithSign(userInfo.getToken(), IConstants.sGetMyOrder, requestParams, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                LogUtils.d("获取我的订单返回：", response.toString());
                myOrderResponse = JSON.parseObject(response.toString(), MyOrderResponse.class);
                mOrders = myOrderResponse.data;
                adapter = new OrderListAdapter1(OrderActivity.this, mOrders);
                mOrderListView.setAdapter(adapter);
                if (myOrderResponse.data.size() == 0) {
                    exceptionLinearLayout.setException(IConstants.EXCEPTION_ORDER_IS_NULL);
                    ll_content.addView(ExceptionView);
                } else {
                    ll_content.removeView(ExceptionView);
                }
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
//                viewHolder.bt_comment = (Button) view.findViewById(R.id.bt_comment);
                viewHolder.rl_top = (RelativeLayout) view.findViewById(R.id.rl_top);
                view.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) view.getTag();
            }

            final MyOrderMode myOrderMode = mOrders.get(i);
            int state = myOrderMode.getState();
            state = divisiveState(state);
            final int orderState = state;
            viewHolder.rl_top.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mContext.startActivity(new Intent(mContext, OrderDetailActivity.class)
                            .putExtra("id", myOrderMode.getId())
                            .putExtra("state", orderState));
                }
            });
            viewHolder.rl_amount.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mContext.startActivity(new Intent(mContext, OrderDetailActivity.class)
                            .putExtra("id", myOrderMode.getId())
                            .putExtra("state", orderState));
                }
            });
            if(state == IConstants.sCancel){
                viewHolder.rl_button.setVisibility(View.GONE);
            }else{
                viewHolder.rl_button.setVisibility(View.VISIBLE);
            }


            if (state == IConstants.sUnpaid) {
                viewHolder.bt_cancel.setVisibility(View.VISIBLE);
                viewHolder.bt_cancel.setText("取消订单");
                viewHolder.bt_submit.setText("去支付");
                viewHolder.bt_cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        updateOrderDialog(orderState,myOrderMode);
                    }
                });
                viewHolder.bt_submit.setOnClickListener(new PayClickListener(myOrderMode));

            } else if (state == IConstants.sUnreceived) {
                viewHolder.bt_cancel.setVisibility(View.GONE);
                viewHolder.bt_cancel.setText("查看物流");
                viewHolder.bt_submit.setText("确认收货");
                viewHolder.bt_cancel.setOnClickListener(null);
                viewHolder.bt_submit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        updateOrderDialog(orderState, myOrderMode);
                    }
                });
            } else if(state == IConstants.sReceived){
                viewHolder.bt_cancel.setVisibility(View.GONE);
                if(checkCommentState(myOrderMode)){
                    viewHolder.rl_button.setVisibility(View.VISIBLE);
                }else{
                    viewHolder.rl_button.setVisibility(View.GONE);
                }
                viewHolder.bt_submit.setText("评价");
                viewHolder.bt_submit.setOnClickListener(new View.OnClickListener() {

                        @Override
                        public void onClick(View view) {
                            if(myOrderMode.getGoods().size() > 1) {
                                mContext.startActivity(new Intent(mContext, RefundOrCommentActivity.class).putExtra("type", 1)
                                        .putExtra("data", myOrderMode));
                            }else{
                                mContext.startActivity(new Intent(mContext, ProductCommentActivity.class).putExtra("order",myOrderMode)
                                        .putExtra("goods",myOrderMode.getGoods().get(0)));
                            }
                        }
                    });
            }
//                    viewHolder.bt_comment.setOnClickListener(new View.OnClickListener() {
//
//                        @Override
//                        public void onClick(View view) {
//                            mContext.startActivity(new Intent(mContext, RefundOrCommentActivity.class).putExtra("type", 1));
//                        }
//                    });


            viewHolder.tv_orderType.setText(mOrders.get(i).getState_cn());

            viewHolder.tv_showNum.setText("共" + getProductsNum(mOrders.get(i)) + "件商品，合计:");
            viewHolder.tv_amount.setText(mOrders.get(i).getAmount_total());
            viewHolder.tv_orderNo.setText(mOrders.get(i).getOrder_no());
            viewHolder.tv_freight.setText("(含运费：￥" + mOrders.get(i).getAmount_express() + ")");

            OrderChildListAdapter adapter = new OrderChildListAdapter(mContext, mOrders.get(i).getGoods(), state);
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
        public Button bt_cancel, bt_submit;
//        public Button bt_comment;
    }

    /**
     * 去付款接口
     */
    private void goPay(String payType,MyOrderMode myOrderMode) {

        RequestParams requestParams = new RequestParams();
        requestParams.put("uid", userInfo.getUid());
        requestParams.put("order_no",myOrderMode.getOrder_no());
        requestParams.put("pay_type",payType);
        HttpUtil.postWithSign(GDUserInfoHelper.getInstance(this).getUserInfo().getToken(), IConstants.sOrderPay, requestParams, new JsonHttpResponseHandler() {
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

    private void updateOrder(String update, final MyOrderMode orderMode) {
        final String state = update;
        String id = orderMode.getId();
        RequestParams requestParams = new RequestParams();
        requestParams.put("uid", userInfo.getUid());
        requestParams.put("id", id);
        requestParams.put("state", update);
        HttpUtil.postWithSign(userInfo.getToken(), IConstants.sOrder + "/" + id, requestParams, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                LogUtils.d("修改订单状态返回：",response.toString());
                if (response.optInt("ret") >= 0) {
                    if (state.equals(CANCEL_ORDER)) {
                        PromptUtils.showToast("取消订单成功");
                        if (type == 0) {
                            orderMode.setState(12);
                            orderMode.setState_cn("已取消");
                        } else {
                            mOrders.remove(orderMode);
                        }
                        adapter.notifyDataSetChanged();
                    } else if (state.equals(SURE_ORDER)) {
                        PromptUtils.showToast("确认收货成功");
                        if (type == 0) {
                            orderMode.setState(5);
                            orderMode.setState_cn("交易完成");
                        } else {
                            mOrders.remove(orderMode);
                        }
                        adapter.notifyDataSetChanged();

                    }


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
        MyOrderMode myOrderMode;

        public PayClickListener(MyOrderMode myOrderMode) {
            this.myOrderMode = myOrderMode;

        }

        @Override
        public void onClick(View view) {
            mPayButton = (Button) view;
            mPayButton.setOnClickListener(null);//防止重复点击
            chooseOrderMode = myOrderMode;
            choosePayTypeDialog(myOrderMode);

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
                mPayButton.setOnClickListener(new PayClickListener(chooseOrderMode));
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
        } else if(state ==5||state == 6){
            code = IConstants.sReceived;
        }else{
            code = IConstants.sCancel;
        }
        return code;
    }


    /**
     * 计算一个订单的商品数量
     * @param myOrderMode
     * @return
     */
    private int getProductsNum(MyOrderMode myOrderMode){
        int num = 0;
        for(MyOrderGoodsMode myOrderGoodsMode: myOrderMode.getGoods()){
            num += Integer.parseInt(myOrderGoodsMode.getNum());
        }
        return num;
    }

    /**
     * 该订单下的商品是否可以评论
     * @param myOrderMode
     * @return
     */
    private boolean checkCommentState(MyOrderMode myOrderMode){
        boolean canComment = false;
        for(MyOrderGoodsMode myOrderGoodsMode:myOrderMode.getGoods()){
            if(myOrderGoodsMode.getIs_commented().equals("0")){
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
        if(state == IConstants.sUnpaid){
            tv_tip.setText("确认取消订单");
            stateString = CANCEL_ORDER;
        }else if(state == IConstants.sUnreceived){
            tv_tip.setText("确认收货");
            stateString = SURE_ORDER;
        }
        final String finalStateString = stateString;
        bt_sure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                updateOrder(finalStateString,myOrderMode);
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

    Dialog mPayTypeChooseDialog;
    boolean isAlipay = true;
    private void choosePayTypeDialog(final MyOrderMode myOrderMode){
        mPayTypeChooseDialog = new Dialog(this,R.style.notParentDialog);
        mPayTypeChooseDialog.setCancelable(true);
        mPayTypeChooseDialog.setContentView(R.layout.pay_type_choose_dialog);
        TextView tv_money = (TextView) mPayTypeChooseDialog.findViewById(R.id.tv_money);
        RelativeLayout rl_alipay = (RelativeLayout) mPayTypeChooseDialog.findViewById(R.id.rl_alipay);
        RelativeLayout rl_wxpay = (RelativeLayout) mPayTypeChooseDialog.findViewById(R.id.rl_wxpay);
        TextView tv_submit = (TextView) mPayTypeChooseDialog.findViewById(R.id.submit);
        final ImageView img_alipay = (ImageView) mPayTypeChooseDialog.findViewById(R.id.img_alipay);
        final ImageView img_wxpay  = (ImageView) mPayTypeChooseDialog.findViewById(R.id.img_wxpay);


        img_alipay.setSelected(isAlipay);
        img_wxpay.setSelected(!isAlipay);
        tv_money.setText("支付金额：￥"+myOrderMode.getAmount_total());
        rl_alipay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isAlipay = !isAlipay;
                img_alipay.setSelected(isAlipay);
                img_wxpay.setSelected(!isAlipay);
            }
        });
        rl_wxpay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isAlipay = !isAlipay;
                img_alipay.setSelected(isAlipay);
                img_wxpay.setSelected(!isAlipay);
            }
        });
        tv_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goPay(isAlipay?"1":"2",myOrderMode);
                mPayTypeChooseDialog.dismiss();
            }
        });

        WindowManager m = this.getWindowManager();

        Window dialogWindow = mPayTypeChooseDialog.getWindow();

//        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
//        dialogWindow.setGravity(Gravity.TOP);
//        lp.y = DisplayUtils.dp2px(50);
//        dialogWindow.setAttributes(lp);

        Display d = m.getDefaultDisplay(); // 获取屏幕宽、高用
        WindowManager.LayoutParams p = dialogWindow.getAttributes(); // 获取对话框当前的参数值
//        p.height = (int) d.getHeight() ; // 高度设置为屏幕
        p.width = (int) d.getWidth() ; // 宽度设置为屏幕
        dialogWindow.setAttributes(p);
        mPayTypeChooseDialog.show();
    }
}
