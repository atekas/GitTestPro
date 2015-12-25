package com.sensu.android.zimaogou.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import com.sensu.android.zimaogou.R;
import com.sensu.android.zimaogou.ReqResponse.CartDataResponse;
import com.sensu.android.zimaogou.view.CartLinearLayout;

/**
 * Created by zhangwentao on 2015/12/2.
 */
public class ShoppingBagAdapter extends SimpleBaseAdapter {

    private CartDataResponse mCartDataResponse;

    //true  为编辑状态  false为未编辑状态
    public boolean mIsEditProduct;
    public ListView mListView;

    public ShoppingBagAdapter(Context context, ListView listView) {
        super(context);
        mListView = listView;
    }

    public void setCartDataGroup(CartDataResponse cartDataResponse) {
        mCartDataResponse = cartDataResponse;
        notifyDataSetChanged();
    }

    public void setIsEditProduct(boolean isEditProduct) {
        mIsEditProduct = isEditProduct;
        notifyDataSetChanged();
    }

    public void isAllSelect(int i) {
        CartDataResponse.CartDataGroup cartDataGroup = mCartDataResponse.data.get(i);
        for (CartDataResponse.CartDataChild cartDataChild : cartDataGroup.data) {
            if (cartDataChild.getIsSelect()) {
                cartDataGroup.setIsAllSelect(true);
            } else {
                cartDataGroup.setIsAllSelect(false);
                break;
            }
        }
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return mCartDataResponse == null ? 0 : mCartDataResponse.data.size();
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        final ShoppingBagViewHolder shoppingBagViewHolder;
        if (view == null) {
            view = LayoutInflater.from(mContext).inflate(R.layout.shopping_list_item, null);
            shoppingBagViewHolder = new ShoppingBagViewHolder();
            shoppingBagViewHolder.img_choose = (ImageView) view.findViewById(R.id.img_choose);
            shoppingBagViewHolder.tv_warehouseName = (TextView) view.findViewById(R.id.tv_warehouseName);
            shoppingBagViewHolder.product_child = (CartLinearLayout) view.findViewById(R.id.product_child);
            view.setTag(shoppingBagViewHolder);
        } else {
            shoppingBagViewHolder = (ShoppingBagViewHolder) view.getTag();
        }
        //todo mGoodsOrderAdapter 只刷新数据
        final CartDataResponse.CartDataGroup cartDataGroup = mCartDataResponse.data.get(i);

        shoppingBagViewHolder.tv_warehouseName.setText(cartDataGroup.deliver_address);

        if (cartDataGroup.getIsAllSelect()) {
            shoppingBagViewHolder.img_choose.setSelected(true);
        } else {
            shoppingBagViewHolder.img_choose.setSelected(false);
        }

        shoppingBagViewHolder.product_child.setCartGroup(cartDataGroup, mIsEditProduct, mListView, this, i);

        shoppingBagViewHolder.img_choose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (cartDataGroup.getIsAllSelect()) {
                    cartDataGroup.setIsAllSelect(false);
                    for (CartDataResponse.CartDataChild cartDataChild : cartDataGroup.data) {
                        cartDataChild.setIsSelect(false);
                    }
                } else {
                    cartDataGroup.setIsAllSelect(true);
                    for (CartDataResponse.CartDataChild cartDataChild : cartDataGroup.data) {
                        cartDataChild.setIsSelect(true);
                    }
                }
                notifyDataSetChanged();
            }
        });

        return view;
    }

    class ShoppingBagViewHolder {
        public ImageView img_choose;
        public TextView tv_warehouseName;
        public CartLinearLayout product_child;
    }
}
