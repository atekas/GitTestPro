package com.sensu.android.zimaogou.activity;

import android.app.Dialog;
import android.content.ComponentName;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.sensu.android.zimaogou.IConstants;
import com.sensu.android.zimaogou.R;
import com.sensu.android.zimaogou.activity.mycenter.OrderDetailActivity;
import com.sensu.android.zimaogou.external.greendao.helper.GDUserInfoHelper;
import com.sensu.android.zimaogou.external.greendao.model.UserInfo;
import com.sensu.android.zimaogou.utils.HttpUtil;
import com.sensu.android.zimaogou.utils.PromptUtils;
import org.apache.http.Header;
import org.json.JSONObject;

/**
 * Created by zhangwentao on 2016/1/4.
 */
public class PayResultActivity extends BaseActivity implements View.OnClickListener {

    public static final String ORDER_DATA = "order_data";
    public static final String ORDER_ID = "order_id";
    public static final int PAYMENT_REQUEST_CODE = 1000;

    private String mPayInfo;
    private String mOrderId;
    private UserInfo mUserInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pay_result_activity);

        initViews();
    }

    private void initViews() {
        mUserInfo = GDUserInfoHelper.getInstance(this).getUserInfo();
        findViewById(R.id.back).setOnClickListener(this);
        findViewById(R.id.check_order).setOnClickListener(this);
        findViewById(R.id.cancel_order).setOnClickListener(this);
        findViewById(R.id.pay_again).setOnClickListener(this);

        mPayInfo = getIntent().getStringExtra(ORDER_DATA);
        mOrderId = getIntent().getStringExtra(ORDER_ID);
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

//                PromptUtils.showToast("支付返回：" + result + "  " + errorMsg + "  " + extraMsg);
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
                if (mOrderId == null) {
                    return;
                }
                Intent intent1 = new Intent(this, OrderDetailActivity.class);
                intent1.putExtra("id", mOrderId);
                intent1.putExtra("state", 1);
                startActivity(intent1);
                break;
            case R.id.cancel_order:
//                finish();
                showDialog();
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

    private Dialog mDialog;

    private void showDialog() {
        mDialog = new Dialog(this, R.style.dialog);
        mDialog.setCancelable(true);
        mDialog.setContentView(R.layout.delete_address_dialog);

        ((TextView) mDialog.findViewById(R.id.tv_tip)).setText("确认取消支付");
        mDialog.findViewById(R.id.bt_sure).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        mDialog.show();
    }

    private void cancelOrder() {
        RequestParams requestParams = new RequestParams();
        requestParams.put("uid", mUserInfo.getUid());
        requestParams.put("id", mOrderId);
        requestParams.put("state", "1");
        HttpUtil.postWithSign(mUserInfo.getToken(), IConstants.sOrder + "/" + mOrderId, requestParams, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                if (response.optString("ret").equals("0")){
                     PromptUtils.showToast("取消订单成功");
                    if (mDialog != null) {
                        mDialog.dismiss();
                    }
                    finish();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);
            }
        });
    }
}
