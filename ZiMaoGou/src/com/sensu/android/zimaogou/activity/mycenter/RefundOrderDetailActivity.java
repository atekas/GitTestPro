package com.sensu.android.zimaogou.activity.mycenter;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import com.sensu.android.zimaogou.R;
import com.sensu.android.zimaogou.activity.BaseActivity;
import com.sensu.android.zimaogou.external.greendao.helper.GDUserInfoHelper;
import com.sensu.android.zimaogou.external.greendao.model.UserInfo;

/**
 * Created by qi.yang on 2016/1/14.
 */
public class RefundOrderDetailActivity extends BaseActivity {
    TextView mRefundState,mRefundMoney,mRefundTime,mRefundReason,mRefundInstruction,mRefundNo,mApplyTime;
    UserInfo userInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.refund_order_detail_activity);
        userInfo = GDUserInfoHelper.getInstance(this).getUserInfo();
        findViewById(R.id.back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        initView();
    }
    private void initView(){
        mRefundState = (TextView) findViewById(R.id.tv_refundState);
        mRefundMoney = (TextView) findViewById(R.id.tv_refundMoney);
        mRefundTime  = (TextView) findViewById(R.id.tv_refundTime);
        mRefundReason = (TextView) findViewById(R.id.tv_refundReason);
        mRefundInstruction = (TextView) findViewById(R.id.tv_instructions);
        mRefundNo = (TextView) findViewById(R.id.tv_refundNo);
        mApplyTime = (TextView) findViewById(R.id.tv_applyTime);

    }
}
