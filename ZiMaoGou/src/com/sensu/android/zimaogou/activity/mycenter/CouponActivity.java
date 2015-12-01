package com.sensu.android.zimaogou.activity.mycenter;

import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import com.sensu.android.zimaogou.R;
import com.sensu.android.zimaogou.activity.BaseActivity;
import com.sensu.android.zimaogou.adapter.CouponInvalidListAdapter;
import com.sensu.android.zimaogou.adapter.CouponValidListAdapter;
import com.sensu.android.zimaogou.utils.UiUtils;

/**
 * 优惠券
 * Created by qi.yang on 2015/11/19.
 */
public class CouponActivity extends BaseActivity implements AdapterView.OnItemClickListener {

    ListView mValidListView,mInvalidListView;
    ImageView mBackImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.coupon_activity);
        initView();
    }

    private void initView(){
        mValidListView = (ListView) findViewById(R.id.lv_valid);
        mInvalidListView = (ListView) findViewById(R.id.lv_invalid);
        mBackImageView = (ImageView) findViewById(R.id.back);
        mValidListView.setDivider(null);
        mValidListView.setAdapter(new CouponValidListAdapter(this));
        mInvalidListView.setDivider(null);
        mInvalidListView.setAdapter(new CouponInvalidListAdapter(this));
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
        setResult(RESULT_OK);
        finish();
    }

    /**
     * 激活优惠券
     * @param v
     */
    public void InvokeCouponClick(View v){
        LoginOutDialog();
    }

    Dialog mInvokeCouponDialog;
    private void LoginOutDialog(){
        mInvokeCouponDialog = new Dialog(this,R.style.dialog);
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
}
