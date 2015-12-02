package com.sensu.android.zimaogou.activity.video;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.sensu.android.zimaogou.R;
import com.sensu.android.zimaogou.activity.BaseActivity;
import com.sensu.android.zimaogou.activity.VerifyOrderActivity;
import com.sensu.android.zimaogou.adapter.ShoppingBagAdapter;
import com.sensu.android.zimaogou.widget.OnRefreshListener;
import com.sensu.android.zimaogou.widget.RefreshListView;

/**
 * Created by qi.yang on 2015/12/2.
 */
public class ProductShopCarActivity extends BaseActivity implements View.OnClickListener{
    private RefreshListView mGoodsListView;
    private ShoppingBagAdapter mShoppingBagAdapter;

    private TextView mTitleEdit;
    private TextView mSubmit;
    private LinearLayout mTotalLayout;
    private TextView mTotalMoney;
    private ImageView mIsAllSelectView,mBackImageView;

    private boolean mIsEditProduct;
    private boolean mIsAllSelect = true;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.product_shop_car_activity);
        initView();
    }
    private void initView(){
        mTitleEdit = (TextView) findViewById(R.id.goods_edit);
        mSubmit = (TextView) findViewById(R.id.bt_submit);
        mTotalLayout = (LinearLayout) findViewById(R.id.ll_bottom_center);
        mTotalMoney = (TextView) findViewById(R.id.total_money);
        mIsAllSelectView = (ImageView) findViewById(R.id.is_all_select);
        mGoodsListView = (RefreshListView) findViewById(R.id.bag_goods_list);
        mBackImageView = (ImageView) findViewById(R.id.back);

        mShoppingBagAdapter = new ShoppingBagAdapter(this);
        mGoodsListView.setAdapter(mShoppingBagAdapter);
        mGoodsListView.setOnRefreshListener(mOnRefreshListener);

        mBackImageView.setOnClickListener(this);
        mTitleEdit.setOnClickListener(this);
        mSubmit.setOnClickListener(this);
        mIsAllSelectView.setOnClickListener(this);
        //默认全选
        mIsAllSelectView.setSelected(true);
        mShoppingBagAdapter.mShoppingBagIsAllSelect = true;
        mShoppingBagAdapter.notifyDataSetChanged();

    }
    private Handler mHandler = new Handler();
    private OnRefreshListener mOnRefreshListener = new OnRefreshListener() {
        @Override
        public void onDownPullRefresh() {
            //下拉刷新接口
            mHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    mGoodsListView.hideHeaderView();
                }
            }, 2000);
        }

        @Override
        public void onLoadingMore() {
            //上拉加载接口
            mHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    mGoodsListView.hideFooterView();
                }
            }, 2000);
        }
    };
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.goods_edit:
                if (mIsEditProduct) {
                    statueIsEdit();
                } else {
                    statueIsNotEdit();
                }
                break;
            case R.id.bt_submit:
                if (mIsEditProduct) {
                    //todo 删除选中的商品
                } else {
                    //todo 提交选中商品订单  进入到确认订单页面
                    startActivity(new Intent(this, VerifyOrderActivity.class));
                }
                break;
            case R.id.is_all_select:
                //todo 所有商品全部选择
                if (mIsAllSelect) {
                    mIsAllSelect = false;
                    mIsAllSelectView.setSelected(false);
                    mShoppingBagAdapter.mShoppingBagIsAllSelect = false;
                    mShoppingBagAdapter.notifyDataSetChanged();
                } else {
                    mIsAllSelect = true;
                    mIsAllSelectView.setSelected(true);
                    mShoppingBagAdapter.mShoppingBagIsAllSelect = true;
                    mShoppingBagAdapter.notifyDataSetChanged();
                }
            case R.id.back:
                onBackPressed();
                break;
        }
    }

    private void statueIsEdit() {
        mIsEditProduct = false;
        mTitleEdit.setText("完成");
        mSubmit.setText("删除");
        mTotalLayout.setVisibility(View.GONE);
        mIsAllSelectView.setSelected(false);
        mShoppingBagAdapter.mIsEditProduct = true;
        mShoppingBagAdapter.mShoppingBagIsAllSelect = false;
        mShoppingBagAdapter.notifyDataSetChanged();
    }

    private void statueIsNotEdit() {
        mIsEditProduct = true;
        mTitleEdit.setText("编辑");
        mSubmit.setText("去结算");
        mTotalLayout.setVisibility(View.VISIBLE);
        mShoppingBagAdapter.mIsEditProduct = false;
        mShoppingBagAdapter.notifyDataSetChanged();
        //todo 总金额塞入最新金额
    }
}
