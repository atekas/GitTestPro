package com.sensu.android.zimaogou.activity.mycenter;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import com.alibaba.fastjson.JSON;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.sensu.android.zimaogou.IConstants;
import com.sensu.android.zimaogou.R;
import com.sensu.android.zimaogou.ReqResponse.CouponResponse;
import com.sensu.android.zimaogou.activity.BaseActivity;
import com.sensu.android.zimaogou.adapter.CouponInvalidListAdapter;
import com.sensu.android.zimaogou.adapter.CouponValidListAdapter;
import com.sensu.android.zimaogou.external.greendao.helper.GDUserInfoHelper;
import com.sensu.android.zimaogou.external.greendao.model.UserInfo;
import com.sensu.android.zimaogou.utils.HttpUtil;
import com.sensu.android.zimaogou.utils.UiUtils;
import org.apache.http.Header;
import org.json.JSONObject;

import java.util.List;

/**
 * 优惠券
 * Created by qi.yang on 2015/11/19.
 */
public class CouponActivity extends BaseActivity implements AdapterView.OnItemClickListener {

    public static final String TOTAL_AMOUNT = "total_amount";

    public static final String COUPON_ID = "coupon_id";
    public static final String COUPON_AMOUNT = "coupon_amount";
    public static final String COUPON_NAME = "coupon_name";

    ListView mValidListView, mInvalidListView;
    ImageView mBackImageView;

    private String mTotalAmount;

    private CouponValidListAdapter mCouponValidListAdapter;
    private CouponInvalidListAdapter mCouponInvalidListAdapter;

    private List<CouponResponse.Coupon> mCanUseCouponList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.coupon_activity);
        initView();
    }

    private void initView() {
        mTotalAmount = getIntent().getStringExtra(TOTAL_AMOUNT);
        if (mTotalAmount == null) {
            mTotalAmount = "0";
        }
        getCoupon();

        mValidListView = (ListView) findViewById(R.id.lv_valid);
        mInvalidListView = (ListView) findViewById(R.id.lv_invalid);
        mBackImageView = (ImageView) findViewById(R.id.back);

        if (!mTotalAmount.equals("0")) {
            mInvalidListView.setVisibility(View.GONE);
            findViewById(R.id.text_layout).setVisibility(View.GONE);
        } else {
            mInvalidListView.setVisibility(View.VISIBLE);
            findViewById(R.id.text_layout).setVisibility(View.VISIBLE);
        }

        mCouponValidListAdapter = new CouponValidListAdapter(this);
        mCouponInvalidListAdapter = new CouponInvalidListAdapter(this);
        mValidListView.setDivider(null);
        mValidListView.setAdapter(mCouponValidListAdapter);
        mInvalidListView.setDivider(null);
        mInvalidListView.setAdapter(mCouponInvalidListAdapter);

        UiUtils.setListViewHeightBasedOnChilds(mValidListView);
        UiUtils.setListViewHeightBasedOnChilds(mInvalidListView);
        mBackImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        mValidListView.setOnItemClickListener(this);
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        CouponResponse.Coupon coupon = mCanUseCouponList.get(i);
        Intent intent = new Intent();
        intent.putExtra(COUPON_ID, coupon.id);
        intent.putExtra(COUPON_AMOUNT, coupon.amount);
        intent.putExtra(COUPON_NAME, coupon.name);
        setResult(RESULT_OK, intent);
        finish();
    }

    /**
     * 激活优惠券
     *
     * @param v
     */
    public void InvokeCouponClick(View v) {
        LoginOutDialog();
    }

    Dialog mInvokeCouponDialog;

    private void LoginOutDialog() {
        mInvokeCouponDialog = new Dialog(this, R.style.dialog);
        mInvokeCouponDialog.setCancelable(true);
        mInvokeCouponDialog.setContentView(R.layout.invoke_coupon_dialog);

        Button bt_sure = (Button) mInvokeCouponDialog.findViewById(R.id.bt_sure);
        Button bt_cancel = (Button) mInvokeCouponDialog.findViewById(R.id.bt_cancel);

        bt_sure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                mInvokeCouponDialog.dismiss();
            }
        });
        bt_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mInvokeCouponDialog.dismiss();
            }
        });
        mInvokeCouponDialog.show();
    }

    private void getCoupon() {
        UserInfo userInfo = GDUserInfoHelper.getInstance(this).getUserInfo();
        RequestParams requestParams = new RequestParams();
        requestParams.put("uid", userInfo.getUid());
        requestParams.put("total_amount", mTotalAmount);

        HttpUtil.getWithSign(userInfo.getToken(), IConstants.sMyCoupon, requestParams, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                CouponResponse couponResponse = JSON.parseObject(response.toString(), CouponResponse.class);
                couponResponse.splitData();
                mCouponValidListAdapter.setCouponData(couponResponse.mNoUseCouponList);
                mCouponInvalidListAdapter.setCouponData(couponResponse.mCannotUseCouponList);
                mCanUseCouponList = couponResponse.mNoUseCouponList;
                UiUtils.setListViewHeightBasedOnChilds(mValidListView);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);
            }
        });
    }

}
