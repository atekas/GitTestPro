package com.sensu.android.zimaogou.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.sensu.android.zimaogou.IConstants;
import com.sensu.android.zimaogou.Mode.MyOrderGoodsMode;
import com.sensu.android.zimaogou.Mode.MyOrderMode;
import com.sensu.android.zimaogou.Mode.ProductMode;
import com.sensu.android.zimaogou.R;
import com.sensu.android.zimaogou.activity.mycenter.ApplySalesAfterActivity;
import com.sensu.android.zimaogou.utils.ImageUtils;
import com.sensu.android.zimaogou.utils.PromptUtils;

import java.util.ArrayList;

/**
 * Created by qi.yang on 2015/11/18.
 */
public class OrderDetailChildListAdapter extends SimpleBaseAdapter {
    ArrayList<MyOrderGoodsMode> mProducts = new ArrayList<MyOrderGoodsMode>();
    MyOrderMode myOrderMode;
    int state;

    public void flush(MyOrderMode myOrderMode, int state) {
        this.myOrderMode = myOrderMode;
        if (myOrderMode.getGoods() != null) {
            this.mProducts = myOrderMode.getGoods();
        }
        this.state = state;
        this.notifyDataSetChanged();
    }

    public OrderDetailChildListAdapter(Context context, MyOrderMode myOrderMode, int state) {
        super(context);
        this.myOrderMode = myOrderMode;
        if (myOrderMode.getGoods() != null) {
            this.mProducts = myOrderMode.getGoods();
            this.state = state;
        }
    }

    @Override
    public int getCount() {
        return mProducts.size();
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ProductsViewHolder productsViewHolder;
        if (view == null) {
            productsViewHolder = new ProductsViewHolder();
            view = LinearLayout.inflate(mContext, R.layout.order_detail_list_child_item, null);
            productsViewHolder.img_pro = (ImageView) view.findViewById(R.id.img_pro);
            productsViewHolder.tv_productName = (TextView) view.findViewById(R.id.tv_productName);
            productsViewHolder.tv_productNum = (TextView) view.findViewById(R.id.tv_productNum);
            productsViewHolder.tv_productPrice = (TextView) view.findViewById(R.id.tv_money);
            productsViewHolder.tv_refundMoney = (TextView) view.findViewById(R.id.tv_productPrice);
            productsViewHolder.tv_spc = (TextView) view.findViewById(R.id.tv_spc);
            view.setTag(productsViewHolder);
        } else {
            productsViewHolder = (ProductsViewHolder) view.getTag();
        }
        final int position = i;
        ImageUtils.displayImage(mProducts.get(i).getImage(), productsViewHolder.img_pro, ImageUtils.mItemTopOptions);
        productsViewHolder.tv_productName.setText(mProducts.get(i).getName());
        productsViewHolder.tv_productNum.setText("x" + mProducts.get(i).getNum());
        productsViewHolder.tv_productPrice.setText(mProducts.get(i).getPrice());
        productsViewHolder.tv_spc.setText(mProducts.get(i).getSpec());

        if (mProducts.get(position).getIs_returned().equals("0")) {
            if (state == IConstants.sReceived) {
                productsViewHolder.tv_refundMoney.setVisibility(View.VISIBLE);
                productsViewHolder.tv_refundMoney.setText("退货退款");
            } else if (state == IConstants.sUnreceived) {
                productsViewHolder.tv_refundMoney.setVisibility(View.VISIBLE);
                productsViewHolder.tv_refundMoney.setText("退款");
            } else {
                productsViewHolder.tv_refundMoney.setVisibility(View.GONE);
            }
        }else{
            productsViewHolder.tv_refundMoney.setVisibility(View.GONE);
        }
        productsViewHolder.tv_refundMoney.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //TODO 退款链接
                mContext.startActivity(new Intent(mContext, ApplySalesAfterActivity.class)
                        .putExtra("order", myOrderMode)
                        .putExtra("goods", mProducts.get(position)));
            }
        });
        return view;
    }

    public static class ProductsViewHolder {
        public ImageView img_pro;
        public TextView tv_productName, tv_productNum, tv_productPrice, tv_refundMoney, tv_spc;

    }
}
