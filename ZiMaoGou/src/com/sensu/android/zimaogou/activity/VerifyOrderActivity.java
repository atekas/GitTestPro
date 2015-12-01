package com.sensu.android.zimaogou.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import com.sensu.android.zimaogou.R;
import com.sensu.android.zimaogou.activity.mycenter.CouponActivity;
import com.sensu.android.zimaogou.activity.mycenter.ReceiverAddressActivity;
import com.sensu.android.zimaogou.adapter.VerifyOrderAdapter;
import com.sensu.android.zimaogou.utils.PromptUtils;

/**
 * Created by zhangwentao on 2015/11/19.
 */
public class VerifyOrderActivity extends BaseActivity implements View.OnClickListener {

    //默认0 为支付宝付款 1 为微信支付
    public static final int ZFB_PAY = 0;
    public static final int WE_CHAT_PAY = 1;
    //确认订单中选择地址
    public static final int CHOOSE_ADDRESS_CODE = 100;
    public static final int CHOOSE_COUPON_CODE = 101;

    private int mPayWay;
    private ListView mListView;
    private VerifyOrderAdapter mVerifyOrderAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.verify_order_activity);

        initViews();
    }

    private void initViews() {
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
                startActivityForResult(new Intent(this, CouponActivity.class), CHOOSE_COUPON_CODE);
                break;
            case R.id.verify_order:
                if (mPayWay == ZFB_PAY) {
                    PromptUtils.showToast("支付宝支付");
                } else if (mPayWay == WE_CHAT_PAY) {
                    PromptUtils.showToast("微信支付");
                }
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }
}
