package com.sensu.android.zimaogou.activity.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import com.sensu.android.zimaogou.R;
import com.sensu.android.zimaogou.activity.VerifyOrderActivity;
import com.sensu.android.zimaogou.adapter.ShoppingBagAdapter;
import com.sensu.android.zimaogou.widget.OnRefreshListener;
import com.sensu.android.zimaogou.widget.RefreshListView;

/**
 * Created by zhangwentao on 2015/11/10.
 */
public class ShoppingBagFragment extends BaseFragment implements View.OnClickListener {

    private RefreshListView mGoodsListView;
    private ShoppingBagAdapter mShoppingBagAdapter;

    private TextView mTitleEdit;
    private TextView mSubmit;
    private LinearLayout mTotalLayout;
    private TextView mTotalMoney;
    private ImageView mIsAllSelectView;

    private boolean mIsEditProduct;
    private boolean mIsAllSelect = true;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.shopping_bag_fragment, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (hidden) {

        } else {

        }
    }

    @Override
    protected void initView() {
        mTitleEdit = (TextView) mParentActivity.findViewById(R.id.goods_edit);
        mSubmit = (TextView) mParentActivity.findViewById(R.id.bt_submit);
        mTotalLayout = (LinearLayout) mParentActivity.findViewById(R.id.ll_bottom_center);

        mTotalMoney = (TextView) mParentActivity.findViewById(R.id.total_money);
        mIsAllSelectView = (ImageView) mParentActivity.findViewById(R.id.is_all_select);
        mGoodsListView = (RefreshListView) mParentActivity.findViewById(R.id.bag_goods_list);
        mShoppingBagAdapter = new ShoppingBagAdapter(mParentActivity);
        mGoodsListView.setAdapter(mShoppingBagAdapter);
        mGoodsListView.setOnRefreshListener(mOnRefreshListener);

        mTitleEdit.setOnClickListener(this);
        mSubmit.setOnClickListener(this);
        mIsAllSelectView.setOnClickListener(this);
        //默认全选
        mIsAllSelectView.setSelected(true);
        mShoppingBagAdapter.mShoppingBagIsAllSelect = true;
        mShoppingBagAdapter.notifyDataSetChanged();
    }


    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onResume() {
        super.onResume();
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
                    startActivity(new Intent(mParentActivity, VerifyOrderActivity.class));
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
