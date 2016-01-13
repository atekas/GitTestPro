package com.sensu.android.zimaogou.activity;

import android.content.ComponentName;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import com.sensu.android.zimaogou.IConstants;
import com.sensu.android.zimaogou.R;
import com.sensu.android.zimaogou.utils.PromptUtils;

/**
 * Created by zhangwentao on 2016/1/4.
 */
public class PayResultActivity extends BaseActivity implements View.OnClickListener {

    public static final String ORDER_DATA = "order_data";
    public static final int PAYMENT_REQUEST_CODE = 1000;

    private String mPayInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pay_result_activity);

        initViews();
    }

    private void initViews() {

        findViewById(R.id.back).setOnClickListener(this);
        findViewById(R.id.check_order).setOnClickListener(this);
        findViewById(R.id.cancel_order).setOnClickListener(this);
        findViewById(R.id.pay_again).setOnClickListener(this);

        mPayInfo = getIntent().getStringExtra(ORDER_DATA);
        if (mPayInfo != null) {
            Intent intent = new Intent();
            String packageName = getPackageName();
            ComponentName componentName = new ComponentName(packageName, packageName + ".wxapi.WXPayEntryActivity");
            intent.setComponent(componentName);
            intent.putExtra(IConstants.EXTRA_CHARGE, mPayInfo);
            startActivityForResult(intent, PAYMENT_REQUEST_CODE);
        } else {
            ((TextView) findViewById(R.id.pay_result_title)).setText("支付失败");
            findViewById(R.id.pay_failed_layout).setVisibility(View.VISIBLE);
            findViewById(R.id.pay_success_layout).setVisibility(View.GONE);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_OK) {
            ((TextView) findViewById(R.id.pay_result_title)).setText("支付失败");
            findViewById(R.id.pay_failed_layout).setVisibility(View.VISIBLE);
            findViewById(R.id.pay_success_layout).setVisibility(View.GONE);
            return;
        }
        switch (requestCode) {
            case PAYMENT_REQUEST_CODE:
                String result = data.getExtras().getString("pay_result");
                /* 处理返回值
                 * "success" - payment succeed
                 * "fail"    - payment failed
                 * "cancel"  - user canceld
                 * "invalid" - payment plugin not installed
                 */
                String errorMsg = data.getExtras().getString("error_msg"); // 错误信息
                String extraMsg = data.getExtras().getString("extra_msg"); // 错误信息

                if (result.equals("success")) {
                    ((TextView) findViewById(R.id.pay_result_title)).setText("支付成功");
                    findViewById(R.id.pay_failed_layout).setVisibility(View.GONE);
                    findViewById(R.id.pay_success_layout).setVisibility(View.VISIBLE);
                } else {
                    ((TextView) findViewById(R.id.pay_result_title)).setText("支付失败");
                    findViewById(R.id.pay_failed_layout).setVisibility(View.VISIBLE);
                    findViewById(R.id.pay_success_layout).setVisibility(View.GONE);
                }

                PromptUtils.showToast("支付返回：" + result + "  " + errorMsg + "  " + extraMsg);
                break;
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.back:
                finish();
                break;
            case R.id.check_order:
                break;
            case R.id.cancel_order:
                finish();
                break;
            case R.id.pay_again:
                if (mPayInfo != null) {
                    Intent intent = new Intent();
                    String packageName = getPackageName();
                    ComponentName componentName = new ComponentName(packageName, packageName + ".wxapi.WXPayEntryActivity");
                    intent.setComponent(componentName);
                    intent.putExtra(IConstants.EXTRA_CHARGE, mPayInfo);
                    startActivityForResult(intent, PAYMENT_REQUEST_CODE);
                } else {
                    ((TextView) findViewById(R.id.pay_result_title)).setText("支付失败");
                    findViewById(R.id.pay_failed_layout).setVisibility(View.VISIBLE);
                    findViewById(R.id.pay_success_layout).setVisibility(View.GONE);
                }
                break;
        }
    }
}
