package com.sensu.android.zimaogou.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.sensu.android.zimaogou.Mode.MyOrderGoodsMode;
import com.sensu.android.zimaogou.Mode.MyOrderMode;
import com.sensu.android.zimaogou.Mode.ProductMode;
import com.sensu.android.zimaogou.R;
import com.sensu.android.zimaogou.utils.ImageUtils;
import com.sensu.android.zimaogou.utils.PromptUtils;

import java.util.ArrayList;

/**
 * Created by qi.yang on 2015/11/18.
 */
public class OrderDetailChildListAdapter extends SimpleBaseAdapter {
    ArrayList<MyOrderGoodsMode> mProducts = new ArrayList<MyOrderGoodsMode>();
    MyOrderMode myOrderMode;
    public void flush(MyOrderMode myOrderMode){
        this.myOrderMode = myOrderMode;
        if(myOrderMode.getGoods() != null) {
            this.mProducts = myOrderMode.getGoods();
        }
        this.notifyDataSetChanged();
    }

    public OrderDetailChildListAdapter(Context context, MyOrderMode myOrderMode) {
        super(context);
        this.myOrderMode = myOrderMode;
        if(myOrderMode.getGoods() != null) {
            this.mProducts = myOrderMode.getGoods();
        }
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
            view = LinearLayout.inflate(mContext, R.layout.order_detail_list_child_item,null);
            productsViewHolder.img_pro = (ImageView) view.findViewById(R.id.img_pro);
            productsViewHolder.tv_productName = (TextView) view.findViewById(R.id.tv_productName);
            productsViewHolder.tv_productNum = (TextView) view.findViewById(R.id.tv_productNum);
            productsViewHolder.tv_productPrice = (TextView) view.findViewById(R.id.tv_money);
            productsViewHolder.tv_refundMoney = (TextView) view.findViewById(R.id.tv_productPrice);
            productsViewHolder.tv_spc = (TextView) view.findViewById(R.id.tv_spc);
            view.setTag(productsViewHolder);
        }else{
            productsViewHolder = (ProductsViewHolder) view.getTag();
        }
        ImageUtils.displayImage(mProducts.get(i).getImage(),productsViewHolder.img_pro,ImageUtils.mItemTopOptions);
        productsViewHolder.tv_productName.setText(mProducts.get(i).getName());
        productsViewHolder.tv_productNum.setText("x"+mProducts.get(i).getNum());
        productsViewHolder.tv_productPrice.setText(mProducts.get(i).getPrice());
        productsViewHolder.tv_spc.setText(mProducts.get(i).getSpec());
        productsViewHolder.tv_refundMoney.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //TODO 退款链接
            }
        });
        return view;
    }

    public static class ProductsViewHolder {
        public ImageView img_pro;
        public TextView tv_productName,tv_productNum,tv_productPrice,tv_refundMoney,tv_spc;

    }
}
