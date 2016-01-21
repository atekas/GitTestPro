package com.sensu.android.zimaogou.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;
import com.alibaba.fastjson.JSON;
import com.google.gson.Gson;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.sensu.android.zimaogou.IConstants;
import com.sensu.android.zimaogou.Mode.ReceiverAddressMode;
import com.sensu.android.zimaogou.Mode.SelectProductModel;
import com.sensu.android.zimaogou.R;
import com.sensu.android.zimaogou.ReqResponse.ExpressRuleResponse;
import com.sensu.android.zimaogou.ReqResponse.ReceiverAddressResponse;
import com.sensu.android.zimaogou.activity.mycenter.CouponActivity;
import com.sensu.android.zimaogou.activity.mycenter.ReceiverAddressActivity;
import com.sensu.android.zimaogou.adapter.VerifyOrderAdapter;
import com.sensu.android.zimaogou.external.greendao.helper.GDUserInfoHelper;
import com.sensu.android.zimaogou.external.greendao.model.UserInfo;
import com.sensu.android.zimaogou.utils.*;
import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by zhangwentao on 2015/11/19.
 */
public class VerifyOrderActivity extends BaseActivity implements View.OnClickListener {

    public static final String PRODUCT_FOR_PAY = "product_for_pay";

    //默认1 为支付宝付款 2 为微信支付
    public static final int ZFB_PAY = 1;
    public static final int WE_CHAT_PAY = 2;
    //确认订单中选择地址
    public static final int CHOOSE_ADDRESS_CODE = 100;
    public static final int CHOOSE_COUPON_CODE = 101;

    private int mPayWay;
    private ListView mListView;
    private TextView mAmountMoneyView;
    private VerifyOrderAdapter mVerifyOrderAdapter;

    private SelectProductModel mSelectProductModel;
    private ReceiverAddressMode mAddressDefault;

