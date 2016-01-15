package com.sensu.android.zimaogou.activity.mycenter;

import android.app.Activity;
import android.app.Dialog;
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
import com.sensu.android.zimaogou.Mode.MyOrderGoodsMode;
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
 * 退货单页面
 * Created by qi.yang on 2015/11/18.
 */
public class RefundOrderActivity extends BaseActivity {
    ImageView mBackImageView;
    RefreshListView mOrderListView;

    public int sAllOrder = 0;//全部订单
    TextView mTitleTextView;
    int type = 3;
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

    int agreeRefundGoods = 1;//同意退货
    int agreeRefundMoney = 12;//同意退款

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.order_activity);
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

        mTitleTextView.setText("退货单");
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
        requestParams.put("uid", userInfo.getUid());
        requestParams.put("state", type + "");
        HttpUtil.getWithSign(userInfo.getToken(), IConstants.sGetMyOrder, requestParams, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                LogUtils.d("获取我的退货单返回：", response.toString());
                myOrderResponse = JSON.parseObject(response.toString(), MyOrderResponse.class);
                mOrders = myOrderResponse.data;
                adapter = new OrderListAdapter1(RefundOrderActivity.this, mOrders);
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
                view = LayoutInflater.from(mContext).inflate(R.layout.order_refund_list_item, null);
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
                    mContext.startActivity(new Intent(mContext, RefundOrderDetailActivity.class)
                            .putExtra("id", myOrderMode.getId())
                            .putExtra("state", orderState));
                }
            });
            viewHolder.rl_amount.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mContext.startActivity(new Intent(mContext, RefundOrderDetailActivity.class)
                            .putExtra("id", myOrderMode.getId())
                            .putExtra("state", orderState));
                }
            });
            if (state == agreeRefundGoods) {
                viewHolder.bt_cancel.setVisibility(View.VISIBLE);

            } else {
                viewHolder.bt_cancel.setVisibility(View.GONE);
            }
            viewHolder.bt_submit.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View view) {
                    updateOrderDialog(myOrderMode);
                }
            });
            viewHolder.bt_cancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    startActivity(new Intent(RefundOrderActivity.this,EditLogisticsActivity.class).putExtra("data",myOrderMode));
                }
            });

            viewHolder.tv_orderType.setText(mOrders.get(i).getState_cn());

            viewHolder.tv_showNum.setText("共" + getProductsNum(mOrders.get(i)) + "件商品，合计:");
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
        public Button bt_cancel, bt_submit;
//        public Button bt_comment;
    }


    private void updateOrder(final MyOrderMode orderMode) {
        String id = orderMode.getId();
        RequestParams requestParams = new RequestParams();
        requestParams.put("uid", userInfo.getUid());
        requestParams.put("id", id);
        requestParams.put("state", "1");
        HttpUtil.postWithSign(userInfo.getToken(), IConstants.sRefundOrder + "/" + id, requestParams, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                LogUtils.d("撤销退款订单状态返回：", response.toString());
                if (response.optInt("ret") >= 0) {
                    PromptUtils.showToast("撤销退款成功");
                    mOrders.remove(orderMode);
                    adapter.notifyDataSetChanged();

                } else {

                    PromptUtils.showToast("撤销退款失败");

                }
            }
        });

    }


//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        if (requestCode == REQUEST_CODE_PAYMENT) {
//            if (resultCode == Activity.RESULT_OK) {
//                String result = data.getExtras().getString("pay_result");
//                /* 处理返回值
//                 * "success" - payment succeed
//                 * "fail"    - payment failed
//                 * "cancel"  - user canceld
//                 * "invalid" - payment plugin not installed
//                 */
//                String errorMsg = data.getExtras().getString("error_msg"); // 错误信息
//                String extraMsg = data.getExtras().getString("extra_msg"); // 错误信息
//                PromptUtils.showToast("支付返回：" + result + "  " + errorMsg + "  " + extraMsg);
//                mPayButton.setOnClickListener(new PayClickListener());
//            }
//        }
//    }

    /**
     * 根据后台返回stateCode区分状态
     *
     * @param state
     */
    private int divisiveState(int state) {
        int code = 0;
        if (state == 1) {
            code = agreeRefundGoods;
        } else {
            code = agreeRefundMoney;
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
     * 修改订单状态dialog
     */
    Dialog mUpdateOrderDialog;

    private void updateOrderDialog(final MyOrderMode myOrderMode) {
        mUpdateOrderDialog = new Dialog(this, R.style.dialog);
        mUpdateOrderDialog.setCancelable(true);
        mUpdateOrderDialog.setContentView(R.layout.delete_address_dialog);

        TextView tv_tip = (TextView) mUpdateOrderDialog.findViewById(R.id.tv_tip);
        Button bt_sure = (Button) mUpdateOrderDialog.findViewById(R.id.bt_sure);
        Button bt_cancel = (Button) mUpdateOrderDialog.findViewById(R.id.bt_cancel);

        tv_tip.setText("确认撤销退款");

        bt_sure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                updateOrder(myOrderMode);
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
