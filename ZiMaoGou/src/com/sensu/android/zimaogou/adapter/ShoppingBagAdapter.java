package com.sensu.android.zimaogou.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import com.sensu.android.zimaogou.R;

/**
 * Created by zhangwentao on 2015/12/2.
 */
public class ShoppingBagAdapter extends SimpleBaseAdapter {

    //true  为编辑状态  false为未编辑状态
    public boolean mIsEditProduct;
    //所有订单是否全选
    public boolean mShoppingBagIsAllSelect;

    public boolean mChildIsAllSelect;

    private GoodsOrderAdapter mGoodsOrderAdapter;
    private OnOrderChangeListener mOnOrderChangeListener;

    public ShoppingBagAdapter(Context context) {
        super(context);
        mGoodsOrderAdapter = new GoodsOrderAdapter(context);
    }

    public void setOnOrderChangeListener(OnOrderChangeListener onOrderChangeListener) {
        mOnOrderChangeListener = onOrderChangeListener;
    }

    @Override
    public void notifyDataSetChanged() {
        super.notifyDataSetChanged();
        mGoodsOrderAdapter.notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return 2;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        final ShoppingBagViewHolder shoppingBagViewHolder;
        if (view == null) {
            view = LayoutInflater.from(mContext).inflate(R.layout.shopping_list_item, null);
            shoppingBagViewHolder = new ShoppingBagViewHolder();
            shoppingBagViewHolder.img_choose = (ImageView) view.findViewById(R.id.img_choose);
            shoppingBagViewHolder.tv_warehouseName = (TextView) view.findViewById(R.id.tv_warehouseName);
            shoppingBagViewHolder.lv_products = (ListView) view.findViewById(R.id.lv_products);
            shoppingBagViewHolder.lv_products.setAdapter(mGoodsOrderAdapter);
            view.setTag(shoppingBagViewHolder);
        } else {
            shoppingBagViewHolder = (ShoppingBagViewHolder) view.getTag();
        }
        if (mShoppingBagIsAllSelect) {
            shoppingBagViewHolder.img_choose.setSelected(true);
            mChildIsAllSelect = true;
            mGoodsOrderAdapter.notifyDataSetChanged();
        } else {
            shoppingBagViewHolder.img_choose.setSelected(false);
            mChildIsAllSelect = false;
            mGoodsOrderAdapter.notifyDataSetChanged();
        }

        //todo mGoodsOrderAdapter 只刷新数据

        return view;
    }

    class ShoppingBagViewHolder {
        public ImageView img_choose;
        public TextView tv_warehouseName;
        public ListView lv_products;
    }

    public class GoodsOrderAdapter extends SimpleBaseAdapter {

        public GoodsOrderAdapter(Context context) {
            super(context);
        }

        @Override
        public int getCount() {
            return 3;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            GoodsOrderViewHolder goodsOrderViewHolder;
            if (view == null) {
                view = LayoutInflater.from(mContext).inflate(R.layout.shopping_list_child_item, null);
                goodsOrderViewHolder = new GoodsOrderViewHolder();
                goodsOrderViewHolder.mGoodsIsSelect = (ImageView) view.findViewById(R.id.img_choose);
                goodsOrderViewHolder.mGoodPic = (ImageView) view.findViewById(R.id.img_pro);
                goodsOrderViewHolder.mGoodName = (TextView) view.findViewById(R.id.tv_productName);
                goodsOrderViewHolder.mGoodPrice = (TextView) view.findViewById(R.id.tv_productPrice);

                goodsOrderViewHolder.mGoodType = (TextView) view.findViewById(R.id.product_type);
                goodsOrderViewHolder.mGoodNum = (TextView) view.findViewById(R.id.tv_productNum);

                goodsOrderViewHolder.mDecrease = (Button) view.findViewById(R.id.bt_subtract);
                goodsOrderViewHolder.mAdd = (Button) view.findViewById(R.id.bt_add);
                goodsOrderViewHolder.mEditGoodsNum = (EditText) view.findViewById(R.id.et_productNum);

                goodsOrderViewHolder.mShowTyeLayout = (RelativeLayout) view.findViewById(R.id.rl_showType);
                goodsOrderViewHolder.mEditNumLayout = (LinearLayout) view.findViewById(R.id.ll_editNum);
                view.setTag(goodsOrderViewHolder);
            } else {
                goodsOrderViewHolder = (GoodsOrderViewHolder) view.getTag();
            }

            if (mIsEditProduct) {
                goodsOrderViewHolder.mShowTyeLayout.setVisibility(View.GONE);
                goodsOrderViewHolder.mEditNumLayout.setVisibility(View.VISIBLE);
            } else {
                goodsOrderViewHolder.mShowTyeLayout.setVisibility(View.VISIBLE);
                goodsOrderViewHolder.mEditNumLayout.setVisibility(View.GONE);
            }

            if (mChildIsAllSelect) {
                goodsOrderViewHolder.mGoodsIsSelect.setSelected(true);
            } else {
                goodsOrderViewHolder.mGoodsIsSelect.setSelected(false);
            }

            return view;
        }
    }

    class GoodsOrderViewHolder {
        ImageView mGoodsIsSelect;
        ImageView mGoodPic;

        TextView mGoodName;
        TextView mGoodPrice;
        TextView mGoodType;
        TextView mGoodNum;

        Button mDecrease;
        Button mAdd;
        EditText mEditGoodsNum;

        RelativeLayout mShowTyeLayout;
        LinearLayout mEditNumLayout;
    }

    public interface OnOrderChangeListener {
        public void isAllSelect(boolean isAllSelect);
    }
}