    private double mAmountMoney;
    private double mRateMoney;
    private double mExpressMoney;
    private String mCouponId;
    private String mCouponMoney;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.verify_order_activity);

        initViews();
    }

    private void initViews() {

        getExpressRule();

        mSelectProductModel = (SelectProductModel) getIntent().getSerializableExtra(PRODUCT_FOR_PAY);

        mAmountMoneyView = (TextView) findViewById(R.id.sum_money);

        findViewById(R.id.back).setOnClickListener(this);
        findViewById(R.id.into_address_list).setOnClickListener(this);
        findViewById(R.id.zhifubao_pay).setOnClickListener(this);
        findViewById(R.id.we_chat_pay).setOnClickListener(this);
        findViewById(R.id.coupon).setOnClickListener(this);
        findViewById(R.id.verify_order).setOnClickListener(this);

        mPayWay = ZFB_PAY;
        findViewById(R.id.alipay_select).setSelected(true);
        findViewById(R.id.we_chat_pay_select).setSelected(false);

        mListView = (ListView) findViewById(R.id.product_list);
        mListView.setFocusable(false);
        mVerifyOrderAdapter = new VerifyOrderAdapter(this);
        mListView.setAdapter(mVerifyOrderAdapter);

        mCouponId = "0";
        mCouponMoney = "0.00";

        getReceiverAddress();

        if (mSelectProductModel != null) {
            mVerifyOrderAdapter.setSelectProductModel(mSelectProductModel);
            ((TextView) findViewById(R.id.amount_money)).setText("¥ " + StringUtils.getDoubleWithTwo(mSelectProductModel.getTotalMoney()));
            mRateMoney = getAmountRate();
            ((TextView) findViewById(R.id.rate)).setText("¥ " + StringUtils.getDoubleWithTwo(mRateMoney));
            if (mRateMoney > 50.00) {
                findViewById(R.id.rate_toast).setVisibility(View.VISIBLE);
                mAmountMoney = mSelectProductModel.getTotalMoney() + getAmountRate();
            } else {
                findViewById(R.id.rate_toast).setVisibility(View.GONE);
                com.sensu.android.zimaogou.utils.TextUtils.addLineCenter(((TextView) findViewById(R.id.rate)));
                mAmountMoney = mSelectProductModel.getTotalMoney();
            }

            ((TextView) findViewById(R.id.coupon_money)).setText("-¥ " + mCouponMoney);

            mAmountMoneyView.setText("¥ " + StringUtils.getDoubleWithTwo(mAmountMoney));

            ((TextView) findViewById(R.id.express_money)).setText("¥ " + StringUtils.getDoubleWithTwo(mExpressMoney));

            if (!mSelectProductModel.getIsUseCoupon()) {
                findViewById(R.id.coupon).setEnabled(false);
                findViewById(R.id.coupon).setBackgroundColor(getResources().getColor(R.color.invalid_color));
            } else {
                findViewById(R.id.coupon).setEnabled(true);
            }
        }
    }

    private void getExpressMoney(ExpressRuleResponse mExpressRuleResponse) {
        if (mExpressRuleResponse != null) {
            for (ExpressRuleResponse.ExpressRuleData expressRuleData : mExpressRuleResponse.data) {
                if (expressRuleData.deliver_address.equals(mSelectProductModel.getDeliverAddress())) {
                    if (mSelectProductModel.getTotalMoney() > Double.parseDouble(expressRuleData.start_amount)
                            && mSelectProductModel.getTotalMoney() < Double.parseDouble(expressRuleData.end_amount)) {
                        mExpressMoney = Double.parseDouble(expressRuleData.express_amount);
                        ((TextView) findViewById(R.id.express_money)).setText("¥ " + StringUtils.getDoubleWithTwo(mExpressMoney));
                        mAmountMoney += mExpressMoney;
                        mAmountMoneyView.setText("¥ " + StringUtils.getDoubleWithTwo(mAmountMoney));
                        break;
                    }
                }
            }
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.back:
                finish();
                break;
            case R.id.into_address_list:
                PromptUtils.showToast("修改收货地址,进入地址列表页面");
                Intent intent = new Intent(this, ReceiverAddressActivity.class);
                intent.putExtra(ReceiverAddressActivity.IS_NO_EDIT, true);
                startActivityForResult(intent, CHOOSE_ADDRESS_CODE);
                break;
            case R.id.zhifubao_pay:
                PromptUtils.showToast("支付宝支付");
                findViewById(R.id.alipay_select).setSelected(true);
                findViewById(R.id.we_chat_pay_select).setSelected(false);
                mPayWay = ZFB_PAY;
                break;
            case R.id.we_chat_pay:
                PromptUtils.showToast("微信支付");
                findViewById(R.id.alipay_select).setSelected(false);
                findViewById(R.id.we_chat_pay_select).setSelected(true);
                mPayWay = WE_CHAT_PAY;
                break;
            case R.id.coupon:
                PromptUtils.showToast("进入到我的优惠券页面");
                Intent intent1 = new Intent(this, CouponActivity.class);
                intent1.putExtra(CouponActivity.TOTAL_AMOUNT, String.valueOf(mSelectProductModel.getTotalMoney()));
                startActivityForResult(intent1, CHOOSE_COUPON_CODE);
                break;
            case R.id.verify_order:
                if (TextUtils.isEmpty(getAddressJson()) || TextUtils.isEmpty(getGoodsJson())) {
                    PromptUtils.showToast("数据有误");
                    return;
                }
                createOrder();
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_OK) {
            return;
        }
        switch (requestCode) {
            case CHOOSE_ADDRESS_CODE:
                mAddressDefault = (ReceiverAddressMode) data.getSerializableExtra("address");

                ((TextView) findViewById(R.id.name)).setText(mAddressDefault.getName());
                ((TextView) findViewById(R.id.phone_num)).setText(mAddressDefault.getMobile());
                ((TextView) findViewById(R.id.address)).setText(mAddressDefault.getProvince() + mAddressDefault.getCity() + mAddressDefault.getAddress());
                break;
            case CHOOSE_COUPON_CODE:
                mCouponId = data.getStringExtra(CouponActivity.COUPON_ID);
                mCouponMoney = data.getStringExtra(CouponActivity.COUPON_AMOUNT);
                String couponName = data.getStringExtra(CouponActivity.COUPON_NAME);

                ((TextView) findViewById(R.id.coupon_name)).setText(couponName);
                ((TextView) findViewById(R.id.coupon_money)).setText("-¥ " + mCouponMoney);

                mRateMoney = getAmountRateWithCoupon(mSelectProductModel.getTotalMoney(), Double.parseDouble(mCouponMoney));
                ((TextView) findViewById(R.id.rate)).setText("¥ " + StringUtils.getDoubleWithTwo(mRateMoney));
                if (mRateMoney > 50.00) {
                    findViewById(R.id.rate_toast).setVisibility(View.VISIBLE);
                    mAmountMoney = mSelectProductModel.getTotalMoney() + mRateMoney - Double.parseDouble(mCouponMoney) + mExpressMoney;
                } else {
                    findViewById(R.id.rate_toast).setVisibility(View.GONE);
                    com.sensu.android.zimaogou.utils.TextUtils.addLineCenter(((TextView) findViewById(R.id.rate)));
                    mAmountMoney = mSelectProductModel.getTotalMoney() - Double.parseDouble(mCouponMoney) + mExpressMoney;
                }
                mAmountMoneyView.setText("¥ " + StringUtils.getDoubleWithTwo(mAmountMoney));
                break;
        }
    }

    private void createOrder() {
        UserInfo userInfo = GDUserInfoHelper.getInstance(this).getUserInfo();
        RequestParams requestParams = new RequestParams();
        requestParams.put("uid", userInfo.getUid());
        requestParams.put("amount_goods", mAmountMoney);
        requestParams.put("amount_express", "0");
        requestParams.put("amount_coupon", mCouponMoney);
        requestParams.put("coupon_id", mCouponId);
        requestParams.put("amount_tax", mRateMoney);
        requestParams.put("weight", "0");
        requestParams.put("pay_type", mPayWay);
        requestParams.put("deliver_address", mSelectProductModel.getDeliverAddress());
        requestParams.put("receiver_info", getAddressJson());
        requestParams.put("goods", getGoodsJson());
        HttpUtil.postWithSign(userInfo.getToken(), IConstants.sOrder, requestParams, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);

                if (response.optString("ret").equals("-1")) {
                    PromptUtils.showToast(response.optString("msg"));
                    return;
                }

                try {
                    String payInfo = response.getJSONObject("data").getJSONObject("pay_info").toString();
                    String orderId = response.getJSONObject("data").optString("id");
                    Intent intent = new Intent(VerifyOrderActivity.this, PayResultActivity.class);
                    intent.putExtra(PayResultActivity.ORDER_DATA, payInfo);
                    intent.putExtra(PayResultActivity.ORDER_ID, orderId);
                    startActivity(intent);
                    finish();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);
                PromptUtils.showToast("生成订单失败");
            }
        });
    }

    //未选择优惠券的税费
    private double getAmountRate() {
        double amountRate = 0;
        if (mSelectProductModel != null) {
            for (SelectProductModel.GoodsInfo goodsInfo : mSelectProductModel.getGoodsInfo()) {
                double productPrice = Double.parseDouble(goodsInfo.getPrice()) * Double.parseDouble(goodsInfo.getNum());
                double rate = productPrice * RateUtil.getRate(goodsInfo.getRate());
                amountRate += rate;
            }
        }
        return amountRate;
    }

    /**
     * 选择优惠券后新的税费
     * allMoney 商品总金额
     * coupon 优惠金额
     */

    private double getAmountRateWithCoupon(double allMoney, double coupon) {
        return RateUtil.getRateMoneyWithCoupon(allMoney, getAmountRate(), coupon);
    }

    private String getGoodsJson() {
        JSONArray jsonArray = new JSONArray();
        if (mSelectProductModel != null) {
            //(goods_id/source/spec_id/price/num)
            for (SelectProductModel.GoodsInfo goodsInfo : mSelectProductModel.getGoodsInfo()) {
                JSONObject jsonObject = new JSONObject();
                try {
                    jsonObject.put("goods_id", goodsInfo.getGoodsId());
                    jsonObject.put("source", goodsInfo.getSource());
                    jsonObject.put("spec_id", goodsInfo.getSpecId());
                    jsonObject.put("price", goodsInfo.getPrice());
                    jsonObject.put("num", goodsInfo.getNum());
                    jsonArray.put(jsonObject);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
        return jsonArray.toString();
    }

    private String getAddressJson() {
        Gson gson = new Gson();
        if (mAddressDefault != null) {
            String addressJson = gson.toJson(mAddressDefault);
            return addressJson;
        } else {
            return null;
        }
    }

    private void getExpressRule() {
        HttpUtil.get(IConstants.sExpressRule, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                ExpressRuleResponse expressRuleResponse = JSON.parseObject(response.toString(), ExpressRuleResponse.class);
                if (expressRuleResponse.getRet().equals("-1")) {
                    PromptUtils.showToast(expressRuleResponse.getMsg());
                    return;
                }
                getExpressMoney(expressRuleResponse);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);
            }
        });
    }

    /**
     * 获取收货地址
     */
    private void getReceiverAddress() {
        UserInfo userInfo = GDUserInfoHelper.getInstance(this).getUserInfo();
        RequestParams requestParams = new RequestParams();
        requestParams.put("uid", userInfo.getUid());
        HttpUtil.getWithSign(userInfo.getToken(), IConstants.sGetReceiverAddress, requestParams, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                boolean isHaveDefault = false;
                ReceiverAddressResponse receiverAddressResponse = JSON.parseObject(response.toString(), ReceiverAddressResponse.class);
                if (receiverAddressResponse.getRet().equals("-1")) {
                    PromptUtils.showToast(receiverAddressResponse.getMsg());
                    return;
                }
                if (receiverAddressResponse.data.size() != 0) {
                    for (ReceiverAddressMode receiverAddressMode : receiverAddressResponse.data) {
                        if (receiverAddressMode.getIs_default().equals("1")) {
                            isHaveDefault = true;
                            mAddressDefault = receiverAddressMode;
                            break;
                        }
                    }

                    if (!isHaveDefault) {
                        mAddressDefault = receiverAddressResponse.data.get(0);
                    }
                }

                if (mAddressDefault != null) {
                    ((TextView) findViewById(R.id.name)).setText(mAddressDefault.getName());
                    ((TextView) findViewById(R.id.phone_num)).setText(mAddressDefault.getMobile());
                    ((TextView) findViewById(R.id.address)).setText(mAddressDefault.getProvince() + mAddressDefault.getCity() + mAddressDefault.getAddress());
                } else {
                    ((TextView) findViewById(R.id.name)).setText("请选择收货地址");
                    findViewById(R.id.phone_num).setVisibility(View.INVISIBLE);
                    findViewById(R.id.address).setVisibility(View.INVISIBLE);
                }

            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);
                ((TextView) findViewById(R.id.name)).setText("请选择收货地址");
                findViewById(R.id.phone_num).setVisibility(View.INVISIBLE);
                findViewById(R.id.address).setVisibility(View.INVISIBLE);
            }
        });
    }
}
