package com.sensu.android.zimaogou.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import com.sensu.android.zimaogou.Mode.ProductMode;
import com.sensu.android.zimaogou.R;

import java.util.ArrayList;

/**
 * Created by qi.yang on 2015/11/18.
 */
public class OrderChildListAdapter extends SimpleBaseAdapter {
    ArrayList<ProductMode> mProducts;
    int type = 1;
    public void flush(ArrayList<ProductMode> mProducts){
        this.mProducts = mProducts;
        this.notifyDataSetChanged();
    }

    public OrderChildListAdapter(Context context,ArrayList<ProductMode> mProducts,int type) {
        super(context);
        this.mProducts = mProducts;
        this.type = type;
    }

    @Override
    public int getCount() {
        return mProducts.size();
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ProductsViewHolder productsViewHolder;
        if(view == null){
            productsViewHolder = new ProductsViewHolder();
            view = LinearLayout.inflate(mContext, R.layout.order_list_child_item,null);
            productsViewHolder.img_choose = (ImageView) view.findViewById(R.id.img_choose);
            productsViewHolder.img_pro = (ImageView) view.findViewById(R.id.img_pro);
            productsViewHolder.rl_showType = (RelativeLayout) view.findViewById(R.id.rl_showType);
            productsViewHolder.tv_productName = (TextView) view.findViewById(R.id.tv_productName);
            productsViewHolder.tv_productNum = (TextView) view.findViewById(R.id.tv_productNum);
            productsViewHolder.tv_productPrice = (TextView) view.findViewById(R.id.tv_productPrice);
            productsViewHolder.ll_refund = (LinearLayout) view.findViewById(R.id.ll_refund);
            productsViewHolder.tv_refundMoney = (TextView) view.findViewById(R.id.tv_refundMoney);
            view.setTag(productsViewHolder);
        }else{
            productsViewHolder = (ProductsViewHolder) view.getTag();
        }
        productsViewHolder.img_pro.setImageResource(mProducts.get(i).getTestImg());
        productsViewHolder.tv_productPrice.setText("￥" + mProducts.get(i).getPrice());
        productsViewHolder.tv_refundMoney.setText("￥" + mProducts.get(i).getPrice());
        productsViewHolder.tv_productName.setText(mProducts.get(i).getTestTitle());
        productsViewHolder.tv_productNum.setText("x"+mProducts.get(i).getNum());
        if(type == 3){
            productsViewHolder.ll_refund.setVisibility(View.VISIBLE);
            productsViewHolder.tv_productPrice.setVisibility(View.GONE);
        }else{
            productsViewHolder.ll_refund.setVisibility(View.GONE);
            productsViewHolder.tv_productPrice.setVisibility(View.VISIBLE);
        }
        return view;
    }

    public static class ProductsViewHolder {
        public ImageView img_choose,img_pro;
        public TextView tv_productName,tv_productNum,tv_productPrice,tv_refundMoney;
        public RelativeLayout rl_showType;
        public LinearLayout ll_refund;
    }
}
