package com.sensu.android.zimaogou.activity;

import android.content.ComponentName;
import android.content.Intent;
import android.os.Bundle;
import com.sensu.android.zimaogou.R;

/**
 * Created by zhangwentao on 2016/1/4.
 */
public class PayResultActivity extends BaseActivity {

    public static final String ORDER_DATA = "order_data";

    private String mPayInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pay_result_activity);

        initViews();
    }

    private void initViews() {
        mPayInfo = getIntent().getStringExtra(ORDER_DATA);
        if (mPayInfo != null) {
            Intent intent = new Intent();
            String packageName = getPackageName();
            ComponentName componentName = new ComponentName(packageName, packageName + ".wxapi.WXPayEntryActivity");
            intent.setComponent(componentName);
//            intent.putExtra(EXTRA_CHARGE, charge);
//            startActivityForResult(intent, REQUEST_CODE_PAYMENT);
        }
    }
}
