package com.sensu.android.zimaogou.activity.mycenter;

import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.alibaba.fastjson.JSON;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.sensu.android.zimaogou.IConstants;
import com.sensu.android.zimaogou.Mode.MyOrderMode;
import com.sensu.android.zimaogou.R;
import com.sensu.android.zimaogou.activity.BaseActivity;
import com.sensu.android.zimaogou.external.greendao.helper.GDUserInfoHelper;
import com.sensu.android.zimaogou.external.greendao.model.UserInfo;
import com.sensu.android.zimaogou.utils.HttpUtil;
import com.sensu.android.zimaogou.utils.LogUtils;
import com.sensu.android.zimaogou.utils.PromptUtils;
import org.apache.http.Header;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by qi.yang on 2016/1/14.
 */
public class RefundOrderDetailActivity extends BaseActivity {
    TextView mRefundState, mRefundMoney, mRefundTime, mRefundReason, mRefundInstruction, mRefundNo, mApplyTime, mApplyMoney;
    UserInfo userInfo;
    String id = "";
    MyOrderMode myOrderMode = new MyOrderMode();
    RelativeLayout mRefundRealRelativeLayout,mRefundTimeRelativeLayout;
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

        if (getIntent().getExtras() != null) {
            id = getIntent().getExtras().getString("id");
        }
        initView();
        getRefundDetail();
    }

    private void initView() {
        mRefundRealRelativeLayout = (RelativeLayout) findViewById(R.id.rl_refundReal);
        mRefundTimeRelativeLayout = (RelativeLayout) findViewById(R.id.rl_refundTime);
        mRefundState = (TextView) findViewById(R.id.tv_refundState);
        mRefundMoney = (TextView) findViewById(R.id.tv_refundMoney);
        mRefundTime = (TextView) findViewById(R.id.tv_refundTime);
        mRefundReason = (TextView) findViewById(R.id.tv_refundReason);
        mRefundInstruction = (TextView) findViewById(R.id.tv_instructions);
        mRefundNo = (TextView) findViewById(R.id.tv_refundNo);
        mApplyTime = (TextView) findViewById(R.id.tv_applyTime);
        mApplyMoney = (TextView) findViewById(R.id.tv_applyMoney);
    }

    private void getRefundDetail() {
        RequestParams requestParams = new RequestParams();
        requestParams.put("uid", userInfo.getUid());
        requestParams.put("id", id);
        requestParams.put("state", "3");
        HttpUtil.getWithSign(userInfo.getToken(), IConstants.sOrder + "/" + id, requestParams, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                LogUtils.d("订单详情返回：", response.toString());
                if (response.optString("ret").equals("-1")) {
                    PromptUtils.showToast(response.optString("msg"));
                    return;
                }
                myOrderMode = JSON.parseObject(response.optJSONObject("data").toString(), MyOrderMode.class);
                updateState_cn(myOrderMode);
                mRefundState.setText(myOrderMode.getState_cn());
                mApplyMoney.setText(myOrderMode.getAmount_apply());
                mRefundMoney.setText(myOrderMode.getAmount_real());
                mRefundTime.setText(myOrderMode.getReturned_at());
                mRefundReason.setText(myOrderMode.getReturn_reason());
                mRefundInstruction.setText(myOrderMode.getReturn_content());
                mRefundNo.setText(myOrderMode.getReturn_no());
                mApplyTime.setText(myOrderMode.getCreated_at());
                double refundRealMoney = Double.parseDouble(myOrderMode.getAmount_real());
                if(refundRealMoney == 0){
                    mRefundTimeRelativeLayout.setVisibility(View.GONE);
                    mRefundRealRelativeLayout.setVisibility(View.GONE);
                }else{
                    mRefundTimeRelativeLayout.setVisibility(View.VISIBLE);
                    mRefundRealRelativeLayout.setVisibility(View.VISIBLE);
                }

            }
        });
    }

    /**
     * 根据state修改订单状态显示
     *
     * @param myOrderMode
     */
    private void updateState_cn(MyOrderMode myOrderMode) {

        switch (myOrderMode.getState()) {
            case 0:
            case 2:
                myOrderMode.setState_cn("等待审核");
                break;
            case 11:
                myOrderMode.setState_cn("审核不通过");
                break;
            case 1:
                myOrderMode.setState_cn("填写物流");
                break;

            case 12:
                myOrderMode.setState_cn("用户撤销退款");
                break;
            case 13:
                myOrderMode.setState_cn("不同意退款");
                break;
            case 3:
            case 6:
                myOrderMode.setState_cn("退款完成");
                break;
        }
    }
}
